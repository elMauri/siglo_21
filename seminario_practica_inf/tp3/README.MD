## Seminario de Práctica de Informática
#### Trabajo Práctico Nro 3
##### Titular Experto: HUGO FERNANDO FRIAS
##### Profesor Titular Disciplinar: PABLO ALEJANDRO VIRGOLINI
##### Alumno: Mauricio David Borelli
##### Fecha de Entrega: 08/06/2025
##### DNI: 28.916.713
<br>

## Proyecto

#### FaceEntry
Sistema inteligente de control de asistencia y acceso que utiliza reconocimiento facial para registrar y gestionar la presencia del personal en tiempo real.
<br>


## Tabla de contenido

* [Creación de la Base de Datos](#creación-base-de-datos)
* [Instalación Scripts](#instalación-scripts-sql)
* [Aplicación Java](#aplicación-java)
<br>


## Creación Base de datos

La creación de la base de datos se realizó utilizando una imagen docker de MySql.

Para levantar dicha imagen se ejecutó el siguiente comando:
```
docker run --name some-mysql -e MYSQL_ROOT_PASSWORD=<una_password> -d mysql:latest
```

- Donde **`some-mysql`** corresponde a un nombre cualquiera que le querramos asignar al contenedor que contendrá la imagen de la base de datos MySQL corriendo

- Donde **`<una_password>`** corresponde a una password que le querramos asignar al usuario root de la base de datos
<br>

## Instalación Scripts SQL

Para la creación y poblado del modelo de datos se dejen ejecutar los scripts en el siguiente orden:

1. `TP2_Mauricio_Borelli_01_create_database.sql`
2. `TP2_Mauricio_Borelli_02_create_tables.sql`
3. `TP2_Mauricio_Borelli_03_insert_data.sql`



##### <u>NOTA</u>: HASTA ACA CORRESPONDE AL TRABAJO PRACTICO NRO 2, YA QUE ES NECESARIO PARA QUE FUNCIONE LA APP DEL TRABAJO PRACTICO NRO 3

*********************************************************************************************************************
## Aplicación Java



###### INSTALACION Y EJECUCION DE LA APLICACION:

1. **Pre-requisitos**:
Haber ejecutado la primera parte correspondiente a MySQL y scripts de base de datos, ya que forman parte del modelo y de los datos necesarios para 
<br>
2. **Descarga del código**:
Para descargar el codigo necesario para su correcto funcionamiento, es necesario clonar este repositorio desde github:

https://github.com/elMauri/siglo_21.git

con el comando:

`git clone https://github.com/elMauri/siglo_21.git`

Una vez descargado, ubicarse en el directorio:

`seminario_practica_inf/tp3`
<br>
3. **Configuración**:
Para que la aplicación funcione, además, es necesario configurar los datos de conexión a la BBDD mencionada. Eso se realiza desde el archivo config.properties ubicado en src/resources debiendo modificar las siguientes keys:

`db.host=127.0.0.1` <- Ingresar la ip de donde corre la BBDD
`db.port=3306` <- ingresar el puerto donde corre la BBDD
`db.name=FACEENTRY` <- dejarlo tal cual
`db.user=<name>` <- completar con el nombre de usuario con el que se corrieron los scripts sql
`db.password=<password>` <- completar con la password del usuario con el que se corrieron los scripts sql
<br>
4. **Compilación**:
La Interfaz de usuario de dicha la aplicación esta hecha en Java Swing y al conectarse con la BBDD Mysql fue necesario agregar entre el codigo fuente el jar correspondiente a los drivers. Dicho jar se encuentra ubicado en `tp3/lib`.
Finalmente para compilar será necesario estar ubicado en el directorio:
<br>
    `~/seminario_practica_inf/tp3`
<br>
    Y ejecutar desde linea de comandos:
    <br>
    a)	Para obtener el listado de todos los archivos .java necesarios:
`find src/com -name "*.java" > sources.txt`
b)	Para compilar:
`javac -cp "lib/mysql-connector-j-8.2.0.jar" -d bin @sources.txt`
c)	Copiar el driver en el jar de destino:
`cp -r " src/resources bin`
d)	Crear MANIFEST si no existe
```
if [ ! -f MANIFEST.MF ]; then
echo "# Generando MANIFEST.MF..."
echo "Manifest-Version: 1.0" > MANIFEST.MF
echo "Main-Class: $MAIN_CLASS" >> MANIFEST.MF
echo "" >> MANIFEST.MF
fi 
```   
e)	Para empaquetar el JAR:
`jar cfm "FaceEntryApp.jar" MANIFEST.MF -C “./bin” .`
<br>
5. **Ejecución**:
Luego de ejecutar el paso anterior, para correr la aplicación será suficiente con ejecutar el siguiente comando:
`java -cp "FaceEntry.jar:/lib/ mysql-connector-j-8.2.0.jar" com.faceentry.app.FaceEntryApp`
<br>
    <u>**NOTA**</u>: PARA SIMPLIFICAR SU COMPILCACION Y EJECUCION SE DEJA UN SCRIPT BASH DE NOMBRE: `build_and_run.sh`  QUE EJECUTA LOS PASOS DEL 4 AL 5 PARA SU AUTOMATIZACIÓN Y SIMPLIFICACIÓN.
<br>
    Basta con ejecutar:
    `./build_and_run.sh`
    <br>
    A SU VEZ SE DEJA LA CARPETA `sql` EN ESTE DIRECTORIO PARA FACILITAR SU UBICACION.
