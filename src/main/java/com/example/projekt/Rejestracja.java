package com.example.projekt;

import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Rejestracja extends JDialog{
    private JPanel Rejestracja;
    private JTextField Login_rej;
    private JPasswordField Haslo_rej;
    private JTextField Imie_rej;
    private JTextField Nazwisko_rej;
    private JTextField Adres_rej;
    private JButton RejestracjaButton;
    private JTextField Telefon_rej;
    private JButton Powrot_Logowanie;

    public Rejestracja() {

        setContentPane(Rejestracja);

        setModal(true);
        getRootPane().setDefaultButton(RejestracjaButton);


        RejestracjaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            String Login = Login_rej.getText();
            String Haslo = Haslo_rej.getText();
            String Imie = Imie_rej.getText();
            String Nazwisko = Nazwisko_rej.getText();
            String Adres = Adres_rej.getText();
            String Telefon = Telefon_rej.getText();

            if(!Login.equals("") && !Haslo.equals("") && !Imie.equals("") && !Nazwisko.equals("") && !Adres.equals("") && Telefon.matches("\\d{9}")) {

                String url = "jdbc:mysql://127.0.0.1:3306/bank";
                String username = "root";
                String password = "";
                try{
                    Connection connection = DriverManager.getConnection(url, username, password);
                    Statement statement = connection.createStatement();
                    Statement statement1 = connection.createStatement();

                    ResultSet resultSet = statement.executeQuery("SELECT login, haslo FROM uzytkownicy WHERE login = '"+ Login + "' AND haslo = '"+ Haslo +"'");
                    ResultSet numer_querry = statement1.executeQuery("SELECT nr_telefonu FROM uzytkownicy");
                        if (resultSet.next() && numer_querry.next()){
                            String telefon = numer_querry.getString("nr_telefonu");
                            String DbLogin = resultSet.getString("login");
                            if (Login.equals(DbLogin) || Telefon.equals(telefon)){
                                JOptionPane.showMessageDialog(null, "Uzytkownik juz istnieje!");
                            }
                            else {
                                JOptionPane.showMessageDialog(null, "Nieprawidłowe hasło");
                                connection.close();
                            }
                        }
                }
                catch (Exception ex){
                    System.out.println(ex.getMessage());
                }

                try{
                    Connection connection = DriverManager.getConnection(url, username, password);
                    Statement statement = connection.createStatement();

                    String query =
                            "INSERT INTO uzytkownicy (login, haslo, imie, nazwisko, adres, nr_telefonu ,bilans) VALUES ('" + Login + "', '" + Haslo + "', '" + Imie + "', " +
                                    "'" + Nazwisko + "','" + Adres + "', '" + Telefon + "', '100')";
                    statement.executeUpdate(query);

                    statement.close();
                    connection.close();

                    JOptionPane.showMessageDialog(null, "Jako nowy uzytkownik KrasBanku dostajesz bonus powitalny o wartosci 100zl. Mozesz teraz sie zalogowac.");

                    try{
                        dispose();
                        Logowanie dialog = new Logowanie();

                        dialog.pack();
                        dialog.setVisible(true);
                        dispose();

                    }catch (Exception ex){
                        JOptionPane.showMessageDialog(null, "Blad");
                }

                }catch (Exception ex){
                    System.out.println(ex.getMessage());
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Nieparwidlowe dane");
            }
            }
        });

        RejestracjaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        Powrot_Logowanie.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    dispose();
                    Logowanie dialog = new Logowanie();
                    dialog.pack();
                    dialog.setVisible(true);

                } catch (Exception ex) {
                    System.out.println("Błąd");
                }
            }
        });
    }




    public static void main(String[] args) {

        Rejestracja dialog = new Rejestracja();
        dialog.pack();
        dialog.setVisible(true);
    }


        }
