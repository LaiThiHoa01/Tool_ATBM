package model.classical;

import java.util.Random;

public class Affine extends ACipher{
	
	private int gcd(int a, int b) {
		if(b==0)
			return a;
		return gcd(b, a%b);
	}
	private int modInverse(int a, int m) {
		a= a%m;
		for (int i = 0; i < m; i++) {
			if((a*i)%m==1)
				return i;
		}
		return 1;
				
	}
	@Override
	public String genKey(boolean isVN) {
		// TODO Auto-generated method stub
		int m = getLowerAlphabet(isVN).length();
		Random random = new Random();
		int a;
		do {
			 a = random.nextInt(m);
		}
		while(a==0|| gcd(a, m)!=1);
		int b= random.nextInt(m);
		return a+" "+b;
	}
	private int[] parseKey(String key) {
		String[] splitKey = key.split(" ");
		if(splitKey.length!=2)
			throw new IllegalArgumentException("Khoá không hợp lệ");
		return new int[] {
				Integer.parseInt(splitKey[0].trim()),
				Integer.parseInt(splitKey[1].trim())
		};
	}

	@Override
	public String encrypt(String text, String key, boolean isVN) {
		// TODO Auto-generated method stub 
	
		String lower = getLowerAlphabet(isVN);
		int m = getLowerAlphabet(isVN).length();
		int[] keyy = parseKey(key);
		int a = keyy[0];
		int b = keyy[1];
		if(gcd(a, m)!=1)
			throw new IllegalArgumentException("Số a và độ dài bảng phải có UCLN là 1");
		StringBuilder result = new StringBuilder();
		for (char c : text.toCharArray()) {
			boolean isUpper = Character.isUpperCase(c);
			char lowerText = Character.toLowerCase(c);
			int pos = lower.indexOf(lowerText);
			if(pos!=-1) {
				int cipherPos = (a*pos+b)%m;
				cipherPos = (cipherPos%m +m)%m;
				char cipher =lower.charAt(cipherPos);
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
		String lower = getLowerAlphabet(isVN);
		int m = getLowerAlphabet(isVN).length();
		int[] keyy = parseKey(key);
		int a = keyy[0];
		int b = keyy[1];
		if(gcd(a, m)!=1)
			throw new IllegalArgumentException("Số a và độ dài bảng phải có UCLN là 1");
		StringBuilder result = new StringBuilder();
		for (char c : text.toCharArray()) {
			boolean isUpper = Character.isUpperCase(c);
			char lowerText = Character.toLowerCase(c);
			int pos = lower.indexOf(lowerText);
			if(pos!=-1) {
				 int plainPos = (modInverse(a, m)*(pos-b))%m;
				 plainPos = (plainPos % m + m) % m;
				char plain =lower.charAt(plainPos);
				result.append(isUpper? Character.toUpperCase(plain):plain);
			}
			else 
				result.append(c);
		}
		return result.toString();
	}
	

}
