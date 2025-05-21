package model.entitas;

import model.items.Point;

public class NPC extends Entity{
    private int maxHeartPoints;
    private int heartPoints;
    private String relationshipStatus;

    public NPC(String name, Point location){
        super(name, location);
        this.maxHeartPoints = 150;
        this.heartPoints = 0;
        this.relationshipStatus = "single";
    }

    public int getMaxHeartPoints(){
        return maxHeartPoints;
    }

    public int getHeartPoints(){
        return heartPoints;
    }

    public void setHeartPoints(int newHeartPoints){
        this.heartPoints = newHeartPoints;
    }

    public String getRelationshipStatus(){
        return relationshipStatus;
    }

    public void setRelationshipStatus(String newRelationshipStatus){
        this.relationshipStatus = newRelationshipStatus;
    }
}