package aed;

public class NotaFinal implements Comparable<NotaFinal> {
    public double _nota;
    public int _id;

    public NotaFinal(double nota, int id){
        _nota = nota;
        _id = id;

        // Complejidad Total: O(1)
    }

    public int compareTo(NotaFinal otra){
        if (otra._id != this._id){
            return this._id - otra._id;
        }
        return Double.compare(this._nota, otra._nota);

        // Complejidad Total: O(1)
    }

    @Override
    public boolean equals(Object otra) {

        if (otra == null) {return false;}

        if (otra.getClass() != this.getClass()){return false;}

        NotaFinal otraNota = (NotaFinal) otra;

        if (this._id == otraNota._id && this._nota == otraNota._nota) {return true;}

        return false;

        // Complejidad Total: O(1)
    }
}
