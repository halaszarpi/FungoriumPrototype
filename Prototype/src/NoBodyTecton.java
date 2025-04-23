public class NoBodyTecton extends Tecton {

    private static final String type = "no body tecton";

    public NoBodyTecton(int percentToBreak, String tectonName, TectonView view) { super(percentToBreak, tectonName, type, view); }

    @Override
    public void addMycelium(Mycelium m) throws Exception { 
        myceliumList.add(m); 

        view.myceliumAdded(this, m);
    }

    @Override
    public void roundPassed() {
        boolean tectonBreaks = generatedNumWithinBound(breakPercent);

        if (tectonBreaks) { breakTecton(); }
    }

    @Override
    public boolean canPlaceBody() throws Exception { 
        throw new Exception(view.noBodyTectonCannotPlaceBody(this));
     }

    @Override
    public void breakTecton() {
        Tecton newTecton = new NoBodyTecton(breakPercent, name + "-2", view);
        removeConnectionAtBreak();
        manageNeighboursAtBreak(newTecton);

        view.tectonBreaks(this, newTecton);
    }

}