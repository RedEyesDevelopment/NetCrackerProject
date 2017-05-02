package projectpackage.support.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Gvozd on 09.04.2017.
 */
public class PdfTool {
    //Simple main method for creating pdf file
    public static void createPDF(String[] data){
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, getSimpleFileDirectory());
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            fillDocument(document,data);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.close();
    }

//    METHOD FOR REAL FILE CREATING IN TEMPORARY FOLDER THAT WILL BE OPEN FOR HTTP REQUESTS
//    private static FileOutputStream getFileDirectoryThroughtRequest(String filename, HttpServletRequest request) throws FileNotFoundException {
//
//        StringBuilder rootPath = new StringBuilder(request.getServletContext().getRealPath("/").toString());
//        rootPath.append("/dynamic/");
//        File dir = new File(rootPath.toString() + File.separator);
//
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }
//
//        File pdfDoc = new File(dir.getAbsolutePath() + File.separator + filename);
//
//        FileOutputStream fileOutputStream = new FileOutputStream(pdfDoc);
//        return fileOutputStream;
//    }

    //Temporary file creation method
    private  static FileOutputStream getSimpleFileDirectory() throws FileNotFoundException {
        Date date = new Date(System.currentTimeMillis());
        StringBuilder stringBuilder = new StringBuilder("Data_");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-'at'-HH-mm-ss");
        stringBuilder.append(dateFormat.format(date));
        stringBuilder.append(".pdf");
        return new FileOutputStream(stringBuilder.toString());
    }

    //Simple filling
    private  static void fillDocument(Document document, String data[]) throws DocumentException {
        document.open();
        document.add(createNewAnchor("BackToTop","BackToTop"));
        Font font = FontFactory.getFont(FontFactory.COURIER, 14, Font.BOLD,	new CMYKColor(255, 255, 255, 255));
        for (String nextStringData:data){
            document.add(createNewParagraph(nextStringData,font,50,50));
        }
    }

    //Simple Anchor Creating
    private  static Anchor createNewAnchor(String anchorName, String anchorData){
        Anchor anchorTarget = new Anchor(anchorData);
        anchorTarget.setName(anchorName);
        return anchorTarget;
    }

    //Simple Paragraph creating
    private  static Paragraph createNewParagraph(String paragraphData, Font font, int spacingBefore, int spacingAfter){
        Paragraph paragraph = new Paragraph(paragraphData,font);
        paragraph.setSpacingBefore(spacingBefore);
        paragraph.setSpacingAfter(spacingAfter);
        return paragraph;
    }
}
