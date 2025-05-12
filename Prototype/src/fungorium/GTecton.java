package fungorium;

import java.awt.Graphics;
import java.util.ArrayList;

public class GTecton implements IObservable {
    Tecton tecton;
    int x;
    int y;
    ArrayList<IObserver> observers;

    public GTecton(Tecton t) {
        tecton = t;
        x = -1;
        y = -1;
        // Újítás, minden osztályhoz kell egy getView(), ami visszaadja az osztály view-ját.
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
            o.draw(g);
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

}
