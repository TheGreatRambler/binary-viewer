package gui;

// gui
import java.awt.*;

// piccolo2d
import org.piccolo2d.*;
import org.piccolo2d.nodes.*;
import org.piccolo2d.event.*;

// commons io
import org.apache.commons.io.FileUtils;

// built in
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.geom.Point2D;
import java.nio.charset.Charset;

// JHexView
import tv.porst.jhexview.JHexView.DefinitionStatus;
import tv.porst.jhexview.SimpleDataProvider;

public class binaryDisplay extends PCanvas {
	private static final long serialVersionUID = 1L;
	private PPath[] bitBoxArray;
    private ArrayList < PPath > bitSurroundingRects;
    private int currentByteHover = -1;
    private int currentBitHover = -1;
    private PText hoverInfo = new PText();
    private PText pageInfo = new PText();
    private boolean hasFile = false;
    private boolean justShifted = false;
    private int currentPage;
    private int pagesCreated = 0;
    private byte[] currentBytes;
    private int bitsInPage;
    @SuppressWarnings("unused")
	private String[] hexStringOfPagedBytes;

    private PPath getBitBox(int bitNum, Color fColor, Color bColor) {
        int x = (bitNum % settings.binaryWidth) * (settings.binaryBoxWidth + 2);
        int y = (bitNum / settings.binaryWidth) * (settings.binaryBoxWidth + 2); // automatically rounds
        PPath bitBox = PPath.createRectangle(x, y, settings.binaryBoxWidth, settings.binaryBoxWidth);
        bitBox.setPaint(fColor);
        bitBox.setStrokePaint(bColor);
        return bitBox;
    }

    private String getHexString(byte raw) {
        // https://stackoverflow.com/a/26975031/9329945
        String HEXES = "0123456789ABCDEF";
        return "0x" + HEXES.charAt((raw & 0xF0) >> 4) + HEXES.charAt((raw & 0x0F));
    }
    
    private byte getByteFromBits(boolean[] bits) {
        String bitString = "";
        for (int currentBit = 0; currentBit < 8; currentBit++) {
            bitString += (bits[currentBit] == true) ? ("1") : ("0");
        }
        return (byte) Integer.parseInt(bitString, 2);
    }
    
    private PPath addBit(boolean bit, int loc, byte thisByte, int visibleLoc) {
        Color fillColor = (bit == true) ? (settings.trueBitColor) : (settings.falseBitColor); // add colors to settings
        PPath bitBox = getBitBox(visibleLoc, fillColor, settings.bitBorderColor); // add colors to settings
        bitBox.addAttribute("type", "bitBox");
        bitBox.addAttribute("bitVal", bit);
        bitBox.addAttribute("bitLoc", loc);
        bitBox.addAttribute("byteNum", loc / 8); // automatically rounds
        bitBox.addAttribute("bitByteLoc", loc % 8); // location in byte
        bitBox.addAttribute("byte", thisByte); // automatically rounds and returns byte
        bitBox.addAttribute("bufferOffset", visibleLoc);
        if (bitBoxArray[loc] != null) {
        	bitBoxArray[loc].removeFromParent();
        }
        bitBoxArray[loc] = bitBox; // overwrites if problem
        return bitBox;
    }
    
    public void createNew(int length) {
    	setupBitBoxArray(length * 8);
    	int currentByte = 0;
    	while ((length - currentByte) > settings.fileBufferSize) {
    		boolean[] bits = new boolean[settings.fileBufferSize * 8];
    		Arrays.fill(bits,  false);
    		byte[] bytes = new byte[settings.fileBufferSize];
    		Arrays.fill(bytes, (byte) 0);
    		addBitArray(bits, currentByte * 8, bytes);
    		currentByte += settings.fileBufferSize;
    	}
    	boolean[] finalBits = new boolean[(length - currentByte) * 8];
		Arrays.fill(finalBits,  false);
		byte[] finalBytes = new byte[(length - currentByte)];
		Arrays.fill(finalBytes, (byte) 0);
		addBitArray(finalBits, currentByte * 8, finalBytes);
    }
    
