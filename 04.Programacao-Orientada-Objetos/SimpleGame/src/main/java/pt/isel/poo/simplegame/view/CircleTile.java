package pt.isel.poo.simplegame.view;

import android.graphics.Canvas;
import android.graphics.Paint;

import pt.isel.poo.tile.Tile;

public class CircleTile implements Tile {
    Paint p = new Paint();

    public CircleTile(int color) {
        p.setColor(color);
    }

    @Override
    public void draw(Canvas canvas, int side) {
        canvas.drawCircle(side/2,side/2,side/2, p);
    }

    @Override
    public boolean setSelect(boolean selected) {
        return false;
    }
}
