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
import java.util.concurrent.atomic.AtomicInteger;

enum EstadosSimulacion{
    EN_EJECUCION, FINALIZADA
}

public class GestorSimulacion implements ObservadorPedidos{
    public static final int TIEMPO_ESPERA_FINALIZACION = 5;
    private ExecutorService simulacion;
    private List<Cliente> clientes;
    private List<Cocinero> cocineros;
    private List<Repartidor> repartidores;
    private ProveedorDePedidos proveedorDePedidos;
    private Configuracion config;
    private VistaSimulacion interfaz;
    private EstadosSimulacion estadosSimulacion;
    private int totalPedidosEsperados;
    private AtomicInteger pedidosEntregadosContador;

    public GestorSimulacion(Configuracion config, ProveedorDePedidos proveedorDePedidos, VistaSimulacion interfaz) {
        this.proveedorDePedidos = proveedorDePedidos;
        this.interfaz = interfaz;
        this.clientes = new ArrayList<>();
        this.cocineros = new ArrayList<>();
        this.repartidores = new ArrayList<>();
        this.estadosSimulacion = EstadosSimulacion.FINALIZADA;
        if (proveedorDePedidos instanceof ObservablePedidos) {
            ((ObservablePedidos) proveedorDePedidos).agregarObservador((ObservadorPedidos) interfaz);
            ((ObservablePedidos) proveedorDePedidos).agregarObservador(this);
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
        int cantClientes = this.config.getCantidadClientes();
        int cantPedidos = this.config.getCantidadMaxPedidosCliente();
        for (int i = 0; i < cantClientes; i++) {
            this.clientes.add(new Cliente(cantPedidos, this.proveedorDePedidos));
        }
    }
    private void configurarCocineros(){
        this.cocineros.clear();
        int cantCocineros = this.config.getCantidadCocineros();
        int cantPedidos = this.config.getCapacidadMaxPedidosCocinero();
        for (int i = 0; i < cantCocineros; i++) {
            this.cocineros.add(new Cocinero(cantPedidos, this.proveedorDePedidos));
        }
    }
    private void configurarRepatidores(){
        this.repartidores.clear();
        int cantRepartidores = this.config.getCantidadRepartidor();
        int cantPedidos = this.config.getCapacidadMaxPedidosRepartidor();
        for (int i = 0; i < cantRepartidores; i++) {
            this.repartidores.add(new Repartidor(cantPedidos, this.proveedorDePedidos));
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

        //Se inicializan los contadores en base a la configuracion del cliente
        this.totalPedidosEsperados = this.config.getCantidadClientes() * this.config.getCantidadMaxPedidosCliente();
        this.pedidosEntregadosContador = new AtomicInteger(0);

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
        if(isSimulacionFinalizada()){
            return; //Para evitar multiples llamadas cuando ya se esta apagando.
        }
        System.out.println("---- Apagando simulación ----");
        this.estadosSimulacion = EstadosSimulacion.FINALIZADA;
        this.simulacion.shutdown();
        try {
            if (!this.simulacion.awaitTermination(TIEMPO_ESPERA_FINALIZACION, TimeUnit.SECONDS)) {
                this.simulacion.shutdownNow();
            }
        } catch (InterruptedException e) {
            this.simulacion.shutdownNow();
            Thread.currentThread().interrupt();
        }
        finally {
            this.interfaz.notificarLimpiarTodasLasSecciones();
            this.interfaz.notificarSimulacionFinalizada();
        }
    }
    public boolean isSimulacionActiva() {
        return this.estadosSimulacion == EstadosSimulacion.EN_EJECUCION;
    }
    public boolean isSimulacionFinalizada() {
        return this.estadosSimulacion == EstadosSimulacion.FINALIZADA;
    }

    @Override
    public void pedidoActualizado(Pedido pedido) {
        if(pedido.getEstadoPedido() == EstadoPedido.ENTREGADO && isSimulacionActiva()){
            int entregados = pedidosEntregadosContador.incrementAndGet();
            if (entregados == this.totalPedidosEsperados){
                this.interfaz.notificarInicioFinalizacion();
                new Thread(this::apagarSimulacion).start(); // Se llama de forma asincronica para no bloquear el hilo notificador
            }
        }
    }
}
