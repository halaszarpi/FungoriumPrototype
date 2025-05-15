package fungorium;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

public class GInsect implements IObservable {
    private Insect insect;
    private int x;
    private int y;
    private ArrayList<IObserver> observers;

    public GInsect(Insect i) {
        this.insect = i;

        // Ezek egyelore csak kezdoertekek, majd valtozik
        this.x = 0;
        this.y = 0;
        
        this.attach(insect.getView());
    }

    public void attach(IObserver o) {
        observers.add(o);
    }

    public void detach(IObserver o) {
        observers.remove(o);
    }

    public IObserver getObserver() { return this.observers.getFirst(); }

    public Insect getInsect() { return this.insect; }

    @Override
    public void update(Graphics g) {
        for (IObserver o : observers) {
            o.draw(g, new Point(x, y));
        }
    }
}
