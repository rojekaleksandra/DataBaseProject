package manage;
import database.BD;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.util.ResourceBundle;

public class ClassesController implements Initializable {
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet res = null;

    @FXML
    private TableView<Classes> classesT;
    @FXML
    private TableColumn<Classes, String> category;
    @FXML
    private TableColumn<Classes, String> name;
    @FXML
    private TableColumn<Classes, String> description;

    /**
     * Ustawia wartość początkową pól.
     */
    private void setData() {
        category.setCellValueFactory(new PropertyValueFactory<>("category"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));

        ObservableList<Classes> list = FXCollections.observableArrayList();
        try {
            con = BD.connect();
            String sql = "select k.nazwa as kategoria, z.nazwa as nazwa, zo.opis as opis from zajecia z\n" +
                    "join kategoria k using(id_kategoria)\n" +
                    "join zajecia_opis zo using(id_opis);";
            pst = con.prepareStatement(sql);
            res = pst.executeQuery();
            while (res.next()) {
                list.add(new Classes(res.getString("kategoria"), res.getString("nazwa"), res.getString("opis")));
            }
            res.close();
            pst.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        classesT.setItems(list);
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
