
package com.faceentry.modelo;

import java.util.*;

public class Empleado extends Persona {
    private String legajo;
    private List<String> fichajes;

    //Constructor clase Empleado
    public Empleado(String nombre, String apellido, String legajo, String dni, String cargo, String estado) {
        super(nombre, apellido, dni, estado, cargo);
        this.legajo = legajo;
        this.fichajes = new ArrayList<>();
    }

    public void registrarFichaje(String hora) {
        fichajes.add(hora);
    }

    public List<String> getFichajes() {
        return fichajes;
    }

    @Override
    public String getTipo() {
        return "Empleado";
    }

    public String getLegajo() {
        return legajo;
    }
}
