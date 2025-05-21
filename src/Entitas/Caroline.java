package Entitas;

public class Caroline extends NPC{
    private String[] loved = new String[]{"Firewood", "Coal"};
    private String[] liked = new String[]{"Potato", "Wheat"};
    private String[] hated = new String[]{"Hot Pepper"};
    
    public Caroline(){
        super("Caroline", null);
        setLovedItems(loved);
        setLikedItems(liked);
        setHatedItems(hated);
    }
    
}