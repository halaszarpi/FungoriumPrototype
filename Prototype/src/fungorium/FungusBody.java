package fungorium;

import java.util.Random;

public class FungusBody {
    private final Mycelium mycelium;
    private int remainingSpores;
    private int scatteringCooldown;
    private final FungusBodyView view;

    public FungusBody(Mycelium mycelium) {
        this.mycelium = mycelium;
        this.remainingSpores = 5;
        this.scatteringCooldown = 0;
        this.view = new FungusBodyView(this);
    }

    private Spore createRandomSpore(FungusFarmer owner) {
        Random rand = new Random();

        int nutrientContent = rand.nextInt(5) + 1;
        int effectDuration = rand.nextInt(3) + 1;

        int sporeType = rand.nextInt(6);

        return switch (sporeType) {
            case 0 -> new OrdinarySpore(owner, nutrientContent, 0, owner.getNewSporeName());
            case 1 -> new SlowingSpore(owner, nutrientContent, effectDuration, owner.getNewSporeName());
            case 2 -> new StunningSpore(owner, nutrientContent, effectDuration, owner.getNewSporeName());
            case 3 -> new BoosterSpore(owner, nutrientContent, effectDuration, owner.getNewSporeName());
            case 4 -> new AntiSeverSpore(owner, nutrientContent, effectDuration, owner.getNewSporeName());
            case 5 -> new InsectDuplicatorSpore(owner, nutrientContent, 1, owner.getNewSporeName());
            default -> new OrdinarySpore(owner, nutrientContent, 0, owner.getNewSporeName());
        };
    }

    public Mycelium getMycelium() {
        return this.mycelium;
    }

    public void scatterTo(Tecton targetTecton) throws Exception {
        FungusFarmer farmer = mycelium.getOwner();

        if (scatteringCooldown > 0) {
            throw new Exception(view.noAvailableSpore());
        }

        if (farmer.getActionPoints() <= 0) {
            throw new Exception(view.noAvailableActionPoint());
        }

        Spore newSpore = createRandomSpore(farmer);
        boolean grownBody = remainingSpores <= 3;
        Tecton currentTecton = mycelium.getTecton();
        boolean sameTecton = currentTecton.equals(targetTecton);
        boolean neigbour = currentTecton.isNeighbour(targetTecton);

        boolean neighbourOrNeighboursNeigbour = currentTecton.isNeighbourOrNeighboursNeighbour(targetTecton);

        if ((!grownBody && (neigbour || sameTecton)) || (grownBody && (neighbourOrNeighboursNeigbour || sameTecton))) {
            targetTecton.addSpore(newSpore);
            farmer.addSpore(newSpore);
            remainingSpores--;
            scatteringCooldown = 2;
        }

        if (remainingSpores == 0) {
            mycelium.bodyDied();
        }
    }

    public void reduceCooldown() {
        if (scatteringCooldown > 0) {
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

    public Player getOwner() {
        return mycelium.getOwner();
    }
}