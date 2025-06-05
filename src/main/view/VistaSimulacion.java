package main.view;

import main.model.Cocinero;
import main.model.Pedido;
import main.model.Repartidor;

import java.util.List;

public interface VistaSimulacion {
    void notificarNuevoPedido(Pedido pedido);
    void notificarCocina(Pedido pedido);
    void notificarPedidoListo(Pedido pedido);
    void notificarReparto(Pedido pedido);
    void notificarEntrega(Pedido pedido);
    void inicializarPersonalEnVista(List<Cocinero> cocineros, List<Repartidor> repartidores);
    void notificarLimpiarTodasLasSecciones();
    void notificarInicioFinalizacion();
    void notificarSimulacionFinalizada();
}
