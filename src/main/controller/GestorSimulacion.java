package main.controller;


import main.config.Configuracion;
import main.model.Cliente;
import main.model.Cocinero;
import main.model.Repartidor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GestorSimulacion {
    private Configuracion config;
    private List<Cliente> clientes;
    private List<Cocinero> cocineros;
    private List<Repartidor> repartidores;
    private ProveedorDePedidos proveedorDePedidos;
    public GestorSimulacion(Configuracion config, ProveedorDePedidos proveedorDePedidos) {
        this.proveedorDePedidos = proveedorDePedidos;
        this.clientes = new ArrayList<>();
        this.cocineros = new ArrayList<>();
        this.repartidores = new ArrayList<>();
        this.cambiarConfiguracion(config);
    }

    private void cambiarConfiguracion(Configuracion config){
        this.config = config;
        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.submit(()->configurarClientes());
        executor.submit(()->configurarCocineros());
        executor.submit(()->configurarRepatidores());
        executor.shutdown(); // Terminar antes de cerrar.
    }

    private void configurarClientes(){
        int cantidad = this.config.getCantidadClientes();
        int threads = this.config.getMaxThreadsCliente();
        this.clientes.clear();
        for (int i = 0; i < cantidad; i++) {
            this.clientes.add(new Cliente(threads));
        }
    }
    private void configurarCocineros(){
        int cantidad = this.config.getCantidadCocineros();
        int threads = this.config.getMaxThreadsCocinero();
        this.cocineros.clear();
        for (int i = 0; i < cantidad; i++) {
            this.cocineros.add(new Cocinero(threads));
        }
    }
    private void configurarRepatidores(){
        int cantidad = this.config.getCantidadRepartidor();
        int threads = this.config.getMaxThreadsRepartidor();
        this.repartidores.clear();
        for (int i = 0; i < cantidad; i++) {
            this.repartidores.add(new Repartidor(threads));
        }
    }
    public void nuevaConfiguracion(int cantidadClientes, int cantidadCocineros, int cantidadRepartidor, int maxThreadsCliente, int maxThreadsCocinero, int maxThreadsRepartidor){
        Configuracion nuevaConfig = new Configuracion(cantidadClientes,cantidadCocineros,cantidadRepartidor,maxThreadsCliente,maxThreadsCocinero,maxThreadsRepartidor);
        if(!nuevaConfig.equals(this.config)){
            cambiarConfiguracion(nuevaConfig);
        }
    }
}
