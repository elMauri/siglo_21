USE FACEENTRY;


-- Insertar empleados
INSERT INTO Empleado (id_jefe, dni, nombre, apellido, legajo, estado, cargo)
VALUES 
  (NULL, '29826734', 'Ana', 'Lopez', 'A1001', 'Activo', 'Jefa de Área'),
  (1, '32790298', 'Juan', 'Perez', 'A1002', 'Activo', 'Desarrollador'),
  (1, '28616213', 'Maria', 'Gomez', 'A1003', 'Activo', 'Tester'),
  (1, '33423439', 'Pedro', 'Martínez', 'A1004', 'Activo', 'Tester'),
  (NULL, '44223423', 'Enzo', 'Fernandez', 'A1005', 'Activo', 'Gerente RRHH'),
  (NULL, '27933200', 'Julian', 'Alvarez', 'A1006', 'Activo', 'Admin Linux');


-- Administradores
INSERT INTO Administrador (id_empleado, tipo, usuario, password)
VALUES 
  (5, 'RRHH', 'admin_sistema', 'admin123'),
  (6, 'Sistema', 'admin_rrhh', 'admin456');

-- Dispositivos
INSERT INTO DispositivoBiometrico (id_administrador, ubicacion, nombre, modelo, estado)
VALUES 
  (1, 'Entrada Principal', 'Dispositivo1', 'ModelX', 'Activo'),
  (1, 'Entrada Planta Baja', 'Dispositivo2', 'ModelY', 'Activo');

-- Fichajes
INSERT INTO Fichaje (id_empleado, fechaIngreso, fechaSalida)
VALUES 
  (2, '2025-04-15', '2025-05-14'),
  (3, '2025-04-15', NULL);

-- Alertas de acceso
INSERT INTO AlertaAcceso (id_administrador, fechaHora, descripcion, criticidad, tipo)
VALUES 
  (1, NOW(), 'Intento de acceso con rostro no reconocido', 'Alta', 'Desconocido');

-- Rutas de imágenes biométricas
INSERT INTO EmpleadoRepositorio (id_empleado, rutaImagenBiometrica)
VALUES 
  (2, '/imagenes/biometricas/empleado_2.jpg'),
  (3, '/imagenes/biometricas/empleado_3.jpg');