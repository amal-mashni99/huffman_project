package application;

public class Map {
	int intCount;
	byte byteCount;
	String Huffman;
	
	
	public Map() {}
	
	public Map(int intCount, String Huffman, byte byteCount) {
		this.intCount = intCount;
		this.byteCount = byteCount ;
		this.Huffman = Huffman;
	}
	
	public Map(byte byteCount,int intCount, String Huffman ) {
		this.intCount = intCount;
		this.byteCount = byteCount ;
		this.Huffman = Huffman;
	}
	
	public Map(int intCount, String Huffman) {
		this.intCount = intCount;
		this.Huffman = Huffman;
	}
}
