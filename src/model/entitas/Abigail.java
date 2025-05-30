package model.entitas;

public class Abigail extends NPC{
    private static final String IMAGE_PATH = "/npcs/abigail.png";

    private String[] loved = new String[]{"Blueberry", "Melon", "Pumpkin", "Grape", "Cranberry"};
    private String[] liked = new String[]{"Baguette", "Pumpkin Pie", "Wine"};
    private String[] hated = new String[]{"Hot Pepper", "Cauliflower", "Parsnip", "Wheat"};

    public Abigail(){
        super("Abigail", null, IMAGE_PATH);
        setLovedItems(loved);
        setLikedItems(liked);
        setHatedItems(hated);
    }
}