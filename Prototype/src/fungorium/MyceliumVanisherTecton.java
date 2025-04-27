package fungorium;

public class MyceliumVanisherTecton extends Tecton {
    private int vanishPrecent;

    public MyceliumVanisherTecton(int precentToBreak, int precentToVanish, String tectonName) {
        super(precentToBreak, tectonName);
        vanishPrecent = precentToVanish;
    }

    @Override
    public void addMycelium(Mycelium m) throws Exception {
        myceliumList.add(m);

        view.myceliumAdded(m);
    }

    @Override
    public void roundPassed() {
        boolean tectonBreaks = generatedNumWithinBound(breakPrecent);
        vanishMycelium();
        if (tectonBreaks) { breakTecton(); }
    }

    @Override
    public boolean canPlaceBody() {
        return canPlaceBodyHelper();
    }

    @Override
    public Tecton breakTecton() {
        Tecton newTecton = new MyceliumVanisherTecton(breakPrecent, vanishPrecent, name + "-2");
        removeConnectionAtBreak();
        manageNeighboursAtBreak(newTecton);

        view.tectonBreaks(newTecton);

        return newTecton;
    }

    @Override
    public String toString() { return tectonToString("mycelium vanisher tectontype") + "\n---------------------------" +
            "\nMycelium vanish chance: " + Integer.toString(vanishPrecent) + "%" +
            "\n--------------------------------------------------------------------------------------------------------\n"; }

    @Override
    public void vanishMycelium(){
        for (Mycelium m : myceliumList) {
            m.decreaseRoundsToLive();
        }
    }

}