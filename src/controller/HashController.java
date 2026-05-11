package controller;

import View.HashView;
import View.View;
import model.hash.Hash;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;

public class HashController {
    private HashView view;
    private Hash model;
    public HashController(View mainView){
        this.view = mainView.getHash();
        this.model = new Hash();
        this.view.addBtnLoadFile(new LoadFile());
        this.view.addBtnHash(e ->  process());



    }
    private void process() {
        String algorithm  = view.getMethods();
        int selectedTab = view.getSeclectTab();
        String hashResult = "";
        try {
            if (selectedTab == 0) {
                String input = view.getInputText();
                if (input == null || input.isEmpty()) {
                    view.showMessage("Vui lòng nhập dữ liệu");
                    return;
                }
                hashResult = model.checkSum(input, algorithm);

            } else if (selectedTab == 1) {
                String path = view.getSelectedFilePath();
                if (path == null || path.isEmpty()) {
                    view.showMessage("Vui lòng chọn file");
                    return;
                }
                hashResult = model.hashFile(path, algorithm);
            }

            view.setOutputHash(hashResult);
        } catch (NoSuchAlgorithmException e) {
            view.showMessage("Thuật toán không hợp lệ: " + algorithm);
        } catch (FileNotFoundException e) {
            view.showMessage("Không tìm thấy file: " + e.getMessage());
        } catch (Exception e) {
            view.showMessage("Đã xảy ra lỗi: " + e.getMessage());
            e.printStackTrace();
        }

    }
    public class LoadFile implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(view);
            FileDialog fd = new FileDialog(frame, "Choose File", FileDialog.LOAD);
            fd.setVisible(true);
            if (fd.getFile() != null) {
                String file = fd.getDirectory() + fd.getFile();
                view.setSelectedFilePath(file);
            }
        }
    }

}
