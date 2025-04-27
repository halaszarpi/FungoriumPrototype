package fungorium;

public class InsectKeeperView {
    InsectKeeper insectKeeper;

    public InsectKeeperView(InsectKeeper insectKeeper) {
        this.insectKeeper = insectKeeper;
    }

    public void chooseAction() {
        System.out.println(insectKeeper.toString());
        System.out.println("Enter command: \n\tMOVETOTECTON(1-3) [INSNAME] [TECNAME]\n\t CUTMYC(1) [INSNAME] [TECNAME]\n\t EATSPORE(1) [INSNAME] [SPONAME]\n\t INFO\n\tSHOWMAP\n\t SKIP");
    }

    public void invalidActionMessage() {
        System.out.println("Invalid action!");
    }

    public void info(){
        System.out.println("INSECTKEEPER INFO:");
        System.out.println("-------------------");
        System.out.println("\tNAME:"+insectKeeper.getName());
        System.out.println("\n\tACTIONPOINTS:"+insectKeeper.getActionPoints());
        System.out.println("\n\tSCORE:"+insectKeeper.getScore());
        System.out.println("\nINSECT'S");
        for(Insect insect:insectKeeper.getInsects()){
            System.out.println("\t"+insect.getName() + "on tecton (" + insect.getTecton().getName() + ")");                                                                                                                                                                                                                                                        }
        System.out.println("-------------------");

    }


}