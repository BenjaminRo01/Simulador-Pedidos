package main.controller;


import main.config.Configuracion;
import main.model.Cliente;
import main.model.Cocinero;
import main.model.ProveedorDePedidos;
import main.model.Repartidor;
import main.view.VistaSimulacion;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GestorSimulacion {
    private List<Cliente> clientes;
    private List<Cocinero> cocineros;
    private List<Repartidor> repartidores;
    private ProveedorDePedidos proveedorDePedidos;
    private Configuracion config;
    private VistaSimulacion interfaz;
    public GestorSimulacion(Configuracion config, ProveedorDePedidos proveedorDePedidos, VistaSimulacion interfaz) {
        this.proveedorDePedidos = proveedorDePedidos;
        this.interfaz = interfaz;
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
            this.clientes.add(new Cliente(threads, this.proveedorDePedidos));
        }
    }
    private void configurarCocineros(){
        int cantidad = this.config.getCantidadCocineros();
        int threads = this.config.getMaxThreadsCocinero();
        this.cocineros.clear();
        for (int i = 0; i < cantidad; i++) {
            this.cocineros.add(new Cocinero(threads, this.proveedorDePedidos));
        }
    }
    private void configurarRepatidores(){
        int cantidad = this.config.getCantidadRepartidor();
        int threads = this.config.getMaxThreadsRepartidor();
        this.repartidores.clear();
        for (int i = 0; i < cantidad; i++) {
            this.repartidores.add(new Repartidor(threads, this.proveedorDePedidos));
        }
    }
    public void nuevaConfiguracion(int cantidadClientes, int cantidadCocineros, int cantidadRepartidor, int maxThreadsCliente, int maxThreadsCocinero, int maxThreadsRepartidor){
        Configuracion nuevaConfig = new Configuracion(cantidadClientes,cantidadCocineros,cantidadRepartidor,maxThreadsCliente,maxThreadsCocinero,maxThreadsRepartidor);
        if(!nuevaConfig.equals(this.config)){
            cambiarConfiguracion(nuevaConfig);
        }
    }
}
