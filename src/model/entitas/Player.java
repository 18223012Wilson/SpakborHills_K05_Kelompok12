package model.entitas;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.Map;

import model.items.Inventory;
import model.items.Item;
import model.items.Point;
import model.items.ShippingBin;
import model.items.Fish;

import model.actions.Action;
import model.actions.Chatting;
import model.actions.ProcessGiftingAction;
import model.actions.Visiting;

import model.calendar.GameCalendar;


public class Player extends Entity {
    private String gender;
    private int maxEnergy;
    private int energy;
    private String farmName;
    private NPC partner;
    private int gold;
    private Inventory inventory;
    private ShippingBin shippingBin;

    private Set<String> unlockedRecipes = new HashSet<>();
    private int totalFishCaught = 0;
    private boolean hasHarvested = false;

    private static int totalIncome = 0;
    private static int totalExpenditure = 0;
    private static int totalDaysPlayed = 0;
    private static int totalCropsHarvested = 0;
    private static int overallFishCaught = 0;
    private static int commonFishCaught = 0;
    private static int regularFishCaught = 0;
    private static int legendaryFishCaught = 0;

    // Milestone tracking flags
    private boolean goldMilestoneAchieved = false;
    private boolean marriageMilestoneAchieved = false;


    public Player(String name, String location, String gender, String farmName) {
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

    public boolean isGoldMilestoneAchieved() {
        return goldMilestoneAchieved;
    }

    public void setGoldMilestoneAchieved(boolean goldMilestoneAchieved) {
        this.goldMilestoneAchieved = goldMilestoneAchieved;
    }

    public boolean isMarriageMilestoneAchieved() {
        return marriageMilestoneAchieved;
    }

    public void setMarriageMilestoneAchieved(boolean marriageMilestoneAchieved) {
        this.marriageMilestoneAchieved = marriageMilestoneAchieved;
    }

    public String getGender() {
        return gender;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public void setEnergy(int newEnergy) {
        this.energy = Math.max(-20, Math.min(newEnergy, this.maxEnergy));
        if (this.energy <= -20) {
            System.out.println(getName() + " is exhausted!");
        }
    }

    public void addEnergy(int amount) {
        setEnergy(this.energy + amount);
    }

    public void decreaseEnergy(int amount) {
        if (amount > 0) {
            setEnergy(this.energy - amount);
        }
    }

    public int getEnergy() {
        return energy;
    }

    public String getFarmName() {
        return farmName;
    }

    public NPC getPartner() {
        return partner;
    }

    public void setPartner(NPC newPartner) {
        this.partner = newPartner;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = Math.max(0, gold);
    }

    public void addGold(int amount) {
        if (amount > 0) {
            this.gold += amount;
            Player.totalIncome += amount;
        }
    }

    public boolean spendGold(int amount) {
        if (amount > 0 && this.gold >= amount) {
            this.gold -= amount;
            Player.totalExpenditure += amount;
            return true;
        }
        return false;
    }

    public void showInventory() {
        inventory.getItemList();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public ShippingBin getShippingBin() {
        return shippingBin;
    }

    public String collectShippingBinRevenue() {
        if (shippingBin != null) {
            int goldCollected = shippingBin.collectPendingGold();
            if (goldCollected > 0) {
                addGold(goldCollected);
                return "Kamu menerima " + goldCollected + "g dari penjualan kemarin.";
            }
        }
        return "";
    }

    public void finalizeDailySales() {
        if (shippingBin != null) {
            shippingBin.processEndOfDaySales();
        }
    }

    public void sleep() {
        finalizeDailySales();

        if (getEnergy() <= -20) {
            setEnergy(getMaxEnergy() / 2);
        }
        else if (getEnergy() < (getMaxEnergy() * 0.1)) {
            setEnergy(getEnergy() + (getMaxEnergy() / 2));
            if(getEnergy() > getMaxEnergy()) setEnergy(getMaxEnergy());
        }
        else if (getEnergy() == 0) {
            setEnergy(10);
        }
        else {
            setEnergy(getMaxEnergy());
        }
    }

    public boolean hasUnlockedRecipe(String recipeId) {
        return unlockedRecipes.contains(recipeId);
    }

    public Set<String> getUnlockedRecipes() {
        return unlockedRecipes;
    }

    public int getTotalFishCaughtThisInstance() {
        return totalFishCaught;
    }

    public void incrementFishCaughtStatistic(Fish.FishType fishType) {
        Player.overallFishCaught++;
        switch (fishType) {
            case COMMON:
                Player.commonFishCaught++;
                break;
            case REGULAR:
                Player.regularFishCaught++;
                break;
            case LEGENDARY:
                Player.legendaryFishCaught++;
                break;
        }
    }


    public boolean getHasHarvestedThisInstance() {
        return hasHarvested;
    }

    public void setHasHarvested(boolean value) {
        if (value) {
            Player.totalCropsHarvested++;
            if (!this.hasHarvested) {
                this.hasHarvested = true;
                if (!unlockedRecipes.contains("recipe_7")) {
                    unlockedRecipes.add("recipe_7");
                }
            }
        }
    }

    public void onFishCaught(Fish fishObject) {
        this.totalFishCaught++;
        incrementFishCaughtStatistic(fishObject.getFishType());

        if (totalFishCaught >= 10 && !unlockedRecipes.contains("recipe_3")) {
            unlockedRecipes.add("recipe_3");
        }
        // Use fishObject.getName() for comparisons
        if (fishObject.getName().equalsIgnoreCase("Legend") && !unlockedRecipes.contains("recipe_11")) {
            unlockedRecipes.add("recipe_11");
        } else if (fishObject.getName().equalsIgnoreCase("Pufferfish") && !unlockedRecipes.contains("recipe_4")) {
            unlockedRecipes.add("recipe_4");
        }
    }

    public void onItemObtained(String itemName) {
        if (itemName.equalsIgnoreCase("Hot Pepper") && !unlockedRecipes.contains("recipe_8")) {
            unlockedRecipes.add("recipe_8");
        }
    }

    public void doAction(Action action) {
        action.execute();
    }

    public void buyItem(Item item) {
        if (item.getBuyPrice() > 0) {
            if (spendGold(item.getBuyPrice())) {
                getInventory().addItem(item, 1);
            }
        }
    }

    public void watch(){
        decreaseEnergy(5);
    }

    public void chat(NPC target){
        if (target != null) {
            target.setHeartPoints(target.getHeartPoints() + 10);
            Chatting.incrementFrequency();
            decreaseEnergy(10);
        }
    }

    public void propose(NPC target){
        if (target != null) {
            if(target.getHeartPoints() >= 150){
                target.setRelationshipStatus("fiance");
                setPartner(target);
                decreaseEnergy(10);
            } else {
                decreaseEnergy(20);
            }
        }
    }

    public void marry(NPC target){
        if(target != null && target.getRelationshipStatus().equalsIgnoreCase("fiance") && this.partner == target){
            decreaseEnergy(80);
            target.setRelationshipStatus("spouse");
        }
    }

    public static void incrementTotalDaysPlayed() {
        totalDaysPlayed++;
    }

    public static int getTotalIncome() {
        return totalIncome;
    }
    public static int getTotalExpenditure() {
        return totalExpenditure;
    }
    public static int getTotalDaysPlayed() {
        return totalDaysPlayed;
    }
    public static int getTotalCropsHarvested() {
        return totalCropsHarvested;
    }
    public static int getOverallFishCaught() {
        return overallFishCaught;
    }
    public static int getCommonFishCaught() {
        return commonFishCaught;
    }
    public static int getRegularFishCaught() {
        return regularFishCaught;
    }
    public static int getLegendaryFishCaught() {
    return legendaryFishCaught;
}


    public static String getStatistics(GameCalendar calendar, List<NPC> npcs) {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Statistik Permainan ---\n");
        sb.append("Total pendapatan: ").append(Player.getTotalIncome()).append("g\n");
        sb.append("Total pengeluaran: ").append(Player.getTotalExpenditure()).append("g\n");

        int seasonsPassed = (Player.getTotalDaysPlayed() > 0) ? ((Player.getTotalDaysPlayed() - 1) / 10) + 1 : 0;
        if (seasonsPassed > 0) {
            sb.append("Rata-rata pendapatan per musim: ").append(Player.getTotalIncome() / seasonsPassed).append("g\n");
            sb.append("Rata-rata pengeluaran per musim: ").append(Player.getTotalExpenditure() / seasonsPassed).append("g\n");
        } else {
            sb.append("Rata-rata pendapatan per musim: N/A (kurang dari satu musim)\n");
            sb.append("Rata-rata pengeluaran per musim: N/A (kurang dari satu musim)\n");
        }
        sb.append("Total hari bermain: ").append(Player.getTotalDaysPlayed()).append("\n");

        sb.append("Status hubungan dengan NPC:\n");
        if (npcs != null) {
            for (NPC npc : npcs) {
                if (npc != null) {
                    sb.append("  ").append(npc.getName()).append(": ").append(npc.getRelationshipStatus());
                    sb.append(" (").append(npc.getHeartPoints()).append("/").append(npc.getMaxHeartPoints()).append(" HP)\n");
                }
            }
        }

        sb.append("Total frekuensi Chatting: ").append(Chatting.getFrequency()).append("\n");
        sb.append("Total frekuensi Gifting: ").append(ProcessGiftingAction.getFrequency()).append("\n");
        sb.append("Total frekuensi Visiting ke area NPC: ").append(Visiting.getFrequency()).append("\n");
        sb.append("Total harvest: ").append(Player.getTotalCropsHarvested()).append("\n");
        sb.append("Total ikan ditangkap: ").append(Player.getOverallFishCaught()).append("\n");
        sb.append("  Ikan COMMON: ").append(Player.getCommonFishCaught()).append("\n");
        sb.append("  Ikan REGULAR: ").append(Player.getRegularFishCaught()).append("\n");
        sb.append("  Ikan LEGENDARY: ").append(Player.getLegendaryFishCaught()).append("\n");
        sb.append("-----------------------\n");
        return sb.toString();
    }
}