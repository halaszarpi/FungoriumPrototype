package fungorium;

import java.util.*;

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

    public void addNeighbour(Tecton t) { 
        neighbours.put(t, false);
        t.neighbours.put(this, false);

        view.neighbourAdded(t);
    }

    public void addConnection(Tecton t) throws Exception{

        if (!isNeighbour(t)) throw new Exception(view.notNeighbour(t));

        neighbours.put(t, true);
        t.neighbours.put(this, true);

        view.connectionAdded(t);
    }

    public boolean hasMycelium(FungusFarmer f) {
        for (Mycelium m : myceliumList) {
            if (m.getOwner() == f) {
                return true;
            }
        }
        return false;
    }

    public abstract void addMycelium(Mycelium m) throws Exception;

    public void addSpore(Spore s) { 
        sporeList.add(s);

        view.sporeAdded(s);
     }

    public void addInsect(Insect i) {
        insectList.add(i); 

        view.insectAdded(i);
    }

    private void removeNeighbour(Tecton t) {

        neighbours.remove(t); 
        t.neighbours.remove(this);

        view.neighbourRemoved(t);
    }

    public void removeMycelium(Mycelium m) { 
        myceliumList.remove(m); 

        view.myceliumRemoved(m);
    }

    public void removeInsect(Insect i) { 
        insectList.remove(i); 

        view.insectRemoved(i);
    }

    public void removeSpore(Spore s) { 
        sporeList.remove(s); 

        view.sporeRemoved(s);
    }

    public void removeConnection(Tecton t) throws Exception {

        if(!isNeighbour(t)) throw new Exception(view.notNeighbour(t));
        if (!isConnectedTo(t)) throw new Exception(view.notConnectedByMycelium(t));

        neighbours.put(t, false);
        t.neighbours.put(this, false);

        view.removeConnection(t);
    }

    public boolean isNeighbour(Tecton t) {
        List<Tecton> neighbourList = new ArrayList<>(neighbours.keySet());
        return neighbourList.contains(t);
    }

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

    public boolean isConnectedTo(Tecton t) {
        if(!neighbours.containsKey(t))
            return false;
        return neighbours.get(t);
    }

    public abstract boolean canPlaceBody();

    protected boolean canPlaceBodyHelper() {

        for (Mycelium m : myceliumList){
            if (m.hasBody()) return false;
        }

        return true;
    }

    public boolean hasSpores(Spore spore) {
        if (sporeList.isEmpty()) { return false; }
        for (Spore s : sporeList) {
            if (s.equals(spore)) {
                return true;
            }
        }
        return false;
    }

    public abstract void breakTecton();

    public abstract void vanishMycelium() throws Exception;

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
        int num = gen.nextInt(100) + 1;
        return num <= chance;
    }

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

    public String getName() { return name; }

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
        return toString().equals(t.toString());
    }

    public List<Spore> getSporeList() { return sporeList; }

    public List<Insect> getInsectList() { return insectList; }

    public List<Mycelium> getMyceliumList() { return myceliumList; }
}