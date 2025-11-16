package aed;

public class Estudiante implements Comparable<Estudiante>{

    private int _id;
    private double _puntaje;
    private boolean _entrego;
    private int[] _respuestas;
    private int _cantRespuestasCorrectas;

    public Estudiante(int id, int cantRespuestas) {
        _puntaje = 0;
        _entrego = false;
        _id = id;
        _respuestas = new int[cantRespuestas]; // -- O(R)
        _cantRespuestasCorrectas = 0;

        for (int i = 0; i < cantRespuestas; i ++){ // R * O(1) = O(R)
            _respuestas[i] = -1;
        }

        // Complejidad Total: O(R) + O(R) = O(R) 
    }

    public double puntaje() {return _puntaje;} // Complejidad Total: O(1)
    public boolean entrego() {return _entrego;} // Complejidad Total: O(1)
    public int[] respuestas() {return _respuestas;} // Complejidad Total: O(1)
    public int id() {return _id;} // Complejidad Total: O(1)

    public void actualizarRespuestaRapido(int ejercicio, int respuesta, int[] examenCanonico){
        // Actualizo solo una respuesta de un ejercicio y el puntaje del estudiante
        _respuestas[ejercicio] = respuesta;
        
        if(examenCanonico[ejercicio] == _respuestas[ejercicio]){
            _cantRespuestasCorrectas ++;
            _puntaje = Math.floor(_cantRespuestasCorrectas * 100 / examenCanonico.length);
        }

        // Complejidad Total: O(1)
    }

    public void actualizarRespuestas(int[] examenCanonico, int[] examen){
        // Actualizo todas las respuestas sobre el examen del estudiante y su puntaje.
        int cantRespuestasCorrectas = 0;
        for (int i = 0; i < examenCanonico.length; i ++){ // R * O(1) = O(R)
            _respuestas[i] = examen[i];

            if (_respuestas[i] == examenCanonico[i]){
                cantRespuestasCorrectas ++;
            }
        }
        _cantRespuestasCorrectas = cantRespuestasCorrectas;
        _puntaje = Math.floor(cantRespuestasCorrectas * 100 / examenCanonico.length);

        // Complejidad Total: O(R)
    }

    public void entregar(){_entrego = true;} // Complejidad Total: O(1)

    @Override
    public int compareTo(Estudiante otro) {
        // Criterio de comparación para el Estudiante del MinHeap. 

        // El primer criterio es si el estudiante entregó, el segundo la menor nota y el tercero el menor id

        // Necesitamos que el primer criterio de comparación sea si entrego, pues en la operación entregar necesitamos 
        // que el estudiante que entregue se mueva hacia la raíz para poder desencolarlo del heap.

        if (otro == null) {
            throw new IllegalArgumentException("No puede compararse con null");
        }

        else if (this._entrego != otro._entrego){
            if (this._entrego == true) {return 1;}
            return -1;
        }

        else if (this._puntaje != otro._puntaje) {
            if (this._puntaje < otro._puntaje) {return 1;}
            return -1;
        }

        else{
            if (this._id < otro._id) {return 1;}
            return -1;
        }

        // Complejidad Total: O(1)
    }
}
