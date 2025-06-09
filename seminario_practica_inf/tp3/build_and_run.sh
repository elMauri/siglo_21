#!/bin/bash

# Configuración
JAR_NAME="FaceEntryApp.jar"
MAIN_CLASS="com.faceentry.app.FaceEntryApp"
SRC_DIR="src"
LIB_DIR="lib"
BIN_DIR="bin"
RES_DIR="$SRC_DIR/resources"
MYSQL_JAR="$LIB_DIR/mysql-connector-j-8.2.0.jar"

# 1. Limpiar compilación anterior
echo "# Limpiando bin/"
rm -rf "$BIN_DIR"
mkdir -p "$BIN_DIR"

# 2. Compilar los .java
echo "# Compilando código fuente..."
find "$SRC_DIR/com" -name "*.java" > sources.txt
javac -cp "$MYSQL_JAR" -d "$BIN_DIR" @sources.txt
rm sources.txt

# 3. Copiar recursos al bin
echo "# Copiando recursos..."
cp -r "$RES_DIR" "$BIN_DIR/"

# 4. Crear MANIFEST si no existe
if [ ! -f MANIFEST.MF ]; then
  echo "# Generando MANIFEST.MF..."
  echo "Manifest-Version: 1.0" > MANIFEST.MF
  echo "Main-Class: $MAIN_CLASS" >> MANIFEST.MF
  echo "" >> MANIFEST.MF
fi

# 5. Empaquetar el JAR
echo "# Empaquetando $JAR_NAME..."
jar cfm "$JAR_NAME" MANIFEST.MF -C "$BIN_DIR" .

# 6. Ejecutar el JAR
echo "# Ejecutando aplicación..."
#java -cp "$JAR_NAME:$MYSQL_JAR" -jar "$JAR_NAME"

java -cp "$JAR_NAME:$MYSQL_JAR" com.faceentry.app.FaceEntryApp
