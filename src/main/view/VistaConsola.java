package main.view;

import main.model.EstadoPedido;
import main.model.Pedido;

public class VistaConsola implements VistaSimulacion, ObservadorPedidos{
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

    @Override
    public void pedidoActualizado(Pedido pedido) {
        EstadoPedido estado = pedido.getEstadoPedido();

        switch (estado) {
            case PENDIENTE:
                notificarNuevoPedido(pedido);
                break;
            case EN_COCINA:
                notificarCocina(pedido);
                break;
            case COCINADO:
                notificarPedidoListo(pedido);
                break;
            case EN_REPARTO:
                notificarReparto(pedido);
                break;
            case ENTREGADO:
                notificarEntrega(pedido);
                break;
            default:
                System.out.println("Estado desconocido para el pedido: " + pedido.getNumOrden());
        }
    }
}
