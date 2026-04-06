package model.classical;

import java.util.Random;

public class Caesar extends ACipher{
	@Override
	public String encrypt(String text, String key, boolean isVN) {
		String lowerAlpha = getLowerAlphabet(isVN);
		int keyy = Integer.parseInt(key);
		StringBuilder result = new StringBuilder();
		int total = lowerAlpha.length();
		keyy = (keyy % total + total) % total;
		for (char c : text.toCharArray()) {
			boolean isUppper = Character.isUpperCase(c);
			char lower = Character.toLowerCase(c);
			int pos = lowerAlpha.indexOf(lower);
			if(pos!=-1) {
				char cipher = lowerAlpha.charAt((keyy+ pos)%total);
				result.append(isUppper? Character.toUpperCase(cipher):cipher );
			}
			else 
				result.append(c);
			
		}
		return result.toString();
	
	}
	@Override
	public String decrypt(String text, String key, boolean isVN) {
		int keyy = Integer.parseInt(key);
		int total = getLowerAlphabet(isVN).length();
		return encrypt(text, String.valueOf(total-(keyy%total)), isVN);

	}
	@Override
	public String genKey(boolean isVN) {
		int total = getLowerAlphabet(isVN).length();
		int randomkey = new Random().nextInt(total-1)+1;
		return String.valueOf(randomkey);
	}

	

}
