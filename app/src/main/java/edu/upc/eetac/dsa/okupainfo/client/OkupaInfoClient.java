package edu.upc.eetac.dsa.okupainfo.client;

import android.util.Log;

import com.google.gson.Gson;

import org.glassfish.jersey.client.ClientConfig;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.upc.eetac.dsa.okupainfo.client.entity.AuthToken;
import edu.upc.eetac.dsa.okupainfo.client.entity.Casal;
import edu.upc.eetac.dsa.okupainfo.client.entity.Link;
import edu.upc.eetac.dsa.okupainfo.client.entity.Root;

/**
 * Created by Guillermo on 21/05/2016.
 */
public class OkupaInfoClient {
    private final static String BASE_URI = "http://192.168.1.12:8080/okupainfo";
    private static OkupaInfoClient instance;
    private AuthToken authToken = null;
    private Casal casal = null;
    private Root root;
    private ClientConfig clientConfig = null;
    private final static String TAG = OkupaInfoClient.class.toString();
    private Client client = null;

    private OkupaInfoClient() {
        clientConfig = new ClientConfig();
        client = ClientBuilder.newClient(clientConfig);
        loadRoot();
    }

    public static OkupaInfoClient getInstance() {
        if (instance == null)
            instance = new OkupaInfoClient();
        return instance;
    }

    private void loadRoot() {
        WebTarget target = client.target(BASE_URI);
        Response response = target.request().get();

        String json = response.readEntity(String.class);
        root = (new Gson()).fromJson(json, Root.class);
    }

    public Root getRoot() {
        return root;
    }

    public final static Link getLink(List<Link> links, String rel){
        for(Link link : links){
            if(link.getRels().contains(rel)) {
                return link;
            }
        }
        return null;
    }

    public boolean login(String userid, String password) {
        String loginUri = getLink(root.getLinks(), "login").getUri().toString();
        WebTarget target = client.target(loginUri);
        Form form = new Form();
        form.param("login", "spongebob");
        form.param("password", "1234");
        String json = target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
        authToken = (new Gson()).fromJson(json, AuthToken.class);
        Log.d(TAG, json);
        return true;
    }

    public String getCasal(String uri) throws OkupaInfoClientException {
        WebTarget target = client.target(uri);
        Response response = target.request().get();
        if (response.getStatus() == Response.Status.OK.getStatusCode())
            return response.readEntity(String.class);
        else
            throw new OkupaInfoClientException(response.readEntity(String.class));
    }

    public String getCasals(String uri) throws OkupaInfoClientException {
        if(uri==null){
            uri = getLink(authToken.getLinks(), "current-casals").getUri().toString();
        }
        WebTarget target = client.target(uri);
        Response response = target.request().get();
        if (response.getStatus() == Response.Status.OK.getStatusCode())
            return response.readEntity(String.class);
        else
            throw new OkupaInfoClientException(response.readEntity(String.class));
    }

    public boolean CreateCasal(Form form) throws OkupaInfoClientException {

        String token = authToken.getToken();
        String uri = getLink(authToken.getLinks(), "create-casal").getUri().toString();
        WebTarget target = client.target(uri);
        Invocation.Builder builder = target.request().header("X-Auth-Token", token);
        Response response = builder.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
        if (response.getStatus() == Response.Status.CREATED.getStatusCode())
            return true;
        else
            return false;


    }
}
