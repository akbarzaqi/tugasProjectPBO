package Masakan;
import MainWindow.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Objects;

public class Masakan extends MyFrame {
    JLabel title;
    JLabel labelFoodName;
    JLabel labelPrice;
    JLabel labelStatus;
    JTextField foodName;
    JTextField price;
    JLabel labelIdMenu;
    JButton btnTrx;
    JButton insert;
    JButton update;
    JButton delete;
    JButton search;
    JButton reset;

    

    public Masakan() {
        super();

        String[] choise = {"tersedia", "habis"};

        title = addLabel(60, 0, 80, 210, "Menu Masakan");
        title.setFont(new Font("poppins", Font.BOLD, 25));
        this.add(title);

        btnTrx = addButton(350, 15, 35, 100, "Transaksi");
        this.add(btnTrx);

        labelIdMenu = addLabel(350, 30, 80,300, "ID Menu");
        labelIdMenu.setFont(new Font("poppins", Font.BOLD, 13));
        this.add(labelIdMenu);
        JComboBox idMenu = new JComboBox();
        idMenu.setBounds(350, 85, 100, 27);
        this.add(idMenu);

        loadMenu(idMenu);

        search = addButton(350, 130, 27, 100, "Search");
        this.add(search);

        labelFoodName = addLabel(20, 60, 80, 300, "Nama Masakan");
        labelFoodName.setFont(new Font("poppins", Font.BOLD, 15));
        this.add(labelFoodName);
        foodName = addTextField(140, 88, 27, 180);
        this.add(foodName);

        labelPrice = addLabel(20, 100, 80, 300, "Harga");
        labelPrice.setFont(new Font("poppins", Font.BOLD, 15));
        this.add(labelPrice);
        price = addTextField(140, 128, 27, 180);
        this.add(price);

        labelStatus = addLabel(20, 140, 80, 300, "Status");
        labelStatus.setFont(new Font("poppins", Font.BOLD, 15));
        this.add(labelStatus);
        JComboBox status = new JComboBox(choise);
        status.setBounds(140, 168, 180, 27);
        this.add(status);

        insert = addButton(25, 218, 35, 100, "Insert");
        this.add(insert);

        update = addButton(135, 218, 35, 100, "Update");
        this.add(update);

        delete = addButton(245, 218, 35, 100, "Delete");
        this.add(delete);

        reset = addButton(355, 218,35, 100, "New");
        this.add(reset);

        showData();


        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                foodName.setText("");
                price.setText("");
                status.setSelectedIndex(0);
                showData();
            }
        });

        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pid = idMenu.getSelectedItem().toString();
                int parsePID = Integer.parseInt(pid);

                try{

                    deleteData(parsePID);
                    foodName.setText("");
                    price.setText("");
                    status.setSelectedIndex(0);
                    idMenu.setSelectedIndex(0);

                    idMenu.removeAllItems();
                    loadMenu(idMenu);
                    showData();

                }
                catch (Exception err)
                {
                    System.out.println("gagal");
                }

                JOptionPane.showMessageDialog(null, "data berhasil didelete");

            }
        });

        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String food = foodName.getText();
                String prc = price.getText();
                String sts = Objects.requireNonNull(status.getSelectedItem()).toString();

                String pid = idMenu.getSelectedItem().toString();
                int parsePID = Integer.parseInt(pid);

                if(food.isEmpty() || prc.isEmpty() || sts.isEmpty())
                {
                    JOptionPane.showMessageDialog(null, "data tidak boleh kosong");
                }
                else
                {
                    try{
                        int parsePrice = Integer.parseInt(prc);
                        updateData(food, parsePrice, sts, parsePID);
                        foodName.setText("");
                        price.setText("");
                        status.setSelectedIndex(0);

                        idMenu.removeAllItems();
                        loadMenu(idMenu);
                        showData();
                    }
                    catch (Exception err)
                    {
                        System.out.println("gagal");
                    }

                    JOptionPane.showMessageDialog(null, "data berhasil diupdate");

                }

            }
        });

        insert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String food = foodName.getText();
                String prc = price.getText();
                String sts = Objects.requireNonNull(status.getSelectedItem()).toString();



                if(food.isEmpty() || prc.isEmpty() || sts.isEmpty())
                {
                    JOptionPane.showMessageDialog(null, "data tidak boleh kosong");
                }
                else
                {
                    try{
                        int parsePrice = Integer.parseInt(prc);
                        createDish(food, parsePrice, sts);
                        foodName.setText("");
                        price.setText("");
                        status.setSelectedIndex(0);

                        idMenu.removeAllItems();
                        loadMenu(idMenu);
                        showData();

                    }
                    catch (Exception err)
                    {
                        System.out.println("gagal");
                    }

                    JOptionPane.showMessageDialog(null, "data berhasil ditambahakan");

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

                    String pid = idMenu.getSelectedItem().toString();
                    int parsePID = Integer.parseInt(pid);

                    String sql = "SELECT * from masakan where id_masakan = ?";
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setInt(1, parsePID);

                    rs = preparedStatement.executeQuery();
                    while (rs.next() == true)
                    {
                        foodName.setText(rs.getString("nama_masakan"));
                        price.setText(String.valueOf(rs.getInt("harga")));
                        status.setSelectedItem(rs.getString("status"));

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

    public void showData()
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
            String column[] = {"id masakan", "nama masakan", "harga", "status"};
            DefaultTableModel tblModel = new DefaultTableModel(null, column);

            while(rs.next())
            {
                String id = String.valueOf(rs.getInt("id_masakan"));
                String foodName = rs.getString("nama_masakan");
                String price = rs.getString("harga");
                String status = rs.getString("status");

                String data[] = {id, foodName, price, status};
                tblModel.addRow(data);

            }

            JTable dishTable = new JTable(tblModel);
            JScrollPane scrollPane = new JScrollPane(dishTable);
            this.add(scrollPane);
            scrollPane.setBounds(20, 270, 450, 160);

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

    public void deleteData(int ID)
    {
        String url = "jdbc:mysql://127.0.0.1:3306/tugas_project";
        String user = "root";
        String password = "";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(url, user, password);

            String sql = "DELETE FROM masakan where id_masakan = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, ID);


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

    public void updateData(String foodName, int price, String status, int ID)
    {
        String url = "jdbc:mysql://127.0.0.1:3306/tugas_project";
        String user = "root";
        String password = "";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(url, user, password);

            String sql = "UPDATE masakan SET nama_masakan = ?, harga = ?, status = ? where id_masakan = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, foodName);
            preparedStatement.setInt(2, price);
            preparedStatement.setString(3, status);
            preparedStatement.setInt(4, ID);


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

    public void loadMenu(JComboBox idmenu)
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

            String sql = "SELECT id_masakan FROM masakan";
            preparedStatement = connection.prepareStatement(sql);

            rs = preparedStatement.executeQuery();
            while(rs.next())
            {
                idmenu.addItem(rs.getString("id_masakan"));
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
