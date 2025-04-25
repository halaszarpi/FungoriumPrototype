package fungorium;
import java.util.Random;

public class FungusBody {
    private Mycelium mycelium;
    private int remainingSpores;
    private int scatteringCooldown;
    private FungusBodyView view;

    private Spore createRandomSpore(String sporeName, FungusFarmer owner) {
        Random rand = new Random();

        int nutrientContent = rand.nextInt(5) + 1;
        int effectDuration = rand.nextInt(3) + 1;

        int sporeType = rand.nextInt(6);

        switch (sporeType) {
            case 0:
                return new OrdinarySpore(owner, nutrientContent, 0, sporeName);
            case 1:
                return new SlowingSpore(owner, nutrientContent, effectDuration, sporeName);
            case 2:
                return new StunningSpore(owner,nutrientContent, effectDuration, sporeName);
            case 3:
                return new BoosterSpore(owner, nutrientContent, effectDuration, sporeName);
            case 4:
                return new AntiSeverSpore(owner, nutrientContent, effectDuration, sporeName);
            case 5:
                return new InsectDuplicatorSpore(owner, nutrientContent, 1, sporeName);
            default:
                return new OrdinarySpore(owner, nutrientContent, 0, sporeName);
        }
    }

    public FungusBody(Mycelium myceliumm, FungusBodyView view) {
        this.mycelium = mycelium;
        this.remainingSpores = 5;
        this.scatteringCooldown = 2;
        this.view = view;

    }

    public Mycelium getMycelium() {
        return this.mycelium;
    }

    public void scatterTo(Tecton targetTecton, String sporeName) throws Exception {
        FungusFarmer farmer = mycelium.getFarmer();
        
        if(scatteringCooldown > 0) {
            throw new Exception(view.noAvailableSpore());
        }

        if (farmer.getActionPoints() <= 0) {
            throw new Exception(view.noAvailableActionPoint(farmer));
        }
        Spore newSpore = createRandomSpore(sporeName, farmer);
        boolean grownBody = remainingSpores <= 3;
        Tecton currentTecton = mycelium.getTecton();
        boolean neigbour = currentTecton.isNeighbour(targetTecton);
        boolean neighbourOrNeighboursNeigbour = currentTecton.isNeighbourOrNeighboursNeighbour(targetTecton);
         if ((!grownBody && neigbour) || (grownBody && neighbourOrNeighboursNeigbour)) {
            targetTecton.addSpore(newSpore);
            remainingSpores--;
            scatteringCooldown = 2;
         }
        if(remainingSpores == 0) {
            mycelium.bodyDied();
        }
    }

    public void reduceCooldown(){
        if(scatteringCooldown > 0){
            scatteringCooldown--;
        }
    }

    @Override
    public String toString() {
        String returnString = "FungusBody: ";
        returnString += "\nMycelium: ";
        returnString += mycelium;
        returnString += "\nRemainingSpores: ";
        returnString += remainingSpores;
        returnString += "\nScatteringCooldown: ";
        returnString += scatteringCooldown;
        return returnString;
    }
}