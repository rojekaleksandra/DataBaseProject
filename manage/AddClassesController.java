package manage;
import database.BD;
import validator.Validate;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.sql.Connection;

public class AddClassesController implements Initializable {
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet res = null;

    @FXML
    private TextField categoryField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private ComboBox<String> categoryCB;
    @FXML
    private ComboBox<String> descriptionCB;
    @FXML
    private TextField nameField;
    @FXML
    private int categoryId;
    @FXML
    private int descriptionId;

    /**
     * Ustawia wartość początkową pól.
     */
    public void setData(){
        setCategoryCB();
        setDescriptionCB();
    }

    /**
     * Ustawia wartość pola categoryCB.
     */
    private void setCategoryCB() {
        try {
            con = BD.connect();
            categoryCB.getItems().clear();
            String sql = "select nazwa from kategoria";
            pst = con.prepareStatement(sql);
            res = pst.executeQuery();
            while (res.next()) {
                categoryCB.getItems().add(res.getString("nazwa"));
            }
            res.close();
            pst.close();
            con.close();

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Ustawia wartość pola descriptionCB.
     */
    private void setDescriptionCB(){
        try{
            con = BD.connect();
            descriptionCB.getItems().clear();
            String sql = "select opis from zajecia_opis" ;
            pst = con.prepareStatement(sql);
            res = pst.executeQuery();
            while (res.next()) {
                descriptionCB.getItems().add(res.getString("opis"));
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
     * W zależności od pól categoryCB oraz descriptionCB ustawia wartość id kategorii oraz opisu.
     */
    private void setCategoryAndDescIdField(){
        try{
            if(!categoryCB.getItems().isEmpty() && !descriptionCB.getItems().isEmpty()) {
                con = BD.connect();
                String sql = "select id_kategoria from kategoria where nazwa = ?;";
                pst = con.prepareStatement(sql);
                pst.setString(1, categoryCB.getSelectionModel().getSelectedItem());
                res = pst.executeQuery();
                while (res.next()) {
                    categoryId = res.getInt("id_kategoria");
                }
                res.close();
                pst.close();

                sql = "select id_opis from zajecia_opis where opis = ?;";
                pst = con.prepareStatement(sql);
                pst.setString(1, descriptionCB.getSelectionModel().getSelectedItem());
                res = pst.executeQuery();
                while (res.next()) {
                    descriptionId = res.getInt("id_opis");
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
     * Dodaje kategorię do bazy danych. W przypadku niepowodzenia wyrzuca wyjątek.
     * @param categoryField - nazwa kategorii
     * @return boolean
     */
    @FXML
    private boolean categoryCanBeAdded(String categoryField) {
        try {
            con = BD.connect();
            String sql = "Select kategoria_dodaj(?);";
            pst = con.prepareStatement(sql);
            pst.setString(1, categoryField);
            pst.execute();
            pst.close();
            con.close();
            return true;

        } catch (SQLException e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Bład");
            a.setHeaderText("Nie można dodać kategorii!");
            a.setContentText(e.getMessage().split("Where")[0]);
            a.showAndWait();
        } finally {
            BD.close(con,pst);
        }
        return false;
    }

    /**
     * Sprawdza walidaję pól w formularzu i wywołuje metodę dodającą kategorię do bazy danych.
     */
    @FXML
    private void addCategory() {
        if(Validate.checkCategory(categoryField)) {
            if (categoryCanBeAdded(categoryField.getText())) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Gratulacje!");
                a.setHeaderText("Dodano kategorię!");
                a.showAndWait();
                setCategoryCB();
            }
        }
    }

    /**
     * Dodaje opis zajęć do bazy danych. W przypadku niepowodzenia wyrzuca wyjątek.
     * @param descriptionField - opis zajęć
     * @return boolean
     */
    @FXML
    private boolean descriptionCanBeAdded(String descriptionField) {
        try {
            con = BD.connect();
            String sql = "Select opis_dodaj(?);";
            pst = con.prepareStatement(sql);
            pst.setString(1, descriptionField);
            pst.execute();
            pst.close();
            con.close();
            return true;

        } catch (SQLException e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Bład");
            a.setHeaderText("Nie można dodać opisu zajęć!");
            a.setContentText(e.getMessage().split("Where")[0]);
            a.showAndWait();
        } finally {
            BD.close(con,pst);
        }
        return false;
    }

    /**
     * Sprawdza walidaję pól w formularzu i wywołuje metodę dodającą opis zajęć do bazy danych.
     */
    @FXML
    private void addDescription() {
        if(Validate.checkExists(descriptionField.getText())) {
            if (descriptionCanBeAdded(descriptionField.getText())) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Gratulacje!");
                a.setHeaderText("Dodano opis zajęć!");
                a.showAndWait();
            }
        }
        setDescriptionCB();
    }

    /**
     * Dodaje zajęcia do bazy danych. W przypadku niepowodzenia wyrzuca wyjątek.
     * @param descriptionId - parametr id opisu zajęć
     * @param categoryId - parametr id kategorii
     * @param nameField - nazwa zajęć
     * @return boolean
     */
    @FXML
    private boolean classesCanBeAdded(int descriptionId, int categoryId, String nameField) {
        try {
            con = BD.connect();
            String sql = "Select zajecia_dodaj(?,?,?);";
            pst = con.prepareStatement(sql);
            pst.setInt(1, descriptionId);
            pst.setInt(2, categoryId);
            pst.setString(3, nameField);
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
     * Sprawdza walidaję pól w formularzu i wywołuje metodę dodającą zajęcia do bazy danych.
     */
    @FXML
    private void addClasses() {
        setCategoryAndDescIdField();
        if(Validate.checkExists(categoryCB.getSelectionModel().getSelectedItem()) && Validate.checkName(nameField) && Validate.checkExists(descriptionCB.getSelectionModel().getSelectedItem())) {
            if (classesCanBeAdded(descriptionId, categoryId, nameField.getText())) {
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
