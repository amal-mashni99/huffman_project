package application;

/**
 * 	Decompress File is used to deCompres .huf files into its original format 
 * read Header from .huf file then creates its heap and find Huffman code for it 
 */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class Decompress {
	int OByte;
	String HuffFile; // input file is .hfm file , output file is the
	// uncompressed file
	Header header;
	Map[] HuffMap;
	ObjectInputStream ObjectReader;
	FileInputStream inputFileSteam; // is .hfm file
	BufferedOutputStream bufferedSteam;
	BufferedInputStream inputFile;
	private FileOutputStream outf;
	Node[] node;
	int headerSize;
	Compress c = new Compress();
int cSize =c.getCompresionByte();
	Decompress(String HuffFile) {

		this.HuffFile = HuffFile;
		try {
			File InputFile = new File(HuffFile);
			FileInputStream inputFileSteam = new FileInputStream(InputFile);
			ObjectInputStream ObjectReader = new ObjectInputStream(inputFileSteam);
			BufferedInputStream inputFile = new BufferedInputStream(inputFileSteam);
			header = (Header) (ObjectReader.readObject());// to read the header
			// from compress
			// file
			File outputFile = new File(header.getFileName());// give the
			// original name
			outf = new FileOutputStream(outputFile);							// of file (
			// which save
			// into compress
			// file) to new
			// file
			OByte = header.getFileSize();
			ObjectReader.close();
			inputFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createHeap() {
		
		 node = new Node[header.getCount().length];
		for (int i = 0; i < node.length; i++)
			node[i] = new Node(header.getCount()[i], header.getBytes()[i]);
		//	Heap<Counter> arrayofHeap[] = new Heap[header.getCount().length];
		Heap h= new Heap(header.getCount().length+1 ); 

		// create arrayofheap
		for (int i=0;i<node.length;i++) 
		{
			h.addElt(node[i]);
		}
		while (h.size> 1) {
			/*
			 * tempA is an array of Counters which is from array of Heap tempB
			 * is an array of Counters which is from array of Heap each array
			 * contain a max int which represents the count needed , all other
			 * items are its sum
			 */
			Node n = new Node(); 

			Node x = h.deleteMin();
			Node z = h.deleteMin();
			n.setCount(x.getCount()+z.getCount());
			n.left=x;
			n.right=z;
			h.addElt(n);
		}
		printCode(h.getElt()[1], "");

	}

	Map[] createMap ()
	{createHeap();
		HuffMap = new Map[header.getCount().length];
		for (int i=0;i<HuffMap.length;i++)
			HuffMap[i]=new Map(node[i].getCount(),node[i].getHuffman(),node[i].getCh());
		return HuffMap;
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







	

	public void readHuffFile() throws IOException, ClassNotFoundException {
		this.createHeap();
		// get last Name for file
		if (header.getFileName().contains("\\")) {
			String FileNmae[] = header.getFileName().replace('\\', '*').split("\\*");
			bufferedSteam = new BufferedOutputStream(new FileOutputStream(FileNmae[FileNmae.length - 1]));
		} else
			bufferedSteam = new BufferedOutputStream(new FileOutputStream(header.getFileName()));

		FileInputStream in = new FileInputStream(HuffFile);
		inputFile = new BufferedInputStream(in);
		//ObjectInputStream ob = new ObjectInputStream(in);
		// read the header
		//Header h = (Header) ob.readObject();

		byte[] output = new byte[4];
		int outputCount = 0;
		String bits = ""; // contain decompressed bits
		int counter = 0;
		ArrayList<String> listBit = new ArrayList<String>(); // contain huffman
		// code
		ArrayList<Byte> listchar = new ArrayList<Byte>();

		// while counter < Obytes mean that the file still have bytes to
		// read
		// inputFile have bytes ..stay in the loop
		while (counter < cSize && inputFile.available() > 0) {
			// read byte
			byte tempByte = (byte) inputFile.read();
			System.out.println(tempByte +"  "+counter);

			byte tempbit = 0;
			int bitCount = 0; // num of bits in byte

			while (bitCount < 8 && counter < cSize) {
				for (int k = 7; k >= 0 && counter < cSize; k--) {
					tempbit = getBit(tempByte, k);
					bits += tempbit;// append
					
					System.out.println(tempbit);
					if (listBit.size() == 0) {// list are empty
						// Search for huffman that starts with bits
						for (int j = 0; j < HuffMap.length; j++)
							if (HuffMap[j] != null && HuffMap[j].Huffman.startsWith(bits + "")) {
								listBit.add(HuffMap[j].Huffman);
								listchar.add(HuffMap[j].byteCount);
							}
					} else {// if list have values , search on these values .
						for (int i = 0; i < listBit.size(); i++)
							if (listBit.get(i).startsWith(bits + "") == false) {
								// remove items that doesn't match
								listBit.remove(i);
								listchar.remove(i);
								i--;
							}

					}
					bitCount++;

					if (listBit.size() == 1) { // element found
						// add element to output byte array
						output[outputCount++] = listchar.get(0);
						counter++;
						listBit.remove(0);
						listchar.remove(0);
						bits = "";
						if (outputCount == 4) {
							// write byte array into file when it is filled
							bufferedSteam.write(output);
							outf.write(output);
							bufferedSteam.flush();
							output = new byte[4];
							outputCount = 0;
						}
					}
				}
			}
		}
		if (outputCount != 0) {
			// write all reminder bytes
			for (int i = 0; i < outputCount; i++){
				bufferedSteam.write(output[i]);
				outf.write(output[i]);
			}
		}
		bufferedSteam.close();
		outf.close();
		//ob.close();
		inputFile.close();
	}

	public static byte getBit(byte ID, int position) {
		// return cretin bit in selected byte
		return (byte) ((ID >> position) & 1);
	}

}