package main.model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ColaDePedidos implements ProveedorDePedidos {
    private BlockingQueue<Pedido> pedidosPendientes = new LinkedBlockingQueue<>();
    private BlockingQueue<Pedido> pedidosCocinados = new LinkedBlockingQueue<>();
    private BlockingQueue<Pedido> pedidosEntregados = new LinkedBlockingQueue<>(); //?

    @Override
    public Pedido obtenerPedidoPendiente() throws InterruptedException {
        Pedido pedidoPendiente = pedidosPendientes.take();
        pedidoPendiente.setEstadoPedido(EstadoPedido.EN_COCINA);
        return pedidoPendiente; //ver si se puede tomar y una vez tomado cambiar el estado, en este caso, a "cocinando"
    }

    @Override
    public void agregarPedidoPendiente(Pedido pedido) throws InterruptedException {
        pedido.setEstadoPedido(EstadoPedido.PENDIENTE);
        pedidosPendientes.put(pedido);
    }

    @Override
    public Pedido obtenerPedidoCocinado() throws InterruptedException {
        return pedidosCocinados.take();
    }

    @Override
    public void agregarPedidoCocinado(Pedido pedido) throws InterruptedException {
        pedido.setEstadoPedido(EstadoPedido.COCINADO);
        pedidosCocinados.put(pedido);
    }

    @Override
    public Pedido obtenerPedidoEntregado() throws InterruptedException {
        return pedidosEntregados.poll(); // si no hay elementos, devuelve null.
    }

    @Override
    public void agregarPedidoEntregado(Pedido pedido) throws InterruptedException {
        pedidosEntregados.put(pedido);
    }
}
