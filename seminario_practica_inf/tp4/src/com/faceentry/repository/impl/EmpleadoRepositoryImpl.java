package com.faceentry.repository.impl;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import com.faceentry.db.ConexionDB;
import com.faceentry.model.Empleado;
import com.faceentry.repository.interfaces.IEmpleadoRepository;

public class EmpleadoRepositoryImpl implements IEmpleadoRepository {
	private final ConexionDB connection;

	private static final Properties sqlProps = new Properties();

	static {
		try {
			sqlProps.load(
					EmpleadoRepositoryImpl.class.getClassLoader().getResourceAsStream("resources/sql.properties"));
		} catch (Exception e) {
			throw new RuntimeException("No se pudo cargar sql.properties", e);
		}
	}

	private String getSql(String key) {
		return sqlProps.getProperty(key);
	}

	public EmpleadoRepositoryImpl() {
		connection = new ConexionDB();

	}

	@Override
	public Empleado obtenerEmpleadoPorLegajo(String legajo) throws Exception {
		String sql = getSql("empleado.obtenerEmpleadoPorLegajo");
		try (PreparedStatement ps = connection.conectar().prepareStatement(sql)) {
			ps.setString(1, legajo);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return new Empleado(rs.getString("nombre"), 
						rs.getString("apellido"), 
						rs.getString("legajo"),
						rs.getString("dni"), 
						rs.getString("cargo"), 
						rs.getString("estado"),
						rs.getString("rutaImagenBiometrica"))	;
			}
		} finally {
			connection.desconectar();
		}
		return null;
	}

	@Override
	public void registrarEmpleado(Empleado empleado) throws Exception {
		String sql = getSql("empleado.registrarEmpleado");
		try (PreparedStatement ps = connection.conectar().prepareStatement(sql)) {
			ps.setString(1, empleado.getDni());
			ps.setString(2, empleado.getNombre());
			ps.setString(3, empleado.getApellido());
			ps.setString(4, empleado.getLegajo());
			ps.setString(5, empleado.getEstado());
			ps.setString(6, empleado.getCargo());
			ps.executeUpdate();
		} finally {
			connection.desconectar();
		}
	}

	@Override
	public void registrarEmpleado(Empleado empleado, String rutaImagen) throws Exception {
		this.registrarEmpleado(empleado);
		String sql = getSql("empleado.registrarEmpleadoConRuta");
		try (PreparedStatement ps = connection.conectar().prepareStatement(sql)) {
			ps.setString(1, empleado.getDni());
			ps.setString(2, empleado.getLegajo());
			ps.setString(3, rutaImagen);
			ps.executeUpdate();
		} finally {
			connection.desconectar();
		}

	}
	
	//Sobrecarga del metodo registrarEmpleado para que acepte llamarse con el legajo del jefe
	public void registrarEmpleado(Empleado empleado, String rutaImagen, String legajoFeje) throws Exception {
		String sql = getSql("empleado.registrarEmpleadoConFejeYConRuta");
		try (PreparedStatement ps = connection.conectar().prepareStatement(sql)) {
			ps.setString(1, empleado.getDni());
			ps.setString(2, empleado.getNombre());
			ps.setString(3, empleado.getApellido());
			ps.setString(4, empleado.getLegajo());
			ps.setString(5, empleado.getEstado());
			ps.setString(6, empleado.getCargo());
			ps.setString(7, legajoFeje);
			ps.executeUpdate();
		} finally {
			connection.desconectar();
		}
		sql = getSql("empleado.registrarEmpleadoConRuta");
		try (PreparedStatement ps = connection.conectar().prepareStatement(sql)) {
			ps.setString(1, empleado.getDni());
			ps.setString(2, empleado.getLegajo());
			ps.setString(3, "/"+rutaImagen);
			ps.executeUpdate();
		} finally {
			connection.desconectar();
		}
	}

	@Override
	public Map<String, ArrayList<?>> obtenerTodos() throws Exception {

		Map<String, ArrayList<?>> listado = new HashMap<>();
		ArrayList<String> nombresColumnas = new ArrayList<>();
		ArrayList<ArrayList<?>> datos = new ArrayList<>();

		String sql = getSql("empleado.obtenerTodos");
		try (Statement st = connection.conectar().createStatement()) {
			ResultSet rs = st.executeQuery(sql);
			ResultSetMetaData metaData = rs.getMetaData();
			int columnas = metaData.getColumnCount();
			for (int i = 1; i <= columnas; i++) {
				nombresColumnas.add(metaData.getColumnLabel(i).toUpperCase());
			}
			listado.put("nombre_columnas", nombresColumnas);

			while (rs.next()) {
				ArrayList<String> fila = new ArrayList<>();
				for (int i = 1; i <= columnas; i++) {
					if (rs.getObject(i) == null || rs.getObject(i) == "")
						fila.add(" " + "No asignado");
					else
						fila.add(" " + rs.getObject(i));
				}
				datos.add(fila);
			}
			listado.put("datos", datos);
		} finally {
			connection.desconectar();
		}
		return listado;
	}
	
	public ArrayList<Empleado> obtenerListaEmpleados() throws Exception {
		ArrayList<Empleado> empleados = new ArrayList<>();

		String sql = getSql("empleado.obtenerListaEmpleados");
		try (Statement st = connection.conectar().createStatement()) {
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				empleados.add(new Empleado(rs.getString("nombre"),
						rs.getString("apellido"),
						rs.getString("legajo"),
						rs.getString("dni"),
						rs.getString("cargo"),
						rs.getString("estado"),
						rs.getString("rutaImagenBiometrica")));
			}
		} finally {
			connection.desconectar();
		}
		return empleados;
	}

	@Override
	public String getRutaImagenBiometrica(Empleado empleado) throws Exception {
		String rutaImagen = "";
		Empleado emp = this.obtenerEmpleadoPorLegajo(empleado.getLegajo());
		String sql = getSql("empleado.getRutaImagenBiometrica");
		try (PreparedStatement ps = connection.conectar().prepareStatement(sql)) {
			ps.setString(1, emp.getDni());
			ps.setString(2, emp.getLegajo());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				rutaImagen = rs.getString("rutaImagenBiometrica");
			}
		} finally {
			connection.desconectar();
		}
		return rutaImagen;
	}
	
	public Empleado obtenerEmpleadoPorDNI(String dni) throws Exception{
        
		String sql = getSql("empleado.buscarPorDNI");
		try (PreparedStatement ps = connection.conectar().prepareStatement(sql)) {
			ps.setString(1, dni);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return new Empleado(rs.getString("nombre"), 
						rs.getString("apellido"), 
						rs.getString("legajo"),
						rs.getString("dni"), 
						rs.getString("cargo"), 
						rs.getString("estado"),
						rs.getString("rutaImagenBiometrica"));
			}
		} finally {
			connection.desconectar();
		}
		return null;
	}
	
	// Metodo que se utiliza para mostrar el listado de legajos, Nombres y Apellidos de los Jefes
	// Para luego asociar dicho empleado con su Jefe reutilizando el metodo obtenerTodos()
	public ArrayList<Empleado> obtenerListadoJefes() throws Exception {
		ArrayList<Empleado> listadoEmpleados = obtenerListaEmpleados();
		ArrayList<Empleado> listadoJefes = new ArrayList<>();
		listadoJefes = (ArrayList<Empleado>) listadoEmpleados.stream()
			.filter(empleado -> empleado.getCargo().contains("Jefe"))
			.collect(Collectors.toList());
		return listadoJefes;
	}
	

}