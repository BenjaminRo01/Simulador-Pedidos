package main.model;

import main.view.ObservadorPedidos;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ColaDePedidos implements ProveedorDePedidos, ObservablePedidos {
    private BlockingQueue<Pedido> pedidosPendientes = new LinkedBlockingQueue<>();
    private BlockingQueue<Pedido> pedidosCocinados = new LinkedBlockingQueue<>();
    private BlockingQueue<Pedido> pedidosEntregados = new LinkedBlockingQueue<>();
    private final List<ObservadorPedidos> observadores = new ArrayList<>();

    @Override
    public void agregarPedidoPendiente(Pedido pedido) throws InterruptedException {
        pedido.setEstadoPedido(EstadoPedido.PENDIENTE);
        pedidosPendientes.put(pedido);
        notificarObservadores(pedido);
    }
    @Override
    public Pedido obtenerPedidoPendiente() throws InterruptedException {
        Pedido pedidoPendiente = pedidosPendientes.take();
        pedidoPendiente.setEstadoPedido(EstadoPedido.EN_COCINA);
        notificarObservadores(pedidoPendiente);
        return pedidoPendiente;
    }
    @Override
    public void agregarPedidoCocinado(Pedido pedido) throws InterruptedException {
        pedido.setEstadoPedido(EstadoPedido.COCINADO);
        pedidosCocinados.put(pedido);
        notificarObservadores(pedido);
    }
    @Override
    public Pedido obtenerPedidoCocinado() throws InterruptedException {
        Pedido pedidoCocinado = pedidosCocinados.poll(100, TimeUnit.MILLISECONDS); // espera hasta 100ms si hay elementos, si no devuelve null.
        if(pedidoCocinado == null){
            return null;
        }
        pedidoCocinado.setEstadoPedido(EstadoPedido.EN_REPARTO);
        notificarObservadores(pedidoCocinado);
        return pedidoCocinado;
    }
    @Override
    public void agregarPedidoEntregado(Pedido pedido) throws InterruptedException {
        pedido.setEstadoPedido(EstadoPedido.ENTREGADO);
        pedidosEntregados.put(pedido);
        notificarObservadores(pedido);
    }
    @Override
    public Pedido obtenerPedidoEntregado() throws InterruptedException { //?
        return pedidosEntregados.take();
    }
    @Override
    public void agregarObservador(ObservadorPedidos o) {
        observadores.add(o);
    }

    @Override
    public void eliminarObservador(ObservadorPedidos o) {
        observadores.remove(o);
    }

    @Override
    public void notificarObservadores(Pedido pedido) {
        for (ObservadorPedidos o : observadores) {
            o.pedidoActualizado(pedido);
        }
    }
}
