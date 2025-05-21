package Entitas;
import ITEMS.*;

public class Emily extends NPC{
    private String[] loved = new String[]{"Seeds"}; //seluruh item seeds
    private String[] liked = new String[]{"Catfish", "Salmon", "Sardine"};
    private String[] hated = new String[]{"Coal", "Wood"};
    
    public Emily(){
        super("Emily", null);
        setLovedItems(loved);
        setLikedItems(liked);
        setHatedItems(hated);
    }

    public static void Sell(Player player, Item item){
        //cek lokasi store
        int price = item.getBuyPrice();
        if(player.getGold() < price){
            System.out.println("Uangmu tidak cukup!");
        }else{
            player.setGold(player.getGold()-price);
            player.getInventory().addItem(item, 1);
            System.out.println("Terimakasih sudah berbelanja!");
        }

    }
}