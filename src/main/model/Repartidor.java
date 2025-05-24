package main.model;

public class Repartidor implements Runnable{
    private static int contadorRepartidor;
    private final int numRepartidor;
    private int maxThreads;

    public Repartidor(int maxThreads) {
        this.maxThreads = maxThreads;
        this.numRepartidor = contadorRepartidor++;
    }
    @Override
    public void run() {

    }
    public int getNumRepartidor() {
        return this.numRepartidor;
    }
}
