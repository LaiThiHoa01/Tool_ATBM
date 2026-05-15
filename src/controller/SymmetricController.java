package controller;

import View.View;
import View.SymmetricView;
import model.symmetric.Symmetric;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class SymmetricController {
    private SymmetricView symmetricView;
    private Symmetric symmetricModel;
    private String lastOutputText = "";
    private String lastOutputFilePath = "";

    public SymmetricController(View view) {
        this.symmetricView = view.getSymmetricPanel();
        this.symmetricModel = new Symmetric(symmetricView.getAlgorithm(), symmetricView.getMode(), symmetricView.getPadding());
        updateLenghtOptionKey();
        symmetricView.addAlgorithmChangeListener(e -> updateLenghtOptionKey());
        symmetricView.addBtnCreateKey(new CreateKey());
        symmetricView.addBtnInputKey(new LoadKey());
        symmetricView.addBtnSaveKey(new SaveKey());
        symmetricView.addBtnEncrypt(new EncryptSymmetric());
        symmetricView.addBtnDecrypt(new DecryptSymmetric());
        symmetricView.addBtnChooseFile(new ChooseFileSymmetric());
        symmetricView.addBtnDeleteKey(e -> {
            symmetricView.setSymmetricKey("");
            symmetricModel.clearKey();
        });
        symmetricView.addBtnCopyKey(new CopyKey());
        symmetricView.addBtnSaveFileOut(new SaveFileOut());
    }

    private void updateLenghtOptionKey(){
        String algorithm = symmetricView.getAlgorithm();
        if(algorithm.equals("AES") || algorithm.equals("ARIA") || algorithm.equals("CAST6") || algorithm.equals("Serpent")
                || algorithm.equals("Camellia") || algorithm.equals("Twofish")){
            symmetricView.updateKeyLengthOptions(new String[]{"128", "192", "256"});
        }
        else if(algorithm.equals("Blowfish")){
            symmetricView.updateKeyLengthOptions(new String[]{"128", "256", "448"});
        }
        else if(algorithm.equals("DES")){
            symmetricView.updateKeyLengthOptions(new String[]{"56"});
        }
        else if(algorithm.equals("DESede")){
            symmetricView.updateKeyLengthOptions(new String[]{"112", "168"});
        }
        else if(algorithm.equals("CAST5")){
            symmetricView.updateKeyLengthOptions(new String[]{"40", "80", "128"});
        }
        else {
            symmetricView.updateKeyLengthOptions(new String[]{"128"});
        }
    }

    public class CopyKey implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String copyKey = symmetricView.getSymmetricKey();
            if (copyKey == null || copyKey.trim().isEmpty()) {
                symmetricView.showMessage("Không có khoá để sao chép");
                return;
            }
            StringSelection selection = new StringSelection(copyKey);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, null);
            symmetricView.showMessage("Đã sao chép khoá thành công!");
        }
    }

    private void syncModel() {
        symmetricModel.updateConfig(symmetricView.getAlgorithm(), symmetricView.getMode(), symmetricView.getPadding());
    }

    public class CreateKey implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try{
                syncModel();
                symmetricModel.genKey(symmetricView.getKeyLength());
                symmetricView.setSymmetricKey(symmetricModel.exportKeyToBase64());
            } catch (Exception ex) {
                symmetricView.showMessage("Lỗi tạo khoá: " + ex.getMessage());
            }
        }
    }

    public class LoadKey implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(symmetricView);
            FileDialog fd = new FileDialog(frame, "Chọn file lưu khoá", FileDialog.LOAD);
            fd.setVisible(true);
            String filename = fd.getFile();
            String directory = fd.getDirectory();
            if (fd.getFile() != null) {
                File file = new File(directory, filename);
                try {
                    byte[] bytes = Files.readAllBytes(file.toPath());
                    String key = new String(bytes, java.nio.charset.StandardCharsets.UTF_8).trim();
                    if (key.isEmpty()) {
                        symmetricView.showMessage("File khóa trống!");
                    } else {
                        symmetricView.setSymmetricKey(key);
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    public class SaveKey implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String key = symmetricView.getSymmetricKey();
            if(key==null || key.isEmpty()){
                symmetricView.showMessage("Không có khoá để lưu!");
                return;
            }
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(symmetricView);
            FileDialog fd = new FileDialog(frame, "Lưu file khoá", FileDialog.SAVE);
            fd.setVisible(true);
            String filename = fd.getFile();
            String directory = fd.getDirectory();
            if (filename != null) {
                File file = new File(directory, filename);
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(key);
                    symmetricView.showMessage("Đã lưu khóa thành công!");
                } catch (IOException ex) {
                    symmetricView.showMessage("Lỗi lưu file: " + ex.getMessage());
                }
            }
        }
    }

    public class EncryptSymmetric implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            syncModel();
            try {
                symmetricModel.importKeyFromBase64(symmetricView.getSymmetricKey());
                if(symmetricView.getSymmetricSelectedTab()==0){
                    lastOutputText = symmetricModel.encryptText(symmetricView.getSymmetricInputText());
                    symmetricView.setSymmetricOutputText(lastOutputText);
                }else {
                    String src = symmetricView.getSelectedFilePath();
                    String dest = src + ".enc";
                    symmetricModel.encryptFile(src, dest);
                    lastOutputFilePath = dest;
                    symmetricView.showMessage("Mã hoá file thành công: " + dest);
                    symmetricView.setTextAreaOutputFile("File mã hoá được lưu tại: " + dest);
                }
            } catch (Exception ex) {
                symmetricView.showMessage("Lỗi mã hoá: " + ex.getMessage());
            }
        }
    }

    public class DecryptSymmetric implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            syncModel();
            try {
                symmetricModel.importKeyFromBase64(symmetricView.getSymmetricKey());
                if(symmetricView.getSymmetricSelectedTab() == 0){
                    lastOutputText = symmetricModel.decryptText(symmetricView.getSymmetricInputText());
                    symmetricView.setSymmetricOutputText(lastOutputText);
                } else {
                    String src = symmetricView.getSelectedFilePath();
                    if (src == null || src.trim().isEmpty()) {
                        symmetricView.showMessage("Vui lòng tải file để tiếp tục!");
                        return;
                    }

                    String dest;
                    if (src.endsWith(".enc")) {
                        String originalName = src.replace(".enc", "");
                        int dotIndex = originalName.lastIndexOf(".");
                        if (dotIndex != -1) {
                            dest = originalName.substring(0, dotIndex) + "_decrypted" + originalName.substring(dotIndex);
                        } else {
                            dest = originalName + "_decrypted";
                        }
                    } else {
                        dest = src + "_decrypted.txt";
                    }

                    byte[] fileBytes = Files.readAllBytes(new File(src).toPath());
                    String content = new String(fileBytes, java.nio.charset.StandardCharsets.UTF_8).trim();

                    boolean isBase64Text = content.replaceAll("\\s+", "").matches("^[a-zA-Z0-9+/]*={0,2}$") && !content.isEmpty();

                    if (isBase64Text) {
                        String decryptedText = symmetricModel.decryptText(content);
                        try (FileWriter fw = new FileWriter(dest)) {
                            fw.write(decryptedText);
                        }
                    } else {
                        symmetricModel.decryptFile(src, dest);
                    }

                    lastOutputFilePath = dest;
                    symmetricView.showMessage("Thành công! File: " + dest);
                    symmetricView.setTextAreaOutputFile("File giải mã được lưu tại:\n" + dest);
                }
            } catch (Exception ex) {
                symmetricView.showMessage("Lỗi giải mã: " + ex.getMessage());
            }
        }
    }

    public class ChooseFileSymmetric implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(symmetricView);
            FileDialog fd = new FileDialog(frame, "Choose File", FileDialog.LOAD);
            fd.setVisible(true);
            if (fd.getFile() != null) {
                String file = fd.getDirectory() + fd.getFile();
                symmetricView.setSelectedFilePath(file);
            }
        }
    }

    public class SaveFileOut implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (symmetricView.getSymmetricSelectedTab() == 0) {
                if (lastOutputText.isEmpty()) {
                    symmetricView.showMessage("Không có dữ liệu để lưu!");
                    return;
                }
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(symmetricView);
                FileDialog fd = new FileDialog(frame, "Lưu file", FileDialog.SAVE);
                fd.setVisible(true);
                if (fd.getFile() != null) {
                    try (FileWriter fw = new FileWriter(fd.getDirectory() + fd.getFile())) {
                        fw.write(lastOutputText);
                        symmetricView.showMessage("Lưu file thành công!");
                    } catch (Exception ex) {
                        symmetricView.showMessage("Lỗi lưu file!");
                    }
                }
            } else {
                if (lastOutputFilePath.isEmpty()) {
                    symmetricView.showMessage("Chưa có file nào được xử lý!");
                } else {
                    symmetricView.showMessage("File đã được lưu tại vị trí:\n" + lastOutputFilePath);
                }
            }
        }
    }
}
