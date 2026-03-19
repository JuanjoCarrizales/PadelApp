/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mypadelapp.padelapp;

import com.mypadelapp.padelapp.modelo.PartidoPadel;
import com.mypadelapp.padelapp.modelo.DatabaseManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    
    //Página 1: 
    //Modelo del partido:
    private PartidoPadel partido;
    
    //Labels para ver la información (parte de arriba):
    private Label labelSets;
    private Label labelJuegos;
    
    //Botones-Marcador:
    private Button botonMarcadorPareja1;
    private Button botonMarcadorPareja2;
    
    //Boton de deshacer:
    private Button botonDeshacer;

    //Página 2: Cronómetro:
    private Label labelCronometro;
    private Label labelKcal;
    private Button botonCronometro;
    private Timeline cronometro;
    private int totalSegundos = 0;
    private boolean cronometroActivo = false;
    private static final double KCAL_POR_SEGUNDO = 300.0/3600.0;
    
    //Navegacón entre las páginas:
    private StackPane contenedor;
    private VBox pagina1;
    private VBox pagina2;
    private VBox pagina3;
    private VBox lPrincipal;
    private Circle punto1;
    private Circle punto2;
    private Circle punto3;
    private int paginaActual = 0;
    private double mousePressX;
    
    //Página 3: Estadísticas:
    private Label labelTotalPartidos;
    private Label labelVictorias;
    private Label labelDerrotas;
    private Label labelDuracionMedia;
     
    @Override
    public void start(Stage primaryStage){
        //Creamos el modelo partido:
        partido = new PartidoPadel();
        primaryStage.setTitle("Marcador de Pádel");
        
        //Creación de las páginas:
        pagina1 = crearPagina1();
        pagina2 = crearPagina2();
        pagina3 = crearPagina3();
        
        //Gestos al deslizar entre páginas:
        gestos(pagina1);
        gestos(pagina2);
        gestos(pagina3);
        
        //Contenedor que superpone las 2 páginas:
        contenedor = new StackPane();
        contenedor.getChildren().addAll(pagina3, pagina2, pagina1);
        botonResponsivo(botonMarcadorPareja1);
        botonResponsivo(botonMarcadorPareja2);
        
        //Navegación por "puntos":
        punto1 = new Circle(5);
        punto1.getStyleClass().add("punto-navegacion-dinamico");
        
        punto2 = new Circle(5);
        punto2.getStyleClass().add("punto-navegacion");
        
        punto3 = new Circle(5);
        punto3.getStyleClass().add("punto-navegacion");
        
        HBox puntos = new HBox(8);
        puntos.setAlignment(Pos.CENTER);
        puntos.getChildren().addAll(punto1, punto2, punto3);
        gestos(puntos);
        
        //Layout principal:
        lPrincipal = new VBox(10);
        lPrincipal.setAlignment(Pos.CENTER);
        lPrincipal.getStyleClass().add("fondo-principal");
        lPrincipal.getChildren().addAll(contenedor, puntos);
        
        Scene scene = new Scene(lPrincipal, 480, 560);
        //Escalado global (con respecto ASPECT_RATIO):
        actualizarEscalado(scene.getWidth(), scene.getHeight());
        
        //Listener de actualización (SOLO del width):
        scene.widthProperty().addListener((obs, oldval, newVal) -> {
            actualizarEscalado(newVal.doubleValue(), scene.getHeight());
        });
        scene.widthProperty().addListener((obs, oldval, newVal) -> {
            actualizarEscalado(scene.getWidth(), newVal.doubleValue());
        });
        
        scene.getStylesheets().add(
            //Cargamos el CSS:
            getClass().getResource("/styles.css").toExternalForm()
        );
        
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setMinWidth(300);
        primaryStage.setMinHeight(375);
        
        final double ASPECT_RATIO = 4.0 / 5.0;
        final boolean[] ajustando = {false};
        
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (!ajustando[0]) {
                ajustando[0] = true;
                double newWidth = newVal.doubleValue();
                double newHeight = newWidth / ASPECT_RATIO;             
                primaryStage.setHeight(newHeight);
                ajustando[0] = false;
            } 
        });
        
        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
            if (!ajustando[0]) {
                ajustando[0] = true;
                double newHeight = newVal.doubleValue();
                double newWidth = newHeight * ASPECT_RATIO;             
                primaryStage.setWidth(newWidth);
                ajustando[0] = false;
            }
        });
        
        primaryStage.show();
    }
    
    private void actualizarEscalado(double width, double height) {
        double minDimension = Math.min(width / 30, height / 35);
        double espaciado = Math.max(2, height * 0.015);
        lPrincipal.setSpacing(espaciado);
        
        double padding = Math.max(3, height * 0.01);
        lPrincipal.setStyle("-fx-font-size: " + minDimension + "px; -fx-padding: " + padding + ";");   
    }
    
    //Hacer los botones responsivos:
    private void botonResponsivo(Button boton){
        boton.prefWidthProperty().bind(contenedor.widthProperty().multiply(0.37));
        boton.prefHeightProperty().bind(contenedor.widthProperty().multiply(0.37));
        boton.setMinWidth(50);
        boton.setMinHeight(50);
    }
 
    //Gestos de la mano/ratón para deslizar:
    private void gestos(javafx.scene.Node nodo){
        nodo.setOnMousePressed(e -> {
            mousePressX = e.getSceneX();
            e.consume();
        });
        
        nodo.setOnMouseReleased(e -> {
            double diferencia = e.getSceneX() - mousePressX;
            if (diferencia < -50 && paginaActual < 2){
                irPagina(paginaActual + 1);
            } else if (diferencia > 50 && paginaActual > 0){
                irPagina(paginaActual - 1);
            }
            e.consume();
        });
    }
    
    //1ª Página:
    private VBox crearPagina1(){
        //Título de la pagina1:    
        Label titulo = new Label("Partido");
        titulo.getStyleClass().add("titulo");

        //Panel de información: sets y juegos:
        labelSets = new Label("0  SETS  0");
        labelSets.getStyleClass().add("label-sets");
        labelJuegos = new Label("0  JUEGOS  0");
        labelJuegos.getStyleClass().add("label-juegos");

        VBox panelInformacion = new VBox(6);
        panelInformacion.setAlignment(Pos.CENTER);
        panelInformacion.getStyleClass().add("panel-informacion");
        panelInformacion.getChildren().addAll(labelSets, labelJuegos);

        //Botones-Marcador:
        botonMarcadorPareja1 = new Button("0");
        botonMarcadorPareja1.getStyleClass().add("boton-marcador");
        botonMarcadorPareja1.setOnAction(e -> puntoPareja1());
        botonMarcadorPareja1.setDisable(true);
        
        botonMarcadorPareja2 = new Button("0");
        botonMarcadorPareja2.getStyleClass().add("boton-marcador");
        botonMarcadorPareja2.setOnAction(e -> puntoPareja2());
        botonMarcadorPareja2.setDisable(true);

        //Botón de deshacer:
        botonDeshacer = new Button("↶");
        botonDeshacer.getStyleClass().add("boton-deshacer");
        botonDeshacer.setOnAction(e -> deshacer());
        botonDeshacer.setDisable(true);
        botonDeshacer.setPrefWidth(180);


        //Etiquetas de pareja:
        Label labelPareja1 = new Label("PAREJA 1");
        labelPareja1.getStyleClass().add("label-pareja");
        labelPareja1.setPrefWidth(180);
        labelPareja1.setAlignment(Pos.CENTER);
        
        Label labelPareja2 = new Label("PAREJA 2");
        labelPareja2.getStyleClass().add("label-pareja");
        labelPareja2.setPrefWidth(180);
        labelPareja2.setAlignment(Pos.CENTER);

        //Layouts marcador-etiquetas:
        HBox layoutMarcador = new HBox(20);
        layoutMarcador.setAlignment(Pos.CENTER);
        layoutMarcador.autosize();
        layoutMarcador.getChildren().addAll(botonMarcadorPareja1, botonMarcadorPareja2);
        
        HBox layoutEtiquetas = new HBox(20);
        layoutEtiquetas.setAlignment(Pos.CENTER);
        layoutMarcador.autosize();
        layoutEtiquetas.getChildren().addAll(labelPareja1, labelPareja2);
        
        HBox layoutDeshacer = new HBox(20);
        layoutDeshacer.setAlignment(Pos.CENTER);
        layoutDeshacer.getChildren().addAll(botonDeshacer);

        //Layout pagina:
        VBox pagina = new VBox(10);
        pagina.setAlignment(Pos.CENTER);
        pagina.getStyleClass().add("fondo-pagina");
        pagina.getChildren().addAll(
            titulo,
            panelInformacion,
            layoutEtiquetas,
            layoutMarcador,
            layoutDeshacer
        );
        return pagina;
    }
 
    //2ª Página:
    private VBox crearPagina2(){
        //Título de la pagina2: 
        Label titulo = new Label("Crono");
        titulo.getStyleClass().add("titulo2");
        
        //Cronometro:
        Label tituloCronometro = new Label("⏱");
        tituloCronometro.getStyleClass().add("label-titulo-kcal");
        labelCronometro = new Label("00:00:00");
        labelCronometro.getStyleClass().add("label-cronometro");
        
        //Botones del cronómetro:
        botonCronometro = new Button("▶");
        botonCronometro.getStyleClass().add("boton-inicio-cronometro");
        botonCronometro.setOnAction(e -> toggleCronometro());
        
        Button botonReinicorCompleto = new Button("↺");
        botonReinicorCompleto.getStyleClass().add("boton-reiniciar-cronometro");
        botonReinicorCompleto.setOnAction(e -> reinicioCompleto());
        
        HBox lBotonesCronometro = new HBox(15);
        lBotonesCronometro.setAlignment(Pos.CENTER);
        lBotonesCronometro.getChildren().addAll(botonCronometro, botonReinicorCompleto);
        
        //Label de Kcal:
        Label tituloKcal = new Label("Kcal quemadas");
        tituloKcal.getStyleClass().add("label-titulo-kcal");
        labelKcal = new Label("0kcal");
        labelKcal.getStyleClass().add("label-valor-kcal");
        
        //Iniciamos el cronómetro con el Timeline:
        cronometro = new Timeline(
            new KeyFrame(Duration.seconds(1), e -> tickCronometro())
        );
        cronometro.setCycleCount(Timeline.INDEFINITE);
        
        //Layout pagina:
        VBox pagina = new VBox(10);
        pagina.setAlignment(Pos.CENTER);
        pagina.getStyleClass().add("fondo-pagina");
        pagina.getChildren().addAll(
            titulo,
            tituloCronometro,
            labelCronometro,
            lBotonesCronometro,
            tituloKcal,
            labelKcal
        );
        return pagina;
    }
    
    //3ª Página:
    private VBox crearPagina3(){
        //Título de la página3:
        Label titulo = new Label("Estadísticas");
        titulo.getStyleClass().add("titulo-estadisticas");
        
        //nº Total de partidos:
        Label lTotalPartidos = new Label("PARTIDOS JUGADOS");
        lTotalPartidos.getStyleClass().add("label-estadisticas");
        labelTotalPartidos = new Label("0");
        labelTotalPartidos.getStyleClass().add("valor-numerico-estadisticas");
        
        //Layout total partidos jugados:
        VBox bTotalPartidos = new VBox(5);
        bTotalPartidos.setAlignment(Pos.CENTER);
        bTotalPartidos.getStyleClass().add("container-estadisticas");
        bTotalPartidos.getChildren().addAll(lTotalPartidos, labelTotalPartidos);
        
        //Victorias y derrotas:
        Label lResultados = new Label("RESULTADOS");
        lResultados.getStyleClass().add("label-estadisticas");
        
        Label lTusVictorias = new Label("Tus victorias:");
        lTusVictorias.getStyleClass().add("valor-numerico-estadisticas");
        labelVictorias = new Label("0");
        labelVictorias.getStyleClass().add("valor-numerico-estadisticas");
        
        Label lTusDerrotas = new Label("Tus derrotas:");
        lTusDerrotas.getStyleClass().add("valor-numerico-estadisticas");
        labelDerrotas = new Label("0");
        labelDerrotas.getStyleClass().add("valor-numerico-estadisticas");
        
        //Layout victorias y derrotas:
        HBox bVictorias = new HBox(10);
        bVictorias.setAlignment(Pos.CENTER);
        bVictorias.getChildren().addAll(lTusVictorias, labelVictorias);
        
        HBox bDerrotas = new HBox(10);
        bDerrotas.setAlignment(Pos.CENTER);
        bDerrotas.getChildren().addAll(lTusDerrotas, labelDerrotas);
        
        VBox bResultadosValores = new VBox(8);
        bResultadosValores.setAlignment(Pos.CENTER);
        bResultadosValores.getChildren().addAll(bVictorias, bDerrotas);
        
        VBox bResultados = new VBox(5);
        bResultados.setAlignment(Pos.CENTER);
        bResultados.getStyleClass().add("container-estadisticas");
        bResultados.getChildren().addAll(lResultados, bResultadosValores);
        
        //Duración media de partidos:
        Label lDuracion = new Label("DURACIÓN MEDIA");
        lDuracion.getStyleClass().add("label-estadisticas");
        
        labelDuracionMedia = new Label("00:00");
        labelDuracionMedia.getStyleClass().add("valor-estadisticas-2");
        
        //Layout Duración media de partido:
        VBox bDuracion = new VBox(5);
        bDuracion.setAlignment(Pos.CENTER);
        bDuracion.getStyleClass().add("container-estadisticas");
        bDuracion.getChildren().addAll(lDuracion, labelDuracionMedia);
           
        //Layout principal:
        VBox pagina = new VBox(10);
        pagina.setAlignment(Pos.CENTER);
        pagina.getStyleClass().add("fondo-pagina");
        pagina.getChildren().addAll(
            titulo,
            bTotalPartidos,
            bResultados,
            bDuracion
        );
        return pagina;
    }
    
    //Actualización de las estadísticas desde la BBDD:
    private void actualizarEstadisticas(){
        DatabaseManager db = partido.getDatabase();
        
        int totalPartidos = db.getTotalPartidos();
        int victoriasPareja1 = db.getVictoriasPareja1();
        int derrotasPareja1 = db.getDerrotasPareja1();
        int duracionMedia = db.getDuracionMedia();
        int totalPuntos = db.getPuntosTotales();
        
        labelTotalPartidos.setText(String.valueOf(totalPartidos));
        labelVictorias.setText(String.valueOf(victoriasPareja1));
        labelDerrotas.setText(String.valueOf(derrotasPareja1));
        
        //Conversión de la duración media del partido a "mm:ss" :
        int minutos = duracionMedia / 60;
        int segundos = duracionMedia % 60;
        labelDuracionMedia.setText(String.format("%02d:%02d", minutos, segundos));
    }
    
    //Navegación entre páginas:
    private void irPagina(int pagina){
        paginaActual = pagina;
        //Actualizamos la pagina visible:
        switch (pagina) {
            case 0 -> pagina1.toFront();
            case 1 -> pagina2.toFront();
            case 2 -> {
                pagina3.toFront();
                actualizarEstadisticas();
            }
            default -> {
            }
        }
        punto1.getStyleClass().setAll(pagina == 0 ? "punto-navegacion-dinamico" : "punto-navegacion");
        punto2.getStyleClass().setAll(pagina == 1 ? "punto-navegacion-dinamico" : "punto-navegacion");
        punto3.getStyleClass().setAll(pagina == 2 ? "punto-navegacion-dinamico" : "punto-navegacion");
    }
    
    //Añadimos la parte lógica al cronómetro:
    private void toggleCronometro(){
        if (cronometroActivo){
            cronometro.pause();
            cronometroActivo = false;
            botonCronometro.setText("▶");
            botonCronometro.getStyleClass().setAll("boton-inicio-cronometro");
            //Deshabilitamos los botones del marcador:
            botonMarcadorPareja1.setDisable(true);
            botonMarcadorPareja2.setDisable(true);
            botonDeshacer.setDisable(true);
        } else {
            //Creamos partido en la BBDD si es la primera vez:
            if (totalSegundos == 0) {
                partido.iniciarPartido();
            }
            //Inicio de partido:
            cronometro.play();
            cronometroActivo = true;
            botonCronometro.setText("⏸");
            botonCronometro.getStyleClass().setAll("boton-pausa-cronometro");
            //Habilitamos los botones del marcador:
            botonMarcadorPareja1.setDisable(false);
            botonMarcadorPareja2.setDisable(false); 
            actualizarBotonDeshacer();
        }
    }

    private void actualizarCronometro(){
        int horas = totalSegundos / 3600;
        int minutos = (totalSegundos % 3600) / 60;
        int segundos = totalSegundos % 60;
        
        labelCronometro.setText(
            String.format("%02d:%02d:%02d", horas, minutos, segundos)
        );
        
        //Calcular kcal, teniendo en cuenta estimación (300kcal/hora):
        double kcal = totalSegundos * KCAL_POR_SEGUNDO;
        labelKcal.setText(String.format("%.1f", kcal) + "kcal");
    }
    
    private void tickCronometro(){
        totalSegundos++;
        actualizarCronometro();
    }
    
    //Método cuando pareja 1 gane un punto:
    private void puntoPareja1(){
        partido.addPuntoPareja1();
        actualizarMarcador();
        actualizarBotonDeshacer();
        verificarGanador();
    }
    
    //Método cuando pareja 2 gane un punto:
    private void puntoPareja2(){
        partido.addPuntoPareja2();
        actualizarMarcador();
        actualizarBotonDeshacer();
        verificarGanador();
    }
    
    //Actualizar todos los labels con la información del partido:
    private void actualizarMarcador(){
        //Actualizamos los botones-marcador:
        String[] puntos = partido.getPuntuacion().split(" - ");
        botonMarcadorPareja1.setText(puntos[0]);
        botonMarcadorPareja2.setText(puntos[1]);
        
        //Actualizamos la información de los sets y los juegos:
        labelSets.setText(partido.getsetsPareja1() + "  SETS  " + partido.getsetsPareja2());
        
        if (partido.istieBreak()){
          labelJuegos.setText("Tie-Break");
          labelJuegos.getStyleClass().setAll("label-tiebreak");
        } else {
          labelJuegos.setText(partido.getJuegosPareja1() + "  JUEGOS  " + partido.getJuegosPareja2());
          labelJuegos.getStyleClass().setAll("label-juegos");
        }  
    }
    
    //Verificamos el ganador:
    private void verificarGanador(){
        if (partido.ganador()){
            if (cronometroActivo) {
                cronometro.pause();
                cronometroActivo = false;
            }
            //Finalizamos en BBDD:
            partido.finalizarPartidoBD();
            
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Final del partido");
            alerta.setHeaderText(partido.getGanador());
            alerta.setContentText("Se reiniciará el partido");
            alerta.showAndWait();
            
            //Reiniciamos automáticamente el partido:
            reiniciar();
        }
    }
    
    //Método para deshacer el último punto:
    private void deshacer(){
        if (partido.deshacer()){
            actualizarMarcador();
            actualizarBotonDeshacer();
        }
    }
    
    //Método para actualizar el estado de los botonea de deshacer:
    private void actualizarBotonDeshacer(){
        boolean historial = partido.historial();
        botonDeshacer.setDisable(!historial);
    }
    
    //Método para el reinicio del partido (manual):
    private void reinicioCompleto(){
        //Paramos el cronometro cuando le damos al botón de reinicio:
        boolean  cronoEmpezado = cronometroActivo;
        if (cronometroActivo) {
            cronometro.pause();
            cronometroActivo = false;
        }
        //Mensaje de alerta tras darle al botón de reinicio:
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Reiniciar Partido");
        alerta.setHeaderText("¿Quieres reiniciar el partido?");
        alerta.setContentText("Se perderán todos los datos del partido actual.\n¿Quieres continuar?");
        
        //Botones de confirmación:
        ButtonType botonSi = new ButtonType("Continuar");
        ButtonType botonNo = new ButtonType("Deshacer", ButtonBar.ButtonData.CANCEL_CLOSE);
        alerta.getButtonTypes().setAll(botonSi, botonNo);
        
        alerta.showAndWait().ifPresent(response -> {
            if (response == botonSi){
                reiniciarTodo();
            } else {
                if (cronoEmpezado) {
                    cronometro.play();
                    cronometroActivo = true;
                    botonCronometro.setText("⏸");
                    botonCronometro.getStyleClass().setAll("boton-inicio-cronometro");
                }
            }
        });
    }

    //Método para reiniciar cronómetro + marcador (manual):
    private void reiniciarTodo() {
        cronometro.stop();
        totalSegundos = 0;
        cronometroActivo = false;
        botonCronometro.setText("▶");
        botonCronometro.getStyleClass().setAll("boton-inicio-cronometro");
        labelCronometro.setText("00:00:00");
        labelKcal.setText("0kcal");
        
        partido.reiniciar();
        
        //Reinicio de la interfaz:
        labelSets.getStyleClass().setAll("label-sets");
        labelJuegos.getStyleClass().setAll("label-juegos");
        botonMarcadorPareja1.getStyleClass().setAll("boton-marcador");
        botonMarcadorPareja2.getStyleClass().setAll("boton-marcador");
        actualizarMarcador();
        actualizarBotonDeshacer();
        
        //Deshabilitar los botones del marcador:
        botonMarcadorPareja1.setDisable(true);
        botonMarcadorPareja2.setDisable(true);
    }
    
    //Método para reiniciar el marcador (automático):
    private void reiniciar(){
        reiniciarTodo();
    }
    
    public static void main(String[] args){
        launch(args);
    }
}