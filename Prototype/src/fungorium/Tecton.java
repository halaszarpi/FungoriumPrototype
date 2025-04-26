package fungorium;

import java.util.*;

public abstract class Tecton implements IRoundFollower{
    private String tectonType;
    private Map<Tecton, Integer> neighbours;
    private List<Spore> sporeList;
    private List<Insect> insectList;
    protected List<Mycelium> myceliumList;
    protected String name;
    protected int breakPrecent;
    protected Random gen;
    protected TectonView view;
    private static final int noMyceliumConnectionVal = 0;
    private static final int maxMyceliumSurvivalTimeVal = 5;

    protected Tecton(int precentToBreak, String tectonName, String type, TectonView tectonView) {
        tectonType = type;
        sporeList = new ArrayList<>();
        neighbours = new HashMap<>();
        insectList = new ArrayList<>();
        name = tectonName;
        breakPrecent = precentToBreak;
        gen = new Random();
        myceliumList = new ArrayList<>();
        view = tectonView;

        view.tectonCreated(this);
    }

    public void addNeighbour(Tecton t) { 
        neighbours.put(t, noMyceliumConnectionVal); 
        t.neighbours.put(this, noMyceliumConnectionVal);

        view.neighbourAdded(this, t);
    }

    public boolean addConnection(Tecton t) throws Exception{

        if (!isNeighbour(t)) return false; //Felesleges if()-be rakni, ha false úgyis exceptiont dob, de a szépség miatt beleírom

        neighbours.put(t, maxMyceliumSurvivalTimeVal);
        t.neighbours.put(this, maxMyceliumSurvivalTimeVal);

        view.connectionAdded(this, t);
        return true;
    }

    public abstract void addMycelium(Mycelium m) throws Exception;

    public void addSpore(Spore s) { 
        sporeList.add(s);

        view.sporeAdded(this, s);
     }

    public void addInsect(Insect i) {
        insectList.add(i); 

        view.insectAdded(this, i);
    }

    private void removeNeighbour(Tecton t) {

        neighbours.remove(t); 
        t.neighbours.remove(this);

        view.neighbourRemoved(this, t);
    }

    public void removeMycelium(Mycelium m) { 
        myceliumList.remove(m); 

        view.myceliumRemoved(this, m);
    }

    public void removeInsect(Insect i) { 
        insectList.remove(i); 

        view.insectRemoved(this, i);
    }

    public void removeSpore(Spore s) { 
        sporeList.remove(s); 

        view.sporeRemoved(this, s);
    }

    public void removeConnection(Tecton t) throws Exception { 

        isNeighbour(t);

        neighbours.put(t, noMyceliumConnectionVal);
        t.neighbours.put(this, noMyceliumConnectionVal);

        view.removeConnection(this, t);
    }

    //Kód duplikálás miatt
    private boolean isNeighbourHelper(Tecton t) {
        List<Tecton> neighbourList = new ArrayList<>(neighbours.keySet());
        return neighbourList.contains(t);
    }

    public boolean isNeighbour(Tecton t) throws Exception {
        if (!isNeighbourHelper(t)) throw new Exception(view.notNeighbour(this, t));
        return true;
    }

    public boolean isNeighbourOrNeighboursNeighbour(Tecton t) throws Exception {
        boolean isNeighbourBoolean = isNeighbourHelper(t);
        boolean isNeighboursNeighbourBoolean = false;

        List<Tecton> neighbourList = new ArrayList<>(neighbours.keySet());

        for (Tecton tecton : neighbourList) {
            List<Tecton> neighboursNeigbourList = new ArrayList<>(tecton.neighbours.keySet());
            if (neighboursNeigbourList.contains(t)) { isNeighboursNeighbourBoolean = true; }
        }

        if (!isNeighbourBoolean && !isNeighboursNeighbourBoolean) throw new Exception(view.notNeighbourOrNeigboursNeighbour(this, t));
        return true;
    }

    public boolean isConnectedTo(Tecton t) throws Exception {
        if (!isNeighbour(t)) return false; //Felesleges if()-be rakni, ha false úgyis exceptiont dob, de a szépség miatt beleírom

        int myceliumSurvivalTimeVal = neighbours.get(t);
        if (myceliumSurvivalTimeVal == noMyceliumConnectionVal) { throw new Exception(view.notConnectedByMycelium(this, t)); }
        return true;
    }

    public abstract boolean canPlaceBody() throws Exception;

    //Kód duplikálás miatt
    protected boolean canPlaceBodyHelper() {

        for (Mycelium m : myceliumList){
            if (m.hasBody()) return false;
        }

        return true;
    }

    public boolean hasSpores() throws Exception { 
        if (sporeList.isEmpty()) { throw new Exception(view.tectonHasNoSpores(this)); }
        return true;
    }

    public abstract Tecton breakTecton();

    public abstract void vanishMycelium() throws Exception;

    protected void callRoundPasseds() {
        for (Insect i : insectList) {
            i.roundPassed();
        }

        for (Mycelium m : myceliumList) {
            m.roundPassed();
        }
    }

