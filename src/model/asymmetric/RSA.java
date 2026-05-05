package model.asymmetric;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSA {
    private KeyPair keyPair;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private String padding = "RSA/ECB/PKCS1Padding";

    private static final String AES_ALGO = "AES/CTR/NoPadding";
    private static final int BUFFER_SIZE = 8192;

    public void setPadding(String padding) {
        this.padding = padding;
    }

    public String encryptBase64(String data) throws Exception {
        return Base64.getEncoder().encodeToString(encrypt(data));
    }

    public byte[] encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance(padding);
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(dataBytes);
    }

    public String decrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance(padding);
        byte[] dataBytes = Base64.getDecoder().decode(data);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] out = cipher.doFinal(dataBytes);
        return new String(out, StandardCharsets.UTF_8);
    }

    public void genKeyPair(int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);
        keyPair = keyPairGenerator.generateKeyPair();
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
    }

    public String getPublicKeyBase64() {
        return (publicKey != null) ? Base64.getEncoder().encodeToString(publicKey.getEncoded()) : "";
    }

    public String getPrivateKeyBase64() {
        return (privateKey != null) ? Base64.getEncoder().encodeToString(privateKey.getEncoded()) : "";
    }

    public void setPublicKeyFromBase64(String base64Key) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        this.publicKey = keyFactory.generatePublic(spec);
    }

    public void setPrivateKeyFromBase64(String base64Key) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        this.privateKey = keyFactory.generatePrivate(spec);
    }

    public void encryptFile(String inputFile, String outputFile) throws Exception {
        if (publicKey == null) throw new IllegalStateException("Chưa tải Public Key!");

        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256, new SecureRandom());
        SecretKey aesKey = keyGen.generateKey();

        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher rsaCipher = Cipher.getInstance(padding);
        rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedAesKey = rsaCipher.doFinal(aesKey.getEncoded());

        Cipher aesCipher = Cipher.getInstance(AES_ALGO);
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKey, ivSpec);
        try (FileInputStream fis = new FileInputStream(inputFile);
             DataOutputStream dos = new DataOutputStream(new FileOutputStream(outputFile))) {
            dos.writeShort(encryptedAesKey.length);
            dos.write(encryptedAesKey);
            dos.write(iv);
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                byte[] output = aesCipher.update(buffer, 0, bytesRead);
                if (output != null) dos.write(output);
            }
            byte[] finalBytes = aesCipher.doFinal();
            if (finalBytes != null) dos.write(finalBytes);
        }
    }

    public void decryptFile(String inputFile, String outputFile) throws Exception {
        if (privateKey == null) throw new IllegalStateException("Chưa tải Private Key!");
        try (DataInputStream dis = new DataInputStream(new FileInputStream(inputFile));
             FileOutputStream fos = new FileOutputStream(outputFile)) {
            short keyLength = dis.readShort();
            byte[] encryptedAesKey = new byte[keyLength];
            dis.readFully(encryptedAesKey);
            byte[] iv = new byte[16];
            dis.readFully(iv);

            Cipher rsaCipher = Cipher.getInstance(padding);
            rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedAesKeyBytes = rsaCipher.doFinal(encryptedAesKey);
            SecretKey originalAesKey = new SecretKeySpec(decryptedAesKeyBytes, "AES");

            Cipher aesCipher = Cipher.getInstance(AES_ALGO);
            aesCipher.init(Cipher.DECRYPT_MODE, originalAesKey, new IvParameterSpec(iv));

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = dis.read(buffer)) != -1) {
                byte[] output = aesCipher.update(buffer, 0, bytesRead);
                if (output != null) fos.write(output);
            }
            byte[] finalBytes = aesCipher.doFinal();
            if (finalBytes != null) fos.write(finalBytes);
        }
    }
}