package MainWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyFrame extends JFrame implements ActionListener {
    JButton button;
    public MyFrame()
    {
        this.setTitle("main frame");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setResizable(false);
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public JButton addButton(int x, int y, int height, int width, String title)
    {
        JButton button = new JButton(title);
        button.setBounds(x, y, width, height);
        return button;
    }

    public JLabel addLabel(int x, int y, int height, int width, String text)
    {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        return label;
    }

    public JTextField addTextField(int x, int y, int height, int width)
    {
        JTextField textField = new JTextField();
        textField.setBounds(x, y, width, height);
        return textField;
    }




    @Override
    public void actionPerformed(ActionEvent e) {}
}
