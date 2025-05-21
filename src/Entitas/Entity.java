package Entitas;

public abstract class Entity{
    private String name;
    private String location;

    public Entity(String name, String location){
        this.name = name;
        this.location = location;
    }

    public String getName(){
        return name;
    }

    public String getLocation(){
        return location;
    }

    public void ShowLocation(){
        System.out.println("Kamu berada di " + getLocation());
    }

    public void moveTo(String newLocation){
        this.location = newLocation;
    }
}