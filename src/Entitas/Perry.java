package Entitas;

public class Perry extends NPC{
    private String[] loved = new String[]{"Cranberry", "Blueberry"};
    private String[] liked = new String[]{"Wine"};
    private String[] hated = new String[]{"Fish"};//Seluruh jenis fish
    
    public Perry(){
        super("Perry", null);
        setLovedItems(loved);
        setLikedItems(liked);
        setHatedItems(hated);
    }
    
}