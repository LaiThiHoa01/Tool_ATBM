package model.classical;

import java.util.Random;

public class Caesar extends ACipher{
    @Override
    public String encrypt(String text, String key, boolean isVN) {
        String alpha = getFullAlphabet(isVN);
        int keyy = Integer.parseInt(key);
        StringBuilder result = new StringBuilder();
        int total = alpha.length();
        int shift = (keyy % total + total) % total;
        for (char c : text.toCharArray()) {
            int pos = alpha.indexOf(c);
            if(pos!=-1) {
                char cipher = alpha.charAt((shift+ pos)%total);
                result.append(cipher);
            }
            else
                result.append(c);

        }
        return result.toString();

    }
    @Override
    public String decrypt(String text, String key, boolean isVN) {
        int keyy = Integer.parseInt(key);
        return encrypt(text, String.valueOf(-keyy), isVN);

    }
    @Override
    public String genKey(boolean isVN) {
        int total = getFullAlphabet(isVN).length();
        int randomkey = new Random().nextInt(total-1)+1;
        return String.valueOf(randomkey);
    }



}
