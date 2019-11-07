import java.util.ArrayList;

public class User {
    private String name;
    private int health;
    private ArrayList<String> items;

    /**
     * Constructs User object for each adventurer
     * @param name
     * @param items
     */
    public User(String name, ArrayList<String> items) {
        this.name = name;
        this.health = 10;
        this.items = items;
    }

    public User(String name) {
        this(name, new ArrayList<>());
    }

    public String getName() {
        return this.name;
    }

    public int getHealth() {
      return this.health;
    }

    public void loseHealth(int damage) {
        this.health -= damage;
    }

    public void addItem(String item) {
        this.items.add(item);
    }

    public void putItem(String item) {
        this.items.remove(item);
    }

}
