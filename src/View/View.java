package View;

import javax.swing.*;
import java.awt.*;

public class View extends JFrame {
    private final Color bgDark = Color.decode("#004643");
    private final Color bgLight = Color.decode("#e8e4e6");
    private final Color textMain = Color.decode("#fffffe");
    private final Color btnColor = Color.decode("#f9bc60");
    private final Color btnTextColor = Color.decode("#001e1d");

    private ClassicalView classicalPanel;
    private SymmetricView symmetricPanel;
    private AsymmetricView asymmetricPanel;
    private HashView hash;


    public View() {
        UIManager.put("Panel.background", bgDark);
        UIManager.put("Label.foreground", textMain);
        UIManager.put("RadioButton.background", bgDark);
        UIManager.put("RadioButton.foreground", textMain);
        UIManager.put("TabbedPane.background", bgLight);
        UIManager.put("TabbedPane.foreground", btnTextColor);
        UIManager.put("TabbedPane.selected", btnColor);
        UIManager.put("TabbedPane.contentAreaColor", bgDark);
        UIManager.put("ComboBox.background", bgLight);
        UIManager.put("ComboBox.foreground", btnTextColor);
        UIManager.put("ScrollPane.background", bgDark);
        UIManager.put("ScrollPane.border", BorderFactory.createEmptyBorder());
        UIManager.put("OptionPane.background", bgDark);
        UIManager.put("OptionPane.messageForeground", textMain);

        setSize(1350, 650);
        setTitle("Tool mã hoá");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(bgDark);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

        classicalPanel = new ClassicalView();
        symmetricPanel = new SymmetricView();
        asymmetricPanel = new AsymmetricView();
        hash = new HashView();



        tabbedPane.add("Mã hoá cổ điển", classicalPanel);
        tabbedPane.add("Mã hoá đối xứng", symmetricPanel);
        tabbedPane.add("Mã hoá bất đối xứng", asymmetricPanel);
        tabbedPane.add("Hash", hash);
        tabbedPane.setBorder(BorderFactory.createEmptyBorder());
        this.add(tabbedPane, BorderLayout.CENTER);
    }

    public ClassicalView getClassicalPanel() {
        return classicalPanel;
    }

    public SymmetricView getSymmetricPanel() {
        return symmetricPanel;
    }
    public AsymmetricView getAsymmetricPanel() {
        return asymmetricPanel;
    }
    public HashView getHash() {return hash;}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            View view = new View();
            view.setVisible(true);
        });
    }

}