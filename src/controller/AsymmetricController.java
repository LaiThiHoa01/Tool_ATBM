package controller;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.*;

import View.Asymmetric;
import View.View;
import model.asymmetric.RSA;

public class AsymmetricController {
    private Asymmetric asymmetricView;
    private RSA rsaModel;

    public AsymmetricController(View mainView) {
        this.asymmetricView = mainView.getAsymmetricPanel();
        this.rsaModel = new RSA();

        this.asymmetricView.addBtnCreateKeyPair(new GenKeyPair());
        this.asymmetricView.addBtnChooseFile(new ChooseFile());
        this.asymmetricView.addBtnEncrypt(new Encrypt());
        this.asymmetricView.addBtnDecrypt(new Decrypt());
        this.asymmetricView.addBtnCopyPubKey(new CopyPubKey());
        this.asymmetricView.addBtnCopyPrivKey(new CopyPrivKey());
        this.asymmetricView.addBtnInputPrivKey(new LoadPrivateKey());
        this.asymmetricView.addBtnInputPubKey(new LoadPublicKey());
        this.asymmetricView.addBtnOutputPrivKey(new SavePrivateKey());
        this.asymmetricView.addBtnOutputPubKey(new SavePublicKey());

    }

    private void updateModelConfig() {
        try {
            String algo = asymmetricView.getAlgorithm();
            String mode = asymmetricView.getMode();
            String padding = asymmetricView.getPadding();
            rsaModel.setPadding(algo + "/" + mode + "/" + padding);

            String pubKeyStr = asymmetricView.getPublicKey();
            String privKeyStr = asymmetricView.getPrivateKey();

            if (!pubKeyStr.isEmpty()) rsaModel.setPublicKeyFromBase64(pubKeyStr);
            if (!privKeyStr.isEmpty()) rsaModel.setPrivateKeyFromBase64(privKeyStr);

        } catch (Exception e) {
            asymmetricView.showMessage("Lỗi nạp khóa: Key không hợp lệ hoặc sai định dạng.");
        }
    }

