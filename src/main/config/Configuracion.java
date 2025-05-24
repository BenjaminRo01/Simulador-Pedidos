package main.config;

import java.util.Objects;

public class Configuracion {
    private int cantidadClientes;
    private int cantidadCocineros;
    private int cantidadRepartidor;
    private int maxThreadsCliente;
    private int maxThreadsCocinero;
    private int maxThreadsRepartidor;

    public Configuracion(int cantidadClientes, int cantidadCocineros, int cantidadRepartidor, int maxThreadsCliente, int maxThreadsCocinero, int maxThreadsRepartidor) {
        this.cantidadClientes = cantidadClientes;
        this.cantidadCocineros = cantidadCocineros;
        this.cantidadRepartidor = cantidadRepartidor;
        this.maxThreadsCliente = maxThreadsCliente;
        this.maxThreadsCocinero = maxThreadsCocinero;
        this.maxThreadsRepartidor = maxThreadsRepartidor;
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

    public int getMaxThreadsCliente() {
        return this.maxThreadsCliente;
    }

    public int getMaxThreadsCocinero() {
        return this.maxThreadsCocinero;
    }

    public int getMaxThreadsRepartidor() {
        return this.maxThreadsRepartidor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Configuracion that = (Configuracion) o;
        return cantidadClientes == that.cantidadClientes && cantidadCocineros == that.cantidadCocineros && cantidadRepartidor == that.cantidadRepartidor && maxThreadsCliente == that.maxThreadsCliente && maxThreadsCocinero == that.maxThreadsCocinero && maxThreadsRepartidor == that.maxThreadsRepartidor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cantidadClientes, cantidadCocineros, cantidadRepartidor, maxThreadsCliente, maxThreadsCocinero, maxThreadsRepartidor);
    }
}
