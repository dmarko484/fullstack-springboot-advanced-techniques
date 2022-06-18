package app.pdf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class PdfGeneratorService {
  @Autowired
  SpringTemplateEngine springTemplateEngine;

  public ByteArrayOutputStream createPdf(String title) {
    return HtmlToPDFService.createPdfIText(renderHtmlForPDF(title),false);
  }

  private String renderHtmlForPDF(String title) {
    Context context = new Context();
    context.setVariable("title", title);
    context.setVariable("image1", ImageUtils.convertImageToBase64("pdf/images/image1.png"));
    context.setVariable("barcode1",BarCodeService.getBarCodeAsBase64("Invoice-12354287"));

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    context.setVariable("today", dateFormat.format(new Date()));

    return springTemplateEngine.process("pdf/confirmation.html", context);
  }

}
