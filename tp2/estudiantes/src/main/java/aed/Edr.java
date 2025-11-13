package aed;
import java.util.ArrayList;
import java.util.logging.Handler;

import aed.MinHeap.Handle;

public class Edr {
    private ListaOrdenada<MinHeap.Handle> _listaOrdenada;
    private MinHeap _minHeap;
    private int _ladoAula;
    private int[] _examenCanonico;
    private int _cantEstudiantes;
    private Handle[] _entregados;

    // PREGUNTAR SI ESTÁ BIEN ASUMIR QUE LAS RESPUESTAS DE LOS EJERCICIOS SON
    // NÚMEROS POSITIVOS. CUANDO INICIALIZAMOS, PONEMOS TODOS LOS VALORES EN -1

    // HACER DOS MÉTODOS PARA VECINOS, EN LOS QUE ME FIJO SI ESTOY EN PRIMERA FILA,
    // SOY EL ÚLTIMO. HACER ESTO PARA AHORRAR LÍNEAS DE CÓDIGO 

    // AL FINAL HEAPIFY NO ERA HEAPIFY, LA OPERACIÓN DE HWAPIFY ES LA DE BAJAR

    public Edr(int LadoAula, int Cant_estudiantes, int[] ExamenCanonico) {
        _ladoAula = LadoAula;
        //_examenCanonico = new int[ExamenCanonico.length];
        _examenCanonico = ExamenCanonico;

        Estudiante[] listaEstudiantes = listaDeEstudiantes(Cant_estudiantes, ExamenCanonico.length);

        _minHeap = new MinHeap(listaEstudiantes);
        _listaOrdenada = _minHeap.heapToList();

        _cantEstudiantes = Cant_estudiantes;
        _entregados = new Handle[_cantEstudiantes];
    }

    public Estudiante[] listaDeEstudiantes(int cantEstudiantes, int cantRespuestas){
        Estudiante[] listaDeEstudiantes = new Estudiante[cantEstudiantes];

        for (int i = 0; i < cantEstudiantes; i ++){
            listaDeEstudiantes[i] = new Estudiante(i, cantRespuestas);
        }

        return listaDeEstudiantes;
    }

//-------------------------------------------------NOTAS--------------------------------------------------------------------------

    public double[] notas(){
        double[] listaDeNotas = new double[_cantEstudiantes];

        for (int i = 0; i < _cantEstudiantes; i ++){
            listaDeNotas[i] = _listaOrdenada.accederAPosicion(i).puntaje();
        }

        return listaDeNotas;
    }

//------------------------------------------------COPIARSE------------------------------------------------------------------------



    public void copiarse(int estudiante) {
        int[] misVecinos = vecinos(estudiante);

        if (misVecinos != null){
            int maximo = 0;
            int idMaximo = -1;

            for (int i = 0; i < 3; i ++){
                if (misVecinos[i] != -1){
                    int cantRespuestas = cantRespuestasCompletadas(misVecinos[i], estudiante);
                    if (cantRespuestas > maximo){
                        maximo = cantRespuestas;
                        idMaximo = misVecinos[i];
                    }

                    else if (cantRespuestas == maximo){
                        if (idMaximo < misVecinos[i]) {
                            maximo = cantRespuestas;
                            idMaximo = misVecinos[i];
                        }
                    }
                }
            }

            if (maximo != 0){
                int i = 0;
                MinHeap.Handle miEstudiante = _listaOrdenada.accederAPosicion(estudiante);
                int[] misRespuestas = miEstudiante.respuestas();
                int[] respuestasVecino = _listaOrdenada.accederAPosicion(idMaximo).respuestas();

                while (!(misRespuestas[i] == -1 && respuestasVecino[i] != -1)){
                    i++;
                }

                miEstudiante.actualizarRespuestaRapido(i, respuestasVecino[i], _examenCanonico);
                miEstudiante.actualizarHeap(miEstudiante.posicionHeap());
            }
        }
    }

    public int cantRespuestasCompletadas(int vecino, int estudiante){
        int cantRespuestasCompletadas = 0;
        int[] misRespuestas = _listaOrdenada.accederAPosicion(estudiante).respuestas();
        int[] respuestasVecino = _listaOrdenada.accederAPosicion(vecino).respuestas();

        for (int i = 0; i < misRespuestas.length; i ++){
            if (misRespuestas[i] == -1 && respuestasVecino[i] != -1){
                cantRespuestasCompletadas = cantRespuestasCompletadas + 1;
            }
        }

        return cantRespuestasCompletadas;
    }

