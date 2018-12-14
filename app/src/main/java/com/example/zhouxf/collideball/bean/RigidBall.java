package com.example.zhouxf.collideball.bean;

import com.example.zhouxf.collideball.CollideEvent;

import java.util.List;

/**
 * 二维平面上的刚体小球
 */
public class RigidBall extends CollidedObject {

    /**
     * 质量
     */
    private float mass;

    /**
     * 半径
     */
    private float radius;
    /**
     * 圆心
     */
    private Point centerPoint;

    /**
     * 速度
     */
    private VectorTwo velocity;

    public RigidBall(float mass, float radius, Point centerPoint, VectorTwo velocity) {
        this.mass = mass;
        this.radius = radius;
        this.centerPoint = centerPoint;
        this.velocity = velocity;
    }

    public RigidBall(RigidBall ball){
        this.mass = ball.mass;
        this.radius = ball.radius;
        this.centerPoint = ball.centerPoint;
        this.velocity = ball.velocity;
    }

    public RigidBall() {
    }

    /**
     * 得到运动1秒的圆心轨迹
     * @return 线段
     */
    public LineSegment getLine(){
        return new LineSegment(centerPoint,new Point(centerPoint.getX()+velocity.getX(),centerPoint.getY()+velocity.getY()));
    }

    /**
     * 没有碰撞情况下，运动一段时间之后,只有圆心位置改变
     * @param second 时间
     * @return this
     */
    public RigidBall timeLater(float second){
        centerPoint= new Point(centerPoint.getX()+second* velocity.getX(),
                centerPoint.getY()+second* velocity.getY());
        return this;
    }

    /**
     * 没有碰撞情况下，运动一段时间之后,只有圆心位置改变
     * @param ball 球
     * @param second 时间
     * @return new ball
     */
    public static RigidBall timeLater(RigidBall ball,float second){
        Point newCenter=new Point(ball.centerPoint.getX()+second*ball.velocity.getX(),
                ball.centerPoint.getY()+second*ball.velocity.getY());
        return new RigidBall(ball.getMass(),ball.getRadius(),newCenter,ball.getVelocity());
    }

