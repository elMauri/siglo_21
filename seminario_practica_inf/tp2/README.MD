## Seminario de Práctica de Informática
### Trabajo Práctico Nro 2

##### Titular Experto: HUGO FERNANDO FRIAS
##### Profesor Titular Disciplinar: PABLO ALEJANDRO VIRGOLINI
##### Alumno: Mauricio David Borelli
##### Fecha de Entrega: 18/05/2025
##### DNI: 28.916.713

## Proyecto 

##### FACE-ENTRY

## Tabla de contenido

* [Creación de la Base de Datos](#creación-base-de-datos)
* [Instalación Scripts](#instalación-scripts)

## Creación Base de datos

La creación de la base de datos se realizó utilizando una imagen docker de MySql. 

Para levantar dicha imagen se ejecutó el siguiente comando:
```
docker run --name some-mysql -e MYSQL_ROOT_PASSWORD=<una_password> -d mysql:latest
```

- Donde **`some-mysql`** corresponde a un nombre cualquiera que le querramos asignar al contenedor que contendrá la imagen de la base de datos MySQL corriendo

- Donde **`<una_password>`** corresponde a una password que le querramos asignar al usuario root de la base de datos
<br></br>

### Instalación Scripts
___
Para la creación y poblado del modelo de datos se dejen ejecutar los scripts en el siguiente orden:

1. `TP2_Mauricio_Borelli_01_create_database.sql`
2. `TP2_Mauricio_Borelli_01_create_database.sql`
3. `TP2_Mauricio_Borelli_01_create_database.sql`