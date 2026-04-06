package model.classical;

import java.util.Random;

public class Vigenere extends ACipher{

	@Override
	public String genKey(boolean isVN) {
        String abc = getLowerAlphabet(isVN);
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
        String abc = getLowerAlphabet(isVN);
        int total = abc.length();
        StringBuilder result = new StringBuilder();
        int[] keys = new int[key.length()];
        for (int i = 0; i < keys.length; i++) {
            keys[i] = abc.indexOf(Character.toLowerCase(key.charAt(i)));
        }
        int keyIndex =0;
        int keyLength = key.length();
        for( char c : text.toCharArray()){
            boolean isUpper= Character.isUpperCase(c);
            char lower = Character.toLowerCase(c);
            int pos = abc.indexOf(lower);
            if(pos != -1){
                int shift = keys[keyIndex%keyLength];
                int newPos = (pos+shift)%total;
                char enChar = abc.charAt(newPos);
                result.append(isUpper?Character.toUpperCase(enChar):enChar);
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
        String abc = getLowerAlphabet(isVN);
        int total = abc.length();
        StringBuilder result = new StringBuilder();
        int[] keys = new int[key.length()];
        for (int i = 0; i < keys.length; i++) {
            keys[i] = abc.indexOf(Character.toLowerCase(key.charAt(i)));
        }
        int keyIndex =0;
        int keyLength = key.length();
        for( char c : text.toCharArray()){
            boolean isUpper= Character.isUpperCase(c);
            char lower = Character.toLowerCase(c);
            int pos = abc.indexOf(lower);
            if(pos != -1){
                int shift = keys[keyIndex%keyLength];
                int newPos = (pos-shift+total)%total;
                char enChar = abc.charAt(newPos);
                result.append(isUpper?Character.toUpperCase(enChar):enChar);
                keyIndex++;
            }else result.append(c);
        }

        return result.toString();
	}

}
