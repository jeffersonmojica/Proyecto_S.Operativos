package memoria;

import java.util.*;

 

public class Ajustes {

      public static int ultimoBloqueAsignado = 0;


      public static int primerAjuste(List<int[]> listaControl, int tamanyo) {

            int res = 0;

            for (int[] bloque : listaControl) {

                  if (bloque[0] == 0 && bloque[2] >= tamanyo) {

                        break;

                  }

                  res++;

            }

            if (res == listaControl.size()) {

                  res = -1;

            }

            return res;

      }

 

      public static int siguienteAjuste(List<int[]> listaControl, int tamanyo) {

            int res = ultimoBloqueAsignado;

            boolean listaRecorrida=false;

            while (res!=-1) {

                  int bloque[] = listaControl.get(res);

                  if (bloque[0] == 0 && bloque[2] >= tamanyo) {

                        ultimoBloqueAsignado=res;

                        break;

                  }

                  res++;

                  if (res>=listaControl.size()) {

                        if (listaRecorrida) {

                             res=-1;

                        } else {

                             res%=listaControl.size();

                             listaRecorrida=true;

                        }

                  }

            }

            return res;

      }

 


      public static int mejorAjuste(List<int[]> listaControl, int tamanyo) {

            int i = 0;

            int menorBloque = -1;

            int tamMenorBloque = -1;

            for (int[] bloque : listaControl) {

                  if (bloque[0] == 0 && bloque[2] >= tamanyo

                             && (menorBloque == -1 || bloque[2] < tamMenorBloque)) {

                        menorBloque = i;

                        tamMenorBloque = bloque[2];

                  }

                  i++;

            }

            return menorBloque;

      }



      public static int peorAjuste(List<int[]> listaControl, int tamanyo) {

            int i = 0;

            int mayorBloque = -1;

            int tamMayorBloque = -1;

            for (int[] bloque : listaControl) {

                  if (bloque[0] == 0 && bloque[2] >= tamanyo

                             && (mayorBloque == -1 || bloque[2] > tamMayorBloque)) {

                        mayorBloque = i;

                        tamMayorBloque = bloque[2];

                  }

                  i++;

            }

            return mayorBloque;

            }    
}