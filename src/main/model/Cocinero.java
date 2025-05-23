package main.model;

public class Cocinero implements Runnable{
    private static int contadorCocinero;
    private final int numCocinero;

    public Cocinero() {
        this.numCocinero = contadorCocinero++;
    }
    @Override
    public void run() {

    }
    public int getNumCocinero() {
        return this.numCocinero;
    }
}
