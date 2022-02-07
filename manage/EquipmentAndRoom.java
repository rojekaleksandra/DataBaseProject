package manage;
/**
 * Klasa przechowująca informacje o sprzęcie i salach.
 * Zawiera konstruktor ustawiający wszystkie dane oraz gettery i settery dla wszystkich pól.
 */
public class EquipmentAndRoom {
    String room;
    int amountR;
    String equipment;
    int amount;

    public EquipmentAndRoom(String room, int amountR, String equipment, int amount) {
        this.room = room;
        this.amountR=amountR;
        this.equipment = equipment;
        this.amount = amount;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmountR() {
        return amountR;
    }

    public void setAmountR(int amountR) {
        this.amountR = amountR;
    }
}
