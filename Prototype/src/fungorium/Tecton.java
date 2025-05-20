package fungorium;

import java.util.*;

/**
 * Abstract class representing a Tecton in the Fungorium world.
 * A Tecton can have neighbours, spores, insects, and mycelia.
 * It also manages connections and breaking behavior.
 */
public abstract class Tecton implements IRoundFollower{

    protected TectonMap map;
    protected Map<Tecton, List<FungusFarmer>> neighbours;
    private List<Spore> sporeList;
    private List<Insect> insectList;
    protected List<Mycelium> myceliumList;
    protected String name;
    protected int breakPrecent;
    protected Random gen;
    protected TectonView view;
    protected GTecton gt;

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
        gt = null;
    }

    /**
     * Adds a tecton as a neighbour.
     *
     * @param t The tecton to add as a neighbour.
     */
    public void addNeighbour(Tecton t) { 
        neighbours.put(t, new ArrayList<>());
        t.neighbours.put(this, new ArrayList<>());
    }

    public void addConnection(Tecton t, FungusFarmer f) throws Exception{

        if (!isNeighbour(t)) throw new Exception(view.notNeighbour(t));

        List<FungusFarmer> fungusFarmerList = neighbours.get(t);
        fungusFarmerList.add(f);

        neighbours.put(t, fungusFarmerList);
        t.neighbours.put(this, fungusFarmerList);
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
    }

    /**
     * Removes a mycelium from the tecton.
     *
     * @param m The mycelium to remove.
     */
    public void removeMycelium(Mycelium m) { 
        myceliumList.remove(m); 
        view.myceliumRemoved(m);

        List<Tecton> neighbourList = new ArrayList<>(neighbours.keySet());

        for (Tecton t : neighbourList) {

            List<FungusFarmer> farmers = neighbours.get(t);

            for (FungusFarmer f : farmers) {

                if (f.equals(m.getOwner())) {
                    farmers.remove(f);
                    neighbours.put(t, farmers);
                }
            }

        }
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


    public void removeConnection(Mycelium m) throws Exception {
        Tecton t = m.getTecton();

        if(!isNeighbour(t)) throw new Exception(view.notNeighbour(t));
        if (!isConnectedTo(m)) throw new Exception(view.notConnectedByMycelium(t));

        List<FungusFarmer> fungusFarmerList = neighbours.get(t);
        fungusFarmerList.remove(m.getOwner());

        neighbours.put(t, fungusFarmerList);
        t.neighbours.put(this, fungusFarmerList);
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

    public List<Mycelium> getConnectedMyceliums() {

        List<Tecton> neighbourTectonList = new ArrayList<>(neighbours.keySet());
        List<Mycelium> returnMyceliumList = new ArrayList<>();

        for (Tecton t : neighbourTectonList) {
            List<Mycelium> neighbourMyceliumList = t.getMyceliumList();
            List<FungusFarmer> fungusFarmerList = neighbours.get(t);
            
            for (Mycelium m : neighbourMyceliumList) {
                if (fungusFarmerList.contains(m.getOwner())) returnMyceliumList.add(m);
            }
        }

        return returnMyceliumList;

    }


    public boolean isConnectedTo(Mycelium m) {
        Tecton t = m.getTecton();
        if(!neighbours.containsKey(t))
            return false;
        return !neighbours.get(t).isEmpty();
    }

    public boolean isConnectedTo(Tecton t) {
        if(!neighbours.containsKey(t))
            return false;
        return !neighbours.get(t).isEmpty();
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
    public abstract void breakTecton(String oneNeighbourNameOfTecton);

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
    protected void manageNeighboursAtBreak(Tecton newTecton, Tecton randomNeighbour) {

        List<Tecton> neighbourList = new ArrayList<>(neighbours.keySet());

        if (neighbourList.isEmpty()) return;

        if (randomNeighbour == null) {
            int randomNeighbourIndex = gen.nextInt(neighbourList.size());
            randomNeighbour = neighbourList.get(randomNeighbourIndex);
        }
        
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
            Map<Tecton, List<FungusFarmer>> tmpMap = new HashMap<>();

            for (Tecton t : neighbours.keySet()) {
                tmpMap.put(t, new ArrayList<>());
            }
            neighbours = tmpMap;
        }
        catch(Exception e) {
            //The breakTecton call is always true, so there is nothing to do
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
    public boolean findBody(FungusFarmer owner, List<Tecton> checkedTectons, Tecton currentTecton) {
        checkedTectons.add(currentTecton);

        for(Mycelium m : currentTecton.getMyceliumList()) {
            if (m.hasBody() && owner.equals(m.getOwner())) { return true; }
        }

        List<Tecton> newCheckedTectons = new ArrayList<>();
        Map<Tecton, List<FungusFarmer>> neighbourMap = currentTecton.getNeighbourMap();
        List<Tecton> neighbourList = new ArrayList<>(neighbourMap.keySet());

        for (Tecton t : neighbourList) {
            if (neighbourMap.get(t) != null && !checkedTectons.contains(t)) { newCheckedTectons.add(t); }
        }

        for (Tecton t : newCheckedTectons) {
            if(findBody(owner, checkedTectons, t)) return true;
        }

        return false;

    }

    /**
     * Returns the name of the tecton.
     *
     * @return The tecton's name.
     */
    public String getName() { return name; }

    public TectonView getView() { return view; }

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

    public Map<Tecton, List<FungusFarmer>> getNeighbourMap() { return neighbours; }

    public void setBreakPercent(int percentage) {
        this.breakPrecent = percentage;
    }

    protected Tecton generateRandomTectonNeighbour(String oneNeighbourNameOfTecton) {
        
        ArrayList<Tecton> neighbourList = new ArrayList<>(neighbours.keySet());
        int randomTectonIndex = gen.nextInt(neighbourList.size());
        Tecton randomTecton = neighbourList.get(randomTectonIndex);

        try {
            if (oneNeighbourNameOfTecton != null && map.findTecton(oneNeighbourNameOfTecton) != null) {
                randomTecton = map.findTecton(oneNeighbourNameOfTecton);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return randomTecton;
    }

    public void setGTecton(GTecton gt) {
        this.gt = gt;
    }

    public GTecton getGTecton() { 
        return gt;
    }
}