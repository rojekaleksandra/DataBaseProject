package register;
import validator.Validate;
import database.BD;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.sql.Connection;


public class RegisterController implements Initializable {
    Connection con = null;
    PreparedStatement pst = null;
    @FXML
    private TextField nameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passField;
    @FXML
    private RadioButton userButton;
    @FXML
    private RadioButton employeeButton;

    /**
     * Dodaje użytkownika do bazy danych. W przypadku niepowodzenia wyrzuca wyjątek.
     * @param name - imię użytkownika
     * @param surname - nazwisko użytkownika
     * @param number - numer tel użytkownika
     * @param email - email użytkownika
     * @param login - login użytkownika
     * @param pass - hasło użytkownika
     * @return
     */
    public boolean userCanBeAdded(String name, String surname, String number, String email, String login, String pass) {
        try {
            con = BD.connect();
            String sql = "Select klient_zarejestruj(?,?,?,?,?,?);";
            pst = con.prepareStatement(sql);
            pst.setString(1, name);
            pst.setString(2,surname);
            pst.setString(3,number);
            pst.setString(4,email);
            pst.setString(5,login);
            pst.setString(6,pass);

            pst.execute();

            pst.close();
            con.close();
            return true;

        } catch (SQLException e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Bład");
            a.setHeaderText("Użytkownik niezarejestrowany! Spróbuj jeszcze raz.");
            a.setContentText(e.getMessage().split("Where")[0]);
            a.showAndWait();
        } finally {
            BD.close(con,pst);
        }
        return false;
    }

    /**
     * Dodaje pracownika do bazy danych. W przypadku niepowodzenia wyrzuca wyjątek.
     * @param name - imię pracownika
     * @param surname - nazwisko pracownika
     * @param number - numer tel pracownika
     * @param email - email pracownika
     * @param login - login pracownika
     * @param pass - hasło pracownika
     * @return
     */
    public boolean employeeCanBeAdded(String name, String surname, String number, String email, String login, String pass) {
        try {
            con = BD.connect();
            String sql = "Select prowadzacy_zarejestruj(?,?,?,?,?,?);";
            pst = con.prepareStatement(sql);
            pst.setString(1, name);
            pst.setString(2,surname);
            pst.setString(3,number);
            pst.setString(4,email);
            pst.setString(5,login);
            pst.setString(6,pass);

            pst.execute();

            pst.close();
            con.close();
            return true;

        } catch (SQLException e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Bład");
            a.setHeaderText("Pracownik niezarejestrowany! Spróbuj jeszcze raz.");
            a.setContentText(e.getMessage().split("Where")[0]);
            a.showAndWait();
        } finally {
            BD.close(con,pst);
        }
        return false;
    }

    /**
     * Sprawdza walidaję pól w formularzu i wywołuje metodę użytkownika/pracownika do bazy danych.
     * Dodatkowo w przypadku rejestracji pracownika wymaga podania pinu w celu autoryzacji.
     */
    public void register(MouseEvent event) throws IOException {
        String name = nameField.getText();
        String surname = surnameField.getText();
        String phoneNumb = phoneNumberField.getText();
        String email = emailField.getText();
        String login = loginField.getText();
        String pass = passField.getText();
        if (Validate.checkNameField(nameField) && Validate.checkSurnameField(surnameField) && Validate.checkNumberField(phoneNumberField) && Validate.checkEmailField(emailField) && Validate.checkLogin(loginField) && Validate.checkPass(passField)) {
            if(userButton.isSelected()){
                if (userCanBeAdded(name, surname, phoneNumb, email, login, pass)) {
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setTitle("Gratulacje!");
                    a.setHeaderText("Użytkownik zarejestrowany.");
                    a.showAndWait();
                    ((Node)event.getSource()).getScene().getWindow().hide();
                    Parent root = FXMLLoader.load(getClass().getResource("../login/Login.fxml"));
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setTitle("Klub fitness");
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.show();
                }
            }else if(employeeButton.isSelected()){
                TextInputDialog dialog = new TextInputDialog("Pin");
                dialog.setTitle("Confirmation");
                dialog.setHeaderText("Enter pin:");
                dialog.setContentText("Pin:");
                Optional<String> result = dialog.showAndWait();
                if(result.isPresent()) {
                    if (result.get().equals("1234")) {
                        if (employeeCanBeAdded(name, surname, phoneNumb, email, login, pass)) {
                            Alert a = new Alert(Alert.AlertType.INFORMATION);
                            a.setTitle("Gratulacje!");
                            a.setHeaderText("Pracownik zarejestrowany.");
                            a.showAndWait();
                            ((Node) event.getSource()).getScene().getWindow().hide();
                            Parent root = FXMLLoader.load(getClass().getResource("../login/Login.fxml"));
                            Scene scene = new Scene(root);
                            Stage stage = new Stage();
                            stage.setTitle("Klub fitness");
                            stage.setScene(scene);
                            stage.setResizable(false);
                            stage.show();
                        }
                    } else {
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setTitle("Błąd!");
                        a.setHeaderText("Nie można zarejestrować pracownika. Pin niezgodny");
                        a.showAndWait();
                    }
                }
            }
        }
    }

    /**
     * Przekierowuje użytkownika/pracownika do strony logowania.
     * @param event
     */
    @FXML
    private void login(MouseEvent event) throws IOException {
        ((Node)event.getSource()).getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("../login/Login.fxml"));
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