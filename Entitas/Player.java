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

    public Player(String name, Point location, String gender, String farmName){
        //check exception gender
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
        // check exception
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

    public void Sleep(){
        if(getEnergy() == 0){
            setEnergy(10);
        }else if(getEnergy() < (getMaxEnergy()/10)){
            setEnergy(getEnergy() + (getMaxEnergy()/2));
        }else{
            setEnergy(getMaxEnergy());
        } // tidur ketika jam 2
    }

    public void watch(){
        // cek di rumah/ngga
        // cek ada tv/ngga
        // -15mnt in game
        setEnergy(getEnergy() - 5);
        // tampilin cuaca
    } 

    public void Chat(NPC target){
        // check exception NPC
        // kondisi di rumah NPC
        // -10mnt in game / buat percakapan sendiri
        target.setHeartPoints(target.getHeartPoints() + 10);
        setEnergy(getEnergy()-10);
    }

    // public void Gift(NPC target, Item item){
        // check exception NPC dan item
        // kondisi di rumah NPC
        // -10mnt in game / buat percakapan sendiri
        // cek item termasuk loved, liked, hated, atau neutral item
        // loved: +25, liked: +20, hated: -25, neutral: +0
        // item hilang dari inventory
        // setEnergy(getEnergy()-5);
    // }

    public void Propose(NPC target){
        // check exception NPC dan proposal ring
        if(target.getHeartPoints() == target.getMaxHeartPoints()){
            // -1jam in game
            target.setRelationshipStatus("fiance");
            setPartner(target);
            setEnergy(getEnergy() - 10);
            // simpan waktu propose
        }else if(target.getHeartPoints() < target.getMaxHeartPoints()){
            // -1jam in game
            setEnergy(getEnergy() - 20);
        }
    }

    public void Marry(NPC target){
        // check exception NPC dan proposal ring
        // cek 1 hari setelah propose
        if(target.getRelationshipStatus().equals("fiance")){
            // time skip 22.00
            // tp rumah
            setEnergy(getEnergy() - 80);
            target.setRelationshipStatus("spouse");
        }
    }
}