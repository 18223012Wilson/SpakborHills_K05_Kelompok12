public class NPC extends Entity{
    private int maxHeartPoints;
    private int heartPoints;
    // private Item[] lovedItems;
    // private Item[] likedItems;
    // private Item[] hatedItems;
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
        // check exception
        this.heartPoints = newHeartPoints;
    }

    public String getRelationshipStatus(){
        return relationshipStatus;
    }

    public void setRelationshipStatus(String newRelationshipStatus){
        // check exception
        this.relationshipStatus = newRelationshipStatus; 
    }
}