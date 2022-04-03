package app.receive_emails.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Email {
  @Id
  String id;

  String subject;
  String body;

}
