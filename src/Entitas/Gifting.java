package Entitas;
import java.util.Map;

import ITEMS.*;
import LocalCalendar.*;

public class Gifting extends Action{
    private Player player;
    private GameCalendar calendar;
    private NPC npc;
    private Item gift;
    public static int frequency;
    public Gifting(Player player, GameCalendar calendar, NPC npc, Item gift){
        super("Gifting", "Memberikan item kepada seorang NPC", null);
        this.player = player;
        this.calendar = calendar;
        this.npc = npc;
        this.gift = gift;
    }

    public void execute(){
        // cek apakah di rumah NPC
        System.out.println("Melakukan aksi: " + name);
        
        int energy = player.getEnergy();
        if((energy-5) < -20){
            System.out.println("Energi tidak cukup!");
            return;
        }
        
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
        
        int npcHeartPoints = npc.getHeartPoints();
        player.setEnergy(energy-5);
        if(!gift.isGiftable()){
            System.out.println("Kamu tidak bisa memberikan item tersebut!");
        }else{
            //untuk loveditems
            frequency++;
            if(npc.getName().equals("Emily")){
                if(gift instanceof Seed){
                    System.out.println(player.getName() + " : Hai " + npc.getName() + ", aku ada sesuatu untukmu.");
                    try {
                        Thread.sleep(1000); 
                    } catch (InterruptedException e) {
                        e.printStackTrace(); 
                    }
                    System.out.println(npc.getName() + " : Wah terimakasih, aku sangat suka barang ini!");
                    try {
                        Thread.sleep(1000); 
                    } catch (InterruptedException e) {
                        e.printStackTrace(); 
                    }
                    System.out.println(npc.getName() + " terlihat sangat senang.");
                    
                    player.getInventory().removeItem(gift, 1);
                    npcHeartPoints += 25;
                    npc.setHeartPoints(npcHeartPoints);
                    
                    return;
                }
            }else{
                for(String s : npc.getLovedItems()){
                    if(gift.getName().equals(s)){
                        System.out.println(player.getName() + " : Hai " + npc.getName() + ", aku ada sesuatu untukmu.");
                        try {
                            Thread.sleep(1000); 
                        } catch (InterruptedException e) {
                            e.printStackTrace(); 
                        }
                        System.out.println(npc.getName() + " : Wah terimakasih, aku sangat suka barang ini!");
                        try {
                            Thread.sleep(1000); 
                        } catch (InterruptedException e) {
                            e.printStackTrace(); 
                        }
                        System.out.println(npc.getName() + " terlihat sangat senang.");
                        
                        player.getInventory().removeItem(gift, 1);
                        npcHeartPoints += 25;
                        npc.setHeartPoints(npcHeartPoints);
                        return;
                    }
                }
            }
            //untuk likeditems
            for(String s : npc.getLikedItems()){
                if(gift.getName().equals(s)){
                    System.out.println(player.getName() + " : Hai " + npc.getName() + ", aku ada sesuatu untukmu.");
                    try {
                        Thread.sleep(1000); 
                    } catch (InterruptedException e) {
                        e.printStackTrace(); 
                    }
                    System.out.println(npc.getName() + " : Barang ini sangat bagus, terimakasih!");
                    try {
                        Thread.sleep(1000); 
                    } catch (InterruptedException e) {
                        e.printStackTrace(); 
                    }
                    System.out.println(npc.getName() + " terlihat senang.");

                    player.getInventory().removeItem(gift, 1);
                    npcHeartPoints += 20;
                    npc.setHeartPoints(npcHeartPoints);
                    return;
                }
            }
            //untuk hateditems
            if(npc.getName().equals("Mayor Tedi")){
                System.out.println(player.getName() + " : Hai " + npc.getName() + ", aku ada sesuatu untukmu.");
                try {
                    Thread.sleep(1000); 
                } catch (InterruptedException e) {
                    e.printStackTrace(); 
                }
                System.out.println(npc.getName() + " : Oh, terimakasih?");
                try {
                    Thread.sleep(1000); 
                } catch (InterruptedException e) {
                    e.printStackTrace(); 
                }
                System.out.println(npc.getName() + " terlihat kurang senang.");

                player.getInventory().removeItem(gift, 1);
                npcHeartPoints -= 25;
                npc.setHeartPoints(npcHeartPoints);
                return;
            }else if(npc.getName().equals("Perry")){
                if(gift instanceof Fish){
                    System.out.println(player.getName() + " : Hai " + npc.getName() + ", aku ada sesuatu untukmu.");
                    try {
                        Thread.sleep(1000); 
                    } catch (InterruptedException e) {
                        e.printStackTrace(); 
                    }
                    System.out.println(npc.getName() + " : Oh, terimakasih?");
                    try {
                        Thread.sleep(1000); 
                    } catch (InterruptedException e) {
                        e.printStackTrace(); 
                    }
                    System.out.println(npc.getName() + " terlihat kurang senang.");

                    player.getInventory().removeItem(gift, 1);
                    npcHeartPoints -= 25;
                    npc.setHeartPoints(npcHeartPoints);
                    return;
                }
            }else{
                for(String s : npc.getHatedItems()){
                    if(gift.getName().equals(s)){
                        System.out.println(player.getName() + " : Hai " + npc.getName() + ", aku ada sesuatu untukmu.");
                        try {
                            Thread.sleep(1000); 
                        } catch (InterruptedException e) {
                            e.printStackTrace(); 
                        }
                        System.out.println(npc.getName() + " : Oh, terimakasih?");
                        try {
                            Thread.sleep(1000); 
                        } catch (InterruptedException e) {
                            e.printStackTrace(); 
                        }
                        System.out.println(npc.getName() + " terlihat kurang senang.");

                        player.getInventory().removeItem(gift, 1);
                        npcHeartPoints -= 25;
                        npc.setHeartPoints(npcHeartPoints);
                        return;
                    }
                }
            }
            System.out.println(player.getName() + " : Hai " + npc.getName() + ", aku ada sesuatu untukmu.");
            try {
                Thread.sleep(1000); 
            } catch (InterruptedException e) {
                e.printStackTrace(); 
            }
            System.out.println(npc.getName() + " : Terimakasih!");
            try {
                Thread.sleep(1000); 
            } catch (InterruptedException e) {
                e.printStackTrace(); 
            }
            System.out.println(npc.getName() + " terlihat biasa saja.");
        }
    }
}