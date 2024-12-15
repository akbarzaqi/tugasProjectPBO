package Masakan;
import MainWindow.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class Masakan extends MyFrame {
    JLabel title;
    JLabel labelFoodName;
    JLabel labelPrice;
    JLabel labelStatus;
    JTextField foodName;
    JTextField price;
    JTextField status;
    JButton btnTrx;
    JButton insert;
    JButton update;
    JButton delete;

    public Masakan() {
        super();

        String[] choise = {"tersedia", "habis"};

        title = addLabel(60, 0, 80, 210, "Menu Masakan");
        title.setFont(new Font("poppins", Font.BOLD, 25));
        this.add(title);

        btnTrx = addButton(350, 25, 35, 100, "Transaksi");
        this.add(btnTrx);

        labelFoodName = addLabel(20, 60, 80, 300, "Nama Masakan");
        labelFoodName.setFont(new Font("poppins", Font.BOLD, 15));
        this.add(labelFoodName);
        foodName = addTextField(180, 88, 27, 220);
        this.add(foodName);

        labelPrice = addLabel(20, 100, 80, 300, "Harga");
        labelPrice.setFont(new Font("poppins", Font.BOLD, 15));
        this.add(labelPrice);
        price = addTextField(180, 128, 27, 220);
        this.add(price);

        labelStatus = addLabel(20, 140, 80, 300, "Status");
        labelStatus.setFont(new Font("poppins", Font.BOLD, 15));
        this.add(labelStatus);
        JComboBox status = new JComboBox(choise);
        status.setBounds(180, 168, 220, 27);
        this.add(status);

        insert = addButton(40, 218, 35, 130, "Insert");
        this.add(insert);

        update = addButton(180, 218, 35, 130, "Update");
        this.add(update);

        delete = addButton(320, 218, 35, 130, "Delete");
        this.add(delete);

        insert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String food = foodName.getText();
                String prc = price.getText();
                String sts = Objects.requireNonNull(status.getSelectedItem()).toString();

                if(food.isEmpty() || prc.isEmpty() || sts.isEmpty())
                {
                    JOptionPane.showMessageDialog(null, "data tidak boleh kosong");
                }else
                {
                    try{
                        int parsePrice = Integer.parseInt(prc);
                        createDish(food, parsePrice, sts);
                        foodName.setText("");
                        price.setText("");
                        status.setSelectedIndex(0);

                    }
                    catch (Exception err)
                    {
                        System.out.println("gagal");
                    }

                    JOptionPane.showMessageDialog(null, "data berhasil ditambahakan");


                }


            }
        });

    }

    public void createDish(String foodName, int price, String status)
    {
        String url = "jdbc:mysql://127.0.0.1:3306/tugas_project";
        String user = "root";
        String password = "";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(url, user, password);

            String sql = "INSERT INTO masakan (nama_masakan, harga, status) VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, foodName);
            preparedStatement.setInt(2, price);
            preparedStatement.setString(3, status);

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
