package model.entitas;

public class Caroline extends NPC{
    private static final String IMAGE_PATH = "/npcs/caroline.png";
    private String[] loved = new String[]{"Firewood", "Coal"};
    private String[] liked = new String[]{"Potato", "Wheat"};
    private String[] hated = new String[]{"Hot Pepper"};

    public Caroline(){
        super("Caroline", null, IMAGE_PATH);
        setLovedItems(loved);
        setLikedItems(liked);
        setHatedItems(hated);
    }

}