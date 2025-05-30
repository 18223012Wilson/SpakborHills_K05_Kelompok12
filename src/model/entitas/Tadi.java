package model.entitas;

public class Tadi extends NPC{
    private static final String IMAGE_PATH = "/npcs/tad.png";
    private String[] loved = new String[]{"Legend"};
    private String[] liked = new String[]{"Angler", "CrimsonFish", "GlacierFish"};
    private String[] hated = new String[]{};

    public Tadi(){
        super("Mayor Tadi", null, IMAGE_PATH);
        setLovedItems(loved);
        setLikedItems(liked);
        setHatedItems(hated);
    }
}