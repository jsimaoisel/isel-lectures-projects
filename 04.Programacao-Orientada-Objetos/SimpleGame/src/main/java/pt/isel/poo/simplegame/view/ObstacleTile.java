package pt.isel.poo.simplegame.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import pt.isel.poo.simplegame.R;
import pt.isel.poo.tile.Img;
import pt.isel.poo.tile.Tile;

public class ObstacleTile implements Tile {

    Paint p;
    Img obstacleImg;

    public ObstacleTile(Context ctx) {
        p = new Paint();
        obstacleImg = new Img(ctx, R.drawable.wall);
    }

    @Override
    public void draw(Canvas canvas, int side) {
        obstacleImg.draw(canvas, side, side, p);
    }

    @Override
    public boolean setSelect(boolean selected) {
        return false;
    }
}
