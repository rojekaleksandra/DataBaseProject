package client;
import database.BD;
import login.LoginController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.net.URL;
import java.util.ResourceBundle;

public class ReservationsController implements Initializable {
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet res = null;

    @FXML
    private TableView<Reservation> reservationT;
    @FXML
    private TableColumn<Reservation, String> category;
    @FXML
    private TableColumn<Reservation, String> name;
    @FXML
    private TableColumn<Reservation, String> coach;
    @FXML
    private TableColumn<Reservation, String> room;
    @FXML
    private TableColumn<Reservation, String> date;
    @FXML
    private TableColumn<Reservation, Double> price;
    @FXML
    private TableColumn<Reservation, String> status;
    @FXML
    private int pzajId;

    /**
     *Ustawia wartość początkową pól. Na ekran główny wyświetla informacje o rezerwacjach.
     */
    private void setData() {
        category.setCellValueFactory(new PropertyValueFactory<>("category"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        coach.setCellValueFactory(new PropertyValueFactory<>("coach"));
        room.setCellValueFactory(new PropertyValueFactory<>("room"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));

        ObservableList<Reservation> list = FXCollections.observableArrayList();
        try{
            con = BD.connect();
            String sql = "select * from rezerwacja_info join klient using(id_klient) where id_klient = ?;";
            pst = con.prepareStatement(sql);
            pst.setInt(1, LoginController.acc);
            res = pst.executeQuery();
            while(res.next()) {
                list.add(new Reservation(res.getString("kategoria"), res.getString("nazwa"), res.getString("imie") + " " + res.getString("nazwisko"), res.getString("sala"),  res.getString("termin"),  res.getDouble("cena"), res.getString("status")));
            }
            res.close();
            pst.close();
            con.close();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        reservationT.setItems(list);
    }

    /**
     * Aktualizuje status rezerwacji w bazie danych. W przypadku niepowodzenia wyrzuca wyjątek.
     * @return boolean
     */
    @FXML
    private boolean reservationCanBeUpdated() {
        try {
            if(reservationT.getSelectionModel().getSelectedItem() == null) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Bład");
                a.setHeaderText("Nie można potwierdzić!");
                a.setContentText("Nie zaznaczono rezerwacji!");
                a.showAndWait();
                return false;
            }
            if(reservationT.getSelectionModel().getSelectedItem().getStatus().equals("potwierdzono")) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Bład");
                a.setHeaderText("Nie można potwierdzić!");
                a.setContentText("Rezerwacja już jest potwierdzona!");
                a.showAndWait();
                return false;
            }
            con = BD.connect();
            String sql = "select pojedyncze_zajecia.id_pzaj from pojedyncze_zajecia join prowadzacy using(id_prowadzacy)" +
                    " join zajecia using(id_zajecia) where zajecia.nazwa=? and prowadzacy.imie=? and prowadzacy.nazwisko=? and pojedyncze_zajecia.termin=?;";
            pst = con.prepareStatement(sql);
            pst.setString(1, reservationT.getSelectionModel().getSelectedItem().getName());
            pst.setString(2, reservationT.getSelectionModel().getSelectedItem().getCoach().split(" ")[0]);
            pst.setString(3, reservationT.getSelectionModel().getSelectedItem().getCoach().split(" ")[1]);
            pst.setString(4, reservationT.getSelectionModel().getSelectedItem().getDate());
            res = pst.executeQuery();
            while (res.next()) {
                pzajId = res.getInt("id_pzaj");
            }
            res.close();
            pst.close();

            sql = "update rezerwacja set status=? where id_klient=? and id_pzaj=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, "potwierdzono");
            pst.setInt(2, LoginController.acc);
            pst.setInt(3, pzajId);
            pst.execute();
            pst.close();
            con.close();
            return true;

        } catch (SQLException e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Bład");
            a.setHeaderText("Nie można potwierdzić!");
            a.setContentText(e.getMessage().split("Where")[0]);
            a.showAndWait();
        } finally {
            BD.close(con,pst);
        }
        return false;
    }

    /**
     * Wywołuje metodę aktualizującą status rezerwacji w bazie danych.
     */
    public void confirmReservation() {
        if (reservationCanBeUpdated()) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Gratulacje!");
                a.setHeaderText("Potwierdzono zajęcia!");
                a.showAndWait();
                setData();
        }
    }

    /**
     * Zapisuje raport rezerwacji klienta do pliku.
     */
    @FXML
    private void saveReport(){
        try{
            Path pathRes =  Paths.get("Rezerwacje_klient_raport.txt");
            BufferedWriter writer = Files.newBufferedWriter(pathRes, StandardOpenOption.CREATE);
            con = BD.connect();
            writer.write("Raport rezerwacji dla klienta: \n\n");
            String sql = "select * from rezniep_klient_report where id_klient=?;";
            pst = con.prepareStatement(sql);
            pst.setInt(1, LoginController.acc);
            res = pst.executeQuery();
            while(res.next()) {
                StringBuilder row = new StringBuilder();
                row.append("Klient: " + res.getString("imie"));
                row.append(" " + res.getString("nazwisko") + "\n");
                row.append("\nNiepotwierdzono:\n");
                row.append("Ilość rezerwacji niepotwierdzonych: " +res.getString("ilosc_rez") + "\n");
                row.append("Cena: " +res.getString("sum") + "\n\n");
                writer.write(row.toString());
            }
            res.close();
            pst.close();


            sql = "select * from rezpot_klient_report where id_klient=?;";
            pst = con.prepareStatement(sql);
            pst.setInt(1, LoginController.acc);
            res = pst.executeQuery();
            while(res.next()) {
                StringBuilder row = new StringBuilder();
                row.append("\nPotwierdzono:\n");
                row.append("Ilość rezerwacji potwierdzonych: " +res.getString("ilosc_rez") + "\n");
                row.append("Cena: " +res.getString("sum") + "\n\n");
                writer.write(row.toString());
            }
            res.close();
            pst.close();

            writer.write("\nMoje rezerwacje:\n\n");
            sql = "select * from rezerwacja_info where id_klient = ?;";
            pst = con.prepareStatement(sql);
            pst.setInt(1, LoginController.acc);
            res = pst.executeQuery();
            while(res.next()) {
                StringBuilder row = new StringBuilder();
                row.append("Kategoria: " +res.getString("kategoria") + "\n");
                row.append("Nazwa: " +res.getString("nazwa") + "\n");
                row.append("Prowadzący: " + res.getString("imie"));
                row.append(" " + res.getString("nazwisko") + "\n");
                row.append("Sala: " +res.getString("sala") + "\n");
                row.append("Termin: " +res.getString("termin") + "\n");
                row.append("Cena: " +res.getString("cena") + "\n");
                row.append("Status: " +res.getString("status") + "\n\n");
                writer.write(row.toString());
            }
            writer.close();
            res.close();
            pst.close();
            con.close();
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Gratulacje!");
            a.setHeaderText("Zapisano raport rezerwacji.");
            a.showAndWait();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally{
            BD.close(con, pst);
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