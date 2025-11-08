package aed;

public class Estudiante {

    private int _nota;
    private boolean _entrego;
    private int[] _respuestas;

    public Estudiante(int nota, boolean entrego, int[] respuestas) {
        _nota = nota;
        _entrego = entrego;
        _respuestas = respuestas;
    }
}