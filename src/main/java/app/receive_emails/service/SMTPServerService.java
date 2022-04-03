package app.receive_emails.service;

import app.receive_emails.model.EmailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.subethamail.smtp.helper.SimpleMessageListenerAdapter;
import org.subethamail.smtp.server.SMTPServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
@Slf4j
public class SMTPServerService {

    @Value("${smtpserver.enabled}")
    boolean enabled = false;

    @Value("${smtpserver.hostName}")
    String hostName = "";

    @Value("${smtpserver.port}")
    String port = "";

    @Autowired
    EmailRepository emailRepository;

    SMTPServer smtpServer;


    @PostConstruct
    public void start() {
        if (enabled) {
            SimpleMessageListenerImpl l = new SimpleMessageListenerImpl(emailRepository);
            smtpServer = new SMTPServer(new SimpleMessageListenerAdapter(l));

            smtpServer.setHostName(this.hostName);
            smtpServer.setPort(Integer.parseInt(port));
            smtpServer.setConnectionTimeout(1000 * 60 * 10);
            smtpServer.start();

            log.info("****** SMTP Server is running for domain " + smtpServer.getHostName() + " on port " + smtpServer.getPort());
        } else {
            log.info("****** SMTP Server NOT ENABLED by settings ");
        }

    }

    @PreDestroy
    public void stop() {
        if (enabled) {
            log.info("****** Stopping SMTP Server for domain " + smtpServer.getHostName() + " on port " + smtpServer.getPort());
            smtpServer.stop();
        }

    }


    public boolean isRunning() {
        return smtpServer.isRunning();
    }
}
