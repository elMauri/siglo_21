package com.faceentry.mocks;

import com.faceentry.model.Empleado;
import com.faceentry.repository.impl.EmpleadoRepositoryImpl;

import java.io.File;
import java.util.List;

public class FacialRecognitionMock {
	/**
	 * Mock: identifica al empleado por coincidencia de nombre de archivo. En
	 * producción, aquí iría la lógica real de reconocimiento facial.
	 * @throws Exception 
	 */
	public static Empleado identificarEmpleado(File imagen, Empleado emp, List<EmpleadoRepositoryImpl> empleados) throws Exception {
		for (EmpleadoRepositoryImpl empRepoImpl : empleados) {
			if (empRepoImpl.getRutaImagenBiometrica(emp) != null
					&& new File(empRepoImpl.getRutaImagenBiometrica(emp)).getName().equals(imagen.getName())) {
				return emp;
			}
		}
		return null;
	}
}
