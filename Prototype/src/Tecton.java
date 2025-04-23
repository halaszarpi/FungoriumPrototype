import java.util.*;
import java.util.Map;

public abstract class Tecton implements IRoundFollower{
    private String tectonType;
    private List<Spore> sporeList;
    private Map<Tecton, Integer> neighbours;
    private List<Insect> insectList;
    protected String name;
    protected int breakPrecent;
    protected Random gen;
    protected List<Mycelium> myceliumList;
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

    public abstract void breakTecton();

    protected void manageNeighboursAtBreak(Tecton newTecton) {

        List<Tecton> neighbourList = new ArrayList<>(neighbours.keySet());

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

        String returnString = "Tecton name: ";

        returnString += name;

        returnString += "\nTecton type: ";

        returnString += tectonType;

        returnString += "\nSpores on tecton: ";

        if (!sporeList.isEmpty()) {
            for (Spore s : sporeList){
                returnString += s.name + ", ";
            }
        }
        else { 
            returnString += "-"; 
        }

        returnString += "\nNeigbours of tecton: ";

        List<Tecton> neighbourList = new ArrayList<>(neighbours.keySet());

        if (!neighbourList.isEmpty()) {
            for (Tecton t : neighbourList){
                returnString += t.name + ", ";
            }
        }
        else { 
            returnString += "-"; 
        }

        returnString+= "\nMycelium on tecton: ";

        if (!myceliumList.isEmpty()) {
            for (Mycelium m : myceliumList){
                returnString += m.name + ", ";
            }
        }
        else { 
            returnString += "-"; 
        }

        returnString += "\nInsect on tecton: ";

        if (!insectList.isEmpty()){
            for (Insect i : insectList){
                returnString += i.name + ", ";
            }
        }
        else { 
            returnString += "-"; 
        }

        returnString += "\nBreak chance: ";
        returnString += Integer.toString(breakPrecent) + "%";

        return returnString;
    }

}