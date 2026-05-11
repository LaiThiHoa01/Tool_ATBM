package controller;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import View.View;
import View.ClassicalView;
import model.classical.*;

public class ClassicalController {
    private ClassicalView classicalView;

    public ClassicalController(View mainView) {
        this.classicalView = mainView.getClassicalPanel();

        this.classicalView.addbtnEncrypt(new Encrypt());
        this.classicalView.addGenKey(new GenKey());
        this.classicalView.addbtnDecrypt(new Decrypt());
        this.classicalView.addCopy(new CopyKey());
        this.classicalView.addDeleteKey(new DeleteKey());
    }

    private ACipher getAlgorithm(String method) {
        switch (method) {
            case "Caesar":
                return new Caesar();
            case "Substitution":
                return new Substitution();
            case "Affine":
                return new Affine();
            case "Hill":
                return new Hill();
            case "Vigenere":
                return new Vigenere();
            default:
                return null;
        }
    }

    private void process(boolean isEncrypt) {
        String method = classicalView.getMethods();
        String key = classicalView.getKey();
        String input = classicalView.getInputText();
        boolean isVN = classicalView.isVN();

        if (input.trim().isEmpty()) {
            classicalView.showMessage("Vui lòng nhập dữ liệu");
            return;
        }

        try {
            ACipher cipher = getAlgorithm(method);
            if (cipher != null) {
                String result = isEncrypt ? cipher.encrypt(input, key, isVN) : cipher.decrypt(input, key, isVN);
                classicalView.setOutputText(result);
            } else {
                classicalView.showMessage("Lỗi: Không tìm thấy thuật toán");
            }
        } catch (NumberFormatException e) {
            classicalView.showMessage("Khoá không hợp lệ!");
        } catch (Exception e) {
            classicalView.showMessage("Lỗi: " + e.getMessage());
        }
    }

    public class Encrypt implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            process(true);
        }
    }

    public class Decrypt implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            process(false);
        }
    }

    public class GenKey implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(!classicalView.isSelectLanguage()){
                classicalView.showMessage("Vui lòng chọn ngôn ngữ trước khi tạo khoá!");
                return;
            }
            String method = classicalView.getMethods();
            boolean isVN = classicalView.isVN();
            ACipher cipher = getAlgorithm(method);
            if (cipher != null) {
                classicalView.setKey(cipher.genKey(isVN));
            }
        }
    }

    public class CopyKey implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String copyKey = classicalView.getKey();
            if (copyKey == null || copyKey.trim().isEmpty()) {
                classicalView.showMessage("Không có khoá để sao chép");
                return;
            }
            StringSelection selection = new StringSelection(copyKey);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, null);
            classicalView.showMessage("Đã sao chép khoá thành công!");
        }
    }

    public class DeleteKey implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            classicalView.setKey("");
        }
    }
}