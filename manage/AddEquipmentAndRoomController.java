package manage;
import database.BD;
import validator.Validate;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.sql.Connection;

public class AddEquipmentAndRoomController implements Initializable {
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet res = null;

    @FXML
    private TextField roomField;
    @FXML
    private ComboBox<String> roomCB;
    @FXML
    private TextField amountRField;
    @FXML
    private TextField equipmentField;
    @FXML
    private ComboBox<String> equipmentCB;
    @FXML
    private TextField amountField;
    @FXML
    private int roomId;
    @FXML
    private int equipmentId;

    /**
     *Ustawia wartość początkową pól.
     */
    public void setData(){
        setRoomCB();
        setEquipmentCB();
    }

    /**
     * Ustawia wartość pola roomCB.
     */
    private void setRoomCB() {
        try {
            con = BD.connect();
            roomCB.getItems().clear();
            String sql = "select nazwa from sala";
            pst = con.prepareStatement(sql);
            res = pst.executeQuery();
            while (res.next()) {
                roomCB.getItems().add(res.getString("nazwa"));
            }
            res.close();
            pst.close();
            con.close();

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            BD.close(con,pst);
        }
    }

    /**
     * Ustawia wartość pola equipmentCB.
     */
    private void setEquipmentCB(){
        try{
            con = BD.connect();
            equipmentCB.getItems().clear();
            String sql = "select id_nazwa from sprzet" ;
            pst = con.prepareStatement(sql);
            res = pst.executeQuery();
            while (res.next()) {
                equipmentCB.getItems().add(res.getString("id_nazwa"));
            }
            res.close();
            pst.close();
            con.close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            BD.close(con,pst);
        }
    }

    /**
     * W zależności od pól roomCB oraz equipmentCB ustawia wartość id sali oraz sprzętu.
     */
    private void setRoomAndEquipmentId(){
        try{
            if(!roomCB.getItems().isEmpty() && !equipmentCB.getItems().isEmpty()) {
                con = BD.connect();
                String sql = "select id_sala from sala where nazwa = ?;";
                pst = con.prepareStatement(sql);
                pst.setString(1, roomCB.getSelectionModel().getSelectedItem());
                res = pst.executeQuery();
                while (res.next()) {
                    roomId = res.getInt("id_sala");
                }
                res.close();
                pst.close();

                sql = "select id_sprzet from sprzet where id_nazwa = ?;";
                pst = con.prepareStatement(sql);
                pst.setString(1, equipmentCB.getSelectionModel().getSelectedItem());
                res = pst.executeQuery();
                while (res.next()) {
                    equipmentId = res.getInt("id_sprzet");
                }
                res.close();
                pst.close();
                con.close();
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            BD.close(con,pst);
        }
    }

    /**
     * Dodaje salę do bazy danych. W przypadku niepowodzenia wyrzuca wyjątek.
     * @param room - nazwa sali
     * @param amountR - ilość miejsc w sali
     * @return boolean
     */
    @FXML
    private boolean roomCanBeAdded(String room, String amountR) {
        try {
            con = BD.connect();
            String sql = "Select sala_dodaj(?,?);";
            pst = con.prepareStatement(sql);
            pst.setString(1, room);
            pst.setInt(2, Integer.parseInt(amountR));
            pst.execute();
            pst.close();
            con.close();
            return true;

        } catch (SQLException e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Bład");
            a.setHeaderText("Nie można dodać sali!");
            a.setContentText(e.getMessage().split("Where")[0]);
            a.showAndWait();
        } finally {
            BD.close(con,pst);
        }
        return false;
    }

    /**
     * Sprawdza walidaję pól w formularzu i wywołuje metodę dodającą salę do bazy danych.
     */
    @FXML
    private void addRoom() {
        if(Validate.checkRoom(roomField) && Validate.checkAmountR(amountRField)) {
            if (roomCanBeAdded(roomField.getText(), amountRField.getText())) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Gratulacje!");
                a.setHeaderText("Dodano salę!");
                a.showAndWait();
                setRoomCB();
            }
        }
    }

    /**
     * Dodaje salę do bazy danych. W przypadku niepowodzenia wyrzuca wyjątek.
     * @param equipment - nazwa sprzętu
     * @return boolean
     */
    @FXML
    private boolean equipmentCanBeAdded(String equipment) {
        try {
            con = BD.connect();
            String sql = "Select sprzet_dodaj(?);";
            pst = con.prepareStatement(sql);
            pst.setString(1, equipment);
            pst.execute();
            pst.close();
            con.close();
            return true;

        } catch (SQLException e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Bład");
            a.setHeaderText("Nie można dodać sprzetu!");
            a.setContentText(e.getMessage().split("Where")[0]);
            a.showAndWait();
        } finally {
            BD.close(con,pst);
        }
        return false;
    }

    /**
     * Sprawdza walidaję pól w formularzu i wywołuje metodę dodającą sprzęt do bazy danych.
     */
    @FXML
    private void addEquipment() {
        if(Validate.checkEquipment(equipmentField)) {
            if (equipmentCanBeAdded(equipmentField.getText())) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Gratulacje!");
                a.setHeaderText("Dodano sprzęt!");
                a.showAndWait();
                setEquipmentCB();
            }
        }
    }

    /**
     * Przypisuje sprzęt wraz z ilością do sali w bazie danych. W przypadku niepowodzenia wyrzuca wyjątek.
     * @param equipmentId - parametr id sprzętu
     * @param roomId - parametr id sali
     * @param amount - ilość sprzętu
     * @return
     */
    @FXML
    private boolean roomAndEquipmentCanBeAdded(int equipmentId, int roomId, String amount) {
        try {
            con = BD.connect();
            String sql = "Select sprzetsala_dodaj(?,?,?);";
            pst = con.prepareStatement(sql);
            pst.setInt(1, equipmentId);
            pst.setInt(2, roomId);
            pst.setInt(3, Integer.parseInt(amount));
            pst.execute();
            pst.close();
            con.close();
            return true;

        } catch (SQLException e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Bład");
            a.setHeaderText("Nie można dodać sprzetu!");
            a.setContentText(e.getMessage().split("Where")[0]);
            a.showAndWait();
        } finally {
            BD.close(con,pst);
        }
        return false;
    }

    /**
     * Sprawdza walidaję pól w formularzu i wywołuje metodę dodającą sprzęt i salę do tabeli asocjacyjnej w bazie danych.
     */
    @FXML
    private void addEquipmentToRoom() {
        setRoomAndEquipmentId();
        if(Validate.checkExists(roomCB.getSelectionModel().getSelectedItem()) && Validate.checkExists(equipmentCB.getSelectionModel().getSelectedItem()) && Validate.checkAmount(amountField)) {
            if (roomAndEquipmentCanBeAdded(equipmentId, roomId, amountField.getText())) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Gratulacje!");
                a.setHeaderText("Dodano sprzęt do sali!");
                a.showAndWait();
                setEquipmentCB();
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
