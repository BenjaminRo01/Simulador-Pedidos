package main.model;

public class Pedido {
    private static int contadorOrden;
    private final int numOrden;
    private EstadoPedido estadoPedido;
    private Cliente cliente;

    public Pedido(Cliente cliente) {
        this.numOrden = contadorOrden++;
        this.cliente = cliente;
    }
    public int getNumOrden() {
        return this.numOrden;
    }
    public EstadoPedido getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(EstadoPedido estadoPedido) {
        this.estadoPedido = estadoPedido;
    }
    public int getNumCliente(){
        return this.cliente.getNumCliente();
    }
}
