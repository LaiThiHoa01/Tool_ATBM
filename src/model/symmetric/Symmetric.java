package model.symmetric;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class Symmetric {
    private SecretKey key;
    private String algorithm;
    private String mode;
    private String padding;

    public Symmetric( String algorithm, String mode, String padding) {
        this.algorithm = algorithm;
        this.mode = mode;
        this.padding = padding;
    }
    public void updateConfig(String algorithm, String mode, String padding) {
        this.algorithm = algorithm;
        this.mode = mode;
        this.padding = padding;
    }

    private String getTransformation() {
        return algorithm + "/" + mode + "/" + padding;
    }
    public void genKey(int keySize) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(this.algorithm,"BC");
        keyGen.init(keySize);
        this.key = keyGen.generateKey();
    }
    public void importKeyFromBase64(String base64Key) throws Exception {
        byte[] decodekey = Base64.getDecoder().decode(base64Key);
        this.key = new SecretKeySpec(decodekey, 0, decodekey.length, this.algorithm);
    }
    public String exportKeyToBase64() {
        return (key == null) ? "" : Base64.getEncoder().encodeToString(key.getEncoded());
    }
    public String encryptText(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance(getTransformation(),"BC");
        if(mode.equals("ECB")){
            cipher.init(Cipher.ENCRYPT_MODE, this.key);
            byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(cipherText);
        }
        else{
            byte[] iv = new byte[cipher.getBlockSize()];
            new SecureRandom().nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, this.key, ivSpec);
            byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            byte[] combined = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(cipherText, 0, combined, iv.length, cipherText.length);
            return Base64.getEncoder().encodeToString(combined);

        }
    }
    public String decryptText(String cipherText) throws Exception {
        byte[] combined = Base64.getDecoder().decode(cipherText);
        Cipher cipher = Cipher.getInstance(getTransformation(),"BC");
        if(mode.equals("ECB")){
            cipher.init(Cipher.DECRYPT_MODE, this.key);
            return new String(cipher.doFinal(combined), StandardCharsets.UTF_8);
        }
        else{
            int ivSize = cipher.getBlockSize();
            byte[] iv = new byte[ivSize];
            byte[] cipherBytes = new byte[combined.length - ivSize];
            System.arraycopy(combined, 0, iv, 0, ivSize);
            System.arraycopy(combined, ivSize, cipherBytes, 0, cipherBytes.length);
            cipher.init(Cipher.DECRYPT_MODE, this.key, new IvParameterSpec(iv));
            return new String(cipher.doFinal(cipherBytes), StandardCharsets.UTF_8);

        }
    }
    public void encryptFile(String filePath, String  destFile) throws Exception {
        Cipher cipher = Cipher.getInstance(getTransformation(),"BC");
        try
            (FileInputStream fis = new FileInputStream(filePath);
            FileOutputStream  fos = new FileOutputStream(destFile)){
                if(!mode.equals("ECB")) {
                    byte[] iv =  new byte[cipher.getBlockSize()];
                    new SecureRandom().nextBytes(iv);
                    fos.write(iv);
                    IvParameterSpec ivSpec = new IvParameterSpec(iv);
                    cipher.init(Cipher.ENCRYPT_MODE, this.key, ivSpec);
                }
                else {
                    cipher.init(Cipher.ENCRYPT_MODE, this.key);
                }
                try(CipherOutputStream cos = new CipherOutputStream(fos, cipher)){
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while((bytesRead = fis.read(buffer)) != -1) {
                        cos.write(buffer, 0, bytesRead);
                    }
                }

        }


    }
    public void decryptFile(String filePath, String  destFile) throws Exception {

        Cipher cipher = Cipher.getInstance(getTransformation(),"BC");
        try(FileInputStream fis = new FileInputStream(filePath)){
            if(!mode.equals("ECB")) {
                byte[] iv =  new byte[cipher.getBlockSize()];
                fis.read(iv);
                IvParameterSpec ivSpec = new IvParameterSpec(iv);
                cipher.init(Cipher.DECRYPT_MODE, this.key, ivSpec);
            }
            else {
                cipher.init(Cipher.DECRYPT_MODE, this.key);
            }
            try(CipherInputStream cis = new CipherInputStream(fis, cipher);
                FileOutputStream fos = new FileOutputStream(destFile)
            ){
                byte[] buffer = new byte[1024];
                int bytesRead;
                while((bytesRead = cis.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }
        }
    }

}
