package main.model;

public class Repartidor implements Runnable{
    private static int contadorRepartidor;
    private final int numRepartidor;

    public Repartidor() {
        this.numRepartidor = contadorRepartidor++;
    }
    @Override
    public void run() {

    }
    public int getNumRepartidor() {
        return this.numRepartidor;
    }
}
