package aed;

public class IdYPuntaje {
    private int _puntaje;
    private int _id;

    public IdYPuntaje(int puntaje, int id){
        _puntaje = puntaje;
        _id = id;
    }

    public boolean esMenorQue(IdYPuntaje otro) {
        if (otro == null) {
            throw new IllegalArgumentException("No puede compararse con null");
        }

        else if (this._puntaje != otro._puntaje) {
            return this._puntaje < otro._puntaje;
        }

        return this._id < otro._id;
    }
}
