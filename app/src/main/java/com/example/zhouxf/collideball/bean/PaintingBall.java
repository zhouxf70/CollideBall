package com.example.zhouxf.collideball.bean;

import com.example.zhouxf.collideball.view.DrawView;

public class PaintingBall extends RigidBall {

    public int id;

    public float startTime;

    public float endTime;

    public int color;

    public PaintingBall(RigidBall ball, int color) {
        super(ball);
        this.color = color;
        startTime=0;
        endTime=0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
