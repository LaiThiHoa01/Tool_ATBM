package model.classical;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Substitution extends ACipher{

    private void validateKey(String key, String alphabet) {
        if (key == null || key.length() != alphabet.length()) {
            throw new IllegalArgumentException("Khoá không hợp lệ");
        }
        for (char c : alphabet.toCharArray()) {
            if (key.indexOf(c) == -1) {
                throw new IllegalArgumentException("Khoá không hợp lệ");
            }
        }
    }

    private String shuffle(String alphabet) {
        List<Character> chars = new ArrayList<Character>();
        for (char c : alphabet.toCharArray()) {
            chars.add(c);
        }
        Collections.shuffle(chars);
        StringBuilder key = new StringBuilder();
        for (char c : chars) {
            key.append(c);
        }
        return key.toString();
    }

    @Override
    public String genKey(boolean isVN) {
        String alphabet = getFullAlphabet(isVN);
        return shuffle(alphabet);
    }

    @Override
    public String encrypt(String text, String key, boolean isVN)  {
        String alphabet = getFullAlphabet(isVN);
        StringBuilder result = new StringBuilder();
        validateKey(key, alphabet);
        for (char c : text.toCharArray()) {
            int pos = alphabet.indexOf(c);
            if(pos !=-1) {
                char cipher = key.charAt(pos);
                result.append(cipher);
            }
            else
                result.append(c);
        }
        return result.toString();
    }

    @Override
    public String decrypt(String text, String key, boolean isVN) {
        String alphabet = getFullAlphabet(isVN);
        validateKey(key, alphabet);
        StringBuilder result  = new StringBuilder();
        for (char c : text.toCharArray()) {
            int pos = key.indexOf(c);
            if(pos!=-1) {
                char planText = alphabet.charAt(pos);
                result.append(planText);
            }
            else
                result.append(c);

        }
        return result.toString();
    }

}
