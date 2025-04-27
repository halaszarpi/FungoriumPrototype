package fungorium;

public class MyceliumSustainerTecton extends Tecton{

    public MyceliumSustainerTecton(int precentToBreak, String tectonName) {
        super(precentToBreak, tectonName);
    }

    @Override
    public void addMycelium(Mycelium m) throws Exception {
        myceliumList.add(m);

        view.myceliumAdded(m);
    }

    @Override
    public void roundPassed() {
        boolean tectonBreaks = generatedNumWithinBound(breakPrecent);

        if (tectonBreaks) { breakTecton(); }
        for (Mycelium m : myceliumList){
            m.increaseRoundsToLive();
        }
    }

    @Override
    public boolean canPlaceBody() {
        return canPlaceBodyHelper();
    }

    @Override
    public Tecton breakTecton() {
        Tecton newTecton = new OrdinaryTecton(breakPrecent, name + "-2");
        removeConnectionAtBreak();
        manageNeighboursAtBreak(newTecton);

        view.tectonBreaks(newTecton);

        return newTecton;
    }

    @Override
    public void vanishMycelium() throws Exception { throw new Exception(view.notMyceliumVanisherTecton()); }

    @Override
    public String toString() {
        return tectonToString("mycelium sustainer tecton");
    }

}