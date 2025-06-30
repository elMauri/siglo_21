package com.faceentry.model;

public abstract class Persona {
	protected String nombre;
    protected String apellido;
    protected String dni;
    protected String cargo;
    protected String estado;
    protected String rutaImagen;
    
    public Persona(String nombre, String apellido, String dni, String cargo, String estado) {
		super();
		this.nombre = nombre;
		this.apellido = apellido;
		this.dni = dni;
		this.cargo = cargo;
		this.estado = estado;
	}
    
    public Persona(String nombre, String apellido, String dni, String cargo, String estado, String rutaImagen) {
		super();
		this.nombre = nombre;
		this.apellido = apellido;
		this.dni = dni;
		this.cargo = cargo;
		this.estado = estado;
		this.rutaImagen = rutaImagen;
	}


    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    public abstract String getTipo();


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getApellido() {
		return apellido;
	}


	public void setApellido(String apellido) {
		this.apellido = apellido;
	}


	public String getDni() {
		return dni;
	}


	public void setDni(String dni) {
		this.dni = dni;
	}


	public String getCargo() {
		return cargo;
	}


	public void setCargo(String cargo) {
		this.cargo = cargo;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getRutaImagen() {
		return rutaImagen;
	}

	public void setRutaImagen(String rutaImagen) {
		this.rutaImagen = rutaImagen;
	}
}
