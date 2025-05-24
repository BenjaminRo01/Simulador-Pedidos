package main.controller;

import main.model.Pedido;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProveedorDesdeCola implements ProveedorDePedidos{
    private BlockingQueue<Pedido> pedidosPendientes = new LinkedBlockingQueue<>();
    private BlockingQueue<Pedido> pedidosEnCocina = new LinkedBlockingQueue<>();
    private BlockingQueue<Pedido> pedidosCocinados = new LinkedBlockingQueue<>();
    private BlockingQueue<Pedido> pedidosEnReparto = new LinkedBlockingQueue<>();
    private BlockingQueue<Pedido> pedidosEntregados = new LinkedBlockingQueue<>();


    @Override
    public Pedido obtenerPedidoPendiente() {
        return null;
    }

    @Override
    public Pedido obtenerPedidoEnCocina() {
        return null;
    }

    @Override
    public Pedido obtenerPedidoCocinados() {
        return null;
    }

    @Override
    public Pedido obtenerPedidoEnReparto() {
        return null;
    }
}
