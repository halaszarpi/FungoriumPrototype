package fungorium;

public class FungusBodyView {
    public String noAvailableSpore() {
        return "Fungus body has no spore to scatter!";
    }

    public String noAvailableActionPoint(FungusFarmer farmer) {
        return "Fungus farmer (" + farmer.getName() + ") has not enough action point!";
    }
}