package fungorium;

public class MyceliumVanisherTecton extends Tecton {

    private static final String type = "mycelium vanisher tecton";
    private int vanishPrecent;

    public MyceliumVanisherTecton(int precentToBreak, int precentToVanish, String tectonName, TectonView view) { 
        super(precentToBreak, tectonName, type, view); 
        vanishPrecent = precentToVanish;
    }

    @Override
    public void addMycelium(Mycelium m) throws Exception { 
        myceliumList.add(m); 

        view.myceliumAdded(this, m);
    }

    @Override
    public void roundPassed() {
        boolean myceliumVanishes = generatedNumWithinBound(vanishPrecent);
        boolean tectonBreaks = generatedNumWithinBound(breakPrecent);

        if (myceliumVanishes) {

            try { vanishMycelium(); }
            catch(Exception e) { e.printStackTrace(); }
            
        }

        if (tectonBreaks) { breakTecton(); }
    }

    @Override
    public boolean canPlaceBody() throws Exception {
        if (!canPlaceBodyHelper()) throw new Exception(view.alreadyHasFungusbody(this));
        return true;
    }

    @Override
    public void breakTecton() {
        Tecton newTecton = new MyceliumVanisherTecton(breakPrecent, vanishPrecent, name + "-2", view);
        removeConnectionAtBreak();
         manageNeighboursAtBreak(newTecton);

        view.tectonBreaks(this, newTecton);
        callRoundPasseds();
    }

    @Override 
    public String toString(){ return super.toString() + "\nMycelium vanish chance: " + Integer.toString(vanishPrecent) + "%"; }

    @Override
    public void vanishMycelium() throws Exception { 
        int randomMyceliumIndex = gen.nextInt(myceliumList.size());
        Mycelium randomMycelium = myceliumList.get(randomMyceliumIndex);
        removeMycelium(randomMycelium);
    }

}