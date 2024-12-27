package Transaksi;

import MainWindow.*;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;

public class MenuTransaksi extends MyFrame {
    JLabel title;
    JLabel labelNamaPelanggan;
    JLabel tgl;
    JLabel labelQty;
    JLabel labelIDMasakan;
    JLabel labelIDTrx;
    JComboBox idMasakan;
    JComboBox IDTrx;
    JTextField namaPelanggan;
    JTextField qty;
    JButton btnMenu;
    JButton search;
    JButton update;
    JButton createTrx;
    JDateChooser calendar;
    public MenuTransaksi()
    {
        super();
        title = addLabel(60, 0, 80, 210, "Data Transaksi");
        title.setFont(new Font("poppins", Font.BOLD, 25));
        this.add(title);

        btnMenu = addButton(350, 15, 35, 100, "Menu");
        this.add(btnMenu);

        labelIDTrx = addLabel(360, 35, 80, 100, "ID Transaksi");
        labelIDTrx.setFont(new Font("poppins", Font.BOLD, 15));
        this.add(labelIDTrx);
        IDTrx = new JComboBox();
        IDTrx.setBounds(360, 87, 100, 27);
        this.add(IDTrx);

        loadIDTransaksi(IDTrx);

        search = addButton(360, 126, 27, 100, "Search");
        this.add(search);

        labelNamaPelanggan = addLabel(20, 60, 80, 300, "Nama Pelanggan");
        labelNamaPelanggan.setFont(new Font("poppins", Font.BOLD, 15));
        this.add(labelNamaPelanggan);
        namaPelanggan = addTextField(150, 88, 27, 190);
        this.add(namaPelanggan);

        labelIDMasakan = addLabel(20, 100 , 80, 300, "ID Masakan");
        labelIDMasakan.setFont(new Font("poppins", Font.BOLD, 15));
        this.add(labelIDMasakan);
        idMasakan = new JComboBox();
        idMasakan.setBounds(150, 128, 190, 27);
        this.add(idMasakan);

        loadIDMasakan(idMasakan);

        tgl = addLabel(20, 140, 80, 300, "Tanggal");
        tgl.setFont(new Font("poppins", Font.BOLD, 15));
        this.add(tgl);
        calendar = new JDateChooser();
        calendar.setBounds(150, 168, 190, 27);
        this.add(calendar);

        labelQty = addLabel(20, 188, 80, 300, "Qty");
        labelQty.setFont(new Font("poppins", Font.BOLD, 15));
        this.add(labelQty);
        qty = addTextField(150, 208, 27, 190);
        this.add(qty);

        update = addButton(250, 248, 35, 90, "Update");
        this.add(update);


        createTrx = addButton(150, 248, 35, 95, "Add Trx");
        this.add(createTrx);

        createTrx.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Transaksi();
                dispose();
            }
        });

        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = "jdbc:mysql://127.0.0.1:3306/tugas_project";
                String user = "root";
                String password = "";

                Connection connection = null;
                PreparedStatement preparedStatement = null;
                ResultSet rs = null;

                String cusName = namaPelanggan.getText();
                String idTransaction = IDTrx.getSelectedItem().toString();
                String idMskn = idMasakan.getSelectedItem().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String date = sdf.format(calendar.getDate());
                String quantity = qty.getText();



                if(idTransaction.isEmpty() || idMskn.isEmpty() || date.isEmpty() || quantity.isEmpty())
                {
                    JOptionPane.showMessageDialog(null, "data tidak boleh kosong");
                }
                else
                {
                    try{
                        Class.forName("com.mysql.cj.jdbc.Driver");

                        connection = DriverManager.getConnection(url, user, password);

                        int parseIdTrx = Integer.parseInt(idTransaction);
                        int parseIdMasakan = Integer.parseInt(idMskn);
                        int parseQty = Integer.parseInt(quantity);

                        int harga = 0;

                        String sql = "SELECT * from masakan where id_masakan = ?";
                        preparedStatement = connection.prepareStatement(sql);
                        preparedStatement.setInt(1, parseIdMasakan);
                        rs = preparedStatement.executeQuery();
                        while (rs.next() == true)
                        {
                            harga = rs.getInt("harga");
                        }
                        int totalPrice = parseQty * harga;
                        System.out.println("harga : " + harga);
                        System.out.println("total harga : " + totalPrice);

                        updateData(parseIdTrx, cusName, parseIdMasakan, date, parseQty, totalPrice);

                        idMasakan.removeAllItems();
                        IDTrx.removeAllItems();
                        loadIDMasakan(idMasakan);
                        loadIDTransaksi(IDTrx);
                    }
                    catch (Exception err)
                    {
                        System.out.println("gagal");
                    }
                }
            }
        });

        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = "jdbc:mysql://127.0.0.1:3306/tugas_project";
                String user = "root";
                String password = "";

                Connection connection = null;
                PreparedStatement preparedStatement = null;
                ResultSet rs = null;

                try {

                    Class.forName("com.mysql.cj.jdbc.Driver");

                    connection = DriverManager.getConnection(url, user, password);

                    String idTrx = IDTrx.getSelectedItem().toString();
                    int parsePID = Integer.parseInt(idTrx);

                    String sql = "SELECT t.id_transaksi, t.nama_pelanggan, t.qty, t.tanggal, m.id_masakan from transaksi t inner join masakan m on t.id_masakan = m.id_masakan where t.id_transaksi = ?";
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setInt(1, parsePID);

                    rs = preparedStatement.executeQuery();

                    loadIDMasakan(idMasakan);
                    loadIDTransaksi(IDTrx);
                    while (rs.next() == true)
                    {
                        namaPelanggan.setText(rs.getString("nama_pelanggan"));
                        qty.setText(String.valueOf(rs.getInt("qty")));
                        Date tanggal = rs.getDate("tanggal");
                        calendar.setDate(tanggal);
                        idMasakan.setSelectedItem(rs.getString("id_masakan"));

                    }
                } catch (ClassNotFoundException | SQLException err) {
                    err.printStackTrace();
                } finally {

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

    public void updateData(int transactionID, String cusName, int dishID, String date, int qty, int totalPrice)
    {
        String url = "jdbc:mysql://127.0.0.1:3306/tugas_project";
        String user = "root";
        String password = "";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(url, user, password);

            String sql = "UPDATE transaksi SET nama_pelanggan = ?, id_masakan = ?, tanggal = ?, qty = ?, total_harga = ? where id_transaksi = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, cusName);
            preparedStatement.setInt(2, dishID);
            preparedStatement.setString(3, date);
            preparedStatement.setInt(4, qty);
            preparedStatement.setInt(5, totalPrice);
            preparedStatement.setInt(6, transactionID);


            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "data berhasil diupdate");
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


    public void loadIDTransaksi(JComboBox IdTrx)
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

            String sql = "SELECT id_transaksi FROM transaksi";
            preparedStatement = connection.prepareStatement(sql);

            rs = preparedStatement.executeQuery();
            while(rs.next())
            {
                IdTrx.addItem(rs.getString("id_transaksi") );
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


    public void loadIDMasakan(JComboBox IdMasak)
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
                IdMasak.addItem(rs.getString("id_masakan"));
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

