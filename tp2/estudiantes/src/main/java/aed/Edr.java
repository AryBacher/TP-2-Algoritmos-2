package aed;
import java.util.ArrayList;

public class Edr {
    private ListaOrdenada<Estudiante> _listaOrdenada;
    private MinHeap<Estudiante> _minHeap;
    private int _ladoAula;
    private int[] _examenCanonico;
    private int _cantEstudiantes;
    private ArrayList<Integer> _noSospechososDeCopia;

    public Edr(int LadoAula, int Cant_estudiantes, int[] ExamenCanonico) {
        //  Inicializamos todos nuestros atributos, creamos el heap y la lista ordenada.
        _ladoAula = LadoAula;
        _examenCanonico = ExamenCanonico; // -- O(1)

        Estudiante[] listaEstudiantes = listaDeEstudiantes(Cant_estudiantes, ExamenCanonico.length); // -- O(E * R)

        _minHeap = new MinHeap<Estudiante>(listaEstudiantes); // -- O(E)
        _listaOrdenada = _minHeap.heapToList(); // -- O(E)

        _cantEstudiantes = Cant_estudiantes;
        _noSospechososDeCopia = new ArrayList<Integer>(Cant_estudiantes); // -- O(E)

        // Complejidad Total: O(E * R) + 3 * O(E) = O(E * R)
    }

    private Estudiante[] listaDeEstudiantes(int cantEstudiantes, int cantRespuestas){
        // Dada una cantidad de estudiantes (E) y una cantidad de respuestas (R), creamos E estudiantes,
        // donde cada uno tiene un exámen sin completar de R posiciones y devolvemos ese array de estudiantes.

        Estudiante[] listaDeEstudiantes = new Estudiante[cantEstudiantes]; // -- O(E)

        for (int i = 0; i < cantEstudiantes; i ++){ // -- E * O(R) = O(E * R)
            listaDeEstudiantes[i] = new Estudiante(i, cantRespuestas);
        }

        return listaDeEstudiantes;

        // Complejidad Total: O(E * R)
    }

//-------------------------------------------------NOTAS--------------------------------------------------------------------------

    public double[] notas(){
        // Devolvemos la nota de todos los estudiantes ordenado por id. Para eso simplemente, accedemos a la nota (puntaje) de cada estudiante
        // accediendo desde la lista ordenada

        double[] listaDeNotas = new double[_cantEstudiantes]; // -- O(E)

        for (int i = 0; i < _cantEstudiantes; i ++){ // -- E * O(1) = O(E)
            listaDeNotas[i] = _listaOrdenada.accederAPosicion(i).valor().puntaje();
        }

        return listaDeNotas;

        // Complejidad Total: O(E)
    }

//------------------------------------------------COPIARSE------------------------------------------------------------------------



