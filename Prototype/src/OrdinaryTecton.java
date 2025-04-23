public class OrdinaryTecton extends Tecton {

    private static final String type = "ordinary tecton";

    public OrdinaryTecton(int precentToBreak, String tectonName, TectonView view) { super(precentToBreak, tectonName, type, view); }

    @Override
    public void addMycelium(Mycelium m) throws Exception { 
        myceliumList.add(m); 

        view.myceliumAdded(this, m);
    }

    @Override
    public void roundPassed() {
        boolean tectonBreaks = generatedNumWithinBound(breakPrecent);

        if (tectonBreaks) { breakTecton(); }
    }

    @Override
    public boolean canPlaceBody() throws Exception{
        if (!canPlaceBodyHelper()) throw new Exception(view.alreadyHasFungusbody(this));
        return true;
    }

    @Override
    public void breakTecton() {
        Tecton newTecton = new OrdinaryTecton(breakPrecent, name + "-2", view);
        removeConnectionAtBreak();
        manageNeighboursAtBreak(newTecton);

        view.tectonBreaks(this, newTecton);
    }

}