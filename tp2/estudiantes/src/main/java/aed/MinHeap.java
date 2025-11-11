package aed;

public class MinHeap {
    private IdYPuntaje[] _estudiantes;
    private int _tamaño;

    // FIJARSE QUE HAY QUE TENER EL ID TAMBIÉN PARA DESEMPATAR POR ID.
    // ME SIRVE TAMBIÉN COMO MI VUELTA A LA LISTA DE HANDLES

    public MinHeap(IdYPuntaje[] estudiantes) {
        _estudiantes = estudiantes;
        _tamaño = _estudiantes.length;

        int primerPadre = padre(_tamaño - 1);

        for (int i = primerPadre; i >= 0; i--) {
            heapify(i);
        }
    }

    public class Handle {
        private Estudiante _estudiante;
        private int _posicionHeap;

        private Handle(Estudiante est, int posicion){
            _estudiante = est;
            _posicionHeap = posicion;
        }

        // public boolean esMenorQue(Handle otro) {
        //     if (otro == null) {
        //         throw new IllegalArgumentException("No puede compararse con null");
        //     }

        //     else if (this._estudiante.puntaje() != otro._estudiante.puntaje()) {
        //         return this._estudiante.puntaje() < otro._estudiante.puntaje();
        //     }

        //     return this._estudiante.id() < otro._estudiante.id();
        // }

        // public int puntaje(){
        //     return _estudiante.puntaje();
        // }

        // public void actualizarPosicion(int posicion){
        //     _posicionHeap = posicion;
        // }

        public Estudiante estudiante() {return _estudiante;}
        public int posicionHeap() {return _posicionHeap;}

        // public void ejemplo(){
        //     MinHeap.this.heapify(1);
        // }
    }

    private int padre(int posicion) {
        return (posicion - 1) / 2; 
    }

    private int hijo_izq(int posicion) {
        return 2 * posicion + 1;
    }

    private int hijo_der(int posicion) {
        return 2 * posicion + 2;
    }

    private void intercambiar(int padre, int hijo){
        IdYPuntaje nuevo_puntaje = _estudiantes[hijo];
        
        _estudiantes[hijo] = _estudiantes[padre];
        _estudiantes[padre] = nuevo_puntaje;

        _estudiantes[hijo].actualizarPosicion(hijo, padre);
        _estudiantes[padre].actualizarPosicion(padre, hijo);

        // Caso 1
        // Handle nuevo_puntaje = _estudiantes[hijo_izq(padre)];
                
        // _estudiantes[hijo_izq(padre)] = _estudiantes[padre];
        // _estudiantes[padre] = nuevo_puntaje;


        // Caso 2
        // Handle nuevo_puntaje = _estudiantes[hijo_der(padre)];
                
        // _estudiantes[hijo_der(padre)] = _estudiantes[padre];
        // _estudiantes[padre] = nuevo_puntaje;
    }

    private void heapify(int padre){
        // Caso Base
        if (hijo_izq(padre) >= _tamaño) return;
        
        // Caso recursivo
        else if (hijo_izq(padre) == _tamaño - 1) {
            if (! _estudiantes[padre].esMenorQue(_estudiantes[hijo_izq(padre)])){
                intercambiar(padre, hijo_izq(padre));
            }
        }

        else {
            if (! _estudiantes[padre].esMenorQue(_estudiantes[hijo_izq(padre)]) || ! _estudiantes[padre].esMenorQue(_estudiantes[hijo_der(padre)])) {
                if(_estudiantes[hijo_izq(padre)].esMenorQue(_estudiantes[hijo_der(padre)])) {
                    intercambiar(padre, hijo_izq(padre));

                    heapify(hijo_izq(padre));
                } 
                
                else {
                    intercambiar(padre, hijo_der(padre));

                    heapify(hijo_der(padre));
                }
            }
        }
    }

    public IdYPuntaje desencolar(){
        if (_tamaño > 0){
            IdYPuntaje aDevolver = _estudiantes[0];

            if (_tamaño == 1){
                _estudiantes[0] = null;    
            }

            else {
                _estudiantes[0] = _estudiantes[_tamaño - 1]; 
                _estudiantes[_tamaño - 1] = null;

                _tamaño = _tamaño - 1;

                heapify(0);
            }

            return aDevolver;
        }

        return null;
    }

    public void encolar(IdYPuntaje elemento){
        // Encuentro el primer -1 que hay en mi lista y lo piso con mi elemento a encolar. 
        // Como para encolar requiero que se haya desencolado, entonces _puntajes[tamaño] nunca se va a ir de rango. 
        _estudiantes[_tamaño] = elemento;
        _tamaño = _tamaño + 1;

        subir(_tamaño - 1);
    }

    private void subir(int posicion){
        while (posicion != 0 && ! _estudiantes[padre(posicion)].esMenorQue(_estudiantes[posicion])){
            intercambiar(padre(posicion), posicion);

            posicion = padre(posicion);
        }
    }

    public void actualizar(int posicion){
        // Cuando actualizo la posición, tu puntaje nunca puede bajar, solo subir
        // Luego, simplemente puedo heapificar.
        heapify(posicion);
    }
}
