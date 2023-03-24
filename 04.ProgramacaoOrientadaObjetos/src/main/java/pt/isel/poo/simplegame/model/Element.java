package pt.isel.poo.simplegame.model;

/**
 *
 */
public abstract class Element {
    /**
     * Determines what happens to the model when the Actor collides with it and if the Actor can
     * go to the cell this element is in
     * @param m the model
     * @return true if the actor can move to the place of this element
     */
    abstract public boolean actorCollision(GameModel m);
}
