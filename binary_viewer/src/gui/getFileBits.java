package gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedInputStream;

import java.util.ArrayList;

//file libs
import java.io.File;

public class getFileBits implements Runnable {
	private BufferedInputStream inputStream;
	private File selectedFile;
	public getFileBits(File file) {
		try {
			inputStream = new BufferedInputStream(new FileInputStream(file));
			selectedFile = file;
		} catch (FileNotFoundException e) {
			System.out.println("File " + file.getAbsolutePath() + " does not exist or permissions are incorrect.");
		}
	}
	
	private boolean[] getBitArray(byte[] bytes) {
		boolean[] returnBits = new boolean[bytes.length * 8];
		for (int currentByte = 0; currentByte < bytes.length; currentByte++) {
			char[] bits = Integer.toBinaryString((bytes[currentByte] & 0xFF) + 0x100).substring(1).toCharArray();
			for (int currentBit = 0; currentBit < 8; currentBit++) {
				boolean thisBit = (bits[currentBit] == '0') ? (false) : (true);
				returnBits[(currentByte * 8) + currentBit] = thisBit;
			}
		}
		return returnBits;
	}
	
	public void run() {
		try {
			int totalNumOfBits = (int) selectedFile.length() * 8;
			gui.drawingLocation.setupBitBoxArray(totalNumOfBits);
   	 		ArrayList < Boolean > bits = new ArrayList<Boolean>(0);
   	 		byte[] buffer = new byte[settings.fileBufferSize];
   	 		//int len; // bytes
   	 		//int offsetHere = 0; // bytes
   	 		int loopTime = 0;
   	 		while ((totalNumOfBits - (loopTime * settings.fileBufferSize * 8)) >= (settings.fileBufferSize * 8)) {
   	 			inputStream.read(buffer);
   	 			boolean[] theseBits = getBitArray(buffer);
   	 			for (int thisBitNum = 0; thisBitNum < theseBits.length; thisBitNum++) {
   	 				bits.add(theseBits[thisBitNum]);
   	 			}
   	 			gui.drawingLocation.addBitArray(theseBits, loopTime * settings.fileBufferSize * 8, buffer);
   	 			loopTime++;
   	 		}
   	 		byte[] finalBuffer = new byte[(int) selectedFile.length() - (loopTime * settings.fileBufferSize)];
   	 		inputStream.read(finalBuffer);
			boolean[] lastBits = getBitArray(finalBuffer);
			for (int thisBitNum = 0; thisBitNum < lastBits.length; thisBitNum++) {
				bits.add(lastBits[thisBitNum]);
			}
			gui.drawingLocation.addBitArray(lastBits, loopTime * settings.fileBufferSize * 8, finalBuffer);
   	 		inputStream.close();
   	 		gui.drawingLocation.loadingDone();
		} catch (IOException fileError) {
			System.out.println("File " + selectedFile.getAbsolutePath() + " does not exist or permissions are incorrect.");
		}
    }
}
