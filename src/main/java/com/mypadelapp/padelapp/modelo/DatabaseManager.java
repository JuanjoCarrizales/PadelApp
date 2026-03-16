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
            getJugadorPrincipal();
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
                partido_finalizado INTEGER DEFAULT 0         
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
                tiebreak INTEGER,
                FOREIGN KEY (id_partido) REFERENCES partidos(id)         
            )                     
        """;
        
        String sqlJugadores = """
            CREATE TABLE IF NOT EXISTS jugadores (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT,
                email TEXT UNIQUE,
                password_hash TEXT,
                creado_desde_app INTEGER,
                fecha_creacion TEXT
            )
        """;
        
        String sqlPartidoJugadores = """
            CREATE TABLE IF NOT EXISTS partido_jugadores (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                partido_id INTEGER NOT NULL,
                jugador_id INTEGER,
                pareja INTEGER NOT NULL,
                posicion INTEGER NOT NULL,
                FOREIGN KEY (partido_id) REFERENCES partidos(id),
                FOREIGN KEY (jugador_id) REFERENCES jugadores(id)
            )
        """;
        
        Statement sentencia = conexion.createStatement();
        sentencia.execute(sqlPartidos);
        sentencia.execute(sqlPuntos);
        sentencia.execute(sqlJugadores);
        sentencia.execute(sqlPartidoJugadores);
        sentencia.close();
        
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
            //Vinculación del jugador principal (yo) como la Pareja1, posición1:
            vincularJugadorPartido(idPartido, 1, 1, 1);
            //El resto de jugadores sin asignar:
            vincularJugadorPartido(idPartido, null, 1, 2);//Pareja1, posición2
            vincularJugadorPartido(idPartido, null, 2, 1);//Pareja2, posición1
            vincularJugadorPartido(idPartido, null, 2, 2);//Pareja2, posición2
            
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
    
    //Creación-verificación del jugador principal(yo):
    public int getJugadorPrincipal(){
        try {
            //Comprobamos si el jugador principal existe o no:
            Statement sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery("SELECT id FROM jugadores WHERE id = 1");
            
            if (resultado.next()){
                return 1; 
            }
            //Si no existe, creamos el jugador principal:
            String sql = "INSERT INTO jugadores (id, nombre, creado_desde_app, fecha_creacion)" +
                "VALUES (1, 'Jugador Principal', 1, ?)";
            PreparedStatement pstatement = conexion.prepareStatement(sql);
            pstatement.setString(1, java.time.LocalDateTime.now().toString());
            pstatement.executeUpdate();
            
            System.out.println("Jugador principal creado correctamente. ID: 1");
            return 1;
        } catch (SQLException e){
            System.err.println("Error al crear el jugador principal: " + e.getMessage());
            return 1;
        }
    }
    
    //Vinculación de un jugador a un partido:
    private void  vincularJugadorPartido(int partidoId, Integer jugadorId, int pareja, int posicion) throws SQLException {
        String sql = "INSERT INTO partido_jugadores (partido_id, jugador_id, pareja, posicion)" +
                     "VALUES (?, ?, ?, ?)";
        PreparedStatement pstatement = conexion.prepareStatement(sql);
        pstatement.setInt(1, partidoId);
        if (jugadorId != null) {
            pstatement.setInt(2, jugadorId);
        } else {
            pstatement.setNull(2, java.sql.Types.INTEGER);
        }
        pstatement.setInt(3, pareja);
        pstatement.setInt(4, posicion);
        pstatement.executeUpdate();
    }
    
    //Cerramos la conexión:
    public void cerrarConexion(){
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión cerrada");
            }
        } catch (SQLException e){
            System.out.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
    
    //Obtenemos el total de los partidos jugados:
    public int getTotalPartidos(){
        try {
            Statement sentencia = conexion.createStatement();
            ResultSet rs = sentencia.executeQuery("SELECT COUNT(*) FROM partidos WHERE partido_finalizado = 1");
            if (rs.next()){
                return rs.getInt(1);
            }
        } catch (SQLException e){
            System.err.println("Error al obtener el total de partidos: " + e.getMessage());
        }
        return 0;
    }
    
    //Estadísitcas del jugador principal (yo):
    //Total partidos jugados jugador1:
    public int getTotalPartidosJugador() {
        try {
            String sql = "SELECT COUNT(DISTINCT p.id) FROM partidos p " + 
                         "JOIN partido_jugadores pj ON p.id = pj.partido_id " +
                         "WHERE p.partido_finalizado = 1 AND pj.jugador_id = 1";
            Statement sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery(sql);
            if (resultado.next()) {
                return resultado.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el total de partidos: " + e.getMessage());
        }
        return 0;
    }
    
    //Total voctorias jugador1:
    public int getVictoriasJugador() {
         try {
            String sql = "SELECT COUNT(DISTINCT p.id) FROM partidos p " + 
                         "JOIN partido_jugadores pj ON p.id = pj.partido_id " +
                         "WHERE p.partido_finalizado = 1 AND pj.jugador_id = 1 AND pj.pareja = p.ganador";
            Statement sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery(sql);
            if (resultado.next()) {
                return resultado.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el total de victorias del jugador1: " + e.getMessage());
        }
        return 0;
    }
    
    //Total derrotas jugador1:
    public int getDerrotasJugador() {
         try {
            String sql = "SELECT COUNT(DISTINCT p.id) FROM partidos p " + 
                         "JOIN partido_jugadores pj ON p.id = pj.partido_id " +
                         "WHERE p.partido_finalizado = 1 AND pj.jugador_id = 1 AND pj.pareja != p.ganador";
            Statement sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery(sql);
            if (resultado.next()) {
                return resultado.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el total de derrotas del jugador1: " + e.getMessage());
        }
        return 0;
    }
    
    //Obtenemos Nº de victorias de la pareja 1:
    public int getVictoriasPareja1(){
        try {
            Statement sentencia = conexion.createStatement();
            ResultSet rs = sentencia.executeQuery("SELECT COUNT(*) FROM partidos WHERE partido_finalizado = 1 AND ganador = 1");
            if (rs.next()){
                return rs.getInt(1);
            }
        } catch (SQLException e){
            System.err.println("Error al obtener victorias de la pareja 1: " + e.getMessage());
        }
        return 0;   
    }
    
    //Obtenemos Nº de victorias de la pareja 2:
    public int getVictoriasPareja2(){
        try {
            Statement sentencia = conexion.createStatement();
            ResultSet rs = sentencia.executeQuery("SELECT COUNT(*) FROM partidos WHERE partido_finalizado = 1 AND ganador = 2");
            if (rs.next()){
                return rs.getInt(1);
            }
        } catch (SQLException e){
            System.err.println("Error al obtener victorias de la pareja 2: " + e.getMessage());
        }
        return 0;   
    }
    
    //Obtención de la duración media de los partidos:
    public int getDuracionMedia(){
        try {
            Statement sentencia = conexion.createStatement();
            ResultSet rs = sentencia.executeQuery("SELECT AVG(duracion_total) FROM partidos WHERE partido_finalizado");
            if (rs.next()){
                return rs.getInt(1);
            }
        } catch (SQLException e){
            System.err.println("Error al obtener duración media: " + e.getMessage());
        }
        return 0;   
    }
    
    //Obtenciónd del total de puntos jugados:
    public int getPuntosTotales(){
        try {
            Statement sentencia = conexion.createStatement();
            ResultSet rs = sentencia.executeQuery("SELECT COUNT(*) FROM puntos");
            if (rs.next()){
                return rs.getInt(1);
            }
        } catch (SQLException e){
            System.err.println("Error al obtener puntos totales: " + e.getMessage());
        }
        return 0;
    }
}