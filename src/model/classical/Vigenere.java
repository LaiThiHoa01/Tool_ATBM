package model.classical;

import java.util.Random;

public class Vigenere extends ACipher{

    private int keyShift(char keyChar, boolean isVN) {
        int shift = getFullAlphabet(isVN).indexOf(keyChar);
        if (shift == -1) {
            throw new IllegalArgumentException("Khoá chứa ký tự không thuộc bảng chữ cái đã chọn");
        }
        return shift;
    }

    @Override
    public String genKey(boolean isVN) {
        String abc = getFullAlphabet(isVN);
        StringBuilder key = new StringBuilder();
        int total  = abc.length();
        Random rd = new Random();
        int lenghtKey = rd.nextInt(5)+4;
        for(int j=0;j< lenghtKey;j++){
            int randomKey = rd.nextInt(total);
            key.append(abc.charAt(randomKey));
        }
        return key.toString();
    }

    @Override
    public String encrypt(String text, String key, boolean isVN) {
        if (text == null || text.isEmpty() || key == null || key.isEmpty()) {
            return "";
        }
        String abc = getFullAlphabet(isVN);
        int total = abc.length();
        StringBuilder result = new StringBuilder();
        int[] keys = new int[key.length()];
        for (int i = 0; i < keys.length; i++) {
            keys[i] = keyShift(key.charAt(i), isVN);
        }
        int keyIndex =0;
        int keyLength = key.length();
        for( char c : text.toCharArray()){
            int pos = abc.indexOf(c);
            if(pos != -1){
                int shift = keys[keyIndex%keyLength];
                int newPos = (pos+shift)%total;
                char enChar = abc.charAt(newPos);
                result.append(enChar);
                keyIndex++;
            }else result.append(c);
        }

        return result.toString();
    }

    @Override
    public String decrypt(String text, String key, boolean isVN) {
        if (text == null || text.isEmpty() || key == null || key.isEmpty()) {
            return "";
        }
        String abc = getFullAlphabet(isVN);
        int total = abc.length();
        StringBuilder result = new StringBuilder();
        int[] keys = new int[key.length()];
        for (int i = 0; i < keys.length; i++) {
            keys[i] = keyShift(key.charAt(i), isVN);
        }
        int keyIndex =0;
        int keyLength = key.length();
        for( char c : text.toCharArray()){
            int pos = abc.indexOf(c);
            if(pos != -1){
                int shift = keys[keyIndex%keyLength];
                int newPos = (pos-shift+total)%total;
                char enChar = abc.charAt(newPos);
                result.append(enChar);
                keyIndex++;
            }else result.append(c);
        }

        return result.toString();
    }

}
