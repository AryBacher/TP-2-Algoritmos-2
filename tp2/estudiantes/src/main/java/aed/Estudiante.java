package aed;

public class Estudiante {

    private int _id;
    private int _puntaje;
    private boolean _entrego;
    private int[] _respuestas;
    private int _cantRespuestasCorrectas;

    public Estudiante(int id, int cantRespuestas) {
        _puntaje = 0;
        _entrego = false;
        _id = id;
        _respuestas = new int[cantRespuestas];
        _cantRespuestasCorrectas = 0;

        for (int i = 0; i < cantRespuestas; i ++){
            _respuestas[i] = -1;
        }
    }

    public int puntaje() {return _puntaje;}
    public boolean entrego() {return _entrego;}
    public int[] respuestas() {return _respuestas;}
    public int id() {return _id;}

    public void cambiarRespuesta(int posicion, int respuesta){
        _respuestas[posicion] = respuesta;
    }

    public void actualizarPuntaje(int[] examenCanonico){
        int cantRespuestasCorrectas = 0;
        for (int i = 0; i < examenCanonico.length; i ++){
            if (_respuestas[i] == examenCanonico[i]){
                cantRespuestasCorrectas ++;
            }
        }
        _cantRespuestasCorrectas = cantRespuestasCorrectas;
        _puntaje = (int) Math.floor(cantRespuestasCorrectas * 100 / examenCanonico.length);
    }
    
    public void actualizarPuntajeRapido(int[] examenCanonico, int ejercicio){
        if(examenCanonico[ejercicio] == _respuestas[ejercicio]){
            _cantRespuestasCorrectas ++;
            _puntaje = (int) Math.floor(_cantRespuestasCorrectas * 100 / examenCanonico.length);
        }
    }
}
