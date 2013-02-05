/*
 * Copyright 2000-2013 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package rest.sandbox;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.core.util.FeaturesAndProperties;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import com.sun.jersey.test.framework.spi.container.TestContainerException;
import org.testng.annotations.*;

import javax.ws.rs.core.MediaType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Vladislav.Rassokhin
 */
public class FilesResourceTest {

  public static final String FILES_ARRAY = "/files/array/";
  public static final String FILES_LIST = "/files/list/";
  public static final String FILES_WRAPPER = "/files/wrapper/";
  private final JerseyTest myJerseyTest;

  public FilesResourceTest() {
    myJerseyTest = new JerseyTest(new WebAppDescriptor.Builder("javax.ws.rs.Application", SandboxApplication.class.getName())
        .initParam(JSONConfiguration.FEATURE_POJO_MAPPING, "true")
        .initParam(FeaturesAndProperties.FEATURE_XMLROOTELEMENT_PROCESSING, "true")
//        .initParam(FeaturesAndProperties.FEATURE_FORMATTED, "true")
        .build()) {
    };
  }

  @BeforeClass
  public void setUp() throws Exception {
    myJerseyTest.setUp();
  }

  @AfterClass
  public void tearDown() throws Exception {
    myJerseyTest.tearDown();
  }

  public WebResource resource() {
    return myJerseyTest.resource();
  }

  public Client client() {
    return myJerseyTest.client();
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
