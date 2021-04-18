package application;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

public class Compress {

	private String fileName ; //name of file/path
	private int  OByte =0 ; //original byte
	private int  compresionByte =0 ; //num of byte in huff file
	static Node []node = new Node [256]; 
	private Map[] map; //contain huff code and freq of each char

	double ratio;
	Header header;


	// build heap 
	void createHeap()
	{
		Heap h= new Heap(node.length+1 ); 
		//add nods to the min heap 
		for (int i=0;i<node.length;i++) 
		{
			h.addElt(node[i]);
		}
		//delete 2 nods from heap then add new node with fre =sum of 2 nodes freq
		//delete until heap size = 1
		while(h.size>1) {
			Node n = new Node(); 

			Node x = h.deleteMin();
			Node z = h.deleteMin();
			n.setCount(x.getCount()+z.getCount());
			n.left=x;
			n.right=z;
			h.addElt(n);

		}
		//print huffman code for each char, traversal
		printCode(h.getElt()[1], "");
	}

	//add all nods to the map
	Map[] createMap ()
	{
		map = new Map[node.length];
		for (int i=0;i<node.length;i++)
			map[i]=new Map(node[i].getCount(),node[i].getHuffman(),node[i].getCh());
		return map;
	}
	public static void printCode(Node root, String s) 
	{ 

		// base case; if the left and right are null 
		// then its a leaf node and we print 
		// the string s generate huffman code foe each char. 
		if (root.left  == null && root.right == null ) 
		{ 

			root.setHuffman(s); 

			return; 
		} 

		// if we go to left then add "0" to the huffman code. 
		// if we go to the right add"1" to the huffman code. 

		printCode(root.left, s + "0"); 
		printCode(root.right, s + "1"); 
	}


	void readFile () throws IOException 
	{
		RandomAccessFile file = new RandomAccessFile(fileName, "r");
		FileChannel fileC =  file.getChannel();
		ByteBuffer buf = ByteBuffer.allocate(1024);

		for (int i =0 ; i <node.length ; i++)
			node[i] = new Node();

		//set byte value 
		byte k = -128; 
		for (int i =0 ; i <node.length ; i++ , k++)
			node[i].setch(k);

		//fileC.read(buf) return -1 if the file is end
		while(fileC.read(buf) > 0)
		{
			buf.flip();
			for (int i = 0; i < buf.limit(); i++){
				byte tempByte  = buf.get() ; 
				node[tempByte + 128 ].setCount(node[tempByte +128].getCount() + 1);
				OByte++;
			}
			buf.clear(); 

		}
		fileC.close();
		file.close();
		//sort node array , then remove all zero freq bytes 
		Arrays.sort(node, 0, node.length);
		int index = 255  ; 
		for (int i =0 ; i<node.length ; i++) 
			if (node[i].getCount() != 0 ){
				index = i ; // index of first byte have freq > 0 
				break; 
			}
		//remove all zeros
		node = Arrays.copyOfRange(node, index, node.length);

	}


	public  void HuffmanFile() throws IOException {

		byte temp ; 
		int outputCount=0; // index for output array 
		String outputByte = "" ; 
		header = new Header(fileName, OByte);
		int freq[] = new int [node.length];
		byte bytes[] = new byte[node.length];
		for (int i=0 ; i <node.length ; i++)
			freq[i] = node[i].getCount();
		for (int i=0 ; i <node.length ; i++)
			bytes[i] = node[i].getCh();
		header.setCount(freq);
		header.setBytes(bytes);


		FileOutputStream outputFile = new FileOutputStream(new java.io.File(nameOfFile(fileName)  + ".huf"));
		ObjectOutputStream  outF = new ObjectOutputStream(outputFile);
		outF.writeObject(header); //write header on huff file

		BufferedOutputStream bufferedSteam = new BufferedOutputStream(outputFile);  
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(fileName));
		byte[]  output = new byte[6];//when we reach 6 byte write it in huff file
	
		
		
		for (int i=0;i<OByte;i++)
		{

			//read byte , then search if huffman code  
			byte character = (byte) in.read();
			String  huffmanCode = search(map, character);
			outputByte += huffmanCode; 

			// if output byte is less than 8 bits , continue to append more bits to the output String 
			if (outputByte.length() < 8 )
				continue;

			temp=(byte)(int)Integer.valueOf(outputByte.substring(0, 8), 2);
			output[outputCount++] = temp ; //add byte to the array of byte
			compresionByte ++; //num of byte in huff file
			if (outputByte.length() > 8 )
				// if huffmanCode contain more than 1 byte, we want to keep all bits more than needed byte  
				outputByte = outputByte.substring(8);
			else 
				outputByte = "" ;
			// write 6 bytes into .hfm file when outputbyte [] is filled
			if (outputCount == 6 ){

				bufferedSteam.write(output);
				outputCount = 0 ;
				output = new byte[6];
			}

		}//end while 
		// if last bytes didn't make complete array to be send, we want to send them 
		if (outputByte.length() != 0){
			while (outputByte.length()<8)
				outputByte += "0"; 
			//temp = toByte(outputByte.substring(0, 8));
			temp=(byte)(int)Integer.valueOf(outputByte, 2);
			output[outputCount++] = temp ; 
			
			compresionByte ++;
		}
		
		if (outputCount != 0 )
			bufferedSteam.write(output);

		//ratio = (1-(float)header.getBytes().length/ (float)OByte) * 100;

		bufferedSteam.close();
		in.close();
		outF.close();


	}
	
	
	
	

	
	
	
	
	
	

	
	
	//	 search for huffman code in map

	private String search(Map[] map, byte tempByte) {

		for (int i = 0 ; i < map.length ; i++){
			if ( map[i] != null && map[i].byteCount == tempByte )
				return map[i].Huffman ;
		}
		return "" ; //return empty string if huffman code not found 
	}


	public  String nameOfFile (String path){
		int i=-1;
		for (int j=0;j<path.length();j++)
		{
			if ((path.charAt(j)+"").equals("."))
				i=j;
		}
		return path.substring(0, i);
	}



	public String getFile() {
		return fileName;
	}
	public void setFile(String file) {
		fileName = file;
	}
	public int getNumOfByte() {
		return OByte;
	}
	public void setNumOfByte(int numOfByte) {
		this.OByte = numOfByte;
	}
	public Map[] getMap() {
		return map;
	}
	public void setMap(Map[] map) {
		this.map = map;
	}

	public int getCompresionByte() {
		return compresionByte;
	}

	public void setCompresionByte(int compresionByte) {
		this.compresionByte = compresionByte;
	}





}
