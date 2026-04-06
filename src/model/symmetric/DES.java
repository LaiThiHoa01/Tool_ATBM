package model.symmetric;

import javax.crypto.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class DES {
    SecretKey key;

    public SecretKey getKey(int keySize) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
        keyGenerator.init(keySize);
        key = keyGenerator.generateKey();
        return key;
    }
    public void loadKey(SecretKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        this.key = key;
    }
    public byte[] encrypt(String text) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, this.key);
        byte[] data = text.getBytes(StandardCharsets.UTF_8);
        return cipher.doFinal(data);
    }
//    private byte[] expand(byte[] data, byte[] expand, int limit){
//
//    }
    public String encryptBase64(String text) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        return Base64.getEncoder().encodeToString(encrypt(text));
    }
    public String decrypt(byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, this.key);
        byte[] bytes = cipher.doFinal(data);
        return new String(bytes, StandardCharsets.UTF_8);
    }



}
