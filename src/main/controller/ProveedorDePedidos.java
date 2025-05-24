package main.controller;

import main.model.Pedido;

public interface ProveedorDePedidos {
    Pedido obtenerPedidoPendiente();
    Pedido obtenerPedidoEnCocina();
    Pedido obtenerPedidoCocinados();
    Pedido obtenerPedidoEnReparto();
}
