package com.faceentry.services;

import com.faceentry.model.Empleado;
import com.faceentry.repository.impl.EmpleadoRepositoryImpl;
import com.faceentry.repository.impl.FichajeRepositoryImpl;
import com.faceentry.mocks.FacialRecognitionMock;

import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

public class FichajeService {


    /**
     * Recibe una imagen, identifica al empleado y registra el fichaje.
     * @param imagen Imagen capturada (BufferedImage o File)
     * @return true si el fichaje fue exitoso, false si no se identificó empleado
     */
    public void identificarYRegistrarFichaje(File imagen) throws Exception {
        // 1. Obtener todos los empleados y sus rutas biométricas


        // 2. Identificar empleado (mock: compara archivos por nombre, real: usar facial recognition)



        // 3. Registrar fichaje (entrada o salida)


//        if (!yaFichoHoy) {
            // Registrar entrada

//        } else {
            // Registrar salida

//        }
//        return true;
    }
}