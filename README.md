# Simulador de Pedidos Online

Este proyecto es un simulador de un sistema de pedidos de comida online, desarrollado como entrega final para la materia de Programación Concurrente. El objetivo principal es aplicar y demostrar conceptos de concurrencia y paralelismo mediante la modelización de un flujo de trabajo donde múltiples actores (clientes, cocineros, repartidores) interactúan de forma simultánea.

## 📽️ Pequeña demostración
![VideoMuestra.gif](src%2Fresources%2FVideoMuestra.gif)

## ⚙️ Funcionalidades Principales

* **Simulación de Flujo de Pedidos:** Modelado completo del ciclo de vida de un pedido:
    * **Pendiente:** Pedidos generados por los clientes.
    * **En Cocina:** Pedidos siendo preparados por los cocineros.
    * **Cocinado:** Pedidos listos para ser tomados por los repartidores.
    * **En Reparto:** Pedidos en camino hacia el cliente.
    * **Entregado:** Pedidos que han llegado a su destino.
* **Actores Concurrentes:**
    * **Clientes:** Generan pedidos de forma concurrente.
    * **Cocineros:** Procesan pedidos pendientes y los "cocinan", cada uno con una capacidad de trabajo concurrente limitada.
    * **Repartidores:** Recogen pedidos cocinados y los entregan, gestionando su propia capacidad de carga.
* **Interfaz Gráfica (GUI):**
    * Desarrollada con JavaFX.
    * Permite configurar los parámetros de la simulación (número de clientes, cocineros, repartidores, y sus respectivas capacidades para procesar pedidos).
    * Visualización en tiempo real del estado de los pedidos y la actividad de los actores.
    * Controles para iniciar y detener la simulación.
* **Mecanismos de Concurrencia Aplicados:**
    * **Hilos (`Runnable`, `ExecutorService`):** Para la ejecución concurrente de los actores.
    * **Patrón Productor-Consumidor:** Implementado mediante `BlockingQueue` para la transferencia segura de pedidos entre etapas.
    * **Semáforos (`Semaphore`):** Para controlar la capacidad de procesamiento concurrente de los cocineros.
    * **Variables Atómicas (`AtomicInteger`):** Para la generación segura de IDs únicos.
    * **Sincronización y Manejo de Interrupciones:** Para un funcionamiento robusto y un cierre ordenado.

## 🛠️ Arquitectura del Proyecto

El proyecto sigue una estructura organizada para separar responsabilidades:

* **`main.app`**: Contiene el punto de entrada principal de la aplicación (`Main.java`).
* **`main.config`**: Define la clase `Configuracion.java` para los parámetros de la simulación.
* **`main.controller`**: Incluye `GestorSimulacion.java`, que dirige la lógica de la simulación.
* **`main.model`**: Contiene las entidades del dominio (`Cliente.java`, `Cocinero.java`, `Repartidor.java`, `Pedido.java`, `ColaDePedidos.java`) y las interfaces que definen sus interacciones.
* **`main.view`**: Contiene las clases para la interfaz gráfica (`VistaGUI.java`, `VistaConsola.java`) y el patrón Observador para las actualizaciones de la UI.

