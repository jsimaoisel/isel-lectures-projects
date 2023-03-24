package pt.isel.poo.simplegame.view;

import android.content.Context;
import android.graphics.Color;

import java.util.HashMap;

import pt.isel.poo.simplegame.model.Actor;
import pt.isel.poo.simplegame.model.Element;
import pt.isel.poo.simplegame.model.Food;
import pt.isel.poo.simplegame.model.ModelListener;
import pt.isel.poo.simplegame.model.GameModel;
import pt.isel.poo.simplegame.model.Obstacle;
import pt.isel.poo.tile.Tile;
import pt.isel.poo.tile.TilePanel;

public final class GameView implements ModelListener {

    private final TilePanel panel;
    /*CircleTile actorTile; // = new CircleTile(Color.RED);
    CircleTile foodTile; // = new CircleTile(Color.BLUE);
    ObstacleTile obstacleTile;*/

    /*
    Actor.class -> CircleTile
    Food.class -> CircleTile
    Obstacle.class -> ObstacleTile
     */
    HashMap<Class, Tile> modelToView;

    public GameView(Context ctx, TilePanel panel) {
        this.panel = panel;
        modelToView = new HashMap<>();
        modelToView.put(Actor.class, new CircleTile(Color.RED));
        modelToView.put(Food.class, new CircleTile(Color.BLUE));
        modelToView.put(Obstacle.class, new ObstacleTile(ctx));

    }
    @Override
    public void update(int x, int y, Element e) {
        setElementView(x, y, e);
    }

    public void initView(GameModel model) {
        for (int col=0; col < model.getColumns(); ++col) {
            for (int lin = 0; lin < model.getLines(); ++lin) {
                setElementView(col, lin, model.getElement(col, lin));
            }
        }
    }

    private void setElementView(int x, int y, Element e) {
        if (e == null) {
            panel.setTile(x,y,null);
        } else {
            Tile tile = modelToView.get(e.getClass());
            panel.setTile(x, y, tile);
            /*if (e instanceof Actor) {
                panel.setTile(x,y, actorTile);
            } else if (e instanceof Food) {
                panel.setTile(x,y, foodTile);
            } else if (e instanceof Obstacle){
                panel.setTile(x,y, obstacleTile);
            }*/

        }
    }
}
