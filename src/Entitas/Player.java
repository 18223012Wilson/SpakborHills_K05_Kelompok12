package Entitas;
import java.util.HashSet;
import java.util.Set;

import Action.*;
import ITEMS.*;
import LocalCalendar.GameCalendar;

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

    // statistik
    private static int totalIncome = 0;
    private static int totalExpenditure = 0;
    private static int averageSeasonIncome;
    private static int averageSeasonExpenditure;
    private static int totalDaysPlayed;
    private static int cropsHarvested;
    private static int fishCaught = 0;

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
        totalIncome += totalGold;
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
        fishCaught++;
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

    public void doAction(Action action){
        action.execute();
    }

    //beli dari Emily
    public void buyItem(Item item){ 
        Emily.Sell(this, item);
        totalExpenditure += item.getBuyPrice(); 
    }

    public static void showStatistic(GameCalendar calendar, NPC tadi, NPC caroline, NPC perry, NPC dasco, NPC emily, NPC abigail){
        averageSeasonIncome = totalIncome/totalDaysPlayed;
        averageSeasonExpenditure = totalIncome/totalDaysPlayed;
        totalDaysPlayed = calendar.getDay();
        cropsHarvested = Harvesting.cropsHarvested;

        System.out.println("Total income : " + totalIncome);
        System.out.println("Total expenditure : " + totalExpenditure);
        System.out.println("Average season income : " + averageSeasonIncome);
        System.out.println("Average season expenditure : " + averageSeasonExpenditure);
        System.out.println("Total days played : " + totalDaysPlayed);
        System.out.println("Relationship status : ");
        System.out.println("Mayor Tadi : " + tadi.getRelationshipStatus());
        System.out.println("Caroline : " + caroline.getRelationshipStatus());
        System.out.println("Perry : " + perry.getRelationshipStatus());
        System.out.println("Dasco : " + dasco.getRelationshipStatus());
        System.out.println("Emily : " + emily.getRelationshipStatus());
        System.out.println("Abigail : " + abigail.getRelationshipStatus());
        System.out.println("Chatting frequency : " + Chatting.frequency);
        System.out.println("Gifting frequency : " + Gifting.frequency);
        // System.out.println("Visiting frequency : " + Visiting.frequency);
        System.out.println("Crop harvested : " + Harvesting.cropsHarvested);
        System.out.println("Fish caught : " + fishCaught);
    }
}