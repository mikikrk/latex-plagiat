package pl.edu.agh.kiro.latex_plagiat.commons.util;

import com.snowtide.PDF;
import com.snowtide.pdf.Document;
import com.snowtide.pdf.OutputTarget;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

public class PdfUtils {
    public static String getText(File pdfFile) throws IOException {
        try (Document pdf = PDF.open(pdfFile)) {
            StringWriter buffer = new StringWriter();
            pdf.pipe(new OutputTarget(buffer));
            return buffer.toString();
        }
    }
}
