package com.example.zhouxf.collideball;

/**
 * 两点之间的最短距离：线段
 * 该线段有方向（分起始点和终止点）
 */
public class LineSegment extends CollidedObject {

    private Point startPoint;
    private Point endPoint;

    public LineSegment() {
    }

    public LineSegment(Point startPoint, Point endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }
    public LineSegment(LineSegment lineSegment){
        this.startPoint =lineSegment.startPoint;
        this.endPoint=lineSegment.endPoint;
    }

    /**
     * 由直线得到相同方向的向量
     * @param lineSegment 直线
     * @return 向量
     */
    public static VectorTwo getVector2(LineSegment lineSegment){
        return new VectorTwo(lineSegment.endPoint.getX()-lineSegment.startPoint.getX(),
                lineSegment.endPoint.getY()-lineSegment.startPoint.getY());
    }

    /**
     * 判断点是否在线段上
     * @param point
     * @return
     */
    public Boolean isPointOnLine(Point point){
        //v1 是由startPoint指向point的向量
        VectorTwo v1=new VectorTwo(point.getX()-startPoint.getX(),point.getY()-startPoint.getY());
        //v2 是由point指向endPoint的向量
        VectorTwo v2=new VectorTwo(endPoint.getX()-point.getX(),endPoint.getY()-point.getY());
        return VectorTwo.sameDirection(v1,v2);
    }


    /**
     * 得到线外一点指向直线的垂直向量（只有方向）
     * @return 当点在直线上时，返回一个与直线垂直的向量;
     *          当线段只是一个点时，返回零向量
     */
    public static VectorTwo getPpFromPointToLine(Point point, LineSegment lineSegment){
        //先得到垂直于直线的向量
        VectorTwo perpendicularToLine= VectorTwo.getPerpendicular(getVector2(lineSegment));
        if (VectorTwo.dotProduct(perpendicularToLine,new VectorTwo(point,lineSegment.endPoint))<0){
            perpendicularToLine.opposite();
        }
        return perpendicularToLine;
    }

    /**
     * 得到 点到直线的距离
     * @param point point
     * @param lineSegment lineSegment
     * @return 距离（非负值）
     */
    public static float getDisFromPointToLine(Point point, LineSegment lineSegment){
        return VectorTwo.getProjection(new VectorTwo(point,lineSegment.getStartPoint()),
                getPpFromPointToLine(point,lineSegment));
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
    }

    @Override
    public String toString() {
        return "LineSegment{" +
                "startPoint=" + startPoint.toString() +
                ", endPoint=" + endPoint.toString() +
                '}';
    }
}
