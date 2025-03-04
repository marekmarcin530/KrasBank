package com.example.projekt;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;


public class Zasil extends JDialog{
    private JButton zasilButton;
    private JTextField IleZasil;
    private JPanel Zasil;
    private int userId;


    public Zasil(int userID){

        setContentPane(Zasil);
        setModal(true);
        getRootPane().setDefaultButton(zasilButton);
        this.userId=userID;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                Main dialog = new Main(1);
                dialog.pack();
                dialog.setVisible(true);
            }
        });

        zasilButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String value = IleZasil.getText();

                if (value.matches("[0-9]+")) {
                    int wartosc = Integer.parseInt(value);
                    if(wartosc>0)
                    {
                        String url = "jdbc:mysql://127.0.0.1:3306/bank";
                        String username = "root";
                        String password = "";
                        try {
                            Connection connection = DriverManager.getConnection(url, username, password);
                            String query = "UPDATE uzytkownicy SET bilans = bilans + " + wartosc + " WHERE Id = ?";
                            PreparedStatement statement = connection.prepareStatement(query);
                            statement.setInt(1, userId);
                            if (statement.executeUpdate() == 1) {

                                String query2 = "INSERT INTO operacje (typ_operacji, id_nadawcy, id_odbiorcy, kwota) VALUES ( ?, ?, ?, ?)";
                                PreparedStatement statement2 = connection.prepareStatement(query2);
                                statement2.setString(1, "Zasilenie własne");
                                statement2.setInt(2, userId);
                                statement2.setInt(3, userId);
                                statement2.setInt(4, wartosc);
                                if (statement2.executeUpdate() == 1) {
                                    JOptionPane.showMessageDialog(null, "Przelew wykonany");
                                    dispose();
                                } else {
                                    JOptionPane.showMessageDialog(null, "Podana wartość to 0! Wpisz kwotę jeszcze raz.");
                                }
                                dispose();
                                Main dialog = new Main(userId);
                                dialog.pack();
                                dialog.setVisible(true);
                            } else {
                                JOptionPane.showMessageDialog(null, "Błąd w wykonywaniu polecenia!");
                            }
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Wartosc jest rowna 0! Podaj kwote jeszcze raz.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Wartosc zawiera znaki tekstowe! Podaj kwote jeszcze raz");
                }
            }
        });
    }

    public static void main(String[] args) {
        Zasil dialog = new Zasil(1);
        dialog.pack();


        dialog.setVisible(true);

    }

}
