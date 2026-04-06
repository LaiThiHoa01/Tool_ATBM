package model.classical;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Substitution extends ACipher{

	@Override
	public String genKey(boolean isVN) {
		String alphabet = getLowerAlphabet(isVN);
		List<Character> chars = new ArrayList<Character>();
		for (char c : alphabet.toCharArray()) {
			chars.add(c);
			
		}
		Collections.shuffle(chars);
		StringBuilder keyy = new StringBuilder();
		for (char c : chars) {
			keyy.append(c);
		}
		return keyy.toString();
	}

	@Override
	public String encrypt(String text, String key, boolean isVN)  {
		// TODO Auto-generated method stub
		String lowerAlpha = getLowerAlphabet(isVN);
		StringBuilder result = new StringBuilder();
		if(key == null|| key.length()!= lowerAlpha.length())
			throw new IllegalArgumentException("Khoá không hợp lệ");
		for (char c : text.toCharArray()) {
			boolean isUpper = Character.isUpperCase(c);
			char lowerC = Character.toLowerCase(c);
			int pos = lowerAlpha.indexOf(lowerC);
			if(pos !=-1) {
				char cipher = key.charAt(pos);
				result.append(isUpper? Character.toUpperCase(cipher):cipher);
			}
			else 
				result.append(c);
		}
		return result.toString();
	}

	@Override
	public String decrypt(String text, String key, boolean isVN) {
		// TODO Auto-generated method stub
		String lowerAlpha = getLowerAlphabet(isVN);
		StringBuilder result  = new StringBuilder();
		for (char c : text.toCharArray()) {
			boolean isUpper = Character.isUpperCase(c);
			char lower = Character.toLowerCase(c);
			int pos = key.indexOf(lower);
			if(pos!=-1) {
				char planText = lowerAlpha.charAt(pos);
				result.append(isUpper? Character.toUpperCase(c):planText);
			}
			else 
				result.append(c);
			
		}
		return result.toString();
	}
	
	

}
