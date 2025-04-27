package fungorium;

public class NoBodyTecton extends Tecton {

    public NoBodyTecton(int precentToBreak, String tectonName) { super(precentToBreak, tectonName); }

    @Override
    public void addMycelium(Mycelium m) throws Exception {
        myceliumList.add(m);

        view.myceliumAdded( m);
    }

    @Override
    public void roundPassed() {
        boolean tectonBreaks = generatedNumWithinBound(breakPrecent);

        if (tectonBreaks) { breakTecton(); }
    }

    @Override
    public boolean canPlaceBody() { return false; }

    @Override
    public Tecton breakTecton() {
        Tecton newTecton = new NoBodyTecton(breakPrecent, name + "-2");
        removeConnectionAtBreak();
        manageNeighboursAtBreak(newTecton);

        view.tectonBreaks(newTecton);

        return newTecton;
    }

    @Override
    public void vanishMycelium() throws Exception { throw new Exception(view.notMyceliumVanisherTecton()); }

    @Override
    public String toString() {
        return tectonToString("no body tecton");
    }

}