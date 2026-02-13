/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mypadelapp.padelapp.modelo;

/**
 *
 * @author juanj
 */
public class PartidoPadel {
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
    }
    
    //Puntuación pareja 1:
    public void addPuntoPareja1(){
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
}