    protected void manageNeighboursAtBreak(Tecton newTecton) {

        List<Tecton> neighbourList = new ArrayList<>(neighbours.keySet());

        if (neighbourList.isEmpty()) return;

        int randomNeighbourIndex = gen.nextInt(neighbourList.size());
        Tecton randomNeighbour = neighbourList.get(randomNeighbourIndex);

        List<Tecton> randomTectonNeighbourList = new ArrayList<>(randomNeighbour.neighbours.keySet());
        List<Tecton> commonNeighbours = new ArrayList<>();

        removeNeighbour(randomNeighbour);

        for (Tecton t1 : neighbourList) {
            for (Tecton t2 : randomTectonNeighbourList){
                if (t1 == t2) commonNeighbours.add(t1);
            }
        }

        if (!commonNeighbours.isEmpty()) {
            for (Tecton t : commonNeighbours) {
                t.addNeighbour(newTecton);
            }
        }

        addNeighbour(newTecton);
        randomNeighbour.addNeighbour(newTecton);

    }

    protected void removeConnectionAtBreak() {

        try {
            for (Tecton t : neighbours.keySet()) {
                removeConnection(t);
            }
        }
        catch(Exception e) {
            //A breakTecton lefutása mindig sikeres, tehát nem kell csinálni semmit
        }
        
    }

    protected boolean generatedNumWithinBound(int chance) {
        int num = gen.nextInt(100);
        return num < chance;
    }

    public boolean findBody(FungusFarmer f, List<Tecton> observedTectons) { System.out.println("Not implemented yet!"); return false; }

    public String getName() { return name; }

    @Override
    public String toString() { 

        String returnString = "\n--------------------------------------------------------------------------------------------------------";
        
        returnString += "\nTecton name: ";

        returnString += name;

        returnString += "\nTecton type: ";

        returnString += tectonType;

        returnString += "\n---------------------------";
        returnString += "\nSpores on tecton: ";

        if (!sporeList.isEmpty()) {
            for (Spore s : sporeList){
                returnString += "\n" + s.toString() + "\n";
            }
        }
        else { 
            returnString += "-\n"; 
        }
        returnString += "---------------------------";
        returnString += "\nNeigbours of tecton: ";

        List<Tecton> neighbourList = new ArrayList<>(neighbours.keySet());

        if (!neighbourList.isEmpty()) {
            for (Tecton t : neighbourList){
                returnString += t.name + ", ";
            }
        }
        else { 
            returnString += "-\n"; 
        }

        returnString += "\n---------------------------";
        returnString += "\nConnected by mycelium: ";

        boolean foundAtLeastOne = false;

        if (!neighbourList.isEmpty()) {
            for (Tecton t : neighbourList) {
                if (neighbours.get(t) != 0) { 
                    returnString += t.name + ", "; 
                    foundAtLeastOne = true;
                }
            }
            if (!foundAtLeastOne) { returnString += "-\n"; }
        }
        else { 
            returnString += "-\n"; 
        }

        returnString += "\n---------------------------";
        returnString+= "\nMycelia on tecton: ";

        if (!myceliumList.isEmpty()) {
            for (Mycelium m : myceliumList){
                returnString += "\n" + m.toString() + "\n";
            }
        }
        else { 
            returnString += "-\n"; 
        }

        returnString += "---------------------------";
        returnString += "\nInsects on tecton: ";

        if (!insectList.isEmpty()){
            for (Insect i : insectList) {
                returnString += "\n" + i.toString() + "\n";
            }
        }
        else { 
            returnString += "-\n"; 
        }

        returnString += "---------------------------";
        returnString += "\nBreak chance: ";
        returnString += Integer.toString(breakPrecent) + "%";

        if (!tectonType.equals("mycelium vanisher tecton")) returnString += "\n--------------------------------------------------------------------------------------------------------\n";

        return returnString;
    }

    @Override
    public boolean equals(Object obj) {
        Tecton t = (Tecton)obj;
        return name.equals(t.getName());
    }

    public static <M, V> boolean areNeighbourMapsEqual(Map<M, V> map1, Map<M, V> map2) {
        List<M> map1KeySet = new ArrayList<>(map1.keySet());
        List<M> map2KeySet = new ArrayList<>(map2.keySet());

        if (map1KeySet.size() != map2KeySet.size()) return false;

        for (M map1Key : map1KeySet) {

            boolean foundVal = false;
            for (M map2Key : map2KeySet) {
                if (map2Key.equals(map1Key)) { 

                    foundVal = true; 
                    if (map1.get(map1Key) != map2.get(map2Key)) { return false; }
                }
            }
            if (!foundVal) { return false; }
        }

        return true;
    }

    public boolean isEqual(Tecton t) {
        return tectonType.equals(t.tectonType) && areNeighbourMapsEqual(neighbours, t.neighbours) && sporeList.equals(t.sporeList) &&
        insectList.equals(t.insectList) && myceliumList.equals(t.myceliumList) && name.equals(t.name) &&
        breakPrecent == t.breakPrecent;
    }

    public Map<Tecton, Integer> getNeighbours() { return neighbours; }

    public List<Spore> getSporeList() { return sporeList; }

    public List<Insect> getInsectList() { return insectList; }

    public List<Mycelium> getMyceliumList() { return myceliumList; }
}