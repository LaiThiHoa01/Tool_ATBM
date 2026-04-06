package model.classical;

import java.util.Random;

public class Hill extends ACipher{

    private int gcd(int a, int b){
        if(b==0){
            return a;
        }
        return gcd(b,a%b);
    }

    @Override
    public String genKey(boolean isVN) {
        int total = getLowerAlphabet(isVN).length();
        Random random = new Random();
        while(true){
            int a  = random.nextInt(total);
            int b = random.nextInt(total);
            int c  = random.nextInt(total);
            int d  = random.nextInt(total);
            int det =((a*d-c*b)%total+total)%total;
            if(det!=0 && gcd(det,total)==1){
                return a + " " + b + " " + c + " " + d;
            }
        }
    }

    @Override
    public String encrypt(String text, String key, boolean isVN) {
        if(text == null || text.isEmpty())
            return "";
        String abc = getLowerAlphabet(isVN);
        int total = getLowerAlphabet(isVN).length();
        int[][] matrixKey = keyToMatrix(key);
        int m = matrixKey.length;
        StringBuilder alphabeticChars = new StringBuilder();
        for (char c : text.toCharArray()) {
            if(abc.indexOf(Character.toLowerCase(c))!=-1){
                alphabeticChars.append(c);
            }
        }
        while(alphabeticChars.length()%m != 0){
            alphabeticChars.append(abc.charAt(0));
        }
        StringBuilder encryptedChars = new StringBuilder();
        for(int i = 0; i < alphabeticChars.length(); i+= m){
            int[] vector = new int[m];
            boolean[] isUpper = new boolean[m];
            for(int j = 0; j < m; j++){
                char c = alphabeticChars.charAt(i+j);
                isUpper[j] = Character.isUpperCase(c);
                vector[j] = abc.indexOf(Character.toLowerCase(c));
            }
            int[] vector2 =mutiplyMatrix(matrixKey,vector,total);
            for(int j = 0; j < m; j++){
                char newChar = abc.charAt(vector2[j]);
                if(isUpper[j]){
                    encryptedChars.append(Character.toUpperCase(newChar));
                }
                else{
                    encryptedChars.append(newChar);
                }
            }
        }
        StringBuilder result = new StringBuilder();
        int index = 0;
        for (char c: text.toCharArray()) {
            if(abc.indexOf(Character.toLowerCase(c))!=-1){
                result.append(encryptedChars.charAt(index));
                index++;
            }
            else {
                result.append(c);
            }

        }
        return result.toString();

    }

    @Override
    public String decrypt(String text, String key, boolean isVN) {
        if(text == null || text.isEmpty())
            return "";
        String abc = getLowerAlphabet(isVN);
        int total = getLowerAlphabet(isVN).length();
        int[][] matrixKey = keyToMatrix(key);
        int[][] invMatrix = invertMatrix2x2(matrixKey,total);
        if(invMatrix == null){
            throw new IllegalArgumentException("Khóa không hợp lệ, không tồn tại ma trận nghịch đảo.");
        }
        int m = matrixKey.length;
        StringBuilder alphabeticChars = new StringBuilder();
        for (char c : text.toCharArray()) {
            if(abc.indexOf(Character.toLowerCase(c))!=-1){
                alphabeticChars.append(c);
            }
        }
        while(alphabeticChars.length()%m != 0){
            alphabeticChars.append(abc.charAt(0));
        }
        StringBuilder decryptedChars = new StringBuilder();
        for(int i = 0; i < alphabeticChars.length(); i+= m){
            int[] vector = new int[m];
            boolean[] isUpper = new boolean[m];
            for(int j = 0; j < m; j++){
                char c = alphabeticChars.charAt(i+j);
                isUpper[j] = Character.isUpperCase(c);
                vector[j] = abc.indexOf(Character.toLowerCase(c));
            }
            int[] vector2 =mutiplyMatrix(invMatrix,vector,total);
            for(int j = 0; j < m; j++){
                char newChar = abc.charAt(vector2[j]);
                if(isUpper[j]){
                    decryptedChars.append(Character.toUpperCase(newChar));
                }
                else{
                    decryptedChars.append(newChar);
                }
            }
        }
        StringBuilder result = new StringBuilder();
        int index = 0;
        for (char c: text.toCharArray()) {
            if(abc.indexOf(Character.toLowerCase(c))!=-1){
                result.append(decryptedChars.charAt(index));
                index++;
            }
            else {
                result.append(c);
            }

        }
        return result.toString();
    }
    private int[][] keyToMatrix(String key){
        String[] parts = key.split(" ");
        int m = (int) Math.sqrt(parts.length);
        int[][] matrix = new int[m][m];
        int k =0;
        for(int i = 0; i<m; i++){
            for(int j = 0; j<m; j++){
                matrix[i][j] = Integer.parseInt(parts[k++].trim());
            }
        }
        return matrix;
    }
    private int[][] invertMatrix2x2(int[][] matrixKey, int n){
        int det = matrixKey[0][0]*matrixKey[1][1]- matrixKey[0][1]*matrixKey[1][0];
        det = ((det % n) + n) % n;
        int invDet = -1;
        for(int i = 0; i<n; i++){
            if((det*i)%n==1){
                invDet = i;
                break;
            }

        }
        if(invDet==-1){
            return null;
        }
        int [][] invMatrix = new int[2][2];
        invMatrix[0][0] = (matrixKey[1][1]*invDet)%n;
        invMatrix[1][0] = ((-matrixKey[1][0]*invDet)%n+n)%n;
        invMatrix[0][1] = ((-matrixKey[0][1]*invDet)%n+n)%n;
        invMatrix[1][1] = (matrixKey[0][0]*invDet)%n;

        return invMatrix;

    }
    private int[] mutiplyMatrix(int[][] matrixKey, int[] vector, int n){
        int m = matrixKey.length;
        int[] result = new int[m];
        for(int i = 0; i<m; i++){
            int sum = 0;
            for(int j = 0; j<m; j++){
                sum += matrixKey[i][j]*vector[j];
            }
            result[i] = (sum%n+n)%n;
        }
        return result;
    }

}
