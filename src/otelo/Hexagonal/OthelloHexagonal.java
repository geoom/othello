/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package otelo.Hexagonal;

import java.util.ArrayList;

/**
 *
 * @author cliente
 */
public class OthelloHexagonal{
    private int tamTab;
    private int[][] MatTab;         //Si MatTab ij {-1,0,1}; -1:negro; 0:vacio; 1:blanco
    private int[][] MatPesos;       //Matriz de pesos de ubicacion
    private int turno;              //fichas (b/n) (-1/1)
    private int nivel1;             //define tipo nivel de jug 1
    private int nivel2;             //define tipo nivel de jug 2
    private Jugador jug1;
    private Jugador jug2;
    // <editor-fold defaultstate="collapsed" desc="VARIABLES ESTATICAS">
    // <editor-fold defaultstate="collapsed" desc="Estados de casilleros /turnos">
    public static int FICHA_BLANCA = 1;
    public static int VACIO = 0;
    public static int FICHA_NEGRA = -1;
    public static int FICHA_POSIBLE = -2;
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Tipos de juego">
    public static int HUMANO = 0;
    public static int FACIL = 1;
    public static int MEDIO = 2;
    public static int EXPERTO = 3;
    public static int NO_MAQUINAS = 100;
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Pesos de Criterios">
    private static float CRIT_PESO_POSICION = 0.65f;
    private static float CRIT_FICHAS_INVERTIDAS = 0.35f;
    // </editor-fold>
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Representacion de tablero (tam=6)">
        /*
         * Grafico de la matriz de hexagonos
         * XXXX000000   |
         * XXX0000000   |
         * XX00000000   |
         * X000000000   |
         * 0000000000   |>  2n-2
         * 0000000000   |>
         * 000000000X   |
         * 00000000XX   |
         * 0000000XXX   |
         * 000000XXXX   |
         * donde: X es posicion no valida
         *        0 es posicion valida
         */
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor de OTHELLOHEXAGONAL">
    public OthelloHexagonal(int tamTab, int jugador1, int jugador2) {
        this.tamTab = tamTab;
        this.nivel1 = jugador2;
        if (jugador1 == Jugador.HUMANO)
            jug1 = new Jugador(Jugador.FIC_BLA, Jugador.HUMANO);
        else{
            nivel1 = jugador1;
            jug1 = new Jugador(Jugador.FIC_BLA, Jugador.MAQUINA);
        }
        if (jugador2 == Jugador.HUMANO)
            jug2 = new Jugador(Jugador.FIC_NEG, Jugador.HUMANO);
        else{
            nivel2 = jugador2;
            jug2 = new Jugador(Jugador.FIC_NEG, Jugador.MAQUINA);
        }
        turno = 1;
        iniciarTablero();
        iniciarPesos();
    }
    // </editor-fold>

    public void mt(){
        for(int i=0;i<MatTab.length;i++){
            for(int j=0;j<MatTab[0].length;j++)
                System.out.print(MatTab[i][j]+"\t");
            System.out.println("");
        }
    }