    public void addBitArray(boolean[] bits, int offset, byte[] bytes) {
        for (int currentBitIndex = 0; currentBitIndex < bits.length; currentBitIndex++) {
            int actualBitIndex = currentBitIndex + offset;
            addBit(bits[currentBitIndex], actualBitIndex, bytes[currentBitIndex / 8], currentBitIndex);
            int byteIndex = actualBitIndex / 8;
            currentBytes[byteIndex] = bytes[currentBitIndex / 8];
        }
        pagesCreated++;
        setPageInfo(currentPage, pagesCreated); // update with text
        if (pagesCreated == 1) {
            showPage(1, -1);
        }
    }
    
    private void setPageInfo(int pageToSet, int totalPages) {
        pageInfo.setText("Page " + pageToSet + " out of " + totalPages + "\nEach page is " + FileUtils.byteCountToDisplaySize(settings.fileBufferSize));
        pageInfo.raiseToTop();
		pageInfo.repaint();
    }
    
    public void resetCompletely() { // completely wipes everything so you can start over
    	currentByteHover = -1;
        currentBitHover = -1;
        pagesCreated = 0;
		if (hasFile) {
			getRoot().removeFromParent();
			hasFile = false;
		}
		hoverInfo.removeFromParent();
		hoverInfo = new PText();
		pageInfo.removeFromParent();
		pageInfo = new PText();
		justShifted = false;
		//bitSurroundingRects = null;
		//bitBoxArray = null;
    }
    
    public byte[] getEditedBytes() {
        /*
    	int totalBytes = bitBoxArray.length / 8;
    	byte[] returnBytes = new byte[totalBytes];
    	for (int currentByte = 0; currentByte < totalBytes; currentByte++) {
    		boolean[] bits = new boolean[8];
    		for (int currentBit = 0; currentBit < 8; currentBit++) {
    			int bitLoc = (currentByte * 8) + currentBit;
    			bits[currentBit] = (boolean) bitBoxArray[bitLoc].getAttribute("bitVal")
    		}
    		returnBytes[currentByte] = getByteFromBits(bits);
    	}
    	return returnBytes;
    	*/
    	return currentBytes; //this simple now!
    }
    
    private void showPage(int pageNum, int lastPageNum) {
        if (pageNum >= 1 && pageNum <= pagesCreated) {
            for (int bitNum = 0; bitNum < bitsInPage; bitNum++) {
                int drawOffset = (pageNum - 1) * bitsInPage + bitNum;
                if (lastPageNum != -1) { // if else, there was no last page. This means this is the first
                    int removeOffset = (lastPageNum - 1) * bitsInPage + bitNum;
                    if (bitBoxArray.length > removeOffset) {
                        bitBoxArray[removeOffset].removeFromParent();
                    }
                }
                if (bitBoxArray.length > drawOffset) {
                    getLayer().addChild(bitBoxArray[drawOffset]);
                    bitBoxArray[drawOffset].repaint(); // really just paint
                }
            }
            currentPage = pageNum;
            setPageInfo(currentPage, pagesCreated);
            updateHexString(currentPage);
        } else {
            // error just in case
        }
    }
    
    private void updateHex() {
    	gui.hexView.setData(new SimpleDataProvider(getEditedBytes()));
        gui.hexView.setDefinitionStatus(DefinitionStatus.DEFINED);
        gui.hexView.setEnabled(true);
    }
    
    public void loadingDone() {
    	updateHex();
    }
    
    public void setupBitBoxArray(int length) { // this needs to be called first. sets everything
    	hasFile = true;
        bitBoxArray = new PPath[length];
        
        hoverInfo.setFont(settings.infoHoverFont);
        pageInfo.setFont(settings.infoHoverFont);
        hoverInfo.setX(5);
        pageInfo.setX(5);
        hoverInfo.setY(5);
        int pageInfoY = (int) getCamera().getViewBounds().getHeight() - 50; // being generous here
        pageInfo.setY(pageInfoY); 
        getCamera().addChild(hoverInfo);
        getCamera().addChild(pageInfo);
        
        currentBytes = new byte[length / 8];
        hexStringOfPagedBytes = new String[length / 8];
        
        bitsInPage = settings.fileBufferSize * 8;
    }

