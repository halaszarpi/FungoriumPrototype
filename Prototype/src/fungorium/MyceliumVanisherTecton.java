package fungorium;

public class MyceliumVanisherTecton extends Tecton {

    private static final String type = "mycelium vanisher tecton";
    private final int vanishPrecent;

    public MyceliumVanisherTecton(int precentToBreak, int precentToVanish, String tectonName) {
        super(precentToBreak, tectonName, type);
        vanishPrecent = precentToVanish;
    }

    @Override
    public void addMycelium(Mycelium m) throws Exception { 
        myceliumList.add(m); 

        view.myceliumAdded(m);
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
        if (!canPlaceBodyHelper()) throw new Exception(view.alreadyHasFungusbody());
        return true;
    }

    @Override
    public Tecton breakTecton() {
        Tecton newTecton = new MyceliumVanisherTecton(breakPrecent, vanishPrecent, name + "-2");
        removeConnectionAtBreak();
         manageNeighboursAtBreak(newTecton);

        view.tectonBreaks(newTecton);
        callRoundPasseds();

        return newTecton;
    }

    @Override 
    public String toString() { return super.toString() + "\n---------------------------" + 
    "\nMycelium vanish chance: " + Integer.toString(vanishPrecent) + "%" + 
    "\n--------------------------------------------------------------------------------------------------------\n"; }

    @Override
    public void vanishMycelium() throws Exception { 
        int randomMyceliumIndex = gen.nextInt(myceliumList.size());
        Mycelium randomMycelium = myceliumList.get(randomMyceliumIndex);
        removeMycelium(randomMycelium);
    }

}