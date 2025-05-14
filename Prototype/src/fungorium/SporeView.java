package fungorium;

import java.awt.Graphics;
import java.util.ArrayList;

public class SporeView implements IObserver{
    Spore spore;

    public SporeView(Spore spore) {
        this.spore = spore;
    }

    @Override
    public void draw(Graphics g) {
        //kirajzolás
    }
}
