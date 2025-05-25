package main.model;

import main.model.Pedido;

public interface ProveedorDePedidos {
    Pedido obtenerPedidoPendiente() throws InterruptedException;
    void agregarPedidoPendiente(Pedido pedido) throws InterruptedException;
    Pedido obtenerPedidoCocinado() throws InterruptedException;
    void agregarPedidoCocinado(Pedido pedido) throws InterruptedException;
    Pedido obtenerPedidoEntregado() throws InterruptedException;
    void agregarPedidoEntregado(Pedido pedido) throws InterruptedException;
}
