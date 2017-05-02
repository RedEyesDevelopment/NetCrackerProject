package tests.pdf;

import org.junit.Test;
import projectpackage.support.pdf.PdfTool;
import tests.AbstractTest;

/**
 * Created by Gvozd on 09.04.2017.
 */
public class PDFTest extends AbstractTest {

    @Test
    public void testPDFCreation(){
        PdfTool.createPDF(new String[]{"FUCK YOU!","FUCK YOU!","FUCK YOU!","FUCK YOU!","FUCK YOU!","FUCK YOU!"});
    }
}
