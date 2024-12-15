package MainWindow;
import User.*;


import javax.swing.*;
import java.awt.event.ActionEvent;

public class Home extends MyFrame {
    JButton btnLogin;
    JButton btnRegister;


    public Home() {
        super();
        btnLogin = addButton(190, 130, 50, 100, "Login");
        btnRegister = addButton(190, 190, 50, 100, "Register");
        btnLogin.addActionListener(this::actionPerformed);
        btnRegister.addActionListener(this::actionPerformed);
        this.add(btnLogin);
        this.add(btnRegister);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnLogin)
        {
            this.dispose();
            Login login = new Login();
        }

        if(e.getSource() == btnRegister)
        {
            this.dispose();
            Register register = new Register();
        }
    }

}
