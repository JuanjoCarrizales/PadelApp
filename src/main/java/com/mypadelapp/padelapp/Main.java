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
    
    @Override
    public void start(Stage primaryStage){
        //Creamos el modelo partido:
        partido = new PartidoPadel();
        primaryStage.setTitle("Marcador de Pádel");
        
        //Título de la ventana:
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
        
        //Layout principal:
        VBox layoutPrincipal = new VBox(22);
        layoutPrincipal.setAlignment(Pos.CENTER);
        layoutPrincipal.setPadding(new Insets(35));
        layoutPrincipal.getChildren().addAll(
            titulo,
            panelInformacion,
            layoutEtiquetas,
            layoutMarcador,
            botonReiniciar
        );
        
        //Cargamos el CSS:
        Scene scene = new Scene(layoutPrincipal, 480, 520);
        scene.getStylesheets().add(
            getClass().getResource("/styles.css").toExternalForm()
        );
        
        primaryStage.setScene(scene);
        primaryStage.show();
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
