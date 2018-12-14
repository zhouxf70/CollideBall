package com.example.zhouxf.collideball.view;

import android.animation.TypeEvaluator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.zhouxf.collideball.CollideEvent;
import com.example.zhouxf.collideball.bean.LineSegment;
import com.example.zhouxf.collideball.MainActivity;
import com.example.zhouxf.collideball.bean.PaintingBall;
import com.example.zhouxf.collideball.bean.Point;
import com.example.zhouxf.collideball.bean.RigidBall;
import com.example.zhouxf.collideball.bean.VectorTwo;

import java.util.ArrayList;
import java.util.List;

public class DrawView extends View {

    /**
     * 边界集合
     */
    List<LineSegment> lineList;
    List<Float> timeList;
    List<Point> centerPointList;
    List<VectorTwo> velocityList;
    Point currentPoint;
    float duration;

    private Path path_border;
    private Path path_ball;

    private Paint paint_border;
    private Paint paint_ball_path;
    private Paint paint_ball;

    public List<PaintingBall> ballList;

    public Thread countThread;
    /**
     * 0.1秒计数一次
     */
    public int currentCount=0;
    public boolean isCounting =false;

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //绘制碰撞边界的画笔
        paint_border=new Paint();
        paint_border.setAntiAlias(true);
        paint_border.setStrokeWidth(2);
        paint_border.setColor(Color.RED);
        paint_border.setStyle(Paint.Style.STROKE);
        path_border=new Path();

        //绘制小球轨迹的画笔
        paint_ball_path=new Paint();
        paint_ball_path.setAntiAlias(true);
        paint_ball_path.setStrokeWidth(2);
        paint_ball_path.setColor(Color.GREEN);
        paint_ball_path.setStyle(Paint.Style.STROKE);
        path_ball=new Path();

        //绘制小球的画笔
        paint_ball=new Paint();
        paint_ball.setColor(Color.BLUE);
        paint_ball.setStyle(Paint.Style.FILL);
        paint_ball_path.setAntiAlias(true);

        ballList=new ArrayList<>();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (lineList!=null){
            canvas.drawPath(path_border,paint_border);
        }

        if (!ballList.isEmpty()){
            for (PaintingBall ball:ballList){

            }
        }

        if (currentPoint!=null){
            canvas.drawCircle(currentPoint.getX(),currentPoint.getY(),ballList.get(0).getRadius(),paint_ball);
        }
    }

    public void createBorder(List<LineSegment> lineList){
        this.lineList=lineList;
        for (LineSegment line:lineList){
            path_border.moveTo(line.getStartPoint().getX(),line.getStartPoint().getY());
            path_border.lineTo(line.getEndPoint().getX(),line.getEndPoint().getY());
        }
        invalidate();
    }

    //fraction:用 10秒（动画持续时间） 从0数到1
    class PointEvaluator implements TypeEvaluator{

        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            float currentTime=fraction*duration;
            float x = 0;
            float y = 0;
            for (int i=0;i<centerPointList.size()-1;i++){
                Log.e("Draw",velocityList.get(i).toString());
                if (currentTime>=timeList.get(i)&&currentTime<timeList.get(i+1)){
                    float moveTime=currentTime-timeList.get(i);
                    x=centerPointList.get(i).getX()+moveTime*velocityList.get(i).getX();
                    y=centerPointList.get(i).getY()+moveTime*velocityList.get(i).getY();
//                    Log.e("Draw","i="+i+",currentTime="+currentTime+",startTime="+timeList.get(i)+",current="+new Point(x,y).toString());
                    break;
                }
            }
//            Log.e("Draw","currentTime="+currentTime);
            return new Point(x,y);
        }
    }

    public void addBall(PaintingBall ball){
        calculateCollideTime(ball);
        ballList.add(ball);
    }

    public void startAnimation(){

        if (MainActivity.isStopThread){
            countThread=new Thread(new CountThread());
            MainActivity.isStopThread=false;
            countThread.start();
        }
        isCounting =true;
    }

    class CountThread implements Runnable {

        @Override
        public void run() {
            while (!MainActivity.isStopThread){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (isCounting) {
                    currentCount++;
                    invalidate();
                }
            }
        }
    }

    /**
     * 计算小球的下一次碰撞时间
     * @param ball
     */
    private void calculateCollideTime(PaintingBall ball){
        ball.startTime=ball.endTime;
        ball.endTime+=RigidBall.getMinTimeOfCollideLine(ball,lineList).collideTime;
        Log.e("Draw","startTime="+ball.startTime+"，endTime="+ball.endTime);
    }
}
