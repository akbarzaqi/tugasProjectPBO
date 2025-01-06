package User;
import Koneksi.*;
import MainWindow.MyFrame;
import Masakan.Masakan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Login extends MyFrame {
    JLabel title;
    JLabel username;
    JLabel password;
    JTextField uname;
    JPasswordField pass;
    JButton btnSubmit;
    JButton btnRegis;


    public Login() {
        super();
        title = addLabel(150, 50, 50, 200, "Login");
        title.setFont(new Font("Poppins", Font.BOLD, 30));
        this.add(title);

        username = addLabel(150, 120, 30, 100, "Username");
        uname = addTextField(150, 150, 30, 200);
        this.add(username);
        this.add(uname);

        password = addLabel(150, 180, 30, 100, "Password");
        pass = addPasswordField(150, 210, 30, 200);
        this.add(password);
        this.add(pass);

        btnRegis = addButton(150, 270, 30, 90, "Register");
        this.add(btnRegis);

        btnSubmit = addButton(265, 270, 30, 80, "Submit");
        this.add(btnSubmit);

        btnRegis.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Register register = new Register();
            }
        });

        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = uname.getText();
                String password = pass.getText();

                uname.setText("");
                pass.setText("");

                if(validateLogin(username, password))
                {
                    JOptionPane.showMessageDialog(null, "Login Berhasil");
                    dispose();
                    new Masakan();
                }
                else {
                    JOptionPane.showMessageDialog(null, "username atau password anda salah!");
                }

            }
        });


    }

    boolean validateLogin(String username, String password)
    {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {

            connection = Conn.getConnection();

            String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            rs = preparedStatement.executeQuery();

            if(rs.next())
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
