package ITEMS;

public class Equipment extends Item {
    public Equipment(String name) {
        super(name, 0, 0, false); // not buyable, sellable, or giftable
    }

    public String getCategory() {
        return "Equipment";
    }
}
