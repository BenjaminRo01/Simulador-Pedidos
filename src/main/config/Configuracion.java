package main.config;

import java.util.Objects;

public class Configuracion {
    private int cantidadClientes;
    private int cantidadCocineros;
    private int cantidadRepartidor;
    private int cantidadMaxPedidosCliente;
    private int capacidadMaxPedidosCocinero;
    private int capacidadMaxPedidosRepartidor;

    public Configuracion(int cantidadClientes, int cantidadCocineros, int cantidadRepartidor, int cantidadMaxPedidosCliente, int capacidadMaxPedidosCocinero, int capacidadMaxPedidosRepartidor) {
        this.cantidadClientes = cantidadClientes;
        this.cantidadCocineros = cantidadCocineros;
        this.cantidadRepartidor = cantidadRepartidor;
        this.cantidadMaxPedidosCliente = cantidadMaxPedidosCliente;
        this.capacidadMaxPedidosCocinero = capacidadMaxPedidosCocinero;
        this.capacidadMaxPedidosRepartidor = capacidadMaxPedidosRepartidor;
    }

    public int getCantidadClientes() {
        return this.cantidadClientes;
    }

    public int getCantidadCocineros() {
        return this.cantidadCocineros;
    }

    public int getCantidadRepartidor() {
        return this.cantidadRepartidor;
    }

    public int getCantidadMaxPedidosCliente() {
        return this.cantidadMaxPedidosCliente;
    }

    public int getCapacidadMaxPedidosCocinero() {
        return this.capacidadMaxPedidosCocinero;
    }

    public int getCapacidadMaxPedidosRepartidor() {
        return this.capacidadMaxPedidosRepartidor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Configuracion that = (Configuracion) o;
        return cantidadClientes == that.cantidadClientes && cantidadCocineros == that.cantidadCocineros && cantidadRepartidor == that.cantidadRepartidor && cantidadMaxPedidosCliente == that.cantidadMaxPedidosCliente && capacidadMaxPedidosCocinero == that.capacidadMaxPedidosCocinero && capacidadMaxPedidosRepartidor == that.capacidadMaxPedidosRepartidor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cantidadClientes, cantidadCocineros, cantidadRepartidor, cantidadMaxPedidosCliente, capacidadMaxPedidosCocinero, capacidadMaxPedidosRepartidor);
    }
}