    /**
     * 得到球与一条线段的相遇时间
     * @param lineSegment 线段
     * @return 二位向量组 time[0]的取值：0 表示不相遇；
     *                                   1 表示与线段startPoint相遇；
     *                                  2 表示与线段endPoint相遇；
     *                                 3 表示与线段内的点相遇；
     *                  time[1]为相遇时间，不可取负值，零值只有一种情况：球速度与线段平行
     */
    public float[] getMinTimeOfCollideLine(LineSegment lineSegment){
        float[] time={0,0};

        //圆心指向直线，与直线垂直的向量
        VectorTwo vectorPpDir=LineSegment.getPpFromPointToLine(centerPoint,lineSegment);
        VectorTwo vectorR=VectorTwo.scalarProduct(radius,vectorPpDir.unit());

        VectorTwo centerToStartPoint=new VectorTwo(centerPoint,lineSegment.getStartPoint());
        VectorTwo centerToEndPoint=new VectorTwo(centerPoint,lineSegment.getEndPoint());

        //球心 至 线段的距离   centerPoint To lineSegment
        float disCenterToLine=LineSegment.getDisFromPointToLine(centerPoint,lineSegment);
        //球心 至 线段startPoint的距离   centerPoint To startPoint
        float disCenterToStart=centerToStartPoint.magnitude();
        //球心 至 线段endPoint的距离   centerPoint To endPoint
        float disCenterToEnd=centerToEndPoint.magnitude();
        float disStartToBall=LineSegment.getDisFromPointToLine(lineSegment.getStartPoint(),getLine());
        float disEndToBall=LineSegment.getDisFromPointToLine(lineSegment.getEndPoint(),getLine());

        VectorTwo centerToStartPoint_1=VectorTwo.sub(centerToStartPoint,vectorR);
        VectorTwo centerToEndPoint_1=VectorTwo.sub(centerToEndPoint,vectorR);

        //球只能与线段的两个端点相遇
        if (disCenterToLine<=radius){
            //球离线段的startPoint更近
            if (centerToStartPoint.magnitude()<centerToEndPoint.magnitude()){
                //球的轨迹会与startPoint相遇且方向指向startPoint
                if (disStartToBall<radius&&velocity.dotProduct(centerToStartPoint)>0){
                    time[0]=1;
                    //球与端点相遇时运动的距离除以速度
                    if (velocity.magnitude()==0) time[1]=0;
                    else
                    time[1]= (float) ((Math.sqrt(disCenterToStart*disCenterToStart-disStartToBall*disStartToBall)
                                                -Math.sqrt(radius*radius-disStartToBall*disStartToBall))/velocity.magnitude());
                }
            }
            //球离线段的endPoint更近
            else {
                if (disEndToBall<radius&&velocity.dotProduct(centerToEndPoint)>0){
                    time[0]=2;
                    if (velocity.magnitude()==0) time[1]=0;
                    else
                        time[1]= (float) ((Math.sqrt(disCenterToEnd*disCenterToEnd-disEndToBall*disEndToBall)
                                                -Math.sqrt(radius*radius-disEndToBall*disEndToBall))/velocity.magnitude());
                }
            }
        }
        //球能与线段端点相遇，也能与线段内的点相遇
        else {
            if (velocity.isBetween(centerToStartPoint_1,centerToEndPoint_1)){
                time[0]=3;
                //球与线段内的点相遇时，用 点到直线的距离 除以 速度在垂直直线方向上的投影 求出
                if (VectorTwo.getProjection(velocity,vectorPpDir)==0) time[1]=0;
                else time[1]=(disCenterToLine-radius)/VectorTwo.getProjection(velocity,vectorPpDir);
            }
            else if (disStartToBall<radius&&velocity.dotProduct(centerToStartPoint)>0){
                time[0]=1;
                if (velocity.magnitude()==0) time[1]=0;
                else
                    time[1]= (float) ((Math.sqrt(disCenterToStart*disCenterToStart-disStartToBall*disStartToBall)
                        -Math.sqrt(radius*radius-disStartToBall*disStartToBall))/velocity.magnitude());

            }
            else if (disEndToBall<radius&&velocity.dotProduct(centerToEndPoint)>0){
                time[0]=2;
                if (velocity.magnitude()==0) time[1]=0;
                else
                    time[1]= (float) ((Math.sqrt(disCenterToEnd*disCenterToEnd-disEndToBall*disEndToBall)
                        -Math.sqrt(radius*radius-disEndToBall*disEndToBall))/velocity.magnitude());
            }
        }
//        Log.e("RigidBall",lineSegment.toString()+",type="+time[0]+",time="+time[1]);
        return time;
    }

    /**
     * 得到球与一组边界相遇的最短时间
     * @param ball 球
     * @param lineList 边界集合由多根线段模拟
     * @return CollideEvent
     */
    public static CollideEvent getMinTimeOfCollideLine(RigidBall ball, List<LineSegment> lineList){
        float[] types=new float[lineList.size()];
        float[] times=new float[lineList.size()];
        float minTime=0;
        CollideEvent event=new CollideEvent();

        for (int i=0;i<lineList.size();i++){
            types[i]=ball.getMinTimeOfCollideLine(lineList.get(i))[0];
            times[i]=ball.getMinTimeOfCollideLine(lineList.get(i))[1];
            //得到非负最小值minTime
            if (times[i]>0){
                if (minTime==0){
                    minTime=times[i];
                }
                if (minTime>times[i]){
                    minTime=times[i];
                }
            }
        }
        event.collideTime =minTime;
        for (int i=0;i<times.length;i++){
            RigidBall ball_2=RigidBall.timeLater(ball,times[i]);
//            Log.e("RigidBall","i="+i+",time="+times[i]+"ball_2="+ball_2.toString());
            if (minTime>0&&times[i]==minTime){
                if (types[i]==1) {
                    event.pointList.add(lineList.get(i).getStartPoint());
//                    Log.e("RigidBall","type=1,dis="+new VectorTwo(ball_2.centerPoint,lineList.get(i).getStartPoint()).magnitude());
                }
                else if (types[i]==2) {
                    event.pointList.add(lineList.get(i).getEndPoint());
//                    Log.e("RigidBall","type=2,dis="+new VectorTwo(ball_2.centerPoint,lineList.get(i).getEndPoint()).magnitude());
                }
                else if (types[i]==3) {
                    event.lineList.add(lineList.get(i));
//                    Log.e("RigidBall","type=3,dis="+LineSegment.getDisFromPointToLine(ball_2.centerPoint,lineList.get(i)));
                }
            }
        }
//        Log.e("RigidBall",event.toString());
        return event;
    }


