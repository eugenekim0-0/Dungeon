import java.util.ArrayList;

public class Room {
    private int roomType;
    private String description;
    private ArrayList<User> users;
    private ArrayList<Monster> monsters;
    private Room[] neighborRooms;
    private ArrayList<String> chat;

    /**
     * Constructs room object that takes in room type (transparent as 0 and solid as 1) and room description
     * Also stores the occupants (besides the user), surrounding rooms of the current room, and chat history
     * @param roomType
     * @param description
     * @param users
     * @param monsters
     * @param neighborRooms
     * @param chat
     */
    public Room(int roomType, String description, ArrayList<User> users, ArrayList<Monster> monsters, Room[] neighborRooms, ArrayList<String> chat) {
        this.roomType = roomType;
        this.description = description;
        this.users = users;
        this.monsters = monsters;
        this.neighborRooms = neighborRooms;
        this.chat = chat;
    }

    /**
     * Prints the room description and occupants in the command prompt
     * @param user
     */
    public void roomInfo(User user) {
        int userNum = this.users.size();
        int monNum = this.monsters.size();
        String userStatement =  "Adventurer(s): ";
        String monsterStatement = "Monster(s): ";
        for (User occ : this.users) {
            if (userNum > 1) {
                userStatement += occ.getName() + ", ";
            } else {
                userStatement += occ.getName();
            }
            userNum--;
        }
        if (monNum > 0) {
            for (Monster mon : this.monsters) {
                if (monNum > 1) {
                    monsterStatement += mon.getKind() + ", ";
                } else {
                    monsterStatement += mon.getKind();
                }
                monNum--;
            }
        }
        System.out.println("You are in a " + this.description + " room");
        System.out.println(userStatement);
        System.out.println(monsterStatement);
    }

    //roomType methods
    public void setRoomType(int change) {
        this.roomType = change;
    }

    public boolean isSolid() {
        return this.roomType == 1;
    }

    //users methods
    public ArrayList<User> getUsers() {
        return this.users;
    }

    /**
     * Moves the user from this room to another room
     * @param user
     * @param next
     */
    public void userRoomChange(Room next, User user) {
        next.users.add(user);
        this.users.remove(user);
    }

    //neighborRooms methods
    public Room[] getNeighborRooms() {
        return this.neighborRooms;
    }

    public Room getNeighbor(int dir) {
        return this.neighborRooms[dir];
    }
    
    public void setNeighbor(int dir, Room obj) {
        this.neighborRooms[dir] = obj;
    }
    
    /**
     * Connects the current room with the adjacent room that the user moves into
     * @param dir
     * @param other
     */
    public void connectRooms(int dir, Room other) {
        if (dir >= 0 && dir < 6) {
            if (dir % 2 == 0) {
                other.neighborRooms[dir + 1] = this;
            } else {
                other.neighborRooms[dir - 1] = this;
            }
        }
    }

    //chat methods
    public ArrayList<String> getChat() {
        return this.chat;
    }

    /**
     * Adds message to chat history
     * @param message
     */
    public void addToChat(String message) {
        this.chat.add(message);
    }

    /**
     * Prints the chat history of this room on the command prompt
     */
    public void showChat() {
        System.out.println("~ Current Room Chat ~");
        for (String each : this.chat) {
            System.out.println(each);
        }
    }

}
