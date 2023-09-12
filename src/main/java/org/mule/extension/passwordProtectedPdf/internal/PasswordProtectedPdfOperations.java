package org.mule.extension.passwordProtectedPdf.internal;
import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.pdmodel.font.PDFont;

public class PasswordProtectedPdfOperations{
	private static final PDFont FONT = PDType1Font.HELVETICA;
    private static final float FONT_SIZE = 12;
    private static final float LEADING = -1.5f * FONT_SIZE; 
    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordProtectedPdfOperations.class);
    private static PDPageContentStream contentStream = null;
	@MediaType(value = ANY,strict = false)
	@Alias("createPdf")
	public InputStream makePdf(@ParameterGroup(name = "General Setting") PasswordProtectedPdfParameter p) throws IOException {
		 
		 try {
	            PDDocument doc = new PDDocument();
	            PDPage page = new PDPage();
	            doc.addPage(page);
	            PDRectangle mediaBox = page.getMediaBox();
	            float marginY = 80;
	            float marginX = 60;
	            float width = mediaBox.getWidth() - 2 * marginX;
	            float startX = mediaBox.getLowerLeftX() + marginX;
	            System.out.println(startX);
	            float startY = mediaBox.getUpperRightY() - marginY;
	            int lineNum = 0;
	            
	 
	            
	            contentStream = new PDPageContentStream(doc, page);
	           
	            try {
	                contentStream.beginText();
	                contentStream.setFont(FONT, FONT_SIZE);
	                contentStream.newLineAtOffset(startX,startY);
	              
	               //addtitle   
	                for(int i=0;i<startX;i++) {
						contentStream.showText(" ");
	                }
	                contentStream.showText(p.getTitle());
	                contentStream.newLine();
	                contentStream.newLine();
	                contentStream.newLineAtOffset(0,-15);
	                contentStream.setFont(FONT, FONT_SIZE);
	            
	                List<String> lines = parseLines(p.getContent(), width);
	                for (String line : lines) {
	                    float charSpacing = 0;
	                    if (line.length() > 1) {
	                        float size = FONT_SIZE * FONT.getStringWidth(line) / 1000;
	                        float free = width - size;
	                        if (free > 0 && !lines.get(lines.size() - 1).equals(line)) {
	                            charSpacing = free / (line.length() - 1);
	                        }
	                    }
	                    contentStream.setCharacterSpacing(charSpacing);
	                    contentStream.showText(line);
	                    contentStream.newLineAtOffset(0, LEADING);
	                    lineNum++;
	                    if (lineNum > 35 && !lines.get(lines.size() - 1).equals(line)) {
	                        contentStream.endText();
	                      //Add Image
	                        if(p.getImage() != null) {
	                        setImage(doc,p.getImage(),p.getMoveImageByX(),p.getMoveImageByY(),p.getOpacity());
	                        }
	                        contentStream.close();
	                        page = new PDPage();
	                        doc.addPage(page);
	                        contentStream = new PDPageContentStream(doc, page);
	                        contentStream.beginText();
	                        contentStream.setFont(FONT, FONT_SIZE);
	                        contentStream.newLineAtOffset(startX, startY);
	                        lineNum = 0;
	                    }
	                }
	                contentStream.endText();
	                if(p.getImage() != null) {
                       setImage(doc,p.getImage(),p.getMoveImageByX(),p.getMoveImageByY(),p.getOpacity());
                        }
	                contentStream.close();
	              
	              
	                setPassword(p.getPassword(),doc);
	                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	    			doc.save(byteArrayOutputStream);
	    			InputStream returnThisPlease = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
	    			return returnThisPlease;
	            
	            }
	              finally {
	                if (contentStream != null) {
	                    contentStream.close();
	                }
	                doc.close();
	            }
	        } catch (IOException e) {
	            System.err.println("Exception while trying to create pdf document - " + e);
	        }
	        return null;
	              
	    }
	    private static List<String> parseLines(String text, float width) throws IOException {
	        List<String> lines = new ArrayList<String>();
	        int lastSpace = -1;
	        while (text.length() > 0) {
	            int spaceIndex = text.indexOf(' ', lastSpace + 1);
	            if (spaceIndex < 0)
	                spaceIndex = text.length();
	            String subString = text.substring(0, spaceIndex);
	            float size = FONT_SIZE * FONT.getStringWidth(subString) / 1000;
	            if (size > width) {
	                if (lastSpace < 0){
	                    lastSpace = spaceIndex;
	                }
	                subString = text.substring(0, lastSpace);
	                lines.add(subString);
	                text = text.substring(lastSpace).trim();
	                lastSpace = -1;
	            } else if (spaceIndex == text.length()) {
	                lines.add(text);
	                text = "";
	            } else {
	                lastSpace = spaceIndex;
	            }
	        }
	        return lines;
	    }
	    private static void setPassword(String customPassword,PDDocument doc) throws IOException {
	    	AccessPermission accessPermission = new AccessPermission();
			StandardProtectionPolicy standardProtectionPolicy
			  = new StandardProtectionPolicy("ownerpass", customPassword, accessPermission);
			doc.protect(standardProtectionPolicy);
	    }
	    private static void setImage(PDDocument doc, String imagePath,int moveImageX,int moveImageY,float opacity) throws IOException {
	    	if(moveImageX == 0 && moveImageY == 0) {
	    		moveImageX = 200;
	    		moveImageY = 250;
	    	}
	    	else if (moveImageX == 0 && moveImageY > 0) {
	    		moveImageX = 200;
	    	}
	    	else if (moveImageX > 0 && moveImageY == 0) {
	    		moveImageY = 250;
	    	}
	    	if(opacity == 0.0) opacity = 0.5f;
	    	
	    	PDImageXObject pdImage = PDImageXObject.createFromFile(imagePath, doc);
	    	 contentStream.saveGraphicsState();
             PDExtendedGraphicsState pdExtGfxState = new PDExtendedGraphicsState();
             pdExtGfxState.getCOSObject().setItem(COSName.BM, COSName.MULTIPLY); 
             pdExtGfxState.setNonStrokingAlphaConstant(opacity);
             contentStream.setGraphicsStateParameters(pdExtGfxState);
             contentStream.drawImage(pdImage, moveImageX, moveImageY);
             contentStream.restoreGraphicsState();
	    }
}

