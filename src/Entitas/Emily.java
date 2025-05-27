package PlayernNPC;
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
        if (item instanceof Seed || item instanceof Crop || item instanceof Food || item instanceof Misc){
            // proposal ring cuma bisa dibeli 1x
            if (item.getName().equalsIgnoreCase("Proposal Ring") && player.getInventory().getItemMap().keySet().stream().anyMatch(i -> i.getName().equalsIgnoreCase("Proposal Ring"))) {
                System.out.println("Emily: Maaf, kamu sudah membeli Proposal Ring sebelumnya.");
                return;
            }
            
            int price = item.getBuyPrice();
            if (price == 0) {
                System.out.println("Barang ini tidak bisa dibeli");
                return;
            }

            if (player.getGold() < price) {
                System.out.println("Uangmu tidak cukup!");
            } else {
                player.setGold(player.getGold()-price);
                player.getInventory().addItem(item, 1);
                System.out.println("Terimakasih sudah berbelanja!");
            }

            if (item.getName().startsWith("Recipe:")) {
                String recipeName = item.getName().substring("Recipe:".length()).trim(); // contoh: "Fish Sandwich"
                
                for (Recipe recipe : RecipeDatabase.getAllRecipes()) {
                    if (recipe.getName().equalsIgnoreCase(recipeName)) {
                        player.getUnlockedRecipes().add(recipe.getId());
                        System.out.println("Resep '" + recipe.getName() + "' telah berhasil di-unlock!");
                        break;
                    }
                }
            }
            
        } else {
            System.out.println("Emily : Maaf, aku tidak menjual barang itu.");
        }
    }
}
