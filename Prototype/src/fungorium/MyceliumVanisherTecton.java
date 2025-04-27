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
        boolean myceliumVanishes = generatedNumWithinBound(vanishPrecent);
        boolean tectonBreaks = generatedNumWithinBound(breakPrecent);

        if (myceliumVanishes) {

            try { vanishMycelium(); }
            catch(Exception e) { e.printStackTrace(); }

        }

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
    public void vanishMycelium() throws Exception {
        int randomMyceliumIndex = gen.nextInt(myceliumList.size());
        Mycelium randomMycelium = myceliumList.get(randomMyceliumIndex);
        removeMycelium(randomMycelium);
    }

}