package aed.huffman;

import es.upm.aedlib.Entry;
import es.upm.aedlib.Position;
import es.upm.aedlib.map.*;
import es.upm.aedlib.tree.*;
import es.upm.aedlib.priorityqueue.*;


public class Huffman{

  public static Map<Character,Integer> frequencies(String texto){
    Map<Character, Integer> mapa = new HashTableMap<Character, Integer>();
    for(int i = 0;i< texto.length(); i++){
      char palabra = texto.charAt(i);
      Integer apariciones = mapa.get(palabra); // ¿puede pertenecer?
      if(apariciones == null){
        mapa.put(palabra,1);
      }else{                         // añado o sumo dependiendo del if y else
       mapa.put(palabra,apariciones+1);
      }
    }
    return mapa;
  }

  public static BinaryTree<Character> constructHuffmanTree(Map<Character,Integer> charCounts){
	    PriorityQueue<Integer,BinaryTree<Character>> c = new SortedListPriorityQueue<>();
	    for(Character t:charCounts.keys()){
	      BinaryTree<Character> d = new LinkedBinaryTree<Character>();
	      d.addRoot(t);
	      c.enqueue(charCounts.get(t), d);
	    }															// creamos el constructor
	    while(c.size() > 1){
	        Entry<Integer,BinaryTree<Character>> left = c.dequeue();
	        Entry<Integer,BinaryTree<Character>> right = c.dequeue();
	        BinaryTree<Character> d = joinTrees(' ',left.getValue(),right.getValue());
	        c.enqueue(left.getKey() + right.getKey(),d);
	    }
	    return c.dequeue().getValue();
	}

  public static <E> BinaryTree<E> joinTrees(E e, BinaryTree<E> leftTree,BinaryTree<E> rightTree){
    BinaryTree<E>res=new LinkedBinaryTree<>();
    Position<E> empieza = res.addRoot(e);    // nodo padre
    meter(res, empieza, rightTree, rightTree.root(), false);
	meter(res, empieza, leftTree, leftTree.root(), true); // empezamos con la recursividad en ambos hijos
	
	return res;
    
  }
  private static <E> void meter(BinaryTree<E> devolver, Position<E> t,BinaryTree<E> h, Position<E> nh, boolean i) {
	  
	  if(nh!=null) {
		  if(i) {
			  t=devolver.insertLeft(t, nh.element());			// introducimos en el lado correcto
		  }
		  else {
			  t=devolver.insertRight(t, nh.element());
		  }
		  
		  meter(devolver, t, h, h.right(nh), false);
		  meter(devolver, t, h, h.left(nh), true);
		  
	  }
  }

  public static Map<Character, String> characterCodes(BinaryTree<Character> tree) {
	    Map<Character, String> mapa = new HashTableMap<Character, String>();
	    characterCodesAux(tree, tree.root(), mapa, "");    // creamos resultado y llamamos recursividad
	    return mapa;
	}

	private static void characterCodesAux(BinaryTree<Character> t, Position<Character> p, Map<Character, String> mapa, String camino) {
	    if (p != null){
	        if (t.isExternal(p)){
	            mapa.put(p.element(), camino);
	        }else{
	            characterCodesAux(t, t.left(p), mapa, camino + "0");          //si no es una hoja avanzo
	            characterCodesAux(t, t.right(p), mapa, camino + "1");
	        }
	    }
	}

  public static String encode(String text, Map<Character,String> map){
    String solucion="";
    int k=0;
    while(k<text.length()) {
    	char caracter=text.charAt(k);
    	solucion+=map.get(caracter);   // Cojo los caracteres del string y 
    	k++;								//los busco en el mapa para codificarlos con la estructura del mapa
    }
    return solucion;
  }

  public static String decode(String encodedText, BinaryTree<Character> huffmanTree){
    return decodeAux(encodedText,0,huffmanTree,huffmanTree.root());
  }
  private static String decodeAux(String palabra, int k, BinaryTree<Character> arbol, Position<Character> c){
    
	  if(palabra.length()==k) {
		  return "";  // termino
	  }
	  char numero= palabra.charAt(k);
	  if(numero=='1') {
		  c=arbol.right(c); // compruebo a que hijo voy
	  }
	  else {
		  c=arbol.left(c);
	  }
	  if(arbol.isExternal(c)) {
		  return c.element() +decodeAux(palabra,k+1,arbol,arbol.root());    //si es hoja
	  }
	  return decodeAux(palabra,k+1,arbol,c);   // si  no es hoja
  }

  
}

