package Transaksi;
import MainWindow.*;
import Koneksi.*;
import Masakan.Masakan;
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
    JButton back;
    JButton showDataTrx;
    JDateChooser calendar;




    public Transaksi()
    {
        super();
        title = addLabel(60, 0, 80, 210, "Menu Transaksi");
        title.setFont(new Font("poppins", Font.BOLD, 25));
        this.add(title);

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

        back = addButton(110, 248, 35, 100, "Back");
        this.add(back);

        insert = addButton(330, 248, 35, 100, "Insert");
        this.add(insert);


        showDataTrx = addButton(220, 248, 35, 100, "Show Data");
        this.add(showDataTrx);

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Masakan();
                dispose();
            }
        });

        showDataTrx.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MenuTransaksi();
                dispose();
            }
        });

        insert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Connection connection = null;
                PreparedStatement preparedStatement = null;
                ResultSet rs = null;

                String cus = namaPelanggan.getText();
                String idMskn = Objects.requireNonNull(idMasakan.getSelectedItem()).toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String date = sdf.format(calendar.getDate());
                String quantity = qty.getText();


                try{
                    connection = Conn.getConnection();

                    int parseQty = Integer.parseInt(quantity);
                    int parseID = Integer.parseInt(idMskn);
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
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {

            connection = Conn.getConnection();

            String sql = "INSERT INTO transaksi (nama_pelanggan, id_masakan, tanggal, qty, total_harga) VALUES (?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, customer);
            preparedStatement.setInt(2, ID);
            preparedStatement.setString(3, date);
            preparedStatement.setInt(4, qty);
            preparedStatement.setInt(5, totalPrice);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
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
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {

            connection = Conn.getConnection();

            String sql = "SELECT * FROM masakan";
            preparedStatement = connection.prepareStatement(sql);

            rs = preparedStatement.executeQuery();
            while(rs.next())
            {
                idMenu.addItem(rs.getString("id_masakan"));
            }

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
    }

}
