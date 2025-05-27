package main.model;

import java.util.concurrent.*;

public class Cocinero implements Runnable{
    private static int contadorCocinero;
    private final int numCocinero;
    private final ProveedorDePedidos proveedorDePedidos;
    private ExecutorService cocinaInterna;
    private final Semaphore capacidad;

    public Cocinero(int capacidadMaxima, ProveedorDePedidos proveedorDePedidos) {
        this.numCocinero = contadorCocinero++;
        this.proveedorDePedidos = proveedorDePedidos;
        this.cocinaInterna = Executors.newFixedThreadPool(capacidadMaxima);
        this.capacidad = new Semaphore(capacidadMaxima);
    }
    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()){
            try{
                if(this.capacidad.tryAcquire()){
                    Pedido pedido = this.proveedorDePedidos.obtenerPedidoPendiente();
                    this.cocinaInterna.submit(()->{
                        try {
                            TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(3000, 4000)); //Entre 3s - 4s
                            this.proveedorDePedidos.agregarPedidoCocinado(pedido);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        finally {
                            this.capacidad.release();
                        }
                    });
                }
                else{
                    TimeUnit.MILLISECONDS.sleep(100); //espera un poco antes de volver a intentar
                }
            }
            catch(InterruptedException e){
                Thread.currentThread().interrupt();
                break;
            }
        }
        this.cocinaInterna.shutdown();
    }
    public int getNumCocinero() {
        return this.numCocinero;
    }
}
