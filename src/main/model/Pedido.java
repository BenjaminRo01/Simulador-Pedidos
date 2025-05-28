package main.model;

import java.util.concurrent.atomic.AtomicInteger;

public class Pedido {
    private static final AtomicInteger contadorOrden = new AtomicInteger();
    private final int numOrden;
    private EstadoPedido estadoPedido;
    private Cliente cliente;

    private Cocinero cocineroAsignado;
    private Repartidor repartidorAsignado;

    public Pedido(Cliente cliente) {
        this.numOrden = contadorOrden.incrementAndGet();
        this.cliente = cliente;
        this.estadoPedido = EstadoPedido.PENDIENTE;
    }
    public int getNumOrden() {
        return this.numOrden;
    }
    public EstadoPedido getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(EstadoPedido estadoPedido) {
        this.estadoPedido = estadoPedido;
    }
    public int getNumCliente(){
        return this.cliente.getNumCliente();
    }

    public Cocinero getCocineroAsignado() {
        return cocineroAsignado;
    }

    public void setCocineroAsignado(Cocinero cocineroAsignado) {
        this.cocineroAsignado = cocineroAsignado;
    }

    public Repartidor getRepartidorAsignado() {
        return repartidorAsignado;
    }

    public void setRepartidorAsignado(Repartidor repartidorAsignado) {
        this.repartidorAsignado = repartidorAsignado;
    }
}
