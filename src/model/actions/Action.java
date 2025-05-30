package model.actions;

public abstract class Action {
    protected String name;
    protected String description;
    protected String itemNeeded;

    public Action(String name, String description, String itemNeeded) {
        this.name = name;
        this.description = description;
        this.itemNeeded = itemNeeded;
    }

    public abstract String execute();

    public abstract int getEnergyCost();

    public abstract int getTimeCost();

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