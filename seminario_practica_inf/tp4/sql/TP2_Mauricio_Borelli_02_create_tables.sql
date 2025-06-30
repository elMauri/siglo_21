USE FACEENTRY;

-- Tabla recursiva Empleado
CREATE TABLE  Empleado (
  id_empleado INT AUTO_INCREMENT PRIMARY KEY,
  id_jefe INT,
  dni VARCHAR(20) NOT NULL,
  nombre VARCHAR(50),
  apellido VARCHAR(50),
  legajo VARCHAR(20),
  estado VARCHAR(20),
  cargo VARCHAR(50),
  FOREIGN KEY (id_jefe) REFERENCES Empleado(id_empleado)
);

-- Tabla Administrador
CREATE TABLE  Administrador (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_empleado INT,
  tipo ENUM('RRHH', 'Sistema') NOT NULL,
  usuario VARCHAR(50),
  password TEXT,
  FOREIGN KEY (id_empleado) REFERENCES Empleado(id_empleado)
);

-- Tabla Dispositivo Biometrico
CREATE TABLE  DispositivoBiometrico (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_administrador INT,
  ubicacion VARCHAR(100),
  nombre VARCHAR(50),
  modelo VARCHAR(50),
  estado VARCHAR(20),
  FOREIGN KEY (id_administrador) REFERENCES Administrador(id)
);

-- Tabla Fichaje
CREATE TABLE  Fichaje (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_empleado INT,
  fechaIngreso DATETIME,
  fechaSalida DATETIME,
  FOREIGN KEY (id_empleado) REFERENCES Empleado(id_empleado)
);

-- Tabla Alerta Acceso
CREATE TABLE  AlertaAcceso (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_administrador INT,
  fechaHora DATETIME,
  descripcion TEXT,
  criticidad VARCHAR(20),
  tipo VARCHAR(30),
  FOREIGN KEY (id_administrador) REFERENCES Administrador(id)
);

CREATE TABLE EmpleadoRepositorio (
  id_empleado INT PRIMARY KEY,
  rutaImagenBiometrica VARCHAR(255),
  FOREIGN KEY (id_empleado) REFERENCES Empleado(id_empleado)
);