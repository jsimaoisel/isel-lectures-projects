package pt.isel.poo.simplegame.model;

public class Obstacle extends Element {
    @Override
    public boolean actorCollision(GameModel m) {
        m.killActor();
        return false;
    }
}
