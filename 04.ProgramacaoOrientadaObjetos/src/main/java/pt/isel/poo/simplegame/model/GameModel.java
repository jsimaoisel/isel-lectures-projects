package pt.isel.poo.simplegame.model;

public class GameModel {
    public enum Dir {LEFT, RIGHT, UP, DOWN};

    //private int[][] board;
    private Element[][] board;

    private Position actorPos;
    private int nrFood;
    private boolean actorAlive;

    private ModelListener listener;

    public GameModel(int lines, int columns) {
        // create board with all reference null
        board = new Element[columns][lines];
        // start with a live actor :)
        actorAlive = true;
    }

    public void setModelListener(ModelListener listener) {
        this.listener = listener;
    }

    // TODO: review code to allow a correct initialization
    public void initGame() {
        // three food elements
        nrFood = 3;

        // add food @ x=5, y=1
        board[5][1] = new Food();

        // add food @ x=4, y=8
        board[4][8] = new Food();

        // add food @ x=7, y=4
        board[7][4] = new Food();

        // add Actor @ x=2, y=4
        actorPos = new Position(2,4);
        board[2][4] = new Actor();

        for (int col = 0; col < getColumns(); ++col) {
            board[col][0] = new Obstacle();
            board[col][getLines()-1] = new Obstacle();
        }
        for (int lin = 0; lin < getLines(); ++lin) {
            board[0][lin] = new Obstacle();
            board[getColumns()-1][lin] = new Obstacle();
        }

        currentDir = Dir.RIGHT;
    }

    public int getColumns() {
        return board.length;
    }

    public int getLines() {
        return board[0].length;
    }

    public Element getElement(int col, int lin) {
        return board[col][lin];
    }

    private Dir currentDir;

    public void changeDir(Dir dir) { currentDir = dir; }

    /** moves the Actor in 1 of 4 directions */
    public void move() {
        int newX = actorPos.x, newY = actorPos.y;
        switch (currentDir) {
            case UP: newY = newY - 1; break;
            case DOWN: newY = newY + 1; break;
            case LEFT: newX = newX - 1; break;
            case RIGHT: newX = newX + 1; break;
        }
        Element e = board[newX][newY];
        if (e == null || e.actorCollision(this)) {
            setActorPosition(newX, newY);
        }
    }

    public void put(int y, int x, char e) {
        switch (e) {
            case 'X': board[x][y] = new Obstacle(); break;
            //...
        }
    }

    public boolean isFinished() {
        return nrFood == 0 || !actorAlive;
    }

    void decreaseFood() {
        --nrFood;
    }

    void killActor() {
        actorAlive = false;
    }

    private void setActorPosition(int x, int y) {
        Actor a = (Actor) board[actorPos.x][actorPos.y];
        board[actorPos.x][actorPos.y] = null;
        modelUpdate(actorPos.x,actorPos.y,null);
        actorPos.x = x;
        actorPos.y = y;
        board[actorPos.x][actorPos.y] = a;
        modelUpdate(actorPos.x,actorPos.y,a);
    }

    private void modelUpdate(int x, int y, Element e) {
        if (listener!=null)
            listener.update(x, y, e);
    }

}
