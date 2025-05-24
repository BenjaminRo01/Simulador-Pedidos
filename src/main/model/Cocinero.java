package main.model;

public class Cocinero implements Runnable{
    private static int contadorCocinero;
    private final int numCocinero;
    private int maxThreads;

    public Cocinero(int maxThreads) {
        this.maxThreads = maxThreads;
        this.numCocinero = contadorCocinero++;
    }
    @Override
    public void run() {

    }
    public int getNumCocinero() {
        return this.numCocinero;
    }
}
