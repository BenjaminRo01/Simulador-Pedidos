package main.model;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Cliente implements Runnable{
    public static final int TIEMPO_DESDE = 1000;
    public static final int TIEMPO_HASTA = 1500;
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
                // Simula el tiempo que lleva procesar el pedido en sí (simulando 1s - 1.5s)
                TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(TIEMPO_DESDE, TIEMPO_HASTA));
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
