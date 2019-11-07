import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;


/**
 * Race conditions:
 *  - overwritten or out of order messages in a room's chat history
 *  - users disappearing from the world or appearing in multiple rooms as users are moving
 *  - adjacent rooms of the current room may not be the same for everyone due to the randomness
 *  >>> One solution is thorough DataIndexedStringSet based off the users' names. This is achieved by implementing
 * a HashSet of all the adventurers in the world. The adventurers with a lower hashcode will take precedence in the chat
 * and entering / leaving the room. The users' hashcodes will be determined by converting each letter in the users' names to their
 * ASCII number equivalent which are then multiplied by the powers of 126. 126 is chosen as it is the highest number of the ASCII
 * characters that are printable. For example, bee = (98 x 126^2) + (101 x 126^1) + (101 x 126^0) = 1,568,675
 *
 * Number of users scaling up:
 * There will be more Room objects to compensate for the users. This will lead to a lot of computer memory being taken up,
 * then causing a decrease in performance of the world. One possible solution is to set a limit of how many rooms can be formed,
 * which will then go up if the number of users go beyond a set ratio of users to Rooms. This solution can be achieved with a
 * HashSet where each bucket would be the Room object itself.
 *
 * Interacting with multiple monsters at once:
 * It would be difficult to discern which monster should be dealt with first. This can be
 * resolved through the user interacting with higher level monsters before the others. The level of the monster
 * can be defined by an instance variable of the monster object
 */
public class Main {
    public static final String[] DESCRIBE = {"bare", "damp", "mossy", "dry", "cool", "dark"};
    public static final HashMap<String, Integer> DIRECTIONS = directions();
    //comPlayers serve as test users in this world. If real human users are in the world, the comPlayers can act as NPCs.
    public static User[] comPlayers = {new User("Emma"), new User("Olivia"), new User("Ava"),
                                       new User("Liam"), new User("Noah"), new User("James")};
    public static ArrayList<User> users = new ArrayList<>(Arrays.asList(comPlayers));
    public static final Monster[] MODS = {new Monster("Ghost"), new Monster("Dragon"),
                                          new Monster("Griffin"), new Monster("Basilisk"),
                                          new Monster("Ogre"), new Monster("Elf"),
                                          new Monster("Golem")};
    public static User player;
    public static Room current;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("--- WELCOME TO THE DUNGEON ---");
        System.out.println("> What is your name?");
        String name = input.nextLine();
        player = new User(name, new ArrayList<>());
        ArrayList<User> startRoom = new ArrayList<>();
        startRoom.add(player);
        current = new Room(0, DESCRIBE[0], startRoom, new ArrayList<>(), new Room[6], new ArrayList<>());
        users.add(player);
        System.out.println("> Greetings " + name);
        System.out.println("> To chat with others, type 'say' before your message");
        System.out.println("> To move onto next transparent room, type 'north', 'south', 'east', 'west', 'up', or 'down'");
        while (true) {
            playerAction(input,false);
            current.roomInfo(player);
        }
    }

    /**
     * Returns a HashMap corresponding Strings north, south, east, west, up, down with numbers 0 - 5 respectively
     * @return
     */
    public static HashMap<String, Integer> directions() {
        HashMap<String, Integer> adjacent = new HashMap<>();
        adjacent.put("north", 0);
        adjacent.put("south", 1);
        adjacent.put("east", 2);
        adjacent.put("west", 3);
        adjacent.put("up", 4);
        adjacent.put("down", 5);
        return adjacent;
    }

    /**
     * Instantiates the neighboring rooms of the current room
     * @return
     */
    public static void buildNeighborRooms() {
        int total = 0;
        for (int i = 0; i < 6; i++) {
            if (current.getNeighbor(i) == null) {
                int roomType = (int) Math.round(Math.random());
                int descNum = (int) (Math.random() * DESCRIBE.length);
                current.setNeighbor(i, new Room(roomType, DESCRIBE[descNum], roomUsers(), roomMonsters(), new Room[6], new ArrayList<>()));
                current.connectRooms(i, current.getNeighbor(i));
                total += roomType;
            }
        }
        if (total == 6) {
            int randomOpen = (int) (Math.random() * 6);
            current.getNeighbor(randomOpen).setRoomType(0);
        }
    }

    /**
     * Returns which users are in the current room
     * @return
     */
    public static ArrayList<User> roomUsers() {
        int occupantNum = (int) (Math.random() * (users.size() / 2));
        ArrayList<User> occupants = new ArrayList<>();
        for (int i = 0; i < occupantNum; i++) {
            int randName = (int) (Math.random() * (users.size() - 1));
            occupants.add(comPlayers[randName]);
        }
        return occupants;
    }

    /**
     * Returns which monsters are in the current room
     * @return
     */
    public static ArrayList<Monster> roomMonsters() {
        int monNum = (int) (Math.random() * (MODS.length / 2));
        ArrayList<Monster> monsters = new ArrayList<>();
        for (int i = 0; i < monNum; i++) {
            int randMon = (int) (Math.random() * (users.size() - 1));
            monsters.add(MODS[randMon]);
        }
        return monsters;
    }

    /**
     * Responds through the command prompt according to the player's commands within a room
     * @source: https://stackoverflow.com/questions/11607496/trim-a-string-in-java-to-get-first-word
     * @param input
     * @param moved
     */
    public static void playerAction(Scanner input, boolean moved) {
        buildNeighborRooms();
        while (!moved) {
            String full = input.nextLine();
            String[] splitCommand = full.split(" ");
            if (splitCommand[0].equals("say")) {
                current.addToChat(player.getName() + ":" + full.substring(3));
                current.showChat();
            } else if (DIRECTIONS.containsKey(full)) {
                int nextNum = DIRECTIONS.get(full);
                if (current.getNeighbor(nextNum).isSolid()) {
                    System.out.println("The " + full + " room is solid. You can't move in here");
                } else {
                    current.userRoomChange(current.getNeighbor(nextNum), player);
                    current = current.getNeighbor(nextNum);
                    moved = true;
                }
            } else {
                System.out.println("Invalid command. Valid commands are:");
                System.out.println("say ___, north, south, east, west, up, down");
            }
        }
    }
}
