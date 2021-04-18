package application;



public class Node implements Comparable<Node> {
	private int count;

	private byte ch;
	private String huffman;
	boolean avilable;
	Node left;
	Node right;
	
	public Node(int count, byte ch) {
		this.count = count;
		this.ch = ch;
		left = null;
		right = null;
	}

	public Node() {
	
	}
	
	public void setch(byte byteCount) {
		this. ch=byteCount;
	}
	
	public byte getCh() {
		return ch;
	}
	public Node(int count, byte ch, String huffman, boolean avilable) {
		super();
		this.count = count;
		this.ch = ch;
		this.huffman = huffman;
		this.avilable = avilable;
	}
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	@Override
	public int compareTo(Node object) {
		if (this.count < object.getCount())
			return -1 ; 
		else if (this.getCount() > object.getCount() )
			return 1 ; 
		return 0 ;
	}

	

	
	
	
	
	
	
	
	public String getHuffman() {
		return huffman;
	}

	public void setHuffman(String huffman) {
		this.huffman = huffman;
	}

	@Override
	public String toString() {
		return "Node [count=" + count + ", ch=" + ch +  "]";
	}

	
}
