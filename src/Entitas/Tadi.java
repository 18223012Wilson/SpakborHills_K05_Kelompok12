package Entitas;

public class Tadi extends NPC{
    private String[] loved = new String[]{"Legend"};
    private String[] liked = new String[]{"Angler", "CrimsonFish", "GlacierFish"};
    private String[] hated = new String[]{};
    
    public Tadi(){
        super("Mayor Tadi", null);
        setLovedItems(loved);
        setLikedItems(liked);
        setHatedItems(hated);
    }
    
}