package models;

import javax.persistence.Entity;
import play.db.jpa.Model;

@Entity
public class Task extends Model {
  public String type;

  public Task(String type) {
    this.type = type;
  }
}
