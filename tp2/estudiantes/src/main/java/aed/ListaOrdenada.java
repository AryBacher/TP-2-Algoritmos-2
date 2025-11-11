package aed;

import aed.MinHeap.Handle;

public class ListaOrdenada<T> {
    private MinHeap.Handle[] valores;

    public ListaOrdenada(int tamaño){
        valores = new Handle[tamaño];
    }

    public void cambiarValor(int posicion, Handle valor){
        valores[posicion] = valor; 
    }

    public MinHeap.Handle accederAPosicion(int posicion){
        return valores[posicion];
    }

    public int longitud(){
        return valores.length;
    }
}
