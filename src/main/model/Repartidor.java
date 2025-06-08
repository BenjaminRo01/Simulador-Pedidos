package main.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Repartidor implements Runnable{
    public static final int TIEMPO_DESDE = 1000;
    public static final int TIEMPO_HASTA = 3000;
    private static final AtomicInteger contadorRepartidor = new AtomicInteger();;
    private final int numRepartidor;
    private int capacidadMaxima;
    private final ProveedorDePedidos proveedorDePedidos;

    public Repartidor(int capacidadMaxima, ProveedorDePedidos proveedorDePedidos) {
        this.numRepartidor = contadorRepartidor.incrementAndGet();
        this.capacidadMaxima = capacidadMaxima;
        this.proveedorDePedidos = proveedorDePedidos;
    }
    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()){
            try {
                List<Pedido> pedidosARepartir = new ArrayList<>(); //Se crea nuevamente la lista de pedidos
                // FASE DE RECOLECCION
                for (int i = 0; i < this.capacidadMaxima; i++) {
                    Pedido pedido = this.proveedorDePedidos.obtenerPedidoCocinado();
                    if (pedido == null) {
                        break;
                    }
                    pedido.setRepartidorAsignado(this);
                    pedidosARepartir.add(pedido);
                }
                // FASE DE ENTREGA
                for(Pedido p : pedidosARepartir){
                    TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(TIEMPO_DESDE, TIEMPO_HASTA)); // Entre 1s - 3s
                    this.proveedorDePedidos.agregarPedidoEntregado(p);
                }
            }
            catch (InterruptedException e){
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    public int getNumRepartidor() {
        return this.numRepartidor;
    }
}
