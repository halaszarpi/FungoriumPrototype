package fungorium;

public class InsectKeeper {
    public int getActionPoints() { return 10; }
    public void useActionPoints(int actionPoints) {}
    public void insectDied(Insect i) {}
    public void duplicateInsect(Insect i) {}
    public String getName() { return "apad"; }
    @Override
    public boolean equals(Object obj) { return true; }
}
