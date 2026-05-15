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
        int total = getFullAlphabet(isVN).length();
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
        int[][] matrixKey = keyToMatrix(key);
        if(invertMatrix2x2(matrixKey, getFullAlphabet(isVN).length()) == null){
            throw new IllegalArgumentException("Khóa không hợp lệ, không tồn tại ma trận nghịch đảo.");
        }
        return transformText(text, matrixKey, getShift(matrixKey, getFullAlphabet(isVN).length()), isVN);

    }

    @Override
    public String decrypt(String text, String key, boolean isVN) {
        if(text == null || text.isEmpty())
            return "";
        int[][] matrixKey = keyToMatrix(key);
        int[][] invMatrix = invertMatrix2x2(matrixKey,getFullAlphabet(isVN).length());
        if(invMatrix == null){
            throw new IllegalArgumentException("Khóa không hợp lệ, không tồn tại ma trận nghịch đảo.");
        }
        return transformText(text, invMatrix, -getShift(matrixKey, getFullAlphabet(isVN).length()), isVN);
    }

    private String transformText(String text, int[][] matrix, int singleCharShift, boolean isVN) {
        String alphabet = getFullAlphabet(isVN);
        StringBuilder chars = new StringBuilder();
        for (char c : text.toCharArray()) {
            if(alphabet.indexOf(c)!=-1){
                chars.append(c);
            }
        }
        String transformedChars = transformChars(chars.toString(), alphabet, matrix, singleCharShift);

        StringBuilder result = new StringBuilder();
        int index = 0;
        for (char c: text.toCharArray()) {
            if(alphabet.indexOf(c)!=-1){
                result.append(transformedChars.charAt(index++));
            }
            else {
                result.append(c);
            }
        }
        return result.toString();
    }

    private String transformChars(String text, String alphabet, int[][] matrix, int singleCharShift) {
        if(text.isEmpty()){
            return "";
        }
        int m = matrix.length;
        int total = alphabet.length();
        StringBuilder chars = new StringBuilder(text);
        StringBuilder result = new StringBuilder();
        int fullBlockLength = chars.length() - (chars.length() % m);
        for(int i = 0; i < fullBlockLength; i+= m){
            int[] vector = new int[m];
            for(int j = 0; j < m; j++){
                vector[j] = alphabet.indexOf(chars.charAt(i+j));
            }
            int[] vector2 =mutiplyMatrix(matrix,vector,total);
            for(int j = 0; j < m; j++){
                result.append(alphabet.charAt(vector2[j]));
            }
        }
        for(int i = fullBlockLength; i < chars.length(); i++){
            int pos = alphabet.indexOf(chars.charAt(i));
            int newPos = pos + singleCharShift;
            result.append(alphabet.charAt((newPos % total + total) % total));
        }
        return result.toString();
    }

    private int getShift(int[][] matrixKey, int total){
        int shift = 0;
        for (int[] row : matrixKey) {
            for (int value : row) {
                shift += value;
            }
        }
        shift = shift % total;
        return shift == 0 ? 1 : shift;
    }

    private int[][] keyToMatrix(String key){
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Khóa Hill phải gồm 4 số.");
        }
        String[] parts = key.trim().split("\\s+");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Khóa Hill phải gồm 4 số.");
        }
        int[][] matrix = new int[2][2];
        int k =0;
        for(int i = 0; i<2; i++){
            for(int j = 0; j<2; j++){
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
