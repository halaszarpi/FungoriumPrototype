package fungorium;

/**
 * Represents the view for a FungusBody in the Fungorium game.
 *
 * This class is responsible for providing user-friendly messages related to a FungusBody's actions,
 * such as when it cannot scatter spores or when the owning Fungus Farmer has insufficient action points.
 */
public class FungusBodyView {

    /** The FungusBody associated with this view. */
    FungusBody fungusBody;

    /**
     * Constructs a new FungusBodyView for a given FungusBody.
     *
     * @param fungusBody the FungusBody this view represents
     */
    public FungusBodyView(FungusBody fungusBody) {
        this.fungusBody = fungusBody;
    }

    /**
     * Returns an error message indicating that no spores are available for scattering.
     *
     * @return a string message about no available spores
     */
    public String noAvailableSpore() {
        return "Fungus body on (" + fungusBody.getMycelium().getName() + ") has no spore to scatter!";
    }

    /**
     * Returns an error message indicating that the owner does not have enough action points.
     *
     * @return a string message about not enough action points
     */
    public String noAvailableActionPoint() {
        return "Fungus farmer (" + fungusBody.getOwner().getName() + ") has not enough action point!";
    }
}