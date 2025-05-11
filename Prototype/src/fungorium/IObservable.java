package fungorium;

import java.awt.*;

public interface IObservable {
    public void attach(IObserver o);
    public void detach(IObserver o);
    public void update(Graphics g);
    public IObserver getObserver();
}
