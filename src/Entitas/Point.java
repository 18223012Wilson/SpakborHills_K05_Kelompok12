package Entitas;

public class Point {
    private int x = 0;
    private int y = 0;
    
    public Point(int a, int b) {
        x = a; 
        y = b;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }
}