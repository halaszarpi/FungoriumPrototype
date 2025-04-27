package fungorium;

public class OrdinaryTecton extends Tecton {

    public OrdinaryTecton(int precentToBreak, String tectonName, TectonMap map){ super(precentToBreak, tectonName, map); }

    @Override
    public void addMycelium(Mycelium m) throws Exception {
        myceliumList.add(m);

        view.myceliumAdded(m);
    }

    @Override
    public void roundPassed() {
        boolean tectonBreaks = generatedNumWithinBound(breakPrecent);

        if (tectonBreaks) { breakTecton(); }
    }

    @Override
    public boolean canPlaceBody() {
        return canPlaceBodyHelper();
    }

    @Override
    public void breakTecton() {
        Tecton newTecton = new OrdinaryTecton(breakPrecent, name + "-2", map);
        name += "-1";
        removeConnectionAtBreak();
        manageNeighboursAtBreak(newTecton);

        view.tectonBreaks(newTecton);

        map.add(newTecton);
    }

    @Override
    public void vanishMycelium() throws Exception { throw new Exception(view.notMyceliumVanisherTecton()); }

    @Override
    public String toString() {
        return tectonToString("ordinary tecton");
    }

}