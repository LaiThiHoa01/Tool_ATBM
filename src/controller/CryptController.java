package controller;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import View.View;
import model.ACipher;
import model.Affine;
import model.Caesar;
import model.Substitution;

public class CryptController {
	View view;
	Caesar caesar;
	
	public CryptController(View view) {
		this.view = view;
		this.caesar= new Caesar();
		
		this.view.addbtnEncrypt(new Encrypt());
		this.view.addGenKey(new GenKey());
		this.view.addbtnDecrypt(new Decrypt());
		this.view.addCopy(new CopyKey());
		this.view.addDeleteKey(new DeleteKey());
		
		
	}
	private ACipher getAlgorithm(String method) {
		switch (method) {
		case "Caesar":
			return new Caesar();
		case "Substitution":
			return new Substitution();
		case "Affine":
			return new Affine();
		
		default:
			return null;
		}
	}
	private void process(boolean isEncrypt) {
		String method = view.getMethods();
		String key = view.getKey();
		String input = view.getInputText();
		boolean isVN = view.isVN();
		if(input.trim().isEmpty()) {
			view.showMessage("Vui lòng nhập dữ liệu");
			return;
		}
		try {
			ACipher cipher = getAlgorithm(method);
			if(cipher!=null) {
				String result = isEncrypt? cipher.encrypt(input, key, isVN):cipher.decrypt(input, key, isVN);
				view.setOutputText(result);
			}
			else 
				view.showMessage("Lỗi");
		} catch (NumberFormatException e) {
			view.showMessage("Khoá Caesar là số nguyên");
		} catch (Exception e) {
			// TODO: handle exception
			view.showMessage("Lỗi");
			
		}
	}
	public class Encrypt implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			process(true);
			
		}
		
	}
	public class Decrypt implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			process(false);
			
		}
		
	}
	public class GenKey implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String method = view.getMethods();
			boolean isVN = view.isVN();
			ACipher cipher = getAlgorithm(method);
			if(cipher!= null)
				view.setKey(cipher.genKey(isVN));
		}
		
		
	}
	public class CopyKey implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String copyKey = view.getKey();
			if(copyKey.trim().isEmpty()|| copyKey== null) {
				view.showMessage("Không có khoá để sao chép");
				return;
			}
			StringSelection selection = new StringSelection(copyKey);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(selection, null);
			view.showMessage("Đã sao chép khoá thành công!");
			
		}
		
	}
	public class DeleteKey implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			view.setKey("");
		}
		
	}
	
	

}
