package com.example.zhouxf.collideball.bean;

/**
 * 点
 */
public class Point extends CollidedObject {

    private float x;

    private float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public  Boolean equal(Point point){
        return Point.equal(this,point);
    }

    public static Boolean equal(Point point1,Point point2){
        return point1.getX() == point2.getX() && point1.getY() == point2.getY();
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
