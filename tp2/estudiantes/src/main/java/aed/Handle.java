package aed;

public interface Handle<T>{

    public T valor();
     /*

      Devuelve el valor almacenado por el handle.
     
     */

    public int posicionHeap();
      /*
     
     Devuelve la posición del elemento dentro del heap.
     
     */

    public void actualizarPosicion(int pos);
      /*
     
     Actualiza la posición del elemento dentro del heap.
     
     */
    
    public void actualizarValor(T nuevoValor);
      /*
     
     Actualiza el valor del elemento adentro del Heap
     
     */
}
