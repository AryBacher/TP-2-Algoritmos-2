package aed;
import java.util.ArrayList;
import aed.MinHeap.Handle;

public class Edr {
    private ListaOrdenada<MinHeap.Handle> _listaOrdenada;
    private MinHeap _minHeap;
    private int _ladoAula;

    public Edr(int LadoAula, int Cant_estudiantes, int[] ExamenCanonico) {
        _ladoAula = LadoAula;

        Estudiante[] listaEstudiantes = listaDeEstudiantes(Cant_estudiantes, Cant_estudiantes);

        _minHeap = new MinHeap(listaEstudiantes);
        _listaOrdenada = _minHeap.heapToList();
        // _listaOrdenada = new ListaOrdenada<>(Cant_estudiantes, null)
    }

    public Estudiante[] listaDeEstudiantes(int cantEstudiantes, int cantRespuestas){
        Estudiante[] listaDeEstudiantes = new Estudiante[cantEstudiantes];

        for (int i = 0; i < cantEstudiantes; i ++){
            listaDeEstudiantes[i] = new Estudiante(i, cantRespuestas);
        }

        return listaDeEstudiantes;
    }

    // public MinHeap.Handle crearHeap(int id, int cantRespuestas, int cantEstudiantes){
        
        
    //     // MinHeap heap = new MinHeap(new Estudiante(id, cantRespuestas));
    //     // MinHeap heap = new MinHeap(null);
    //     // return heap.new Handle(new Estudiante(id, cantRespuestas), id);
    // }

//-------------------------------------------------NOTAS--------------------------------------------------------------------------

    public double[] notas(){
        throw new UnsupportedOperationException("Sin implementar");
    }

//------------------------------------------------COPIARSE------------------------------------------------------------------------



    public void copiarse(int estudiante) {
        throw new UnsupportedOperationException("Sin implementar");
    }


//-----------------------------------------------RESOLVER----------------------------------------------------------------




    public void resolver(int estudiante, int NroEjercicio, int res) {
        throw new UnsupportedOperationException("Sin implementar");
    }



//------------------------------------------------CONSULTAR DARK WEB-------------------------------------------------------

    public void consultarDarkWeb(int n, int[] examenDW) {
        throw new UnsupportedOperationException("Sin implementar");
    }
 

//-------------------------------------------------ENTREGAR-------------------------------------------------------------

    public void entregar(int estudiante) {
        throw new UnsupportedOperationException("Sin implementar");
    }

//-----------------------------------------------------CORREGIR---------------------------------------------------------

    public NotaFinal[] corregir() {
        throw new UnsupportedOperationException("Sin implementar");
    }

//-------------------------------------------------------CHEQUEAR COPIAS-------------------------------------------------

    public int[] chequearCopias() {
        throw new UnsupportedOperationException("Sin implementar");
    }
}
