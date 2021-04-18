package application;

import java.io.Serializable;

public class Header implements Serializable{
	private String FileName; 
	private int FileSize; 
	private int count[]; 
	private byte bytes[]; 
	
	public Header() {
		
	}
	
	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}


	public Header (String FileName , int FileSize){
		if (FileName.contains("\\")){
			String FileNmae[] = FileName.replace('\\', '*').split("\\*"); 
			//last name in the Path
			this.FileName = FileNmae[FileNmae.length-1];
			}
		this.FileSize = FileSize;
	}

	public String getFileName() {
		return FileName;
	}

	public void setFileName(String fileName) {
		FileName = fileName;
	}

	public int getFileSize() {
		return FileSize;
	}

	public void setFileSize(int fileSize) {
		FileSize = fileSize;
	}

	public int[] getCount() {
		return count;
	}

	public void setCount(int[] count) {
		this.count = count;
	}


}