    public void mp(){
        for(int i=0;i<MatPesos.length;i++){
            for(int j=0;j<MatPesos[0].length;j++)
                System.out.print(MatPesos[i][j]+"\t");
            System.out.println("");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Estado Inicial">
    private void iniciarTablero(){
        MatTab = new int[2*(tamTab-1)][2*(tamTab-1)];
        for(int i=0;i<MatTab.length;i++)
            for(int j=0;j<MatTab[0].length;j++)
                MatTab[i][j] = 0;
        MatTab[tamTab-2][tamTab-2] = MatTab[tamTab-1][tamTab-1]= 1;
        MatTab[tamTab-1][tamTab-2] = MatTab[tamTab-2][tamTab-1]= -1;
    }

    private void iniciarPesos(){
        MatPesos = new int[2*(tamTab-1)][2*(tamTab-1)];
        for(int i=0;i<MatPesos.length;i++){
            for(int j=0;j<MatPesos[0].length;j++){
                if(tamTab-2<=i+j && i+j<=3*tamTab-4){
                    double difX = Math.abs(i-tamTab+1.5);
                    double difY = Math.abs(j-tamTab+1.5);
                    MatPesos[i][j] = 2*(int)Math.sqrt(Math.pow(difX,2) + Math.pow(difY,2));
                    if(((int)Math.abs(j-i))==(2*tamTab-4))
                        MatPesos[i][j] = -tamTab/2;
                    if((i==tamTab-1 && j==0) || (j==tamTab-1 && i==0))
                        MatPesos[i][j] = -tamTab/2;
                    if((i==tamTab-3 && j==1) || (j==tamTab-3 && i==1))
                        MatPesos[i][j] = -tamTab/2;
                    if((i==tamTab-2 && j==MatPesos[0].length-1) || (j==tamTab-2 && i==MatPesos[0].length-1))
                        MatPesos[i][j] = -tamTab/2;
                    if((i==tamTab && j==MatPesos[0].length-2) || (j==tamTab && i==MatPesos[0].length-2))
                        MatPesos[i][j] = -tamTab/2;
                    if((i==tamTab-2 && j==1) || (j==tamTab-2 && i==1))
                        MatPesos[i][j] = -tamTab/2;
                    if((i==MatPesos[0].length-2 && j==tamTab-1) || (j==MatPesos[0].length-2 && i==tamTab-1))
                        MatPesos[i][j] = -tamTab/2;
                    if((i==MatPesos[0].length-2 && j==1) || (j==MatPesos[0].length-2 && i==1))
                        MatPesos[i][j] = -tamTab/2;
                }else
                    MatPesos[i][j] = 0;
            }
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Contadores de fichas">
    public int cantB(){
        int cant = 0;
        for(int i=0;i<MatTab.length;i++)
            for(int j=0;j<MatTab[0].length;j++)
                if(tamTab-2<=i+j && i+j<=3*tamTab-4)
                    if(MatTab[i][j]==FICHA_BLANCA)
                        cant++;
        return cant;
    }

    public int cantN(){
        int cant = 0;
        for(int i=0;i<MatTab.length;i++)
            for(int j=0;j<MatTab[0].length;j++)
                if(tamTab-2<=i+j && i+j<=3*tamTab-4)
                    if(MatTab[i][j]==FICHA_NEGRA)
                        cant++;
        return cant;
    }// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Verificadores de jugadas validas">
    private boolean verificarDiagonalInferior(int x, int y,int turno, int [][] MatJug){
        int limiteInferior;
        if (y<tamTab-2)
            limiteInferior = 2*tamTab-3;
        else
            limiteInferior = 3*tamTab-4-y;
        boolean cont = true;
        int i = x+1;
        int ultimaficha = MatJug[x][y];
        if(ultimaficha != VACIO)
            return false;
        while(cont && i<limiteInferior){
            if(MatJug[i][y]==VACIO || MatJug[i][y]==turno)
                cont = false;
            else if (MatJug[i][y]==(-turno)){
                ultimaficha = MatJug[i][y]; i++;
            }
        }
        try{
            if (ultimaficha == (-turno) && MatJug[i][y] == turno)
                return true;
            else
                return false;
        }catch(ArrayIndexOutOfBoundsException ae){
            return false;
        }
    }

    private boolean verificarDiagonalSuperior(int x, int y, int turno, int [][] MatJug){
        int limiteSuperior;
        if (y<tamTab-2)
            limiteSuperior = tamTab-3-y;
        else
            limiteSuperior = 0;
        boolean cont = true;
        int i = x-1;
        int ultimaficha = MatJug[x][y];
        if(ultimaficha != VACIO)
            return false;
        while(cont && i >=limiteSuperior){
            if(MatJug[i][y]==VACIO || MatJug[i][y]==turno)
                cont = false;
            else if (MatJug[i][y]==(-turno)){
                ultimaficha = MatJug[i][y]; i--;
            }
        }
        try{
            if (ultimaficha == (-turno) && MatJug[i][y] == turno)
                return true;
            else
                return false;
        }catch(ArrayIndexOutOfBoundsException ae){
            return false;
        }
    }

    private boolean verificarContraDiagonalInferior(int x, int y, int turno, int [][] MatJug){
        int limiteSuperior;
        if (x<tamTab-2)
            limiteSuperior = tamTab-3-x;
        else
            limiteSuperior = 0;
        boolean cont = true;
        int j = y-1;
        int ultimaficha = MatJug[x][y];
        if(ultimaficha != VACIO)
            return false;
        while(cont && j >=limiteSuperior){
            if(MatJug[x][j]==VACIO || MatJug[x][j]==turno)
                cont = false;
            else if (MatJug[x][j]==(-turno)){
                ultimaficha = MatJug[x][j]; j--;
            }
        }
        try{
            if (ultimaficha == (-turno) && MatJug[x][j] == turno)
                return true;
            else
                return false;
        }catch(ArrayIndexOutOfBoundsException ae){
            return false;
        }
    }

    private boolean verificarContraDiagonalSuperior(int x, int y,int turno, int [][] MatJug){
        int limiteInferior;
        if (x<tamTab-2)
            limiteInferior = 2*tamTab-3;
        else
            limiteInferior = 3*tamTab-4-x;
        boolean cont = true;
        int j = y+1;
        int ultimaficha = MatJug[x][y];
        if(ultimaficha != VACIO)
            return false;
        while(cont && j <limiteInferior){
            if(MatJug[x][j]==VACIO || MatJug[x][j]==turno)
                cont = false;
            else if (MatJug[x][j]==(-turno)){
                ultimaficha = MatJug[x][j]; j++;
            }
        }
        try{
            if (ultimaficha == (-turno) && MatJug[x][j] == turno)
                return true;
            else
                return false;
        }catch(ArrayIndexOutOfBoundsException ae){
            return false;
        }
    }

    private boolean verificarVerticalInferior(int x, int y,int turno, int [][] MatJug){
        boolean cont = true;
        int i = x - 1;
        int j = y + 1;
        int ultimaficha = MatJug[x][y];
        if(ultimaficha != VACIO)
            return false;
        while(cont && i>=0 && j<2*tamTab-2){
            if(MatJug[i][j] == VACIO || MatJug[i][j] == turno)
                cont = false;
            else if (MatJug[i][j]==(-turno)){
                ultimaficha = MatJug[i][j]; i--; j++;
            }
        }
        try{
            if (ultimaficha == (-turno) && MatJug[i][j] == turno)
                return true;
            else
                return false;
        }catch(ArrayIndexOutOfBoundsException ae){
            return false;
        }
    }

    private boolean verificarVerticalSuperior(int x, int y, int turno, int [][] MatJug){
        boolean cont = true;
        int i = x + 1;
        int j = y - 1;
        int ultimaficha = MatJug[x][y];
        if(ultimaficha != VACIO)
            return false;
        while(cont && 2*tamTab-2>i && j>=0){
            if(MatJug[i][j] == VACIO || MatJug[i][j] == turno)
                cont = false;
            else if (MatJug[i][j]==(-turno)){
                ultimaficha = MatJug[i][j]; i++; j--;
            }
        }
        try{
            if (ultimaficha == (-turno) && MatJug[i][j] == turno)
                return true;
            else
                return false;
        }catch(ArrayIndexOutOfBoundsException ae){
            return false;
        }
    }// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Realizadores de nuevo estado">
    public int cambiarFichasDiagonalInferior(int x, int y, int turno, int[][] MatJug){
        int i = x+1;
        int cont = 0;
        while(MatJug[i][y]!=turno){
            MatJug[i][y] = turno;
            i++;
            cont++;
        }
        return cont;
    }

    public int cambiarFichasDiagonalSuperior(int x, int y, int turno, int[][] MatJug){
        int i = x-1;
        int cont = 0;
        while(MatJug[i][y]!=turno){
            MatJug[i][y] = turno;
            cont++;
            i--;
        }
        return cont;
    }

    public int cambiarFichasContraDiagonalInferior(int x, int y, int turno, int[][] MatJug){
        int j = y-1;
        int cont = 0;
        while(MatJug[x][j]!=turno){
            MatJug[x][j] = turno;
            j--;
            cont++;
        }
        return cont;
    }

    public int cambiarFichasContraDiagonalSuperior(int x, int y, int turno, int[][] MatJug){
        int j = y+1;
        int cont = 0;
        while(MatJug[x][j]!=turno){
            MatJug[x][j] = turno;
            j++;
            cont++;
        }
        return cont;
    }

    public int cambiarFichasVerticalInferior(int x, int y, int turno, int[][] MatJug){
        int inc = 1;
        int cont = 0;
        while(MatJug[x-inc][y+inc]!=turno){
            MatJug[x-inc][y+inc] = turno;
            inc++;
            cont++;
        }
        return cont;
    }

    public int cambiarFichasVerticalSuperior(int x, int y, int turno, int[][] MatJug){
        int inc = 1;
        int cont = 0;
        while(MatJug[x+inc][y-inc]!=turno){
            MatJug[x+inc][y-inc] = turno;
            inc++;
            cont++;
        }
        return cont;
    }// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Movimientos del juego">
    public void colocarFichaBlanca(int x, int y){
        if(tamTab-2<=x+y && x+y<=3*tamTab-4){//si esta dentro del tablero
            if(MatTab[x][y] == 0){//si la posicion esta vacia
                boolean res = false;
                int cont = 0;
                if(verificarDiagonalInferior(x, y, FICHA_BLANCA,MatTab)){
                    cont+=cambiarFichasDiagonalInferior(x, y, FICHA_BLANCA, MatTab);
                    res = true;
                }
                if(verificarDiagonalSuperior(x, y, FICHA_BLANCA,MatTab)){
                    cont+=cambiarFichasDiagonalSuperior(x, y, FICHA_BLANCA, MatTab);
                    res = true;
                }
                if(verificarContraDiagonalInferior(x, y, FICHA_BLANCA,MatTab)){
                    cont+=cambiarFichasContraDiagonalInferior(x, y, FICHA_BLANCA, MatTab);
                    res = true;
                }
                if(verificarContraDiagonalSuperior(x, y, FICHA_BLANCA,MatTab)){
                    cont+=cambiarFichasContraDiagonalSuperior(x, y, FICHA_BLANCA, MatTab);
                    res = true;
                }
                if(verificarVerticalInferior(x, y, FICHA_BLANCA,MatTab)){
                    cont+=cambiarFichasVerticalInferior(x, y, FICHA_BLANCA, MatTab);
                    res = true;
                }
                if(verificarVerticalSuperior(x, y, FICHA_BLANCA,MatTab)){
                    cont+=cambiarFichasVerticalSuperior(x, y, FICHA_BLANCA, MatTab);
                    res = true;
                }
                if(res){
                    MatTab[x][y] = FICHA_BLANCA;
                    turno = FICHA_NEGRA;
                    System.out.println("Se cambiaron :"+cont+" fichas");
                }else
                    System.out.println("no se ingreso ficha");
            }else
                System.out.println("movimiento invalido");
        }else
            System.out.println("valor de x,y "+MatTab[x][y]);        
    }

    public void colocarFichaNegra(int x, int y){
        if(tamTab-2<=x+y && x+y<=3*tamTab-4){//si esta dentro del tablero
            if(MatTab[x][y]==0){//si la posicion esta vacia
                boolean res = false;
                int cont = 0;
                if(verificarDiagonalInferior(x, y, FICHA_NEGRA,MatTab)){
                    cont+=cambiarFichasDiagonalInferior(x, y, FICHA_NEGRA, MatTab);
                    res = true;
                }
                if(verificarDiagonalSuperior(x, y, FICHA_NEGRA,MatTab)){
                    cont+=cambiarFichasDiagonalSuperior(x, y, FICHA_NEGRA, MatTab);
                    res = true;
                }
                if(verificarContraDiagonalInferior(x, y, FICHA_NEGRA,MatTab)){
                    cont+=cambiarFichasContraDiagonalInferior(x, y, FICHA_NEGRA, MatTab);
                    res = true;
                }
                if(verificarContraDiagonalSuperior(x, y, FICHA_NEGRA,MatTab)){
                    cont+=cambiarFichasContraDiagonalSuperior(x, y, FICHA_NEGRA, MatTab);
                    res = true;
                }
                if(verificarVerticalInferior(x, y, FICHA_NEGRA,MatTab)){
                    cont+=cambiarFichasVerticalInferior(x, y, FICHA_NEGRA, MatTab);
                    res = true;
                }
                if(verificarVerticalSuperior(x, y, FICHA_NEGRA,MatTab)){
                    cont+=cambiarFichasVerticalSuperior(x, y, FICHA_NEGRA, MatTab);
                    res = true;
                }
                System.out.println("valor de x,y "+MatTab[x][y]);
                if(res){
                    MatTab[x][y] = FICHA_NEGRA;
                    turno = FICHA_BLANCA;
                    System.out.println("Se cambiaron "+cont+" fichas");
                }else
                    System.out.println("no se inngreso ficha");
            }else
                System.out.println("movimiento invalido");
        }else            
            System.out.println("valor de x,y "+MatTab[x][y]);
    }// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Condiciones de estado meta">
    public boolean isFin(){
        int cantFich = cantB() + cantN();
        ArrayList<Integer> jugs = buscarJugadasPosibles(turno,MatTab).get(0);
        if(cantFich == (tamTab-1)*(3*tamTab-2) || jugs.size() == 0){//cant == (otheHex.getTamTab()-1)*(3*otheHex.getTamTab()-2)
            return true;
        }
        return false;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Busqueda de jugadas Posibles">
    public ArrayList <ArrayList <Integer>> buscarJugPos(int turnoFich, int[][] MatTab){
        return buscarJugadasPosibles(turnoFich, MatTab);
    }

    private ArrayList <ArrayList <Integer>> buscarJugadasPosibles(int turnoFich,int[][] MatEstAct){
        ArrayList <ArrayList <Integer>> listJug = new ArrayList<ArrayList<Integer>>();
        ArrayList <Integer> xs = new ArrayList<Integer>();
        ArrayList <Integer> ys = new ArrayList<Integer>();
        for(int i=0;i<MatEstAct.length;i++){
            for(int j=0;j<MatEstAct[0].length;j++){
                if(tamTab-2<=i+j && i+j<=3*tamTab-4){
                    boolean res = verificarContraDiagonalInferior(i, j, turnoFich,MatEstAct) || verificarContraDiagonalSuperior(i, j, turnoFich,MatEstAct)
                            || verificarDiagonalInferior(i, j, turnoFich,MatEstAct) || verificarDiagonalSuperior(i, j, turnoFich,MatEstAct)
                            || verificarVerticalInferior(i, j, turnoFich,MatEstAct) || verificarVerticalSuperior(i, j, turnoFich,MatEstAct);
                    if (res){
                        xs.add(i);
                        ys.add(j);
                    }
                }
            }
        }
        listJug.add(xs);
        listJug.add(ys);
        return listJug;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Busquedas por niveles de maquina">
    private int[] buscarJugadaFacil(int ficha){
        ArrayList <ArrayList <Integer>> jugadas = buscarJugadasPosibles(ficha,MatTab);
        ArrayList <Integer> xs = jugadas.get(0);
        ArrayList <Integer> ys = jugadas.get(1);
        int ind = (int)(Math.random()*xs.size());
        System.out.println("Las pos jugdas son:");
        for (int i = 0; i < xs.size(); i++)
            System.out.println("i: "+i+" => "+xs.get(i)+" "+ys.get(i));
        System.out.println("La jugada es "+ind+" de "+xs.size());
        int jugada[] = {xs.get(ind), ys.get(ind)};
        return jugada;
    }
    
    private int[] buscarJugadaMedio(int ficha){
        ArrayList <ArrayList <Integer>> jugadas = buscarJugadasPosibles(ficha,MatTab);
        ArrayList <Integer> xs = jugadas.get(0);
        ArrayList <Integer> ys = jugadas.get(1);
        ArrayList <Float> pesos = new ArrayList<Float>();
        for(int i = 0; i < xs.size(); i++)
            pesos.add(funcionEvaluadora(xs.get(i), ys.get(i), ficha, MatTab));
        int ind = 0;
        /*float may = pesos.get(ind);
        for(int i = 0; i < pesos.size(); i++)
            if(pesos.get(i)>may){
                ind = i;
                may = pesos.get(i);
            }
        System.out.println("Elegi jugada "+ ind +" peso: "+may);*/
        ind = indiceMejorJugada(pesos);
        int jugada[] = {xs.get(ind), ys.get(ind)};
        return jugada;
    }

    private int[] buscarJugadaExperto(int ficha){
        ArrayList <ArrayList <Integer>> jugadas = buscarJugadasPosibles(ficha,MatTab);
        ArrayList <Integer> xs = jugadas.get(0);
        ArrayList <Integer> ys = jugadas.get(1);
        ArrayList <Float> difGanancias = new ArrayList<Float>();
        System.out.println("exiten "+(xs.size())+" jugs mias");
        int[][] estTemp = new int[MatTab.length][MatTab.length];
        for(int index=0;index<xs.size();index++){
            for(int i=0;i<MatTab.length;i++)
                for(int j=0;j<MatTab.length;j++)
                    estTemp[i][j] = MatTab[i][j];
            difGanancias.add(funcionEvaluadora(xs.get(index), ys.get(index), ficha, estTemp)
                    - mejorJugadaContrincante(xs.get(index), ys.get(index), ficha, estTemp));
        }
        int ind = 0;
        /*float may = difGanancias.get(ind);
        for(int i = 0; i < difGanancias.size(); i++)
            if(difGanancias.get(i) > may){
                ind = i;
                may = difGanancias.get(i);
            }
        System.out.println("Elegi jugada "+ ind +" peso: "+may);*/
        ind = indiceMejorJugada(difGanancias);
        int jugada[] = {xs.get(ind), ys.get(ind)};
        return jugada;
    }

    private float mejorJugadaContrincante(int x, int y, int ficha, int[][] estAnt){
        ArrayList <ArrayList <Integer>> jugadas = buscarJugadasPosibles(-ficha,estAnt);
        ArrayList <Integer> xs = jugadas.get(0);
        ArrayList <Integer> ys = jugadas.get(1);
        ArrayList <Float> pesos = new ArrayList<Float>();
        System.out.println("exiten "+(xs.size())+" jugs contr");
        for(int i = 0; i < xs.size(); i++)
            pesos.add(funcionEvaluadora(xs.get(i), ys.get(i), -ficha, estAnt));
        int ind = 0;
        if(xs.isEmpty())
            return 0.0f;
        float may = pesos.get(ind);
        for(int i = 0; i < pesos.size(); i++){
            System.out.println("jugada "+i+" de contrincante "+ pesos.get(i));
            if(pesos.get(i)>may){
                ind = i;
                may = pesos.get(i);
            }
        }
        System.out.println("Elegi jugada Cont "+ ind +" peso: "+may);
        return may;
    }

    private int indiceMejorJugada(ArrayList<Float> array){
        float mayor = array.get(0);
        ArrayList<Float> Mayores = new ArrayList<Float>();
        for (Float elemArr : array) {
            if(mayor<elemArr){
                Mayores = new ArrayList<Float>();
                Mayores.add(elemArr);
                mayor = elemArr;
            }else{
                if(mayor == elemArr)
                    Mayores.add(elemArr);
            }
        }
        System.out.println("son "+Mayores.size()+ " jugadas posibles");
        return (int)(Mayores.size()*Math.random());
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Selector de estrategia">
    public void movimientoMaquina(){
        if(turno == FICHA_BLANCA){
            if(jug1.getTipo()==Jugador.HUMANO){
                //esperar click
            }else{
                if(nivel1 == FACIL){
                    System.out.println("Entre a nivel facil");
                    int jug[] = buscarJugadaFacil(FICHA_BLANCA);
                    colocarFichaBlanca(jug[0], jug[1]);
                }
                if(nivel1 == MEDIO){
                    System.out.println("Entre a nivel medio");
                    int jug[] = buscarJugadaMedio(FICHA_BLANCA);
                    colocarFichaBlanca(jug[0], jug[1]);
                    // <editor-fold defaultstate="collapsed" desc="Estrategia MEDIO">
                    // </editor-fold>
                }
                if(nivel1 == EXPERTO){
                    System.out.println("Entre a nivel medio");
                    int jug[] = buscarJugadaExperto(FICHA_BLANCA);
                    colocarFichaBlanca(jug[0], jug[1]);
                    // <editor-fold defaultstate="collapsed" desc="Estrategia EXPERTO">
                    // </editor-fold>
                }
            }
        }else{
            if(jug2.getTipo()==Jugador.HUMANO){
                //esperar click
            }else{
                if(nivel2 == FACIL){
                    int jug[] = buscarJugadaFacil(FICHA_NEGRA);
                    colocarFichaNegra(jug[0], jug[1]);
                }
                if(nivel2 == MEDIO){
                    int jug[] = buscarJugadaMedio(FICHA_NEGRA);
                    colocarFichaNegra(jug[0], jug[1]);
                    // <editor-fold defaultstate="collapsed" desc="Estrategia MEDIO">
                    // </editor-fold>
                }
                if(nivel2 == EXPERTO){
                    int jug[] = buscarJugadaExperto(FICHA_NEGRA);
                    colocarFichaNegra(jug[0], jug[1]);
                    // <editor-fold defaultstate="collapsed" desc="Estrategia EXPERTO">
                    // </editor-fold>
                }
            }
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Funcion evaluadora/criterios">
    private float funcionEvaluadora(int x, int y, int ficha, int[][] estAct){
        System.out.println("Peso en "+x+" "+y+" = "+(fichasInvert(x,y,ficha,estAct)*CRIT_FICHAS_INVERTIDAS+pesoPosicion(x,y)*CRIT_PESO_POSICION));
        return fichasInvert(x,y,ficha,estAct)*CRIT_FICHAS_INVERTIDAS+pesoPosicion(x,y)*CRIT_PESO_POSICION;
    }

    //crea una matriz temporal a partir del parametro que solicita
    private int fichasInvert(int x, int y,int ficha, int[][] estAct){
        int[][] estTemp = new int[estAct.length][estAct.length];
        for(int i=0;i<estAct.length;i++)
            for(int j=0;j<estAct.length;j++)
                estTemp[i][j] = estAct[i][j];
        int cont = 0;
        if(verificarDiagonalInferior(x, y, ficha, estTemp))
            cont+=cambiarFichasDiagonalInferior(x, y, ficha, estTemp);
        if(verificarDiagonalSuperior(x, y, ficha, estTemp))
            cont+=cambiarFichasDiagonalSuperior(x, y, ficha, estTemp);
        if(verificarContraDiagonalInferior(x, y, ficha, estTemp))
            cont+=cambiarFichasContraDiagonalInferior(x, y, ficha, estTemp);
        if(verificarContraDiagonalSuperior(x, y, ficha, estTemp))
            cont+=cambiarFichasContraDiagonalSuperior(x, y, ficha, estTemp);
        if(verificarVerticalInferior(x, y, ficha, estTemp))
            cont+=cambiarFichasVerticalInferior(x, y, ficha, estTemp);
        if(verificarVerticalSuperior(x, y, ficha, estTemp))
            cont+=cambiarFichasVerticalSuperior(x, y, ficha, estTemp);
        return cont;
    }

    private int pesoPosicion(int x, int y){
        return MatPesos[x][y];
    }
    // </editor-fold>

    public int getTamTab() {
        return tamTab;
    }

    public void setTamTab(int tamTab) {
        this.tamTab = tamTab;
    }

    public int[][] getMatTab() {
        return MatTab;
    }

    public void setMatTab(int[][] MatTab) {
        this.MatTab = MatTab;
    }

    public int getTurno() {
        return turno;
    }

    public void setTurno(int turno) {
        this.turno = turno;
    }

    public int getTipoJug1(){
        return jug1.getTipo();
    }

    public int getTipoJug2(){
        return jug2.getTipo();
    }

}
