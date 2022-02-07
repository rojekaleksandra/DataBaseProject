package manage;
import database.BD;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class EquipmentAndRoomController implements Initializable {
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet res = null;

    @FXML
    private TableView<EquipmentAndRoom> roomAndEquipmentT;
    @FXML
    private TableColumn<EquipmentAndRoom, String> room;
    @FXML
    private TableColumn<EquipmentAndRoom, Integer> amountR;
    @FXML
    private TableColumn<EquipmentAndRoom, String> equipment;
    @FXML
    private TableColumn<EquipmentAndRoom, Integer> amount;

    /**
     * Ustawia wartość początkową pól.
     */
    private void setData() {
        room.setCellValueFactory(new PropertyValueFactory<>("room"));
        amountR.setCellValueFactory(new PropertyValueFactory<>("amountR"));
        equipment.setCellValueFactory(new PropertyValueFactory<>("equipment"));
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        ObservableList<EquipmentAndRoom> list = FXCollections.observableArrayList();
        try {
            con = BD.connect();
            String sql = "select sala.nazwa as sala, sala.ilosc_miejsc, sprzet.id_nazwa as sprzet, sprzet_sala.ilosc\n" +
                    "from sprzet_sala join sala using(id_sala)\n" +
                    "join sprzet using(id_sprzet);";
            pst = con.prepareStatement(sql);
            res = pst.executeQuery();
            while (res.next()) {
                list.add(new EquipmentAndRoom(res.getString("sala"), res.getInt("ilosc_miejsc"), res.getString("sprzet"), res.getInt("ilosc")));
            }
            res.close();
            pst.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        roomAndEquipmentT.setItems(list);
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


