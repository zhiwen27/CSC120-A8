import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Set;
import java.lang.Math;
public class Player implements Contract, Cloneable{

    private String name;
    private int x;
    private int y;
    private int size;
    private double strength;
    protected Hashtable<String, Integer> inventory;
    protected int maxCapacity;
    protected ArrayList<Player> undo = new ArrayList<>();

    /**
     * Default Constructor: Intialize the inventory of the user and set its maxcapacity to be 10
     *                      Initialize the user's location to be at (0,0)
     *                      Initialize the user's size to be 10, and strength to be 50
     */
    public Player(){
        this.name = "Undefined";
        this.maxCapacity = 10;
        this.x = 0;
        this.y = 0;
        this.size = 10;
        this.strength = 50.0;
        this.inventory = new Hashtable<>(this.maxCapacity);
        Player newPlayer = this;
        Player newPlayerCopy = (Player) newPlayer.clone();
        undo.add(newPlayerCopy);
    }

    /**
     * Constructor for names
     * @param name the name you want to use
     */
    public Player(String name){
        this();
        this.name = name;
        Player newPlayer = this;
        Player newPlayerCopy = (Player) newPlayer.clone();
        undo.add(newPlayerCopy);
    }

    /**
     * Constructor for name and size of the inventory
     * @param name the name you want to use
     * @param max the size of the new inventory
     */
    public Player(String name, int max){
        this();
        this.name = name;
        this.maxCapacity = max;
        Player newPlayer = this;
        Player newPlayerCopy = (Player) newPlayer.clone();
        undo.add(newPlayerCopy);
    }

    /**
     * Constructor for name and location
     * @param name the name you want to use
     * @param x starting x coordinate
     * @param y starting y coordinate
     */
    public Player(String name, int x, int y){
        this();
        this.name = name;
        this.x = x;
        this.y = y;
        Player newPlayer = this;
        Player newPlayerCopy = (Player) newPlayer.clone();
        undo.add(newPlayerCopy);
    }

    /**
     * Constructor for name, location and size
     * @param name the name you want to use
     * @param x starting x coordinate
     * @param y starting y coordinate
     * @param size starting size
     */
    public Player(String name, int x, int y, int size){
        this();
        this.name = name;
        this.x = x;
        this.y = y;
        this.size = size;
        Player newPlayer = this;
        Player newPlayerCopy = (Player) newPlayer.clone();
        undo.add(newPlayerCopy);
    }

