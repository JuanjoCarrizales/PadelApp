/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mypadelapp.padelapp;

import com.mypadelapp.padelapp.modelo.PartidoPadel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
    private Circle punto1;
    private Circle punto2;
    private int paginaActual = 0;
    private double mousePressX;
    
    @Override
    public void start(Stage primaryStage){
        //Creamos el modelo partido:
        partido = new PartidoPadel();
        primaryStage.setTitle("Marcador de Pádel");
        
        //Creación de las páginas:
        pagina1 = crearPagina1();
        pagina2 = crearPagina2();
        
        //Contenedor que superpone las 2 páginas:
        contenedor = new StackPane();
        contenedor.getChildren().addAll(pagina1, pagina2);
        
        //Gestos de la mano/ratón para deslizar:
        contenedor.setOnMousePressed(e -> mousePressX = e.getSceneX());
        contenedor.setOnMouseReleased(e -> {
            double diferencia = e.getSceneX() - mousePressX;
            if (diferencia < -50 && paginaActual == 0){
                //Deslizamos a la izquierda a la página 2:
                irPagina(1);
            } else if (diferencia > 50 && paginaActual == 1){
                //Deslizamos a la derecha a la página 1:
                irPagina(0);
            }
        });
        
        //Navegación por "puntos":
        punto1 = new Circle(5);
        punto1.getStyleClass().add("punto-navegacion-dinamico");
        punto2 = new Circle(5);
        punto2.getStyleClass().add("punto-navegacion");
        
        HBox puntos = new HBox(8);
        puntos.setAlignment(Pos.CENTER);
        puntos.getChildren().addAll(punto1, punto2);
        
        //Layout principal:
        VBox lPrincipal = new VBox(10);
        lPrincipal.setAlignment(Pos.CENTER);
        lPrincipal.getStyleClass().add("fondo-principal");
        lPrincipal.getChildren().addAll(contenedor, puntos);
        
        //Cargamos el CSS:
        Scene scene = new Scene(lPrincipal, 480, 520);
        scene.getStylesheets().add(
            getClass().getResource("/styles.css").toExternalForm()
        );
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }   
    
    //1ª Página:
    private VBox crearPagina1(){
        //Título de la pagina1:    
        Label titulo = new Label ("Partido");
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
        botonMarcadorPareja2 = new Button("0");
        botonMarcadorPareja2.getStyleClass().add("boton-marcador");
        botonMarcadorPareja2.setOnAction(e -> puntoPareja2());

        //Botón de reinicio:
        Button botonReiniciar = new Button("↺");
        botonReiniciar.getStyleClass().add("boton-reiniciar");
        botonReiniciar.setOnAction(e -> reiniciar());

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
        layoutMarcador.getChildren().addAll(botonMarcadorPareja1, botonMarcadorPareja2);
        HBox layoutEtiquetas = new HBox(20);
        layoutEtiquetas.setAlignment(Pos.CENTER);
        layoutEtiquetas.getChildren().addAll(labelPareja1, labelPareja2);

        //Layout pagina:
        VBox pagina = new VBox(22);
        pagina.setAlignment(Pos.CENTER);
        pagina.getStyleClass().add("fondo-pagina");
        pagina.getChildren().addAll(
            titulo,
            panelInformacion,
            layoutEtiquetas,
            layoutMarcador,
            botonReiniciar
        );
        return pagina;
    }
    
    //2ª Página:
    private VBox crearPagina2(){
        //Título de la pagina2: 
        Label titulo = new Label("Estadísticas");
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
        Button botonReiniciarCronometro = new Button("↺");
        botonReiniciarCronometro.getStyleClass().add("boton-reiniciar-cronometro");
        botonReiniciarCronometro.setOnAction(e -> reiniciarCronometro());
        
        HBox lBotonesCronometro = new HBox(15);
        lBotonesCronometro.setAlignment(Pos.CENTER);
        lBotonesCronometro.getChildren().addAll(botonCronometro, botonReiniciarCronometro);
        
        //Label de Kcal:
        Label tituloKcal = new Label("Kcal quemadas");
        tituloKcal.getStyleClass().add("label-titulo-kcal");
        labelKcal = new Label("0");
        labelKcal.getStyleClass().add("label-valor-kcal");
        
        //Iniciamos el cronómetro con el Timeline:
        cronometro = new Timeline(
            new KeyFrame(Duration.seconds(1), e -> tickCronometro())
        );
        cronometro.setCycleCount(Timeline.INDEFINITE);
        
        //Layout pagina:
        VBox pagina = new VBox(18);
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
    
    //Añadimos la parte lógica al cronómetro:
    private void toggleCronometro(){
        if (cronometroActivo){
            cronometro.pause();
            botonCronometro.setText("▶");
            botonCronometro.getStyleClass().setAll("boton-inicio-cronometro");
        } else{
            cronometro.play();
            botonCronometro.setText("⏸");
            botonCronometro.getStyleClass().setAll("boton-pausa-cronometro");
        }
        cronometroActivo =! cronometroActivo;
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
        labelKcal.setText(String.format("%.1f", kcal));
    }
    
    private void tickCronometro(){
        totalSegundos++;
        actualizarCronometro();
    }
    
    private void reiniciarCronometro(){
        cronometro.stop();
        cronometroActivo = false;
        totalSegundos = 0;
        botonCronometro.setText("▶");
        botonCronometro.getStyleClass().setAll("boton-inicio-cronometro");
        labelCronometro.setText("00:00:00");
        labelKcal.setText("0kcal");
    }

    //Navegación entre páginas:
    private void irPagina(int pagina){
        paginaActual = pagina;
        if (pagina == 0){
            pagina1.toFront();
            punto1.getStyleClass().setAll("punto-navegacion-dinamico");
            punto2.getStyleClass().setAll("punto-navegacion");
        } else {
            pagina2.toFront();
            punto1.getStyleClass().setAll("punto-navegacion");
            punto2.getStyleClass().setAll("punto-navegacion-dinamico");
        }
    }
    
    //Método cuando pareja 1 gane un punto:
    private void puntoPareja1(){
        partido.addPuntoPareja1();
        actualizarMarcador();
        verificarGanador();
    }
    
    //Método cuando pareja 2 gane un punto:
    private void puntoPareja2(){
        partido.addPuntoPareja2();
        actualizarMarcador();
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
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Final del partido");
            alerta.setHeaderText(partido.getGanador());
            alerta.setContentText("Presiona 'Reiniciar' para jugar de nuevo");
            alerta.showAndWait();
        }
    }
    
    //Método par reiniciar el marcador:
    private void reiniciar(){
        partido.reiniciar();
        
        //Reiniciamos las clases CSS:
        labelJuegos.getStyleClass().setAll("label-juegos");
        actualizarMarcador();
    }
    
    public static void main(String[] args){
        launch(args);
    }
}
