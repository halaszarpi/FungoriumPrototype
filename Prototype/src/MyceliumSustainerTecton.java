public class MyceliumSustainerTecton extends Tecton{

	private static final String type = "mycelium sustainer tecton";

    public MyceliumSustainerTecton(int percentToBreak, String tectonName, TectonView view) { super(percentToBreak, tectonName, type, view); }

    @Override
    public void addMycelium(Mycelium m) throws Exception { 
        myceliumList.add(m); 

        view.myceliumAdded(this, m);
    }

    @Override
    public void roundPassed() {
        boolean tectonBreaks = generatedNumWithinBound(breakPercent);

        if (tectonBreaks) { breakTecton(); }
        for (Mycelium m : myceliumList){
            m.increaseRoundsToLive();
        }
    }

    @Override
    public boolean canPlaceBody() throws Exception{
        if (!canPlaceBodyHelper()) throw new Exception(view.alreadyHasFungusbody(this));
        return true;
    }

    @Override
    public void breakTecton() {
        Tecton newTecton = new OrdinaryTecton(breakPercent, name + "-2", view);
        removeConnectionAtBreak();
        manageNeighboursAtBreak(newTecton);

        view.tectonBreaks(this, newTecton);
    }
    
}