    public void copiarse(int estudiante) {
        // Obtengo los vecinos de mi estudiante (los ids de los vecinos).
        int[] misVecinos = vecinos(estudiante); // -- O(1)

        if (misVecinos != null){
            int maximo = 0;
            int idMaximo = -1;

            // Recorro los 3 posibles vecinos que tengo y me fijo cual de ellos tiene la cantidad de respuestas máxima.
            // Si hay 2 o más que tienen la misma cantidad de respuestas, desempato por id mayor, para eso me voy guardando el máximo de respuestas y el mayor id.
            for (int i = 0; i < 3; i ++){ // 3 * O(R) = O(3R) = O(R)
                if (misVecinos[i] != -1){
                    int cantRespuestas = cantRespuestasCompletadas(misVecinos[i], estudiante); // -- O(R)
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
                MinHeap<Estudiante>.HandleHeap<Estudiante> miEstudiante = _listaOrdenada.accederAPosicion(estudiante); // -- O(1)
                int[] misRespuestas = miEstudiante.valor().respuestas(); // -- O(1)
                int[] respuestasVecino = _listaOrdenada.accederAPosicion(idMaximo).valor().respuestas(); // -- O(1)
                
                // Recorro hasta la primera posición que el estudiante del que me copio tiene una respuesta que yo no tengo
                while (!(misRespuestas[i] == -1 && respuestasVecino[i] != -1)){ // -- R * O(1) = O(R)
                    i++;
                }

                // Me copio de esa respuesta
                miEstudiante.valor().actualizarRespuestaRapido(i, respuestasVecino[i], _examenCanonico); // -- O(1)
                miEstudiante.actualizarValor(miEstudiante.valor()); // -- O(log E)
                //_minHeap.actualizar(miEstudiante.posicionHeap()); // -- O(log E)
            }

            // Complejidad Total: O(R) + O(R) + O(log E) = O(R + log E)
        }
    }

    public int cantRespuestasCompletadas(int vecino, int estudiante){
        int cantRespuestasCompletadas = 0;
        int[] misRespuestas = _listaOrdenada.accederAPosicion(estudiante).valor().respuestas(); // -- O(1)
        int[] respuestasVecino = _listaOrdenada.accederAPosicion(vecino).valor().respuestas(); // -- O(1)

        // Si en una posición hay un -1, significa que ese estudiante no tiene una respuesta completada,
        // luego, cuando no hay un -1, sumamos 1 al contador.
        
        for (int i = 0; i < misRespuestas.length; i ++){ // -- R * O(1) = O(R)
            if (misRespuestas[i] == -1 && respuestasVecino[i] != -1){
                cantRespuestasCompletadas = cantRespuestasCompletadas + 1;
            }
        }

        return cantRespuestasCompletadas;

        // Complejidad Total: O(1) + O(R) = O(R)
    }

    public int[] vecinos(int posicion){
        // La posición es el id del estudiante. Como convención, tenemos una lista de 3 posiciones (cantidad máxima de vecinos posibles).
        // En la 1era posición guardamos el vecino de la izquierda, en la 2da el vecino de la derecha, y en la 3era el de adelante.
        // Si no hay un vecino en alguna de esas posiciones, guardamos un -1. 
        
        int[] vecinos = new int[3]; // -- O(3) = O(1)
        int estPorFila = (int) Math.ceil(((double) _ladoAula) / 2);

        if (_cantEstudiantes == 1) {return null;}

        // Caso particular. (Aula 2x2)
        if (estPorFila == 1){
            vecinos[0] = -1;
            vecinos[1] = -1;

            if (posicion == 2){
                vecinos[2] = 0;
            }
            
            else{
                vecinos[2] = -1;
            }

        }

        // Me fijo si estoy en una punta
        // Punta izquierda
        else if (posicion % estPorFila == 0){
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

        // Complejidad Total: O(1)
    }

//-----------------------------------------------RESOLVER----------------------------------------------------------------




    public void resolver(int estudiante, int NroEjercicio, int res) {
        // Un estudiante resuelve un ejercicio en particular y se le actualiza su puntaje y su array de respuestas.
        // Luego, actualizamos el heap para que se siga manteniendo el orden.

        MinHeap<Estudiante>.HandleHeap<Estudiante> miEstudiante = _listaOrdenada.accederAPosicion(estudiante);
        
        miEstudiante.valor().actualizarRespuestaRapido(NroEjercicio, res, _examenCanonico); // -- O(1)
        miEstudiante.actualizarValor(miEstudiante.valor()); // -- O(log E)
        //_minHeap.actualizar(miEstudiante.posicionHeap()); // -- O(log E)

        // Complejidad Total: O(log E)
    } 

    



//------------------------------------------------CONSULTAR DARK WEB-------------------------------------------------------

    public void consultarDarkWeb(int n, int[] examenDW) {
        // n estudiantes se copian de la dark web. Para eso, primero necesitamos saber quienes son los n estudiantes
        // con la peor nota. Por eso primero los desencolamos del heap, les actualizamos sus respuestas y sus notas (copiadas de la dark web),
        // y una vez tenemos eso, recién ahí podemos encolar a esos estudiantes con sus nuevos puntajes, y que se pueda reordenar el heap.

        ArrayList<MinHeap<Estudiante>.HandleHeap<Estudiante>> copiados = new ArrayList<MinHeap<Estudiante>.HandleHeap<Estudiante>>(n); // -- O(k), con k = n

        for (int i = 0; i < n; i ++){ // k * (O(log E) + O(R)) = O(k * (R + log(E)))
            copiados.add( _minHeap.desencolar()); // -- O(log E)
            copiados.get(i).valor().actualizarRespuestas(_examenCanonico, examenDW); // -- O(R)
        }

        for (int i = 0; i < n; i ++){ // k * O(log E) = O(k * log(E))
            _minHeap.encolar(copiados.get(i));
        }

        // Complejidad Total: O(k * (R + log(E))) + O(k * log(E)) = O(k * (R + log(E)) 
    }
 

//-------------------------------------------------ENTREGAR-------------------------------------------------------------

    public void entregar(int estudiante) {
        // Dado un estudiante, este entrega su examen.

        // Luego de que entrega su examen, cambia su valor de entregado a verdadero, luego,
        // cuando quiero actualizar el heap, este estudiante está garantizado a terminar arriba de todo
        // pues sería justamente el único que ya entregó y sigue en el heap. Por lo tanto
        // puedo desencolarlo del heap (y voy a desencolar a ese estudiante) y el heap se reordena.

        MinHeap<Estudiante>.HandleHeap<Estudiante> miEstudiante = _listaOrdenada.accederAPosicion(estudiante);

        miEstudiante.valor().entregar();

        _minHeap.eliminar(estudiante);
        // miEstudiante.eliminarValor(estudiante);

        // _minHeap.subir(miEstudiante.posicionHeap()); // -- O(log E)

        // _minHeap.desencolar(); // -- O(log E)

        // Complejidad Total: O(log E) + O(log E) = O(log E)
    }

//-----------------------------------------------------CORREGIR---------------------------------------------------------

    public NotaFinal[] corregir() {
        NotaFinal[] notasFinales = new NotaFinal[_noSospechososDeCopia.size()]; // -- O(E)
        Estudiante[] listaEstudiantes = new Estudiante[_noSospechososDeCopia.size()]; // -- O(E)
        
        for (int i = 0; i < _noSospechososDeCopia.size(); i ++){ // E * O(1) = O(E)
            listaEstudiantes[i] = _listaOrdenada.accederAPosicion(_noSospechososDeCopia.get(i)).valor();
        }
        
        // Como el heap está vació, tenemos que armarlo con todos los estudiantes que no se hayan copiado
        // Esto lo hacemos con el algoritmo de Floyd.
        _minHeap = new MinHeap<Estudiante>(listaEstudiantes); // -- O(E)
        _minHeap.algoritmoDeFloyd(); // -- O(E)

        // Luego, por cada estudiante, lo desencolo del heap y ya queda ordenado 
        // de manera descendente en notasFinales. Para cada estudiante, le creamos su notaFinal.
        for (int i = _noSospechososDeCopia.size() - 1; i >= 0; i --){ // E * O(log E) = O(E * log E)
            MinHeap<Estudiante>.HandleHeap<Estudiante> est = _minHeap.desencolar(); // -- O(log E)
            NotaFinal nota = new NotaFinal(est.valor().puntaje(), est.valor().id()); // -- O(1)

            notasFinales[i] = nota;
        }

        return notasFinales;

        // Complejidad Total: 5 * O(E) + O(E * log E) = O(E * log E)
    }

//-------------------------------------------------------CHEQUEAR COPIAS-------------------------------------------------

    public int[] chequearCopias() {
        int[][] conteosPorPregunta = new int[_examenCanonico.length][10];
        
        // Tengo una matriz para contar cuantas respuestas hay por cada pregunta.
        // Por ejemplo, para la primera pregunta, si hay 7 estudiantes que respondieron "4", 
        // y 3 que respondieron "2", en la primera lista del array, va a haber ceros en todas las 
        // posiciones, menos en la posición 4, que habrá un 7, y en la posición 2, habrá un 3.

        for (int pregunta = 0; pregunta < _examenCanonico.length; pregunta ++) { // -- R * O(E) = O(E * R)
            for (int est = 0; est < _cantEstudiantes; est++) { // -- E * O(1) = O(E)
                int respuesta = _listaOrdenada.accederAPosicion(est).valor().respuestas()[pregunta];

                if (respuesta != -1 && respuesta >= 0 && respuesta < 10) {
                    conteosPorPregunta[pregunta][respuesta] ++;
                }
            }
        }

        ArrayList<Integer> sospechosos = new ArrayList<Integer>(_cantEstudiantes); // -- O(E)

        // Vamos viendo por cada estudiante si es sospechoso respetando los criterios propuestos.
        for (int est = 0; est < _cantEstudiantes; est++) { // -- E * O(R) = O(E * R)
            int[] respuestas = _listaOrdenada.accederAPosicion(est).valor().respuestas();
            boolean esSospechoso = true;
            boolean tieneRespuestas = false;
            
            for (int pregunta = 0; pregunta < respuestas.length; pregunta ++) { // R * O(1) = O(R)
                int respuesta = respuestas[pregunta];
                
                if (respuesta != -1) {
                    tieneRespuestas = true;
                    
                    int cantidadTotal = conteosPorPregunta[pregunta][respuesta];
                    
                    // No cuento al estudiante que analizo
                    int cantidadOtros = cantidadTotal - 1;
                    
                    // Si hay solo un ejercicio en el que las respuestas de 
                    // un estudiante no es igual a más del 25%, no es sospechoso de copia
                     
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

        // Vemos quienes son los estudiantes que se copiaron y los devolvemos.
        int[] estudiantesCopiados = new int[sospechosos.size()];
        for (int i = 0; i < sospechosos.size(); i++) { // -- E * O(1) = O(E)
            estudiantesCopiados[i] = sospechosos.get(i);
        }

        return estudiantesCopiados;

        // Complejidad Total: O(E * R) + O(E * R) + O(E) = O(E * R)
    }
}