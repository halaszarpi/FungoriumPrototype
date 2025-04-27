package fungorium;

public class SingleMyceliumTecton extends Tecton {

    public SingleMyceliumTecton(int precentToBreak, String tectonName) { super(precentToBreak, tectonName); }

    @Override
    public void addMycelium(Mycelium m) throws Exception{

        if (myceliumList.isEmpty()) { myceliumList.add(m); }
        else { throw new Exception(view.singleMyceliumTectonAlreadyHasMycelium()); }
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
    public Tecton breakTecton() {
        Tecton newTecton = new SingleMyceliumTecton(breakPrecent, name + "-2");
        removeConnectionAtBreak();
        manageNeighboursAtBreak(newTecton);

        view.tectonBreaks(newTecton);

        return newTecton;
    }

    @Override
    public void vanishMycelium() throws Exception { throw new Exception(view.notMyceliumVanisherTecton()); }

    @Override
    public String toString() {
        return tectonToString("single mycelium tecton");
    }
}