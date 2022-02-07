package client;
/**
 * Klasa przechowująca informacje o rezerwacjach.
 * Zawiera konstruktor ustawiający wszystkie dane oraz gettery i settery dla wszystkich pól.
 */
public class Reservation {
    String category;
    String name;
    String coach;
    String room;
    String date;
    double price;
    String status;

    public Reservation(String category, String name, String coach, String room, String date, double price, String status) {
        this.category = category;
        this.name = name;
        this.coach = coach;
        this.room = room;
        this.date = date;
        this.price = price;
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoach() {
        return coach;
    }

    public void setCoach(String coach) {
        this.coach = coach;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
