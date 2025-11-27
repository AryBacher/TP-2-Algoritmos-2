package aed;

import java.util.ArrayList;



public class MinHeap<T extends Comparable<T>> {

    private ArrayList<aed.Handle<T>> _estudiantes;
    private int _tamaño;

    
    

    // ESTO ES LO QUE CORREGIMOS DEL HEAP// 

    // Nos creamos el handle dentro de nuestro MinHeap generico, que nos va a permitir interactuar con el handle
    public class HandleHeap implements aed.Handle<T> {
    
        private int posicion;
        private T valor;

    public HandleHeap(T val, int pos) { 
        this.valor = val;
        this.posicion = pos;
    }
    
    public T valor() {
        return valor;
    }

    public int posicionHeap() {
        return posicion;
    }

    public void actualizarPosicion(int nuevaPos) {
        this.posicion = nuevaPos;
    }
    
    public void actualizarValor (T nuevoValor){
        this.valor = nuevoValor; 
    }
}


    public MinHeap(T[] estudiantes) {
        // Creamos una lista de handles y en cada posicion le asignamos toda la información de un estudiante
        ArrayList<aed.Handle<T>> listaEstudiantes = new ArrayList<aed.Handle<T>>(estudiantes.length); // -- O(E)

        for (int i = 0; i < estudiantes.length; i ++){ // -- E * O(1) = O(E)
            listaEstudiantes.add(new HandleHeap(estudiantes[i], i));
            // -- O(1) (real, no amortizado pues listaEstudiantes ya tiene espacio disponible)
        }

        _estudiantes = listaEstudiantes;
        _tamaño = _estudiantes.size();
        // Complejidad Total: O(E) + O(E) = O(E)
    }

    private int padre(int posicion) {
        // Devolvemos el padre de una posición en el heap.
        return (posicion - 1) / 2;

        // Complejidad Total: O(1)
    }

    private int hijo_izq(int posicion) {
        // Devolvemos el hijo izquierdo de una posición en el heap.
        return 2 * posicion + 1;

        // Complejidad Total: O(1)
    }

    private int hijo_der(int posicion) {
        // Devolvemos el hijo derecho de una posición en el heap.
        return 2 * posicion + 2;

        // Complejidad Total: O(1)
    }

    private void intercambiar(int padre, int hijo){
        // Dadas dos posiciones del heap, intercambiamos los Handles y actualizamos las posiciones

        aed.Handle<T> nuevo_puntaje = _estudiantes.get(hijo);

        _estudiantes.set(hijo, _estudiantes.get(padre));
        _estudiantes.set(padre, nuevo_puntaje);

        _estudiantes.get(hijo).actualizarPosicion(hijo);
        _estudiantes.get(padre).actualizarPosicion(padre);

        // Complejidad Total: O(1)
    }

    public ListaOrdenada<T> heapToList(){
        // Dado un heap (un array), creamos una lista ordenada y copiamos todos los Handles de ese heap a la lista ordenada.
        // De esta manera, queda en primer lugar la lista ordenada por id (al igual que el heap en un primer momento) 
        // y además, los handles que están en el heap son los mismos que en la lista (solo que van a estar en diferentes posiciones).
        // Entonces, si yo cambio algún valor de un Handle que está en el heap, se va a cambiar en ese mismo Handle de la lista ordenada.

        // Una vez que tenemos el heap y la lista ordenada por id, ahora sí corremos el algoritmo de Floyd sobre el heap para que quede ordenado correctamente.


        ListaOrdenada<T> res = new ListaOrdenada<T>(_estudiantes.size()); // -- O(E)

        for (int i = 0; i < _estudiantes.size(); i++){ // -- E * O(1) = O(E)
            res.cambiarValor(i,  (MinHeap<T>.HandleHeap) _estudiantes.get(i));
        }

        algoritmoDeFloyd(); // -- O(E)

        return res;

        // Complejidad Total: O(E) + O(E) + O(E) = O(E)
    }

    public void algoritmoDeFloyd(){
        // Algoritmo para crear un minHeap dado un array.

        // Tomamos todos los padres de todos los árboles y subárboles (ordenamos a los padres de abajo hacia arriba en el orden del arbol) 
        // y los bajamos e intercambiamos según corresponda para mantener el invariante de representación de un minHeap.

        int primerPadre = padre(_tamaño - 1);

        for (int i = primerPadre; i >= 0; i--) {
            bajar(i);
        }

        // Complejidad Total: O(E) por definición del algoritmoDeFloyd
    }

