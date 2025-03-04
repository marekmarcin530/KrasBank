package com.example.projekt;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Przelew extends JDialog{

    private JTextField Tel_przelew;
    private JTextField Ile_przelew;
    private JButton wykonajButton;
    private JPanel Przelew;
    private Connection connection;
    private int userId;

    public Przelew(int userId) {
        setContentPane(Przelew);
        setModal(true);
        getRootPane().setDefaultButton(wykonajButton);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                Main dialog = new Main(1);
                dialog.pack();
                dialog.setVisible(true);
            }
        });
        wykonajButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String odbiorca = Tel_przelew.getText();
                String kwota = Ile_przelew.getText();

                int intodbiorca = Integer.parseInt(odbiorca);
                int intkwota = Integer.parseInt(kwota);
                int id_odbiorcy = 0;
                int nadawca = 0;

                try{
                    String url = "jdbc:mysql://localhost:3306/bank";
                    String username = "root";
                    String password = "";

                    connection = DriverManager.getConnection(url, username, password);

                    String query1 = "SELECT nr_telefonu FROM uzytkownicy WHERE Id = ?";
                    PreparedStatement statement1 = connection.prepareStatement(query1);
                    statement1.setInt(1, userId);
                    ResultSet resultSet1 = statement1.executeQuery();
                    if (resultSet1.next()) {
                        nadawca = resultSet1.getInt("nr_telefonu");
                        String snadawca = resultSet1.getString("nr_telefonu");
                        if ( snadawca == "" || nadawca == intodbiorca) {
                            JOptionPane.showMessageDialog(null, "Wysylasz przekew sam do siebie");
                            Tel_przelew.setText("");
                            Ile_przelew.setText("");
                            return;
                        }
                    }

                    String query2 = "SELECT nr_telefonu FROM uzytkownicy WHERE nr_telefonu = ?";
                    PreparedStatement statement2 = connection.prepareStatement(query2);
                    statement2.setInt(1, intodbiorca);
                    ResultSet resultSet2 = statement2.executeQuery();
                    if (!resultSet2.next()) {
                        JOptionPane.showMessageDialog(null, "Nieprawidlowy numer telefonu odbiorcy");
                        Tel_przelew.setText("");
                        Ile_przelew.setText("");
                        return;
                    }

                    String query21 = "SELECT Id FROM uzytkownicy WHERE nr_telefonu = ?";
                    PreparedStatement statement21 = connection.prepareStatement(query21);
                    statement21.setInt(1, intodbiorca);
                    ResultSet resultSet21 = statement21.executeQuery();
                    if (resultSet21.next()) {
                        id_odbiorcy = resultSet21.getInt("Id");
                    }
                    //System.out.println("Id odbiorcy " + id_odbiorcy);

                    String query3 = "SELECT bilans FROM uzytkownicy WHERE Id = ?";
                    PreparedStatement statement3 = connection.prepareStatement(query3);
                    statement3.setInt(1, userId);
                    ResultSet resultSet3 = statement3.executeQuery();
                    if (resultSet3.next()) {
                        int bilans_nadawca = resultSet3.getInt("bilans");
                        if(bilans_nadawca<intkwota){
                            JOptionPane.showMessageDialog(null, "Twoj bilans jest mniejszy niz wysylana kwota");
                            Tel_przelew.setText("");
                            Ile_przelew.setText("");
                        }
                        else {
                            String query4 = "UPDATE uzytkownicy SET bilans = bilans + ? WHERE nr_telefonu = ?";
                            PreparedStatement statement4 = connection.prepareStatement(query4);
                            statement4.setInt(1, intkwota);
                            statement4.setInt(2, intodbiorca);
                            statement4.executeUpdate();
                            System.out.println("Wyslano");

                            String query5 = "UPDATE uzytkownicy SET bilans = bilans - ? WHERE Id = ?";
                            PreparedStatement statement5 = connection.prepareStatement(query5);
                            statement5.setInt(1, intkwota);
                            statement5.setInt(2, userId);
                            statement5.executeUpdate();
                            System.out.println("Zabrano z konta adresata");


                            String query6 = "INSERT INTO operacje (typ_operacji, id_nadawcy, id_odbiorcy, kwota) VALUES (?, ?, ?, ?)";
                            PreparedStatement statement6 = connection.prepareStatement(query6);
                            statement6.setString(1, "Przelew do "+ nadawca + " od "+ intodbiorca);
                            statement6.setInt(2, userId);
                            statement6.setInt(3, id_odbiorcy);
                            statement6.setInt(4, intkwota);
                            if (statement6.executeUpdate() == 1) {
                                System.out.println("Dodano rekord do tabeli operacje");
                            }
                        }
                        dispose();
                        Main dialog = new Main(userId);
                        dialog.pack();
                        dialog.setVisible(true);
                    }
                }
                catch (Exception ex)
                {
                    System.out.println(ex.getMessage());
                }
            }
        });
    }

    public static void main(String[] args) {
        Przelew dialog = new Przelew(1);
        dialog.pack();
        dialog.setVisible(true);
    }
}
