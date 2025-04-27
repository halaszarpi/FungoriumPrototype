package fungorium;

public class FungusBodyView {

    FungusBody fungusBody;

    public FungusBodyView(FungusBody fungusBody) {
        this.fungusBody = fungusBody;
    }

    public String noAvailableSpore() {
        return "Fungus body has no spore to scatter!";
    }

    public String noAvailableActionPoint() {
        return "Fungus farmer (" + fungusBody.getOwner().getName() + ") has not enough action point!";
    }
}