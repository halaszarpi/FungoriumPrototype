package fungorium;

import java.util.Random;

/**
 * Represents a FungusBody in the Fungorium game.
 *
 * A FungusBody is attached to a Mycelium and is responsible for creating and scattering spores
 * to nearby tectons. It manages the available spores and scattering cooldowns.
 */
public class FungusBody {
    private final Mycelium mycelium;
    private int remainingSpores;
    private int scatteringCooldown;
    private final FungusBodyView view;

    /**
     * Constructs a new FungusBody associated with a specific Mycelium.
     *
     * @param mycelium the Mycelium this FungusBody is attached to
     */
    public FungusBody(Mycelium mycelium) {
        this.mycelium = mycelium;
        this.remainingSpores = 5;
        this.scatteringCooldown = 0;
        this.view = new FungusBodyView(this);
    }

    /**
     * Creates a random Spore. If sporeTypeString and sporeName are provided,
     * creates a specific type of Spore with fixed nutrient content and effect duration.
     *
     * @param owner the FungusFarmer who owns the new Spore
     * @param sporeTypeString the desired type of the Spore (optional)
     * @param sporeName the desired name of the Spore (optional)
     * @return a new Spore instance
     */
    private Spore createRandomSpore(FungusFarmer owner, String sporeTypeString, String sporeName) {
        Random rand = new Random();

        int nutrientContent = rand.nextInt(5) + 1;
        int effectDuration = rand.nextInt(3) + 1;

        int sporeType = rand.nextInt(6);

        String resultSporeTypeString = null;

        switch (sporeType) {
            case 0 -> resultSporeTypeString = "ORD";
            case 1 -> resultSporeTypeString = "SLO";
            case 2 -> resultSporeTypeString = "STU";
            case 3 -> resultSporeTypeString = "BST";
            case 4 -> resultSporeTypeString = "ANT";
            case 5 -> resultSporeTypeString = "DUP";
            default -> resultSporeTypeString = "ORD";
        }

        if (sporeTypeString != null && sporeName != null) { 
            resultSporeTypeString = sporeTypeString; 
            nutrientContent = 5;
            effectDuration = 3;
        }

        return switch (resultSporeTypeString) {
            case "ORD" -> new OrdinarySpore(owner, nutrientContent, effectDuration, owner.getNewSporeName());
            case "SLO" -> new SlowingSpore(owner, nutrientContent, effectDuration, owner.getNewSporeName());
            case "STU" -> new StunningSpore(owner, nutrientContent, effectDuration, owner.getNewSporeName());
            case "BST" -> new BoosterSpore(owner, nutrientContent, effectDuration, owner.getNewSporeName());
            case "ANT" -> new AntiSeverSpore(owner, nutrientContent, effectDuration, owner.getNewSporeName());
            case "DUP" -> new InsectDuplicatorSpore(owner, nutrientContent, effectDuration, owner.getNewSporeName());
            default -> new OrdinarySpore(owner, nutrientContent, effectDuration, owner.getNewSporeName());
        };
    }

    /**
     * Gets the Mycelium associated with this FungusBody.
     *
     * @return the Mycelium instance
     */
    public Mycelium getMycelium() {
        return this.mycelium;
    }

    /**
     * Attempts to scatter a spore onto a target Tecton.
     *
     * Throws an exception if scattering is not allowed (e.g., cooldown active or no action points).
     *
     * @param targetTecton the Tecton to scatter to
     * @param sporeType the type of Spore to scatter (optional)
     * @param sporeName the name of the Spore (optional)
     * @throws Exception if scattering cannot be performed
     */
    public void scatterTo(Tecton targetTecton, String sporeType, String sporeName) throws Exception {
        FungusFarmer farmer = mycelium.getOwner();

        if (scatteringCooldown > 0) {
            throw new Exception(view.noAvailableSpore());
        }

        if (farmer.getActionPoints() <= 0) {
            throw new Exception(view.noAvailableActionPoint());
        }

        Spore newSpore = createRandomSpore(farmer, sporeType, sporeName);
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

    /**
     * Reduces the scattering cooldown by one round if it is active.
     */
    public void reduceCooldown() {
        if (scatteringCooldown > 0) {
            scatteringCooldown--;
        }
    }

    /**
     * Returns a string representation of the FungusBody, including its state.
     *
     * @return a string describing the FungusBody
     */
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

    /**
     * Gets the owner (player) of this FungusBody.
     *
     * @return the Player who owns the Mycelium
     */
    public Player getOwner() {
        return mycelium.getOwner();
    }
}