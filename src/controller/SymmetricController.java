package controller;

import View.View;
import View.SymmetricView;
import model.symmetric.Symmetric;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class SymmetricController {
    private SymmetricView symmetricView;
    private Symmetric symmetricModel;
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
        symmetricView.addTabChangeListener(new TabChange());


    }
    private void updateLenghtOptionKey(){
        String algorithm = symmetricView.getAlgorithm();
        if(algorithm.equals("AES")){
            symmetricView.updateKeyLengthOptions(new String[]{"128", "192", "256"});
        }
        else if(algorithm.equals("DES")){
            symmetricView.updateKeyLengthOptions(new String[]{"56"});
        }
        else if(algorithm.equals("DESede")){
            symmetricView.updateKeyLengthOptions(new String[]{"112", "168"});
        }
    }
    public class TabChange implements javax.swing.event.ChangeListener {
        @Override
        public void stateChanged(javax.swing.event.ChangeEvent e) {
            int selectTab = symmetricView.getSymmetricSelectedTab();
            if(selectTab == 1){
                symmetricView.setTextAreaOutputFile(false);
            }
            else if(selectTab == 0){
                symmetricView.setTextAreaOutputFile(true);
            }
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
            JFileChooser chooser = new JFileChooser();
            if(chooser.showSaveDialog(symmetricView) == JFileChooser.APPROVE_OPTION){
                try (FileWriter writer = new FileWriter(chooser.getSelectedFile())) {
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
                    symmetricView.setSymmetricOutputText(symmetricModel.encryptText(symmetricView.getSymmetricInputText()));
                }else {
                    String src = symmetricView.getSelectedFilePath();
                    symmetricModel.encryptFile(src, src + ".enc");
                    symmetricView.showMessage("Mã hoá file thành công: " + src + ".enc");
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
                if(symmetricView.getSymmetricSelectedTab()==0){
                    symmetricView.setSymmetricOutputText(symmetricModel.decryptText(symmetricView.getSymmetricInputText()));
                } else{
                    String src = symmetricView.getSelectedFilePath();
                    String dest = src.replace(".enc", "") ;
                    symmetricModel.decryptFile(src, dest);
                    symmetricView.showMessage("Thành công! File: " + dest);
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
}
