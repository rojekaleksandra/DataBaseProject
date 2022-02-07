package client;
import database.BD;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import login.LoginController;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class DataController implements Initializable {
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet res = null;
    @FXML
    private Label name;
    @FXML
    private Label surname;
    @FXML
    private Label phoneNumber;
    @FXML
    private Label email;

    /**
     *Ustawia wartość początkową pól.
     */
    private void setData() {
        try {
            con = BD.connect();
            String sql = "SELECT * from klient where id_klient="+LoginController.acc+";";
            pst = con.prepareStatement(sql);
            res = pst.executeQuery();

            if(res.next()) {
                name.setText(res.getString("imie"));
                surname.setText(res.getString("nazwisko"));
                phoneNumber.setText(res.getString("tel"));
                email.setText(res.getString("email"));
            }
            res.close();
            pst.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
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

