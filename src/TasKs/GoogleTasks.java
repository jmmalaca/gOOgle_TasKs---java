package TasKs;

import java.util.*;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import TasKs.builder.*;
import TasKs.builder.api.*;
import TasKs.model.*;
import TasKs.oauth.*;

public class GoogleTasks{
	
	private static final String NETWORK_NAME = "Google";
	private static final String AUTHORIZE_URL = "https://www.google.com/accounts/OAuthAuthorizeToken?oauth_token=";
	private static final String SCOPE = "https://www.googleapis.com/auth/tasks";
	
	private static String apiKey = "826588851215.apps.googleusercontent.com";
	private static String apiSecret = "3IyguGocDL_4TGshw6xmsUFW";
	
	private static Token accessToken = null;
	private static OAuthService service = null;
	private static Properties prop = null;
	
	private static void Connect(){
		Scanner in = new Scanner(System.in);
		  
		System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===\n");
	
		//Obtain the Request Token
		System.out.println("Fetching the Request Token...");
		Token requestToken = service.getRequestToken();
		System.out.println("Got the Request Token!");
		System.out.println("(if your curious it looks like this:\n - " + requestToken + " )");
	    	
		System.out.println("\nNow go and authorize Scribe here:");
		System.out.println(AUTHORIZE_URL + requestToken.getToken());
		  
		System.out.println("And paste the verifier here");
		System.out.print(">>");
		Verifier verifier = new Verifier(in.nextLine());
		System.out.println();
	    
		// Trade the Request Token and Verfier for the Access Token
		System.out.println("Trading the Request Token for an Access Token...");
		accessToken = service.getAccessToken(requestToken, verifier);
		System.out.println("Got the Access Token!");
		System.out.println("(if your curious it looks like this:\n -" + accessToken + " )");
		
		/*
		try {
			prop.store(new FileOutputStream("appProperties"), null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	private static void getTasksLists(){
		// Now let's go and ask for a protected resource!
		System.out.println("\nGET TASKS LISTS");
		String url2ListTasks = "https://www.googleapis.com/tasks/v1/users/@me/lists";
		OAuthRequest request = new OAuthRequest(Verb.GET, url2ListTasks);
		service.signRequest(accessToken, request);
		request.addHeader("GData-Version", "3.0");
		  
		Response response = request.send();
		System.out.println("Lista de Listas de Tarefas Existentes");
		System.out.println("-response Code:");
		System.out.println(response.getCode());
		System.out.println("-response Body:");
		System.out.println(response.getBody());
	}
	
	private static void getListOfTasks_fromTasKList(){
		// Now let's go and ask for a protected resource!
		System.out.println("\nGET TASKS FROM LIST");
		System.out.println("And paste the List_TaskID here");
		System.out.print(">>");
		String listsTasks = leituras.readString();
		
		String url2Tasks = "https://www.googleapis.com/tasks/v1/lists/"+listsTasks+"/tasks";
		
		OAuthRequest request = new OAuthRequest(Verb.GET, url2Tasks);
		service.signRequest(accessToken, request);
		request.addHeader("GData-Version", "3.0");
		  
		Response response = request.send();
		System.out.println("\nLista das tarefas da Lista de Tarefas com esse ID: ");
		System.out.println("-response Code:");
		System.out.println(response.getCode());
		System.out.println("-response Body:");
		System.out.println(response.getBody());
	}
	
	@SuppressWarnings("unchecked")
	private static void addNewTask2List(){
		// Now let's go and ask for a protected resource!
		System.out.println("\nADD NEW TASK TO LIST");
		System.out.println("And paste the List_TaskID here");
		System.out.print(">>");
		String listsTasks = leituras.readString();
		
		String url2Tasks = "https://www.googleapis.com/tasks/v1/lists/"+listsTasks+"/tasks";
		OAuthRequest request = new OAuthRequest(Verb.POST, url2Tasks);
		service.signRequest(accessToken, request);
		request.addHeader("GData-Version", "3.0");
		request.addHeader("Content-Type", "application/json");
		
		System.out.println("Escreve aqui o titulo para a nova Task:");
		System.out.print(">>");
		String titulo = leituras.readString();
		System.out.println("Escreve aqui a descrisao para a nova Task:");
		System.out.print(">>");
		String descrisao = leituras.readString();
		
		//adicionar conteudo:
		//title: "New Task",
		//notes: "Please complete me",
		//due: "2010-10-15T12:00:00.000Z"
		JSONObject obj = new JSONObject();
		obj.put("title", titulo);
		obj.put("notes", descrisao);
		//DEBUG
		//System.out.println("JSON STRING criada: \n"+obj.toJSONString());
		//payload e n add2body... not form-encoded mas sim... raw-coded 
		request.addPayload(obj.toJSONString());
		
		Response response = request.send();
		System.out.println("\nResposta de Criar uma nova Task com o nome:"+titulo);
		System.out.println("-response Code:");
		System.out.println(response.getCode());
		System.out.println("-response Body:");
		System.out.println(response.getBody());
	}
	
	private static void deleteTask2List(){
		// Now let's go and ask for a protected resource!
		System.out.println("\nDELETE TASK FROM LIST");
		System.out.println("And paste the List_TaskID here");
		System.out.print(">>");
		String listsTasks = leituras.readString();
		System.out.println("Escreve aqui o ID da Task a apagar da lista");
		System.out.print(">>");
		String ID = leituras.readString();
		
		String url2Tasks = "https://www.googleapis.com/tasks/v1/lists/"+listsTasks+"/tasks/"+ID;
		OAuthRequest request = new OAuthRequest(Verb.DELETE, url2Tasks);
		service.signRequest(accessToken, request);
		request.addHeader("GData-Version", "3.0");
		
		Response response = request.send();
		System.out.println("\nResposta de Apagar a Task com o ID: "+ID);
		System.out.println("-response Code:");
		System.out.println(response.getCode());
		System.out.println("-response Body:");
		System.out.println(response.getBody());
		//se apagou, resposta: HTTP/1.1 204 No Content
	}
	
	@SuppressWarnings("unchecked")
	private static void updateTask2List(){
		// Now let's go and ask for a protected resource!
		System.out.println("\nUPDATE TASK TO LIST");
		System.out.println("And paste the List_TaskID here");
		System.out.print(">>");
		String listsTasks = leituras.readString();
		System.out.println("Escreve aqui o ID da Task a actualizar da lista");
		System.out.print(">>");
		String ID = leituras.readString();
		
		//Receber a TASK em questao para o UPDATE
		String url2Tasks = "https://www.googleapis.com/tasks/v1/lists/"+listsTasks+"/tasks/"+ID;
		OAuthRequest request = new OAuthRequest(Verb.GET, url2Tasks);
		service.signRequest(accessToken, request);
		request.addHeader("GData-Version", "3.0");
		
		Response response = request.send();
		/*System.out.println("\nResposta de obter uma Task com o ID:"+ID);
		System.out.println("-response Code:");
		System.out.println(response.getCode());
		System.out.println("-response Body:");
		System.out.println(response.getBody());*/
		
		//parse da resposta para um objecto JSON
		JSONObject obj = (JSONObject) JSONValue.parse(response.getBody());
		System.out.println("JSON: "+obj.toJSONString());
		
		//actualizar os dados
		System.out.println("Escreve aqui o titulo para actualizar a Task:");
		System.out.print(">>");
		String titulo = leituras.readString();
		obj.put("title", titulo);
		
		System.out.println("Escreve aqui a descrisao para actualizar a Task:");
		System.out.print(">>");
		String descrisao = leituras.readString();
		obj.put("notes", descrisao);
		
		System.out.println("Escreve aqui o status(completed) para actualizar a Task(yes or no):");
		System.out.print(">>");
		String status = leituras.readString();
		if(status.equals("yes") == true){
			obj.put("status", "completed");
		}else{
			obj.put("status", "needsAction");
			obj.put("completed", null);
		}
		//DEBUG
		System.out.println("JSON: "+obj.toJSONString());
	
		url2Tasks = "https://www.googleapis.com/tasks/v1/lists/"+listsTasks+"/tasks/"+ID;
		request = new OAuthRequest(Verb.PUT, url2Tasks);
		service.signRequest(accessToken, request);
		request.addHeader("GData-Version", "3.0");
		request.addHeader("Content-Type", "application/json");
		
		//payload e n add2body... not form-encoded mas sim... raw-coded
		request.addPayload(obj.toJSONString());
		
		response = request.send();
		System.out.println("\nResposta de actualizar uma Task com o ID:"+ID);
		System.out.println("-response Code:");
		System.out.println(response.getCode());
		System.out.println("-response Body:");
		System.out.println(response.getBody());
	}
	
	public static void main(String[] args){
		
		prop = new Properties();
		
		/*
    	try {
    		//set the properties value
    		prop.setProperty("apiKey", "client ID on google console");
    		prop.setProperty("apiSecret", "cliente secret");
 
    		//save properties to project root folder
    		prop.store(new FileOutputStream("appProperties"), null);
 
    	} catch (IOException ex) {
    		ex.printStackTrace();
        }
		
    	//load a properties file
		try {
			prop.load(new FileInputStream("appProperties"));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		service = new ServiceBuilder()
	  		.provider(GoogleApi.class)
	  		.apiKey(apiKey)
	  		.apiSecret(apiSecret)
	  		.scope(SCOPE)
	  		.build();
	  
		//connect to googleTasks
		Connect();
		
		//START using the connection with googleTasks:
		
		//ir buscar a lista das Listas de Tarefas
		getTasksLists();
		
		//ir buscar as tarefas de uma das Listas de Tarefas
		getListOfTasks_fromTasKList();
		
		//add nova Task a essa Lista de Tarefas
		//addNewTask2List();
		
		//delete uma Task dessa Lista de Tarefas
		//deleteTask2List();
		
		//update uma Task dessa Lista de Tarefas
		//updateTask2List();
		
  }
}