    private void removeBitSurroundingRects() {
        if (bitSurroundingRects != null) {
            for (PPath surroundingRect: bitSurroundingRects) {
                surroundingRect.removeFromParent();
            }
            bitSurroundingRects = null;
        }
    }
    
    private void updateHexString(int page) { // only when changing pages
        for (int currentByteIndex = 0; currentByteIndex < bitsInPage / 8; currentByteIndex++) {
            //int actualOffset = ((bitsInPage / 8) * realPageNum) + currentByteIndex;
            //byte currentByte = currentBytes[actualOffset];
    		//String hexString = getUnicode(currentByte); // or get hex string
    		//hexStringOfPagedBytes[currentByteIndex] = hexString;
        }
    }
    
    @SuppressWarnings("unused")
	private void updateCurrentBytes(int page) { // only used in extreme cases
        int realPageNum = page - 1; // because of 0 based array indexing
        for (int currentByteIndex = 0; currentByteIndex < bitsInPage / 8; currentByteIndex++) {
            int actualOffset = ((bitsInPage / 8) * realPageNum) + currentByteIndex;
            boolean[] bits = new boolean[8];
    		for (int currentBit = 0; currentBit < 8; currentBit++) {
    			int bitLoc = (actualOffset * 8) + currentBit;
    			bits[currentBit] = (boolean) bitBoxArray[bitLoc].getAttribute("bitVal");
    		}
    		currentBytes[actualOffset] = getByteFromBits(bits);
        }
    }
    
    private void drawTextBox(Point2D mouseLocation, String text) {
    	hoverInfo.setText(text);
    	//hoverInfo.recomputeLayout();
    	if (!hoverInfo.getVisible()) {
			hoverInfo.setVisible(true);
		}
		hoverInfo.raiseToTop();
		hoverInfo.repaint();
    }
    
    private void hideTextBox() {
    	if (hoverInfo.getVisible()) {
    		hoverInfo.setVisible(false);
    	}
    }
    
