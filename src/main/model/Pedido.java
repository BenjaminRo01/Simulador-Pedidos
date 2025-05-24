package main.model;

public class Pedido {
    private static int contadorOrden;
    private final int numOrden;
    private String[] carrito;
    private int precioTotal;
    private EstadoPedido estadoPedido;

    public Pedido(String[] carrito, int precioTotal) {
        this.carrito = carrito;
        this.precioTotal = precioTotal;
        this.numOrden = contadorOrden++;
    }

    public int getNumOrden() {
        return this.numOrden;
    }

    public String[] getCarrito() {
        return this.carrito;
    }

    public void setCarrito(String[] carrito) {
        this.carrito = carrito;
    }

    public int getPrecioTotal() {
        return this.precioTotal;
    }

    public void setPrecioTotal(int precioTotal) {
        this.precioTotal = precioTotal;
    }

    public EstadoPedido getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(EstadoPedido estadoPedido) {
        this.estadoPedido = estadoPedido;
    }
}
