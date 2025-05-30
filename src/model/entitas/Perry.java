package model.entitas;

public class Perry extends NPC{
    private static final String IMAGE_PATH = "/npcs/perry.png";
    private String[] loved = new String[]{"Cranberry", "Blueberry"};
    private String[] liked = new String[]{"Wine"};
    private String[] hated = new String[]{"Fish"};

    public Perry(){
        super("Perry", null,IMAGE_PATH);
        setLovedItems(loved);
        setLikedItems(liked);
        setHatedItems(hated);
    }

}