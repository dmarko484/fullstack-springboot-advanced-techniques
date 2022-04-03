package app.receive_emails.model;

import org.springframework.data.repository.CrudRepository;

public interface EmailRepository extends CrudRepository<Email, String> {
}
