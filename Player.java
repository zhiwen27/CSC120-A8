import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Scanner;
import java.lang.Math;
import java.util.Scanner;
public class Player implements Contract {

    private String name;
    private int x;
    private int y;
    private int size;
    private double strength;
    protected Hashtable<String, Integer> inventory;
    protected int maxCapacity;

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
    }

    /**
     * Constructor for names
     * @param name the name you want to use
     */
    public Player(String name){
        this();
        this.name = name;
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
                    this.inventory.put(item, utilizability);
                    System.out.println("Great! Your " + item + " now has " + utilizability + " utilizability!");
                }
                else{
                    this.strength -= 5.0;
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
                    this.inventory.remove(item);
                    return "Wise choice! You have successfully remove " + item + " from your inventory.";
                }
                else{
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
    public void printItem(){
        Enumeration<String> keys = this.inventory.keys();
        int num = 1;    // Simply indicate the number of the item
        while(keys.hasMoreElements()){
            System.out.println("Item " + num + ": " + keys.nextElement());
            num++;
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
                System.out.println("Your " + item + " now has " + this.inventory.get(item) + " utilizability!");
            }
            else{
                this.strength -= 2.0;
                System.out.println("Sorry, you don't have " + item + "yet, try discover it!");
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
            int utilizability = this.inventory.get(item);
            if (utilizability > 0){
                this.strength -= 5.0;
                utilizability -= 10;    // assuming using the item each time would reduce its utilizability by 10;
                this.inventory.put(item,utilizability);
            }
            else{
                System.out.println("Sorry, your " + item + " is running out of utilizability. Try find another one or simply just drop it!");
            }
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
     * A movement of distance 1 would use up 10 strengths
     * If not typing in specific directions, would just walk in the direction you like for distance 1 each time
     */
    public boolean walk(String direction){
        while(checkStrength()){
            if (direction.contains("right") || direction.contains("east")){
                this.x += 1;
                this.strength -= 10.0;
                System.out.println("You're now at (" + this.x + "," + this.y + ").");
            }
            else if (direction.contains("left") || direction.contains("west")){
                this.x -= 1;
                this.strength -= 10.0;
                System.out.println("You're now at (" + this.x + "," + this.y + ").");
            }
            else if (direction.contains("up") || direction.contains("north")){
                this.y += 1;
                this.strength -= 10.0;
                System.out.println("You're now at (" + this.x + "," + this.y + ").");
            }
            else if (direction.contains("down") || direction.contains("south")){
                this.y -= 1;
                this.strength -= 10.0;
                System.out.println("You're now at (" + this.x + "," + this.y + ").");
            }
            else if (direction.contains("upper-right") || direction.contains("north-east")){
                this.x += 1;
                this.y += 1;
                this.strength -= 10.0 * Math.sqrt(2);
                System.out.println("You're now at (" + this.x + "," + this.y + ").");
            }
            else if (direction.contains("upper-left") || direction.contains("north-west")){
                this.x -= 1;
                this.y += 1;
                this.strength -= 10.0 * Math.sqrt(2);
                System.out.println("You're now at (" + this.x + "," + this.y + ").");
            }
            else if (direction.contains("lower-right") || direction.equals("south-east")){
                this.x += 1;
                this.y -= 1;
                this.strength -= 10.0 * Math.sqrt(2);
                System.out.println("You're now at (" + this.x + "," + this.y + ").");
            }
            else if (direction.contains("lower-left") || direction.equals("south-west")){
                this.x -= 1;
                this.y -= 1;
                this.strength -= 10.0 * Math.sqrt(2);
                System.out.println("You're now at (" + this.x + "," + this.y + ").");
            }
            else{
                this.strength -= 10.0;
                System.out.println("You're now walking to the " + direction + ".");
            }
        }
        System.out.println("You don't have enough strength to walk any more. Take a rest!");
        return false;
    }

    /**
     * Fly to some place with certain speed
     * @param x the change in x coordinate each moment
     * @param y the change in y coordinate each moment
     */
    public boolean fly(int x, int y){
        while (checkStrength()){
            this.x -= x;
            this.y -= y;
            this.strength -= 10.0 * Math.sqrt(x * x + y * y);
            System.out.println("You're now at (" + this.x + "," + this.y + ").");
        }
        System.out.println("You don't have enough strength to fly any more. Take a rest!");
        return false;
    }

    /**
     * Shrink by a factor of two: return the new size
     */
    public Number shrink(){
        return this.size / 2;
    }

    /**
     * Grow by a factor of two: return the new size
     */
    public Number grow(){
        return this.size * 2;
    }

    /**
     * Have a rest: recharging strength by 50
     */
    public void rest(){
        this.strength += 50;
        System.out.println("You're now having a rest.");
    }

    public void undo(){
        
    }

    public static void main(String[] args) {
        Player newPlayer = new Player("A");
        newPlayer.grab("Thing");
        newPlayer.grab("a");
        newPlayer.grab("B");
        newPlayer.printItem();
        newPlayer.examine("Thing");
        System.out.println(newPlayer.drop("a"));
    }
}
