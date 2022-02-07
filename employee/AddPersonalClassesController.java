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

public class AddPersonalClassesController implements Initializable {
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet res = null;

    @FXML
    private ComboBox<String> clientField;
    @FXML
    private ComboBox<String> roomField;
    @FXML
    private TextField dateField;
    @FXML
    private TextField priceField;
    @FXML
    private int clientId;
    @FXML
    private int roomId;

    /**
     *Ustawia wartość początkową pól.
     */
    public void setData(){
        try {
            con = BD.connect();
            clientField.getItems().clear();
            roomField.getItems().clear();
            String sql = "select imie,nazwisko, email from klient";
            pst = con.prepareStatement(sql);
            res = pst.executeQuery();
            while (res.next()) {
                clientField.getItems().add(res.getString("imie") +" " +  res.getString("nazwisko")
                        +" , email: " +  res.getString("email"));
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
     * W zależności od wartości pól clientField, roomField ustala wartość id klienta i sali.
     */
    private void setClientIdAndRoomIdField(){
        try{
            if(!clientField.getItems().isEmpty() && !roomField.getItems().isEmpty() ) {
                con = BD.connect();
                String sql = "select id_klient from klient where imie=? and nazwisko=? and email=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, (clientField.getSelectionModel().getSelectedItem()).split(" ")[0]);
                pst.setString(2, (clientField.getSelectionModel().getSelectedItem()).split(" ")[1]);
                pst.setString(3, (clientField.getSelectionModel().getSelectedItem()).split(" ")[4]);
                res = pst.executeQuery();
                while (res.next()) {
                    clientId = res.getInt("id_klient");
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
     * Dodaje zajęcia personalne do bazy danych. W przypadku niepowodzenia wyrzuca wyjątek.
     * @param clientId - parametr id klienta
     * @param roomId - parametr id sali
     * @param employeeId - parametr id ocenego pracownika/prowadzącego
     * @param date - termin zajęć
     * @param price - cena zajęć
     * @return
     */
    @FXML
    private boolean personalClassesCanBeAdded(int clientId, int roomId, int employeeId, String date, String price) {
        try {
            con = BD.connect();
            String sql = "Select zajecia_personalne_dodaj(?,?,?,?,?);";
            pst = con.prepareStatement(sql);
            pst.setInt(1, clientId);
            pst.setInt(2, roomId);
            pst.setInt(3, employeeId);
            pst.setString(4, date);
            pst.setBigDecimal(5, new BigDecimal(price));
            pst.execute();
            pst.close();
            con.close();
            return true;

        } catch (SQLException e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Bład");
            a.setHeaderText("Nie można dodać zajęć personalnych!");
            a.setContentText(e.getMessage().split("Where")[0]);
            a.showAndWait();
        } finally {
            BD.close(con,pst);
        }
        return false;
    }

    /**
     * Sprawdza walidaję pól w formularzu i wywołuje metodę dodającą zajęcia personalne do bazy danych.
     */
    public void addPersonalClasses() {
        setClientIdAndRoomIdField();
        if (Validate.checkExists(clientField.getSelectionModel().getSelectedItem()) && Validate.checkExists(roomField.getSelectionModel().getSelectedItem()) && Validate.checkDate(dateField) && Validate.checkPrice(priceField)) {
            if (personalClassesCanBeAdded(clientId, roomId, LoginController.acc, dateField.getText(), priceField.getText())) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Gratulacje!");
                a.setHeaderText("Dodano zajęcia personalne!");
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
