package app.emailer.service;

import app.emailer.model.Email;
import app.emailer.model.EmailRepository;
import jodd.mail.ReceivedEmail;
import lombok.extern.slf4j.Slf4j;
import org.subethamail.smtp.TooMuchDataException;
import org.subethamail.smtp.helper.SimpleMessageListener;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


@Slf4j
public class SimpleMessageListenerImpl implements SimpleMessageListener {

  EmailRepository emailRepository;

  public SimpleMessageListenerImpl(EmailRepository emailRepository) {
    this.emailRepository = emailRepository;
  }

  @Override
  public boolean accept(String from, String recipient) {
    return true;
  }

  /**
   *
   */
  @Override
  public void deliver(String from, String recipient, InputStream data) throws TooMuchDataException, IOException {
    log.info("+++ Email message delivered for recipient: {} from {}", recipient, from);

    Properties prop = System.getProperties();
    prop.setProperty("mail.mime.base64.ignoreerrors", "true");
    Session session = Session.getDefaultInstance(prop);

    MimeMessage m = null;
    try {
      m = new MimeMessage(session, data);
      ReceivedEmail email = new ReceivedEmail(m, false, null);

      Email e = new Email();
      e.setSubject(email.subject());
      e.setBody(email.messages().get(0).getContent());
      emailRepository.save(e);

    } catch (MessagingException e) {
      e.printStackTrace();
    }


  }
}