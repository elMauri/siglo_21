package com.faceentry.app;

import com.faceentry.modelo.Empleado;
import com.faceentry.db.ConexionDB;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.util.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class FaceEntryApp extends JFrame {
	private static final long serialVersionUID = 1L;
	private Map<String, Empleado> empleados = new HashMap<>();
	private ConexionDB db = new ConexionDB();

	public FaceEntryApp() {
		super("FaceEntry - Sistema de Registro Biometrico de Empleados");
		try {
			// Testeo que funcione la conexión a la BBDD, si no se conecta a la BBD no abre la aplicación
			db.conectar();
			db.desconectar();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
			this.dispose();
		}
		;
		setSize(640, 480);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Menu");

		JMenuItem registrar = new JMenuItem("Registrar Empleado");
		registrar.addActionListener(e -> mostrarFormularioEmpleado());

		JMenuItem fichar = new JMenuItem("Fichar");
		fichar.addActionListener(e -> registrarFichaje());

		JMenuItem reporte = new JMenuItem("Ver Reporte de Asistencias");
		reporte.addActionListener(e -> {
			try {
				mostrarReporte();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		JMenuItem salir = new JMenuItem("Salir");
		salir.addActionListener(e -> System.exit(0));

		menu.add(registrar);
		menu.add(fichar);
		menu.add(reporte);
		menu.add(salir);
		menuBar.add(menu);
		
		setJMenuBar(menuBar);
		cargarLogoInicio();
	}

	private void mostrarFormularioEmpleado() {
		JPanel contenedor = new JPanel(new GridLayout(2, 3)); // 2 filas, 3 columnas
		contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));

		// Carga de Imagen biométrica
		JPanel panelImagen = new JPanel();
//	    panelImagen.setLayout(new BoxLayout(panelImagen, BoxLayout.Y_AXIS));
		panelImagen.setPreferredSize(new Dimension(150, 150));
		panelImagen.setMaximumSize(new Dimension(150, 150));
		panelImagen.setMinimumSize(new Dimension(150, 150));
		panelImagen.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		panelImagen.setLayout(new BorderLayout());

		JLabel imagenLabel = new JLabel("Sin imagen", SwingConstants.CENTER);
		imagenLabel.setHorizontalAlignment(SwingConstants.CENTER);
		imagenLabel.setVerticalAlignment(SwingConstants.CENTER);
//	    
//	    imagenLabel.setPreferredSize(new Dimension(100, 100));
//	    imagenLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
//	    imagenLabel.setHorizontalAlignment(JLabel.CENTER);
//	    imagenLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelImagen.add(imagenLabel);
		panelImagen.add(Box.createVerticalStrut(20));

		JLabel lblImagen = new JLabel("Foto biométrica:");
		lblImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelImagen.add(lblImagen);

		JButton btnCargarImagen = new JButton("Cargar Imagen");
		btnCargarImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnCargarImagen.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			int result = fileChooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				File archivo = fileChooser.getSelectedFile();
				ImageIcon imagen = new ImageIcon(archivo.getAbsolutePath());
				Image escalada = imagen.getImage().getScaledInstance(imagenLabel.getWidth(), imagenLabel.getHeight(),
						Image.SCALE_SMOOTH);
				imagenLabel.setIcon(new ImageIcon(escalada));
				imagenLabel.setText(null); // Oculta texto
			}
		});
		panelImagen.add(Box.createVerticalStrut(10));
		panelImagen.add(btnCargarImagen);

		contenedor.add(panelImagen);
		contenedor.add(Box.createVerticalStrut(20));

		JPanel linea = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel linea2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel lblAsterisco = new JLabel("*");
		JLabel lblAsterisco2 = new JLabel("*");
		JLabel lblAsterisco3 = new JLabel("*");
		JLabel lblAsterisco4 = new JLabel("*");
		JLabel lblAsterisco5 = new JLabel("*");

		JLabel lblDni = new JLabel("DNI:");
		JTextField dniField = new JTextField(10);

		JLabel lblNombre = new JLabel("Nombre:");
		JTextField nombreField = new JTextField(10);

		JLabel lblApellido = new JLabel("Apellido:");
		JTextField apellidoField = new JTextField(10);

		JLabel lblLegajo = new JLabel("Legajo:");
		JTextField legajoField = new JTextField(10);

		JLabel lblCargo = new JLabel("Cargo:");
		String[] cargoOpciones = { "Empleado", "Jefe", "Administrador de Sistema", "Administrado RRHH" };
		JComboBox<String> cargoField = new JComboBox<>(cargoOpciones);

		JLabel lblEstado = new JLabel("Estado:");
		String[] estadoOpciones = { "Activo", "Inactivo" };
		JComboBox<String> estadoField = new JComboBox<>(estadoOpciones);

		linea.add(lblDni);
		linea.add(lblAsterisco);
		linea.add(dniField);

		linea.add(lblNombre);
		linea.add(lblAsterisco2);
		linea.add(nombreField);

		linea.add(lblApellido);
		linea.add(lblAsterisco3);
		linea.add(apellidoField);
		contenedor.add(linea);

		linea2.add(lblLegajo);
		linea2.add(lblAsterisco4);
		linea2.add(legajoField);

		linea2.add(lblCargo);
		linea2.add(lblAsterisco5);
		linea2.add(cargoField);

		linea2.add(lblEstado);
		linea2.add(estadoField);
		contenedor.add(linea2);

		this.add(contenedor);

		Object[] campos = { "Nombre:", nombreField, "Apellido:", apellidoField, "Legajo:", legajoField, "DNI:",
				dniField, "Estado:", estadoField, "Cargo:", cargoField, };

		int option = JOptionPane.showConfirmDialog(this, contenedor, "Registrar Empleado",
				JOptionPane.OK_CANCEL_OPTION);
		if (option == JOptionPane.OK_OPTION) {
			try {
				String nombre = nombreField.getText();
				String apellido = apellidoField.getText();
				String legajo = legajoField.getText();
				String dni = dniField.getText();
				String cargo = (String) cargoField.getSelectedItem();
				String estado = (String) estadoField.getSelectedItem();

				if (nombre.isEmpty() || apellido.isEmpty() || legajo.isEmpty() || dni.isEmpty() || cargo.isEmpty())
					throw new IllegalArgumentException("Todos los campos con (*) son obligatorios.");

				// Aca uso el constructor de Empleado que implementa de la clase Abstracta
				// Persona
				Empleado emp = new Empleado(nombre, apellido, legajo, dni, cargo, estado);
				empleados.put(legajo, emp);

				PreparedStatement stmt = db.conectar().prepareStatement(
						"INSERT INTO empleado (dni, nombre, apellido, legajo, estado, cargo) VALUES (?, ?, ?, ?, ?, ?)");
				stmt.setString(1, dni);
				stmt.setString(2, nombre);
				stmt.setString(3, apellido);
				stmt.setString(4, legajo);
				stmt.setString(5, estado);
				stmt.setString(6, cargo);
				stmt.executeUpdate();
				db.desconectar();
				JOptionPane.showMessageDialog(this, "Empleado registrado correctamente.");
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
			}
		}
	}

	private void registrarFichaje() {
		JOptionPane.showMessageDialog(this, "Funcionalidad pendiente de Implementar");
	}

	
	
	private void mostrarReporte() throws SQLException {
	    JFrame ventana = new JFrame("Reporte de Asistencia");
	    ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    ventana.setSize(800, 400);
	    ventana.setLocationRelativeTo(null);

	    JPanel panelReporte = new JPanel(new BorderLayout());
	    panelReporte.setPreferredSize(new Dimension(640, 480));

	    String consulta = """
			SELECT CONCAT(e.apellido, ', ', e.nombre) AS empleado, 
			    DATE_FORMAT(f.fechaIngreso, '%d/%m/%Y') AS fecha,
			    e.legajo,
			    SUM(TIMESTAMPDIFF(HOUR, f.fechaIngreso, f.fechaSalida)) AS totalHoras
			FROM Fichaje f
			JOIN Empleado e ON f.id_empleado = e.id_empleado
			WHERE YEAR(f.fechaIngreso) = YEAR(CURDATE())
			GROUP BY empleado, fecha, e.legajo
			ORDER BY e.legajo ASC, fecha;
	    """;

	    Statement stmt;
	    Vector<String> nombresColumnas = new Vector<>();
	    Vector<Vector<Object>> datos = new Vector<>();
	    
		try {
			stmt = db.conectar().createStatement();
			ResultSet rs = stmt.executeQuery(consulta);
		    ResultSetMetaData metaData = rs.getMetaData();
		    int columnas = metaData.getColumnCount();

		    
		    for (int i = 1; i <= columnas; i++) {
		        nombresColumnas.add(metaData.getColumnName(i).toUpperCase());
		    }

		    
		    while (rs.next()) {
		        Vector<Object> fila = new Vector<>();
		        for (int i = 1; i <= columnas; i++) {
		            fila.add(" "+ rs.getObject(i));
		        }
		        datos.add(fila);
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(ABORT);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(ABORT);
		}


	    JTable tabla = new JTable(new DefaultTableModel(datos, nombresColumnas));
        tabla.setRowHeight(28);
        tabla.setShowVerticalLines(true);
        tabla.setShowHorizontalLines(false);
        tabla.setGridColor(new Color(180, 180, 180));
//        tabla.setShowGrid(false);
        tabla.setIntercellSpacing(new Dimension(0, 0));

        // Línea horizontal inferior entre filas
        tabla.setDefaultRenderer(Object.class, (TableCellRenderer) new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(180, 180, 180)));
                return c;
            }
        });
        
        // Estética del encabezado con líneas verticales
        JTableHeader header = tabla.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 28));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel(value.toString(), JLabel.CENTER);
                label.setOpaque(true);
                label.setBackground(new Color(230, 230, 230));
                label.setForeground(Color.BLACK);
                label.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));
                return label;
            }
        });
        
        
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(new DefaultTableModel(datos, nombresColumnas));
        tabla.setRowSorter(sorter);

	    JScrollPane scroll = new JScrollPane(tabla);
	    panelReporte.add(scroll, BorderLayout.CENTER);

	    ventana.add(panelReporte);
	    ventana.setVisible(true);

	    db.desconectar();
	}

	public void cargarLogoInicio() {
		ImageIcon logo = new ImageIcon(getClass().getClassLoader().getResource("resources/FaceEntryLogo.png"));

		// Escalar imagen si es necesario
		Image imagenEscalada = logo.getImage().getScaledInstance(400, 440, Image.SCALE_SMOOTH);
		JLabel labelLogo = new JLabel(new ImageIcon(imagenEscalada));

		// Centrar imagen en el frame
		labelLogo.setHorizontalAlignment(SwingConstants.CENTER);
		labelLogo.setVerticalAlignment(SwingConstants.CENTER);

		// Panel principal con BorderLayout
		setLayout(new BorderLayout());
		this.add(labelLogo, BorderLayout.CENTER);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new FaceEntryApp().setVisible(true));
	}
}
