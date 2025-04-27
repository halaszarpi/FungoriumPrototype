package fungorium;

import java.util.*;

/**
 * Abstract class representing a Tecton in the Fungorium world.
 * A Tecton can have neighbours, spores, insects, and mycelia.
 * It also manages connections and breaking behavior.
 */
public abstract class Tecton implements IRoundFollower{
    TectonMap map;
    private Map<Tecton, Boolean> neighbours;
    private List<Spore> sporeList;
    private List<Insect> insectList;
    protected List<Mycelium> myceliumList;
    protected String name;
    protected int breakPrecent;
    protected Random gen;
    protected TectonView view;

    /**
     * Constructs a Tecton with a break chance, name, and associated map.
     *
     * @param percentToBreak The percentage chance for the tecton to break.
     * @param tectonName The name of the tecton.
     * @param m The map the tecton belongs to.
     */
    protected Tecton(int percentToBreak, String tectonName, TectonMap m) {
        sporeList = new ArrayList<>();
        neighbours = new HashMap<>();
        insectList = new ArrayList<>();
        name = tectonName;
        breakPrecent = percentToBreak;
        gen = new Random();
        myceliumList = new ArrayList<>();
        view = new TectonView(this);
        map = m;

        view.tectonCreated();
    }

    /**
     * Adds a tecton as a neighbour.
     *
     * @param t The tecton to add as a neighbour.
     */
    public void addNeighbour(Tecton t) { 
        neighbours.put(t, false);
        t.neighbours.put(this, false);

        view.neighbourAdded(t);
    }

    /**
     * Creates a mycelium connection with a neighbouring tecton.
     *
     * @param t The tecton to connect to.
     * @throws Exception if the tecton is not a neighbour.
     */
    public void addConnection(Tecton t) throws Exception{

        if (!isNeighbour(t)) throw new Exception(view.notNeighbour(t));

        neighbours.put(t, true);
        t.neighbours.put(this, true);

        view.connectionAdded(t);
    }

