package com.example.zhouxf.collideball.view;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.zhouxf.collideball.LineSegment;
import com.example.zhouxf.collideball.MainActivity;
import com.example.zhouxf.collideball.Point;
import com.example.zhouxf.collideball.RigidBall;
import com.example.zhouxf.collideball.VectorTwo;

import java.util.ArrayList;
import java.util.List;

public class DrawView extends View {

    List<LineSegment> lineList;
    List<RigidBall> ballList;
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

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (lineList!=null){
            canvas.drawPath(path_border,paint_border);
        }
        if (ballList!=null&& MainActivity.isShowPath){
            canvas.drawPath(path_ball,paint_ball_path);
        }
        if (currentPoint!=null){
            canvas.drawCircle(currentPoint.getX(),currentPoint.getY(),ballList.get(0).getRadius(),paint_ball);
        }
    }

    public void createBorder(List<LineSegment> lineList){
        this.lineList=lineList;
        for (LineSegment line:lineList){
//            Log.e("DrawView",line.toString());
            path_border.moveTo(line.getStartPoint().getX(),line.getStartPoint().getY());
            path_border.lineTo(line.getEndPoint().getX(),line.getEndPoint().getY());
        }
        invalidate();
    }

    public void drawBallPath(List<RigidBall> balls,List<Float> times){
        this.ballList=balls;
        this.timeList=times;
        for (int i=0;i<balls.size();i++){
            Log.e("Draw",ballList.get(i).toString());
        }
        centerPointList=new ArrayList<>();
        velocityList=new ArrayList<>();
        centerPointList.add(ballList.get(0).getCenterPoint());
        velocityList.add(ballList.get(0).getVelocity());
        path_ball.moveTo(centerPointList.get(0).getX(),centerPointList.get(0).getY());
        for (int i=1;i<ballList.size();i++){

            centerPointList.add(ballList.get(i).getCenterPoint());
            velocityList.add(ballList.get(i).getVelocity());

            path_ball.lineTo(centerPointList.get(i).getX(),centerPointList.get(i).getY());
        }
        startAnimation();
        invalidate();
    }

    private void startAnimation(){
        duration=timeList.get(timeList.size() - 1);
        final ValueAnimator animator=ValueAnimator.ofObject(new PointEvaluator(),centerPointList.get(centerPointList.size()-1));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentPoint= (Point) animator.getAnimatedValue();
                Log.e("DrawView",currentPoint.toString());
                invalidate();
//                Log.e("DrawView","value="+animation.getAnimatedValue());
            }
        });
        animator.setDuration((long) duration*1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
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
}
