package Entitas;
import ITEMS.*;

public class Player extends Entity{
    private String gender;
    private int maxEnergy;
    private int energy;
    private String farmName;
    private NPC partner;
    private int gold;
    private Inventory inventory;

    public Player(String name, String location, String gender, String farmName){
        super(name, location);
        this.gender = gender;
        this.maxEnergy = 100;
        this.energy = this.maxEnergy;
        this.farmName = farmName;
        this.partner = null;
        this.gold = 0;
        this.inventory = new Inventory();
    }
    
    public String getGender(){
        return gender;
    }

    public int getMaxEnergy(){
        return maxEnergy;
    }

    public void setEnergy(int newEnergy){
        this.energy = newEnergy;
    }

    public int getEnergy(){
        return energy;
    }

    public String getFarmName(){
        return farmName;
    }

    public NPC getPartner(){
        return partner;
    }

    public void setPartner(NPC newPartner){
        this.partner = newPartner;
    }

    public int getGold(){
        return gold;
    }

    public void setGold(int gold){
        this.gold = gold;
    }

    public void showInventory(){
        inventory.getItemList();
    }

    public Inventory getInventory(){
        return inventory;
    }

    // public void buyItem(Item item){ //beli dari Emily
    //     Emily.Sell(this, item);
    // }

}