    private void process(boolean isEncrypt) {
        updateModelConfig();
        int currentTab = asymmetricView.getAsymmetricSelectedTab();
        try {
            if (currentTab == 0) {
                String input = asymmetricView.getAsymmetricInputText();
                if (input == null || input.trim().isEmpty()) {
                    asymmetricView.showMessage("Vui lòng nhập dữ liệu văn bản!");
                    return;
                }

                String result = isEncrypt ? rsaModel.encryptBase64(input) : rsaModel.decrypt(input);
                asymmetricView.setAsymmetricOutputText(result);

            } else {
                String inputFilePath = asymmetricView.getSelectedFilePath();
                if (inputFilePath == null || inputFilePath.trim().isEmpty()) {
                    asymmetricView.showMessage("Vui lòng tải file để tiếp tục!");
                    return;
                }
                if (isEncrypt) {
                    String outputFilePath = inputFilePath + ".enc";
                    rsaModel.encryptFile(inputFilePath, outputFilePath);
                    asymmetricView.setAsymmetricOutputText("Đã mã hóa thành công ra file:\n" + outputFilePath);
                } else {
                    String base = inputFilePath.replace(".enc", "");
                    int dot = base.lastIndexOf(".");
                    String outputFilePath = (dot == -1) ? base + "_decrypt" : base.substring(0, dot) + "_decrypt" + base.substring(dot);

                    rsaModel.decryptFile(inputFilePath, outputFilePath);
                    asymmetricView.setAsymmetricOutputText("Đã giải mã thành công ra file:\n" + outputFilePath);
                }
            }
        } catch (Exception e) {
            asymmetricView.showMessage("Lỗi xử lý: " + e.getMessage());
            e.printStackTrace();
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

    public class GenKeyPair implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int keySize = asymmetricView.getKeyLength();
                rsaModel.genKeyPair(keySize);

                asymmetricView.setPublicKey(rsaModel.getPublicKeyBase64());
                asymmetricView.setPrivateKey(rsaModel.getPrivateKeyBase64());
            } catch (Exception ex) {
                asymmetricView.showMessage("Lỗi tạo khóa: " + ex.getMessage());
            }
        }
    }

    public class ChooseFile implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(asymmetricView);
            FileDialog fd = new FileDialog(frame, "Choose File", FileDialog.LOAD);
            fd.setVisible(true);
            if (fd.getFile() != null) {
                String file = fd.getDirectory() + fd.getFile();
                asymmetricView.setSelectedFilePath(file);
            }
        }
    }
    public class LoadPublicKey implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(asymmetricView);
            FileDialog fd = new FileDialog(frame, "Tải khoá công khai", FileDialog.LOAD);
            fd.setVisible(true);
            String filename = fd.getFile();
            String directory = fd.getDirectory();
            if (fd.getFile() != null) {
                File file = new File(directory, filename);
                try {
                    asymmetricView.setSelectedFilePath(directory);
                    byte[] bytes = Files.readAllBytes(file.toPath());
                    String key = new String(bytes, java.nio.charset.StandardCharsets.UTF_8).trim();
                    if (key.isEmpty()) {
                        asymmetricView.showMessage("File khóa trống!");
                    } else {
                        asymmetricView.setPublicKey(key);
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }
        }
    }
    public class LoadPrivateKey implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(asymmetricView);
            FileDialog fd = new FileDialog(frame, "Tải khoá riêng tư", FileDialog.LOAD);
            fd.setVisible(true);
            String filename = fd.getFile();
            String directory = fd.getDirectory();
            if (fd.getFile() != null) {
                File file = new File(directory, filename);
                try {
                    asymmetricView.setSelectedFilePath(directory);
                    byte[] bytes = Files.readAllBytes(file.toPath());
                    String key = new String(bytes, java.nio.charset.StandardCharsets.UTF_8).trim();
                    if (key.isEmpty()) {
                        asymmetricView.showMessage("File khóa trống!");
                    } else {
                        asymmetricView.setPrivateKey(key);
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }
        }
    }
    public class SavePrivateKey implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String key = asymmetricView.getPrivateKey();
            if(key==null || key.isEmpty()){
                asymmetricView.showMessage("Không có khoá để lưu!");
                return;
            }
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(asymmetricView);
            FileDialog fd = new FileDialog(frame, "Lưu file khoá", FileDialog.SAVE);
            fd.setVisible(true);
            String filename = fd.getFile();
            String directory = fd.getDirectory();
            if (filename != null) {
                File file = new File(directory, filename);
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(key);
                    asymmetricView.showMessage("Đã lưu khóa thành công!");
                } catch (IOException ex) {
                    asymmetricView.showMessage("Lỗi lưu file: " + ex.getMessage());
                }
            }
        }
    }
    public class SavePublicKey implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String key = asymmetricView.getPublicKey();
            if(key==null || key.isEmpty()){
                asymmetricView.showMessage("Không có khoá để lưu!");
                return;
            }
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(asymmetricView);
            FileDialog fd = new FileDialog(frame, "Lưu file khoá", FileDialog.SAVE);
            fd.setVisible(true);
            String filename = fd.getFile();
            String directory = fd.getDirectory();
            if (filename != null) {
                File file = new File(directory, filename);
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(key);
                    asymmetricView.showMessage("Đã lưu khóa thành công!");
                } catch (IOException ex) {
                    asymmetricView.showMessage("Lỗi lưu file: " + ex.getMessage());
                }
            }
        }
    }

    public class CopyPubKey implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String copyKey = asymmetricView.getPublicKey();
            if (copyKey == null || copyKey.trim().isEmpty()) {
                asymmetricView.showMessage("Không có khoá để sao chép");
                return;
            }
            StringSelection selection = new StringSelection(copyKey);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, null);
            asymmetricView.showMessage("Đã sao chép khoá thành công!");
        }
    }

    public class CopyPrivKey implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String copyKey = asymmetricView.getPrivateKey();
            if (copyKey == null || copyKey.trim().isEmpty()) {
                asymmetricView.showMessage("Không có khoá để sao chép");
                return;
            }
            StringSelection selection = new StringSelection(copyKey);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, null);
            asymmetricView.showMessage("Đã sao chép khoá thành công!");
        }
    }
}