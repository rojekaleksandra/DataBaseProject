package employee;
import database.BD;
import login.LoginController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable {
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet res = null;
    @FXML
    private Label nameLabel;
    @FXML
    private StackPane center;

    /**
     * Ustawia wartość początkową pól.
     */
    public void setData() {
        try {
            con = BD.connect();
            String sql = "select * from prowadzacy where id_prowadzacy=?;";
            pst = con.prepareStatement(sql);
            pst.setInt(1, LoginController.acc);
            res = pst.executeQuery();

            if(res.next()) {
                StringBuilder tmp = new StringBuilder();
                tmp.append(res.getString("imie")).append(" ").append(res.getString("nazwisko"));
                nameLabel.setText(tmp.toString());
            }
            res.close();
            pst.close();
            con.close();

            Parent fxml = FXMLLoader.load(getClass().getResource("Data.fxml"));
            center.getChildren().clear();
            center.getChildren().addAll(fxml);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Wylogowuje pracownika. Przechodzi do strony głównej logowania.
     * @param event
     * @throws IOException
     */
    public void logout(MouseEvent event) throws IOException {
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
     * Przekierowuje użytkownika do strony Moje dane.
     * @throws IOException
     */
    @FXML
    private void data() throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("Data.fxml"));
        center.getChildren().clear();
        center.getChildren().addAll(fxml);
    }

    /**
     * Przekierowuje użytkownika do strony Moje zajęcia.
     * @throws IOException
     */
    @FXML
    private void classes() throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("OneClass.fxml"));
        center.getChildren().clear();
        center.getChildren().addAll(fxml);
    }

    /**
     * Przekierowuje użytkownika do strony Dodaj zajęcia.
     * @throws IOException
     */
    @FXML
    private void addClasses() throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("AddOneClass.fxml"));
        center.getChildren().clear();
        center.getChildren().addAll(fxml);
    }

    /**
     * Przekierowuje użytkownika do strony Moje zajęcia personalne.
     * @throws IOException
     */
    @FXML
    private void personal_classes() throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("PersonalClasses.fxml"));
        center.getChildren().clear();
        center.getChildren().addAll(fxml);
    }

    /**
     * Przekierowuje użytkownika do strony Dodaj zajęcia personalne.
     * @throws IOException
     */
    @FXML
    private void addPersonalClasses() throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("AddPersonalClasses.fxml"));
        center.getChildren().clear();
        center.getChildren().addAll(fxml);
    }

    /**
     * Przekierowuje użytkownika do strony zarządzania klubem.
     * @throws IOException
     */
    @FXML
    private void manage(MouseEvent event) throws IOException {
        ((Node)event.getSource()).getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("../manage/ManageClub.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Klub fitness");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Implementuje metodę initialize. Wywołuje funkcję setData().
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setData();
    }
}
