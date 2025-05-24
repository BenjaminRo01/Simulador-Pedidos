package main.model;

public class Cliente implements Runnable{
    private static int contadorCliente;
    private final int numCliente;
    private int maxThreads;

    public Cliente(int maxThreads) {
        this.maxThreads = maxThreads;
        this.numCliente = contadorCliente++;
    }
    @Override
    public void run() {

    }
    public int getNumCliente() {
        return this.numCliente;
    }
}
