package edu.uci.ics.perpetual.api.utilities;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

public class Authentication {
    public static Response authenticate (HttpHeaders headers) {

        /*
        String uri = "http://128.195.53.189:5000";
        String path = "/api/2/me";

        Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);
        WebTarget webTarget = client.target(uri).path(path);
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, headers.getHeaderString("Authorization"));

        Response response = invocationBuilder.get();
        if (response.getStatus() != 200)
            return response;

        //*/
        return null;
    }
}
