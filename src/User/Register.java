package User;
import MainWindow.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Register extends MyFrame {
    JLabel title;
    JLabel labelUname;
    JLabel labelPass;
    JTextField textFieldUname;
    JPasswordField textFieldPass;
    JButton btnSubmit;
    JButton btnLogin;

    public Register()
    {
        super();
        title = addLabel(150, 50, 50, 200, "Register");
        title.setFont(new Font("Poppins", Font.BOLD, 30));
        this.add(title);

        labelUname = addLabel(150, 120, 30, 100, "Username");
        textFieldUname = addTextField(150, 150, 30, 200);
        this.add(labelUname);
        this.add(textFieldUname);

        labelPass = addLabel(150, 180, 30, 100, "Password");
        textFieldPass = addPasswordField(150, 210, 30, 200);
        this.add(labelPass);
        this.add(textFieldPass);

        btnLogin = addButton(150, 270, 30, 80, "Login");
        this.add(btnLogin);
        btnSubmit = addButton(265, 270, 30, 80, "Submit");
        this.add(btnSubmit);

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Login login = new Login();
            }
        });



        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = textFieldUname.getText();
                String password = textFieldPass.getText();

                try {
                    createUser(username, password);
                    textFieldUname.setText("");
                    textFieldPass.setText("");

                }
                catch (Exception err)
                {
                    System.out.println("gagal");
                }

                JOptionPane.showMessageDialog(null, "data berhasil ditambahakan");
            }
        });




    }

    public void createUser(String uname, String pass)
    {
        String url = "jdbc:mysql://127.0.0.1:3306/tugas_project";
        String user = "root";
        String password = "";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(url, user, password);

            String sql = "INSERT INTO user (username, password) VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uname);
            preparedStatement.setString(2, pass);

            preparedStatement.executeUpdate();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {

            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }
}
