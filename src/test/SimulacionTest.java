package test;
import main.model.*;
import main.controller.*;
import main.config.*;
import main.view.*;
import org.junit.jupiter.api.Test;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;

public class SimulacionTest {
    @Test //1. transición de estado (PENDIENTE y luego a EN_COCINA) que ocurre dentro de ColaDePedidos
    void transicionesDeEstados() throws InterruptedException {
        // Preparacion (Assert)
        ColaDePedidos colaDePedidos = new ColaDePedidos();
        Cliente cliente = new Cliente(1, colaDePedidos);
        Pedido pedido = new Pedido(cliente);
        assertEquals(EstadoPedido.PENDIENTE, pedido.getEstadoPedido());

        // Ejecución (Act)
        colaDePedidos.agregarPedidoPendiente(pedido);
        Pedido pedidoProcesado = colaDePedidos.obtenerPedidoPendiente();

        // Verificación (Assert)
        assertEquals(pedido.getNumOrden(), pedidoProcesado.getNumOrden());
        assertEquals(EstadoPedido.EN_COCINA, pedidoProcesado.getEstadoPedido(), "El estado debería cambiar a EN_COCINA al ser obtenido por un cocinero");
    }
    @Test //2. Lo mismo que el anterior pero para todos los estados
    void testTodosLosCambiosDeEstado() throws InterruptedException {
        // Arrange
        ColaDePedidos colaDePedidos = new ColaDePedidos(); // Usamos la implementación real
        Cliente cliente = new Cliente(1, colaDePedidos);
        Pedido pedido = new Pedido(cliente);

        // Act & Assert

        // 1. PENDIENTE -> EN_COCINA
        colaDePedidos.agregarPedidoPendiente(pedido);
        assertEquals(EstadoPedido.PENDIENTE, pedido.getEstadoPedido(), "El pedido debe estar PENDIENTE al ser agregado.");

        Pedido pedidoEnCocina = colaDePedidos.obtenerPedidoPendiente();
        assertEquals(EstadoPedido.EN_COCINA, pedidoEnCocina.getEstadoPedido(), "El pedido debe cambiar a EN_COCINA al ser obtenido.");
        assertEquals(pedido.getNumOrden(), pedidoEnCocina.getNumOrden());

        // 2. EN_COCINA -> COCINADO
        colaDePedidos.agregarPedidoCocinado(pedidoEnCocina);
        assertEquals(EstadoPedido.COCINADO, pedidoEnCocina.getEstadoPedido(), "El pedido debe estar COCINADO al ser agregado a la cola de cocinados.");

        // 3. COCINADO -> EN_REPARTO
        Pedido pedidoEnReparto = colaDePedidos.obtenerPedidoCocinado();
        assertEquals(EstadoPedido.EN_REPARTO, pedidoEnReparto.getEstadoPedido(), "El pedido debe cambiar a EN_REPARTO al ser obtenido por un repartidor.");
        assertEquals(pedido.getNumOrden(), pedidoEnReparto.getNumOrden());

        // 4. EN_REPARTO -> ENTREGADO
        colaDePedidos.agregarPedidoEntregado(pedidoEnReparto);
        assertEquals(EstadoPedido.ENTREGADO, pedidoEnReparto.getEstadoPedido(), "El pedido debe cambiar a ENTREGADO al ser entregado.");
    }
    @Test //3. Prueba de finalizacion de la simulacion, como veremos termina antes del tiempo especificado en timeOutSegundos
    void testSimulacionCompletaFinalizaCorrectamente() throws InterruptedException {
        // Arrange
        int numClientes = 2;
        int pedidosPorCliente = 3;
        int cantidadCocineros = 2;
        int cantidadRepartidor = 2;
        int capacidadMaxPedidosCocinero = 2;
        int capacidadMaxPedidosRepartidor = 2;

        ColaDePedidos proveedor = new ColaDePedidos();
        Configuracion config = new Configuracion(numClientes, cantidadCocineros, cantidadRepartidor, pedidosPorCliente, capacidadMaxPedidosCocinero, capacidadMaxPedidosRepartidor);
        VistaSimulacion vista = new VistaConsola();
        GestorSimulacion gestor = new GestorSimulacion(config, proveedor, vista);

        // Act
        System.out.println("Iniciando simulación de prueba simplificada...");
        gestor.iniciarSimulacion();

        // Damos un tiempo máximo para que la simulación termine por sí sola.
        int timeoutSegundos = 20;
        long tiempoInicio = System.currentTimeMillis();
        while (gestor.isSimulacionActiva() && (System.currentTimeMillis() - tiempoInicio) < timeoutSegundos * 1000) {
            TimeUnit.MILLISECONDS.sleep(100);
        }

        System.out.println("Simulación finalizada o timeout alcanzado.");

        // Assert
        // La única y más importante verificación es que la simulación
        // haya concluido por su propia lógica interna.
        assertFalse(gestor.isSimulacionActiva(), "La simulación debería haberse detenido por sí misma.");
        assertTrue(gestor.isSimulacionFinalizada(), "El gestor debería estar en estado finalizado.");
    }
}
