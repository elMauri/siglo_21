package com.faceentry.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionDB {



    private Connection conexion;

    public Connection conectar() throws IOException, SQLException {
    	Properties props = new Properties();
        InputStream input = getClass().getClassLoader().getResourceAsStream("resources/config.properties");

        if (input == null) {
            throw new RuntimeException("Archivo config.properties no encontrado");
        }

        props.load(input);

        String host = props.getProperty("db.host");
        String port = props.getProperty("db.port");
        String db = props.getProperty("db.name");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");

        String url = "jdbc:mysql://" + host + ":" + port + "/" + db;

        try {
            if (conexion == null || conexion.isClosed()) {
                conexion = DriverManager.getConnection(url, user, password);
                System.out.println("Conexión exitosa a la base de datos.");
            }
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            throw new SQLException("Error al conectar a la base de datos, "
            		+ "por favor verifique que no haya problemas de conectividad hacia la misma:\n\n Error: " + e.getMessage().split("\\n")[0]);
        }
        return conexion;
    }

    public void desconectar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión cerrada correctamente.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }

    public Connection getConexion() {
        return conexion;
    }
}