package Transaksi;
import MainWindow.*;
import Masakan.Masakan;
import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class Transaksi extends MyFrame {
    JLabel title;
    JLabel labelNamaPelanggan;
    JLabel tgl;
    JLabel labelQty;
    JLabel labelIDMasakan;
    JComboBox idMasakan;
    JTextField namaPelanggan;
    JTextField qty;
    JButton btnMenu;
    JButton insert;
    JButton showDataTrx;
    JDateChooser calendar;




    public Transaksi()
    {
        super();
        title = addLabel(60, 0, 80, 210, "Menu Transaksi");
        title.setFont(new Font("poppins", Font.BOLD, 25));
        this.add(title);

        btnMenu = addButton(350, 15, 35, 100, "Menu");
        this.add(btnMenu);

        labelNamaPelanggan = addLabel(20, 60, 80, 300, "Nama Pelanggan");
        labelNamaPelanggan.setFont(new Font("poppins", Font.BOLD, 15));
        this.add(labelNamaPelanggan);
        namaPelanggan = addTextField(150, 88, 27, 280);
        this.add(namaPelanggan);

        labelIDMasakan = addLabel(20, 100 , 80, 300, "ID Masakan");
        labelIDMasakan.setFont(new Font("poppins", Font.BOLD, 15));
        this.add(labelIDMasakan);
        idMasakan = new JComboBox();
        idMasakan.setBounds(150, 128, 280, 27);
        this.add(idMasakan);

        loadMenu(idMasakan);

        tgl = addLabel(20, 140, 80, 300, "Tanggal");
        tgl.setFont(new Font("poppins", Font.BOLD, 15));
        this.add(tgl);
        calendar = new JDateChooser();
        calendar.setBounds(150, 168, 280, 27);
        this.add(calendar);

        labelQty = addLabel(20, 188, 80, 300, "Qty");
        labelQty.setFont(new Font("poppins", Font.BOLD, 15));
        this.add(labelQty);
        qty = addTextField(150, 208, 27, 280);
        this.add(qty);

        insert = addButton(330, 248, 35, 100, "Insert");
        this.add(insert);


        showDataTrx = addButton(220, 248, 35, 100, "Show Data");
        this.add(showDataTrx);

        btnMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Masakan();
                dispose();
            }
        });

        insert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String url = "jdbc:mysql://127.0.0.1:3306/tugas_project";
                String user = "root";
                String password = "";

                Connection connection = null;
                PreparedStatement preparedStatement = null;
                ResultSet rs = null;

                String cus = namaPelanggan.getText();
                String idMskn = Objects.requireNonNull(idMasakan.getSelectedItem()).toString();
                String idMasak = "";
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String date = sdf.format(calendar.getDate());
                String quantity = qty.getText();

                if(idMskn != null && !idMskn.isEmpty())
                {
                    String[] parts = idMskn.split(" \\| ");
                    idMasak = parts[0];
                }

                try{
                    Class.forName("com.mysql.cj.jdbc.Driver");

                    connection = DriverManager.getConnection(url, user, password);

                    int parseQty = Integer.parseInt(quantity);
                    int parseID = Integer.parseInt(idMasak);
                    int harga = 0;

                    String sql = "SELECT * from masakan where id_masakan = ?";
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setInt(1, parseID);


                    rs = preparedStatement.executeQuery();
                    while (rs.next() == true)
                    {
                        harga = rs.getInt("harga");
                    }

                    if(date.isEmpty())
                    {
                        JOptionPane.showMessageDialog(null, "data tidak boleh kosong");

                    }
                    else
                    {
                        int totalPrc = parseQty * harga;
                        createTransaction(cus, parseID, date, parseQty, totalPrc);
                        namaPelanggan.setText("");
                        idMasakan.setSelectedIndex(0);
                        calendar.setCalendar(null);
                        qty.setText("");
                    }

                }catch (Exception err)
                {
                    err.getMessage();
                }finally {

                    try {
                        if (preparedStatement != null) preparedStatement.close();
                        if (connection != null) connection.close();
                    } catch (SQLException err) {
                        err.printStackTrace();
                    }
                }




            }
        });

    }

    public void createTransaction(String customer, int ID, String date, int qty, int totalPrice)
    {
        String url = "jdbc:mysql://127.0.0.1:3306/tugas_project";
        String user = "root";
        String password = "";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(url, user, password);

            String sql = "INSERT INTO transaksi (nama_pelanggan, id_masakan, tanggal, qty, total_harga) VALUES (?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, customer);
            preparedStatement.setInt(2, ID);
            preparedStatement.setString(3, date);
            preparedStatement.setInt(4, qty);
            preparedStatement.setInt(5, totalPrice);
            preparedStatement.executeUpdate();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            JOptionPane.showMessageDialog(null, "data berhasil ditambahakan");

            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadMenu(JComboBox idMenu)
    {
        String url = "jdbc:mysql://127.0.0.1:3306/tugas_project";
        String user = "root";
        String password = "";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(url, user, password);

            String sql = "SELECT * FROM masakan";
            preparedStatement = connection.prepareStatement(sql);

            rs = preparedStatement.executeQuery();
            while(rs.next())
            {
                idMenu.addItem(rs.getString("id_masakan") + " | " + rs.getString("nama_masakan") + " | " + rs.getInt("harga"));
            }

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
