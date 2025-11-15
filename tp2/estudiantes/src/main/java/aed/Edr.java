package aed;
import java.util.ArrayList;

public class Edr {
    private ListaOrdenada<Estudiante> _listaOrdenada;
    private MinHeap<Estudiante> _minHeap;
    private int _ladoAula;
    private int[] _examenCanonico;
    private int _cantEstudiantes;
    private ArrayList<Integer> _noSospechososDeCopia;

    // HACER DOS MÉTODOS PARA VECINOS, EN LOS QUE ME FIJO SI ESTOY EN PRIMERA FILA,
    // SOY EL ÚLTIMO. HACER ESTO PARA AHORRAR LÍNEAS DE CÓDIGO 

    


    // COMPLETAR LAS COMPLEJIDADES (EN EL CÓDIGO)

    // COMENTAR EL CÓDIGO

    // HACER NUESTROS PROPIOS TESTS

    public Edr(int LadoAula, int Cant_estudiantes, int[] ExamenCanonico) {
        //  Inicializamos todos nuestros atributos, creamos el heap y la lista ordenada.
        _ladoAula = LadoAula;
        _examenCanonico = ExamenCanonico;

        Estudiante[] listaEstudiantes = listaDeEstudiantes(Cant_estudiantes, ExamenCanonico.length);

        _minHeap = new MinHeap<Estudiante>(listaEstudiantes);
        _listaOrdenada = _minHeap.heapToList();

        _cantEstudiantes = Cant_estudiantes;
        //_entregados = new ArrayList<MinHeap<Estudiante>.Handle>(_cantEstudiantes);
        _noSospechososDeCopia = new ArrayList<Integer>(Cant_estudiantes);
    }

    public Estudiante[] listaDeEstudiantes(int cantEstudiantes, int cantRespuestas){
        // Dada una cantidad de estudiantes (E) y una cantidad de respuestas (R), creamos E estudiantes,
        // donde cada uno tiene un exámen sin completar de R posiciones y devolvemos ese array de estudiantes.

        Estudiante[] listaDeEstudiantes = new Estudiante[cantEstudiantes];

        for (int i = 0; i < cantEstudiantes; i ++){
            listaDeEstudiantes[i] = new Estudiante(i, cantRespuestas);
        }

        return listaDeEstudiantes;
    }

//-------------------------------------------------NOTAS--------------------------------------------------------------------------

    public double[] notas(){
        // Devolvemos la nota de todos los estudiantes ordenado por id. Para eso simplemente, accedemos a la nota (puntaje) de cada estudiante
        // accediendo desde la lista ordenada

        double[] listaDeNotas = new double[_cantEstudiantes];

        for (int i = 0; i < _cantEstudiantes; i ++){
            listaDeNotas[i] = _listaOrdenada.accederAPosicion(i).valor().puntaje();
        }

        return listaDeNotas;
    }

//------------------------------------------------COPIARSE------------------------------------------------------------------------



    public void copiarse(int estudiante) {
        // Obtengo los vecinos de mi estudiante (los ids de los vecinos).
        int[] misVecinos = vecinos(estudiante);

        if (misVecinos != null){
            int maximo = 0;
            int idMaximo = -1;

            // Recorro los 3 posibles vecinos que tengo y me fijo cual de ellos tiene la cantidad de respuestas máxima.
            // Si hay 2 o más que tienen la misma cantidad de respuestas, desempato por id mayor, para eso me voy guardando el máximo de respuestas y el mayor id.
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
                // Obtengo las respuestas de ese estudiante.
                int i = 0;
                MinHeap<Estudiante>.Handle miEstudiante = _listaOrdenada.accederAPosicion(estudiante);
                int[] misRespuestas = miEstudiante.valor().respuestas();
                int[] respuestasVecino = _listaOrdenada.accederAPosicion(idMaximo).valor().respuestas();
                
                // Recorro hasta la primera posición que el estudiante del que me copio tiene una respuesta que yo no tengo
                while (!(misRespuestas[i] == -1 && respuestasVecino[i] != -1)){
                    i++;
                }

                // Me copio de esa respuesta
                miEstudiante.valor().actualizarRespuestaRapido(i, respuestasVecino[i], _examenCanonico);
                miEstudiante.actualizarHeap(miEstudiante.posicionHeap());
            }
        }
    }

    public int cantRespuestasCompletadas(int vecino, int estudiante){
        int cantRespuestasCompletadas = 0;
        int[] misRespuestas = _listaOrdenada.accederAPosicion(estudiante).valor().respuestas();
        int[] respuestasVecino = _listaOrdenada.accederAPosicion(vecino).valor().respuestas();

        // Si en una posición hay un -1, significa que ese estudiante no tiene una respuesta completada,
        // luego, cuando no hay un -1, sumamos 1 al contador.
        
        for (int i = 0; i < misRespuestas.length; i ++){
            if (misRespuestas[i] == -1 && respuestasVecino[i] != -1){
                cantRespuestasCompletadas = cantRespuestasCompletadas + 1;
            }
        }

        return cantRespuestasCompletadas;
    }

    public int[] vecinos(int posicion){
        // La posición es el id del estudiante. Como convención, tenemos una lista de 3 posiciones (cantidad máxima de vecinos posibles).
        // En la 1era posición guardamos el vecino de la izquierda, en la 2da el vecino de la derecha, y en la 3era el de adelante.
        // Si no hay un vecino en alguna de esas posiciones, guardamos un -1. 
        
        int[] vecinos = new int[3];
        int estPorFila = (int) Math.ceil(((double) _ladoAula) / 2);

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
        // Un estudiante resuelve un ejercicio en particular y se le actualiza su puntaje y su array de respuestas.
        // Luego, actualizamos el heap para que se siga manteniendo el orden.

        MinHeap<Estudiante>.Handle miEstudiante = _listaOrdenada.accederAPosicion(estudiante);
        
        miEstudiante.valor().actualizarRespuestaRapido(NroEjercicio, res, _examenCanonico); 
        miEstudiante.actualizarHeap(miEstudiante.posicionHeap());
    } 

    



