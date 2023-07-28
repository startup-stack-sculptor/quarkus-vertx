/*
 * 
 * 
 */
package org.acme;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.Vertx;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.client.WebClient;
import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author pm
 */
@Path("/vertex")
public class VertexResource {
    
    private final Vertx vertex;
    private final WebClient client;
    
    private static final String URL = "https://en.wikipedia.org/w/api.php?action=parse&page=Quarkus&format=json&prop=langlinks";
            
    @Inject
    public VertexResource(Vertx vertex) {
        this.vertex = vertex;
        this.client = WebClient.create(vertex);
    }
    
    @Inject
    EventBus bus;
    
    
    
    @GET
    @Path("/lorem")
    public Uni<String> readShortFile() {
        return vertex.fileSystem().readFile("lorem.txt")
                .onItem().transform(content -> content.toString(StandardCharsets.UTF_8));
    }
    
    @GET
    @Path("/book")
    public Multi<String> readLargeFile() {
        return vertex.fileSystem().open("book.txt", new OpenOptions().setRead(true)
        )
                .onItem().transformToMulti(file -> file.toMulti())
                .onItem().transform(content -> content.toString(StandardCharsets.UTF_8)
                           + "\n-----------%%%%%%%%%%%---\n");
    }
    
    @GET
    @Path("/hello")
    public Uni<String> hello(@QueryParam("name") String name) {
        return bus.<String>request("greetings", name)
                .onItem().transform(response -> response.body());
    }
    
    @GET
    @Path("/web")
    public Uni<JsonArray> retrieveDataFromWikipedia() {
        return client.getAbs(URL).send()
                .onItem().transform(json -> json.getJsonObject("parse")
                              .getJsonArray("langlinks"));
    }
}
