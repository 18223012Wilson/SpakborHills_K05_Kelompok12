package Action;


public class Action {
    protected String name;
    protected String description;
    protected String itemNeeded;

    // Constructor
    public Action(String name, String description, String itemNeeded) {
        this.name = name;
        this.description = description;
        this.itemNeeded = itemNeeded;
    }

    // Default implementation
    public void execute() {
        System.out.println("Melakukan aksi: " + name);
    }

    public int getEnergyCost() {
        return 0; // Default: tidak memerlukan energi
    }

    public int getTimeCost() {
        return 0; // Default: aksi instan
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getItemNeeded() {
        return itemNeeded;
    }
}