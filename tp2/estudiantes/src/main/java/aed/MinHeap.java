package aed;

import java.util.ArrayList;

public class MinHeap<T extends Comparable<T>> {
    private ArrayList<HandleHeap<T>> _heap;
    private int _tamaño;

    public MinHeap(T[] estudiantes) {
        // Creamos una lista de handles y en cada posicion le asignamos toda la información de un estudiante
        ArrayList<HandleHeap<T>> listaEstudiantes = new ArrayList<HandleHeap<T>>(estudiantes.length); // -- O(E)

        for (int i = 0; i < estudiantes.length; i ++){ // -- E * O(1) = O(E)
            listaEstudiantes.add(new HandleHeap<T>(estudiantes[i], i)); 
            // -- O(1) (real, no amortizado pues listaEstudiantes ya tiene espacio dispnible)
        }

        _heap = listaEstudiantes;
        _tamaño = _heap.size();
        // Complejidad Total: O(E) + O(E) = O(E)
    }

    public class HandleHeap<S extends Comparable<S>> implements Handle<S> {
        private S _valor;
        private int _posicionHeap;

        public HandleHeap(S est, int posicion){
            _valor = est;
            _posicionHeap = posicion;
            // Complejidad Total: O(1)
        }

        public void actualizarPosicion(int posicion){
            _posicionHeap = posicion; // Complejidad Total: O(1)
        }

        public S valor() {return _valor;} // Complejidad Total: O(1)
        public int posicionHeap() {return _posicionHeap;} // Complejidad Total: O(1)

        public void actualizarValor(S a){actualizar(_posicionHeap);}
        public void eliminarValor(int posicion) {eliminar(posicion);}

        // public void actualizarHeap(int posicion) {MinHeap.this.actualizar(posicion);} // Complejidad Total: O(log E)
        // public void subirHeap(int posicion) {MinHeap.this.subir(posicion);} // Complejidad Total: O(log E)
        // public Handle desencolarHeap() {return MinHeap.this.desencolar();} // Complejidad Total: O(log E)
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
        // Dadas dos posiciones de la lista del heap, intercambiamos esos dos estudiantes y les actualizamos su posición del heap (guardada en el handle)

        HandleHeap<T> nuevo_puntaje = _heap.get(hijo);
        
        _heap.set(hijo, _heap.get(padre));
        _heap.set(padre, nuevo_puntaje);

        _heap.get(hijo).actualizarPosicion(hijo);
        _heap.get(padre).actualizarPosicion(padre);

        // Complejidad Total: O(1)
    }

    public ListaOrdenada<T> heapToList(){
        // Dado un heap (un array), creamos una lista ordenada y copiamos todos los Handles de ese heap a la lista ordenada.

        // De esta manera, queda en primer lugar la lista ordenada por id (al igual que el heap en un primer momento)
        // y además, los handles que están en el heap son los mismos que en la lista (solo que van a estar en diferentes posiciones).
        // Entonces, si yo cambio algún valor de un Handle que está en el heap, se va a cambiar en ese mismo Handle de la lista ordenada.

        // Una vez que tenemos el heap y la lista ordenada por id, ahora sí corremos el algoritmo de Floyd sobre el heap para que quede ordenado correctamente.

        ListaOrdenada<T> res = new ListaOrdenada<T>(_heap.size()); // -- O(E)
        
        for (int i = 0; i < _heap.size(); i++){ // -- E * O(1) = O(E)
            res.cambiarValor(i, _heap.get(i));
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
            if (_heap.get(padre).valor().compareTo(_heap.get(hijo_izq(padre)).valor()) == -1){
                intercambiar(padre, hijo_izq(padre));
            }
        }

        // Si tengo dos hijos y uno de ellos es menor al elemento que estoy analizando.
        else {
            if (_heap.get(padre).valor().compareTo(_heap.get(hijo_izq(padre)).valor()) == -1 || _heap.get(padre).valor().compareTo(_heap.get(hijo_der(padre)).valor()) == -1) {
                if(_heap.get(hijo_izq(padre)).valor().compareTo(_heap.get(hijo_der(padre)).valor()) == 1) {
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

    public HandleHeap<T> desencolar(){
        // Desencolo del Handle. Lo que hacemos es "sacar" el estudiante en la primera posición del heap, es decir, quién está en la raíz.
        // Pero en realidad no es que lo sacamos del array del heap, pues si quisiera tener un heap sin ese estudiante, debería de crear
        // un nuevo array con los estudiantes que de verdad se encuentran en l heap, pero eso superaría la complejidad permitida.

        // Lo que hacemos entonces, es decir que el estudiante que desencolamos tiene como posición en el heap al -1, y vamos a cambiar su contenido a null.
        // La manera que tenemos de conocer la cantidad de estudiantes en el heap es mediante el atributo _tamaño, el cual se va restando cuando se desencola.
        
        // Para poder hacer todo esto, nos dirijimos a la última posición (donde está el último estudiante) y lo intercambio con el primer lugar (la raíz).
        // Cuando actualizo las posiciones en el array, y le pongo null al estudiante desencolado, tengo que sobre mi nueva raíz, bajar a ese elemento 
        // hasta que se cumpla el invariante de representación del heap, es decir, hasta que mi array sea justamente un heap. Finalmente, devuelvo al estudiante desencolado.

        if (_tamaño > 0){
            HandleHeap<T> aDevolver = _heap.get(0);

            _heap.get(0).actualizarPosicion(-1);

            if (_tamaño == 1){
                _heap.set(0, null);
                _tamaño = _tamaño - 1;
            }

            else {
                _heap.set(0, _heap.get(_tamaño - 1)); 
                _heap.get(0).actualizarPosicion(0);
                _heap.set(_tamaño - 1, null);

                _tamaño = _tamaño - 1;

                bajar(0); // -- O(log E)
            }

            return aDevolver;
        }

        return null;

        // Complejidad Total: O(log E)
    }

    public void encolar(HandleHeap<T> elemento){
        // Encuentro el primer null que hay en mi lista y lo piso con mi elemento a encolar. 
        // Como para encolar requiero que se haya desencolado, entonces _puntajes[tamaño] nunca se va a ir de rango y va a ser exactamente mi primer null del heap. 
        
        _heap.set(_tamaño, elemento);
        _heap.get(_tamaño).actualizarPosicion(_tamaño);
        _tamaño = _tamaño + 1;

        subir(_tamaño - 1); // -- O(log E)

        // Complejidad Total: O(log E)
    }

    public void subir(int posicion){
        // Dado un estudiante (una posición en el heap), lo subo en el árbol hasta que su padre sea menor que él o que se convierta en la raíz del árbol.
        while (posicion != 0 && _heap.get(padre(posicion)).valor().compareTo(_heap.get(posicion).valor()) == -1){ // log E * O(1) = O(log E)
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

    public void eliminar(int posicion){

        if (posicion == _tamaño - 1){
            _tamaño = _tamaño - 1;
        }
        
        else if (_tamaño == 1){
            _heap.set(0, null);
            _tamaño = _tamaño - 1;
        }

        else {
            intercambiar(posicion, _tamaño - 1);

            _tamaño = _tamaño - 1;

            subir(posicion);
            bajar(posicion);
        }
    }
}