    /**
     * 与线段碰撞后，只有速度的方向发生改变
     * @param ball ball
     * @param event 与哪一条线段碰撞
     * @return new ball
     */
    public static RigidBall collideLine(RigidBall ball, CollideEvent event){
        RigidBall afterCollide=timeLater(ball,event.collideTime);
        switch (event.getType()){
            case CollideEvent.COLLIDE_POINT:
                afterCollide.velocity=collidePoint(afterCollide,event.pointList);
                break;
            case CollideEvent.COLLIDE_LINE:
                afterCollide.velocity=collideInLine(afterCollide,event.lineList);
                break;
            case CollideEvent.COLLIDE_LINE_POINT:
                afterCollide.velocity=collideLine_Point(afterCollide,event.lineList,event.pointList);
                break;
            default:
                break;
        }
//        Log.e("Rigid","outBall="+ball.toString());
        return afterCollide;
    }

    /**
     * 与线段内的点碰撞后，速度在 所受合力方向 上的分速度改变
     * @param pointList 与哪几条线段同时碰撞
     *  碰撞后的速度=速度-2*(速度在所受合力方向上的投影)
     */
    public static VectorTwo collidePoint(RigidBall ball,List<Point> pointList){
        VectorTwo forceDirection=new VectorTwo(0,0);
        for (Point point:pointList){
            //受到一个点的力的方向 与 该点指向球心的向量 方向相同
            forceDirection.add(new VectorTwo(point,ball.centerPoint));
        }
        return VectorTwo.sub(ball.velocity,VectorTwo.getProjectionVector(ball.velocity,forceDirection).scalarProduct(2));
    }

    /**
     * 与线段内的点碰撞后，速度在 所受合力方向 上的分速度改变
     * @param lineList 与哪几条线段同时碰撞
     *  碰撞后的速度=速度-2*(速度在所受合力方向上的投影)
     */
    public static VectorTwo collideInLine(RigidBall ball,List<LineSegment> lineList){
        VectorTwo forceDirection=new VectorTwo(0,0);
        for (LineSegment line:lineList){
            //受到一条直线的力的方向 与 球心指向直线的垂直向量 方向相反
            forceDirection.sub(LineSegment.getPpFromPointToLine(ball.centerPoint,line).unit());
        }

        return VectorTwo.sub(ball.velocity,VectorTwo.getProjectionVector(ball.velocity,forceDirection).scalarProduct(2));
    }
    /**
     * 与线段内的点和其他线段的端点同时碰撞，速度在 所受合力方向 上的分速度改变
     * @param lineList 与哪几条线段同时碰撞
     *  碰撞后的速度=速度-2*(速度在所受合力方向上的投影)
     */
    public static VectorTwo collideLine_Point(RigidBall ball,List<LineSegment> lineList, List<Point> pointList){
        VectorTwo forceDirection=new VectorTwo(0,0);
        for (LineSegment line:lineList){
            //受到一条直线的力的方向 与 球心指向直线的垂直向量 方向相反
            forceDirection.sub(LineSegment.getPpFromPointToLine(ball.centerPoint,line).unit());
        }
        for (Point point:pointList){
            //受到一个点的力的方向 与 该点指向球心的向量 方向相同
            forceDirection.add(new VectorTwo(point,ball.centerPoint).unit());
        }
        return VectorTwo.sub(ball.velocity,VectorTwo.getProjectionVector(ball.velocity,forceDirection).scalarProduct(2));
    }


    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public Point getCenterPoint() {
        return centerPoint;
    }

    public void setCenterPoint(Point centerPoint) {
        this.centerPoint = centerPoint;
    }

    public VectorTwo getVelocity() {
        return velocity;
    }

    public void setVelocity(VectorTwo velocity) {
        this.velocity = velocity;
    }

    @Override
    public String toString() {
        return "{" +
                "mass=" + mass +
                ", radius=" + radius +
                ", centerPoint=" + centerPoint.toString() +
                ", velocity=" + velocity.toString() +
                '}';
    }
}
