package View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class HashView extends JPanel {
    private JLabel algorithm;
    private JComboBox<String> algorithms;
    private JButton btnHash, btnFileHash;
    private JTextArea textInputHash, outputHash,fileInputHash ;
    private JTabbedPane tabFileOrText;
    private JScrollPane scrollOutput;
    private String selectedFilePath = "";


    private final Color bgDark = Color.decode("#004643");
    private final Color btnColor = Color.decode("#f9bc60");
    private final Color btnTextColor = Color.decode("#001e1d");

    public HashView() {
        setLayout(new BorderLayout(10, 10));
        setBackground(bgDark);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JPanel panelTop = new JPanel(new GridLayout(1, 2, 15, 5));
        panelTop.setOpaque(false);
        algorithm = new JLabel("Thuật toán:");
        algorithms = new JComboBox<>(new String[]{"MD2","MD5", "SHA-1", "SHA-224","SHA-384","SHA-256", "SHA-512","SHA-512/224","SHA-512/256","BLAKE2B-512","RIPEMD160","Whirlpool"});
        panelTop.add(algorithm);
        panelTop.add(algorithms);

        JPanel panelCenter = new JPanel(new GridLayout(1, 2, 15, 0));
        panelCenter.setOpaque(false);
        tabFileOrText = new JTabbedPane();
        textInputHash = new JTextArea();
        textInputHash.setLineWrap(true);
        textInputHash.setWrapStyleWord(true);
        textInputHash.setMargin(new Insets(10, 10, 10, 10));

        JPanel pnFile = new JPanel(new BorderLayout(0, 15));
        pnFile.setOpaque(false);
        pnFile.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        fileInputHash = new JTextArea("Chưa chọn file...");
        fileInputHash.setEditable(false);
        fileInputHash.setLineWrap(true);
        fileInputHash.setWrapStyleWord(true);
        fileInputHash.setMargin(new Insets(10, 10, 10, 10));
        fileInputHash.setEditable(false);
        fileInputHash.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        fileInputHash.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane spFile = new JScrollPane(fileInputHash);
        JPanel pnBottomBtn = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnBottomBtn.setOpaque(false);
        btnFileHash = new JButton("Tải File");
        btnFileHash.setPreferredSize(new Dimension(200, 40));
        styleButton(btnFileHash);
        pnBottomBtn.add(btnFileHash);
        pnFile.add(spFile, BorderLayout.CENTER);
        pnFile.add(pnBottomBtn, BorderLayout.SOUTH);
        tabFileOrText.addTab("Văn bản", new JScrollPane(textInputHash));
        tabFileOrText.addTab("File", pnFile);
        JPanel panelRightWrapper = new JPanel(new BorderLayout());
        panelRightWrapper.setOpaque(false);
        panelRightWrapper.setBorder(BorderFactory.createEmptyBorder(32, 0, 12, 0));
        outputHash = new JTextArea();
        outputHash.setLineWrap(true);
        outputHash.setWrapStyleWord(true);
        outputHash.setMargin(new Insets(10, 10, 10, 10));
        outputHash.setEditable(false);
        scrollOutput = new JScrollPane(outputHash);
        panelRightWrapper.add(scrollOutput, BorderLayout.CENTER);
        panelCenter.add(tabFileOrText);
        panelCenter.add(panelRightWrapper);

        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBottom.setOpaque(false);
        btnHash = new JButton("Hash");
        btnHash.setPreferredSize(new Dimension(200, 40));
        styleButton(btnHash);
        panelBottom.add(btnHash);


        add(panelTop, BorderLayout.NORTH);
        add(panelCenter, BorderLayout.CENTER);
        add(panelBottom, BorderLayout.SOUTH);


    }
    private void styleButton(JButton btn) {
        btn.setBackground(btnColor);
        btn.setForeground(btnTextColor);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorder(BorderFactory.createCompoundBorder(new LineBorder(btnColor, 1, true),
                new EmptyBorder(5, 15, 5, 15)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
    }
    public String getMethods(){
        return algorithms.getSelectedItem().toString();
    }
    public int getSeclectTab(){
        return tabFileOrText.getSelectedIndex();
    }
    public String getInputText() {
        return textInputHash.getText();
    }
    public void setSelectedFilePath(String path) {
        this.selectedFilePath = path;
        fileInputHash.setText(path);
    }
    public String getSelectedFilePath() {
        return selectedFilePath;
    }
    public void setOutputHash(String hash) {
        outputHash.setText(hash);
    }
    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
    public void addBtnLoadFile(ActionListener listener) {
        btnFileHash.addActionListener(listener);
    }
    public void addBtnHash(ActionListener listener) {
        btnHash.addActionListener(listener);
    }




}
