package controllers;

import play.*;
import play.mvc.*;
import jobs.*;

public class Tasks extends CRUD {

  @Before(only={"blank"})
  static void initialize(String type) {
    params.put("object.type", type);
  }

  @After(only={"create", "save"})
  static void execute(String type) {
    new ImportBacklog().now();
  }

}
