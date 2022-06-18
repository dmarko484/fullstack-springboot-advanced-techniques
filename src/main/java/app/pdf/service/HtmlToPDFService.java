package app.pdf.service;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.net.URL;

public class HtmlToPDFService {

  public static ByteArrayOutputStream createPdfIText(String html, boolean secured) {
    ITextRenderer renderer = new ITextRenderer();

    URL fontResourceURL = HtmlToPDFService.class.getResource("/fonts/LiberationSans.ttf");
    URL fontResourceURL2 = HtmlToPDFService.class.getResource("/fonts/LiberationSans-Bold.ttf");

    try {
      renderer.getFontResolver().addFont(fontResourceURL.getPath(),
              BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

      renderer.getFontResolver().addFont(fontResourceURL2.getPath(),
              BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    } catch (DocumentException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    renderer.setDocumentFromString(html);
    renderer.layout();

    ByteArrayOutputStream fs = new ByteArrayOutputStream();
    try {
      renderer.createPDF(fs);
      fs.close();
      PdfReader reader = new PdfReader(fs.toByteArray());

      ByteArrayOutputStream out = new ByteArrayOutputStream();
      PdfStamper stamper = new PdfStamper(reader, out);

      if (secured == true) {
        stamper.setEncryption(null, null, PdfWriter.ALLOW_PRINTING, PdfWriter.STANDARD_ENCRYPTION_128);
      } else {
        stamper.setEncryption(null, null, PdfWriter.ALLOW_PRINTING | PdfWriter.ALLOW_MODIFY_CONTENTS, PdfWriter.STANDARD_ENCRYPTION_128);
      }
      stamper.close();

      return out;


    } catch (DocumentException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return fs;
  }

}