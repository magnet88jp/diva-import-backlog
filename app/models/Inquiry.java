package models;

import javax.persistence.*;
import play.db.jpa.Model;
import play.data.validation.*;

@Entity
public class Inquiry extends Model {
  public String code;
  public String subject;
  @Lob
  @MaxSize(4000)
  public String question;
  @Lob
  @MaxSize(4000)
  public String answer;

  public Inquiry(String code, String subject, String question, String answer) {
    this.code = code;
    this.subject = subject;
    this.question = question;
    this.answer = answer;
  }

  public String toString() {
    return subject;
  }
}
