package com.dgr.rat.wsclient;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.ws.rs.client.AsyncInvoker;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.jersey.client.ClientConfig;

import com.dgr.rat.login.json.ChooseDomainResponse;
import com.dgr.rat.login.json.LoginResponse;
import com.dgr.rat.login.json.LoginResponseDeserializer;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class LoginHelper {
	private Client _client = null;
	private String _wsURL = null;
	
	public LoginHelper(String wsURL) {
		_client = ClientBuilder.newClient(new ClientConfig());
		_wsURL = wsURL;
	}
	
    private URI getBaseURI(String uri) {
        return UriBuilder.fromUri(uri).build();
    }
    
    public LoginResponse login(String userName, String password) throws JsonParseException, JsonMappingException, IOException{
    	String json = "{\"userName\":\"" + userName + "\",\"password\":\"" + password + "\"}";
    	WebTarget target = _client.target(this.getBaseURI(_wsURL));
    	
    	Response response = target.path("login").request().post(Entity.entity(json, MediaType.TEXT_PLAIN_TYPE));
    	json = response.readEntity(String.class);
    	System.out.println("Login response: " + json);
    	
    	LoginResponseDeserializer loginResponseDeserializer = new LoginResponseDeserializer();
    	// TODO: questi deserializer sono da ridurre ad uno solo generico
    	LoginResponse loginResponse = loginResponseDeserializer.deserialize(LoginResponse.class, json);
    	
    	return loginResponse;
    }
    
    public ChooseDomainResponse chooseDomain(LoginResponse loginResponse, String domain) throws InterruptedException, ExecutionException, JsonParseException, JsonMappingException, IOException{
    	String userName = loginResponse.getUserName();
    	String json = "{\"sessionID\":\"" + loginResponse.get_sessionID() + "\",\"userName\":\"" + userName + "\",\"domainName\":\"" + domain + "\"}";
    	WebTarget target = _client.target(this.getBaseURI(_wsURL + "/choosedomain?sessionid=" +  loginResponse.get_sessionID()));
    	Response response = target.request().post(Entity.entity(json, MediaType.TEXT_PLAIN_TYPE));
    	json = response.readEntity(String.class);
    	System.out.println("ChooseDomain response: " + json);
    	
    	LoginResponseDeserializer loginResponseDeserializer = new LoginResponseDeserializer();
    	// TODO: questi deserializer sono da ridurre ad uno solo generico
    	ChooseDomainResponse chooseDomainResponse = loginResponseDeserializer.deserialize(ChooseDomainResponse.class, json);
    	
    	return chooseDomainResponse;
    }
    
    public void createDomain(ChooseDomainResponse chooseDomainResponse, String newDomain, String roleName) throws InterruptedException, ExecutionException, TimeoutException{
    	String sessionID = chooseDomainResponse.get_sessionID();
    	String domainName = chooseDomainResponse.getDomainName();
    	String json = this.readFile("/home/dgr/dev/RATPlatform/RAT/TestData/Commands/DomainConfiguration.conf");
    	json = json.replace("@domainPlaceholder@", newDomain);
    	
    	WebTarget target = _client.target(this.getBaseURI(_wsURL + "/createcollaborationdomain?sessionid=" +  sessionID) + "&roleName=" + roleName + "&domainName=" + domainName);
    	final AsyncInvoker asyncInvoker = target.request().async();
    	final Future<Response> responseFuture = asyncInvoker.post(Entity.entity(json, MediaType.TEXT_PLAIN_TYPE));

    	System.out.println("Request is being processed asynchronously.");
    	final Response response = responseFuture.get(100000, TimeUnit.MILLISECONDS);
    	System.out.println("Response received from RATWSServerClient: " + response.readEntity(String.class));
    }
    
	private String readFile(String path) {
		BufferedReader br = null;
		String result = "";
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(path));
			while ((sCurrentLine = br.readLine()) != null) {
				result += sCurrentLine;
			}
 
		} 
		catch (IOException e) {
			e.printStackTrace();
		} 
		finally {
			try {
				if (br != null)br.close();
			} 
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		return result;
	}
}
