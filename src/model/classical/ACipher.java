package model.classical;

public abstract class ACipher {
    protected final String VN_LOWER = "aàáảãạăằắẳẵặâầấẩẫậbcdđeèéẻẽẹêềếểễệghiìíỉĩịklmnoòóỏõọôồốổỗộơờớởỡợpqrstuùúủũụưừứửữựvxyỳýỷỹỵ";
    protected final String ENG_LOWER = "abcdefghijklmnopqrstuvwxyz";
    protected final String DIGITS = "0123456789";

    protected String getLowerAlphabet(boolean isVN) {
        return isVN? VN_LOWER: ENG_LOWER;
    }

    protected String getLowerLetterAlphabet(boolean isVN) {
        String lowerAlphabet = getLowerAlphabet(isVN);
        StringBuilder letterAlphabet = new StringBuilder();
        for (char c : lowerAlphabet.toCharArray()) {
            if (Character.isLetter(c)) {
                letterAlphabet.append(c);
            }
        }
        return letterAlphabet.toString();
    }

    protected String getUpperLetterAlphabet(boolean isVN) {
        String lowerAlphabet = getLowerLetterAlphabet(isVN);
        StringBuilder upperAlphabet = new StringBuilder();
        for (char c : lowerAlphabet.toCharArray()) {
            upperAlphabet.append(Character.toUpperCase(c));
        }
        return upperAlphabet.toString();
    }

    protected String getFullAlphabet(boolean isVN) {
        return getLowerLetterAlphabet(isVN) + getUpperLetterAlphabet(isVN) + DIGITS;
    }

    public abstract String genKey(boolean isVN) ;
    public abstract String encrypt(String text, String key, boolean isVN) ;
    public abstract String decrypt(String text, String key, boolean isVN) ;



}