    public int[] vecinos(int posicion){
        // La posición es el id del estudiante
        
        int[] vecinos = new int[3];
        int estPorFila = (int) Math.ceil(_ladoAula / 2);

        if (_cantEstudiantes == 1) {return null;}

        // Me fijo si estoy en una punta
        // Punta izquierda
        if (posicion % estPorFila == 0){
            vecinos[0] = -1;
            // Me fijo que no soy el último estudiante y agrego el de mi derecha
            if (posicion != _cantEstudiantes - 1){
                vecinos[1] = posicion + 1;
            }

            else {
                vecinos[1] = -1;
            }

            // Me fijo que no estoy en la 1era fila y agrego al de adelante
            if (posicion != 0){
                vecinos[2] = posicion - estPorFila;
            }

            else {
                vecinos[2] = -1;
            }
        }

        // Punta derecha
        else if ((posicion + 1) % estPorFila == 0) {
            vecinos[1] = -1;
            
            // Agrego al vecino de mi izquierda
            vecinos[0] = posicion - 1;

            // Me fijo que no estoy en la 1era fila y agrego al de adelante
            if (posicion >= estPorFila){
                vecinos[2] = posicion - estPorFila;
            }

            else {
                vecinos[2] = -1;
            }
        }

        // No estoy en una punta
        else {
            vecinos[0] = posicion - 1;

            // Me fijo que no sea el último estudiante
            if (posicion != _cantEstudiantes - 1){
                vecinos[1] = posicion + 1;
            }

            else {
                vecinos[1] = -1;
            }

            // Me fijo que no estoy en la 1era fila y agrego al de adelante
            if (posicion >= estPorFila){
                vecinos[2] = posicion - estPorFila;
            }

            else {
                vecinos[2] = -1;
            }
        }

        return vecinos;
    }

//-----------------------------------------------RESOLVER----------------------------------------------------------------




    public void resolver(int estudiante, int NroEjercicio, int res) {
        MinHeap.Handle miEstudiante = _listaOrdenada.accederAPosicion(estudiante);
        
        miEstudiante.actualizarRespuestaRapido(NroEjercicio, res, _examenCanonico);
        miEstudiante.actualizarHeap(miEstudiante.posicionHeap());
    } 

    



//------------------------------------------------CONSULTAR DARK WEB-------------------------------------------------------

    public void consultarDarkWeb(int n, int[] examenDW) {
        Handle[] copiados = new Handle[n];

        for (int i = 0; i < n; i ++){
            copiados[i] = _minHeap.desencolar();
            copiados[i].actualizarRespuestas(_examenCanonico, examenDW);
        }

        for (int i = 0; i < n; i ++){
            _minHeap.encolar(copiados[i]);
        }
    }
 

//-------------------------------------------------ENTREGAR-------------------------------------------------------------

    public void entregar(int estudiante) {
        MinHeap.Handle miEstudiante = _listaOrdenada.accederAPosicion(estudiante);

        miEstudiante.entregar();

        miEstudiante.subirHeap(miEstudiante.posicionHeap());

        MinHeap.Handle entregado = miEstudiante.desencolarHeap();

        _entregados[estudiante] = entregado;
    }

//-----------------------------------------------------CORREGIR---------------------------------------------------------

    public NotaFinal[] corregir() {
        throw new UnsupportedOperationException("Sin implementar");
    }

//-------------------------------------------------------CHEQUEAR COPIAS-------------------------------------------------

    public int[] chequearCopias() {
        ArrayList<ConteoRespuesta>[] conteosPorPregunta = new ArrayList[_examenCanonico.length];
        for (int i = 0; i < _examenCanonico.length; i ++) {
            conteosPorPregunta[i] = new ArrayList<ConteoRespuesta>();
        }
        for (int i = 0; i < _examenCanonico.length; i ++) {
            for (int est = 0; est < _cantEstudiantes; est ++) {
                int respuesta = _listaOrdenada.accederAPosicion(est).respuestas()[i];
                if (respuesta != -1) {
                    boolean encontrado = false;
                    for (ConteoRespuesta cr : conteosPorPregunta[i]) {
                        if (cr.respuesta == respuesta) {
                            cr.cantidad = cr.cantidad + 1;
                            encontrado = true;
                            break;
                        }
                    }
                    if (encontrado == false) {
                        conteosPorPregunta[i].add(new ConteoRespuesta(respuesta, 1));
                    }
                }
            }
        }

        ArrayList<Integer> sospechosos = new ArrayList<Integer>();
        
        for (int est = 0; est < _cantEstudiantes; est++) {
            int[] respuestas = _listaOrdenada.accederAPosicion(est).respuestas();
            boolean esSospechoso = true;
            boolean tieneRespuestas = false;
            
            for (int pregunta = 0; pregunta < respuestas.length; pregunta++) {
                int respuesta = respuestas[pregunta];
                
                // Me sirve para chequear solo respuestas completadas
                if (respuesta != -1) {
                    tieneRespuestas = true;
                    
                    int cantidadTotal = 0;
                    for (ConteoRespuesta cr : conteosPorPregunta[pregunta]) {
                        if (cr.respuesta == respuesta) {
                            cantidadTotal = cr.cantidad;
                            break;
                        }
                    }
                    
                    // No cuento al estudiante que analizo
                    int cantidadOtros = cantidadTotal - 1;
                    
                    if (cantidadOtros < _cantEstudiantes / 4) {
                        esSospechoso = false;
                        break;
                    }
                }
            }
            
            if (esSospechoso && tieneRespuestas) {
                sospechosos.add(est);
            }
        }

        int[] resultado = new int[sospechosos.size()];

        for (int i = 0; i < sospechosos.size(); i++) {
            resultado[i] = sospechosos.get(i);
        }
        
        return resultado;

    }
}
