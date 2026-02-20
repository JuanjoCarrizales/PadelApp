/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mypadelapp.padelapp.modelo;
import java.util.Stack;

/**
 *
 * @author juanj
 */
public class PartidoPadel {
    //Clase para guardar el estado del partido:
    private static class EstadoPartido{
        int puntosP1, puntosP2;
        int juegosP1, juegosP2;
        int setsP1, setsP2;
        boolean ventajaP1, ventajaP2;
        boolean tieBreak;
        int puntosTieBreakP1, puntosTieBreakP2;
        
        EstadoPartido (int puntosP1, int puntosP2, int juegosP1, int juegosP2,
                int setsP1, int setsP2, boolean ventajaP1, boolean ventajaP2,
                boolean tieBreak, int puntosTieBreakP1, int puntosTieBreakP2){
            this.puntosP1 = puntosP1;
            this.puntosP2 = puntosP2;
            this.juegosP1 = juegosP1;
            this.juegosP2 = juegosP2;
            this.setsP1 = setsP1;
            this.setsP2 = setsP2;
            this.ventajaP1 = ventajaP1;
            this.ventajaP2 = ventajaP2;
            this.tieBreak = tieBreak;
            this.puntosTieBreakP1 = puntosTieBreakP1;
            this.puntosTieBreakP2 = puntosTieBreakP2;
        }
    }
    
    //Historial Para deshacer:
    private Stack<EstadoPartido> historial;
    
    //Puntos actuales en el juego:
    private int puntosPareja1;
    private int puntosPareja2;
    
    //Juegos ganados en el set actual:
    private int juegosPareja1;
    private int juegosPareja2;
    
    //Sets ganados en el partido:
    private int setsPareja1;
    private int setsPareja2;
    
    //Estado especial (ventajas):
    private boolean ventajaPareja1;
    private boolean ventajaPareja2;
    
    //TieBreak:
    private boolean tieBreak;
    private int puntosTieBreakPareja1;
    private int puntosTieBreakPareja2;
    
    //Método constructor: Se inicia todo en 0:
    public PartidoPadel(){
        reiniciar();
    }
    
    //Reiniciar el partido completo:
    public void reiniciar(){
        puntosPareja1 = 0;
        puntosPareja2 = 0;
        juegosPareja1 = 0;
        juegosPareja2 = 0;
        setsPareja1 = 0;
        setsPareja2 = 0;
        ventajaPareja1 = false;
        ventajaPareja2 = false;
        tieBreak = false;
        puntosTieBreakPareja1 = 0;
        puntosTieBreakPareja2 = 0; 
        historial = new Stack<>();
    }
    
    //Puntuación pareja 1:
    public void addPuntoPareja1(){
        //Estado del partido:
        guardarEstado();
        
        //Si estamos en Tie-Break:
        if (tieBreak){
            puntosTieBreakPareja1++;
            //Comprobamos si el lo gana la pareja 1 (7 puntos y con 2 de diferencia):
            if (puntosTieBreakPareja1 >= 7 && puntosTieBreakPareja1 >= puntosTieBreakPareja2 + 2){
                ganarSetPareja1TieBreak();
            } 
            return;
        }
        
        //Si estamos en iguales (40-40):
        if (puntosPareja1 >= 3 && puntosPareja2 >= 3){
            if (ventajaPareja2){
                ventajaPareja2 = false;
            } else if (ventajaPareja1){
                ganarJuegoPareja1();
            } else {
                ventajaPareja1 = true;
            }
        } else {
            //Contamos la puntuación normal del juego:
            puntosPareja1++;
            if (puntosPareja1 >= 4 && puntosPareja1 >= puntosPareja2 + 2){
                ganarJuegoPareja1();
            }
        }
    }
    
    //Puntuación pareja 2:
    public void addPuntoPareja2(){
        //Estado del partido:
        guardarEstado();
        
        //Si estamos en Tie-Break:
        if (tieBreak){
            puntosTieBreakPareja2++;
            //Comprobamos si el lo gana la pareja 2 (7 puntos y con 2 de diferencia):
            if (puntosTieBreakPareja2 >= 7 && puntosTieBreakPareja2 >= puntosTieBreakPareja1 + 2){
                ganarSetPareja2TieBreak();
            } 
            return;
        }
        
        //Si estamos en iguales (40-40):
        if (puntosPareja2 >= 3 && puntosPareja1 >= 3){
            if (ventajaPareja1){
                ventajaPareja1 = false;
            } else if (ventajaPareja2){
                ganarJuegoPareja2();
            } else {
                ventajaPareja2 = true;
            }
        } else {
            //Contamos la puntuación normal del juego:
            puntosPareja2++;
            if (puntosPareja2 >= 4 && puntosPareja2 >= puntosPareja1 + 2){
                ganarJuegoPareja2();
            }
        }
    }
    
    //Pareja 1 gana un juego:
    private void ganarJuegoPareja1(){
        juegosPareja1++;
        reiniciarPuntos();
        verificarGanadorSet();
    }
    
    //Pareja 2 gana un juego:
    private void ganarJuegoPareja2(){
        juegosPareja2++;
        reiniciarPuntos();
        verificarGanadorSet();
    }
    
