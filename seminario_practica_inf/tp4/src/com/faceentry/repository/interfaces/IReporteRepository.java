package com.faceentry.repository.interfaces;

import java.util.ArrayList;
import java.util.Map;

public interface IReporteRepository {

    Map<String, ArrayList<?>>  obtenerReporteAsistencias() throws Exception;
}