    /**
     * Checks if the tecton has any mycelium owned by the given farmer.
     *
     * @param f The fungus farmer.
     * @return True if the farmer owns a mycelium on this tecton.
     */
    public boolean hasMycelium(FungusFarmer f) {
        for (Mycelium m : myceliumList) {
            if (m.getOwner() == f) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a mycelium to the tecton.
     *
     * @param m The mycelium to add.
     * @throws Exception if not allowed to add.
     */
    public abstract void addMycelium(Mycelium m) throws Exception;

    /**
     * Adds a spore to the tecton.
     *
     * @param s The spore to add.
     */
    public void addSpore(Spore s) { 
        sporeList.add(s);

        view.sporeAdded(s);
     }

    /**
     * Adds an insect to the tecton.
     *
     * @param i The insect to add.
     */
    public void addInsect(Insect i) {
        insectList.add(i); 

        view.insectAdded(i);
    }

    /**
     * Removes a neighbouring tecton without removing a connection.
     *
     * @param t The neighbour to remove.
     */
    private void removeNeighbour(Tecton t) {

        neighbours.remove(t); 
        t.neighbours.remove(this);

        view.neighbourRemoved(t);
    }

    /**
     * Removes a mycelium from the tecton.
     *
     * @param m The mycelium to remove.
     */
    public void removeMycelium(Mycelium m) { 
        myceliumList.remove(m); 

        view.myceliumRemoved(m);
    }

    /**
     * Removes an insect from the tecton.
     *
     * @param i The insect to remove.
     */
    public void removeInsect(Insect i) { 
        insectList.remove(i); 

        view.insectRemoved(i);
    }

    /**
     * Removes a spore from the tecton.
     *
     * @param s The spore to remove.
     */
    public void removeSpore(Spore s) { 
        sporeList.remove(s); 

        view.sporeRemoved(s);
    }

    /**
     * Removes a mycelium connection with a neighbour.
     *
     * @param t The neighbour tecton.
     * @throws Exception if no connection exists.
     */
    public void removeConnection(Tecton t) throws Exception {

        if(!isNeighbour(t)) throw new Exception(view.notNeighbour(t));
        if (!isConnectedTo(t)) throw new Exception(view.notConnectedByMycelium(t));

        neighbours.put(t, false);
        t.neighbours.put(this, false);

        view.removeConnection(t);
    }

    /**
     * Checks if a tecton is a neighbour.
     *
     * @param t The tecton to check.
     * @return True if neighbour.
     */
    public boolean isNeighbour(Tecton t) {
        List<Tecton> neighbourList = new ArrayList<>(neighbours.keySet());
        return neighbourList.contains(t);
    }

    /**
     * Checks if a tecton is a direct neighbour or a neighbour's neighbour.
     *
     * @param t The tecton to check.
     * @return True if neighbour or neighbour's neighbour.
     */
    public boolean isNeighbourOrNeighboursNeighbour(Tecton t) {
        boolean isNeighbourBoolean = isNeighbour(t);
        if(isNeighbourBoolean) return true;

        boolean isNeighboursNeighbourBoolean = false;

        List<Tecton> neighbourList = new ArrayList<>(neighbours.keySet());

        for (Tecton tecton : neighbourList) {
            List<Tecton> neighboursNeigbourList = new ArrayList<>(tecton.neighbours.keySet());
            if (neighboursNeigbourList.contains(t)) { isNeighboursNeighbourBoolean = true; }
        }
        return isNeighboursNeighbourBoolean;
    }

    /**
     * Checks if the tecton is connected by mycelium to another tecton.
     *
     * @param t The tecton to check.
     * @return True if connected.
     */
    public boolean isConnectedTo(Tecton t) {
        if(!neighbours.containsKey(t))
            return false;
        return neighbours.get(t);
    }

    /**
     * Checks if a body can be placed on this tecton.
     *
     * @return True if possible.
     */
    public abstract boolean canPlaceBody();

    /**
     * Helper method to check if placing a body is allowed (no body already exists).
     *
     * @return True if no body is present.
     */
    protected boolean canPlaceBodyHelper() {

        for (Mycelium m : myceliumList){
            if (m.hasBody()) return false;
        }

        return true;
    }

    /**
     * Checks if a specific spore exists on the tecton.
     *
     * @param spore The spore to search for.
     * @return True if found.
     */
    public boolean hasSpores(Spore spore) {
        if (sporeList.isEmpty()) { return false; }
        for (Spore s : sporeList) {
            if (s.equals(spore)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Defines how the tecton breaks into new tectons.
     */
    public abstract void breakTecton();

    /**
     * Removes all mycelium from the tecton.
     *
     * @throws Exception if removal fails.
     */
    public abstract void vanishMycelium() throws Exception;

    /**
     * Manages neighbour relationships when a tecton breaks.
     *
     * @param newTecton The new tecton created from breaking.
     */
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

    /**
     * Removes all connections (mycelium) when breaking.
     */
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

    /**
     * Generates a random number and checks if it falls within a chance boundary.
     *
     * @param chance The success chance (1-100).
     * @return True if successful.
     */
    protected boolean generatedNumWithinBound(int chance) {
        int num = gen.nextInt(100) + 1;
        return num <= chance;
    }

    /**
     * Recursively searches for a body belonging to a specific farmer.
     *
     * @param owner The fungus farmer.
     * @param checkedTectons Already checked tectons to avoid loops.
     * @return True if found.
     */
    public boolean findBody(FungusFarmer owner, List<Tecton> checkedTectons) {
        checkedTectons.add(this);
        for (Mycelium mycelium : myceliumList) {
            if (mycelium.hasBody() && mycelium.getOwner().equals(owner)) {
                return true;
            } else {
                List<Tecton> newCheckedTectons = new ArrayList<>();
                for (Tecton neighbour : neighbours.keySet()) {
                    if (!checkedTectons.contains(neighbour)) {
                        newCheckedTectons.add(neighbour);
                    }
                }
                if (newCheckedTectons.isEmpty()) {
                    return false;
                }
                for (Tecton neighbour : newCheckedTectons) {
                    if (neighbour.findBody(owner, checkedTectons)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns the name of the tecton.
     *
     * @return The tecton's name.
     */
    public String getName() { return name; }

    /**
     * Converts the tecton into a human-readable string format.
     *
     * @param tectonType The type of the tecton.
     * @return The formatted tecton string.
     */
    protected String tectonToString(String tectonType) {

        String returnString = "\n--------------------------------------------------------------------------------------------------------";

        returnString += "\nTecton name: ";

        returnString += name;

        returnString += "\nTecton type: ";

        returnString += tectonType;

        returnString += "\n---------------------------";
        returnString += "\nSpores on tecton: ";

        Collections.sort(sporeList, new Comparator<Spore>() {
            @Override
            public int compare(Spore s1, Spore s2) {
                return s1.getName().compareTo(s2.getName());
            }
        });

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

        Collections.sort(neighbourList, new Comparator<Tecton>() {
            @Override
            public int compare(Tecton t1, Tecton t2) {
                return t1.getName().compareTo(t2.getName());
            }
        });

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
                if (neighbours.get(t)) {
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

        Collections.sort(myceliumList, new Comparator<Mycelium>() {
            @Override
            public int compare(Mycelium e1, Mycelium e2) {
                return e1.getName().compareTo(e2.getName());
            }
        });

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

        Collections.sort(insectList, new Comparator<Insect>() {
            @Override
            public int compare(Insect i1, Insect i2) {
                return i1.getName().compareTo(i2.getName());
            }
        });

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
        returnString += "\n--------------------------------------------------------------------------------------------------------\n";

        return returnString;
    }

    /**
     * Checks if two tectons are completely equal (deep comparison).
     *
     * @param t The other tecton.
     * @return True if tectons are equal.
     */
    public boolean isEqual(Tecton t) {
        return toString().equals(t.toString());
    }

    /**
     * Returns the list of spores on the tecton.
     *
     * @return List of spores.
     */
    public List<Spore> getSporeList() { return sporeList; }

    /**
     * Returns the list of insects on the tecton.
     *
     * @return List of insects.
     */
    public List<Insect> getInsectList() { return insectList; }

    /**
     * Returns the list of mycelia on the tecton.
     *
     * @return List of mycelia.
     */
    public List<Mycelium> getMyceliumList() { return myceliumList; }
}