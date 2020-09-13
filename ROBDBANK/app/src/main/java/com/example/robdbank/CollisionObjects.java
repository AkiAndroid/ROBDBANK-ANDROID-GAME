package com.example.ROBDBANK;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CollisionObjects
{
    List<Point> Coins;
    List<Point> Obstacles;
    int Thickness = 80;
    int Score=0;
    GAMEinfo gamEinfo;


    int totalobj = 15;

    public CollisionObjects(int width,int height, Point Start, Point End,GAMEinfo gamEinfo)
    {
        this.gamEinfo = gamEinfo;
        Coins=new ArrayList<>();
        Obstacles=new ArrayList<>();


        for (int i = 0; i < totalobj;)
        {
            Random A = new Random();
            int posx = A.nextInt(width - (2 * Thickness)) + Thickness;
            int posy = A.nextInt(height - (2 * Thickness)) + Thickness;

            int coinfactor = A.nextInt(2);

            boolean alreadypresent = false;

            for (int j = 0; j < Obstacles.size(); j++)
            {
                float distance = (float) Math.sqrt(Math.pow(posx - Obstacles.get(j).x,2) + Math.pow(posy - Obstacles.get(j).y,2));
                if (distance < 2 * Thickness)
                {
                    alreadypresent = true;
                    break;
                }
            }

            for (int j = 0; j < Coins.size(); j++)
            {
                float distance = (float) Math.sqrt(Math.pow(posx - Coins.get(j).x,2) + Math.pow(posy - Coins.get(j).y,2));
                if (distance < 2 * Thickness)
                {
                    alreadypresent = true;
                    break;
                }
            }

            float distance = (float) Math.sqrt(Math.pow(posx - Start.x,2) + Math.pow(posy - Start.y,2));

            if (distance < Thickness * 3)
            {
                alreadypresent = true;
            }

            distance = (float) Math.sqrt(Math.pow(posx - End.x,2) + Math.pow(posy - End.y,2));

            if (distance < Thickness * 4)
            {
                alreadypresent = true;
            }



            if (!alreadypresent)
            {
                if (coinfactor == 1)
                {
                    Coins.add(new Point(posx,posy));
                }
                else
                {
                    Obstacles.add(new Point(posx,posy));
                }
                i++;
            }
        }

    }

    public boolean isHit(Point CurrentPoint)
    {
        for (int i = 0; i < Obstacles.size();i++)
        {
            float distance = (float) Math.sqrt(Math.pow(CurrentPoint.x - Obstacles.get(i).x,2) + Math.pow(CurrentPoint.y - Obstacles.get(i).y,2));

            if (distance < Thickness*0.75f)
                return true;
        }

        for (int i = 0; i < Coins.size();i++)
        {
            float distance = (float) Math.sqrt(Math.pow(CurrentPoint.x - Coins.get(i).x,2) + Math.pow(CurrentPoint.y - Coins.get(i).y,2));

            if (distance < Thickness)
            {
                Score += 100;
                Coins.remove(Coins.get(i));
                MainActivity.cash_sound.start();
                gamEinfo.UpdateScore(Score);
                break;
            }
        }

        return false;

    }
}
