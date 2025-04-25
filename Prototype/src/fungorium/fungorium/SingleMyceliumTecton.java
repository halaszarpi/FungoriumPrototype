package fungorium;

public class SingleMyceliumTecton extends Tecton {

    private static final String type = "single mycelium tecton";

    public SingleMyceliumTecton(int precentToBreak, String tectonName, TectonView view) { super(precentToBreak, tectonName, type, view); }

    @Override
    public void addMycelium(Mycelium m) throws Exception{ 

        if (myceliumList.isEmpty()) { myceliumList.add(m); }
        else { throw new Exception(view.singleMyceliumTectonAlreadyHasMycelium(this)); }
    }

    @Override
    public void roundPassed() {
        boolean tectonBreaks = generatedNumWithinBound(breakPrecent);

        if (tectonBreaks) { breakTecton(); }
    }

    @Override
    public boolean canPlaceBody() throws Exception {
        if (!canPlaceBodyHelper()) throw new Exception(view.alreadyHasFungusbody(this));
        return true;
    }

    @Override
    public Tecton breakTecton() {
        Tecton newTecton = new SingleMyceliumTecton(breakPrecent, name + "-2", view);
        removeConnectionAtBreak();
        manageNeighboursAtBreak(newTecton);

        view.tectonBreaks(this, newTecton);
        return newTecton;

    }

    @Override
    public void vanishMycelium() throws Exception { throw new Exception(view.notMyceliumVanisherTecton(this)); }
}