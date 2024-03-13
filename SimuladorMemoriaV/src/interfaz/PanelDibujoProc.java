package interfaz;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.UIManager;

import logica.ParticionesDinamicas;
import logica.ParticionesEstFijas;

@SuppressWarnings("serial")
public class PanelDibujoProc extends JPanel{

    public PanelDibujoProc(int modelo, int asignacion) {
        this.modelo = modelo;
        this.asignacion = asignacion;

        setToolTipText("");

        inicialModelo();
        iniciarDibujoMemLibre();
    }

    private PanelDibujoMem dibujoMemLibre;

    private int modelo = 0;
    private int asignacion = 0;

    private Color verde = new Color(86, 186, 7);
    private Color amarillo = new Color(215, 215, 84);
    private Color negro = new Color(0, 0, 0);

    private ParticionesEstFijas particionesEstFijas;
    private ParticionesDinamicas particionesDinamicas;

    public void paint(Graphics g) {

        if(modelo == 1) {

            int pos = 0;
            g.setFont(new Font("Tahoma", Font.BOLD, 7));

            double tamanoSO = (particionesEstFijas.getSO().getTamano()*100.0)/particionesEstFijas.getMemoriaPpal();
            int drawSO = (int) (getWidth()*(tamanoSO/100));

            g.setColor(amarillo);
            g.fillRect(pos, 0, drawSO, getHeight());
            g.setColor(negro);
            g.drawRect(pos, 0, drawSO, getHeight()-1);

            g.drawString(particionesEstFijas.getParticiones()[0].getProceso().getNombre(), 5, (getHeight()*2)/5);
            g.drawString("PID=" + particionesEstFijas.getParticiones()[0].getProceso().getPID(), 5, ((getHeight()*4)/5));

            pos = pos + drawSO;

            for(int i=1; i<particionesEstFijas.getParticiones().length;i++) {

                int tamDibujo = cacularTamDibujo(i);
                int tamProceso = 0;

                g.setColor(verde);

                if(particionesEstFijas.getParticiones()[i].getDisponible() == true)
                    tamProceso = 0;
                else
                    tamProceso = cacularTamProceso(i);

                g.fillRect(pos, 0, tamDibujo, getHeight());

                if (tamProceso != 0) {
                    g.setColor(particionesEstFijas.getParticiones()[i].getProceso().getColor());
                    g.fillRect(pos, 0, tamProceso, getHeight());
                }

                g.setColor(negro);
                g.drawRect(pos, 0, tamDibujo, getHeight()-1);

                if(particionesEstFijas.getParticiones()[i].getDisponible() == false) {
                    g.drawString(particionesEstFijas.getParticiones()[i].getProceso().getNombre(), pos+5, (getHeight()*2)/5);
                    g.drawString("PID=" + particionesEstFijas.getParticiones()[i].getProceso().getPID(), pos+5, ((getHeight()*4)/5));
                   }
                pos = pos + tamDibujo;
            }
        }



        if(modelo == 2) {

            int pos = 0;
            g.setFont(new Font("Tahoma", Font.BOLD, 7));

            double tamanoSO = (particionesDinamicas.getSO().getTamano()*100.0)/particionesDinamicas.getMemoriaPpal();
            int drawSO = (int) (getWidth()*(tamanoSO/100));

            g.setColor(amarillo);
            g.fillRect(pos, 0, drawSO, getHeight());
            g.setColor(negro);
            g.drawRect(pos, 0, drawSO, getHeight()-1);

            g.drawString(particionesDinamicas.getParticiones()[0].getProceso().getNombre(), 5, (getHeight()*2)/5);
            g.drawString("PID=" + particionesDinamicas.getParticiones()[0].getProceso().getPID(), 5, ((getHeight()*4)/5));

            pos = pos + drawSO;

            for(int i=1; i<particionesDinamicas.getParticiones().length;i++) {

                int tamDibujo = cacularTamDibujo(i);
                int tamProceso = 0;

                g.setColor(verde);

                 if(particionesDinamicas.getParticiones()[i].getDisponible() == true)
                    tamProceso = 0;
                else
                    tamProceso = cacularTamProceso(i);

                g.fillRect(pos, 0, tamDibujo, getHeight());

                if (tamProceso != 0) {
                    g.setColor(particionesDinamicas.getParticiones()[i].getProceso().getColor());
                    g.fillRect(pos, 0, tamProceso, getHeight());
                }

                g.setColor(negro);
                g.drawRect(pos, 0, tamDibujo, getHeight()-1);

                if(particionesDinamicas.getParticiones()[i].getDisponible() == false) {
                    g.drawString(particionesDinamicas.getParticiones()[i].getProceso().getNombre(), pos+5, (getHeight()*2)/5);
                    g.drawString("PID=" + particionesDinamicas.getParticiones()[i].getProceso().getPID(), pos+5, ((getHeight()*4)/5));
                    }
                pos = pos + tamDibujo;		//Se suma el area dibujada a la posiscion
            }
        }



        //Modelo Segmentacion


    }

