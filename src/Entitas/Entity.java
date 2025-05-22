package Entitas;

public abstract class Entity{
    private String name;
    private String location;
    private Point coordinate;

    public Entity(String name, String location){
        this.name = name;
        this.location = location;
        coordinate.setX(0);
        coordinate.setY(0);
    }

    public String getName(){
        return name;
    }

    public String getLocation(){
        return location;
    }

    public Point getCoordinate(){
        return coordinate;
    }

    public void showLocation(){
        System.out.println("Kamu berada di " + location);
    }

    public void ShowCoordinate(){
        System.out.println("Kamu berada di " + coordinate.getX() + "," + coordinate.getY());
    }

    public void moveTo(Point newCoordinate){
        this.coordinate = newCoordinate;
    }
}