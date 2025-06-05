package main.view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import main.config.Configuracion;
import main.controller.GestorSimulacion;
import main.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VistaGUI extends Application implements VistaSimulacion, ObservadorPedidos{
    private FlowPane pedidosPendientesContainer;
    private VBox pedidosEnCocinaGrid;
    private FlowPane pedidosCocinadosContainer;
    private VBox pedidosEnRepartoGrid;
    private FlowPane pedidosEntregadosContainer;

    private Map<Integer, HBox> pedidoBoxes = new HashMap<>();
    private Map<Integer, VBox> cocineroBoxes = new HashMap<>();
    private Map<Integer, HBox> cocineroPedidosContainers = new HashMap<>();
    private Map<Integer, Label> cocineroStatusLabels = new HashMap<>();
    private Map<Integer, VBox> repartidorBoxes = new HashMap<>();
    private Map<Integer, HBox> repartidorPedidosContainers = new HashMap<>();
    private Map<Integer, Label> repartidorStatusLabels = new HashMap<>();

    private GestorSimulacion gestorSimulacion;

    private TextField cantClientesField;
    private TextField cantPedidosClienteField;
    private TextField cantCocinerosField;
    private TextField capacidadCocineroField;
    private TextField cantRepartidoresField;
    private TextField capacidadRepartidorField;

    private Button playButton;
    private Button finalizeButton;
    private ProgressIndicator loadingIndicator;
    private ExecutorService backgroundExecutor; // Para ejecutar tareas de fondo para evitar congelamiento de la interfaz.


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Centro de Simulación de Pedidos Online");
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(1000);
        Configuracion configuracion = new Configuracion(0,0,0,0,0,0);
        ProveedorDePedidos proveedorDePedidos = new ColaDePedidos();
        gestorSimulacion = new GestorSimulacion(configuracion, proveedorDePedidos,this);

        backgroundExecutor = Executors.newSingleThreadExecutor();
        primaryStage.setOnCloseRequest(e -> {
            if (gestorSimulacion.isSimulacionActiva()) {
                gestorSimulacion.apagarSimulacion();
            }
            backgroundExecutor.shutdownNow();
            Platform.exit();
        });

        HBox root = new HBox();
        root.setPadding(new Insets(10));
        root.setSpacing(15);

        VBox configPanel = new VBox(20);
        configPanel.setPadding(new Insets(15));
        configPanel.setPrefWidth(280);
        configPanel.setMaxWidth(300);
        configPanel.setStyle("-fx-background-color: #34495e; -fx-background-radius: 8;");

        Label configTitle = new Label("Configuración de Simulación");
        configTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        configTitle.setStyle("-fx-text-fill: white;");
        configPanel.getChildren().add(configTitle);

        cantClientesField = createNumericTextField("3");
        cantPedidosClienteField = createNumericTextField("3");
        cantCocinerosField = createNumericTextField("3");
        capacidadCocineroField = createNumericTextField("3");
        cantRepartidoresField = createNumericTextField("3");
        capacidadRepartidorField = createNumericTextField("3");

        configPanel.getChildren().add(createConfigSection("Clientes", "Cantidad clientes:", cantClientesField, "Cantidad pedidos:", cantPedidosClienteField));
        configPanel.getChildren().add(createConfigSection("Cocineros", "Cantidad cocineros:", cantCocinerosField, "Capacidad pedidos:", capacidadCocineroField));
        configPanel.getChildren().add(createConfigSection("Repartidores", "Cantidad repartidores:", cantRepartidoresField, "Capacidad pedidos:", capacidadRepartidorField));

        VBox controlButtons = new VBox(10);
        controlButtons.setAlignment(Pos.CENTER);
        controlButtons.setPadding(new Insets(10, 0, 0, 0));
        playButton = new Button("▶ Iniciar Simulación");
        finalizeButton = new Button("■ Finalizar");

        playButton.setMaxWidth(Double.MAX_VALUE);
        finalizeButton.setMaxWidth(Double.MAX_VALUE);
        playButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15;");
        finalizeButton.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15;");

        loadingIndicator = new ProgressIndicator();
        loadingIndicator.setMaxSize(30, 30);
        loadingIndicator.setVisible(false);
        VBox.setMargin(loadingIndicator, new Insets(10, 0, 0, 0));

        controlButtons.getChildren().addAll(playButton, finalizeButton, loadingIndicator);
        configPanel.getChildren().add(controlButtons);

        HBox.setHgrow(configPanel, Priority.NEVER);
        root.getChildren().add(configPanel);

        GridPane pedidosGrid = new GridPane();
        pedidosGrid.setHgap(15);
        pedidosGrid.setVgap(15);
        pedidosGrid.setPadding(new Insets(0));

        // --- Ajuste de columnas del GridPane ---
        // Asignar el 50% del espacio horizontal disponible a cada columna
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        col1.setHgrow(Priority.ALWAYS); // Permite que se estiren si hay espacio extra
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        col2.setHgrow(Priority.ALWAYS); // Permite que se estiren si hay espacio extra
        pedidosGrid.getColumnConstraints().addAll(col1, col2);

        pedidosPendientesContainer = new FlowPane(5,5);
        pedidosEnCocinaGrid = new VBox(10); // Contenedor para los VBox de cocineros
        pedidosCocinadosContainer = new FlowPane(5,5);
        pedidosEnRepartoGrid = new VBox(10); // Contenedor para los VBox de repartidores
        pedidosEntregadosContainer = new FlowPane(5,5);

        // --- Uso de createDisplaySection con alturas fijas/preferidas ---
        // Pasamos un heightPreferido para que la sección tenga un tamaño fijo
        pedidosGrid.add(createDisplaySection("PEDIDOS PENDIENTES", pedidosPendientesContainer, Color.LIGHTBLUE, 180), 0, 0);
        pedidosGrid.add(createDisplaySection("PEDIDOS EN COCINA", pedidosEnCocinaGrid, Color.LIGHTGREEN, 180), 1, 0);
        pedidosGrid.add(createDisplaySection("PEDIDOS COCINADOS", pedidosCocinadosContainer, Color.LIGHTYELLOW, 180), 0, 1);
        pedidosGrid.add(createDisplaySection("PEDIDOS EN REPARTO", pedidosEnRepartoGrid, Color.LIGHTCORAL, 180), 1, 1);
        pedidosGrid.add(createDisplaySection("PEDIDOS ENTREGADOS", pedidosEntregadosContainer, Color.LIGHTGRAY, 100), 0, 2, 2, 1); // Más corto para los entregados

        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(35); // 35% del espacio vertical para la primera fila
        row1.setVgrow(Priority.ALWAYS);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(35); // 35% del espacio vertical para la segunda fila
        row2.setVgrow(Priority.ALWAYS);
        RowConstraints row3 = new RowConstraints();
        row3.setPercentHeight(30); // 30% del espacio vertical para la tercera fila
        row3.setVgrow(Priority.ALWAYS);
        pedidosGrid.getRowConstraints().addAll(row1, row2, row3);

        HBox.setHgrow(pedidosGrid, Priority.ALWAYS); // Permite que la grilla ocupe el espacio horizontal restante
        root.getChildren().add(pedidosGrid);

        Scene scene = new Scene(root, 1280, 720); // Tamaño inicial de la ventana
        primaryStage.setScene(scene);
        primaryStage.show();

        addNoPedidosLabelIfEmpty(pedidosPendientesContainer, "No hay pedidos pendientes.");
        addNoPedidosLabelIfEmpty(pedidosCocinadosContainer, "No hay pedidos cocinados listos.");
        addNoPedidosLabelIfEmpty(pedidosEntregadosContainer, "No hay historial de pedidos entregados.");

        playButton.setOnAction(event -> handleIniciarSimulacion());
        finalizeButton.setOnAction(event -> handleFinalizarSimulacion());

        finalizeButton.setDisable(true);
    }

    private void handleIniciarSimulacion() {
        if (!gestorSimulacion.isSimulacionActiva()) {
            int cantidadClientes = parseTextFieldValue(cantClientesField, 1);
            int cantidadPedidosCliente = parseTextFieldValue(cantPedidosClienteField, 1);
            int cantidadCocineros = parseTextFieldValue(cantCocinerosField, 1);
            int capacidadCocineros = parseTextFieldValue(capacidadCocineroField, 1);
            int cantidadRepartidores = parseTextFieldValue(cantRepartidoresField, 1);
            int capacidadRepartidores = parseTextFieldValue(capacidadRepartidorField, 1);

            // 1. El gestor se configura (y crea sus objetos Cocinero/Repartidor)
            gestorSimulacion.crearConfiguracion(
                    cantidadClientes, cantidadCocineros, cantidadRepartidores,
                    cantidadPedidosCliente, capacidadCocineros, capacidadRepartidores
            );

            setConfigurationFieldsEditable(false);
            playButton.setDisable(true);
            finalizeButton.setDisable(false);

            // 2. Antes de iniciar la simulación real, la vista se resetea y el personal se inicializa
            // Esta llamada ahora también manejará la reconstrucción de las cajas de cocineros/repartidores
            notificarLimpiarTodasLasSecciones(); // Limpia todas las secciones y reestablece los mensajes por defecto

            // 3. El gestor inicia la simulación, que incluirá llamar a inicializarPersonalEnVista
            gestorSimulacion.iniciarSimulacion();
        }
    }

    private void handleFinalizarSimulacion() {
        if (gestorSimulacion.isSimulacionActiva()) {
            loadingIndicator.setVisible(true);
            finalizeButton.setDisable(true);
            backgroundExecutor.execute(() -> {
                gestorSimulacion.apagarSimulacion();
            });
        }
    }

    private TextField createNumericTextField(String defaultValue) {
        TextField textField = new TextField(defaultValue);
        textField.setPrefWidth(60);
        textField.setMaxWidth(60);

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                textField.setText("");
                return;
            }
            if (!newValue.matches("\\d*")) {
                textField.setText(oldValue);
                return;
            }
            try {
                int value = Integer.parseInt(newValue);
                if (value < 1) {
                    textField.setText("1");
                } else if (value > 5) {
                    textField.setText("5");
                }
            } catch (NumberFormatException e) {
                textField.setText(oldValue);
            }
        });
        return textField;
    }

    private VBox createConfigSection(String title, String label1Text, TextField field1, String label2Text, TextField field2) {
        VBox section = new VBox(5);
        section.setStyle("-fx-background-color: #3f5b72; -fx-background-radius: 5; -fx-padding: 10;");
        Label sectionTitle = new Label(title);
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        sectionTitle.setStyle("-fx-text-fill: white;");
        section.getChildren().add(sectionTitle);

        section.getChildren().add(createLabeledFieldRow(label1Text, field1));
        section.getChildren().add(createLabeledFieldRow(label2Text, field2));

        return section;
    }

    private HBox createLabeledFieldRow(String labelText, TextField textField) {
        HBox fieldRow = new HBox(5);
        fieldRow.setAlignment(Pos.CENTER_LEFT);
        Label label = new Label(labelText);
        label.setStyle("-fx-text-fill: #ecf0f1;");
        fieldRow.getChildren().addAll(label, textField);
        return fieldRow;
    }

    private VBox createDisplaySection(String title, Pane contentContainer, Color backgroundColor, double preferredHeight) {
        VBox section = new VBox(5);
        section.setPadding(new Insets(10));
        section.setStyle("-fx-background-color: " + toHex(backgroundColor) + "; -fx-border-color: #ccc; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-radius: 5;");
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        titleLabel.setStyle("-fx-padding: 0 0 5 0;");
        section.getChildren().add(titleLabel);

        ScrollPane scrollPane = new ScrollPane(contentContainer);
        scrollPane.setFitToWidth(true); // Crucial para que FlowPane envuelva
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // No necesitamos scroll horizontal para FlowPane
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Scroll vertical si el contenido excede la altura
        scrollPane.setStyle("-fx-background-color: transparent;");
        scrollPane.setPrefHeight(preferredHeight);
        scrollPane.setMinHeight(preferredHeight);

        section.getChildren().add(scrollPane);

        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        VBox.setVgrow(section, Priority.ALWAYS);
        return section;
    }

    private void setConfigurationFieldsEditable(boolean editable) {
        cantClientesField.setDisable(!editable);
        cantPedidosClienteField.setDisable(!editable);
        cantCocinerosField.setDisable(!editable);
        capacidadCocineroField.setDisable(!editable);
        cantRepartidoresField.setDisable(!editable);
        capacidadRepartidorField.setDisable(!editable);
    }

    private int parseTextFieldValue(TextField field, int defaultValue) {
        try {
            return Integer.parseInt(field.getText());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    @Override
    public void pedidoActualizado(Pedido pedido) {
        if(this.gestorSimulacion.isSimulacionFinalizada()){
            return;
        }
        Platform.runLater(() -> {
            switch (pedido.getEstadoPedido()) {
                case PENDIENTE:
                    notificarNuevoPedido(pedido);
                    break;
                case EN_COCINA:
                    notificarCocina(pedido);
                    break;
                case COCINADO:
                    notificarPedidoListo(pedido);
                    break;
                case EN_REPARTO:
                    notificarReparto(pedido);
                    break;
                case ENTREGADO:
                    notificarEntrega(pedido);
                    break;
            }
        });
    }

    @Override
    public void notificarNuevoPedido(Pedido pedido) {
        HBox pedidoBox = getOrCreatePedidoBox(pedido);
        removePedidoBoxFromAllContainers(pedido);
        pedidosPendientesContainer.getChildren().add(pedidoBox);
        removeNoPedidosLabel(pedidosPendientesContainer, "No hay pedidos pendientes.");
    }

    @Override
    public void notificarCocina(Pedido pedido) {
        removePedidoBoxFromContainer(pedido, pedidosPendientesContainer);
        addNoPedidosLabelIfEmpty(pedidosPendientesContainer, "No hay pedidos pendientes.");

        HBox pedidoBox = getOrCreatePedidoBox(pedido);

        if (pedido.getCocineroAsignado() == null) {
            System.err.println("Error: Pedido #" + pedido.getNumOrden() + " en cocina sin cocinero asignado.");
            return;
        }

        Cocinero cocinero = pedido.getCocineroAsignado();
        HBox cocineroPedidos = getOrCreateCocineroPedidosContainer(cocinero);

        if (cocineroStatusLabels.containsKey(cocinero.getNumCocinero())) {
            cocineroPedidos.getChildren().remove(cocineroStatusLabels.get(cocinero.getNumCocinero()));
            cocineroStatusLabels.remove(cocinero.getNumCocinero());
        }

        if (!cocineroPedidos.getChildren().contains(pedidoBox)) {
            cocineroPedidos.getChildren().add(pedidoBox);
        }
    }

    @Override
    public void notificarPedidoListo(Pedido pedido) {
        Cocinero cocinero = pedido.getCocineroAsignado();
        if (cocinero != null) {
            HBox cocineroPedidos = cocineroPedidosContainers.get(cocinero.getNumCocinero());
            if (cocineroPedidos != null) {
                cocineroPedidos.getChildren().remove(pedidoBoxes.get(pedido.getNumOrden()));
            }
        }

        HBox pedidoBox = getOrCreatePedidoBox(pedido);
        pedidosCocinadosContainer.getChildren().add(pedidoBox);
        removeNoPedidosLabel(pedidosCocinadosContainer, "No hay pedidos cocinados listos.");
    }

    @Override
    public void notificarReparto(Pedido pedido) {
        removePedidoBoxFromContainer(pedido, pedidosCocinadosContainer);
        addNoPedidosLabelIfEmpty(pedidosCocinadosContainer, "No hay pedidos cocinados listos.");

        HBox pedidoBox = getOrCreatePedidoBox(pedido);

        if (pedido.getRepartidorAsignado() == null) {
            System.err.println("Error: Pedido #" + pedido.getNumOrden() + " en reparto sin repartidor asignado.");
            return;
        }

        Repartidor repartidor = pedido.getRepartidorAsignado();
        HBox repartidorPedidos = getOrCreateRepartidorPedidosContainer(repartidor);

        if (repartidorStatusLabels.containsKey(repartidor.getNumRepartidor())) {
            repartidorPedidos.getChildren().remove(repartidorStatusLabels.get(repartidor.getNumRepartidor()));
            repartidorStatusLabels.remove(repartidor.getNumRepartidor());
        }

        if (!repartidorPedidos.getChildren().contains(pedidoBox)) {
            repartidorPedidos.getChildren().add(pedidoBox);
        }
    }

    @Override
    public void notificarEntrega(Pedido pedido) {
        Repartidor repartidor = pedido.getRepartidorAsignado();
        if (repartidor != null) {
            HBox repartidorPedidos = repartidorPedidosContainers.get(repartidor.getNumRepartidor());
            if (repartidorPedidos != null) {
                repartidorPedidos.getChildren().remove(pedidoBoxes.get(pedido.getNumOrden()));
            }
        }

        HBox pedidoBox = getOrCreatePedidoBox(pedido);
        ((Label) pedidoBox.getChildren().get(0)).setText("Orden #" + pedido.getNumOrden() + " (Cliente #" + pedido.getNumCliente() + ") - Entregado");

        pedidosEntregadosContainer.getChildren().add(pedidoBox);
        removeNoPedidosLabel(pedidosEntregadosContainer, "No hay historial de pedidos entregados.");

        pedidoBoxes.remove(pedido.getNumOrden());
    }

    public void inicializarPersonalEnVista(List<Cocinero> cocineros, List<Repartidor> repartidores) {
        Platform.runLater(() -> {
            // Limpiar los contenedores principales de cocineros y repartidores (la "grilla")
            pedidosEnCocinaGrid.getChildren().clear();
            pedidosEnRepartoGrid.getChildren().clear();

            // Limpiar todos los mapas de referencias a los componentes de UI del personal
            cocineroBoxes.clear();
            cocineroPedidosContainers.clear();
            cocineroStatusLabels.clear();
            repartidorBoxes.clear();
            repartidorPedidosContainers.clear();
            repartidorStatusLabels.clear();

            // Reconstruir las cajas de cocineros
            for (Cocinero cocinero : cocineros) {
                HBox cocineroPedidos = getOrCreateCocineroPedidosContainer(cocinero); // Esto crea el HBox para sus pedidos
                VBox cocineroBox = getOrCreateCocineroBox(cocinero, cocineroPedidos); // Esto crea la VBox del cocinero
                pedidosEnCocinaGrid.getChildren().add(cocineroBox); // Añadir la VBox del cocinero a la grilla principal
            }

            // Reconstruir las cajas de repartidores
            for (Repartidor repartidor : repartidores) {
                HBox repartidorPedidos = getOrCreateRepartidorPedidosContainer(repartidor);
                VBox repartidorBox = getOrCreateRepartidorBox(repartidor, repartidorPedidos);


                pedidosEnRepartoGrid.getChildren().add(repartidorBox);
            }
        });
    }

    @Override
    public void notificarLimpiarTodasLasSecciones() {
        Platform.runLater(() -> {
            pedidosPendientesContainer.getChildren().clear();
            pedidosCocinadosContainer.getChildren().clear();
            pedidosEntregadosContainer.getChildren().clear();

            addNoPedidosLabelIfEmpty(pedidosPendientesContainer, "No hay pedidos pendientes.");
            addNoPedidosLabelIfEmpty(pedidosCocinadosContainer, "No hay pedidos cocinados listos.");
            addNoPedidosLabelIfEmpty(pedidosEntregadosContainer, "No hay historial de pedidos entregados.");

            // Limpiar los pedidos de los contenedores de cocineros/repartidores y reestablecer sus mensajes
            for (Map.Entry<Integer, HBox> entry : cocineroPedidosContainers.entrySet()) {
                entry.getValue().getChildren().clear();
            }

            for (Map.Entry<Integer, HBox> entry : repartidorPedidosContainers.entrySet()) {
                entry.getValue().getChildren().clear();
            }

            pedidoBoxes.clear();
            loadingIndicator.setVisible(false);
        });
    }

    @Override
    public void notificarInicioFinalizacion() {
        Platform.runLater(() -> {
            loadingIndicator.setVisible(true);
            finalizeButton.setDisable(true);
        });
    }

    @Override
    public void notificarSimulacionFinalizada() {
        // Se asegura que la actualizacion grafica se ejecuten en el hilo de app JavaFx
        Platform.runLater(() -> {
            loadingIndicator.setVisible(false);
            finalizeButton.setDisable(true);
            playButton.setDisable(false);
            setConfigurationFieldsEditable(true);
        });
    }

    private HBox getOrCreatePedidoBox(Pedido pedido) {
        String pedidoInfo = "Orden #" + pedido.getNumOrden() + " (Cliente #" + pedido.getNumCliente() + ")";
        HBox box = pedidoBoxes.get(pedido.getNumOrden());
        if (box == null) {
            box = new HBox(5);
            box.setAlignment(Pos.CENTER_LEFT);
            box.setPadding(new Insets(3, 5, 3, 5));
            box.setStyle("-fx-background-color: #ffffff; -fx-border-color: #ddd; -fx-border-width: 1; -fx-border-radius: 3;");
            box.setPrefWidth(120);
            box.setMaxWidth(120);
            Label label = new Label(pedidoInfo);
            label.setWrapText(true);
            box.getChildren().add(label);
            HBox.setHgrow(box, Priority.NEVER);
            pedidoBoxes.put(pedido.getNumOrden(), box);
        } else {
            ((Label) box.getChildren().get(0)).setText(pedidoInfo);
        }
        return box;
    }

    private HBox getOrCreateCocineroPedidosContainer(Cocinero cocinero) {
        HBox container = cocineroPedidosContainers.get(cocinero.getNumCocinero());
        if (container == null) {
            container = new HBox(5);
            container.setPadding(new Insets(3, 0, 0, 0));
            container.setSpacing(5);
            container.setAlignment(Pos.CENTER_LEFT);
            // Si el contenido excede el HBox, se desplaza (scroll)
            container.setPrefHeight(40); // Altura preferida para esta línea de pedidos
            container.setMinHeight(40); // Altura mínima
            HBox.setHgrow(container, Priority.ALWAYS);
            cocineroPedidosContainers.put(cocinero.getNumCocinero(), container);
        }
        return container;
    }

    private VBox getOrCreateCocineroBox(Cocinero cocinero, HBox cocineroPedidosContainer) {
        VBox cocineroBox = cocineroBoxes.get(cocinero.getNumCocinero());
        if (cocineroBox == null) {
            cocineroBox = new VBox(5);
            cocineroBox.setPadding(new Insets(8));
            cocineroBox.setStyle("-fx-background-color: #e0ffe0; -fx-border-color: #90ee90; -fx-border-width: 1; -fx-border-radius: 5;");
            cocineroBox.setPrefHeight(80); // Altura preferida para cada caja de cocinero
            cocineroBox.setMinHeight(80); // Altura mínima
            Label nameLabel = new Label("Cocinero: " + cocinero.getNumCocinero());
            nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            cocineroBox.getChildren().addAll(nameLabel, cocineroPedidosContainer);
            VBox.setVgrow(cocineroBox, Priority.NEVER); // No permitir que crezca verticalmente en el grid de cocineros
            cocineroBoxes.put(cocinero.getNumCocinero(), cocineroBox);
        }
        return cocineroBox;
    }

    private HBox getOrCreateRepartidorPedidosContainer(Repartidor repartidor) {
        HBox container = repartidorPedidosContainers.get(repartidor.getNumRepartidor());
        if (container == null) {
            container = new HBox(5);
            container.setPadding(new Insets(3, 0, 0, 0));
            container.setSpacing(5);
            container.setAlignment(Pos.CENTER_LEFT);
            container.setPrefHeight(40); // Altura preferida para esta línea de pedidos
            container.setMinHeight(40); // Altura mínima
            HBox.setHgrow(container, Priority.ALWAYS);
            repartidorPedidosContainers.put(repartidor.getNumRepartidor(), container);
        }
        return container;
    }

    private VBox getOrCreateRepartidorBox(Repartidor repartidor, HBox repartidorPedidosContainer) {
        VBox repartidorBox = repartidorBoxes.get(repartidor.getNumRepartidor());
        if (repartidorBox == null) {
            repartidorBox = new VBox(5);
            repartidorBox.setPadding(new Insets(8));
            repartidorBox.setStyle("-fx-background-color: #ffe0e0; -fx-border-color: #ffb6c1; -fx-border-width: 1; -fx-border-radius: 5;");
            repartidorBox.setPrefHeight(80); // Altura preferida para cada caja de repartidor
            repartidorBox.setMinHeight(80); // Altura mínima
            Label nameLabel = new Label("Repartidor: " + repartidor.getNumRepartidor());
            nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            repartidorBox.getChildren().addAll(nameLabel, repartidorPedidosContainer);
            VBox.setVgrow(repartidorBox, Priority.NEVER); // No permitir que crezca verticalmente en el grid de repartidores
            repartidorBoxes.put(repartidor.getNumRepartidor(), repartidorBox);
        }
        return repartidorBox;
    }

    private void removePedidoBoxFromContainer(Pedido pedido, Pane container) {
        HBox pedidoBox = pedidoBoxes.get(pedido.getNumOrden());
        if (pedidoBox != null) {
            container.getChildren().remove(pedidoBox);
        }
    }

    private void removePedidoBoxFromAllContainers(Pedido pedido) {
        HBox pedidoBox = pedidoBoxes.get(pedido.getNumOrden());
        if (pedidoBox == null) return;

        pedidosPendientesContainer.getChildren().remove(pedidoBox);
        pedidosCocinadosContainer.getChildren().remove(pedidoBox);
        pedidosEntregadosContainer.getChildren().remove(pedidoBox);

        for (HBox cocineroPedidos : cocineroPedidosContainers.values()) {
            cocineroPedidos.getChildren().remove(pedidoBox);
        }
        for (HBox repartidorPedidos : repartidorPedidosContainers.values()) {
            repartidorPedidos.getChildren().remove(pedidoBox);
        }
    }

    private void addNoPedidosLabelIfEmpty(Pane container, String message) {
        if (container.getChildren().isEmpty() || (container.getChildren().size() == 1 && container.getChildren().get(0) instanceof Label && ((Label)container.getChildren().get(0)).getText().equals(message))) {
            container.getChildren().clear();
            container.getChildren().add(new Label(message));
        }
    }

    private void removeNoPedidosLabel(Pane container, String message) {
        if (!container.getChildren().isEmpty() && container.getChildren().get(0) instanceof Label) {
            Label firstLabel = (Label) container.getChildren().get(0);
            if (firstLabel.getText().equals(message)) {
                container.getChildren().remove(0);
            }
        }
    }

    private String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
