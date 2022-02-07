package employee;
/**
 * Klasa przechowująca informacje o pojedyńczych zajęciach.
 * Zawiera konstruktor ustawiający wszystkie dane oraz gettery i settery dla wszystkich pól.
 */
public class OneClass {
    String category;
    String name;
    String room;
    String date;

    public OneClass(String category, String name, String room, String date) {
        this.category = category;
        this.name = name;
        this.room = room;
        this.date = date;
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
}


