package main.view;

import main.model.Pedido;

public interface VistaSimulacion {
    void notificarNuevoPedido(Pedido pedido);
    void notificarCocina(Pedido pedido);
    void notificarPedidoListo(Pedido pedido);
    void notificarReparto(Pedido pedido);
    void notificarEntrega(Pedido pedido);
}
