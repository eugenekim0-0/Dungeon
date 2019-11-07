public class Monster {
    private String kind;
    private int health;
    private int level;

    public Monster(String kind, int health, int level) {
        this.kind = kind;
        this.health = health;
        this.level = level;
    }

    public Monster(String kind) {
        this(kind, (int) (Math.random() * 50), (int) (Math.random() * 100));
    }

    public String getKind() {
        return this.kind;
    }

    public void loseHealth(int damage) {
        this.health -= damage;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int changed) { this.level = changed; }



}