    //Inicia la valriable del modelo
    public void inicialModelo() {
        if(modelo == 1)
            particionesEstFijas = new ParticionesEstFijas(asignacion);
       else if(modelo == 2)
            particionesDinamicas = new ParticionesDinamicas(asignacion);
    }

    public void iniciarDibujoMemLibre() {

        if(modelo == 1)
            dibujoMemLibre = new PanelDibujoMem(particionesEstFijas.getParticiones(), particionesEstFijas.getMemoriaPpal());
        else if(modelo == 2)
            dibujoMemLibre = new PanelDibujoMem(particionesDinamicas.getParticiones(), particionesDinamicas.getMemoriaPpal());

    }

    /**
     * Calcula el tamaño de la particion a dibujar
     * @param posicion
     * @return tamaño del recuadro a dibujar
     */
    public int cacularTamDibujo(int posicion) {
        if(modelo == 1) {
            double tamano = (particionesEstFijas.getParticiones()[posicion].getTamano()*100.0)/particionesEstFijas.getMemoriaPpal();
            int draw = (int) (getWidth()*(tamano/100));
            return draw;
        }else if(modelo == 2) {
            double tamano = (particionesDinamicas.getParticiones()[posicion].getTamano()*100.0)/particionesDinamicas.getMemoriaPpal();
            int draw = (int) (getWidth()*(tamano/100));
            return draw;
        }else {
            return 0;
        }

    }

    /**
     * Calcula el tamaño del proceso
     * @param posicion
     * @return tamProceso
     */
    public int cacularTamProceso(int posicion) {
        if(modelo == 1) {
            double tamProceso = (particionesEstFijas.getParticiones()[posicion].getProceso().getTamano()*100.0)/particionesEstFijas.getMemoriaPpal();
            int draw = (int) (getWidth()*(tamProceso/100));
            return draw;
        }else if(modelo == 2) {
            double tamProceso = (particionesDinamicas.getParticiones()[posicion].getProceso().getTamano()*100.0)/particionesDinamicas.getMemoriaPpal();
            int draw = (int) (getWidth()*(tamProceso/100));
            return draw;
        }else {
            return 0;
        }

    }

