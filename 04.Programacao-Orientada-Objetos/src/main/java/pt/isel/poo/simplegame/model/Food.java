package pt.isel.poo.simplegame.model;

public class Food extends Element {
    @Override
    public boolean actorCollision(GameModel m) {
        m.decreaseFood();
        return true;
    }
}
