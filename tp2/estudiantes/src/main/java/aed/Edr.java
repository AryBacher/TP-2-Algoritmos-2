package aed;
import java.util.ArrayList;

import aed.MinHeap.Handle;

public class Edr {
    private ListaOrdenada _listaOrdenada;
    private MinHeap _minHeap;
    private int _ladoAula;
    private int[] _examenCanonico;
    private int _cantEstudiantes;
    private Handle[] _entregados;
    private ArrayList<Integer> _noSospechososDeCopia;

    // PREGUNTAR SI ESTÁ BIEN ASUMIR QUE LAS RESPUESTAS DE LOS EJERCICIOS SON
    // NÚMEROS POSITIVOS. CUANDO INICIALIZAMOS, PONEMOS TODOS LOS VALORES EN -1

    // HACER DOS MÉTODOS PARA VECINOS, EN LOS QUE ME FIJO SI ESTOY EN PRIMERA FILA,
    // SOY EL ÚLTIMO. HACER ESTO PARA AHORRAR LÍNEAS DE CÓDIGO 

    // AL FINAL HEAPIFY NO ERA HEAPIFY, LA OPERACIÓN DE HEAPIFY ES LA DE BAJAR

    // PREGUNTAR SI LAS POSIBLES RESPUESTAS A UNA PREGUNTA DEL EXAMEN ESTÁ ACOTADA
    // ENTRE 0 Y 9

    // PREGUNTAR SI PODEMOS ASUMIR QUE DESPUÉS DE HACER CHEQUEAR COPIAS,
    // LA ÚNICA OTRA FUNCIÓN QUE SE PUEDE CORRER ES "CORREGIR", PORQUE SINO
    // TENEMOS UN MINHEAP VACÍO Y LAS OTRAS FUNCIONES PODRÍAN TIRAR ERROR.

    // LO MISMO PARA DESPUÉS DE CORREGIR, QUE OTRAS FUNCIONES SE PUEDEN CORRER?
    // ENTONCES DESPUÉS DE HACER CORREGIR Y CHEQUEAR COPIAS, TENDRÍAMOS QUE REARMAR 
    // EL HEAP?

    // PREGUNTAR SI HACE FALTA AGREGARLE EL TIPO T A LA LISTA ORDENADA Y AL HEAP



    // COMPLETAR LAS COMPLEJIDADES (EN EL CÓDIGO)

    // COMENTAR EL CÓDIGO

    // HACER NUESTROS PROPIOS TESTS

    public Edr(int LadoAula, int Cant_estudiantes, int[] ExamenCanonico) {
        _ladoAula = LadoAula;
        //_examenCanonico = new int[ExamenCanonico.length];
        _examenCanonico = ExamenCanonico;

        Estudiante[] listaEstudiantes = listaDeEstudiantes(Cant_estudiantes, ExamenCanonico.length);

        _minHeap = new MinHeap(listaEstudiantes);
        _listaOrdenada = _minHeap.heapToList();

        _cantEstudiantes = Cant_estudiantes;
        _entregados = new Handle[_cantEstudiantes];
        _noSospechososDeCopia = new ArrayList<Integer>();
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
        NotaFinal[] notasFinales = new NotaFinal[_noSospechososDeCopia.size()];
        Estudiante[] listaEstudiantes = new Estudiante[_noSospechososDeCopia.size()];
        
        for (int i = 0; i < _noSospechososDeCopia.size(); i ++){
            listaEstudiantes[i] = _listaOrdenada.accederAPosicion(_noSospechososDeCopia.get(i)).estudiante();
        }

        _minHeap = new MinHeap(listaEstudiantes);
        _minHeap.algoritmoDeFloyd();

        for (int i = _noSospechososDeCopia.size() - 1; i >= 0; i --){
            Handle est = _minHeap.desencolar();
            NotaFinal nota = new NotaFinal(est.puntaje(), est.id());

            notasFinales[i] = nota;
        }

        return notasFinales;
    }

//-------------------------------------------------------CHEQUEAR COPIAS-------------------------------------------------

    public int[] chequearCopias() {
        
        int[][] conteosPorPregunta = new int[_examenCanonico.length][10];
        
        for (int pregunta = 0; pregunta < _examenCanonico.length; pregunta ++) {
            for (int est = 0; est < _cantEstudiantes; est++) {
                int respuesta = _listaOrdenada.accederAPosicion(est).respuestas()[pregunta];

                if (respuesta != -1 && respuesta >= 0 && respuesta < 10) {
                    conteosPorPregunta[pregunta][respuesta] ++;
                }
            }
        }

        // Complejidades ArrayList: https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html

        ArrayList<Integer> sospechosos = new ArrayList<Integer>();

        for (int est = 0; est < _cantEstudiantes; est++) {
            int[] respuestas = _listaOrdenada.accederAPosicion(est).respuestas();
            boolean esSospechoso = true;
            boolean tieneRespuestas = false;
            
            for (int pregunta = 0; pregunta < respuestas.length; pregunta ++) {
                int respuesta = respuestas[pregunta];
                
                if (respuesta != -1) {
                    tieneRespuestas = true;
                    
                    int cantidadTotal = conteosPorPregunta[pregunta][respuesta];
                    
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

            else{
                _noSospechososDeCopia.add(est);
            }
        }

        int[] estudiantesCopiados = new int[sospechosos.size()];
        for (int i = 0; i < sospechosos.size(); i++) {
            estudiantesCopiados[i] = sospechosos.get(i);
        }

        return estudiantesCopiados;
    }
}
