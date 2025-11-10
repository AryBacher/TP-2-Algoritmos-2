package aed;

public class Estudiante {

    private int _id;
    private int _puntaje;
    private boolean _entrego;
    private int[] _respuestas;

    public Estudiante(int id, int cantRespuestas) {
        _puntaje = 0;
        _entrego = false;
        _id = id;

        for (int i = 0; i < cantRespuestas; i ++){
            _respuestas[i] = -1;
        }
    }

    public int puntaje() {return _puntaje;}
    public boolean entrego() {return _entrego;}
    public int[] respuestas() {return _respuestas;}
    public int id() {return _id;}
}