package Entitas;
import java.util.Map;

import ITEMS.*;
import LocalCalendar.*;

public class Gifting extends Action{
    private Player player;
    private GameCalendar calendar;
    private NPC npc;
    private Item gift;
    public Gifting(Player player, GameCalendar calendar, NPC npc, Item gift){
        super("Gifting", "Memberikan item kepada seorang NPC", null);
        this.player = player;
        this.calendar = calendar;
        this.npc = npc;
        this.gift = gift;
    }

    public void execute(){
        // kondisi di rumah NPC
        System.out.println("Melakukan aksi: " + name);
        
        Boolean found = false;
        for (Map.Entry<Item, Integer> entry : player.getInventory().getItems().entrySet()) {
            Item item = entry.getKey();
            if(gift.getName().equals(item.getName())){
                found = true;
            }
        }
        if(found == false){
            System.out.println("Kamu tidak memiliki " + gift.getName() + "!");
            return;
        }
        
        int energy = player.getEnergy();
        if((energy-5) < -20){
            System.out.println("Energi tidak cukup!");
            return;
        }
        
        int npcHeartPoints = npc.getHeartPoints();
        player.setEnergy(energy-5);
        if(!gift.isGiftable()){
            System.out.println("Kamu tidak bisa memberikan item tersebut");
        }else{
            //untuk loveditems
            if(npc.getName().equals("Emily")){
                if(gift instanceof Seed){
                    player.getInventory().removeItem(gift, 1);
                    npcHeartPoints += 25;
                    npc.setHeartPoints(npcHeartPoints);
                    //percakapan sendiri/+10mnt
                    return;
                }
            }else{
                for(String s : npc.getLovedItems()){
                    if(gift.getName().equals(s)){
                        player.getInventory().removeItem(gift, 1);
                        npcHeartPoints += 25;
                        npc.setHeartPoints(npcHeartPoints);
                        //percakapan sendiri/+10mnt
                        return;
                    }
                }
            }
            //untuk likeditems
            for(String s : npc.getLikedItems()){
                if(gift.getName().equals(s)){
                    player.getInventory().removeItem(gift, 1);
                    npcHeartPoints += 20;
                    npc.setHeartPoints(npcHeartPoints);
                    //percakapan sendiri/+10mnt
                    return;
                }
            }
            //untuk hateditems
            if(npc.getName().equals("Mayor Tedi")){
                player.getInventory().removeItem(gift, 1);
                npcHeartPoints -= 25;
                npc.setHeartPoints(npcHeartPoints);
                //percakapan sendiri/+10mnt
                return;
            }else if(npc.getName().equals("Perry")){
                if(gift instanceof Fish){
                    player.getInventory().removeItem(gift, 1);
                    npcHeartPoints -= 25;
                    npc.setHeartPoints(npcHeartPoints);
                    //percakapan sendiri/+10mnt
                    return;
                }
            }else{
                for(String s : npc.getHatedItems()){
                    if(gift.getName().equals(s)){
                        player.getInventory().removeItem(gift, 1);
                        npcHeartPoints -= 25;
                        npc.setHeartPoints(npcHeartPoints);
                        //percakapan sendiri/+10mnt
                        return;
                    }
                }
            }
            //percakapan neutral item
        }
    }
}