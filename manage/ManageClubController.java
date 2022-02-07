package manage;
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

public class ManageClubController implements Initializable {
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet res = null;
    @FXML
    private Label nameLabel;
    @FXML
    private StackPane center;

    /**
     * Ustawia wartość początkową pól. Przechodzi do strony widoku zajęć.
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

            Parent fxml = FXMLLoader.load(getClass().getResource("Classes.fxml"));
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
     * Wychodzi ze strony zarządzania klubem. Przechodzi do widoku pracownika.
     * @param event
     * @throws IOException
     */
    public void close(MouseEvent event) throws IOException {
        ((Node)event.getSource()).getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("../employee/Employee.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Klub fitness");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Przekierowuje pracownika do strony Zajęcia w klubie.
     * @throws IOException
     */
    @FXML
    private void classes() throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("Classes.fxml"));
        center.getChildren().clear();
        center.getChildren().addAll(fxml);
    }

    /**
     * Przekierowuje pracownika do strony Zarządzaj zajęciami.
     * @throws IOException
     */
    @FXML
    private void addClasses() throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("AddClasses.fxml"));
        center.getChildren().clear();
        center.getChildren().addAll(fxml);
    }

    /**
     * Przekierowuje pracownika do strony Sale wraz ze sprzętem w klubie.
     * @throws IOException
     */
    @FXML
    private void equipmentAndRoom() throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("EquipmentAndRoom.fxml"));
        center.getChildren().clear();
        center.getChildren().addAll(fxml);
    }

    /**
     * Przekierowuje pracownika do strony Zarządzaj salami i sprzętem.
     * @throws IOException
     */
    @FXML
    private void addEquipmentAndRoom(MouseEvent event) throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("AddEquipmentAndRoom.fxml"));
        center.getChildren().clear();
        center.getChildren().addAll(fxml);
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
