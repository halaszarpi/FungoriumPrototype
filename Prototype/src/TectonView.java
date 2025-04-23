//public class TectonView {
//    public String notNeighbour(Tecton t1, Tecton t2) { return "The tectons ("+ t1.getName() + ", " + t2.getName() + ") are not neighbours!"; }
//
//    public String notNeighbourOrNeigboursNeighbour(Tecton t1, Tecton t2) { return "The tectons ("+ t1.getName() + ", " + t2.getName() + ") are not neighbours and not neighbour to any neighbour!"; }
//
//    public String notConnectedByMycelium(Tecton t1, Tecton t2) { return "The tectons ("+ t1.getName() + ", " + t2.getName() + ") are not connected by mycelium!"; }
//
//    public String tectonHasNoSpores(Tecton t) { return "The tecton (" + t.getName() + ") does not have spores!";}
//
//    public String alreadyHasFungusbody(Tecton t) { return "There is already a fungus body on the tecton (" + t.getName() + ")!"; }
//
//    public String singleMyceliumTectonAlreadyHasMycelium(Tecton t) { return "The single mycelium tecton (" + t.getName() + ") has already a mycelium on it!"; }
//
//    public String noBodyTectonCannotPlaceBody(Tecton t) { return "Cannot place fungus body on no body tecton (" + t.getName() + ")!"; }
//
//    public void tectonCreated(Tecton t) { System.out.println("Tecton (" + t.getName() + ") is created!"); }
//
//    public void tectonBreaks(Tecton t, Tecton newTecton) { System.out.println("Tecton (" + t.getName() + ") broke, new half tectons name: "+ newTecton.getName() + "!"); }
//
//    public void myceliumVanishes(Tecton t, Mycelium m) { System.out.println("Mycelium (" + m.getName() + ") vanished from mycelium vanisher tecton (" + t.getName() + ")!"); }
//
//    public void neighbourAdded(Tecton t, Tecton newTecton) { System.out.println("Tecton (" + newTecton.getName() + ") is now a neighbour of (" + t.getName() + ")!"); }
//
//    public void connectionAdded(Tecton t, Tecton newTecton) { System.out.println("Tecton (" + newTecton.getName() + ") is connected to (" + t.getName() + ")!"); }
//
//    public void myceliumAdded(Tecton t, Mycelium m) { System.out.println("Mycelium (" + m.getName() + ") is added on tecton (" + t.getName() + ")!"); }
//
//    public void sporeAdded(Tecton t, Spore s) { System.out.println("Spore (" + s.getName() + ") is added on tecton (" + t.getName() + ")!"); }
//
//    public void insectAdded(Tecton t, Insect i) { System.out.println("Insect (" + i.getName() + ") is added on tecton (" + t.getName() + ")!"); }
//
//    public void neighbourRemoved(Tecton t, Tecton newTecton) { System.out.println("Tecton (" + newTecton.getName() + ") is no longer neighbour of (" + t.getName() + ")!"); }
//
//    public void myceliumRemoved(Tecton t, Mycelium m) { System.out.println("Mycelium (" + m.getName() + ") is no longer on tecton (" + t.getName() + ")!"); }
//
//    public void insectRemoved(Tecton t, Insect i) { System.out.println("Insect (" + i.getName() + ") is no longer on tecton (" + t.getName() + ")!"); }
//
//    public void sporeRemoved(Tecton t, Spore s) { System.out.println("Spore (" + s.getName() + ") is no longer on tecton (" + t.getName() + ")!"); }
//
//    public void removeConnection(Tecton t, Tecton newTecton) { System.out.println("Tecton (" + newTecton.getName() + ") is no longer connected to (" + t.getName() + ")!"); }
//
//}