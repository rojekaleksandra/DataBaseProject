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

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.net.URL;
import java.sql.ResultSetMetaData;
import java.util.ResourceBundle;

public class PersonalClassesController implements Initializable {
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet res = null;

    @FXML
    private TableView<PersonalClasses> personalClassesT;
    @FXML
    private TableColumn<PersonalClasses, String> coach;
    @FXML
    private TableColumn<PersonalClasses, String> room;
    @FXML
    private TableColumn<PersonalClasses, String> date;
    @FXML
    private TableColumn<PersonalClasses, Double> price;

    /**
     *Ustawia wartość początkową pól. Na ekran główny wyświetla informacje o zajeciach pwersonalnych.
     */
    private void setData() {
        coach.setCellValueFactory(new PropertyValueFactory<>("coach"));
        room.setCellValueFactory(new PropertyValueFactory<>("room"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));

        ObservableList<PersonalClasses> list = FXCollections.observableArrayList();
        try{
            con = BD.connect();
            String sql = "select * from zajpersonalne_info where id_klient = ?;";
            pst = con.prepareStatement(sql);
            pst.setInt(1, LoginController.acc);
            res = pst.executeQuery();
            while(res.next()) {
                list.add(new PersonalClasses(res.getString("pimie") + " " + res.getString("pnazwisko"), res.getString("sala"),  res.getString("termin"),  res.getDouble("cena")));
            }
            res.close();
            pst.close();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally{
            BD.close(con, pst);
        }
        personalClassesT.setItems(list);
    }

    /**
     * Zapisuje raport zajęć personalnych klienta do pliku.
     */
    @FXML
    private void saveReport(){
        try{
            Path pathRes =  Paths.get("Zajpers_klient_raport.txt");
            BufferedWriter writer = Files.newBufferedWriter(pathRes, StandardOpenOption.CREATE);
            con = BD.connect();
            writer.write("Raport zajęcia personalne dla klienta: \n\n");
            String sql = "select * from zajpers_klient_report where id_klient = ?;";
            pst = con.prepareStatement(sql);
            pst.setInt(1, LoginController.acc);
            res = pst.executeQuery();
            while(res.next()) {
                StringBuilder row = new StringBuilder();
                row.append("Klient: " + res.getString("imie"));
                row.append(" " + res.getString("nazwisko") + "\n");
                row.append("Ilość zajęć personalnych: " +res.getString("ilosc_zajec") + "\n");
                row.append("Cena za wszystkie zajęcia: " +res.getString("cena") + "\n\n");
                writer.write(row.toString());
            }
            res.close();
            pst.close();
            writer.write("\nMoje zajęcia personalne:\n\n");
            sql = "select * from zajpersonalne_info where id_klient = ?;";
            pst = con.prepareStatement(sql);
            pst.setInt(1, LoginController.acc);
            res = pst.executeQuery();
            while(res.next()) {
                StringBuilder row = new StringBuilder();
                    row.append("Prowadzący: " + res.getString("pimie"));
                    row.append(" " + res.getString("pnazwisko") + "\n");
                    row.append("Sala: " +res.getString("sala") + "\n");
                    row.append("Termin: " +res.getString("termin") + "\n");
                    row.append("Cena: " +res.getString("cena") + "\n\n");
                writer.write(row.toString());
            }
            writer.close();
            res.close();
            pst.close();
            con.close();
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Gratulacje!");
            a.setHeaderText("Zapisano raport zajęć personalnych.");
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
