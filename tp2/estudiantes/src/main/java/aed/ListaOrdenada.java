package aed;

import java.util.ArrayList;

public class ListaOrdenada<T extends Comparable<T>> {
    private ArrayList<MinHeap<T>.Handle> valores;

    public ListaOrdenada(int tamaño){
        valores = new ArrayList<MinHeap<T>.Handle>(tamaño);
    }

    public void cambiarValor(int posicion, MinHeap<T>.Handle valor){
        if (posicion >= valores.size()) {
            valores.add(valor);
        }

        else {
            valores.set(posicion, valor);   
        }
    }

    public MinHeap<T>.Handle accederAPosicion(int posicion){
        return valores.get(posicion);
    }

    public int longitud(){
        return valores.size();
    }
}
