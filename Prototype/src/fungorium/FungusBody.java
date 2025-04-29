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
    String typeToCreate;

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
        this.typeToCreate = "random";
    }

    public FungusBody(Mycelium mycelium, String sporeType) {
        this.mycelium = mycelium;
        this.remainingSpores = 5;
        this.scatteringCooldown = 0;
        this.view = new FungusBodyView(this);
        this.typeToCreate = sporeType;
    }

    /**
     * Creates a random Spore. If sporeTypeString and sporeName are provided,
     * creates a specific type of Spore with fixed nutrient content and effect duration.
     * @param owner the FungusFarmer who owns the new Spore
     * @return a new Spore instance
     */
    private Spore createSpore(FungusFarmer owner) {
        int nutrientContent = 5;
        int effectDuration = 3;
        switch (typeToCreate.toUpperCase()){
            case "RANDOM" -> {
                Random rand = new Random();

                nutrientContent = rand.nextInt(5) + 1;
                effectDuration = rand.nextInt(3) + 1;

                int randomSporeType = rand.nextInt(6);

                switch (randomSporeType) {
                    case 1 -> new SlowingSpore(owner, nutrientContent, effectDuration, owner.getNewSporeName());
                    case 2 -> new StunningSpore(owner, nutrientContent, effectDuration, owner.getNewSporeName());
                    case 3 -> new BoosterSpore(owner, nutrientContent, effectDuration, owner.getNewSporeName());
                    case 4 -> new AntiSeverSpore(owner, nutrientContent, effectDuration, owner.getNewSporeName());
                    case 5 -> new InsectDuplicatorSpore(owner, nutrientContent, effectDuration, owner.getNewSporeName());
                    default -> new OrdinarySpore(owner, nutrientContent, effectDuration, owner.getNewSporeName());
                }
            }
            case "BST" -> {
                return new BoosterSpore(owner, nutrientContent, effectDuration, owner.getNewSporeName());
            }
            case "ANT" -> {
                return new AntiSeverSpore(owner, nutrientContent, effectDuration, owner.getNewSporeName());
            }
            case "SLO" -> {
                return new SlowingSpore(owner, nutrientContent, effectDuration, owner.getNewSporeName());
            }
            case "STU" -> {
                return new StunningSpore(owner, nutrientContent, effectDuration, owner.getNewSporeName());
            }
            case "DUP" -> {
                return new InsectDuplicatorSpore(owner, nutrientContent, effectDuration, owner.getNewSporeName());
            }
            default -> {
                return new OrdinarySpore(owner, nutrientContent, effectDuration, owner.getNewSporeName());
            }
        }
        return new OrdinarySpore(owner, nutrientContent, effectDuration, owner.getNewSporeName());
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
     * @throws Exception if scattering cannot be performed
     */
    public void scatterTo(Tecton targetTecton) throws Exception {
        FungusFarmer farmer = mycelium.getOwner();

        if (scatteringCooldown > 0) {
            throw new Exception(view.noAvailableSpore());
        }

        if (farmer.getActionPoints() <= 0) {
            throw new Exception(view.noAvailableActionPoint());
        }

        Spore newSpore = createSpore(farmer);
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