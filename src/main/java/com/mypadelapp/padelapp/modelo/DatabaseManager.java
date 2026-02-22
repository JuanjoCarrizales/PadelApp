/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mypadelapp.padelapp.modelo;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:padelapp.db";
    private Connection conexion;
    
    //Método constructor: Conecta a la BBDD y crea tablas si no existen:
    public DatabaseManager(){
        try {
            conectar();
            crearTablas();
        } catch (SQLException e){
            System.err.println("Error al iniciar la BBDD: " + e.getMessage());
        }
    }
    
    //Conexión a la BBDD:
    private void conectar() throws SQLException {
        conexion = DriverManager.getConnection(DB_URL);
        System.out.println("Conexión SQLite establecida");
    }
    
    //Creación de las tablas si no existen:
    private void crearTablas() throws SQLException {
        String sqlPartidos = """ 
            CREATE TABLE IF NOT EXISTS partidos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                fecha_inicio TEXT NOT NULL,                 
                fecha_fin TEXT,                 
                duracion_total INTEGER,                 
                nombre_pareja1 TEXT DEFAULT 'Pareja 1',
                nombre_pareja2 TEXT DEFAULT 'Pareja 2',
                sets_pareja1 INTEGER DEFAULT 0,                 
                sets_pareja2 INTEGER DEFAULT 0,                 
                ganador INTEGER,
                partido_finalizado BOOLEAN DEFAULT 0         
            )                     
        """;
        
        String sqlPuntos = """ 
            CREATE TABLE IF NOT EXISTS puntos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                id_partido INTEGER NOT NULL,                 
                timestamp INTEGER NOT NULL,                 
                pareja_ganadora INTEGER NOT NULL,                 
                puntos_pareja1 INTEGER,
                puntos_pareja2 INTEGER,
                juegos_pareja1 INTEGER,
                juegos_pareja2 INTEGER,           
                sets_pareja1 INTEGER,                 
                sets_pareja2 INTEGER,                 
                ganador INTEGER,
                tiebreak BOOLEAN,
                FOREIGN KEY (id_partido) REFERENCES partidos(id)         
            )                     
        """;
        
        Statement sentencia = conexion.createStatement();
        sentencia.execute(sqlPartidos);
        sentencia.execute(sqlPuntos);
        System.out.println("Tablas creadas/verificadas");
    }
    
    //Inicio de nuevo partido (devuelve id_partido):
    public int iniciarPartido(String nombreP1, String nombreP2) throws SQLException {
        String sql = """ 
            INSERT INTO partidos (fecha_inicio, nombre_pareja1, nombre_pareja2)
            VALUES (?, ? ,?)         
        """;
        
        PreparedStatement pstatement = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pstatement.setString(1, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        pstatement.setString(2, nombreP1);
        pstatement.setString(3, nombreP2);
        pstatement.executeUpdate();
        
        ResultSet resultado = pstatement.getGeneratedKeys();
        if (resultado.next()){
            int idPartido = resultado.getInt(1);
            System.out.println("ID de partido: " + idPartido);
            return idPartido;
        }
        throw new SQLException("No se pudo recuperar el ID del partido");
    }
    
    //Guardado de puntos:
    public void guardarPuntos(int idPartido, int timestamp, int parejaGanadora,
            int puntosP1, int puntosP2, int juegosP1, int juegosP2, int setsP1,
            int setsP2, boolean tiebreak) throws SQLException {
        String sql = """ 
            INSERT INTO puntos (id_partido, timestamp, pareja_ganadora, puntos_pareja1,
                puntos_pareja2, juegos_pareja1, juegos_pareja2, sets_pareja1, sets_pareja2, tiebreak)         
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)                         
        """;
    
        PreparedStatement pstatement = conexion.prepareStatement(sql);
        pstatement.setInt(1, idPartido);
        pstatement.setInt(2, timestamp);
        pstatement.setInt(3, parejaGanadora);
        pstatement.setInt(4, puntosP1);
        pstatement.setInt(5, puntosP2);
        pstatement.setInt(6, juegosP1);
        pstatement.setInt(7, juegosP2);
        pstatement.setInt(8, setsP1);
        pstatement.setInt(9, setsP2);
        pstatement.setBoolean(10, tiebreak);
        pstatement.executeUpdate();
    }
    
    //Fin del partido:
    public void finalizarPartido(int idPartido, int duracionTotal, int setsP1,
            int setsP2, int ganador) throws SQLException {
        String sql = """ 
            UPDATE partidos         
            SET fecha_fin = ?, duracion_total = ?, sets_pareja1 = ?,         
            sets_pareja2 = ?, ganador = ?, partido_finalizado = 1         
            WHERE id = ?         
        """;
        
        PreparedStatement pstatement = conexion.prepareStatement(sql);
        pstatement.setString(1, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        pstatement.setInt(2, duracionTotal);
        pstatement.setInt(3, setsP1);
        pstatement.setInt(4, setsP2);
        pstatement.setInt(5, ganador);
        pstatement.setInt(6, idPartido);
        pstatement.executeUpdate();
        
        System.out.println("Partido acabado y guardado");
    }
    
    //Cerramos la conexión:
    public void cerrarConexion(){
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión cerrada");
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}
