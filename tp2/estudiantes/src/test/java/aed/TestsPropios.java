package aed;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
      this.d_aula = 5;
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
   void todos_misma_nota() {
      this.edr.resolver(0, 0, 0);
      this.edr.resolver(1, 1, 1);
      this.edr.resolver(2, 2, 2);
      this.edr.resolver(3, 3, 3);
      this.edr.entregar(0);
      this.edr.entregar(1);
      this.edr.entregar(2);
      this.edr.entregar(3);
      NotaFinal[] notas = this.edr.corregir();
      double[] var10000 = new double[]{10.0, 10.0, 10.0, 10.0};
   }
}
