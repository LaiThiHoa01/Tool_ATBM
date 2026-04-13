package model.symmetric;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class DES {
    SecretKey key;
    IvParameterSpec iv;

    public SecretKey getKey(int keySize) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
        keyGenerator.init(keySize);
        key = keyGenerator.generateKey();
        return key;
    }
    public void loadKey(SecretKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        this.key = key;
    }
    public IvParameterSpec genRandomIV() throws NoSuchPaddingException, InvalidAlgorithmParameterException {
        byte[] ivBytes = new byte[8];
        new SecureRandom().nextBytes(ivBytes);
        this.iv = new IvParameterSpec(ivBytes);
        return iv;

    }
    public byte[] encrypt(String text) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance("DES/CTR/NoPadding");
        if(iv == null)
            return genRandomIV().getIV();
        cipher.init(Cipher.ENCRYPT_MODE, this.key,iv);
        byte[] data = text.getBytes(StandardCharsets.UTF_8);
        return cipher.doFinal(data);
    }
    private byte[] expand(byte[] data, byte[] expand, int limit){
        if(data == null){
            byte[] out = new byte[limit];
            System.arraycopy(expand,0,out,0,limit);
            return out;
        }
        byte[] out = new byte[data.length+ limit];
        System.arraycopy(data,0,out,0,data.length);
        System.arraycopy(expand,0,out,data.length,limit);
        return out;

    }
    public String encryptBase64(String text) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        return Base64.getEncoder().encodeToString(encrypt(text));
    }
    public String decrypt(byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance("DES/CTR/NoPadding");

        cipher.init(Cipher.DECRYPT_MODE, this.key,iv);
        byte[] bytes = cipher.doFinal(data);
        return new String(bytes, StandardCharsets.UTF_8);
    }
    public String decryptBase64(String text) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        return decrypt(Base64.getDecoder().decode(text));
    }
    public boolean encryptFile(String src, String dest) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, this.key);
        try (FileInputStream fis = new FileInputStream(src);
             FileOutputStream fos = new FileOutputStream(dest);
             CipherOutputStream  cipherOutputStream = new CipherOutputStream(fos, cipher)
        )
        {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                cipherOutputStream.write(buffer, 0, bytesRead);
            }
            return true;

        }
    }
    public boolean decryptFile(String src, String dest) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, this.key);
        BufferedInputStream input = new BufferedInputStream(new FileInputStream(src));
        BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(dest));
        CipherOutputStream cipherOutput = new CipherOutputStream(output, cipher);
        int i;
        byte[] read =  new byte[1024];
        byte[] re = null;
        while((i = input.read(read)) !=-1){
            cipherOutput.write(read,0,i);
        }
        read = cipher.doFinal();
        if(read!=null){
            cipherOutput.write(read);

        }
        input.close();
        output.flush();
        output.close();
        return true;
    }



}
