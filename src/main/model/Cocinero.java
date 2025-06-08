package main.model;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Cocinero implements Runnable{
    public static final int TIEMPO_DESDE = 3000;
    public static final int TIEMPO_HASTA = 4000;
    public static final int TIEMPO_ESPERA_REINTENTO = 100;
    private static final AtomicInteger contadorCocinero = new AtomicInteger();
    private final int numCocinero;
    private final ProveedorDePedidos proveedorDePedidos;
    private ExecutorService cocinaInterna;
    private final Semaphore capacidad;

    public Cocinero(int capacidadMaxima, ProveedorDePedidos proveedorDePedidos) {
        this.numCocinero = contadorCocinero.incrementAndGet();
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
                    pedido.setCocineroAsignado(this);
                    this.cocinaInterna.submit(()->{
                        try {
                            TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(TIEMPO_DESDE, TIEMPO_HASTA)); //Entre 3s - 4s
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
                    TimeUnit.MILLISECONDS.sleep(TIEMPO_ESPERA_REINTENTO); //espera un poco antes de volver a intentar
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
