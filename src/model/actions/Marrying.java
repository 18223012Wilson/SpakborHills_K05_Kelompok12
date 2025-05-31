package model.actions;

import java.util.Map;
import model.items.Item;
import model.items.Point;
import model.calendar.GameCalendar;
import model.entitas.NPC;
import model.entitas.Player;
import main.Main;
import model.map.FarmMap;

public class Marrying extends Action {
    private Player player;
    private GameCalendar calendar;
    private NPC npc;
    private Main mainFrame;

    public Marrying(Player player, GameCalendar calendar, NPC npc, Main mainFrame) {
        super("Marry " + npc.getName(), "Satukan janji suci dengan tunanganmu, " + npc.getName() + ".", "Proposal Ring");
        this.player = player;
        this.calendar = calendar;
        this.npc = npc;
        this.mainFrame = mainFrame;
    }

    @Override
    public String execute() {
        StringBuilder resultMessage = new StringBuilder();
        resultMessage.append("Hari besar telah tiba! Bersiap untuk menikahi ").append(npc.getName()).append(".\n");

        boolean hasRing = false;
        for (Map.Entry<Item, Integer> entry : player.getInventory().getAllItemsWithQuantities().entrySet()) {
            if (entry.getKey().getName().equals(getItemNeeded())) {
                hasRing = true;
                break;
            }
        }

        if (!hasRing) {
            resultMessage.append("Tunggu dulu! Kamu memerlukan ").append(getItemNeeded()).append(" untuk upacara penting ini!\n");
            return resultMessage.toString();
        }

        if (!npc.getRelationshipStatus().equals("fiance")) {
            resultMessage.append(npc.getName()).append(" kan belum jadi tunanganmu. Sabar ya!\n");
            return resultMessage.toString();
        }

        if (Proposing.dayOfProposal >= calendar.getDay()) {
            resultMessage.append("Sepertinya terlalu cepat untuk menikah! Beri waktu setidaknya sehari setelah lamaran, dong.\n");
            return resultMessage.toString();
        }

        int currentEnergy = player.getEnergy();
        int energyCost = getEnergyCost();

        if (currentEnergy - energyCost < -20 && currentEnergy > -20 ) {
            resultMessage.append("Waduh, energi kamu kritis! Bisa-bisa pingsan di altar nanti.\n");
            return resultMessage.toString();
        }
        if (currentEnergy < energyCost && energyCost > 0) {
            resultMessage.append("Energi kamu tidak cukup untuk melangsungkan hari bahagia ini.\n");
            return resultMessage.toString();
        }

        player.decreaseEnergy(energyCost);
        calendar.timeSkip(22, 0);
        npc.setRelationshipStatus("spouse");

        player.setLocation("Farm");
        mainFrame.switchToFarmMap();
        Point homeLocation = new Point(mainFrame.getFarmMap().getHouseCol() + FarmMap.HOUSE_WIDTH / 2, mainFrame.getFarmMap().getHouseRow() + FarmMap.HOUSE_HEIGHT);
        if (!(homeLocation.getY() < FarmMap.MAP_SIZE && homeLocation.getX() < FarmMap.MAP_SIZE &&
                mainFrame.getFarmMap().getTileType(homeLocation.getY(), homeLocation.getX()) == '.')) {
            homeLocation = new Point(1,1);
        }
        player.moveTo(homeLocation);

        resultMessage.append(player.getName()).append(": \"Di hadapan hari yang indah ini, aku ingin menghabiskan sisa hidupku bersamamu. Maukah kau menikah denganku?\"\n");
        try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        resultMessage.append(npc.getName()).append(": \"Dengan segenap jiwa dan ragaku, aku bersedia. Aku mencintaimu.\"\n");
        try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        resultMessage.append("Kalian merayakan hari pernikahan dengan penuh sukacita dan kebahagiaan.\n");
        resultMessage.append(npc.getName()).append(" kini telah resmi menjadi pasangan hidupmu.\n");
        resultMessage.append("Sisa energimu: ").append(player.getEnergy()).append(".\n");
        resultMessage.append("Waktu telah menunjukkan pukul 22:00, saatnya istirahat setelah hari yang panjang.\n");

        return resultMessage.toString();
    }

    @Override
    public int getEnergyCost() {
        return 80;
    }

    @Override
    public int getTimeCost() {
        return 0;
    }
}