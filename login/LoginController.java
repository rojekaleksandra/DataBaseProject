package login;
import database.BD;
import validator.Validate;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public static int acc;
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet res = null;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passField;
    @FXML
    private RadioButton userButton;
    @FXML
    private RadioButton employeeButton;
    @FXML

    /**
     * Loguje klienta do bazy danych. W przypadku niepowodzenia wyrzuca wyjątek.
     * @param login - login użytkownika
     * @param password - hasło użytkownika
     * @return boolean
     */
    public boolean userExists(String login, String password) {
        try {
            con = BD.connect();
            String sql = "Select klient_zaloguj(?,?);";
            pst = con.prepareStatement(sql);
            pst.setString(1,login);
            pst.setString(2,password);
            pst.execute();

            pst.close();
            con.close();
            return true;

        } catch (SQLException e) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Bład");
                a.setHeaderText("Użytkownik niezalogowany!");
                a.setContentText(e.getMessage().split("Where")[0]);
                a.showAndWait();
        }  finally {
            BD.close(con,pst);
        }
        return false;
    }

    /**
     * Loguje pracownika do bazy danych. W przypadku niepowodzenia wyrzuca wyjątek.
     * @param login - login pracownika
     * @param password - hasło pracownika
     * @return boolean
     */
    public boolean employeeExists(String login, String password) {
        try {
            con = BD.connect();
            String sql = "Select prowadzacy_zaloguj(?,?);";
            pst = con.prepareStatement(sql);
            pst.setString(1,login);
            pst.setString(2,password);
            pst.execute();

            pst.close();
            con.close();
            return true;

        } catch (SQLException e) {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Bład");
                    a.setHeaderText("Nie udało się zalogować");
                    a.setContentText(e.getMessage().split("Where")[0]);
                    a.showAndWait();
        } finally {
            BD.close(con, pst);
        }
        return false;
    }

    /**
     * Sprawdza walidaję pól w formularzu i wywołuje metodę logującą do bazy danych.
     * Przekierowuje użytkownika w zależności od roli do jakiej się zalogował.
     *      @param event
     */
    public void login(MouseEvent event) throws IOException{
        String login = loginField.getText();
        String password = passField.getText();
        if (Validate.checkLogin(loginField) && Validate.checkPass(passField)) {
            if (userButton.isSelected()) {
                if (userExists(login, password)) {
                    try {
                        con = BD.connect();
                        String sql = "select * from klient where login=? and haslo=?;";
                        pst = con.prepareStatement(sql);
                        pst.setString(1, loginField.getText());
                        pst.setString(2, passField.getText());
                        res = pst.executeQuery();

                        if (res.next()) {
                            acc = res.getInt("id_klient");
                        }
                        res.close();
                        pst.close();
                        con.close();

                        ((Node) event.getSource()).getScene().getWindow().hide();
                        Parent root = FXMLLoader.load(getClass().getResource("../client/Client.fxml"));
                        Scene scene = new Scene(root);
                        Stage stage = new Stage();
                        stage.setTitle("Klub fitness");
                        stage.setScene(scene);
                        stage.setResizable(false);
                        stage.show();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            } else if (employeeButton.isSelected()) {
                if (employeeExists(login, password)) {
                    try {
                        con = BD.connect();
                        String sql = "select * from prowadzacy where login=? and haslo=?;";
                        pst = con.prepareStatement(sql);
                        pst.setString(1, loginField.getText());
                        pst.setString(2, passField.getText());
                        res = pst.executeQuery();

                        if (res.next()) {
                            acc = res.getInt("id_prowadzacy");
                        }
                        res.close();
                        pst.close();
                        con.close();

                        ((Node) event.getSource()).getScene().getWindow().hide();
                        Parent root = FXMLLoader.load(getClass().getResource("../employee/Employee.fxml"));
                        Scene scene = new Scene(root);
                        Stage stage = new Stage();
                        stage.setTitle("Klub fitness");
                        stage.setScene(scene);
                        stage.setResizable(false);
                        stage.show();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * Przekierowuje użytkownika/pracownika do strony rejestracji.
     * @param event
     */
    @FXML
    private void register(MouseEvent event) throws IOException {
        ((Node)event.getSource()).getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("../register/Register.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Klub fitness");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Implementuje metodę initialize.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {}
}
