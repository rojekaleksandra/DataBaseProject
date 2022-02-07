package employee;
import database.BD;
import login.LoginController;
import validator.Validate;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddOneClassController implements Initializable {
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet res = null;

    @FXML
    private ComboBox<String> classesField;
    @FXML
    private ComboBox<String> roomField;
    @FXML
    private TextField dateField;
    @FXML
    private TextField priceField;
    @FXML
    private int classesId;
    @FXML
    private int roomId;

    /**
     *Ustawia wartość początkową pól.
     */
    public void setData(){
        try {
            con = BD.connect();
            classesField.getItems().clear();
            roomField.getItems().clear();
            String sql = "select nazwa from zajecia";
            pst = con.prepareStatement(sql);
            res = pst.executeQuery();
            while (res.next()) {
                classesField.getItems().add(res.getString("nazwa"));
            }
            res.close();
            pst.close();

            sql = "select nazwa from sala";
            pst = con.prepareStatement(sql);
            res = pst.executeQuery();
            while (res.next()) {
                roomField.getItems().add(res.getString("nazwa"));
            }
            res.close();
            pst.close();
            con.close();

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * W zależności od pól classesField oraz roomField ustawia wartość id zajęć oraz sali.
     */
    private void setClassesIdAndRoomIdField(){
        try{
            if(!classesField.getItems().isEmpty() && !roomField.getItems().isEmpty() ) {
                con = BD.connect();
                String sql = "select id_zajecia from zajecia where nazwa=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, (classesField.getSelectionModel().getSelectedItem()));
                res = pst.executeQuery();
                while (res.next()) {
                    classesId = res.getInt("id_zajecia");
                }
                res.close();
                pst.close();
                sql = "select id_sala from sala where nazwa=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, (roomField.getSelectionModel().getSelectedItem()));
                res = pst.executeQuery();
                while (res.next()) {
                    roomId = res.getInt("id_sala");
                }
                res.close();
                pst.close();
                con.close();
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Dodaje pojedyńcze zajęcia do bazy danych. W przypadku niepowodzenia wyrzuca wyjątek.
     * @param classesId - parametr id pojedyńczych zajęć
     * @param roomId - parametr id sali
     * @param clientId - parametr id obecnego klienta
     * @param date - data zajęć
     * @param price - cena zajęć
     * @return boolean
     */
    @FXML
    private boolean classesCanBeAdded(int classesId, int roomId, int clientId, String date, String price) {
        try {
            con = BD.connect();
            String sql = "Select zajpoj_dodaj(?,?,?,?,?);";
            pst = con.prepareStatement(sql);
            pst.setInt(1, classesId);
            pst.setInt(2, roomId);
            pst.setInt(3, clientId);
            pst.setString(4, date);
            pst.setBigDecimal(5, new BigDecimal(price));
            pst.execute();
            pst.close();
            con.close();
            return true;

        } catch (SQLException e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Bład");
            a.setHeaderText("Nie można dodać zajęć!");
            a.setContentText(e.getMessage().split("Where")[0]);
            a.showAndWait();
        } finally {
            BD.close(con,pst);
        }
        return false;
    }

    /**
     * Sprawdza walidaję pól w formularzu i wywołuje metodę dodającą pojedyńcze zajęcia do bazy danych.
     */
    @FXML
    private void addClasses() {
        setClassesIdAndRoomIdField();
        if (Validate.checkExists(classesField.getSelectionModel().getSelectedItem()) && Validate.checkExists(roomField.getSelectionModel().getSelectedItem()) && Validate.checkDate(dateField) && Validate.checkPrice(priceField)) {
            if (classesCanBeAdded(classesId, roomId, LoginController.acc, dateField.getText(), priceField.getText())) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Gratulacje!");
                a.setHeaderText("Dodano zajęcia!");
                a.showAndWait();
            }
        }
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
