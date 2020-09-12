package com.example.crazyparking;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class GameBoard extends View
{
     int thickness=0;
     int norow,nocol=10;
     CarInfo Car1;
     boolean isCarTouched=true;

     ValueAnimator CarAnimator;

     CollisionObjects CollisionObj;



     Bitmap CoinsImage;
     Bitmap PoliceImage;
     Bitmap houseimage,bankimage;
     GAMEinfo gamEinfo;
     int animationtime=4000;



    public GameBoard(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        CoinsImage = BitmapFactory.decodeResource(getResources(),R.drawable.money_image,null);
        PoliceImage = BitmapFactory.decodeResource(getResources(),R.drawable.policeimage,null);
        houseimage=BitmapFactory.decodeResource(getResources(),R.drawable.houseimage,null);
        bankimage=BitmapFactory.decodeResource(getResources(),R.drawable.bankimage,null);



        //Log.d("hello", Car1.FindAngle(new Point(0,1),new Point(0,0), new Point(1,0)) + "");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int posx=(int) event.getX();
        int posy=(int) event.getY();

        if (isCarTouched) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    MainActivity.engine_reviving_sound.start();
                    MainActivity.engine_reviving_sound.setLooping(true);
                    Car1.ClearTrajectory();
                    Car1.AddPoint(new Point(posx,posy));
                    invalidate();
                    return true;
                case  MotionEvent.ACTION_MOVE:
                    Point lastpoint = Car1.trajectoryPoint.get(Car1.trajectoryPoint.size() - 1);
                    float distance = (float) Math.sqrt(Math.pow(lastpoint.x - posx,2) + Math.pow(lastpoint.y - posy,2));
                    if (distance > 50)
                        Car1.AddPoint(new Point(posx,posy));
                    break;

                case MotionEvent.ACTION_UP:
                    MainActivity.engine_reviving_sound.stop();
                    //Endpoint Logic
                    //Car1.AddPoint(new Point(posx,posy));
                    if (Car1.isEnded())
                    {
                        isCarTouched = false;
                        Animate(0);
                    }
                    else
                    {
                        Car1.ClearTrajectory();
                    }
                    invalidate();
                    break;
            }
        }
        invalidate();
        return super.onTouchEvent(event);
    }

    public void Animate(final int index)
    {
        if (index < Car1.trajectoryPoint.size() - 1) {
            if (!MainActivity.engine_sound.isPlaying())
            {
                MainActivity.engine_sound.start();
                MainActivity.engine_sound.setLooping(true);
            }
            CarAnimator = ValueAnimator.ofInt(0, 100);
            CarAnimator.setDuration(animationtime/Car1.trajectoryPoint.size());

            if (index > 0 && index < Car1.trajectoryPoint.size() - 1)
                Car1.RotateMatrix(Car1.FindAngle(Car1.trajectoryPoint.get(index), Car1.trajectoryPoint.get(index + 1)));
            else if (index == 0)
                Car1.RotateMatrix(Car1.FindAngle(Car1.trajectoryPoint.get(0), Car1.trajectoryPoint.get(1)));

            CarAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int upadtedvalue = (int) animation.getAnimatedValue();
                    int x= Car1.trajectoryPoint.get(index).x + (int)((upadtedvalue / 100f) * (Car1.trajectoryPoint.get(index + 1).x - Car1.trajectoryPoint.get(index).x));
                    int y= Car1.trajectoryPoint.get(index).y + (int)((upadtedvalue / 100f) * (Car1.trajectoryPoint.get(index + 1).y - Car1.trajectoryPoint.get(index).y));
                    Car1.CarCurrentPosition = new Point(x, y);
                    if (CollisionObj.isHit(Car1.CarCurrentPosition))
                    {
                        Log.d("Hello1","Hi");
                        CarAnimator.removeAllListeners();
                        CarAnimator.cancel();
                        MainActivity.engine_sound.stop();
                        MainActivity.sire_sound.start();
                        MainActivity.caught_window.animate().alpha(1).setDuration(2000);
                        Handler A = new Handler();
                        A.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getContext().startActivity(new Intent(getContext(),GameMenu.class));
                                MainActivity.sire_sound.stop();
                                ((MainActivity)getContext()).finish();

                            }
                        },2100);
                    }
                    invalidate();
                }
            });
            CarAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    Animate(index + 1);
                }
            });
            CarAnimator.start();
        }
        else
        {
            Car1.CarCurrentPosition = Car1.EndPoint;
            MainActivity.engine_sound.stop();
            MainActivity.escape_sound.start();
            MainActivity.escape_window.animate().alpha(1).setDuration(2000);

            Handler A = new Handler();
            A.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getContext().startActivity(new Intent(getContext(),GameMenu.class));
                    MainActivity.escape_sound.stop();
                    ((MainActivity)getContext()).finish();
                }
            },2100);

        }
    }

    public void setGamEinfo(GAMEinfo gamEinfo)
    {
        this.gamEinfo = gamEinfo;
        if (CollisionObj != null)
        CollisionObj.gamEinfo = gamEinfo;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if (Car1 == null)
        {
            SharedPreferences S = getContext().getSharedPreferences("carid",Context.MODE_PRIVATE);
            Car1 = new CarInfo(Color.RED, BitmapFactory.decodeResource(getResources(),getResources().getIdentifier("carimage" + S.getInt("car",1),"drawable",getContext().getPackageName()),null),new Point(getWidth()/2 ,getHeight() - (int) (100 * getResources().getDisplayMetrics().density)),new Point(getWidth()/2, (int) (100 * getResources().getDisplayMetrics().density)));
            CollisionObj = new CollisionObjects(getWidth(),getHeight(), new Point(getWidth()/2 ,getHeight() - (int) (100 * getResources().getDisplayMetrics().density)),new Point(getWidth()/2, (int) (100 * getResources().getDisplayMetrics().density)),gamEinfo);

            if (S.getInt("car",1) == 1)
            {
                animationtime = 4000;
            }
            else if (S.getInt("car",1) == 2)
            {
                animationtime = 3500;
            }
            else
            {
                animationtime = 3000;
            }


        }
        canvas.drawPath(Car1.getPath(),Car1.CarPaint);

        Paint CirclePaint = new Paint();
        CirclePaint.setColor(Color.RED);
        CirclePaint.setStyle(Paint.Style.FILL);
        canvas.drawBitmap(houseimage,null,new Rect(Car1.EndPoint.x - 150,Car1.EndPoint.y - 150,Car1.EndPoint.x + 150, Car1.EndPoint.y + 150), CirclePaint);
        //canvas.drawCircle(Car1.EndPoint.x,Car1.EndPoint.y,100,CirclePaint);
        canvas.drawBitmap(bankimage,null,new Rect(Car1.CarStartPosition.x-150,Car1.CarStartPosition.y-150,Car1.CarStartPosition.x+150,Car1.CarStartPosition.y + 150),CirclePaint);
        canvas.drawBitmap(Bitmap.createBitmap(Car1.CarImage,0,0,100,100,Car1.RotateMatrix,true),null,new Rect(Car1.CarCurrentPosition.x - 75,Car1.CarCurrentPosition.y - 75, Car1.CarCurrentPosition.x + 75,Car1.CarCurrentPosition.y + 75),CirclePaint);


        for (int i = 0; i < CollisionObj.Obstacles.size();i++)
        {
            canvas.drawBitmap(PoliceImage,null,new Rect(CollisionObj.Obstacles.get(i).x - (CollisionObj.Thickness/2),CollisionObj.Obstacles.get(i).y - (CollisionObj.Thickness/2),CollisionObj.Obstacles.get(i).x + (CollisionObj.Thickness/2),CollisionObj.Obstacles.get(i).y + (CollisionObj.Thickness/2)),CirclePaint);
        }

        for (int i = 0; i < CollisionObj.Coins.size();i++)
        {
            canvas.drawBitmap(CoinsImage,null,new Rect(CollisionObj.Coins.get(i).x - (CollisionObj.Thickness/2),CollisionObj.Coins.get(i).y - (CollisionObj.Thickness/2),CollisionObj.Coins.get(i).x + (CollisionObj.Thickness/2),CollisionObj.Coins.get(i).y + (CollisionObj.Thickness/2)),CirclePaint);
        }

    }
}
