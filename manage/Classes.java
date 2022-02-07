package manage;
/**
 * Klasa przechowująca informacje o zajęciach.
 * Zawiera konstruktor ustawiający wszystkie dane oraz gettery i settery dla wszystkich pól.
 */
public class Classes {
    String category;
    String name;
    String description;

    public Classes(String category, String name, String description) {
        this.category = category;
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
