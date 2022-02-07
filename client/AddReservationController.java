package client;
import database.BD;
import login.LoginController;
import validator.Validate;

import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.net.URL;
import java.util.ResourceBundle;

public class AddReservationController implements Initializable {
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet res = null;

    @FXML
    private ComboBox<String> categoryField;
    @FXML
    private ComboBox<String> nameField;
    @FXML
    private ComboBox<String> coachField;
    @FXML
    private ComboBox<String> dateField;
    @FXML
    private int pzajId;

    /**
     *Ustawia wartość początkową pól. Ustawia listenery.
     */
    public void setData(){
        categoryField.valueProperty().addListener((ov, t, t1) -> setNameField());
        nameField.valueProperty().addListener((ov, t, t1) -> setCoachField());
        coachField.valueProperty().addListener((ov, t, t1) -> setDateField());
        dateField.valueProperty().addListener((ov, t, t1) -> setPzajIdField());

        setCategoryField();
    }

    /**
     * Ustawia wartość pola categoryField.
     */
    private void setCategoryField() {
        try {
            con = BD.connect();
            categoryField.getItems().clear();
            nameField.getItems().clear();
            coachField.getItems().clear();
            dateField.getItems().clear();
            pzajId = 0;
            String sql = "select nazwa from kategoria";
            pst = con.prepareStatement(sql);
            res = pst.executeQuery();
            while (res.next()) {
                categoryField.getItems().add(res.getString("nazwa"));
            }
            res.close();
            pst.close();
            con.close();

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Ustawia wartość pola nameField.
     */
    private void setNameField(){
        try{
            con = BD.connect();
            nameField.getItems().clear();
            coachField.getItems().clear();
            dateField.getItems().clear();
            pzajId = 0;
            String sql = "select zajecia.nazwa from zajecia join kategoria using(id_kategoria) where kategoria.nazwa= ?" ;
            pst = con.prepareStatement(sql);
            pst.setString(1,categoryField.getSelectionModel().getSelectedItem());
            res = pst.executeQuery();
            while (res.next()) {
                nameField.getItems().add(res.getString("nazwa"));
            }
            res.close();
            pst.close();
            con.close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Ustawia wartość pola coachField.
     */
    private void setCoachField(){
        try{
            if(!nameField.getItems().isEmpty()) {
                con = BD.connect();
                coachField.getItems().clear();
                dateField.getItems().clear();
                pzajId = 0;
                String sql = "select prowadzacy.imie, prowadzacy.nazwisko from prowadzacy join pojedyncze_zajecia using(id_prowadzacy) join zajecia using(id_zajecia) where zajecia.nazwa= ? group by prowadzacy.imie, prowadzacy.nazwisko";
                pst = con.prepareStatement(sql);
                pst.setString(1, nameField.getSelectionModel().getSelectedItem());
                res = pst.executeQuery();
                while (res.next()) {
                    coachField.getItems().add(res.getString("imie") + " " + res.getString("nazwisko"));
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
     * Ustawia wartość pola dateField.
     */
    private void setDateField(){
        try{
            if(!nameField.getItems().isEmpty() && !coachField.getItems().isEmpty()) {
                con = BD.connect();
                dateField.getItems().clear();
                pzajId = 0;
                String sql = "select pojedyncze_zajecia.termin from pojedyncze_zajecia join prowadzacy using(id_prowadzacy) " +
                        "join zajecia using(id_zajecia) where zajecia.nazwa=? and prowadzacy.imie=? and prowadzacy.nazwisko=?;";
                pst = con.prepareStatement(sql);
                pst.setString(1, nameField.getSelectionModel().getSelectedItem());
                pst.setString(2, (coachField.getSelectionModel().getSelectedItem()).split(" ")[0]);
                pst.setString(3, (coachField.getSelectionModel().getSelectedItem()).split(" ")[1]);
                res = pst.executeQuery();
                while (res.next()) {
                    dateField.getItems().add(res.getString("termin"));
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
     * W zależności od wartości pól nameField, coachField, dateField ustala wartość id pojedyńczych zajęć.
     */
    private void setPzajIdField(){
        try{
            if(!nameField.getItems().isEmpty() && !coachField.getItems().isEmpty() && !dateField.getItems().isEmpty()) {
                con = BD.connect();
                String sql = "select pojedyncze_zajecia.id_pzaj from pojedyncze_zajecia join prowadzacy using(id_prowadzacy)" +
                        " join zajecia using(id_zajecia) where zajecia.nazwa=? and prowadzacy.imie=? and prowadzacy.nazwisko=? and pojedyncze_zajecia.termin=?;";
                pst = con.prepareStatement(sql);
                pst.setString(1, nameField.getSelectionModel().getSelectedItem());
                pst.setString(2, (coachField.getSelectionModel().getSelectedItem()).split(" ")[0]);
                pst.setString(3, (coachField.getSelectionModel().getSelectedItem()).split(" ")[1]);
                pst.setString(4, dateField.getSelectionModel().getSelectedItem());
                res = pst.executeQuery();
                while (res.next()) {
                    pzajId = res.getInt("id_pzaj");
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
     * Dodaje rezerwację do bazy danych. W przypadku niepowodzenia wyrzuca wyjątek.
     * @param pzaj_Id - parametr id pojedyńczych zajęć
     * @param client_id - parametr id obecnego klienta
     * @param status - status rezerwacji
     * @return boolean
     */
    private boolean reservationCanBeAdded(int pzaj_Id, int client_id, String status) {
        try {
            con = BD.connect();
            String sql = "Select rezerwacja_dodaj(?,?,?);";
            pst = con.prepareStatement(sql);
            pst.setInt(1, client_id);
            pst.setInt(2, pzaj_Id);
            pst.setString(3,status);

            pst.execute();

            pst.close();
            con.close();
            return true;

        } catch (SQLException e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Bład");
            a.setHeaderText("Nie można dodać rezerwacji!");
            a.setContentText(e.getMessage().split("Where")[0]);
            a.showAndWait();
        } finally {
            BD.close(con,pst);
        }
        return false;
    }

    /**
     * Sprawdza walidaję pól w formularzu i wywołuje metodę dodającą rezerwację do bazy danych.
     */
    @FXML
    public void addReservation() {
        if(Validate.checkExists(categoryField.getSelectionModel().getSelectedItem()) && Validate.checkExists(nameField.getSelectionModel().getSelectedItem()) && Validate.checkExists(coachField.getSelectionModel().getSelectedItem()) && Validate.checkExists(dateField.getSelectionModel().getSelectedItem())) {
            if (reservationCanBeAdded(pzajId, LoginController.acc, "niepotwierdzono")) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Gratulacje!");
                a.setHeaderText("Dodano rezerwacje!");
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