
import java.util.*;


public class Huffman {
	

private class Node implements Comparable<Node> {
		char data;
		int weight;
		Node left;
		Node right;

		public Node(char data, int weight, Node left, Node right) {
			this.data = data;
			this.weight = weight;
			this.left = left;
			this.right = right;
		}
					
		@Override
		public boolean equals(Object ob) {
			Node o =(Node) ob;
			return this.data == o.data;
		}

		@Override
		public int compareTo(Node o) {
			return this.weight - o.weight;
		}

		public boolean isLeaf() {
			return left == null && right == null;
		}

}


	private String code;
	
	public Huffman(){
		code = "";
		
	}
	/**
	 * Takes a String and uses huffman coding encryption to encrypt the string
	 * 
	 * @param str String to be encrypted
	 * @return String containing huffman tree and input encrypted according to value in huffman-tree
	 * @throws NoSuchElementException If input String is empty
	 */
	public String encrypt(String str){
		code = "";
		if(str == "")
			throw new NoSuchElementException("Empty input string!");

		char[] stringAsArray = str.toCharArray();
		PriorityQueue<Node> nodeQueue = countAndAddToQueue(stringAsArray);
		 Node root = createTree(nodeQueue);
		 String[] codeList = new String[256];
		 createCodeTable(root, codeList, "");
		 
		 printTree(root);
		 
		 
		 code +=  str.length() + " ";
		
		 for(int i = 0; i < str.length(); i++){
			 String characterCode = codeList[stringAsArray[i]];
			 for(int x = 0; x < characterCode.length(); x++){
				 code += characterCode.charAt(x);
			 }
		 }
		 
		
		return code;
	}

	
	/**
	 * Creates a character node for each individual character and counts frequency of each node
	 * 
	 * @param characters char [] of each character to be counted
	 * @return priorityQueue containing nodes ordered by weight
	 */
	private PriorityQueue<Node> countAndAddToQueue(char[] characters){
		
		ArrayList<Node> list = new ArrayList<Node>();
		for(char c : characters){
			Node n = new Node(c,1, null,null);
			if(list.contains(n)){
			   int n1  = list.indexOf(n); 
			   list.get(n1).weight++;
			}else
				list.add(n);
		}
		return new PriorityQueue<Node>(list);
	
	}
	
	/**
	 * Builds huffman-tree. Goes through all nodes in the priorityQueue while size of queue is bigger than 1, takes the first two values in the queue
	 * (values with lowest weight) and adds them as children to a new parent node. The parent node is then added back to the priorityQueue. When size is == 1, 
	 * the huffman tree has been created, the remaining node in priorityQueue is the root of this tree and is returned.   
	 * @param nodeQueue Queue of nodes to be assembled into huffman-tree
	 * @return Root node of huffman-tree
	 */
	private Node createTree(PriorityQueue<Node> nodeQueue){
		while(nodeQueue.size() > 1){
			Node node1 = nodeQueue.remove();
			Node node2 = nodeQueue.remove();
			Node parent = new Node('\0',node1.weight+node2.weight, node1, node2);
			nodeQueue.add(parent);
	
		}
		
		return nodeQueue.remove();
	}
	
	
	
	/**
	 * Recursive function, objective is to traverse the huffman tree and append binary information of the tree to the start of the encrypted string. If node is a leaf then binary true + character code in byte form is appended,
	 * else binary false is appended and children nodes are sent recursively back to the function. 
	 * @param node Node to be coded
	 */
	private void printTree(Node node) {
		if(node.isLeaf()){		
			code += "1";
			code += toByte(node.data);
			return;
		}else{
			code += "0";
			printTree(node.left);
			printTree(node.right);
		}
		
		
	}
	/**
	 * Takes character data and transforms to binary, ensures that each character is represented by one Byte, no bits should be lost.
	 * @param data character to decode
	 * @return	String representing binary byte of character
	 */
	private String toByte(char data) {
		String d = Integer.toBinaryString(256 + (int) data).substring(1);
		return d;
	}

	/**
	 * Recursive function, traverses huffman-tree and inserts huffman-code for each character into an array containing huffman-code.
	 * If node is NOT a leaf the function is called recursively for children of node and path to node is added to String parameter. 
	 * If node is a leaf the path-code created is added to respective character value in array
	 * @param node Current node being traversed
	 * @param codeList	List with 256 positions containing huffman-code for each character present in the string to be encrypted
	 * @param string String containing huffman-code to each leaf in the tree.
	 */
	private void createCodeTable(Node node, String[] codeList, String string) {
		if(!node.isLeaf()){
			createCodeTable(node.left, codeList, string + '0');
			createCodeTable(node.right, codeList, string + '1');
		}else{

			codeList[node.data] = string;		
		}
	}
	
	public String decrypt(String encryption) {
		String decrypt = "";
	
		Scanner sc = new Scanner(encryption);
		Node root = decryptTree(sc);

		String size = "";
		String next = sc.next();
		while(!next.equals(" ")){
			size += next;
			next = sc.next();
		}
		sc.useDelimiter("");

			
		for(int i = 0; i < Integer.parseInt(size); i++){
			Node node = root;
			while(!node.isLeaf()){
				String direction = sc.next();
				if(direction.equals("0"))
					node = node.left;
				if(direction.equals("1"))
					node = node.right;
			}
			decrypt += node.data;
		}
		return decrypt;
	}



	private Node decryptTree(Scanner sc) {
		sc.useDelimiter("");
		String nodeByte = "";
		String current = sc.next();
	
		if(current.equals("1")){
			while(nodeByte.length() < 8){
				nodeByte += sc.next();
			}
				return new Node((char)Integer.parseInt(nodeByte,2),-1 ,null, null);
			
		}
			Node leftChild = decryptTree(sc);
			Node rightChild = decryptTree(sc);
			return new Node('\0', -1, leftChild, rightChild);
		
		
		
			

	}
	


}
