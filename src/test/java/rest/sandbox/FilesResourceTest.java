package rest.sandbox;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.core.util.FeaturesAndProperties;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import com.sun.jersey.test.framework.spi.container.TestContainerException;
import org.junit.Test;

import javax.ws.rs.core.MediaType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Vladislav.Rassokhin
 */
public class FilesResourceTest extends JerseyTest {

  public static final String FILES_ARRAY = "/files/array/";
  public static final String FILES_LIST = "/files/list/";
  public static final String FILES_WRAPPER = "/files/wrapper/";

  public FilesResourceTest() throws TestContainerException {
    super(new WebAppDescriptor.Builder("javax.ws.rs.Application", SandboxApplication.class.getName())
        .initParam(JSONConfiguration.FEATURE_POJO_MAPPING, "true")
        .initParam(FeaturesAndProperties.FEATURE_XMLROOTELEMENT_PROCESSING, "true")
//        .initParam(FeaturesAndProperties.FEATURE_FORMATTED, "true")
        .build());
  }

  //  @Test
  public void testArray() throws Exception {
    runTestArrayJson(0);
    runTestArrayJson(1);
    runTestArrayJson(5);
  }

  private void runTestArrayJson(int count) {
    final WebResource r = resource();
    final String msg = r.path("/files/array/" + count).accept(MediaType.APPLICATION_JSON).get(String.class);
//    System.out.println("msg = " + msg);
    JsonParser parser = new JsonParser();
    JsonElement element = parser.parse(msg);
    assertTrue(element instanceof JsonArray);
    JsonArray array = (JsonArray) element;
    assertEquals(array.size(), count);
    for (JsonElement e : array) {
      assertTrue(e instanceof JsonObject);
      JsonObject o = (JsonObject) e;
      assertTrue(o.has("name"));
    }
  }

  @Test
  public void testPrintArray() throws Exception {
    System.out.println("ARRAY");
    doPrint(FILES_ARRAY + 0);
    doPrint(FILES_ARRAY + 1);
    doPrint(FILES_ARRAY + 2);
  }

  @Test
  public void testPrintList() throws Exception {
    System.out.println("LIST");
    doPrint(FILES_LIST + 0);
    doPrint(FILES_LIST + 1);
    doPrint(FILES_LIST + 2);
  }

  @Test
  public void testPrintWrapper() throws Exception {
    System.out.println("WRAPPER");
    doPrint(FILES_WRAPPER + 0);
    doPrint(FILES_WRAPPER + 1);
    doPrint(FILES_WRAPPER + 2);
  }

  private void doPrint(String path) {
    final WebResource r = resource();
    System.out.println("===xml===\n" + r.path(path).accept(MediaType.APPLICATION_XML).get(String.class));
    System.out.println("===json===\n" + r.path(path).accept(MediaType.APPLICATION_JSON).get(String.class));
  }

//  @Test
  public void testArrayAndListSame() throws Exception {
    assertEquals(getAsString(FILES_ARRAY + 0, MediaType.APPLICATION_XML_TYPE), getAsString(FILES_LIST + 0, MediaType.APPLICATION_XML_TYPE));
    assertEquals(getAsString(FILES_ARRAY + 1, MediaType.APPLICATION_XML_TYPE), getAsString(FILES_LIST + 1, MediaType.APPLICATION_XML_TYPE));
    assertEquals(getAsString(FILES_ARRAY + 2, MediaType.APPLICATION_XML_TYPE), getAsString(FILES_LIST + 2, MediaType.APPLICATION_XML_TYPE));
//    assertEquals(getAsString(FILES_WRAPPER + 0, MediaType.APPLICATION_XML_TYPE), getAsString(FILES_LIST + 0, MediaType.APPLICATION_XML_TYPE));
    assertEquals(getAsString(FILES_WRAPPER + 1, MediaType.APPLICATION_XML_TYPE), getAsString(FILES_LIST + 1, MediaType.APPLICATION_XML_TYPE));
    assertEquals(getAsString(FILES_WRAPPER + 2, MediaType.APPLICATION_XML_TYPE), getAsString(FILES_LIST + 2, MediaType.APPLICATION_XML_TYPE));
    assertEquals(getAsString(FILES_ARRAY + 0, MediaType.APPLICATION_JSON_TYPE), getAsString(FILES_LIST + 0, MediaType.APPLICATION_JSON_TYPE));
    assertEquals(getAsString(FILES_ARRAY + 1, MediaType.APPLICATION_JSON_TYPE), getAsString(FILES_LIST + 1, MediaType.APPLICATION_JSON_TYPE));
    assertEquals(getAsString(FILES_ARRAY + 2, MediaType.APPLICATION_JSON_TYPE), getAsString(FILES_LIST + 2, MediaType.APPLICATION_JSON_TYPE));
  }

  private String getAsString(String path, MediaType type) {
    final WebResource r = resource();
    return r.path(path).accept(type).get(String.class);
  }
}
