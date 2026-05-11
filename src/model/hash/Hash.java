package model.hash;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.DigestException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {
    public String checkSum(String input, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
        BigInteger bi = new BigInteger(1, digest);
        String hashtext = bi.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }
    public String hashFile(String path, String algorithm) throws NoSuchAlgorithmException, FileNotFoundException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        InputStream is = new BufferedInputStream(new FileInputStream(path));
        DigestInputStream dis = new DigestInputStream(is, md);
        byte[] buffer = new byte[1024];
        int read;
        do{
            try {
                read = dis.read(buffer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } while(read != -1);
        BigInteger bi = new BigInteger(1, dis.getMessageDigest().digest());
        return bi.toString(16);
    }

}
