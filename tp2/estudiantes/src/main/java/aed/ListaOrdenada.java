package aed;

import java.util.ArrayList;

//import aed.MinHeap.HandleHeap;

public class ListaOrdenada<T extends Comparable<T>> {

    private ArrayList<MinHeap<T>.HandleHeap> valores;
    
    
    public ListaOrdenada(int tamaño){
        valores = new ArrayList<MinHeap<T>.HandleHeap>(tamaño); // -- O(tamaño)
    }

    public void cambiarValor(int posicion, MinHeap<T>.HandleHeap valor){
        if (posicion >= valores.size()) {
            valores.add(valor); // -- O(1) pues, en nuestro caso, siempre hay una posición disponible
        }
        else {
            valores.set(posicion, valor); // -- O(1)
        }

         // Complejidad Total: O(1)

    }

    public MinHeap<T>.HandleHeap accederAPosicion(int posicion){
        return valores.get(posicion); // -- O(1)
    }

    public int longitud(){
        return valores.size(); // -- O(1)
    }
}
