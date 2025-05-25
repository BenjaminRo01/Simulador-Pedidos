package main.model;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Repartidor implements Runnable{
    private static int contadorRepartidor;
    private final int numRepartidor;
    private int capacidadMaxima;
    private final ProveedorDePedidos proveedorDePedidos;

    public Repartidor(int capacidadMaxima, ProveedorDePedidos proveedorDePedidos) {
        this.numRepartidor = contadorRepartidor++;
        this.capacidadMaxima = capacidadMaxima;
        this.proveedorDePedidos = proveedorDePedidos;
    }
    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()){
            try {
                List<Pedido> pedidosARepartir = new ArrayList<>();
                for (int i = 0; i < this.capacidadMaxima; i++) {
                    Pedido pedido = this.proveedorDePedidos.obtenerPedidoCocinado();
                    if (pedido == null) {
                        break;
                    }
                    pedidosARepartir.add(pedido);
                }
                // Simula el repartir los pedidos secuencialmente (por domicilio)
                for(Pedido p : pedidosARepartir){
                    TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(1000, 2000)); // Entre 1s - 2s
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
