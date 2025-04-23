//public class MyceliumVanisherTecton extends Tecton {
//
//    private static final String type = "mycelium vanisher tecton";
//    private int vanishPercent;
//
//    public MyceliumVanisherTecton(int percentToBreak, int percentToVanish, String tectonName, TectonView view) {
//        super(percentToBreak, tectonName, type, view);
//        vanishPercent = percentToVanish;
//    }
//
//    @Override
//    public void addMycelium(Mycelium m) throws Exception {
//        myceliumList.add(m);
//
//        view.myceliumAdded(this, m);
//    }
//
//    @Override
//    public void roundPassed() {
//        boolean myceliumVanishes = generatedNumWithinBound(vanishPercent);
//        boolean tectonBreaks = generatedNumWithinBound(breakPercent);
//
//        if (myceliumVanishes) {
//            int randomMyceliumIndex = gen.nextInt(myceliumList.size());
//            Mycelium randomMycelium = myceliumList.get(randomMyceliumIndex);
//            removeMycelium(randomMycelium);
//        }
//
//        if (tectonBreaks) { breakTecton(); }
//    }
//
//    @Override
//    public boolean canPlaceBody() throws Exception {
//        if (!canPlaceBodyHelper()) throw new Exception(view.alreadyHasFungusbody(this));
//        return true;
//    }
//
//    @Override
//    public void breakTecton() {
//        Tecton newTecton = new MyceliumVanisherTecton(breakPercent, vanishPercent, name + "-2", view);
//        try {
//            removeConnectionAtBreak();
//            manageNeighboursAtBreak(newTecton);
//        }
//        catch (Exception e) {
//            //Ha jól van implementálva nem lehet exception, a breakTecton lefutása mindig sikeres
//        }
//
//        view.tectonBreaks(this, newTecton);
//    }
//
//    @Override
//    public String toString(){ return super.toString() + "\nMycelium vanish chance: " + Integer.toString(vanishPercent) + "%"; }
//
//}