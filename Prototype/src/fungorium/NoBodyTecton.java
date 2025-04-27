package fungorium;

public class NoBodyTecton extends Tecton {

    private static final String type = "no body tecton";

    public NoBodyTecton(int precentToBreak, String tectonName) { super(precentToBreak, tectonName, type); }

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
    public boolean canPlaceBody() throws Exception { 
        throw new Exception(view.noBodyTectonCannotPlaceBody());
     }

    @Override
    public Tecton breakTecton() {
        Tecton newTecton = new NoBodyTecton(breakPrecent, name + "-2");
        removeConnectionAtBreak();
        manageNeighboursAtBreak(newTecton);

        view.tectonBreaks(newTecton);
        callRoundPasseds();

        return newTecton;
    }

    @Override
    public void vanishMycelium() throws Exception { throw new Exception(view.notMyceliumVanisherTecton()); }

}