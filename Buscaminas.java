package Buscaminas;

import java.util.Random;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class Buscaminas {

	public static void main(String[] args) {
		try (// TODO Auto-generated method stub
		Scanner pen = new Scanner(System.in)) {
			System.out.println("\t\t\t\t\tBIENVENIDO AL JUEGO BUSCAMINAS");

				int filas=15, columnas=15, minas=30;
				Porcion [][]matriz=CrearMatriz(filas,columnas,minas);//crea la matriz de el objeto Porcion
				String [][]matrizUsuario=MatrizUsuario(filas,columnas);//crea una matriz que es la que se mostrara al usuario
				imprimirfantasma(matrizUsuario,filas,columnas); //muestra la matriz para que el usuario la vea 
				int gano=0;	//decide cuando se gana
				do{
					System.out.println("Decide tu tirada");
					System.out.println("Celda [C] o Bandera[B]"); //pide una tirada para abrir o para señalar una bandera
					String x=pen.next();
					char decide=x.charAt(0);
					if(decide=='C') {
						System.out.println("Que celda quiere abrir(indique coordenadas[0-"+(filas-1)+"]-[0-"+(columnas-1)+"])");
						int fila=pen.nextInt();		//pide las coordenadas de la casilla a abrir
						int columna=pen.nextInt();
						if((fila>=0)&&(fila<=filas-1)&&(columna>=0)&&(columna<=columnas-1)) {
							if(matriz[fila][columna].visitado==false) {//si ya lo visito no tiene caso descubrirlo de nuevo
								imprimirfantasma(DescubreMatriz(matrizUsuario,matriz,fila,columna),filas,columnas);//llama a la funcion para imprimir la matriz y abre la casilla que dice el usuario
							}else {
								System.out.println("Ya tiraste en esa celda");
							}
							boolean win= verificagane(matriz);//verifica si gana o no
							if(win==true) {
								gano=1;
								break;
							}
							if(matriz[fila][columna].terreno==-1) {//verifica en la matriz de fondo si pierde o no al sacar un -1 ya que es una mina
								gano=-1;
								break;
							}
						}else {
							System.out.println("Por favor ingrese coordenadas validas"); //si ingresa coordenadas fuera del limite le mandara este mensaje
						}
					}else if(decide=='B') {
						System.out.println("Dame la celda que quieres señalar como bandera");//si quiere una bandera aqui pide coordenadas
						int fila=pen.nextInt();
						int columna=pen.nextInt();
						if(matriz[fila][columna].visitado==false) {// si la celda esta abierta no tiene caso asignar una bandera
							matrizUsuario[fila][columna]="-!!";//el simbolo de la bandera es: !!
						}
						imprimirfantasma(matrizUsuario,filas,columnas);
					}else {
						System.out.println("Opcion no valida"); //si no dijita una entrada valida le mand este mensaje
					}
					
					
				}while((gano!=1) || (gano!=-1)) ;//aqui misntras que gane o pierda se repetira el do-while
				if(gano==1) {
					System.out.println("Felicidades haz ganado!!");
					JOptionPane.showMessageDialog(null,"Felicidades haz ganado!!");// si es 1 significa que gana y le mostrara este mensaje
				}else if(gano==-1) {
					System.out.println("Perdiste, mejor suerte la proxima");
					JOptionPane.showMessageDialog(null,"Perdiste, mejor suerte la proxima");//si es - significa que perdio
				}
		}

			
			
	}

	public static boolean verificagane(Porcion[][] matriz) {//verifica que gana
		/*
		 * Recorre la matriz y donde ya haya visitado significa que ya las abrio y le sumara uno a gane
		 * como la matriz es de 15x15 entotal son 225 casillas asi que tenemos 30 minas 
		 * entonces si a 225 le quitamos lo que sumo gane y da 30 quiere decir que solo quedan las minas
		 * y entonces ya gano
		 */
		int gane=0;
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz.length; j++) {
				if(matriz[i][j].visitado==true) {
					gane++;
				}
			}
		}
		int de= 225-gane;
		if(de==30) {
			return true;
		}
		return false;
	}
	public static void explorar(Porcion[][]matriz, int fila, int columna) {
		/*
		 * Funcion que explora la matriz cuando se encuentra con un 0 que significa que hay una explocion y no
		 * hay minas al rededor hasta encontrar un numero
		 */
		
		for (int i = fila-1; i <= fila+1; i++) {
			for (int j = columna-1; j <=columna+1; j++) {
				if((i>=0)&&(i<=14)&&(j>=0)&&(j<=14)) {
					if(!matriz[i][j].visitado) {
						matriz[i][j].visitado=true;
						if(matriz[i][j].terreno==0) {
							explorar(matriz,i,j);
						}
					}
				}
			}
		}
	}

	public static String[][] DescubreMatriz(String[][] matruzusuario,Porcion [][] matriz, int coordenada1,int coordenada2){
		/*
		 * Descubre matriz tiene tres apartados cuando ve un 0, cuando tiene un numero y cuando encuentra una mina
		 */
		String [][] regreso=matruzusuario;
		if(matriz[coordenada1][coordenada2].terreno==0) {//si encuantra un cero llama a explorar y lo que regrese explorar lo pondra en la matrizUsuario
			explorar(matriz,coordenada1,coordenada2);
			for (int i = 0; i < matriz.length; i++) {
				for (int j = 0; j < matriz.length; j++) {
					if(matriz[i][j].visitado==true) {
						regreso[i][j]= Integer.toString(matriz[i][j].terreno);
					}
				}
			}
			
		}else if(matriz[coordenada1][coordenada2].terreno==-1){//si encuentra una mina va a destapar todas las minas
			for (int i = 0; i < matriz.length; i++) {
				for (int j = 0; j < matriz.length; j++) {
					if(matriz[i][j].terreno==-1) {
						regreso[i][j]="*";
					}
				}
			}
		}else { // si solo encuentra un numero diferente de 0 o -1 solo destapa el numero
		matriz[coordenada1][coordenada2].visitado=true;
		regreso[coordenada1][coordenada2]=Integer.toString(matriz[coordenada1][coordenada2].terreno);
		}
		return regreso;
	}
		
		public static void imprimir(Porcion [][]matriz, int filas, int columnas) {//imprime la matriz debajo de la capa
		for(int i=0;i<filas;i++) {
			for(int j=0;j<columnas;j++) {
				System.out.print("["+matriz[i][j].terreno+"]\t");
			}
			System.out.println();
		}
	}
	public static void imprimirfantasma(String [][]matriz, int filas, int columnas) {//imprime la maytriz que vera el usuario
		for(int i=0;i<filas;i++) {
			for(int j=0;j<columnas;j++) {
				System.out.print("["+matriz[i][j]+"]\t");
			}
			System.out.println();
		}
	}
	public static String[][] MatrizUsuario( int filas, int columnas) { //le asigna unos - a la matriz usuario para que vea que esta vacia
		String [][]matriz= new String[filas][columnas];
		for(int i=0;i<filas;i++) {
			for(int j=0;j<columnas;j++) {
				matriz[i][j]="-";
			}
			
		}
		return matriz;
	}
	public static Porcion [][] CrearMatriz(int filas, int columnas, int minas){//funcion que crea la matriz aleatoria con las minas
		Porcion [][] matrizBuscaminas=new Porcion[filas][columnas];//crea una matriz con las filas y columnas deseadas
		Random ralazar= new Random(); //importamos random que se usa en las minas aleatorias
		for (int i = 0; i < matrizBuscaminas.length; i++) {//inicializa la matriz con puros ceros
			for (int j = 0; j < matrizBuscaminas.length; j++) {
				matrizBuscaminas[i][j]= new Porcion();
				matrizBuscaminas[i][j].indicador=0;
				matrizBuscaminas[i][j].terreno=0;
				matrizBuscaminas[i][j].visitado=false;
			}
		}
		
		
		while(minas>0) {//mientras las minas sean mayores que 0 va a ejecutarce la asignacion de minas
			int x= ralazar.nextInt(filas); //aleatoriamente asigna un valos entre el parametro filas y columnas
			int y= ralazar.nextInt(columnas);
			
			if(matrizBuscaminas[x][y].terreno!=-1) {//condicion para que no asigne una mina donde ya la habia asignado si es que existe
				matrizBuscaminas[x][y].terreno=-1;
				minas--;
			}
		}
		 //BUSCA LAS MINAS QUE ESTAN AL REDEDOR
		for (int i = 0; i < filas; i++) {
			for (int j = 0; j < columnas;j++) {
				
				for (int k = i-1; k <= i+1; k++) {
					for (int l = j-1; l <= j+1; l++) {
						if(k>=0 && l>=0&&k<filas&&l<columnas) {
							if(matrizBuscaminas[k][l].terreno==-1 && matrizBuscaminas[i][j].terreno!=-1) {// identifica que la posicion de i sea =-1 y la segunda sea un valor diferencte de -1
								matrizBuscaminas[i][j].terreno++;
							}
						}
					}
					
				}
			
			}
			
		}
		return matrizBuscaminas;//retorna la matriz con minas y numeros
	}

}
