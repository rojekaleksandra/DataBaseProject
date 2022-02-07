package employee;
/**
 * Klasa przechowująca informacje o zajęciach personalnych.
 * Zawiera konstruktor ustawiający wszystkie dane oraz gettery i settery dla wszystkich pól.
 */
public class PersonalClasses {
    String client;
    String room;
    String date;
    double price;

    public PersonalClasses(String client, String room, String date, double price) {
        this.client = client;
        this.room = room;
        this.date = date;
        this.price = price;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
