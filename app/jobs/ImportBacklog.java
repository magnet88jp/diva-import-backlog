package jobs;

import java.util.*;
import play.*;
import play.jobs.*;
import play.libs.WS;
import play.libs.WS.*;
import play.libs.XPath;
import play.templates.*;
import org.w3c.dom.*;
import models.*;

public class ImportBacklog extends Job {

  enum Issue {
    key, url, summary, description, updated_on, UNKNOWN;
    public static Issue getValue(String value) {
      try {
        return valueOf(value);
      } catch (Exception e) {
        return UNKNOWN;
      }
    }
    public static int size() { return UNKNOWN.ordinal(); }
  }

  public void doJob() {
    String authorizeUrl = Play.configuration.get("local.backlog.authorizeUrl").toString();
    String clientId = Play.configuration.get("local.backlog.clientId").toString();
    String clientSecret = Play.configuration.get("local.backlog.clientSecret").toString();

    WSRequest wsrequest = WS.url(authorizeUrl).authenticate(clientId, clientSecret);

    Template template = TemplateLoader.load("conf/templates/backlog.findIssue.xml");
    Map<String, Object> map = new HashMap<String, Object>(16);
    map.put("projectId", "5816");
    map.put("limit", "100");
    map.put("offset", "2");

    wsrequest.body = template.render(map);

    Document document = wsrequest.post().getXml();

    Map<String, String> data = new HashMap<String, String>(16);
    for(Node node: XPath.selectNodes("//data/value/struct/member", document)) {
      String name = XPath.selectText("name", node);
      String value = XPath.selectText("value", node);

      switch(Issue.getValue(name)) {
      case key:
      case url:
      case summary:
      case description:
      case updated_on:
        data.put(name, value);
        break;
      default:
        break;
      }
      if(data.size() == Issue.size()) {
        // create Inquiry
        Inquiry inquiry = null;
        List<Inquiry> inquirys = Inquiry.find("byCode", data.get("kay")).fetch();
        if(inquirys.size() == 0) {
          inquiry = new Inquiry(data.get("key"), data.get("summary"), data.get("description"), "");
        } else {
          inquiry = inquirys.get(0);
        }
        inquiry._save();
 
        data.clear();
      }
    }
  }
}
