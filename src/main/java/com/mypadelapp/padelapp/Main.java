/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mypadelapp.padelapp;

import com.mypadelapp.padelapp.modelo.PartidoPadel;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {
    
    //Modelo del partido:
    private PartidoPadel partido;
    
    //Labels para ver la información (parte de arriba):
    private Label labelSets;
    private Label labelJuegos;
    
    //Botones-Marcador:
    private Button botonMarcadorPareja1;
    private Button botonMarcadorPareja2;
    
    //Colores principales de la interfaz:
    private static final String COLOR_DE_FONDO = "#000000";
    private static final String COLOR_PANEL_INFORMACION = "#000000";
    private static final String COLOR_BOTON_PAREJA1 = "#aeaeb0";
    private static final String COLOR_BOTON_PAREJA2 = "#aeaeb0";
    private static final String COLOR_BOTON_HOVER1 = "#818182";
    private static final String COLOR_BOTON_HOVER2 = "#818182";
    private static final String COLOR_TEXTO_PUNTOS = "#ffffff";
    private static final String COLOR_SETS = "#f1c40f";
    private static final String COLOR_JUEGOS = "#aaaacc";
    private static final String COLOR_REINICIAR = "#8e0000";
    
    @Override
    public void start(Stage primaryStage){
        //Creamos el modelo partido:
        partido = new PartidoPadel();
        primaryStage.setTitle("Marcador de Pádel");
        
        //Título de la ventana:
        Label titulo = new Label ("Partido");
        titulo.setStyle("-fx-font-size: 26px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: " + COLOR_SETS + ";");
        
        //Panel de información: sets y juegos:
        labelSets = new Label("0  SETS  0");
        labelSets.setStyle("-fx-font-size: 16px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill:" + COLOR_SETS + ";");
        
        labelJuegos = new Label("0  JUEGOS  0");
        labelJuegos.setStyle("-fx-font-size: 16px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill:" + COLOR_JUEGOS + ";");
        
        VBox panelInformacion = new VBox(6);
        panelInformacion.setAlignment(Pos.CENTER);
        panelInformacion.setPadding(new Insets(12, 20, 12, 20));
        panelInformacion.setStyle("-fx-background-color: " + COLOR_PANEL_INFORMACION + "; -fx-background-radius: 12;");
        panelInformacion.getChildren().addAll(labelSets, labelJuegos);
        
        //Botones-Marcador:
        botonMarcadorPareja1 = new Button("0");
        botonMarcadorPareja1.setPrefSize(180, 180);
        botonMarcadorPareja1.setStyle(crearEstiloBotonMarcador(COLOR_BOTON_PAREJA1));
        botonMarcadorPareja1.setOnAction(e -> puntoPareja1());
        
        //Creamos el efecto hover de la pareja1:
        botonMarcadorPareja1.setOnMouseEntered(e -> botonMarcadorPareja1.setStyle(crearEstiloBotonMarcador(COLOR_BOTON_HOVER1)));
        botonMarcadorPareja1.setOnMouseExited(e -> botonMarcadorPareja1.setStyle(crearEstiloBotonMarcador(COLOR_BOTON_PAREJA1)));
        
        botonMarcadorPareja2 = new Button("0");
        botonMarcadorPareja2.setPrefSize(180, 180);
        botonMarcadorPareja2.setStyle(crearEstiloBotonMarcador(COLOR_BOTON_PAREJA2));
        botonMarcadorPareja2.setOnAction(e -> puntoPareja2());
        
        //Creamos el efecto hover de la pareja2:
        botonMarcadorPareja2.setOnMouseEntered(e -> botonMarcadorPareja2.setStyle(crearEstiloBotonMarcador(COLOR_BOTON_HOVER2)));
        botonMarcadorPareja2.setOnMouseExited(e -> botonMarcadorPareja2.setStyle(crearEstiloBotonMarcador(COLOR_BOTON_PAREJA2)));
        

        //Layout de los botones del marcador:
        HBox layoutMarcador = new HBox(20);
        layoutMarcador.setAlignment(Pos.CENTER);
        layoutMarcador.getChildren().addAll(botonMarcadorPareja1, botonMarcadorPareja2);
            
        //Botón de reinicio:
        Button botonReiniciar = new Button("↺");
        botonReiniciar.setStyle("-fx-font-size: 14px;"
                + "-fx-padding: 10px 24px;"
                + "-fx-background-color:" + COLOR_REINICIAR + ";"
                + "-fx-text-fill: white;"
                + "-fx-background-radius: 20;"
                + "-fx-cursor: hand;"
        );
        botonReiniciar.setOnAction(e -> reiniciar());
        
        //Etiquetas de pareja:
        Label labelPareja1 = new Label("PAREJA 1");
        labelPareja1.setStyle("-fx-font-size: 20px;" 
                + "-fx-text-fill: #f7f7f2;"
                + "-fx-text-weight: bold;");
        labelPareja1.setPrefWidth(180);
        labelPareja1.setAlignment(Pos.CENTER);
        
        Label labelPareja2 = new Label("PAREJA 2");
        labelPareja2.setStyle("-fx-font-size: 20px;" 
                + "-fx-text-fill: #f7f7f2;"
                + "-fx-text-weight: bold;");
        labelPareja2.setPrefWidth(180);
        labelPareja2.setAlignment(Pos.CENTER);
        
        HBox layoutEtiquetas = new HBox(20);
        layoutEtiquetas.setAlignment(Pos.CENTER);
        layoutEtiquetas.getChildren().addAll(labelPareja1, labelPareja2);
        
        //Layout principal:
        VBox layoutPrincipal = new VBox(22);
        layoutPrincipal.setAlignment(Pos.CENTER);
        layoutPrincipal.setPadding(new Insets(35));
        layoutPrincipal.setStyle("-fx-background-color: " + COLOR_DE_FONDO + ";");
        layoutPrincipal.getChildren().addAll(
                titulo,
                panelInformacion,
                layoutEtiquetas,
                layoutMarcador,
                botonReiniciar
        );
        
        Scene scene = new Scene(layoutPrincipal, 480, 520);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    //Contrucción del estilo CSS de los botones del marcador:
    private String crearEstiloBotonMarcador(String colorFondo){
        return "-fx-font-size: 72px;" +
               "-fx-font-weight: bold;" + 
               "-fx-font-fill: " + COLOR_TEXTO_PUNTOS + ";" +
               "-fx-background-color :" + colorFondo + ";" +
               "-fx-background-radius: 20;" +
               "-fx-cursor: hand;";
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
      if (partido.istieBreak()){
          labelJuegos.setText("Tie-Break");
          labelJuegos.setStyle("-fx-font-size: 16px;"
                  + "-fx-font-wight: bold;"
                  + "-fx-text-fill: #e74c3c;");
      } else {
          labelJuegos.setText(partido.getJuegosPareja1() + "  JUEGOS  " + partido.getJuegosPareja2());
          labelJuegos.setStyle("-fx-font-size: 16px;"
                  + "-fx-font-wight: bold;"
                  + "-fx-text-fill: " + COLOR_JUEGOS + ";");
      }
      
      labelSets.setText(partido.getsetsPareja1() + "  SETS  " + partido.getsetsPareja2());
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
        actualizarMarcador();
    }
    
    public static void main(String[] args){
        launch(args);
    }
}
