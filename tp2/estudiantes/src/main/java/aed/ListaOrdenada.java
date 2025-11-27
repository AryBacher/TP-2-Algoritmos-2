package aed;

import java.util.ArrayList;

public class ListaOrdenada<T extends Comparable<T>> {

    private ArrayList<Handle<T>> valores;
    
    
    public ListaOrdenada(int tamaño){
        valores = new ArrayList<Handle<T>>(tamaño); // -- O(tamaño)
    }

    public void cambiarValor(int posicion, Handle<T> valor){
        if (posicion >= valores.size()) {
            valores.add(valor); // -- O(1) pues, en nuestro caso, siempre hay una posición disponible
        }
        else {
            valores.set(posicion, valor); // -- O(1)
        }

         // Complejidad Total: O(1)

    }

    public Handle<T> accederAPosicion(int posicion){
        return valores.get(posicion); // -- O(1)
    }

    public int longitud(){
        return valores.size(); // -- O(1)
    }
}
