package com.example.crazyparking;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class CarInfo
{
    List<Point> trajectoryPoint;
    Paint CarPaint;
    Bitmap CarImage;
    Point CarStartPosition;
    Point EndPoint;
    Point CarCurrentPosition;

    Matrix RotateMatrix;

    public CarInfo(int color,Bitmap CarImage,Point CarPosition,Point EndPoint)
    {
        CarPaint = new Paint();
        CarPaint.setColor(color);
        CarPaint.setStrokeWidth(40f);
        CarPaint.setStyle(Paint.Style.STROKE);
        CarPaint.setStrokeJoin(Paint.Join.ROUND);

        trajectoryPoint = new ArrayList<>();
        this.CarImage = CarImage;
        this.CarStartPosition = CarPosition;
        this.EndPoint=EndPoint;
        this.CarCurrentPosition = CarPosition;

        this.CarImage = Bitmap.createScaledBitmap(CarImage,100,100,false);
        RotateMatrix = new Matrix();
    }

    public boolean isEnded()
    {
        Point f = trajectoryPoint.get(trajectoryPoint.size() - 1);
        float distance = (float) Math.sqrt(Math.pow(f.x - EndPoint.x,2) + Math.pow(f.y - EndPoint.y,2));

        if (distance <100 )
        {
            trajectoryPoint.add(EndPoint);
            return true;
        }
        else
        {
            return false;
        }
    }

    public void AddPoint(Point A)
    {
        trajectoryPoint.add(A);
    }

    public void ClearTrajectory()
    {
        trajectoryPoint = null;
        trajectoryPoint = new ArrayList<Point>();
        trajectoryPoint.add(CarStartPosition);
    }

    public float FindAngle(Point B, Point C)
    {
        Point Vector2 = new Point(C.x - B.x, B.y - C.y);

        float modv2 = (float)Math.sqrt(Math.pow(Vector2.x,2) + Math.pow(Vector2.y,2));

        float degree2 = (float)Math.acos(Vector2.y/modv2) * (180/(float)Math.PI);

        if (modv2 == 0)
        {
            degree2 = 0;
        }

        if (Vector2.x < 0)
        {
            degree2 = -degree2;
        }

        return  degree2;
    }

    public void RotateMatrix (float degrees)
    {
        RotateMatrix = new Matrix();
        RotateMatrix.postRotate(degrees);
    }

    public Path getPath()
    {
        Path output = new Path();
        if (trajectoryPoint.size() > 0) {
            output.moveTo(trajectoryPoint.get(0).x, trajectoryPoint.get(0).y);
            for (int i = 1; i  < trajectoryPoint.size();i++)
            {
                output.lineTo(trajectoryPoint.get(i).x,trajectoryPoint.get(i).y);
            }
        }
        return output;
    }
}
