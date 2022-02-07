package client;
/**
 * Klasa przechowująca informacje o zajęciach personalych.
 * Zawiera konstruktor ustawiający wszystkie dane oraz gettery i settery dla wszystkich pól.
 */
public class PersonalClasses {
    String coach;
    String room;
    String date;
    double price;

    public PersonalClasses(String coach, String room, String date, double price) {
        this.coach = coach;
        this.room = room;
        this.date = date;
        this.price = price;
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
}
