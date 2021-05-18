import java.util.ArrayList;
import java.util.Arrays;

public class ProyectoCYK{

	public String cadena; 
	public ArrayList<ArrayList<String>> Gramatica;
	public ArrayList<Node>[][] matriz;
	
	public ProyectoCYK(ArrayList<ArrayList<String>> Gramatica, String cadena) {//inicializamos todo
		int capacidad=cadena.length();
		this.cadena = cadena;
		this.Gramatica = Gramatica;
		this.matriz = new ArrayList[capacidad][capacidad];
		
		for(int i=0;i<matriz.length;i++) {
			for(int j=0;j<matriz.length;j++) {
				matriz[i][j]=null;
			}
		}
		
		for(int i=0; i<matriz.length;i++) {
			for(int j=0; j<matriz.length;j++) {
				matriz[i][j] = new ArrayList<Node>();
				
			}
		}
	}
	
	public boolean solveCYK() {
		
		for(int i = 0; i<matriz.length;i++) {
			for(ArrayList<String> Produccion : Gramatica) {
				if(Produccion.contains(Character.toString(cadena.charAt(i)))){
					matriz[i][i].add(new Node(Produccion.get(0)));
				}
			}
		}
	
		for(int cont = 1; cont<matriz.length;cont++) {
			for(int i=0;i<matriz.length-cont;i++) {
				matriz[i][i+cont]=solveCasilla(i,i+cont);
			}
		}
		
		for(Node node : matriz[0][matriz.length-1]) {
			if(node.root.equals("S")){
				return true; 
			}
		}
		
		return false; 
	}
	
	public void printMatriz() {
		
		for(int i = 0; i<this.matriz.length;i++) {
			for(int j = 0; j<this.matriz.length;j++) {
				for(int k=0; k<this.matriz[i][j].size();k++) {
					System.out.print(matriz[i][j].get(k).root + " ");
				
				}
			}
			System.out.println();
		}
		
	}
	
	
	

	public ArrayList<Node> solveCasilla(int i, int j){//i=fila, j=columna
		int columna = i;
		ArrayList<Node> resultado = new ArrayList<Node>();
		for(int fila= i+1; fila<matriz.length;fila++) {
			//System.out.println(fila+""+columna);
			//columna ira disminuyendo para representar los cuadros a la izquierda, y fila aumentado para representar los cuadros abajo
			for(int x = 0; x<matriz[fila][j].size();x++) {
				for(int y = 0; y<matriz[i][columna].size();y++) {
					resultado.addAll(validar(matriz[i][columna].get(y),matriz[fila][j].get(x)));
				}
			}
			columna++;
		}
		return resultado;
	}
	
	public ArrayList<Node> validar(Node a, Node b) {
		ArrayList<Node> resultado = new ArrayList<Node>();
		String cadenaAChecar = a.root + b.root;//creamos una cadena que es la sumatoria de los dos nodos
		for(int i=0; i<Gramatica.size();i++) {//recorremos la gramatica
			for(int j= 1; j < Gramatica.get(i).size(); j++) {
				if(Gramatica.get(i).get(j).equals(cadenaAChecar)) {//comparamos la gramatica con la cadena a checar
					resultado.add(new Node(Gramatica.get(i).get(0),a,b));//si coincide agregamos el nodo
				}
			}
		}
		return resultado; 
	}
	
	public void printDerivacion() {
		for(Node node : matriz[0][matriz.length-1]) {
			if(node.root.equals("S")){
				printNode(node);
				break;
			}
		}
	}
	
	public void printNode(Node node) {
		if(node.tieneHijos()) {
			System.out.println(node.root);
			printNode(node.right);
			printNode(node.left);
		}else {
			System.out.println(node.root);
		}
	}
	
	public static void main(String[] args) {
		ArrayList<String> S = new ArrayList<String>(Arrays.asList("S","AB","SS","AC","BD"));
		ArrayList<String> A = new ArrayList<String>(Arrays.asList("A","a"));
		ArrayList<String> B = new ArrayList<String>(Arrays.asList("B","b"));
		ArrayList<String> C = new ArrayList<String>(Arrays.asList("C","SB"));
		ArrayList<String> D = new ArrayList<String>(Arrays.asList("D","SA"));
		
		ArrayList<ArrayList<String>> Gramatica = new ArrayList<ArrayList<String>>(Arrays.asList(S,A,B,C,D));
		
		String cadena = "aabbab";
		
		ProyectoCYK test = new ProyectoCYK(Gramatica, cadena);
		Boolean pr = test.solveCYK();
		test.printMatriz();
		
		System.out.println(pr);
		
		System.out.println("\nDerivaciones");
		test.printDerivacion();
		
	}
}

class Node{
	
	public String root;
	public Node right;
	public Node left;
	
	public Node(String raiz) {
		this.root = raiz; 
	}
	
	public Node(String raiz, Node hijoDerecho, Node hijoIzquierdo) {
		this.root = raiz;
		this.right = hijoDerecho;
		this.left = hijoIzquierdo; 
	}
	
	public boolean tieneHijos() {
		return right != null && left != null; 
	}
	
	
}
