package fungorium;

public class MyceliumVanisherTecton extends Tecton {

    public MyceliumVanisherTecton(int precentToBreak, String tectonName, TectonMap map) { super(precentToBreak, tectonName, map); }

    @Override
    public void addMycelium(Mycelium m) throws Exception {
        myceliumList.add(m);

        view.myceliumAdded(m);
    }

    @Override
    public void roundPassed() {
        boolean tectonBreaks = generatedNumWithinBound(breakPrecent);
        vanishMycelium();
        if (tectonBreaks) { breakTecton(); }
    }

    @Override
    public boolean canPlaceBody() {
        return canPlaceBodyHelper();
    }

    @Override
    public void breakTecton() {
        Tecton newTecton = new MyceliumVanisherTecton(breakPrecent, name + "-2", map);
        name += "-1";
        removeConnectionAtBreak();
        manageNeighboursAtBreak(newTecton);

        view.tectonBreaks(newTecton);

        map.add(newTecton);
    }

    @Override
    public String toString() {
        return tectonToString("mycelium vanisher tecton");
    }

    @Override
    public void vanishMycelium(){
        for (Mycelium m : myceliumList) {
            m.decreaseRoundsToLive();
        }
    }

}