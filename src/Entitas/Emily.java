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
        if(item instanceof Seed || item instanceof Crop || item instanceof Food || item instanceof Misc){
            int price = item.getBuyPrice();
            if(price == 0){
                System.out.println("Barang ini tidak bisa dibeli");
            }
            if(player.getGold() < price){
                System.out.println("Uangmu tidak cukup!");
            }else{
                player.setGold(player.getGold()-price);
                player.getInventory().addItem(item, 1);
                System.out.println("Terimakasih sudah berbelanja!");
            }
        }else{
            System.out.println("Emily : Maaf, aku tidak menjual barang itu.");
        }
    }
}