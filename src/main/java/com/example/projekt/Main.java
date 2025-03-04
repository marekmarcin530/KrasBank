package com.example.projekt;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.json.JSONObject;


public class Main extends JDialog {
    private JPanel panel1;
    private JTextField bilans;
    private JTable table;
    private JButton Przelew;
    private JButton Zasil;
    private JButton wylogujButton;
    private JScrollPane scrollPane;
    private JLabel EURUSD;
    private JLabel WitajLabel;
    private JLabel BilansLabel;
    private JLabel PLNEUR;
    private JLabel PLNUSD;
    private int userId;
    private Connection connection;


    public Main(int userId) {

        setContentPane(panel1);
        setModal(true);

        String url = "jdbc:mysql://localhost:3306/bank";
        String username = "root";
        String password = "";
        this.userId = userId;



        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT imie, nazwisko, bilans FROM uzytkownicy WHERE Id = ?")) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String imie = resultSet.getString("imie");
                String nazwisko = resultSet.getString("nazwisko");
                String bilans_wartosc = resultSet.getString("bilans");

                WitajLabel.setText(imie + " " + nazwisko);
                BilansLabel.setText(bilans_wartosc);
            } else {
                System.out.println("Użytkownik nie został znaleziony.");
            }
        } catch (Exception ex) {
            System.out.println("Błąd bazy danych: " + ex.getMessage());
        }


        try {
            connection = DriverManager.getConnection(url, username, password);
            String query = "SELECT id_operacji, typ_operacji, kwota, data FROM operacje WHERE id_odbiorcy = ? OR id_nadawcy = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            statement.setInt(2, userId);
            ResultSet resultSet = statement.executeQuery();

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Id operacji");
            model.addColumn("Typ operacji");
            model.addColumn("Kwota");
            model.addColumn("Data");

            while (resultSet.next()) {
                String id_operacji = resultSet.getString("id_operacji");
                String typ_operacji = resultSet.getString("typ_operacji");
                String kwota = resultSet.getString("kwota");
                String data = resultSet.getString("data");
                model.addRow(new String[] { id_operacji , typ_operacji, kwota, data });
            }

            table.setModel(model);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }


        Zasil.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Zasil dialog = new Zasil(userId);
                dialog.pack();
                dialog.setVisible(true);
            }
        });
        wylogujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    // zamknij połączenie z bazą danych
                    connection.close();
                    System.out.println("Polaczenie z baza danych zostalo zamkniete.");
                } catch (Exception ex) {
                    System.out.println("Blad podczas zamykania polaczenia z baza danych: " + ex.getMessage());
                }

                dispose();
                System.exit(0);
                System.out.println("Wylogowano");
            }
        });

        Przelew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Przelew dialog = new Przelew(userId);
                dialog.pack();
                dialog.setVisible(true);
            }
        });

        try {
            URL url1 = new URL("https://api.nbp.pl/api/exchangerates/rates/A/EUR/");
            URLConnection conn = url1.openConnection();

            conn.setRequestProperty("Accept", "application/json");

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();

            JSONObject json = new JSONObject(sb.toString());
            double rate = json.getJSONArray("rates").getJSONObject(0).getDouble("mid");

            PLNEUR.setText("Kurs PLN/EUR: " + rate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            URL url2 = new URL("https://api.nbp.pl/api/exchangerates/rates/A/USD/");
            URLConnection conn = url2.openConnection();

            conn.setRequestProperty("Accept", "application/json");

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();

            JSONObject json = new JSONObject(sb.toString());
            double rate = json.getJSONArray("rates").getJSONObject(0).getDouble("mid");

            PLNUSD.setText("Kurs PLN/USD: " + rate);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void main(String[] args) {
        Main dialog = new Main(1);
        dialog.pack();

        dialog.setVisible(true);
    }


}

