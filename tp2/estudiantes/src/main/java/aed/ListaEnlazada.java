package aed;

public class ListaEnlazada<T> {
    // Completar atributos privados
    private Nodo primero;
    private Nodo ultimo;
    private int longitud;

    private class Nodo {
        T elemento;
        Nodo siguiente;
        Nodo anterior;

        Nodo(T elem) {
            this.elemento = elem;
        }
    }

    public ListaEnlazada() {
        longitud = 0;
        primero = null;
        ultimo = null;
    }

    public int longitud() {
        return longitud;
    }

    public void agregarAdelante(T elem) {
        Nodo nuevoPrimero = new Nodo(elem);
        if (longitud == 0) {
            ultimo = nuevoPrimero;
            primero = nuevoPrimero;
        } else {
            nuevoPrimero.anterior = null;
            nuevoPrimero.siguiente = primero;
            primero.anterior = nuevoPrimero;
            primero = nuevoPrimero;
        }
        longitud++;
    }

    public void agregarAtras(T elem) {
        Nodo nuevoUltimo = new Nodo(elem);
        if (longitud == 0) {
            primero = nuevoUltimo;
            ultimo = nuevoUltimo;
        } else {
            nuevoUltimo.siguiente = null;
            nuevoUltimo.anterior = ultimo;
            ultimo.siguiente = nuevoUltimo;
            ultimo = nuevoUltimo;
        }
        longitud++;
    }

    public T obtener(int i) {
        Nodo nodoActual = primero;
        for (int j = 0; j < i; j++) {
            nodoActual = nodoActual.siguiente;
        }
        return nodoActual.elemento;
    }

    public void eliminar(int i) {
        Nodo nodoAEliminar = primero;
        for (int j = 0; j < i; j++) {
            nodoAEliminar = nodoAEliminar.siguiente;
        }
        if (nodoAEliminar == primero && longitud > 2) {
            primero = primero.siguiente;
            primero.anterior = null;
        } else if (nodoAEliminar == primero && longitud == 2) {
            primero = ultimo;
            primero.anterior = null;
            primero.siguiente = null;
        } else if (nodoAEliminar == primero && longitud == 1) {
            primero = null;
            ultimo = null;
        } else if(nodoAEliminar == ultimo && longitud > 2) {
            ultimo = ultimo.anterior;
            ultimo.siguiente = null;
        } else if(nodoAEliminar == ultimo && longitud == 2) {
            ultimo = primero;
            ultimo.siguiente = null;
            ultimo.anterior = null;
        } else {
            nodoAEliminar.anterior.siguiente = nodoAEliminar.siguiente;
            nodoAEliminar.siguiente.anterior = nodoAEliminar.anterior;
        }
        longitud--;
    }

    public void modificarPosicion(int indice, T elem) {
        Nodo nodoActual = primero;
        for (int j = 0; j < indice; j++) {
            nodoActual = nodoActual.siguiente;
        }
        nodoActual.elemento = elem;
    }

    public ListaEnlazada(ListaEnlazada<T> lista) {
        this.longitud = 0;
        this.primero = null;
        this.ultimo = null;
        Nodo nodoActual = lista.primero;
        while (nodoActual != null) {
            this.agregarAtras(nodoActual.elemento);
            nodoActual = nodoActual.siguiente;
        }
    }
    
    @Override
    public String toString() {
        String resultado = "[";
        Nodo nodoActual = primero;
        while (nodoActual != null) {
            resultado += nodoActual.elemento;
            nodoActual = nodoActual.siguiente;
            if (nodoActual != null) {
                resultado += ", ";
            } else {
                resultado += "]";
            }
        }
        return resultado;
    }

    public class ListaIterador{
    	// Completar atributos privados
        private Nodo siguiente;
        private Nodo anterior;

        public boolean haySiguiente() {
            return siguiente != null;
        }
        
        public boolean hayAnterior() {
	        return anterior != null;
        }

        public T siguiente() {
            // Guardo el valor
            T elem = siguiente.elemento;
            // Avanzo los punteros
	        anterior = siguiente;               // el que devolvimos pasa a ser "anterior"
            siguiente = siguiente.siguiente;    // avanzamos el cursor
            return elem;
        }
        

        public T anterior() {
            // Guardo el valor
            T elem = anterior.elemento;
            // Retrocedo los punteros
	        siguiente = anterior;          // al retroceder, ese pasa a ser el "siguiente"
            anterior = anterior.anterior;  // y corremos el "anterior" hacia atrás
            return elem;
        }
    }

    public ListaIterador iterador() {
        ListaIterador iterador = new ListaIterador();
        iterador.siguiente = primero;
        iterador.anterior = null;
        return iterador;
    }

}
