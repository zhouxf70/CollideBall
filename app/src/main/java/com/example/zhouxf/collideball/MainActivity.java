package com.example.zhouxf.collideball;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.zhouxf.collideball.view.DrawView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 撞击次数
     */
    public final static int collideNum=30;
    /**
     * 是否显示小球轨迹
     */
    public static boolean isShowPath=true;

    public static List<LineSegment> lineList;

    private DrawView drawView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        createBorder();


        List<RigidBall> ballList=new ArrayList<>();
        List<Float> timeList=new ArrayList<>();
        timeList.add((float) 0);
        ballList.add(new RigidBall(0,15,new Point(30,30),new VectorTwo(70,52)));

        CollideEvent event;
        for (int i=0;i<collideNum;i++){
            event=RigidBall.getMinTimeOfCollideLine(ballList.get(i),lineList);
            if (event.collideTime==0){
                Log.e("Main","没有相遇事件发生");
                break;
            }
//            Log.e("Main","将要发生的相遇事件："+event.toString());
//            times[i+1]=event.collideTime+times[i];
            timeList.add(event.collideTime+timeList.get(i));
//            ball_1[i+1]=RigidBall.collideLine(ball_1[i],event);
            ballList.add(RigidBall.collideLine(ballList.get(i),event));
            Log.e("Main","碰撞的第"+i+"次，time="+timeList.get(i)+"，ball="+ballList.get(i).toString());
        }

        drawView.drawBallPath(ballList,timeList);
    }

    private void initView(){

        drawView=findViewById(R.id.drawView);

        Button bt_stop = findViewById(R.id.bt_stop);
        Button bt_start = findViewById(R.id.bt_start);
        Button bt_show_path = findViewById(R.id.bt_show_path);
        Button bt_add_ball = findViewById(R.id.bt_add_ball);
        bt_stop.setOnClickListener(this);
        bt_start.setOnClickListener(this);
        bt_show_path.setOnClickListener(this);
        bt_add_ball.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_stop:
                break;
            case R.id.bt_start:
                break;
            case R.id.bt_show_path:
                isShowPath=!isShowPath;
                break;
            case R.id.bt_add_ball:
                createBall();
                break;
        }
    }

    /**
     * 绘制边界
     */
    private void createBorder(){
        Point point=new Point(0,0);
        Point point1=new Point(1000,0);
        Point point2=new Point(1000,500);
        Point point3=new Point(0,500);

        Point point4=new Point(100,80);
        Point point5=new Point(400,100);
        Point point6=new Point(200,200);

        Point point7=new Point(700,200);
        Point point8=new Point(850,420);
        Point point9=new Point(560,325);

        List<LineSegment> lineList=new ArrayList<>();

        LineSegment line1=new LineSegment(point,point1);
        LineSegment line2=new LineSegment(point1,point2);
        LineSegment line3=new LineSegment(point2,point3);
        LineSegment line4=new LineSegment(point3,point);

        LineSegment line5=new LineSegment(point4,point5);
        LineSegment line6=new LineSegment(point5,point6);
        LineSegment line7=new LineSegment(point6,point4);

        LineSegment line8=new LineSegment(point7,point8);
        LineSegment line9=new LineSegment(point8,point9);
        LineSegment line10=new LineSegment(point9,point7);

        lineList.add(line1);
        lineList.add(line2);
        lineList.add(line3);
        lineList.add(line4);
        lineList.add(line5);
        lineList.add(line6);
        lineList.add(line7);
        lineList.add(line8);
        lineList.add(line9);
        lineList.add(line10);

        drawView.createBorder(lineList);
    }

    /**
     * 向drawView内添加一个RigidBall
     */
    private void createBall(){

    }
}
