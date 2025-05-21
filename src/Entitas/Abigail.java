package Entitas;

public class Abigail extends NPC{
    private String[] loved = new String[]{"Blueberry", "Melon", "Pumpkin", "Grape", "Cranberry"}; 
    private String[] liked = new String[]{"Baguette", "Pumpkin Pie", "Wine"};
    private String[] hated = new String[]{"Hot Pepper", "Cauliflower", "Parsnip", "Wheat"};
    
    public Abigail(){
        super("Abigail", null);
        setLovedItems(loved);
        setLikedItems(liked);
        setHatedItems(hated);
    }
}