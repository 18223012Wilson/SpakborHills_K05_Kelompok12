package model.entitas;

import model.items.Point;

public abstract class NPC extends Entity {
    private int maxHeartPoints;
    private int heartPoints;
    private String[] lovedItems = null;
    private String[] likedItems = null;
    private String[] hatedItems = null;
    private String relationshipStatus;
    private String imagePath;

    public NPC(String name, String location, String imagePath) {
        super(name, location);
        this.maxHeartPoints = 150;
        this.heartPoints = 0;
        this.relationshipStatus = "single";
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getMaxHeartPoints() {
        return maxHeartPoints;
    }

    public int getHeartPoints() {
        return heartPoints;
    }

    public void setHeartPoints(int newHeartPoints) {
        this.heartPoints = Math.max(0, Math.min(newHeartPoints, this.maxHeartPoints));
    }

    public String getRelationshipStatus() {
        return relationshipStatus;
    }

    public void setRelationshipStatus(String newRelationshipStatus) {
        this.relationshipStatus = newRelationshipStatus;
    }

    public String[] getLovedItems() {
        return lovedItems;
    }

    public void setLovedItems(String[] newLovedItems) {
        this.lovedItems = newLovedItems;
    }

    public String[] getLikedItems() {
        return likedItems;
    }

    public void setLikedItems(String[] newLikedItems) {
        this.likedItems = newLikedItems;
    }

    public String[] getHatedItems() {
        return hatedItems;
    }

    public void setHatedItems(String[] newHatedItems) {
        this.hatedItems = newHatedItems;
    }
}