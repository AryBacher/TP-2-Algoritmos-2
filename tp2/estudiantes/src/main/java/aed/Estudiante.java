package aed;

public class Estudiante {

    private int _id;
    private int _nota;
    private boolean _entrego;
    private int[] _respuestas;

    public Estudiante(int nota, boolean entrego, int[] respuestas, int id) {
        _nota = nota;
        _entrego = entrego;
        _respuestas = respuestas;
        _id = id;
    }

    public Estudiante compareTo(Estudiante otro){
            if(otro == null){
                String mensajeDeError = "No puede compararse con null";
                throw new IllegalArgumentException(mensajeDeError);
            }

            else if (this._nota != otro._nota){
                if (this._nota < otro._nota) {return this;}
                else {return otro;}
            }

            else {
                if (this._id < otro._id){return this;}
                else {return otro;}
            }
    }
}