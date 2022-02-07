package validator;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validate {

    /**
     * Sprawdza poprawność wprowadzanego imienia.
     * @param name - wprowadzane imię
     * @return boolean
     */
    public static boolean checkNameField(TextField name) {
        if (name.getText().equals("")) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Błąd");
            a.setHeaderText("Puste pole! Wprowadź swoje IMIĘ.");
            a.showAndWait();
            return false;
        } else {
            Pattern p = Pattern.compile("[a-zA-ZąćęłńóśżźĄĆĘŁŃÓŚŻŹ]+");
            Matcher m = p.matcher(name.getText());

            if(m.find() && m.group().equals(name.getText())) {
                return true;
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Błąd");
                a.setHeaderText("Pole IMIĘ może zawierać tylko litery.");
                a.showAndWait();
                return false;
            }
        }
    }

    /**
     * Sprawdza poprawność wprowadzanego nazwiska.
     * @param surname - wprowadzane nazwisko
     * @return boolean
     */
    public static boolean checkSurnameField(TextField surname) {
        if (surname.getText().equals("")) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Błąd");
            a.setHeaderText("Puste pole! Wprowadź swoje NAZWISKO.");
            a.showAndWait();
            return false;
        } else {
            Pattern p = Pattern.compile("(([a-zA-ZąćęłńóśżźĄĆĘŁŃÓŚŻŹ]+( |-)[a-zA-ZąćęłńóśżźĄĆĘŁŃÓŚŻŹ]+)|([a-zA-ZąćęłńóśżźĄĆĘŁŃÓŚŻŹ]+))");
            Matcher m = p.matcher(surname.getText());

            if(m.find() && m.group().equals(surname.getText())) {
                return true;
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Błąd");
                a.setHeaderText("Pole NAZWISKO może zawierać tylko litery, spację lub znak '-'.");
                a.showAndWait();
                return false;
            }
        }
    }

    /**
     * Sprawdza poprawność wprowadzanego emaila.
     * @param email - wprowadzany email
     * @return boolean
     */
    public static boolean checkEmailField(TextField email) {
        if (email.getText().equals("")) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Błąd");
            a.setHeaderText("Puste pole! Wprowadź swój EMAIL.");
            a.showAndWait();
            return false;
        }
        Pattern p = Pattern.compile("[a-ząćęłńóśżźĄĆĘŁŃÓŚŻŹ0-9._%+-]+@[a-ząćęłńóśżźĄĆĘŁŃÓŚŻŹ0-9.-]+\\.[a-z]{2,4}");
        Matcher m = p.matcher(email.getText());

        if(m.find() && m.group().equals(email.getText())) {
            return true;
        } else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Błąd");
            a.setHeaderText("Twój EMAIL jest nieprawidłowy.");
            a.showAndWait();
            return false;
        }
    }

    /**
     * Sprawdza poprawność wprowadzanego numeru tel.
     * @param number - wprowadzany tel
     * @return boolean
     */
    public static boolean checkNumberField(TextField number) {
        Pattern p = Pattern.compile("[0-9]+");
        Matcher m = p.matcher(number.getText());

        if(m.find() && m.group().equals(number.getText()) && number.getText().length() == 9) {
            return true;
        } else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Błąd");
            a.setHeaderText("Pole NUMER TELEFONU musi zawierać 9 cyfr.");
            a.showAndWait();
            return false;
        }
    }

    /**
     * Sprawdza poprawność wprowadzanego loginu.
     * @param login - wprowadzany login
     * @return boolean
     */
    public static boolean checkLogin(TextField login) {
        if (login.getText().equals("") || login.getText().length() <5) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Błąd");
            a.setHeaderText("Pole LOGIN musi zawierać conajmniej 5 znaków.");
            a.showAndWait();
            return false;
        }
        return true;
    }

    /**
     * Sprawdza poprawność wprowadzanego hasła.
     * @param password - wprowadzane hasło
     * @return boolean
     */
    public static boolean checkPass(PasswordField password) {
        if (password.getText().equals("") || password.getText().length() <5) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Błąd");
            a.setHeaderText("Pole HASŁO musi zawierać conajmniej 5 znaków.");
            a.showAndWait();
            return false;
        }
        return true;
    }

    /**
     * Sprawdza poprawność wprowadzanej daty.
     * @param date - wprowadzana data
     * @return boolean
     */
    public static boolean checkDate(TextField date) {
        if (date.getText().equals("")) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Błąd");
            a.setHeaderText("Puste pole! Wprowadź DATĘ.");
            a.showAndWait();
            return false;
        }
        Pattern p = Pattern.compile("([123]0|[012][1-9]|31)\\.(0[1-9]|1[012])\\.(202[2-9]{1}) ([01][0-9]|2[0-3]):([0-5][0-9])");
        Matcher m = p.matcher(date.getText());

        if(m.find() && m.group().equals(date.getText())) {
            return true;
        } else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Błąd");
            a.setHeaderText("DATA jest nieprawidłowa. Format daty i godziny: DD.MM.YYYY HH:MM");
            a.showAndWait();
            return false;
        }
    }

    /**
     * Sprawdza poprawność wprowadzanej ceny.
     * @param price - wprowadzana cena
     * @return boolean
     */
    public static boolean checkPrice(TextField price) {
        if (price.getText().equals("")) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Błąd");
            a.setHeaderText("Puste pole! Wprowadź CENĘ.");
            a.showAndWait();
            return false;
        }
        Pattern p = Pattern.compile("^(([1-9])|([1-9][0-9]{1,2})|([1-9]\\.(0|00|0[1-9])|([1-9][0-9]))|([1-9][0-9]{1,2}\\.(0|00|0[1-9]|[1-9][0-9])))$");
        Matcher m = p.matcher(price.getText());

        if(m.find() && m.group().equals(price.getText())) {
            return true;
        } else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Błąd");
            a.setHeaderText("CENA jest nieprawidłowa. Podaj cenę 1.00-999.00");
            a.showAndWait();
            return false;
        }
    }

    /**
     * Sprawdza czy dane pole nie jest nullem, bądź nie jest puste.
     * @param field - wprowadzana pole
     * @return boolean
     */
    public static boolean checkExists(String field){
        if (field == null || field.equals("")) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Błąd");
            a.setHeaderText("Puste pole! Wprowadź wszystkie pola.");
            a.showAndWait();
            return false;
        }
        return true;
    }

    /**
     * Sprawdza poprawność wprowadzanej kategorii.
     * @param category - wprowadzana kategoria
     * @return boolean
     */
    public static boolean checkCategory(TextField category) {
        if (category.getText().equals("")) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Błąd");
            a.setHeaderText("Puste pole! Wprowadź KATEGORIĘ.");
            a.showAndWait();
            return false;
        } else {
            Pattern p = Pattern.compile("[a-zA-ZąćęłńóśżźĄĆĘŁŃÓŚŻŹ]+");
            Matcher m = p.matcher(category.getText());

            if(m.find() && m.group().equals(category.getText())) {
                return true;
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Błąd");
                a.setHeaderText("Pole KATEGORIA może zawierać tylko litery.");
                a.showAndWait();
                return false;
            }
        }
    }

    /**
     * Sprawdza poprawność wprowadzanej nazwy.
     * @param name - wprowadzana nazwa
     * @return boolean
     */
    public static boolean checkName(TextField name) {
        if (name.getText().equals("")) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Błąd");
            a.setHeaderText("Puste pole! Wprowadź NAZWĘ.");
            a.showAndWait();
            return false;
        } else {
            Pattern p = Pattern.compile("[a-zA-ZąćęłńóśżźĄĆĘŁŃÓŚŻŹ]+");
            Matcher m = p.matcher(name.getText());

            if(m.find() && m.group().equals(name.getText())) {
                return true;
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Błąd");
                a.setHeaderText("Pole NAZWA może zawierać tylko litery.");
                a.showAndWait();
                return false;
            }
        }
    }

    /**
     * Sprawdza poprawność wprowadzanej nazwy sali.
     * @param room - wprowadzana nazwa sali
     * @return boolean
     */
    public static boolean checkRoom(TextField room) {
        if (room.getText().equals("")) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Błąd");
            a.setHeaderText("Puste pole! Wprowadź SALĘ.");
            a.showAndWait();
            return false;
        } else {
            Pattern p = Pattern.compile("[a-zA-Z1-9ąćęłńóśżźĄĆĘŁŃÓŚŻŹ ]+");
            Matcher m = p.matcher(room.getText());

            if(m.find() && m.group().equals(room.getText())) {
                return true;
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Błąd");
                a.setHeaderText("Pole SALA może zawierać tylko litery, liczby oraz spację.");
                a.showAndWait();
                return false;
            }
        }
    }

    /**
     * Sprawdza poprawność wprowadzanej nazwy sprzętu
     * @param equipment - wprowadzana nazwa sprzętu
     * @return boolean
     */
    public static boolean checkEquipment(TextField equipment) {
        if (equipment.getText().equals("")) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Błąd");
            a.setHeaderText("Puste pole! Wprowadź SPRZĘT.");
            a.showAndWait();
            return false;
        } else {
            Pattern p = Pattern.compile("[a-zA-Z0-9ąćęłńóśżźĄĆĘŁŃÓŚŻŹ ]+");
            Matcher m = p.matcher(equipment.getText());

            if(m.find() && m.group().equals(equipment.getText())) {
                return true;
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Błąd");
                a.setHeaderText("Pole SPRZĘT może zawierać tylko litery, cyfry oraz spację.");
                a.showAndWait();
                return false;
            }
        }
    }

    /**
     * Sprawdza poprawność wprowadzanej ilości miejsc na sali
     * @param amountR - wprowadzana ilość miejsc na sali
     * @return boolean
     */
    public static boolean checkAmountR(TextField amountR) {
        if (amountR.getText().equals("")) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Błąd");
            a.setHeaderText("Puste pole! Wprowadź ILOŚĆ MIEJSC W SALI.");
            a.showAndWait();
            return false;
        } else {
            Pattern p = Pattern.compile("[1-9]+");
            Matcher m = p.matcher(amountR.getText());

            if(m.find() && m.group().equals(amountR.getText())) {
                return true;
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Błąd");
                a.setHeaderText("Pole ILOŚĆ MIEJSC W SALI może zawierać tylko liczby.");
                a.showAndWait();
                return false;
            }
        }
    }

    /**
     * Sprawdza poprawność wprowadzanej ilości sprzętu
     * @param amount - wprowadzana ilości sprzętu
     * @return boolean
     */
    public static boolean checkAmount(TextField amount) {
        if (amount.getText().equals("")) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Błąd");
            a.setHeaderText("Puste pole! Wprowadź ILOŚĆ SPRZĘTU.");
            a.showAndWait();
            return false;
        } else {
            Pattern p = Pattern.compile("[1-9]+");
            Matcher m = p.matcher(amount.getText());

            if(m.find() && m.group().equals(amount.getText())) {
                return true;
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Błąd");
                a.setHeaderText("Pole ILOŚĆ SPRZĘTU może zawierać tylko liczby.");
                a.showAndWait();
                return false;
            }
        }
    }


}
