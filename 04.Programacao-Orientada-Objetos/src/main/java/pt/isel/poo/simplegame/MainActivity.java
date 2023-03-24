package pt.isel.poo.simplegame;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import pt.isel.poo.simplegame.model.GameModel;
import pt.isel.poo.simplegame.view.GameView;
import pt.isel.poo.tile.OnBeatListener;
import pt.isel.poo.tile.TilePanel;


public class MainActivity extends Activity {

    final int MOVE_PERIOD = 1000;

    // View
    GameView view;
    TilePanel panel;

    // Model
    GameModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        panel = findViewById(R.id.tilepanel);
        Button l = findViewById(R.id.left);
        Button r = findViewById(R.id.right);
        Button u = findViewById(R.id.up);
        Button d = findViewById(R.id.down);

        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveActor(GameModel.Dir.LEFT);
            }
        });
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveActor(GameModel.Dir.RIGHT);
            }
        });
        u.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveActor(GameModel.Dir.UP);
            }
        });
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveActor(GameModel.Dir.DOWN);
            }
        });

        view = new GameView(this, panel);
        model = new GameModel(panel.getWidthInTiles(), panel.getHeightInTiles());

        model.setModelListener(view);
        model.initGame();

        view.initView(model);

        // set time base
        setTimeBase();
    }

    private void setTimeBase() {
        panel.setHeartbeatListener(MOVE_PERIOD, new OnBeatListener() {
            @Override
            public void onBeat(long beat, long time) {
                Log.i("onBeat","beat number " + beat + " at " + time);
                model.move();
                if (model.isFinished()) {
                    Toast
                            .makeText(MainActivity.this, "THE END", Toast.LENGTH_LONG)
                            .show();
                    finish();
                }
            }
        });
    }

    private void moveActor(GameModel.Dir dir) {
        model.changeDir(dir);
    }

}
