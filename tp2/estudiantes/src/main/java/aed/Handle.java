package aed;

interface Handle<T> {    
    /**
     * Devuelve el valor del elemento
     * 
     */
    public T valor();

    /**

    Actualiza el valor del elemento adentro del Heap

    */
    public void actualizarValor(T nuevoValor);
}

