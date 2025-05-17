package fungorium;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

public class SporeView implements IObserver{
    Spore spore;
    private final int sporeWidth = 10;
    private final int sporeHeight = 10;

    public SporeView(Spore spore) {
        this.spore = spore;
    }

    @Override
    public void draw(Graphics g, Point coords) {
        g.setColor(Color.WHITE);
        g.fillOval(coords.x - sporeWidth / 2, coords.y - sporeHeight / 2, sporeWidth, sporeHeight);
    }
}
