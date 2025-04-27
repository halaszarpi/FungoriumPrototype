package fungorium;

public class OrdinaryTecton extends Tecton {

    private static final String type = "ordinary tecton";

    public OrdinaryTecton(int precentToBreak, String tectonName) { super(precentToBreak, tectonName, type); }

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
    public boolean canPlaceBody() throws Exception{
        if (!canPlaceBodyHelper()) throw new Exception(view.alreadyHasFungusbody());
        return true;
    }

    @Override
    public Tecton breakTecton() {
        Tecton newTecton = new OrdinaryTecton(breakPrecent, name + "-2");
        removeConnectionAtBreak();
        manageNeighboursAtBreak(newTecton);

        view.tectonBreaks(newTecton);
        callRoundPasseds();

        return newTecton;
    }

    @Override
    public void vanishMycelium() throws Exception { throw new Exception(view.notMyceliumVanisherTecton()); }

}