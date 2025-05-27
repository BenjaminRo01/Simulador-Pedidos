package main.app;

import main.config.Configuracion;
import main.controller.GestorSimulacion;
import main.model.ColaDePedidos;
import main.model.ProveedorDePedidos;
import main.view.VistaConsola;
import main.view.VistaSimulacion;

public class Main {
    public static void main(String[] args) {
        Configuracion configuracion = new Configuracion(3,3,3,5,5,5);
        ProveedorDePedidos proveedorDePedidos = new ColaDePedidos();
        VistaSimulacion vistaConsola = new VistaConsola();
        GestorSimulacion gestorSimulacion = new GestorSimulacion(configuracion, proveedorDePedidos, vistaConsola);
        gestorSimulacion.iniciarSimulacion();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            gestorSimulacion.apagarSimulacion();
            System.out.println("Simulación finalizada correctamente.");
        }));
    }
}