    private boolean getBitToDraw() {
    	String selectedItem = gui.bitToDraw.getSelection().getActionCommand();
    	if (selectedItem == "bitPaintTrue") {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public void flipPage(int direction) {
    	if (direction == 0) {
    		// left
    		if (currentPage > 1) {
                showPage(currentPage - 1, currentPage);
            } else {
                // let user know command was invalid
            }
    	} else {
    		// right
    		if (currentPage < pagesCreated) {
                showPage(currentPage + 1, currentPage);
            } else {
                // let user know command was invalid
            }
    	}
    }
    
    private String getUnicode(byte byteChar) {
    	byte[] byteArray = new byte[1];
    	byteArray[0] = byteChar;
    	return new String(byteArray, Charset.forName("UTF-8"));
    }

    public void setup() {
        addInputEventListener(new PBasicInputEventHandler() {
            public void mouseMoved(PInputEvent e) {
                PNode currentNode = e.getPickedNode();
                if (currentNode.getAttribute("type") == "bitBox" || currentNode.getAttribute("type") == "bitSurroundingBox") {
                	int bitLoc = (int) currentNode.getAttribute("bitLoc");
                    if (e.isShiftDown()) {
                    	justShifted = true;
                    	if (currentNode.getAttribute("type") != "bitSurroundingBox" && currentBitHover != bitLoc) {
                    		removeBitSurroundingRects();
                        	hideTextBox();
                        	int byteNum = (int) currentNode.getAttribute("byteNum");
                            boolean[] bits = new boolean[8];
                            for (int bitInByte = 0; bitInByte < 8; bitInByte++) {
                                int bitIndex = (byteNum * 8) + bitInByte;
                                PPath thisBit = bitBoxArray[bitIndex];
                                if (bitIndex == bitLoc) {
                                    bits[bitInByte] = getBitToDraw();
                                } else {
                                    bits[bitInByte] = (boolean) thisBit.getAttribute("bitVal");
                                }
                            }
                            byte newByte = getByteFromBits(bits);
                            int visibleLoc = (int) currentNode.getAttribute("bufferOffset");
                            int bitByteLoc = (int) currentNode.getAttribute("bitByteLoc");
                            for (int bitInByte = 0; bitInByte < 8; bitInByte++) {
                                int bitIndex = (byteNum * 8) + bitInByte;
                                int visibleIndex = (visibleLoc - bitByteLoc) + bitInByte; // i could just get the buffer offset from each item, but i will try this out first
                                PPath thisBit = bitBoxArray[bitIndex];
                                PPath bitToAdd;
                                if (bitIndex == bitLoc) {
                                    bitToAdd = addBit(getBitToDraw(), (int) thisBit.getAttribute("bitLoc"), newByte, visibleIndex);
                                } else {
                                	bitToAdd = addBit((boolean) thisBit.getAttribute("bitVal"), (int) thisBit.getAttribute("bitLoc"), newByte, visibleIndex);
                                }
                                getLayer().addChild(bitToAdd);
                                bitToAdd.repaint();
                            }
                            currentBytes[byteNum] = newByte; // IMPORTANT! Saves a ton of performance
                            updateHex(); // update hex view
                    	} else {
                    		removeBitSurroundingRects();
                    		currentBitHover = bitLoc + 1; // a hack to get this to work
                    	}
                    } else {
                        // just viewing
                    	int byteNum = (int) currentNode.getAttribute("byteNum");
                    	if (currentBitHover != bitLoc || justShifted) {
                    		justShifted = false;
                    		currentBitHover = bitLoc;
                    		String bitVal = ((boolean) currentNode.getAttribute("bitVal") == true) ? "1" : "0";
                    		// byteNum already declared above
                    		int bitByteLoc = (int) currentNode.getAttribute("bitByteLoc");
                    		byte theByte = (byte) currentNode.getAttribute("byte");
                    		String info = "Bit: " + bitVal + "\nBit number: " + bitLoc + "\nByte number: " + byteNum + "\nBit location in byte: " + bitByteLoc + "\nByte: " + getHexString(theByte) + " " + getUnicode(theByte);
                    		drawTextBox(e.getPosition(), info);
                    		if (currentByteHover == -1 || byteNum != currentByteHover) {
                            	currentByteHover = byteNum;
                                removeBitSurroundingRects();
                                bitSurroundingRects = new ArrayList < PPath > ();
                                for (int bitInByte = 0; bitInByte < 8; bitInByte++) {
                                    PPath thisBitBox = bitBoxArray[(byteNum * 8) + bitInByte];
                                    int bufferOffset = (int) thisBitBox.getAttribute("bufferOffset");
                                    //System.out.println(bufferOffset);
                                    PPath surroundingRect = getBitBox(bufferOffset, settings.bitSurroundingRectsBorderColor, settings.bitSurroundingRectsColor);
                                    surroundingRect.addAttribute("type", "bitSurroundingBox");
                                    surroundingRect.addAttribute("bitVal", (boolean) thisBitBox.getAttribute("bitVal"));
                                    surroundingRect.addAttribute("bitLoc", (int) thisBitBox.getAttribute("bitLoc"));
                                    surroundingRect.addAttribute("byteNum", (int) thisBitBox.getAttribute("byteNum"));
                                    surroundingRect.addAttribute("bitByteLoc", (int) thisBitBox.getAttribute("bitByteLoc"));
                                    surroundingRect.addAttribute("byte", (byte) thisBitBox.getAttribute("byte"));
                                    surroundingRect.addAttribute("bufferOffset", bufferOffset);
                                    bitSurroundingRects.add(surroundingRect);
                                    getLayer().addChild(surroundingRect);
                                    surroundingRect.repaint();
                                }
                            }
                    	}
                    }
                } else {
                	removeBitSurroundingRects();
                	hideTextBox();
                	currentByteHover = -1;
                	currentBitHover = -1;
                }
            }
        });
    }
}



