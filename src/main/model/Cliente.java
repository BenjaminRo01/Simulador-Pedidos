package main.model;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Cliente implements Runnable{
    private static final AtomicInteger contadorCliente = new AtomicInteger();
    private final int numCliente;
    private int cantidadPedidos;
    private final ProveedorDePedidos proveedorDePedidos;

    public Cliente(int cantidadPedidos, ProveedorDePedidos proveedorDePedidos) {
        this.numCliente = contadorCliente.incrementAndGet();
        this.cantidadPedidos = cantidadPedidos;
        this.proveedorDePedidos = proveedorDePedidos;
    }
    @Override
    public void run() {
        for (int i = 0; i < this.cantidadPedidos; i++) {
            Pedido pedido = new Pedido(this);
            System.out.println("Pedido creado: #" + pedido.getNumOrden());

            try{
                proveedorDePedidos.agregarPedidoPendiente(pedido);
                // Simula el tiempo que lleva procesar el pedido en sí (simulando 0.1s - 1s)
                TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(1000, 1500));
            }
            catch (InterruptedException e){
                System.out.println("Cliente " + this.numCliente + " fue interrumpido.");
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    public int getNumCliente() {
        return this.numCliente;
    }
}
