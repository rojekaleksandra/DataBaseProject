package employee;
import database.BD;
import login.LoginController;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class OneClassController implements Initializable {
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet res = null;

    @FXML
    private TableView<OneClass> oneClassT;
    @FXML
    private TableColumn<OneClass, String> category;
    @FXML
    private TableColumn<OneClass, String> name;
    @FXML
    private TableColumn<OneClass, String> room;
    @FXML
    private TableColumn<OneClass, String> date;

    /**
     * Ustawia wartość początkową pól.
     */
    private void setData() {
        category.setCellValueFactory(new PropertyValueFactory<>("category"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        room.setCellValueFactory(new PropertyValueFactory<>("room"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));

        ObservableList<OneClass> list = FXCollections.observableArrayList();
        try {
            con = BD.connect();
            String sql = "select * from pojzaj_info where id_prowadzacy= ?;";
            pst = con.prepareStatement(sql);
            pst.setInt(1, LoginController.acc);
            res = pst.executeQuery();
            while (res.next()) {
                list.add(new OneClass(res.getString("kategoria"), res.getString("nazwa"), res.getString("sala"), res.getString("termin")));
            }
            res.close();
            pst.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        oneClassT.setItems(list);
    }

    /**
     * Zapisuje raport zajęć pracownika do pliku.
     */
    @FXML
    private void saveReport(){
        try{
            Path pathRes =  Paths.get("Zajecia_pracownik_raport.txt");
            BufferedWriter writer = Files.newBufferedWriter(pathRes, StandardOpenOption.CREATE);
            con = BD.connect();
            writer.write("Raport zajęcia dla pracownika: \n\n");
            String sql = "select * from zajecia_prowadzacy_report where id_prowadzacy=?;";
            pst = con.prepareStatement(sql);
            pst.setInt(1, LoginController.acc);
            res = pst.executeQuery();
            while(res.next()) {
                StringBuilder row = new StringBuilder();
                row.append("Prowadzący: " + res.getString("imie"));
                row.append(" " + res.getString("nazwisko") + "\n");
                row.append("Ilość zajęć: " +res.getString("ilosc_zajec") + "\n");
                row.append("Zarobek: " +res.getString("cena") + "\n\n");
                writer.write(row.toString());
            }
            res.close();
            pst.close();
            writer.write("\nMoje zajęcia:\n\n");
            sql = "select * from pojzaj_info where id_prowadzacy=?;";
            pst = con.prepareStatement(sql);
            pst.setInt(1, LoginController.acc);
            res = pst.executeQuery();
            while(res.next()) {
                StringBuilder row = new StringBuilder();
                row.append("Kategoria: " +res.getString("kategoria") + "\n");
                row.append("Nazwa: " +res.getString("nazwa") + "\n");
                row.append("Sala: " +res.getString("sala") + "\n");
                row.append("Termin: " +res.getString("termin") + "\n\n");
                writer.write(row.toString());
            }
            writer.close();
            res.close();
            pst.close();
            con.close();
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Gratulacje!");
            a.setHeaderText("Zapisano raport zajęć.");
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
