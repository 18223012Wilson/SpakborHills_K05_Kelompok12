package model.actions;

import java.util.Map;
import model.items.Item;
import model.calendar.GameCalendar;
import model.entitas.NPC;
import model.entitas.Player;

public class Proposing extends Action {
    public static int dayOfProposal;
    private Player player;
    private GameCalendar calendar;
    private NPC npc;

    public Proposing(Player player, GameCalendar calendar, NPC npc) {
        super("Propose to " + npc.getName(), "Ajak " + npc.getName() + " untuk menjadi tunanganmu.", "Proposal Ring");
        this.player = player;
        this.calendar = calendar;
        this.npc = npc;
    }

    @Override
    public String execute() {
        StringBuilder resultMessage = new StringBuilder();
        resultMessage.append("Mencoba mengutarakan isi hati kepada ").append(npc.getName()).append(".\n");

        boolean hasRing = false;
        for (Map.Entry<Item, Integer> entry : player.getInventory().getAllItemsWithQuantities().entrySet()) {
            if (entry.getKey().getName().equals(getItemNeeded())) {
                hasRing = true;
                break;
            }
        }

        if (!hasRing) {
            resultMessage.append("Sepertinya kamu melupakan sesuatu yang penting... Ya, ").append(getItemNeeded()).append("!\n");
            return resultMessage.toString();
        }

        int currentEnergy = player.getEnergy();
        int energyCostAccepted = 10;
        int energyCostRejected = 20;

        if (npc.getRelationshipStatus().equals("fiance") || npc.getRelationshipStatus().equals("spouse")) {
            resultMessage.append(npc.getName()).append(" kan sudah menjadi ").append(npc.getRelationshipStatus()).append("mu! Mungkin kamu lupa?\n");
            return resultMessage.toString();
        }

        if (npc.getHeartPoints() == npc.getMaxHeartPoints()) {
            if (currentEnergy - energyCostAccepted < -20 && currentEnergy > -20) {
                resultMessage.append("Duh, energi kamu hampir habis! Kalau diterima nanti malah pingsan.\n");
                return resultMessage.toString();
            }
            if (currentEnergy < energyCostAccepted && energyCostAccepted > 0) {
                resultMessage.append("Energi kamu sepertinya tidak cukup untuk momen sebesar ini.\n");
                return resultMessage.toString();
            }

            player.decreaseEnergy(energyCostAccepted);
            calendar.addTime(getTimeCost());
            player.setPartner(npc);
            npc.setRelationshipStatus("fiance");
            Proposing.dayOfProposal = calendar.getDay();

            resultMessage.append(player.getName()).append(": \"").append(npc.getName()).append(", selama ini... maukah kamu menjadi tunanganku?\"\n");
            try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            resultMessage.append(npc.getName()).append(": \"Tentu saja, aku mau! Ini hari terindah dalam hidupku!\"\n");
            try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            resultMessage.append("Kalian berdua larut dalam kebahagiaan tak terhingga.\n");
            resultMessage.append(npc.getName()).append(" kini resmi menjadi tunanganmu.\n");
            resultMessage.append("Sisa energimu: ").append(player.getEnergy()).append(".\n");

        } else {
            if (currentEnergy - energyCostRejected < -20 && currentEnergy > -20) {
                resultMessage.append("Energi menipis! Menyiapkan hati untuk kemungkinan terburuk butuh tenaga ekstra.\n");
                return resultMessage.toString();
            }
            if (currentEnergy < energyCostRejected && energyCostRejected > 0) {
                resultMessage.append("Kamu terlalu lemas untuk menghadapi kemungkinan ditolak.\n");
                return resultMessage.toString();
            }
            player.decreaseEnergy(energyCostRejected);
            calendar.addTime(getTimeCost());
            resultMessage.append(player.getName()).append(": \"").append(npc.getName()).append(", aku... aku ingin kamu jadi tunanganku. Maukah?\"\n");
            try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            resultMessage.append(npc.getName()).append(": \"Kamu orang yang baik, tapi... maaf, sepertinya aku belum siap untuk ini.\"\n");
            try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            resultMessage.append("Rasanya ada sedikit awan mendung di hatimu.\n");
            resultMessage.append("Sisa energimu: ").append(player.getEnergy()).append(".\n");
        }
        return resultMessage.toString();
    }

    @Override
    public int getEnergyCost() {
        return 20;
    }

    @Override
    public int getTimeCost() {
        return 60;
    }
}