package com.faceentry.repository.impl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.faceentry.db.ConexionDB;
import com.faceentry.model.Empleado;
import com.faceentry.repository.interfaces.IReporteRepository;

public class ReporteRepositoryImpl implements IReporteRepository {
	
	private final ConexionDB connection;
    
    private static final Properties sqlProps = new Properties();

    static {
        try {
            sqlProps.load(EmpleadoRepositoryImpl.class.getClassLoader().getResourceAsStream("resources/sql.properties"));
        } catch (Exception e) {
            throw new RuntimeException("No se pudo cargar sql.properties", e);
        }
    }

    private String getSql(String key) {
        return sqlProps.getProperty(key);
    }

    public ReporteRepositoryImpl() {
		connection = new ConexionDB();

    }
	
    @Override
    public Map<String, ArrayList<?>> obtenerReporteAsistencias() throws Exception {
    	
        Map<String, ArrayList<?>> reporte = new HashMap<>();
        ArrayList<String> nombresColumnas = new ArrayList<>();
        ArrayList<ArrayList<?>> datos = new ArrayList<>();
    
    	
    	String sql = getSql("reporte.obtenerReporteAsistencias");
    	try (Statement st = connection.conectar().createStatement()){
            ResultSet rs = st.executeQuery(sql);
    		ResultSetMetaData metaData = rs.getMetaData();
			int columnas = metaData.getColumnCount();
			for (int i = 1; i <= columnas; i++) {
				nombresColumnas.add(metaData.getColumnName(i).toUpperCase());
			}
			reporte.put("nombre_columnas", nombresColumnas);
			
			while (rs.next()) {
				ArrayList<String> fila = new ArrayList<>();
				for (int i = 1; i <= columnas; i++) {
					fila.add(" " + rs.getObject(i));
				}
				datos.add(fila);
			}
			reporte.put("datos", datos);
        }finally {
        	connection.desconectar();
        }
        return reporte;
    }


	
}
