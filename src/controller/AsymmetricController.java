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
import javax.swing.*;

import View.AsymmetricView;
import View.View;
import model.asymmetric.RSA;

public class AsymmetricController {
    private AsymmetricView asymmetricView;
    private RSA rsaModel;
    private String lastOutputText = "";
    private String lastOutputFilePath = "";

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
        this.asymmetricView.addBtnSaveFileOut(new SaveFileOut());
    }

    private void updateModelConfig() {
        try {
            String algo = asymmetricView.getAlgorithm();
            String mode = asymmetricView.getMode();
            String padding = asymmetricView.getPadding();
            rsaModel.setPadding(algo + "/" + mode + "/" + padding);

            String pubKeyStr = asymmetricView.getPublicKey();
            String privKeyStr = asymmetricView.getPrivateKey();

            if (!pubKeyStr.isEmpty()) {
                rsaModel.setPublicKeyFromBase64(pubKeyStr);
            } else {
                rsaModel.clearPublicKey();
            }

            if (!privKeyStr.isEmpty()) {
                rsaModel.setPrivateKeyFromBase64(privKeyStr);
            } else {
                rsaModel.clearPrivateKey();
            }

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
                lastOutputText = result;
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
                    lastOutputFilePath = outputFilePath;
                    asymmetricView.setAsymmetricOutputText("Đã mã hóa thành công ra file:\n" + outputFilePath);
                }
            }
        } catch (java.security.InvalidKeyException ex) {
            asymmetricView.showMessage("Lỗi: Khóa không hợp lệ!\nVui lòng đảm bảo bạn đang sử dụng đúng Public Key để mã hoá.");
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
            updateModelConfig();
            int currentTab = asymmetricView.getAsymmetricSelectedTab();
            try {
                if (currentTab == 0) {
                    String input = asymmetricView.getAsymmetricInputText();
                    if (input == null || input.trim().isEmpty()) {
                        asymmetricView.showMessage("Vui lòng nhập dữ liệu văn bản!");
                        return;
                    }

                    String result = rsaModel.decrypt(input);
                    lastOutputText = result;
                    asymmetricView.setAsymmetricOutputText(result);

                } else {
                    String src = asymmetricView.getSelectedFilePath();
                    if (src == null || src.trim().isEmpty()) {
                        asymmetricView.showMessage("Vui lòng tải file để tiếp tục!");
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
                        String decryptedText = rsaModel.decrypt(content);
                        try (FileWriter fw = new FileWriter(dest)) {
                            fw.write(decryptedText);
                        }
                    } else {
                        rsaModel.decryptFile(src, dest);
                    }

                    lastOutputFilePath = dest;
                    asymmetricView.setAsymmetricOutputText("Đã giải mã thành công ra file:\n" + dest);
                }
            } catch (java.security.InvalidKeyException ex) {
                asymmetricView.showMessage("Lỗi: Khóa không hợp lệ!\nVui lòng đảm bảo bạn đang sử dụng đúng Private Key để giải mã.");
            } catch (Exception ex) {
                asymmetricView.showMessage("Lỗi xử lý: " + ex.getMessage());
                ex.printStackTrace();
            }
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

    public class SaveFileOut implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (asymmetricView.getAsymmetricSelectedTab() == 0) {
                if (lastOutputText.isEmpty()) {
                    asymmetricView.showMessage("Không có dữ liệu để lưu!");
                    return;
                }
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(asymmetricView);
                FileDialog fd = new FileDialog(frame, "Lưu file", FileDialog.SAVE);
                fd.setVisible(true);
                if (fd.getFile() != null) {
                    try (FileWriter fw = new FileWriter(fd.getDirectory() + fd.getFile())) {
                        fw.write(lastOutputText);
                        asymmetricView.showMessage("Lưu file thành công!");
                    } catch (Exception ex) {
                        asymmetricView.showMessage("Lỗi lưu file!");
                    }
                }
            } else {
                if (lastOutputFilePath.isEmpty()) {
                    asymmetricView.showMessage("Chưa có file nào được xử lý!");
                } else {
                    asymmetricView.showMessage("File đã được lưu tại vị trí:\n" + lastOutputFilePath);
                }
            }
        }
    }
}