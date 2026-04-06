package model.classical;

public abstract class ACipher {
	protected final String VN_LOWER = "aàáảãạăằắẳẵặâầấẩẫậbcdđeèéẻẽẹêềếểễệghiìíỉĩịklmnoòóỏõọôồốổỗộơờớởỡợpqrstuùúủũụưừứửữựvxyỳýỷỹỵ0123456789";
    protected final String ENG_LOWER = "abcdefghijklmnopqrstuvwxyz0123456789";
    
    protected String getLowerAlphabet(boolean isVN) {
    	return isVN? VN_LOWER: ENG_LOWER;
    }
    
    public abstract String genKey(boolean isVN) ;
    public abstract String encrypt(String text, String key, boolean isVN) ;
    public abstract String decrypt(String text, String key, boolean isVN) ;
   
    

}