    private void bajar(int padre){
        // Dado un padre de un árbol (un minHeap), lo bajamos en el árbol recursivamente hasta que sus hijos sean ambos mayores que él 
        // o hasta que ese mismo elemento se convierta en una hoja del árbol. Este es nuestro caso base, y sucede cuando la posición del hijo izquierdo
        // es mayor a la longitud del heap, es decir, esa posición no existe, y por lo tanto, ese elemento no tiene hijos, pues el árbol es izquierdista

        // Caso Base

        // Si mi elemento no tiene hijos
        if (hijo_izq(padre) >= _tamaño) return;

        // Casos recursivos


        // Si mi elemento solo tiene un hijo izquierdo
        else if (hijo_izq(padre) == _tamaño - 1) {
            if (_estudiantes.get(padre).valor().compareTo(_estudiantes.get(hijo_izq(padre)).valor()) == -1){
                intercambiar(padre, hijo_izq(padre));
            }
        }

        // Si tengo dos hijos y uno de ellos es menor al elemento que estoy analizando.
        else {
            if (_estudiantes.get(padre).valor().compareTo(_estudiantes.get(hijo_izq(padre)).valor()) == -1 ||
                _estudiantes.get(padre).valor().compareTo(_estudiantes.get(hijo_der(padre)).valor()) == -1) {

                if(_estudiantes.get(hijo_izq(padre)).valor().compareTo(_estudiantes.get(hijo_der(padre)).valor()) == 1) {
                    intercambiar(padre, hijo_izq(padre));
                    bajar(hijo_izq(padre));
                }
                else {
                    intercambiar(padre, hijo_der(padre));
                    bajar(hijo_der(padre));
                }
            }
        }

        // O(log E) por definición de la operación bajar
    }

    public aed.Handle<T> desencolar(){
        // Desencolo del Handle. Lo que hacemos es "sacar" el estudiante en la primera posición del heap, es decir, quién está en la raíz.
        // Pero en realidad no es que lo sacamos del array del heap, pues si quisiera tener un heap sin ese estudiante, debería de crear
        // un nuevo array con los estudiantes que de verdad se encuentran en l heap, pero eso superaría la complejidad permitida.

        // Lo que hacemos entonces, es decir que el estudiante que desencolamos tiene como posición en el heap al -1, y vamos a cambiar su contenido a null.
        // La manera que tenemos de conocer la cantidad de estudiantes en el heap es mediante el atributo _tamaño, el cual se va restando cuando se desencola.
        
        // Para poder hacer todo esto, nos dirijimos a la última posición (donde está el último estudiante) y lo intercambio con el primer lugar (la raíz).
        // Cuando actualizo las posiciones en el array, y le pongo null al estudiante desencolado, tengo que sobre mi nueva raíz, bajar a ese elemento 
        // hasta que se cumpla el invariante de representación del heap, es decir, hasta que mi array sea justamente un heap. Finalmente, devuelvo al estudiante desencolado.

        if (_tamaño > 0){
            aed.Handle<T> aDevolver = _estudiantes.get(0);

            _estudiantes.get(0).actualizarPosicion(-1);

            if (_tamaño == 1){
                _estudiantes.set(0, null);
                _tamaño = _tamaño - 1;
            }
            else {
                _estudiantes.set(0, _estudiantes.get(_tamaño - 1));
                _estudiantes.get(0).actualizarPosicion(0);
                _estudiantes.set(_tamaño - 1, null);

                _tamaño = _tamaño - 1;

                bajar(0); // -- O(log E)
            }

            return aDevolver;
        }

        return null;

        // Complejidad Total: O(log E)
    }

    public void encolar(aed.Handle<T> elemento){
        // Encuentro el primer null que hay en mi lista y lo piso con mi elemento a encolar. 
        // Como para encolar requiero que se haya desencolado, entonces _puntajes[tamaño] nunca se va a ir de rango y va a ser exactamente mi primer null del heap. 

        _estudiantes.set(_tamaño, elemento);
        _estudiantes.get(_tamaño).actualizarPosicion(_tamaño);
        _tamaño = _tamaño + 1;

        subir(_tamaño - 1); // -- O(log E)

        // Complejidad Total: O(log E)
    }

    private void subir(int posicion){
       // Dado un estudiante (una posición en el heap), lo subo en el árbol hasta que su padre sea menor que él o que se convierta en la raíz del árbol.
        while (posicion != 0 && _estudiantes.get(padre(posicion)).valor().compareTo(_estudiantes.get(posicion).valor()) == -1){ // log E * O(1) = O(log E)
            intercambiar(padre(posicion), posicion);

            posicion = padre(posicion);
        }
        // Complejidad Total: O(log E)
    }

     public void actualizar(int posicion){
        // Dado un estudiante, quiero actualizar su posición en el heap porque se ćambió su puntaje o su estado de entrega.

        // Cuando quiero actualizar la posición, en nuestro caso en partícular, el puntaje de un estudiante nunca puede bajar, solo subir,
        // y lo mismo para entregar, no puedo pasar de haber entregado a no haberlo hecho. Luego, simplemente puedo bajar a esa persona que actualizo.
        
        bajar(posicion); // -- O(log E)

        // Complejidad Total: O(log E)
    }
}


