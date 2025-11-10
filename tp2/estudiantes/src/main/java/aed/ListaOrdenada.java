package aed;

import aed.MinHeap.Handle;

public class ListaOrdenada<T> {
    private MinHeap.Handle[] valores;

    public ListaOrdenada(int tamaño, Handle[] estudiantes){
        valores = new Handle[tamaño];

        for (int i = 0; i < tamaño; i++){
            valores[i] = estudiantes[i];
        }
    }

    public MinHeap.Handle accederAPosicion(int posicion){
        return valores[posicion];
    }
}
