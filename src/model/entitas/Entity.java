package model.entitas;

import model.items.Point;

public abstract class Entity{
    private String name;
    private Point location;

    public Entity(String name, Point location){
        this.name = name;
        this.location = location;
    }

    public String getName(){
        return name;
    }

    public Point getLocation(){
        return location;
    }

    public void showLocation(){
        System.out.println("Current location is (" + location.getX() + ", " + location.getY() + ")");
    }

    public void moveTo(Point newLocation){
        this.location = newLocation;
    }
}