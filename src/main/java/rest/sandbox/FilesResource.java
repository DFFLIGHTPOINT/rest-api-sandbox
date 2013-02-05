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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/files")
public class FilesResource {
  // the resource JSON representation will be serialized by Jackson JAX-RS provider,
  // while the XML will still be generated by JAXB


  @Path("/array{count:(/\\d*)?}")
  @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
  public Bean[] getArray(@PathParam("count") String count) {
    List<Bean> list = createList(count);
    return list.toArray(new Bean[list.size()]);
  }

  private List<Bean> createList(String count) {
    if (count.startsWith("/")) {
      count = count.substring(1);
    }
    int i;
    try {
      i = Integer.parseInt(count);
    } catch (NumberFormatException e) {
      i = 0;
    }
    ArrayList<Bean> list = new ArrayList<Bean>(i);
    for (int j = 0; j < i; j++) {
      list.add(new Bean(String.valueOf(i)));
    }
    return list;
  }

  @Path("/list{count:(/\\d*)?}")
  @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
  @GET
  public List<Bean> getList(@PathParam("count") String count) {
    return createList(count);
  }

  @Path("/wrapper{count:(/\\d*)?}")
  @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
  @GET
  public Beans getWrapper(@PathParam("count") String count) {
    Beans ret = new Beans();
    ret.files = createList(count);
    return ret;
  }
}
