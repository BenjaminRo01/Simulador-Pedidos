package main.view;

import main.model.Pedido;

public class VistaConsola implements VistaSimulacion{
    @Override
    public void notificarNuevoPedido(Pedido pedido) {
        System.out.println("[Pedido pendiente]: " + pedido);
    }

    @Override
    public void notificarCocina(Pedido pedido) {
        System.out.println("[Pedido en cocina]: " + pedido);
    }

    @Override
    public void notificarPedidoListo(Pedido pedido) {
        System.out.println("[Pedido cocinado]: " + pedido);
    }

    @Override
    public void notificarReparto(Pedido pedido) {
        System.out.println("[Pedido en reparto]: " + pedido);
    }

    @Override
    public void notificarEntrega(Pedido pedido) {
        System.out.println("[Pedido entregado]: " + pedido);
    }
}
