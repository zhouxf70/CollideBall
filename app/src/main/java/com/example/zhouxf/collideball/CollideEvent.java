package com.example.zhouxf.collideball;

import com.example.zhouxf.collideball.bean.LineSegment;
import com.example.zhouxf.collideball.bean.Point;
import com.example.zhouxf.collideball.bean.RigidBall;

import java.util.ArrayList;
import java.util.List;

/**
 * 碰撞事件
 */
public class CollideEvent {

    public final static String COLLIDE_NULL="collide_null";
    public final static String COLLIDE_LINE="collide_line";
    public final static String COLLIDE_POINT="collide_point";
    public final static String COLLIDE_BALL="collide_ball";
    public final static String COLLIDE_BALL_LINE="collide_ball_line";
    public final static String COLLIDE_BALL_POINT="collide_ball_point";
    public final static String COLLIDE_LINE_POINT="collide_line_point";
    public final static String COLLIDE_BALL_POINT_LINE="collide_ball_point_line";

    public float collideTime;

    public List<LineSegment> lineList;

    public List<Point> pointList;

    public List<RigidBall> ballList;

    public CollideEvent() {
        this.collideTime =0;
        this.lineList=new ArrayList<>();
        this.pointList=new ArrayList<>();
        this.ballList=new ArrayList<>();
    }

    public String getType(){
        if (lineList.isEmpty()){
            if (pointList.isEmpty()){
                if (ballList.isEmpty()) return COLLIDE_NULL;
                else return COLLIDE_BALL;
            }
            else {
                if (ballList.isEmpty()) return COLLIDE_POINT;
                else return COLLIDE_BALL_POINT;
            }
        }
        else {
            if (pointList.isEmpty()){
                if (ballList.isEmpty()) return COLLIDE_LINE;
                else return COLLIDE_BALL_LINE;
            }
            else {
                if (ballList.isEmpty()) return COLLIDE_LINE_POINT;
                else return COLLIDE_BALL_POINT_LINE;
            }
        }
    }

    @Override
    public String toString() {
        return "CollideEvent{在" + collideTime + "秒后与"
                + lineList.size()+"条线相遇；与"
                + pointList.size()+"个线段端点相遇；与"
                + ballList.size() + "个球相遇;type="
                +getType()
                + '}';
    }
}
