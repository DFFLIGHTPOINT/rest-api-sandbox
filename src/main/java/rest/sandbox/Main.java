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

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.ApplicationAdapter;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.core.util.FeaturesAndProperties;
import org.glassfish.grizzly.http.server.HttpServer;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;

/**
 * @author Vladislav.Rassokhin
 */
public class Main {
  public static final URI BASE_URI = UriBuilder.fromUri("http://localhost:9998/").build();

  public static HttpServer startServer() throws IOException {
    final ApplicationAdapter rc = new ApplicationAdapter(new SandboxApplication());
    rc.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
    rc.getFeatures().put(FeaturesAndProperties.FEATURE_XMLROOTELEMENT_PROCESSING, Boolean.TRUE);
    rc.getFeatures().put(FeaturesAndProperties.FEATURE_FORMATTED, Boolean.TRUE);
    return GrizzlyServerFactory.createHttpServer(BASE_URI, rc);
  }

  public static void main(String[] args) throws Exception {
    System.out.println("Starting grizzly...");
    HttpServer httpServer = startServer();
    System.out.println(String.format("Jersey app started with WADL available at %sapplication.wadl\n", BASE_URI));
    System.out.println("Hit enter to stop...");
    System.in.read();
    httpServer.stop();
  }
}
