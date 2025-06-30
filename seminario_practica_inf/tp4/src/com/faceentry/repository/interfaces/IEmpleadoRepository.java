package com.faceentry.repository.interfaces;
import java.util.ArrayList;
import java.util.Map;

import com.faceentry.model.Empleado;

public interface IEmpleadoRepository {

    Empleado obtenerEmpleadoPorLegajo(String legajo) throws Exception;
    String getRutaImagenBiometrica(Empleado empleado) throws Exception;
    void registrarEmpleado(Empleado empleado, String rutaImagen) throws Exception;
    void registrarEmpleado(Empleado empleado) throws Exception;
    Map<String, ArrayList<?>> obtenerTodos()  throws Exception;
}