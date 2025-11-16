package aed;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

public class TestsPropios {
   Edr edr;
   int d_aula;
   int cant_alumnos;
   int[] solucion;

   public TestsPropios() {
   }

   @BeforeEach
   void setUp() {
      this.d_aula = 7;
      this.cant_alumnos = 4;
      this.solucion = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
      this.edr = new Edr(this.d_aula, this.cant_alumnos, this.solucion);
   }

   @Test
   void alumnos_no_se_pueden_copiar() {
      this.edr.resolver(0, 0, 0);
      this.edr.resolver(1, 0, 0);
      this.edr.resolver(2, 0, 0);
      this.edr.resolver(3, 0, 0);
      double[] notas = this.edr.notas();
      double[] notas_esperadas = new double[]{10.0, 10.0, 10.0, 10.0};
      Assertions.assertTrue(Arrays.equals(notas_esperadas, notas));
      this.edr.copiarse(1);
      notas = this.edr.notas();
      Assertions.assertTrue(Arrays.equals(notas_esperadas, notas));
      this.cant_alumnos = 1;
      this.solucion = new int[1];
      this.edr = new Edr(this.d_aula, this.cant_alumnos, this.solucion);
      this.edr.copiarse(0);
      notas = this.edr.notas();
      notas_esperadas = new double[]{0.0};
      Assertions.assertTrue(Arrays.equals(notas_esperadas, notas));
   }

   @Test
   void resuelve_bien_ejercicio() {
      this.edr.resolver(0, 0, 0);
      this.edr.resolver(1, 1, 1);
      this.edr.resolver(2, 2, 2);
      this.edr.resolver(3, 3, 3);
      double[] notas = this.edr.notas();
      double[] notas_esperadas = new double[]{10.0, 10.0, 10.0, 10.0};
      Assertions.assertTrue(Arrays.equals(notas_esperadas, notas));
   }

   @Test
   void resuelve_mal_ejercicio() {
      this.edr.resolver(0, 0, 3);
      this.edr.resolver(1, 1, 2);
      this.edr.resolver(2, 2, 1);
      this.edr.resolver(3, 3, 0);
      double[] notas = this.edr.notas();
      double[] notas_esperadas = new double[]{0.0, 0.0, 0.0, 0.0};
      Assertions.assertTrue(Arrays.equals(notas_esperadas, notas));
   }

   @Test
   void desempatar_id_DarkWeb() {
      this.edr.resolver(0, 0, 3);
      this.edr.resolver(1, 1, 2);
      this.edr.resolver(2, 2, 1);
      this.edr.resolver(3, 3, 0);
      this.edr.consultarDarkWeb(3, this.solucion);
      double[] notas = this.edr.notas();
      double[] notas_esperadas = new double[]{100.0, 100.0, 100.0, 0.0};
      Assertions.assertTrue(Arrays.equals(notas_esperadas, notas));
   }


@Test
void test_Copiones_Solo_Del_De_Adelante() {
    int dAula = 5; 
    int cantAlumnos = 4;
    int[] solucion = {1,2,3,4}; 
    Edr edr = new Edr(dAula, cantAlumnos, solucion);

    edr.resolver(0, 0, 1);
    edr.resolver(0, 1, 1);
    edr.resolver(0, 2, 1);
    edr.resolver(0, 3, 1);

    
    edr.copiarse(1);
    edr.copiarse(2);
    edr.copiarse(3);

    int[] copiones = edr.chequearCopias();

    int[] esperados = {1, 2, 3};

    assertTrue(Arrays.equals(esperados, copiones));
}
@Test
   void alumno_se_copia_adelante() {
      this.d_aula = 5;
      this.cant_alumnos = 8;
      this.solucion = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
      this.edr = new Edr(this.d_aula, this.cant_alumnos, this.solucion);
      this.edr.resolver(0, 0, 0);
      this.edr.resolver(1, 1, 1);
      this.edr.resolver(2, 2, 2);
      this.edr.resolver(3, 3, 3);
      this.edr.resolver(4, 4, 4);
      this.edr.resolver(4, 5, 5);
      this.edr.resolver(5, 5, 5);
      this.edr.resolver(6, 6, 6);
      this.edr.resolver(7, 7, 7);
      this.edr.copiarse(7);
      double[] notas = this.edr.notas();
      double[] notas_esperadas = new double[]{10.0, 10.0, 10.0, 10.0, 20.0, 10.0, 10.0, 20.0};
      Assertions.assertTrue(Arrays.equals(notas_esperadas, notas));
   }

   @Test
void alumnos_sin_adelante_no_pueden_copiar() {
    this.d_aula = 3;
    this.cant_alumnos = 3;
    this.solucion = new int[]{0,1,2,3,4,5,6,7,8,9};
    this.edr = new Edr(this.d_aula, this.cant_alumnos, this.solucion);

    for(int i = 0; i < 3; i++){
        this.edr.resolver(i, i, i);
    }

    this.edr.copiarse(1);
    this.edr.copiarse(2);

    double[] notas = this.edr.notas();
    double[] notas_esperadas = new double[]{10.0, 20.0, 20.0};
    
 
    Assertions.assertTrue(Arrays.equals(notas_esperadas, notas));
}
@Test
void todos_se_copian_del_que_adelante() {
    this.d_aula = 4;
    this.cant_alumnos = 4;
    this.solucion = new int[]{0,1,2,3,4,5,6,7,8,9};
    this.edr = new Edr(this.d_aula, this.cant_alumnos, this.solucion);

    
    this.edr.resolver(0, 0, 0);
    
    this.edr.copiarse(1);
    this.edr.copiarse(2);
    this.edr.copiarse(3);

    double[] notas = this.edr.notas();
    double[] notas_esperadas = new double[]{10.0, 10.0, 10.0, 10.0};
    
    Assertions.assertTrue(Arrays.equals(notas_esperadas, notas));
}


@Test
void copias_por_columnas_adelante() {
   
    this.d_aula = 5;
    this.cant_alumnos = 6;
    this.solucion = new int[]{0,1,2,3,4,5,6,7,8,9};
    this.edr = new Edr(this.d_aula, this.cant_alumnos, this.solucion);

   
    this.edr.resolver(0, 0, 0); 
    this.edr.resolver(1, 1, 1); 
    this.edr.resolver(2, 2, 2); 
    this.edr.resolver(3, 3, 3); 
    this.edr.resolver(4, 4, 4); 
    this.edr.resolver(5, 5, 5); 

    // solo se van a poder copiar del que esta adelante en la misma columna
    this.edr.copiarse(2);
    this.edr.copiarse(3); 
    this.edr.copiarse(4); 
    this.edr.copiarse(5); 

    double[] notas = this.edr.notas();
    
    double[] notas_esperadas = new double[]{10.0, 10.0, 20.0, 20.0, 20.0, 20.0};
    Assertions.assertTrue(Arrays.equals(notas_esperadas, notas));

}


}