    //Reiniciar los puntos del juego actual:
    private void reiniciarPuntos(){
        puntosPareja1 = 0;
        puntosPareja2 = 0;
        ventajaPareja1 = false;
        ventajaPareja2 = false;
    }
    
    //Verificamos quien ganó el set:
    private void verificarGanadorSet(){
        //Comprobamos que se ganó con 6 juegos y al menos 2 de diferencia:
        if (juegosPareja1 >= 6 && juegosPareja1 >= juegosPareja2 + 2){
            setsPareja1++;
            reiniciarJuegos();
        } else if (juegosPareja2 >= 6 && juegosPareja2 >= juegosPareja1 + 2){
            setsPareja2++;
            reiniciarJuegos();
        } 
        //Si se llega a 6-6, se juega tie-break:
        else if (juegosPareja1 == 6 && juegosPareja2 ==6){
            tieBreak = true;
            reiniciarPuntos();
        }  
    }
    
    //Reiniciamos los juegos del set actual:
    private void reiniciarJuegos(){
        juegosPareja1 = 0;
        juegosPareja2 = 0;
    }
    
    //Puntuación en formato texto:
    public String getPuntuacion(){
        //Si estamos en tie-break:
        if (tieBreak){
            return puntosTieBreakPareja1 + " - " + puntosTieBreakPareja2;
        }
        
        //Si estamos en ventajas:
        if (puntosPareja1 >= 3 && puntosPareja2 >= 3){
            if (ventajaPareja1){
                return "V - 40";
            } else if (ventajaPareja2){
                return "40 - V";
            }
        }
        
        //Puntuación normal:
        return convertirPuntos(puntosPareja1) + " - " + convertirPuntos(puntosPareja2);
    }
 
    //Convertimos los puntos en formato padel (0, 15, 30, 40):
    public String convertirPuntos(int puntos){
        switch (puntos){
            case 0: return "0";
            case 1: return "15";
            case 2: return "30";
            case 3: return "40";
            default: return "40";
        }
    }
    
    //Añadimos los getters para mostrar en la interfaz:
    public int getJuegosPareja1(){
        return juegosPareja1;
    }
    public int getJuegosPareja2(){
        return juegosPareja2;
    }
    public int getsetsPareja1(){
        return setsPareja1;
    }
    public int getsetsPareja2(){
        return setsPareja2;
    }
    
    //Verificamos el ganador del partido:
    public boolean ganador(){
        return setsPareja1 >= 2 || setsPareja2 >= 2;
    }
    
    public String getGanador(){
        if (setsPareja1 >= 2){
            return "Pareja 1 gana!";
        } else if (setsPareja2 >= 2){
            return "Pareja 2 gana!";
        }
        return "";
    }
    
    //Si la pareja1 gana el set en tie-break:
    private void ganarSetPareja1TieBreak(){
        setsPareja1++;
        tieBreak = false;
        puntosTieBreakPareja1 = 0;
        puntosTieBreakPareja2 = 0;
        reiniciarJuegos();
    }
    
    //Si la pareja2 gana el set en tie-break:
    private void ganarSetPareja2TieBreak(){
        setsPareja2++;
        tieBreak = false;
        puntosTieBreakPareja1 = 0;
        puntosTieBreakPareja2 = 0;
        reiniciarJuegos();
    }
    
    //Getter para saber si estamos en tie-break:
    public boolean istieBreak(){
        return tieBreak;
    }
    
    //Getter para los puntos del tie-break:
    public int getPuntosTieBreakPareja1(){
        return puntosTieBreakPareja1;
    }
    
     public int getPuntosTieBreakPareja2(){
        return puntosTieBreakPareja2;
    }
     
    //Guardado del estado actual del partido en el historial:
    private void guardarEstado(){
        EstadoPartido estado = new EstadoPartido(puntosPareja1, puntosPareja2,
            juegosPareja1, juegosPareja2, setsPareja1, setsPareja2, ventajaPareja1,
            ventajaPareja2, tieBreak, puntosTieBreakPareja1, puntosTieBreakPareja2
        );
        historial.push(estado);
    }
    
    //Deshacer el último punto:
    public boolean deshacer(){
        if (historial.isEmpty()){
            return false; //En este caso, no deshará nada
        }
        
        EstadoPartido estado = historial.pop();
        //Restauramos el estado/punto:
        puntosPareja1 = estado.puntosP1;
        puntosPareja2 = estado.puntosP2;
        juegosPareja1 = estado.juegosP1;
        juegosPareja2 = estado.juegosP2;
        setsPareja1 = estado.setsP1;
        setsPareja2 = estado.setsP2;
        ventajaPareja1 = estado.ventajaP1;
        ventajaPareja2 = estado.ventajaP2;
        tieBreak = estado.tieBreak;
        puntosTieBreakPareja1 = estado.puntosTieBreakP1;
        puntosTieBreakPareja2 = estado.puntosTieBreakP2;
        
        return true;
    }
    
    //Verificamos si tenemos historial disponible:
    public boolean historial(){
        return !historial.isEmpty();
    }
}
