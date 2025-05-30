package model.entitas;

public class Dasco extends NPC{
    private static final String IMAGE_PATH = "/npcs/dasco.png";
    private String[] loved = new String[]{"The Legends of Spakbor", "Cooked Pig's Head", "Wine", "Fugu", "Spakbor Salad"};
    private String[] liked = new String[]{"Fish Sandwich", "Fish Stew", "Baguette", "Fish n’ Chips"};
    private String[] hated = new String[]{"Legend", "Grape", "Cauliflower", "Wheat", "Pufferfish", "Salmon"};

    public Dasco(){
        super("Dasco", null, IMAGE_PATH);
        setLovedItems(loved);
        setLikedItems(liked);
        setHatedItems(hated);
    }

}