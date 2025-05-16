package fungorium;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

public class GTecton implements IObservable {
    Tecton tecton;
    GMap map;
    int x;
    int y;
    ArrayList<IObserver> observers;

    public GTecton(Tecton t, GMap m) {
        tecton = t;
        map = m; 
        x = -1;
        y = -1;
        // Újítás, minden osztályhoz kell egy getView(), ami visszaadja az osztály view-ját.
        observers = new ArrayList<>();
        attach(tecton.getView());
    }

    @Override
    public void attach(IObserver o) {
        observers.add(o);
    }

    @Override
    public void detach(IObserver o) {
        observers.remove(o);
    }

    // Újítás, kell Graphics g paraméterben
    @Override
    public void update(Graphics g) {
        for (IObserver o : observers) {
            if (o instanceof TectonView tectonView) { tectonView.drawLine(g, this, map.getConnectedGTectonts(this));}
            o.draw(g, new Point(x, y));
        }
    }

    // Újítás, ez kelleni fog minden osztályhoz (ez még nem 100%, lehet máshogy lesz megoldva, de 4 sor szóval rakjuk sztem bele, max nem használjuk)
    public void setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public IObserver getObserver() { return observers.getFirst(); }

    public Tecton getTecton() { return tecton; }

    public Point getCoords() { return new Point(x, y); }

}
