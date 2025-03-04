package com.example.projekt;

import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class Logowanie extends JDialog{
    private JPanel contentPane;
    private JTextField PoleLogowanie;
    private JPasswordField PoleHaslo;
    private JButton ZalogujButton;
    private JButton RejestracjaButton;

    public Logowanie(){
        setContentPane(contentPane);
        setModal(true);
        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        RejestracjaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dispose();
                    Rejestracja dialog = new Rejestracja();
                    dialog.pack();
                    dialog.setVisible(true);

                } catch (Exception ex) {
                    System.out.println("Błąd");
                }
            }
        });
        ZalogujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = PoleLogowanie.getText();
                String haslo = PoleHaslo.getText();

                if (!login.equals("") && !haslo.equals("")) {

                    String url = "jdbc:mysql://127.0.0.1:3306/bank";
                    String username = "root";
                    String password = "";

                    try {
                        Connection connection = DriverManager.getConnection(url, username, password);
                        String query = "SELECT haslo, Id FROM uzytkownicy WHERE login = ?";
                        PreparedStatement statement = connection.prepareStatement(query);
                        statement.setString(1, login);
                        ResultSet resultSet = statement.executeQuery();

                        if (resultSet.next()) {
                            String dbhaslo = resultSet.getString("haslo");

                            if (haslo.equals(dbhaslo)) {
                                try {
                                    dispose();
                                    int userId = resultSet.getInt("Id");
                                    Main dialog = new Main(userId);
                                    dialog.pack();
                                    dialog.setVisible(true);
                                    connection.close();
                                } catch (Exception ex) {
                                    System.out.println("Test");
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Nieprawidlowe hasło");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Użytkownik o podanym loginie nie istnieje");
                        }

                        PoleLogowanie.setText("");
                        PoleHaslo.setText("");
                        statement.close();
                        connection.close();

                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }

                }else{
                    JOptionPane.showMessageDialog(null, "Pozostawiles puste pole");
                }
            }
        });
    }

    private void onCancel() {
        dispose();
    }

    public static void main(String[] args) {
        Logowanie dialog = new Logowanie();
        dialog.pack();
        dialog.setVisible(true);
        }


    }

