package fungorium;

import java.awt.*;
import java.util.ArrayList;

public class GPlayer implements IObservable {
    Player player;
    int x;
    int y;
    ArrayList<IObserver> observers;

    public GPlayer(Player player) {
        this.player = player;
        attach(player.getView());
    }

    @Override
    public void attach(IObserver o) { observers.add(o); }

    @Override
    public void detach(IObserver o) { observers.remove(o); }

    @Override
    public void update(Graphics g) {
        for(IObserver o : observers) {
            o.draw(g, new Point(x, y));
        }
    }

    @Override
    public IObserver getObserver() { return observers.getFirst(); }

    public void setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Player getPlayer() { return player; }
}
