package fungorium;

import java.awt.*;
import java.util.ArrayList;

public class GFungusBody implements IObservable {
    FungusBody fungusbody;
    int x;
    int y;
    ArrayList<IObserver> observers;

    public GFungusBody(FungusBody fb) {
        fungusbody = fb;
        x = -1;
        y = -1;
        attach(fungusbody.getView());
    }

    @Override
    public void attach(IObserver o) { observers.add(o);}

    @Override
    public void detach(IObserver o) { observers.remove(o); }

    @Override
    public void update(Graphics g) {
        for(IObserver o : observers) {
            o.draw(g);
        }
    }

    @Override
    public IObserver getObserver() { return observers.getFirst(); }

    public void setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public FungusBody getFungusbody() { return fungusbody; }
}
