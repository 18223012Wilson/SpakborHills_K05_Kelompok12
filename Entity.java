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

    public Point location(){
        return location;
    }

    public void ShowLocation(){
        System.out.println("Lokasi saat ini adalah (" + location.getX() + ", " + location.getY() + ")");
    }

    public void moveTo(Point newLocation){
        // check exception
        this.location = newLocation;
    }
}