//------------------------------------------------CONSULTAR DARK WEB-------------------------------------------------------

    public void consultarDarkWeb(int n, int[] examenDW) {
        // n estudiantes se copian de la dark web. Para eso, primero necesitamos saber quienes son los n estudiantes
        // con la peor nota. Por eso primero los desencolamos del heap, les actualizamos sus respuestas y sus notas (copiadas de la dark web),
        // y una vez tenemos eso, recién ahí podemos encolar a esos estudiantes con sus nuevos puntajes, y que se pueda reordenar el heap.

        ArrayList<MinHeap<Estudiante>.Handle> copiados = new ArrayList<MinHeap<Estudiante>.Handle>(n);

        for (int i = 0; i < n; i ++){
            copiados.add( _minHeap.desencolar());
            copiados.get(i).valor().actualizarRespuestas(_examenCanonico, examenDW);
        }

        for (int i = 0; i < n; i ++){
            _minHeap.encolar(copiados.get(i));
        }
    }
 

//-------------------------------------------------ENTREGAR-------------------------------------------------------------

    public void entregar(int estudiante) {
        MinHeap<Estudiante>.Handle miEstudiante = _listaOrdenada.accederAPosicion(estudiante);

        miEstudiante.valor().entregar();

        miEstudiante.subirHeap(miEstudiante.posicionHeap());

        miEstudiante.desencolarHeap();

        //MinHeap<Estudiante>.Handle entregado = miEstudiante.desencolarHeap();

        //_entregados[estudiante] = entregado;
    }

//-----------------------------------------------------CORREGIR---------------------------------------------------------

    public NotaFinal[] corregir() {
        NotaFinal[] notasFinales = new NotaFinal[_noSospechososDeCopia.size()];
        Estudiante[] listaEstudiantes = new Estudiante[_noSospechososDeCopia.size()];
        
        for (int i = 0; i < _noSospechososDeCopia.size(); i ++){
            listaEstudiantes[i] = _listaOrdenada.accederAPosicion(_noSospechososDeCopia.get(i)).valor();
        }

        _minHeap = new MinHeap<Estudiante>(listaEstudiantes);
        _minHeap.algoritmoDeFloyd();

        for (int i = _noSospechososDeCopia.size() - 1; i >= 0; i --){
            MinHeap<Estudiante>.Handle est = _minHeap.desencolar();
            NotaFinal nota = new NotaFinal(est.valor().puntaje(), est.valor().id());

            notasFinales[i] = nota;
        }

        return notasFinales;
    }

//-------------------------------------------------------CHEQUEAR COPIAS-------------------------------------------------

    public int[] chequearCopias() {
        
        int[][] conteosPorPregunta = new int[_examenCanonico.length][10];
        
        for (int pregunta = 0; pregunta < _examenCanonico.length; pregunta ++) {
            for (int est = 0; est < _cantEstudiantes; est++) {
                int respuesta = _listaOrdenada.accederAPosicion(est).valor().respuestas()[pregunta];

                if (respuesta != -1 && respuesta >= 0 && respuesta < 10) {
                    conteosPorPregunta[pregunta][respuesta] ++;
                }
            }
        }

        // Complejidades ArrayList: https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html

        ArrayList<Integer> sospechosos = new ArrayList<Integer>(_cantEstudiantes);

        for (int est = 0; est < _cantEstudiantes; est++) {
            int[] respuestas = _listaOrdenada.accederAPosicion(est).valor().respuestas();
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