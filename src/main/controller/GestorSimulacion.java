package main.controller;


import main.config.Configuracion;
import main.model.*;
import main.view.ObservadorPedidos;
import main.view.VistaSimulacion;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

enum EstadosSimulacion{
    EN_EJECUCION, FINALIZADA
}

public class GestorSimulacion {
    private ExecutorService simulacion;
    private List<Cliente> clientes;
    private List<Cocinero> cocineros;
    private List<Repartidor> repartidores;
    private ProveedorDePedidos proveedorDePedidos;
    private Configuracion config;
    private VistaSimulacion interfaz;
    private EstadosSimulacion estadosSimulacion;
    public GestorSimulacion(Configuracion config, ProveedorDePedidos proveedorDePedidos, VistaSimulacion interfaz) {
        this.proveedorDePedidos = proveedorDePedidos;
        this.interfaz = interfaz;
        this.clientes = new ArrayList<>();
        this.cocineros = new ArrayList<>();
        this.repartidores = new ArrayList<>();
        this.estadosSimulacion = EstadosSimulacion.FINALIZADA;
        if (proveedorDePedidos instanceof ObservablePedidos) {
            ((ObservablePedidos) proveedorDePedidos).agregarObservador((ObservadorPedidos) interfaz);
        }
        this.simulacion = Executors.newCachedThreadPool();
        this.cambiarConfiguracion(config);
    }

    private void cambiarConfiguracion(Configuracion config){
        this.config = config;
        this.configurarClientes();
        this.configurarCocineros();
        this.configurarRepatidores();
    }

    private void configurarClientes(){
        this.clientes.clear();
        int cantidad = this.config.getCantidadClientes();
        int threads = this.config.getCantidadMaxPedidosCliente();
        for (int i = 0; i < cantidad; i++) {
            this.clientes.add(new Cliente(threads, this.proveedorDePedidos));
        }
    }
    private void configurarCocineros(){
        this.cocineros.clear();
        int cantidad = this.config.getCantidadCocineros();
        int threads = this.config.getCapacidadMaxPedidosCocinero();
        for (int i = 0; i < cantidad; i++) {
            this.cocineros.add(new Cocinero(threads, this.proveedorDePedidos));
        }
    }
    private void configurarRepatidores(){
        this.repartidores.clear();
        int cantidad = this.config.getCantidadRepartidor();
        int threads = this.config.getCapacidadMaxPedidosRepartidor();
        for (int i = 0; i < cantidad; i++) {
            this.repartidores.add(new Repartidor(threads, this.proveedorDePedidos));
        }
    }
    public void crearConfiguracion(int cantidadClientes, int cantidadCocineros, int cantidadRepartidor,
                                     int cantidadMaxPedidosCliente, int capacidadMaxPedidosPorCocinero, int capacidadMaxPedidosPorRepartidor){
        Configuracion nuevaConfig = new Configuracion(cantidadClientes,cantidadCocineros,cantidadRepartidor,
                cantidadMaxPedidosCliente,capacidadMaxPedidosPorCocinero,capacidadMaxPedidosPorRepartidor);
        cambiarConfiguracion(nuevaConfig);
    }
    public void iniciarSimulacion(){
        System.out.println("---- Configurando simulación ----");
        System.out.println("---- Iniciando simulación ----");
        this.interfaz.inicializarPersonalEnVista(cocineros, repartidores);
        this.estadosSimulacion = EstadosSimulacion.EN_EJECUCION;
        this.reiniciarPool();
        List<Runnable> actoresSimulacion = new ArrayList<>();
        actoresSimulacion.addAll(this.clientes);
        actoresSimulacion.addAll(this.cocineros);
        actoresSimulacion.addAll(this.repartidores);

        for (Runnable actor : actoresSimulacion){
            this.simulacion.execute(actor);
        }
    }
    private void reiniciarPool() {
        if (this.simulacion == null || this.simulacion.isShutdown() || this.simulacion.isTerminated()) {
            this.simulacion = Executors.newCachedThreadPool();
        }
    }
    public void apagarSimulacion(){
        System.out.println("---- Apagando simulación ----");
        this.estadosSimulacion = EstadosSimulacion.FINALIZADA;
        this.simulacion.shutdown();
        try {
            if (!this.simulacion.awaitTermination(5, TimeUnit.SECONDS)) {
                this.simulacion.shutdownNow();
                this.interfaz.notificarLimpiarTodasLasSecciones();
            }
        } catch (InterruptedException e) {
            this.simulacion.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    public boolean isSimulacionActiva() {
        return this.estadosSimulacion == EstadosSimulacion.EN_EJECUCION;
    }
    public boolean isSimulacionFinalizada() {
        return this.estadosSimulacion == EstadosSimulacion.FINALIZADA;
    }
}
