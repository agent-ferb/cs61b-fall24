package core;

public class Avatar {
    public int x;
    public int y;

    public Avatar(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }

    // Setter methods
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

