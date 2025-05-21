package model.entitas;

import model.items.Inventory;
import model.items.Point;
import model.map.FarmMap;


public class Player extends Entity{
    private String gender;
    private int maxEnergy;
    private int energy;
    private String farmName;
    private NPC partner;
    private int gold;
    private Inventory inventory;
    private FarmMap farmMap;

    public Player(String name, Point location, String gender, String farmName, FarmMap farmMap){
        super(name, location);
        this.gender = gender;
        this.maxEnergy = 100;
        this.energy = this.maxEnergy;
        this.farmName = farmName;
        this.partner = null;
        this.gold = 0;
        this.inventory = new Inventory();
        this.farmMap = farmMap;
    }

    public String getGender(){
        return gender;
    }

    public FarmMap getFarmMap(){
        return farmMap;
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

    public void showInventory(){
        inventory.getItemList();
    }

    public Inventory getInventory(){
        return inventory;
    }

    public void sleep(){
        if(getEnergy() == 0){
            setEnergy(10);
        }else if(getEnergy() < (getMaxEnergy()/10)){
            setEnergy(getEnergy() + (getMaxEnergy()/2));
        }else{
            setEnergy(getMaxEnergy());
        }
    }

    public void watch(){
        setEnergy(getEnergy() - 5);
    }

    public void chat(NPC target){
        target.setHeartPoints(target.getHeartPoints() + 10);
        setEnergy(getEnergy()-10);
    }

    public void propose(NPC target){
        if(target.getHeartPoints() == target.getMaxHeartPoints()){
            target.setRelationshipStatus("fiance");
            setPartner(target);
            setEnergy(getEnergy() - 10);
        }else if(target.getHeartPoints() < target.getMaxHeartPoints()){
            setEnergy(getEnergy() - 20);
        }
    }

    public void marry(NPC target){
        if(target.getRelationshipStatus().equals("fiance")){
            setEnergy(getEnergy() - 80);
            target.setRelationshipStatus("spouse");
        }
    }
}