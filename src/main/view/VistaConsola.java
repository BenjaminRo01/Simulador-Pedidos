package main.view;

import main.model.Cocinero;
import main.model.EstadoPedido;
import main.model.Pedido;
import main.model.Repartidor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VistaConsola implements VistaSimulacion, ObservadorPedidos{
    private Map<Integer, EstadoPedido> estadoActualPedidos = new HashMap<>();
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
    public void inicializarPersonalEnVista(List<Cocinero> cocineros, List<Repartidor> repartidores) {

    }

    @Override
    public void notificarLimpiarTodasLasSecciones() {

    }

    @Override
    public void pedidoActualizado(Pedido pedido) {
        EstadoPedido estado = pedido.getEstadoPedido();
        int numOrden = pedido.getNumOrden();
        EstadoPedido estadoAnterior = estadoActualPedidos.get(numOrden);
        if (estadoAnterior != null && estado.ordinal() <= estadoAnterior.ordinal()) {
            return;
        }
        estadoActualPedidos.put(numOrden, estado);

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
