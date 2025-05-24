package Entitas;
import java.util.HashSet;
import java.util.Set;

import ITEMS.*;

public class Player extends Entity{
    private String gender;
    private int maxEnergy;
    private int energy;
    private String farmName;
    private NPC partner;
    private int gold;
    private Inventory inventory;
    private ShippingBin shippingBin;

    // buat recipe
    private Set<String> unlockedRecipes = new HashSet<>();
    private int totalFishCaught = 0;
    private boolean hasHarvested = false;
    private boolean caughtLegendFish = false;
    private boolean caughtPufferfish = false;
    private boolean obtainedHotPepper = false;

    public Player(String name, String location, String gender, String farmName){
        super(name, location);
        this.gender = gender;
        this.maxEnergy = 100;
        this.energy = this.maxEnergy;
        this.farmName = farmName;
        this.partner = null;
        this.gold = 0;
        this.inventory = new Inventory();
        this.shippingBin = new ShippingBin();
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

    public ShippingBin getShippingBin() {
        return shippingBin;
    }
    
    public void prosesShippingBin() {
        int totalGold = shippingBin.getTotalSellValue();
        gold += totalGold;
        System.out.println("Uang hasil penjualan: " + totalGold);
        shippingBin.clearBin();
    }

    public void newDayReset() {
        shippingBin.resetDailySell();
        this.energy = this.maxEnergy;
    }

    public boolean hasUnlockedRecipe(String recipeId) {
        return unlockedRecipes.contains(recipeId);
    }

    public Set<String> getUnlockedRecipes() {
        return unlockedRecipes;
    }   

    public int getTotalFishCaught() {
        return totalFishCaught;
    }

    public void incrementFishCaught() {
        totalFishCaught++;
        if (totalFishCaught >= 10) {
            unlockedRecipes.add("recipe_3"); 
        }
    }

    public boolean getHasHarvested() {
        return hasHarvested;
    }

    public void setHasHarvested(boolean value) {
        if (value) {
            hasHarvested = true;
            unlockedRecipes.add("recipe_7"); 
        }
    }

    public void onFishCaught(String fishName) {
        incrementFishCaught();
        if (fishName.equalsIgnoreCase("Legend Fish")) {
            caughtLegendFish = true;
            unlockedRecipes.add("recipe_11"); 
        } else if (fishName.equalsIgnoreCase("Pufferfish")) {
            caughtPufferfish = true;
            unlockedRecipes.add("recipe_4");
        }
    }

    public void onItemObtained(String itemName) {
        if (itemName.equalsIgnoreCase("Hot Pepper")) {
            obtainedHotPepper = true;
            unlockedRecipes.add("recipe_8");
        }
    }

    //beli dari Emily
    public void buyItem(Item item){ 
        Emily.Sell(this, item);
    }

}