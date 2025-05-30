package model.actions;

import model.entitas.NPC;
import model.entitas.Player;
import model.calendar.GameCalendar;
import model.items.Item;
import model.items.Seed;
import model.items.Fish;
import model.items.Inventory;

public class ProcessGiftingAction extends Action {
    private Player player;
    private GameCalendar calendar;
    private NPC npcTarget;
    private Item gift;
    private static int frequency = 0;

    public ProcessGiftingAction(Player player, GameCalendar calendar, NPC npcTarget, Item gift) {
        super("Processing Gift: " + gift.getName() + " to " + npcTarget.getName(),
                "Internally processes the gifting of an item.", gift.getName());
        this.player = player;
        this.calendar = calendar;
        this.npcTarget = npcTarget;
        this.gift = gift;
    }

    @Override
    public String execute() {
        int energyCost = getEnergyCost();
        if (player.getEnergy() - energyCost < -20 && player.getEnergy() > -20) {
            return "Not enough energy to give a gift. Will become too tired.";
        }
        if (player.getEnergy() < energyCost && energyCost > 0) {
            return "Not enough energy to give a gift. Need: " + energyCost + ", Have: " + player.getEnergy();
        }


        Inventory inventory = player.getInventory();
        if (inventory.getItemCountByName(gift.getName()) == 0) {
            return "You do not have " + gift.getName() + " in your inventory!";
        }

        if (!gift.isGiftable()) {
            return gift.getName() + " cannot be given as a gift!";
        }

        player.decreaseEnergy(energyCost);
        calendar.addTime(getTimeCost());
        inventory.removeItemByName(gift.getName(), 1);
        ProcessGiftingAction.incrementFrequency();

        int heartPointsChange = 0;
        String reactionMessage = npcTarget.getName() + " looks at the " + gift.getName() + " with a neutral expression.";

        if (npcTarget.getName().equalsIgnoreCase("Emily")) {
            if (gift instanceof Seed) {
                heartPointsChange = 25;
                reactionMessage = npcTarget.getName() + ": \"Wow, thank you! I really love this!\" (Very Happy)";
            }
            else if (checkItemInList(npcTarget.getLikedItems(), gift.getName())) {
                heartPointsChange = 20;
                reactionMessage = npcTarget.getName() + ": \"This is a great gift, thank you!\" (Happy)";
            }
            else if (checkItemInList(npcTarget.getHatedItems(), gift.getName())) {
                heartPointsChange = -25;
                reactionMessage = npcTarget.getName() + ": \"Oh, uh, thanks?\" (Unhappy)";
            }
        }
        else if (npcTarget.getName().equalsIgnoreCase("Perry")) {
            if (gift instanceof Fish) {
                heartPointsChange = -25;
                reactionMessage = npcTarget.getName() + ": \"Oh, uh, thanks?\" (Unhappy)";
            }
            else if (checkItemInList(npcTarget.getLovedItems(), gift.getName())) {
                heartPointsChange = 25;
                reactionMessage = npcTarget.getName() + ": \"Wow, thank you! I really love this!\" (Very Happy)";
            }
            else if (checkItemInList(npcTarget.getLikedItems(), gift.getName())) {
                heartPointsChange = 20;
                reactionMessage = npcTarget.getName() + ": \"This is a great gift, thank you!\" (Happy)";
            }
        }
        else if (npcTarget.getName().equalsIgnoreCase("Mayor Tadi")) {
            if (checkItemInList(npcTarget.getLovedItems(), gift.getName())) {
                heartPointsChange = 25;
                reactionMessage = npcTarget.getName() + ": \"Wow, thank you! I really love this!\" (Very Happy)";
            }
            else if (checkItemInList(npcTarget.getLikedItems(), gift.getName())) {
                heartPointsChange = 20;
                reactionMessage = npcTarget.getName() + ": \"This is a great gift, thank you!\" (Happy)";
            }
            else {
                heartPointsChange = -25;
                reactionMessage = npcTarget.getName() + ": \"Oh, uh, thanks?\" (Unhappy)";
            }
        } else {
            if (checkItemInList(npcTarget.getLovedItems(), gift.getName())) {
                heartPointsChange = 25;
                reactionMessage = npcTarget.getName() + ": \"Wow, thank you! I really love this!\" (Very Happy)";
            } else if (checkItemInList(npcTarget.getLikedItems(), gift.getName())) {
                heartPointsChange = 20;
                reactionMessage = npcTarget.getName() + ": \"This is a great gift, thank you!\" (Happy)";
            } else if (checkItemInList(npcTarget.getHatedItems(), gift.getName())) {
                heartPointsChange = -25;
                reactionMessage = npcTarget.getName() + ": \"Oh, uh, thanks?\" (Unhappy)";
            }
            if (heartPointsChange == 0) {
                reactionMessage = npcTarget.getName() + ": \"Thank you for the " + gift.getName() + "!\"";
            }
        }

        npcTarget.setHeartPoints(npcTarget.getHeartPoints() + heartPointsChange);

        return player.getName() + " gave " + gift.getName() + " to " + npcTarget.getName() + ".\n" +
                reactionMessage + "\n" +
                npcTarget.getName() + " now has " + npcTarget.getHeartPoints() + " heart points.\n" +
                "Remaining energy: " + player.getEnergy();
    }

    private boolean checkItemInList(String[] list, String itemName) {
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