    //Muestra el tooltip con la informacion de la particion
    public String getToolTipText(MouseEvent e) {
        String texto= "";
        //Si el area sobre la que esta el mouse no es el area de dibujo returna null
        if(getMousePosition() != null) {

            int inicioPar = 0;
            int finPar = 0;
            int cont = -1;
            int posXMouse = this.getMousePosition().x;

            if(modelo == 1) {

                do{
                    cont++;
                    //el area de dibujo puede tener algunos pixeles mas que el area dibujada asi que si el
                    //contador se pasa lo regresa para evitar error de posicion que no existe
                    if(cont >= particionesEstFijas.getParticiones().length)
                        cont--;

                    //Si la posicion es la 0 el inicio es 0
                    if(cont <= 0) {
                        inicioPar = 0;
                        finPar = (inicioPar + cacularTamDibujo(cont));
                    }else {
                        inicioPar = (finPar);
                        finPar = (inicioPar + cacularTamDibujo(cont));
                    }
                }while( posXMouse < inicioPar ||  posXMouse > finPar);

                texto = "<html>";

                if(particionesEstFijas.getParticiones()[cont].getDisponible()==false) {
                    texto = texto + "<b>Proceso:</b> " + particionesEstFijas.getParticiones()[cont].getProceso().getNombre();
                    texto = texto + "<br/><b>PID:</b> " + particionesEstFijas.getParticiones()[cont].getProceso().getPID();
                    texto = texto + "<br/><b>Tamaño Particion:</b> " + particionesEstFijas.getParticiones()[cont].getTamano() + "KB";
                    texto = texto + "<br/><b>Ocupado:</b> " + particionesEstFijas.getParticiones()[cont].getProceso().getTamano()  + " KB";
                    UIManager.put("ToolTip.background", particionesEstFijas.getParticiones()[cont].getProceso().getColor());
                }else {
                    texto =  texto + "<b>Libre</b>";
                    texto = texto + "<br/><b>Tamaño Particion:</b> " + particionesEstFijas.getParticiones()[cont].getTamano() + "KB";
                    UIManager.put("ToolTip.background", verde);
                }
                texto = texto + "<br /><b>Inicio:</b> " + particionesEstFijas.getParticiones()[cont].getInicio() + " KB<br/><b>Fin: </b>"
                        + (particionesEstFijas.getParticiones()[cont].getInicio() + particionesEstFijas.getParticiones()[cont].getTamano() - 1) + " KB</html>";

            }else if(modelo == 2) {

                do{
                    cont++;
                    //el area de dibujo puede tener algunos pixeles mas que el area dibujada asi que si el
                    //contador se pasa lo regresa para evitar error de posicion que no existe
                    if(cont >= particionesDinamicas.getParticiones().length)
                        cont--;

                    //Si la posicion es la 0 el inicio es 0
                    if(cont <= 0) {
                        inicioPar = 0;
                        finPar = (inicioPar + cacularTamDibujo(cont));
                    }else {
                        inicioPar = (finPar);
                        finPar = (inicioPar + cacularTamDibujo(cont));
                    }
                }while( posXMouse < inicioPar ||  posXMouse > finPar);

                texto = "<html>";

                if(particionesDinamicas.getParticiones()[cont].getDisponible()==false) {
                    texto = texto + "<b>Proceso:</b> " + particionesDinamicas.getParticiones()[cont].getProceso().getNombre();
                    texto = texto + "<br/><b>PID:</b> " + particionesDinamicas.getParticiones()[cont].getProceso().getPID();
                    texto = texto + "<br/><b>Tamaño Particion:</b> " + particionesDinamicas.getParticiones()[cont].getTamano() + "KB";
                    texto = texto + "<br/><b>Ocupado:</b> " + particionesDinamicas.getParticiones()[cont].getProceso().getTamano() + " KB";
                    UIManager.put("ToolTip.background", particionesDinamicas.getParticiones()[cont].getProceso().getColor());
                }else {
                    texto =  texto + "<b>Libre</b>";
                    UIManager.put("ToolTip.background", verde);
                    texto = texto + "<br/><b>Tamaño Particion:</b> " + particionesDinamicas.getParticiones()[cont].getTamano() + "KB";
                }
                texto = texto + "<br/><b>Inicio:</b> " + particionesDinamicas.getParticiones()[cont].getInicio() + " KB<br/><b>Fin:</b> "
                        + (particionesDinamicas.getParticiones()[cont].getInicio() + particionesDinamicas.getParticiones()[cont].getTamano() - 1) + " KB</html>";

           }else {
                return "";
            }

        }
        return texto;
    }

    //Ubicacion del TooltopText
    public Point getToolTipLocation(MouseEvent e){
        Point p = e.getPoint();
        p.y += 15;
        return p;
        //return super.getToolTipLocation(e);
    }

    public ParticionesEstFijas getParticionesEstFijas() {
        return particionesEstFijas;
    }
    public void setParticionesEstFijas(ParticionesEstFijas particionesEstFijas) {
        this.particionesEstFijas = particionesEstFijas;
    }

    public ParticionesDinamicas getParticionesDinamicas() {
        return particionesDinamicas;
    }

    public void setParticionesDinamicas(ParticionesDinamicas particionesDinamicas) {
        this.particionesDinamicas = particionesDinamicas;
    }





    public PanelDibujoMem getDibujoMemLibre() {
        return dibujoMemLibre;
    }

    public void setDibujoMemLibre(PanelDibujoMem dibujoMemLibre) {
        this.dibujoMemLibre = dibujoMemLibre;
    }

}

