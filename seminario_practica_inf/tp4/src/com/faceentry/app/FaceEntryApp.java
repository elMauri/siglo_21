package com.faceentry.app;
import java.net.ServerSocket;

import java.awt.Image;
import javax.swing.ImageIcon;

import com.faceentry.db.ConexionDB;
import com.faceentry.model.Empleado;
import com.faceentry.repository.impl.EmpleadoRepositoryImpl;
import com.faceentry.repository.impl.ReporteRepositoryImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.util.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class FaceEntryApp extends JFrame {
    private static FaceEntryApp instancia;
    private static ServerSocket lockSocket;
    
	private static final long serialVersionUID = 1L;
	private ConexionDB db = new ConexionDB();
	

    public static FaceEntryApp getInstance() {
        if (instancia == null) {
            instancia = new FaceEntryApp();
        }
        return instancia;
    }

    //Constructor de la Aplicación
	public FaceEntryApp() {
		super("FaceEntry - Sistema de Registro Biometrico de Empleados");
	    // Cambia el icono de la ventana y barra de tareas
	    Image icono = new ImageIcon(getClass().getClassLoader().getResource("resources/FaceEntryLogo.png")).getImage();
	    setIconImage(icono);
	    
	    // Cambia el icono del Dock en MacOS
        try {
            // Solo funciona en MacOS y Java 9+
            java.awt.Taskbar taskbar = java.awt.Taskbar.getTaskbar();
            taskbar.setIconImage(icono);
        } catch (Exception e) {
            // Ignorar si no está en MacOS o versión Java no soportada
        }
		try {
			// Testeo que funcione la conexión a la BBDD, si no se conecta a la BBD no abre
			// la aplicación
			db.conectar();
			db.desconectar();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage());
			this.dispose();
			System.exit(ABORT);
		}
		;
		setSize(640, 480);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Menu");
		
		JMenuItem activarFichaje = new JMenuItem("Activar dispotivo para registro de fichajes");
		activarFichaje.addActionListener(e -> {
			try {
				todoMenu();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		JMenuItem login = new JMenuItem("Login a la Plataforma");
		login.addActionListener(e -> {
			try {
				todoMenu();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		JMenuItem registrarUsuario = new JMenuItem("Registrar Usuario");
		registrarUsuario.addActionListener(e -> {
			try {
				todoMenu();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		

		JMenuItem registrarEmpleado = new JMenuItem("Registrar Empleado");
		registrarEmpleado.addActionListener(e -> {
			try {
				mostrarFormularioRegistrarEmpleado();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		JMenuItem listarEmpleados = new JMenuItem("Listar Empleados");
		listarEmpleados.addActionListener(e -> {
			try {
				listarEmpleados();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		JMenuItem reporte = new JMenuItem("Reporte de Asistencias");
		reporte.addActionListener(e -> {
			try {
				mostrarReporteAsistencias();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		JMenuItem salir = new JMenuItem("Salir");
		salir.addActionListener(e -> System.exit(0));

		menu.add(registrarEmpleado);
		menu.add(activarFichaje);
		menu.add(listarEmpleados);
		menu.add(reporte);
		menu.add(login);
		menu.add(registrarUsuario);
		menu.add(salir);
		
		menuBar.add(menu);

		setJMenuBar(OrdenoMenu(menuBar));
		cargarLogoInicio();
	}

	// Metodo que muestra el formulario para la registracion por primera vez
	// de un empleado y se carga la imagen biometrica desde un archivos de imagenes desde el file system
	// NOTA: En Produccion la imagen sería tomada por un dispositivo externo biometrico
	@SuppressWarnings("null")
	private void mostrarFormularioRegistrarEmpleado() throws Exception {
		JPanel contenedor = new JPanel(new GridLayout(3, 3)); // 3 filas, 3 columnas
		contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
		
		final File[] imagenSeleccionada = {null};

		// Cuadro de Imagen biométrica
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

		JLabel lblImagen = new JLabel("Foto biométrica:");
		lblImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelImagen.add(lblImagen);

		JButton btnCargarImagen = new JButton("Cargar Imagen");
		btnCargarImagen.setOpaque(false);
		btnCargarImagen.setContentAreaFilled(false); //También Elimino el borde
		btnCargarImagen.setBorderPainted(false);
		btnCargarImagen.setBackground(new Color(0, 0, 0, 0)); //Fondo transparente
		btnCargarImagen.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
		    fileChooser.setAcceptAllFileFilterUsed(false);
		    fileChooser.addChoosableFileFilter(
		        new javax.swing.filechooser.FileNameExtensionFilter(
		            "Imágenes (*.jpg, *.jpeg, *.png)", "jpg", "jpeg", "png"
		        )
		    );
		    int result = fileChooser.showOpenDialog(this);
		    if (result == JFileChooser.APPROVE_OPTION) {
		        File archivo = fileChooser.getSelectedFile();
		        imagenSeleccionada[0] = archivo; // Guarda la imagen seleccionada
		        ImageIcon imagen = new ImageIcon(archivo.getAbsolutePath());
		        int ancho = 150;
		        int alto = 150;
		        Image escalada = imagen.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
		    	imagenLabel.setFocusable(false); 
		        imagenLabel.setIcon(new ImageIcon(escalada));
		        imagenLabel.setText(null);
		        panelImagen.add(imagenLabel);
		        btnCargarImagen.setText(null);
		        btnCargarImagen.setFocusable(false);
		        
		    }
		});
		
		panelImagen.add(Box.createVerticalStrut(10));
		panelImagen.add(btnCargarImagen);

		contenedor.add(panelImagen);
		contenedor.add(Box.createVerticalStrut(20));

		JPanel linea = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel linea2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel linea3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel lblAsterisco = new JLabel("*");
		JLabel lblAsterisco2 = new JLabel("*");
		JLabel lblAsterisco3 = new JLabel("*");
		JLabel lblAsterisco4 = new JLabel("*");
		JLabel lblAsterisco5 = new JLabel("*");

		JLabel lblNombre = new JLabel("Nombre:");
		JTextField nombreField = new JTextField(10);

		JLabel lblApellido = new JLabel("Apellido:");
		JTextField apellidoField = new JTextField(10);

		JLabel lblDni = new JLabel("DNI:");
		JTextField dniField = new JTextField(10);
		
		JLabel lblLegajo = new JLabel("Legajo:");
		JTextField legajoField = new JTextField(10);

		JLabel lblCargo = new JLabel("Cargo:");
		String[] cargoOpciones = { "Empleado", "Jefe", "Administrador de Sistema", "Administrado RRHH" };
		JComboBox<String> cargoField = new JComboBox<>(cargoOpciones);

		JLabel lblEstado = new JLabel("Estado:");
		String[] estadoOpciones = { "Activo", "Inactivo" };
		JComboBox<String> estadoField = new JComboBox<>(estadoOpciones);
		
		JLabel lblJefes = new JLabel("Jefe del Area a cargo:");
		EmpleadoRepositoryImpl empRepoImpl = new EmpleadoRepositoryImpl();
		ArrayList<Empleado> listadoJefes = empRepoImpl.obtenerListadoJefes();
		String[] jefeOpciones = new String[(listadoJefes != null) ? listadoJefes.size() + 1 : 0];
		jefeOpciones[0] = "";
		if (listadoJefes != null && !listadoJefes.isEmpty()) {
		    for (int i = 0; i < listadoJefes.size(); i++) {
		        StringBuilder sb = new StringBuilder();
		        sb.append(listadoJefes.get(i).getLegajo());
		        sb.append(", ");
		        sb.append(listadoJefes.get(i).getApellido());
		        sb.append(" ");
		        sb.append(listadoJefes.get(i).getNombre());
		        jefeOpciones[i + 1] = sb.toString();
		    }
		}
		JComboBox<String> jefesField = new JComboBox<>(jefeOpciones);
		// Calcula el ancho del texto más largo
		int maxWidth = 0;
		FontMetrics fm = jefesField.getFontMetrics(jefesField.getFont());
		for (String s : jefeOpciones) {
		    if (s != null) {
		        int width = fm.stringWidth(s);
		        if (width > maxWidth) maxWidth = width;
		    }
		}
		// Sumar un margen extra para el botón desplegable
		maxWidth += 40;
		if (maxWidth <= 40)
			maxWidth = 40 * 2;
		jefesField.setPreferredSize(new Dimension(maxWidth, 25));

		linea.add(lblNombre);
		linea.add(lblAsterisco2);
		linea.add(nombreField);

		linea.add(lblApellido);
		linea.add(lblAsterisco3);
		linea.add(apellidoField);
		contenedor.add(linea);

		linea.add(lblDni);
		linea.add(lblAsterisco);
		linea.add(dniField);
		
		linea2.add(lblLegajo);
		linea2.add(lblAsterisco4);
		linea2.add(legajoField);

		linea2.add(lblCargo);
		linea2.add(lblAsterisco5);
		linea2.add(cargoField);

		linea2.add(lblEstado);
		linea2.add(estadoField);
		contenedor.add(linea2);
		
		linea3.add(lblJefes);
		linea3.add(jefesField);
		contenedor.add(linea3);

		this.add(contenedor);


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
				String jefe = (String) jefesField.getSelectedItem();
				
				String legajoJefe = jefe.split(",")[0];

				if (nombre.isEmpty() || apellido.isEmpty() || legajo.isEmpty() || dni.isEmpty() || cargo.isEmpty())
					throw new IllegalArgumentException("Todos los campos con (*) son obligatorios.");
				
				// Copiar la imagen seleccionada al destino
		        String rutaDestino = null;
		        if (imagenSeleccionada[0] != null) {
		            File carpetaDestino = new File("imagenes/biometricas");
		            if (!carpetaDestino.exists()) carpetaDestino.mkdirs();
		            String extension = imagenSeleccionada[0].getName().substring(imagenSeleccionada[0].getName().lastIndexOf('.'));
		            File destino = new File(carpetaDestino, legajo + extension);
		            java.nio.file.Files.copy(
		                imagenSeleccionada[0].toPath(),
		                destino.toPath(),
		                java.nio.file.StandardCopyOption.REPLACE_EXISTING
		            );
		            rutaDestino = destino.getPath();
		        }

				// Aca uso el constructor de Empleado que implementa de la clase Abstracta
				// Persona
				Empleado nuevoEmpleado = new Empleado(nombre, apellido, legajo, dni, cargo, estado);
				
				//Registro (inserto) el empleado que estoy tomando los datos llamando
				//al metodo registrarEmpleado de EmepleadoRepositoryImpl
				EmpleadoRepositoryImpl repo = new EmpleadoRepositoryImpl();
				if (jefe == null || jefe.isEmpty()) {
				    repo.registrarEmpleado(nuevoEmpleado, rutaDestino);
				} else {
				    repo.registrarEmpleado(nuevoEmpleado, rutaDestino, legajoJefe);
				}
				JOptionPane.showMessageDialog(this, "Empleado registrado correctamente.");
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
			}
		}
	}

	private void listarEmpleados() throws Exception {
		EmpleadoRepositoryImpl listaEmpleadosImpl = new EmpleadoRepositoryImpl();
		
		Map<String, ArrayList<?>> listado = listaEmpleadosImpl.obtenerTodos();
		mostrarListado(listado.get("datos"), listado.get("nombre_columnas"),"Listado de Empleados");

	}

	
	//Metodo que muestra por pantalla el reporte de Asistencia de aquellos
	//empleados que hayan sido fichados por el sistema
	private void mostrarReporteAsistencias() throws Exception {
		ReporteRepositoryImpl reporteRepImp = new ReporteRepositoryImpl();
		
		Map<String, ArrayList<?>> reporte = reporteRepImp.obtenerReporteAsistencias();		
		mostrarListado(reporte.get("datos"), reporte.get("nombre_columnas"),"Reporte de Asistencia");
	
	}
	

	// Metodo que carga el logo de la Aplicación al ingresar a la misma
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

	// Muestra información completa del Empleado
	public void mostrarInformacionEmpleado(JTable tabla) throws Exception {
		int columnaLegajo = -1;
	    for (int i = 0; i < tabla.getColumnCount(); i++) {
	        if ("Legajo".equalsIgnoreCase(tabla.getColumnName(i))) {
	            columnaLegajo = i;
	            break;
	        }
	    }
	    if (columnaLegajo == -1) {
	        JOptionPane.showMessageDialog(tabla, "No se encontró la columna Legajo.");
	        return;
	    }

	    // Obtener la fila seleccionada
	    int selectedRow = tabla.getSelectedRow();
	    if (selectedRow == -1) {
	        JOptionPane.showMessageDialog(tabla, "No hay fila seleccionada.");
	        return;
	    }

	    // Convertir a índice del modelo (por si hay ordenamiento)
	    int modelRow = tabla.convertRowIndexToModel(selectedRow);

	    // Obtener el valor del campo "Legajo"
	    Object legajoValue = tabla.getModel().getValueAt(modelRow, columnaLegajo);
	    String legajo = legajoValue != null ? legajoValue.toString().trim() : "";

	    try {
	        EmpleadoRepositoryImpl empRepoImpl = new EmpleadoRepositoryImpl();
	        Empleado emp = empRepoImpl.obtenerEmpleadoPorLegajo(legajo);
	        if (emp != null) {
	            // Panel principal con BoxLayout vertical
	            JPanel panelPrincipal = new JPanel();
	            panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));

	            // Panel de imagen biométrica centrado
	            String rutaImagen = emp.getRutaImagen();
	            // Obtengo la imagen biometrica a partir de la ruta
	            JPanel panelImage = getImagenBiometricFrame(rutaImagen);

	            // Agrego panel de imagen al panel principal
	            panelPrincipal.add(panelImage);

	            // Panel de datos del empleado
	            JPanel panelDatos = new JPanel(new GridLayout(0, 2, 10, 5));
	            panelDatos.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
	            panelDatos.add(new JLabel("Legajo:"));
	            panelDatos.add(new JLabel(emp.getLegajo()));
	            panelDatos.add(new JLabel("Nombre:"));
	            panelDatos.add(new JLabel(emp.getNombre()));
	            panelDatos.add(new JLabel("Apellido:"));
	            panelDatos.add(new JLabel(emp.getApellido()));
	            panelDatos.add(new JLabel("DNI:"));
	            panelDatos.add(new JLabel(emp.getDni()));
	            panelDatos.add(new JLabel("Cargo:"));
	            panelDatos.add(new JLabel(emp.getCargo()));
	            panelDatos.add(new JLabel("Estado:"));
	            panelDatos.add(new JLabel(emp.getEstado()));
	            
	            JPanel panelDatosCentrado = new JPanel(new FlowLayout(FlowLayout.CENTER));
	            panelDatosCentrado.add(panelDatos);

	            // Agrego panel de datos debajo de la imagen
	            panelPrincipal.add(panelDatosCentrado);

	            JOptionPane.showMessageDialog(tabla, panelPrincipal, "Detalle del Empleado", JOptionPane.PLAIN_MESSAGE, null);
	        }
	        db.desconectar();
	    } catch (SQLException ex) {
	        JOptionPane.showMessageDialog(tabla, "Error al obtener datos: " + ex.getMessage());
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	// Panel de imagen biométrica centrado
	public JPanel getImagenBiometricFrame(String rutaImagen) {
		int maxAncho = 200;
	    int maxAlto = 200;

	    JPanel panelImagen = new JPanel(new BorderLayout());
	    panelImagen.setPreferredSize(new Dimension(maxAncho, maxAlto));
	    panelImagen.setMaximumSize(new Dimension(maxAncho, maxAlto));
	    panelImagen.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

	    JLabel imagenLabel = new JLabel("Sin imagen", SwingConstants.CENTER);
	    imagenLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    imagenLabel.setVerticalAlignment(SwingConstants.CENTER);
	    imagenLabel.setPreferredSize(new Dimension(maxAncho, maxAlto));
	    imagenLabel.setMinimumSize(new Dimension(maxAncho, maxAlto));
	    imagenLabel.setMaximumSize(new Dimension(maxAncho, maxAlto));
	    imagenLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2)); // Borde visible

	    if (rutaImagen != null && !rutaImagen.isEmpty()) {
	        File archivo = new File("." + rutaImagen);
	        if (archivo.exists()) {
	            ImageIcon iconoOriginal = new ImageIcon(archivo.getAbsolutePath());
	            int imgAncho = iconoOriginal.getIconWidth();
	            int imgAlto = iconoOriginal.getIconHeight();

	            // Calcula el escalado manteniendo la relación de aspecto
	            double escala = Math.min((double)maxAncho / imgAncho, (double)maxAlto / imgAlto);
	            int anchoEscalado = (int)(imgAncho * escala);
	            int altoEscalado = (int)(imgAlto * escala);

	            Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(anchoEscalado, altoEscalado, Image.SCALE_SMOOTH);
	            imagenLabel.setIcon(new ImageIcon(imagenEscalada));
	            imagenLabel.setText(null); // Solo la imagen
	        } else {
	            imagenLabel.setIcon(null);
	            imagenLabel.setText("Imagen no encontrada");
	        }
	    } else {
	        imagenLabel.setIcon(null);
	        imagenLabel.setText("Sin imagen");
	    }
	    panelImagen.add(imagenLabel, BorderLayout.CENTER);

	    // Panel auxiliar para el salto de Linea y el label descriptivo
	    JPanel panelSur = new JPanel();
	    panelSur.setLayout(new BoxLayout(panelSur, BoxLayout.Y_AXIS));
	    panelSur.add(Box.createVerticalStrut(10)); // Salto de Linea (10 píxeles)
	    JLabel labelDescripcion = new JLabel("Imagen biométrica", SwingConstants.CENTER);
	    labelDescripcion.setAlignmentX(Component.CENTER_ALIGNMENT);
	    panelSur.add(labelDescripcion);

	    panelImagen.add(panelSur, BorderLayout.SOUTH);

	    return panelImagen;		
	}
	
	// Valido si ya esta corriendo la aplicacion reservando un socket
    public static boolean isAlreadyRunning(int port) {
        try {
        	lockSocket = new ServerSocket(port);
            return false;
        } catch (IOException e) {
            return true;
        }
    }
    
    /**
     * Renderiza un listado de empleados de cualquier tipo
     * @param arrayList datos a renderizar 
     * @param nombresColumnas nombres de columanas a mostrar 
     * @param tituloListado el titulo que mostrará la ventana  
     */
    public void mostrarListado (ArrayList<?> datos, ArrayList<?> nombreColumnas, String titulo){
    
        JFrame ventana = new JFrame(titulo);
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.setSize(800, 400);
        ventana.setLocationRelativeTo(null);

        JPanel panelReporte = new JPanel(new BorderLayout());
        panelReporte.setPreferredSize(new Dimension(640, 480));

        // Convertir datos a Object[][] y Agrego columna Acciones
        Object[][] datosArray = datos.stream()
            .map(fila -> {
                ArrayList<?> filaList = (ArrayList<?>) fila;
                Object[] arr = new Object[filaList.size() + 1];
                for (int i = 0; i < filaList.size(); i++) arr[i] = filaList.get(i);
                arr[filaList.size()] = "Acciones"; // Placeholder
                return arr;
            })
            .toArray(Object[][]::new);

        // Agrego columna Acciones
        ArrayList<Object> columnasConAcciones = new ArrayList<>(nombreColumnas);
        columnasConAcciones.add("Acciones");
        Object[] columnasArray = columnasConAcciones.toArray();
        
        DefaultTableModel modelo = new DefaultTableModel(datosArray, columnasArray) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                // Solo la columna de acciones es editable
                return column == getColumnCount() - 1;
            }
        };
        // JTable personalizada para tooltips en iconos de acciones
        JTable tabla = new JTable(modelo) {
            @Override
            public String getToolTipText(java.awt.event.MouseEvent e) {
                java.awt.Point p = e.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);
                if (convertColumnIndexToModel(colIndex) == getColumn("Acciones").getModelIndex()) {
                    int x = p.x - getCellRect(rowIndex, colIndex, false).x;
                    // Suponiendo dos iconos de 20x20 con 5px de separación y centrados en 32px de alto
                    if (x < 32) return "Editar";
                    if (x >= 32 && x < 64) return "Eliminar";
                }
                return super.getToolTipText(e);
            }
        };
        tabla.setRowHeight(32);
        tabla.setShowVerticalLines(true);
        tabla.setShowHorizontalLines(false);
        tabla.setGridColor(new Color(180, 180, 180));
        tabla.setIntercellSpacing(new Dimension(0, 0));

        // Renderer general para sombreado alternado y Linea divisoria
        DefaultTableCellRenderer stripedRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(245,245,245));
                    }
                } else {
                    c.setBackground(table.getSelectionBackground());
                }
                if (c instanceof JLabel) {
                    ((JLabel)c).setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200,200,200)));
                }
                return c;
            }
        };
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            if (!tabla.getColumnName(i).equals("Acciones")) {
                tabla.getColumnModel().getColumn(i).setCellRenderer(stripedRenderer);
            }
        }
        // Renderer para los iconos de acciones, centrados verticalmente y con Linea divisoria y sombreado
        tabla.getColumn("Acciones").setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JPanel panel = new JPanel(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridy = 0;
                gbc.gridx = 0;
                gbc.insets = new java.awt.Insets(0, 0, 0, 5);
                // Editar
                ImageIcon editIcon = new ImageIcon(getClass().getClassLoader().getResource("resources/edit.png"));
                Image editImg = editIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                JLabel lblEditar = new JLabel(new ImageIcon(editImg));
                panel.add(lblEditar, gbc);
                // Eliminor
                gbc.gridx = 1;
                ImageIcon deleteIcon = new ImageIcon(getClass().getClassLoader().getResource("resources/delete.png"));
                Image deleteImg = deleteIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                JLabel lblEliminor = new JLabel(new ImageIcon(deleteImg));
                panel.add(lblEliminor, gbc);
                // Sombreado alternado
                if (!isSelected) {
                    if (row % 2 == 0) {
                        panel.setBackground(Color.WHITE);
                    } else {
                        panel.setBackground(new Color(245,245,245));
                    }
                } else {
                    panel.setBackground(table.getSelectionBackground());
                }
                // Linea divisoria inferior
                panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200,200,200)));
                panel.setOpaque(true);
                return panel;
            }
        });

        // Editor para los botones de acciones
        tabla.getColumn("Acciones").setCellEditor(new AccionesCellEditor(tabla));

        // Se instancia un listener del mouse para que al hacer doble click
    		// sobre algun empleado abra una ficha mostrando la informacion del
    		// mismo incluyendo su foto biometrica
    		tabla.addMouseListener(new java.awt.event.MouseAdapter() {
    			public void mouseClicked(java.awt.event.MouseEvent evt) {
    				if (evt.getClickCount() == 2 && tabla.getSelectedRow() != -1) {
    					try {
							mostrarInformacionEmpleado(tabla);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    				}
    			}
    		});

    		// Estética del encabezado con Lineas verticales
    		JTableHeader header = tabla.getTableHeader();
    		header.setPreferredSize(new Dimension(header.getWidth(), 28));
    		header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
    		header.setDefaultRenderer(new DefaultTableCellRenderer() {
    			private static final long serialVersionUID = 1L;

    			@Override
    			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
    					boolean hasFocus, int row, int column) {
    				JLabel label = new JLabel(value.toString(), JLabel.CENTER);
    				label.setOpaque(true);
    				label.setBackground(new Color(230, 230, 230));
    				label.setForeground(Color.BLACK);
    				label.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));
    				return label;
    			}
    		});

    		TableRowSorter<TableModel> sorter = new TableRowSorter<>(modelo);
    		tabla.setRowSorter(sorter);

    		JScrollPane scroll = new JScrollPane(tabla);
    		panelReporte.add(scroll, BorderLayout.CENTER);

    		ventana.add(panelReporte);
    		ventana.setVisible(true);
    	}
    
    // Metodo generico para mostrar por pantalla un Panel faltante
    public void todoMenu(){
    	JOptionPane.showMessageDialog(this, "Funcionalidad pendiente de Implementar");
    }


	// Clase interna para el editor de la columna Acciones
    private class AccionesCellEditor extends AbstractCellEditor implements javax.swing.table.TableCellEditor {
        private final JPanel panel;
        private final JButton btnEditar;
        private final JButton btnEliminor;
        private int editingRow;
        private JTable tabla;

        public AccionesCellEditor(JTable tabla) {
            this.tabla = tabla;
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            btnEditar = new JButton();
            btnEliminor = new JButton();
            btnEditar.setPreferredSize(new Dimension(32, 32));
            btnEliminor.setPreferredSize(new Dimension(32, 32));
            btnEditar.setFocusable(false);
            btnEliminor.setFocusable(false);
            // Escalar iconos a 24x24
            ImageIcon editIcon = new ImageIcon(getClass().getClassLoader().getResource("resources/edit.png"));
            ImageIcon deleteIcon = new ImageIcon(getClass().getClassLoader().getResource("resources/delete.png"));
            Image editImg = editIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            Image deleteImg = deleteIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            btnEditar.setIcon(new ImageIcon(editImg));
            btnEliminor.setIcon(new ImageIcon(deleteImg));
            btnEditar.setToolTipText("Editar");
            btnEliminor.setToolTipText("Eliminor");
            btnEditar.setText(null);
            btnEliminor.setText(null);
            btnEditar.addActionListener(e -> {
                fireEditingStopped();
                JOptionPane.showMessageDialog(tabla, "Editar fila " + editingRow);

            });
            btnEliminor.addActionListener(e -> {
                fireEditingStopped();
                int confirm = JOptionPane.showConfirmDialog(tabla, "¿Seguro que desea Eliminor esta fila?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    ((DefaultTableModel)tabla.getModel()).removeRow(editingRow);

                }
            });
            panel.add(btnEditar);
            panel.add(btnEliminor);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            editingRow = row;
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "Acciones";
        }
    }
    
    // Metodo que ordena los items del menu
    public static JMenuBar OrdenoMenu(JMenuBar menuBar) {
        // Obtener los menús del JMenuBar y almacenarlos en un arreglo
        int menuCount = menuBar.getMenuCount();
        JMenu[] menus = new JMenu[menuCount];
        for (int i = 0; i < menuCount; i++) {
            menus[i] = menuBar.getMenu(i);
        }
        // Ordeno el arreglo de menús alfabéticamente por el texto del menú
        Arrays.sort(menus, Comparator.comparing(JMenu::getText, String.CASE_INSENSITIVE_ORDER));

        // 3. Para cada menú, Ordeno sus JMenuItems alfabéticamente (excepto "Salir" al final)
        for (JMenu menu : menus) {
            List<Component> items = new ArrayList<>();
            JMenuItem salir = null;
            for (int j = 0; j < menu.getItemCount(); j++) {
                JMenuItem item = menu.getItem(j);
                if (item != null) {
                    if (item.getText() != null && item.getText().trim().equalsIgnoreCase("Salir")) {
                        salir = item;
                    } else {
                        items.add(item);
                    }
                }
            }
            // Ordeno los items alfabéticamente
            items.sort(Comparator.comparing(c -> ((JMenuItem)c).getText(), String.CASE_INSENSITIVE_ORDER));
            menu.removeAll();
            for (Component c : items) {
                menu.add(c);
            }
            if (salir != null) {
                menu.addSeparator();
                menu.add(salir);
            }
        }
        // Elimino todos los menús existentes del JMenuBar
        menuBar.removeAll();
        // Agrego los menús ordenados al JMenuBar
        for (JMenu menu : menus) {
            menuBar.add(menu);
        }
        // Revalidar y repintar el JMenuBar para mostrar los cambios
        return menuBar;
    }
    
	//Inicio/Main de la aplicacion
	public static void main(String[] args) {
	//		SwingUtilities.invokeLater(() -> new FaceEntryApp().setVisible(true));
	//	}
		// Usa un puerto alto poco probable de estar ocupado
		if (isAlreadyRunning(65432)) {
			JOptionPane.showMessageDialog(null, "La aplicación ya está en ejecución.", "Atención", JOptionPane.WARNING_MESSAGE);
			System.exit(0);
		}
		SwingUtilities.invokeLater(() -> FaceEntryApp.getInstance().setVisible(true));
	}
}
