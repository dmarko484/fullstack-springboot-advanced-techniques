package app.pdf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Service
public class ThymeleafTemplateEngine {

  @Autowired
  SpringTemplateEngine springTemplateEngine;


  public Context createContext() {
    return new Context();
  }

  public String renderTemplate(String templatePath, Context context) {
    return springTemplateEngine.process(templatePath, context);
  }

}
