package fungorium;

import java.awt.Graphics;
import java.util.ArrayList;

public class GTecton implements IObservable{
    Tecton tecton;
    int x;
    int y;
    ArrayList<IObserver> observers;
    // Valami ilyesmi fog még kelleni (csak a tektonhoz): JPanel drawPanel;
    // ez majd az a panel lesz, ahol kirajzoljuk a tektonokat 

    public GTecton(Tecton t) {
        tecton = t;
        x = -1;
        y = -1;
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

    // Újítás, ez kelleni fog osztályhoz (ez még nem 100%, de 4 sor szóval rakjuk sztem bele, max nem használjuk)
    public void setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /* Ez is csak a tektonhoz kell majd, ez a törésnél szükséges, hogy hozzáadja a GTekton listához a az új tektont:
     * 
     * public void addGTecton(GTekton gt) {
     *     drawPanel.addGTecton(gt);
     * }
     * 
     */

    @Override
    public IObserver getObserver() { return observers.getFirst(); }

    public Tecton getTecton() { return tecton; }

}
