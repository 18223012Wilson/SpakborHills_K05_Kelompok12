package model.actions;

import model.entitas.NPC;
import model.entitas.Player;
import model.calendar.GameCalendar;
import model.items.Item;
import model.items.Seed;
import model.items.Fish;

public class Gifting extends Action {
    private Player player;
    private GameCalendar calendar;
    private NPC npcTarget;
    private Item gift;
    private static int frequency = 0;

    public Gifting(Player player, GameCalendar calendar, NPC npcTarget, Item gift) {
        super("Gifting to " + npcTarget.getName(), "Memberikan " + gift.getName() + " kepada " + npcTarget.getName(), gift.getName());
        this.player = player;
        this.calendar = calendar;
        this.npcTarget = npcTarget;
        this.gift = gift;
    }

    @Override
    public String execute() {

        if (player.getEnergy() < getEnergyCost()) {
            if (player.getEnergy() > -20 && (player.getEnergy() - getEnergyCost() >= -20) ) {
            }
            else {
                return "Energi tidak cukup untuk memberi hadiah. Butuh: " + getEnergyCost() + ", Punya: " + player.getEnergy();
            }
        }

        if (player.getInventory().getItemCountByName(gift.getName()) == 0) {
            return "Kamu tidak memiliki " + gift.getName() + " di inventory!";
        }

        if (!gift.isGiftable()) {
            return gift.getName() + " tidak bisa diberikan sebagai hadiah!";
        }

        player.decreaseEnergy(getEnergyCost());
        calendar.addTime(getTimeCost());
        player.getInventory().removeItemByName(gift.getName(), 1);
        Gifting.incrementFrequency();

        int heartPointsChange = 0;
        String reactionMessage = npcTarget.getName() + " terlihat biasa saja.";

        if (npcTarget.getName().equalsIgnoreCase("Emily")) {
            if (gift instanceof Seed) {
                heartPointsChange = 25;
                reactionMessage = npcTarget.getName() + ": Wah terimakasih, aku sangat suka barang ini! (Sangat Senang)";
            }
            else if (checkItemCategoryInList(npcTarget.getLikedItems(), gift.getName())) {
                heartPointsChange = 20;
                reactionMessage = npcTarget.getName() + ": Barang ini sangat bagus, terimakasih! (Senang)";
            }
            else if (checkItemCategoryInList(npcTarget.getHatedItems(), gift.getName())) {
                heartPointsChange = -25;
                reactionMessage = npcTarget.getName() + ": Oh, terimakasih? (Kurang Senang)";
            }
        }
        else if (npcTarget.getName().equalsIgnoreCase("Perry")) {
            if (gift instanceof Fish) {
                heartPointsChange = -25;
                reactionMessage = npcTarget.getName() + ": Oh, terimakasih? (Kurang Senang)";
            }
            else if (checkItemCategoryInList(npcTarget.getLovedItems(), gift.getName())) {
                heartPointsChange = 25;
                reactionMessage = npcTarget.getName() + ": Wah terimakasih, aku sangat suka barang ini! (Sangat Senang)";
            }
            else if (checkItemCategoryInList(npcTarget.getLikedItems(), gift.getName())) {
                heartPointsChange = 20;
                reactionMessage = npcTarget.getName() + ": Barang ini sangat bagus, terimakasih! (Senang)";
            }
        }
        else if (npcTarget.getName().equalsIgnoreCase("Mayor Tadi")) {
            if (checkItemCategoryInList(npcTarget.getLovedItems(), gift.getName())) {
                heartPointsChange = 25;
                reactionMessage = npcTarget.getName() + ": Wah terimakasih, aku sangat suka barang ini! (Sangat Senang)";
            }
            else if (checkItemCategoryInList(npcTarget.getLikedItems(), gift.getName())) {
                heartPointsChange = 20;
                reactionMessage = npcTarget.getName() + ": Barang ini sangat bagus, terimakasih! (Senang)";
            }
            else {
                heartPointsChange = -25;
                reactionMessage = npcTarget.getName() + ": Oh, terimakasih? (Kurang Senang)";
            }
        }
        else {
            if (npcTarget.getLovedItems() != null && checkItemCategoryInList(npcTarget.getLovedItems(), gift.getName())) {
                heartPointsChange = 25;
                reactionMessage = npcTarget.getName() + ": Wah terimakasih, aku sangat suka barang ini! (Sangat Senang)";
            }
            else if (npcTarget.getLikedItems() != null && checkItemCategoryInList(npcTarget.getLikedItems(), gift.getName())) {
                heartPointsChange = 20;
                reactionMessage = npcTarget.getName() + ": Barang ini sangat bagus, terimakasih! (Senang)";
            }
            else if (npcTarget.getHatedItems() != null && checkItemCategoryInList(npcTarget.getHatedItems(), gift.getName())) {
                heartPointsChange = -25;
                reactionMessage = npcTarget.getName() + ": Oh, terimakasih? (Kurang Senang)";
            }
        }

        npcTarget.setHeartPoints(npcTarget.getHeartPoints() + heartPointsChange);

        StringBuilder result = new StringBuilder();
        result.append("Kamu memberikan ").append(gift.getName()).append(" kepada ").append(npcTarget.getName()).append(".\n");
        result.append(reactionMessage).append("\n");
        result.append(npcTarget.getName()).append(" sekarang memiliki ").append(npcTarget.getHeartPoints()).append(" poin hati.\n");
        result.append("Energi tersisa: ").append(player.getEnergy());

        return result.toString();
    }

    private boolean checkItemCategoryInList(String[] list, String itemName) {
        if (list == null) return false;
        for (String s : list) {
            if (s != null && s.equalsIgnoreCase(itemName)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public int getEnergyCost() {
        return 5;
    }

    @Override
    public int getTimeCost() {
        return 10;
    }

    public static void incrementFrequency() {
        frequency++;
    }

    public static int getFrequency() {
        return frequency;
    }
}