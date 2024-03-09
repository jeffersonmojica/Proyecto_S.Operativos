package memoria;


import java.util.LinkedList;

import java.util.List;

 

public class GestionMemoria {



       public static final int LONGITUD_MEMORIA = 32;

       public static List<int[]> listaControl;

       public static int[] memoria;

       public static int ultimoPid;



       private static void inicializaMemoria() {

             listaControl = new LinkedList<int[]>();

             memoria = new int[LONGITUD_MEMORIA];

             ultimoPid = 0;


             int[] hueco = { 0, 0, LONGITUD_MEMORIA, 0 };

             listaControl.add(hueco);

       }

     private static void escribeMemoria(int direccion, int tamanyo, int dato) {

             //Escribe el dato 'dato' en las posiciones pertinentes:

             for (int i = 0; i < tamanyo; i++) {

                    memoria[direccion + i] = dato;

             }
       }
 

       public static boolean creaProceso(int pid, int tamanyo) {

             // int hueco = Ajustes.primerAjuste(listaControl, tamanyo);

             int hueco = Ajustes.siguienteAjuste(listaControl, tamanyo);

             // int hueco = Ajustes.mejorAjuste(listaControl, tamanyo);

             // int hueco = Ajustes.peorAjuste(listaControl, tamanyo);

             boolean res = (hueco != -1);

             if (res) {

                    int direcc = listaControl.get(hueco)[1];

                    int[] proceso = { 1, direcc, tamanyo, pid };

                    int espacioRestante = listaControl.get(hueco)[2] - tamanyo;

                    // Inserta el proceso en el lugar del hueco

                    listaControl.set(hueco, proceso);

                    // Si el proceso es más pequeño que el hueco, inserta un hueco

                    if (espacioRestante > 0) {

                           int[] bloqRestante = { 0, direcc + tamanyo, espacioRestante, 0 };

                           listaControl.add(hueco + 1, bloqRestante);

                    }

                    escribeMemoria(direcc, tamanyo, 1);

             }

             return res;

       }

       public static boolean destruyeProceso(int pid) {

             // Busca el índice del proceso en la lista de control

             int indice = 0;

             for (int[] bloque : listaControl) {

                    indice++;

                    if (bloque[3] == pid) {

                           break;

                    }

             }

             indice--;

             boolean encontrado = (indice != -1);


             if (encontrado) {

                    int[] bloqueABorrar = listaControl.get(indice);

                    listaControl.get(indice)[0] = 0;

                    listaControl.get(indice)[3] = 0;

                    escribeMemoria(bloqueABorrar[1], bloqueABorrar[2], 0);

                    fusiona(indice);

                    fusiona(indice - 1);

             }

             return encontrado;

       }

 
       private static boolean fusiona(int indice) {

             boolean fusionable = false;

             if (indice >= 0 && (indice + 1) < listaControl.size()) {

                    fusionable = listaControl.get(indice)[0] == 0

                                  && listaControl.get(indice + 1)[0] == 0;

             }

             if (fusionable) {

                    int tamanyo = listaControl.remove(indice + 1)[2];

                    listaControl.get(indice)[2] += tamanyo;

             }

             return fusionable;

       }



       public static void imprimeMemoria() {

             String s = "Lista de control: {[EST-DIR-TAM-PROC]:";

             for (int[] bloque : listaControl) {

                    s += "[" + bloque[0] + "-" + bloque[1] + "-" + bloque[2] + "-"

                                  + bloque[3];

                    s += "]";

             }

             s += "}\nMemoria: ";

             for (int i : memoria) {

                    s += i;

             }

             System.out.println(s + "\n");

       }

 

       public static void main(String[] args) {

             inicializaMemoria();

            

             creaProceso(1, 6);

             imprimeMemoria();

            

             creaProceso(2, 6);

             imprimeMemoria();

            

             creaProceso(3, 8);

             imprimeMemoria();

            

             creaProceso(4, 7);

             imprimeMemoria();

            

             destruyeProceso(2);

             destruyeProceso(4);

             creaProceso(5, 7);

             imprimeMemoria();

            

             destruyeProceso(1);

             creaProceso(6,5);

             imprimeMemoria();

            

             destruyeProceso(5);

             imprimeMemoria();

            

             destruyeProceso(3);

             destruyeProceso(6);

             imprimeMemoria();

       }

 

}