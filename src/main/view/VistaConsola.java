package main.view;

import main.model.Pedido;

public class VistaConsola implements VistaSimulacion{
    @Override
    public void notificarNuevoPedido(Pedido pedido) {
        System.out.println("[Pedido pendiente]: #" + pedido.getNumOrden());
    }

    @Override
    public void notificarCocina(Pedido pedido) {
        System.out.println("[Pedido en cocina]: #" + pedido.getNumOrden());
    }

    @Override
    public void notificarPedidoListo(Pedido pedido) {
        System.out.println("[Pedido cocinado]: #" + pedido.getNumOrden());
    }

    @Override
    public void notificarReparto(Pedido pedido) {
        System.out.println("[Pedido en reparto]: #" + pedido.getNumOrden());
    }

    @Override
    public void notificarEntrega(Pedido pedido) {
        System.out.println("[Pedido entregado]: #" + pedido.getNumOrden());
    }
}