    /**
     * Copy the Player for the undo method
     */
    public Player clone(){
        try {
            return (Player) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Copy the inventory for the undo method
     * @param h the previous inventory you want to copy
     * @return the copied inventory
     */
    public Hashtable<String, Integer> hashTableClone(Hashtable<String, Integer> h){
        Hashtable<String, Integer> copy = new Hashtable<String, Integer>();
        Set<String> setOfKeys = h.keySet();
        for (String key: setOfKeys) {
            copy.put(key, h.get(key));
        }
        return copy;
    }

    /**
     * Check the strength of the player: return true if the strength is bigger than 0
     */
    public boolean checkStrength(){
        return this.strength > 0.0;
    }

    /**
     * Add the item to your inventory
     * @param item the item you pick up
     */
    public void grab(String item){
        if (checkStrength()){
            if (this.inventory.size() >= this.maxCapacity){
                throw new RuntimeException("Your inventory is full! Enlarge your inventory!");
            }
            else{
                if (this.inventory.containsKey(item)){
                    int utilizability = this.inventory.get(item);
                    utilizability += 50;    // assuming getting the same time would add 50 more utilizability to the item
                    this.strength -= 5.0;
                    Hashtable<String, Integer> inventoryCopy = this.hashTableClone(this.inventory);
                    Player newPlayer = this;
                    newPlayer.inventory = inventoryCopy;
                    Player newPlayerCopy = (Player) newPlayer.clone();
                    undo.add(newPlayerCopy);
                    this.inventory.put(item, utilizability);
                    System.out.println("Great! Your " + item + " now has " + utilizability + " utilizability!");
                }
                else{
                    this.strength -= 5.0;
                    Hashtable<String, Integer> inventoryCopy = this.hashTableClone(this.inventory);
                    Player newPlayer = this;
                    newPlayer.inventory = inventoryCopy;
                    Player newPlayerCopy = (Player) newPlayer.clone();
                    undo.add(newPlayerCopy);
                    this.inventory.put(item, 100);    // initializing each item with 100 utilizability
                    System.out.println("Congratulations! You have discovered a new item: " + item + "!" + " Your " + item + " now has 100 utilizability!");
                }
            }
        }
        else{
            System.out.println("You're running out of strength. Take a rest now!");
        }
    }

    /**
     * Drop the item from your inventory
     * @param item the item you drop
     */
    public String drop(String item){
        if (checkStrength()){
            if (!this.inventory.containsKey(item)){
                throw new RuntimeException("Sorry, you don't have " + item + " in your inventory yet. Try discover it!");
            }
            else{
                int utilizability = this.inventory.get(item);
                if (utilizability == 0){
                    this.strength -= 5.0;
                    Hashtable<String, Integer> inventoryCopy = this.hashTableClone(this.inventory);
                    Player newPlayer = this;
                    newPlayer.inventory = inventoryCopy;
                    Player newPlayerCopy = (Player) newPlayer.clone();
                    undo.add(newPlayerCopy);
                    this.inventory.remove(item);
                    return "Wise choice! You have successfully remove " + item + " from your inventory.";
                }
                else{
                    this.strength -= 5.0;
                    Hashtable<String, Integer> inventoryCopy = this.hashTableClone(this.inventory);
                    Player newPlayer = this;
                    newPlayer.inventory = inventoryCopy;
                    Player newPlayerCopy = (Player) newPlayer.clone();
                    undo.add(newPlayerCopy);
                    this.inventory.remove(item);    // would remove the item but leaving space for probably undo this process
                    return "Your " + item + " still has " + utilizability + " utilizability... Do you really want to drop it?";
                }
            }
        }
        else{
            return "You're running out of strength. Take a rest now!";
        }
    }

    /**
     * Print the items inside the inventory
     */
    public String printItem(){
        if (this.inventory.isEmpty()){
            return "Nothing! Start your discovery now!";
        }
        else{
            Enumeration<String> keys = this.inventory.keys();
            int num = this.inventory.size();    // Simply indicate the number of the item
            ArrayList<String> s = new ArrayList<>();
            while(keys.hasMoreElements()){
                s.add("Item " + num + ": " + keys.nextElement() + "\n");
                num--;
            }
            String print = "";
            for (int i = s.size() - 1; i >= 0; i--){
                print += s.get(i);
            }
            return print;
        }
    }

    /**
     * Examine the utilizability of the item
     * @param item the name of the item you want to examine
     */
    public void examine(String item){
        if (checkStrength()){
            if (this.inventory.containsKey(item)){
                this.strength -= 2.0;
                Player newPlayer = this;
                Player newPlayerCopy = (Player) newPlayer.clone();
                undo.add(newPlayerCopy);
                System.out.println("Your " + item + " now has " + this.inventory.get(item) + " utilizability!");
            }
            else{
                this.strength -= 2.0;
                Player newPlayer = this;
                Player newPlayerCopy = (Player) newPlayer.clone();
                undo.add(newPlayerCopy);
                System.out.println("Sorry, you don't have " + item + " yet, try discover it!");
            }
        }
        else{
            System.out.println("You're running out of strength. Take a rest now!");
        }
    }

    /**
     * Use the item
     * @param item the name of the item you want to use
     */
    public void use(String item){
        if (checkStrength()){
            if(this.inventory.containsKey(item)){
                int utilizability = this.inventory.get(item);
                if (utilizability > 0){
                    this.strength -= 5.0;
                    utilizability -= 10;    // assuming using the item each time would reduce its utilizability by 10;
                    Player newPlayer = this;
                    Player newPlayerCopy = (Player) newPlayer.clone();
                    undo.add(newPlayerCopy);
                    this.inventory.put(item,utilizability);
                }
                else{
                    System.out.println("Sorry, your " + item + " is running out of utilizability. Try find another one or simply just drop it!");
                }
            }
            else{
                System.out.println("Sorry, you don't have " + item + " yet, try discover it!");
            }
        }
        else{
            System.out.println("You're running out of strength. Take a rest now!");
        }
    }

    /**
     * Walk around
     * x coordinate + 1, if walking to the right/ east
     * x coordinate - 1, if walking to the left/ west
     * y coordinate + 1, if walking up/ to the north
     * y coordinate - 1, if walking down/ to the south
     * x coordinate + 1, y cooridinate + 1 if walking to the upper-right/ north-east
     * x coordinate + 1, y cooridinate - 1 if walking to the upper-left/ north-west
     * x coordinate - 1, y cooridinate + 1 if walking to the lower-right/ south-east
     * x coordinate - 1, y cooridinate - 1 if walking to the lower-left/ south-west
     * A movement would use up 10 strengths (ignore that walking to the corners would actually take more strength)
     * If not enter specific directions, would just walk in the direction you like for distance 1
     */
    public boolean walk(String direction){
        if ((this.strength - 10) > 0){
            if (direction.contains("right") || direction.contains("east")){
                this.x += 1;
                this.strength -= 10.0;
                Player newPlayer = this;
                Player newPlayerCopy = (Player) newPlayer.clone();
                undo.add(newPlayerCopy);
                System.out.println("You're now at (" + this.x + "," + this.y + ").");
            }
            else if (direction.contains("left") || direction.contains("west")){
                this.x -= 1;
                this.strength -= 10.0;
                Player newPlayer = this;
                Player newPlayerCopy = (Player) newPlayer.clone();
                undo.add(newPlayerCopy);
                System.out.println("You're now at (" + this.x + "," + this.y + ").");
            }
            else if (direction.contains("up") || direction.contains("north")){
                this.y += 1;
                this.strength -= 10.0;
                Player newPlayer = this;
                Player newPlayerCopy = (Player) newPlayer.clone();
                undo.add(newPlayerCopy);
                System.out.println("You're now at (" + this.x + "," + this.y + ").");
            }
            else if (direction.contains("down") || direction.contains("south")){
                this.y -= 1;
                this.strength -= 10.0;
                Player newPlayer = this;
                Player newPlayerCopy = (Player) newPlayer.clone();
                undo.add(newPlayerCopy);
                System.out.println("You're now at (" + this.x + "," + this.y + ").");
            }
            else if (direction.contains("upper-right") || direction.contains("north-east")){
                this.x += 1;
                this.y += 1;
                this.strength -= 10.0;
                Player newPlayer = this;
                Player newPlayerCopy = (Player) newPlayer.clone();
                undo.add(newPlayerCopy);
                System.out.println("You're now at (" + this.x + "," + this.y + ").");
            }
            else if (direction.contains("upper-left") || direction.contains("north-west")){
                this.x -= 1;
                this.y += 1;
                this.strength -= 10.0;
                Player newPlayer = this;
                Player newPlayerCopy = (Player) newPlayer.clone();
                undo.add(newPlayerCopy);
                System.out.println("You're now at (" + this.x + "," + this.y + ").");
            }
            else if (direction.contains("lower-right") || direction.equals("south-east")){
                this.x += 1;
                this.y -= 1;
                this.strength -= 10.0;
                Player newPlayer = this;
                Player newPlayerCopy = (Player) newPlayer.clone();
                undo.add(newPlayerCopy);
                System.out.println("You're now at (" + this.x + "," + this.y + ").");
            }
            else if (direction.contains("lower-left") || direction.equals("south-west")){
                this.x -= 1;
                this.y -= 1;
                this.strength -= 10.0;
                Player newPlayer = this;
                Player newPlayerCopy = (Player) newPlayer.clone();
                undo.add(newPlayerCopy);
                System.out.println("You're now at (" + this.x + "," + this.y + ").");
            }
            else{
                this.strength -= 10.0;
                Player newPlayer = this;
                Player newPlayerCopy = (Player) newPlayer.clone();
                undo.add(newPlayerCopy);
                System.out.println("You're now walking to the " + direction + ".");
            }
        }
        else{
            System.out.println("You don't have the strength to walk. Take a rest!");
        }
        return false;
    }

    /**
     * Fly to some place with certain speed
     * @param x the change in x coordinate each moment
     * @param y the change in y coordinate each moment
     */
    public boolean fly(int x, int y){
        if ((this.strength - 10.0 * Math.sqrt((x - this.x) * (x - this.x) + (y - this.y) * (y - this.y))) > 0){
            this.x += x;
            this.y += y;
            this.strength -= 10.0 * Math.sqrt((x - this.x) * (x - this.x) + (y - this.y) * (y - this.y));
            Player newPlayer = this;
            Player newPlayerCopy = (Player) newPlayer.clone();
            undo.add(newPlayerCopy);
            System.out.println("You're now at (" + this.x + "," + this.y + ").");
        }
        else{
            System.out.println("You don't have the strength to fly to the destination. Take a rest or change your target!");
        }
        return false;
    }

    /**
     * Shrink by a factor of two: return the new size
     */
    public Number shrink(){
        this.size /= 2;
        Player newPlayer = this;
        Player newPlayerCopy = (Player) newPlayer.clone();
        undo.add(newPlayerCopy);
        return this.size;
    }

    /**
     * Grow by a factor of two: return the new size
     */
    public Number grow(){
        this.size *= 2;
        Player newPlayer = this;
        Player newPlayerCopy = (Player) newPlayer.clone();
        undo.add(newPlayerCopy);
        return this.size;
    }

    /**
     * Have a rest: recharging strength by 50
     */
    public void rest(){
        this.strength += 50;
        Player newPlayer = this;
        Player newPlayerCopy = (Player) newPlayer.clone();
        undo.add(newPlayerCopy);
        System.out.println("You're now having a rest.");
    }

    /**
     * Print out basic information about the player
     */
    public String toString(){
        String s = "\n";
        s += "Your name: " + this.name + ". \nYour size: " + this.size + ". \nYour strength: " + this.strength + ". \nYou're now at: (" + this.x + "," + this.y + ").\n";
        s += "Your inventory has: \n";
        s += this.printItem();
        return s;
    }

    /**
     * undo: get back to the previous step and throw an exception
     */
    public void undo(){
        // I think the removeLast and getLast methods work on my computer, I am a bit confused that it doesn't work in the submission
        undo.remove(undo.size() - 1);
        System.out.println(undo.get(undo.size() - 1));
        throw new RuntimeException("You have know undone your process. Please start over!");
    }

    public static void main(String[] args) {
        Player newPlayer = new Player("A");
        System.out.println(newPlayer);
        //newPlayer.undo();
        newPlayer.grab("Sword 1");
        newPlayer.grab("Sword 2");
        newPlayer.grab("Sword 3");
        System.out.println(newPlayer.drop("Sword 2"));
        System.out.println(newPlayer);
        System.out.println(newPlayer.drop("Sword 3"));
        System.out.println(newPlayer);
        newPlayer.undo();
        newPlayer.use("Sword 3");
        //newPlayer.examine("Item 1");
        //System.out.println(newPlayer);
        //newPlayer.walk("right");
        //System.out.println(newPlayer);
        //newPlayer.fly(1, 0);
        //System.out.println(newPlayer);
        //newPlayer.undo();
        //System.out.println(newPlayer);
        //System.out.println(newPlayer.printItem());
        //newPlayer.examine("Thing");
        //System.out.println(newPlayer.drop("a"));
    }
}
