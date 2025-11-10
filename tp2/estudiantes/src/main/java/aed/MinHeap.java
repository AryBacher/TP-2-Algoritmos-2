package aed;

public class MinHeap {
    private int[] _puntajes;
    private int[] _ids;
    private Estudiante[] _estudiantes;
    private int _tamaño;

    // FIJARSE QUE HAY QUE TENER EL ID TAMBIÉN PARA DESEMPATAR POR ID.
    // ME SIRVE TAMBIÉN COMO MI VUELTA A LA LISTA DE HANDLES. ACORDARSE DE ESTO

    public MinHeap(int[] puntajes) {
        _puntajes = puntajes;
        _tamaño = _puntajes.length;
        
        _ids = new int[_tamaño];
        for (int i = 0; i <= _tamaño; i++){
            _ids[i] = i;
        }

        int primerPadre = padre(_tamaño - 1);

        for (int i = primerPadre; i >= 0; i--) {
            heapify(_puntajes[i]);
        }
    }

    public class HandleHeap {
        private Estudiante _estudiante;
        private int _posicionHeap;

        private HandleHeap(Estudiante est, int posicion){
            _estudiante = est;
            _posicionHeap = posicion;
        }

        public void ejemplo(){
            MinHeap.this.heapify(1);
        }
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

    private void heapify(int padre){
        // Caso Base
        if (hijo_izq(padre) >= _tamaño) return;
        
        // Caso recursivo
        else if (hijo_izq(padre) == _tamaño - 1) {
            if (_puntajes[padre] > _puntajes[hijo_izq(padre)]) {
                int nuevo_padre = _puntajes[hijo_izq(padre)];

                _puntajes[hijo_izq(padre)] = _puntajes[padre];
                _puntajes[padre] = nuevo_padre;

                _ids[hijo_izq(padre)] = _ids[padre];
                _ids[padre] = nuevo_padre;
            }
        }

        else {
            if (_puntajes[padre] > _puntajes[hijo_izq(padre)] || _puntajes[padre] > _puntajes[hijo_der(padre)]) {
                if(hijo_izq(padre) < hijo_der(padre)) {
                    int nuevo_padre = _puntajes[hijo_izq(padre)];

                    _puntajes[hijo_izq(padre)] = _puntajes[padre];
                    _puntajes[padre] = nuevo_padre;

                    _ids[hijo_izq(padre)] = _puntajes[padre];
                    _ids[padre] = nuevo_padre;

                    heapify(hijo_izq(padre));
                } 
                
                else {
                    int nuevo_padre = _puntajes[hijo_der(padre)];

                    _puntajes[hijo_der(padre)] = _puntajes[padre];
                    _puntajes[padre] = nuevo_padre;

                    _ids[hijo_der(padre)] = _puntajes[padre];
                    _ids[padre] = nuevo_padre;

                    heapify(hijo_der(padre));
                }
            }
        }
    }

    public int desencolar(){
        int aDevolver = _puntajes[0];
        
        _puntajes[0] = _puntajes[_tamaño - 1]; 
        _puntajes[_tamaño - 1] = -1;

        _tamaño = _tamaño - 1;

        heapify(0);

        return aDevolver;
    }

    public void encolar(int elemento){
        // Encuentro el primer -1 que hay en mi lista y lo piso con mi elemento a encolar. 
        // Como para encolar requiero que se haya desencolado, entonces _puntajes[tamaño] nunca se va a ir de rango. 
        _puntajes[_tamaño] = elemento;
        _tamaño = _tamaño + 1;

        subir(_tamaño - 1);
    }

    private void subir(int posicion){
        while (posicion != 0 && _puntajes[padre(posicion)] > _puntajes[posicion]){
            int nuevo_padre = _puntajes[posicion];

            _puntajes[posicion] = _puntajes[padre(posicion)];
            _puntajes[padre(posicion)] = nuevo_padre;

            posicion = padre(posicion);
        }
    }

    public void actualizar(int posicion){
        // Cuando actualizo la posición, tu puntaje nunca puede bajar, solo subir
        // Luego, simplemente puedo heapificar.
        heapify(posicion);
    }
}
