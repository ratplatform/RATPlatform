package com.rat.init;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.xbean.spring.context.FileSystemXmlApplicationContext;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.commons.mqmessages.MQMessage;
import com.dgr.rat.commons.mqmessages.RATJSONMessage;
import com.dgr.rat.commons.utils.RATUtils;
import com.dgr.rat.graphgenerator.JSONObjectBuilder;
import com.dgr.rat.json.command.parameters.SystemInitializerTestHelpers;
import com.dgr.rat.json.factory.CommandSink;
import com.dgr.rat.json.factory.Response;
import com.dgr.rat.json.utils.RATJsonUtils;
import com.dgr.rat.json.utils.ReturnType;
import com.dgr.rat.json.utils.VertexType;
import com.dgr.rat.tests.DBManager;
import com.dgr.rat.tests.RATSessionManager;
import com.dgr.utils.AppProperties;
import com.dgr.utils.Utils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinkerpop.blueprints.Vertex;

public class InitDB {
	private FileSystemXmlApplicationContext _context = null;
	private DBManager _dbManager = null;
	private String _rootDomainName = null;
	private String _rootDomainUUID = null;
	
//	private static CommandEnum _status = CommandEnum.UserName;
	
	public InitDB() {
		// TODO Auto-generated constructor stub
	}
	
//	public static void main(String args[])throws IOException{
//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//		String str = null;
//		
//		try {
//			InitDB initDB = new InitDB();
//			initDB.init();
//			
//			String userName = null;
//			String email = null;
//			String pwd = null;
//			List<String> domains = new ArrayList<String>();
//			boolean hasSpecialChar = false;
//			
//			String type = null;
//			boolean exit = false;
//			String file = null;
//			do {
//				if(args.length == 1){
//					type = "bulk";
//					if(FileUtils.fileExists(args[0])){
//						file = args[0];
//						exit = true;
//						continue;
//					}
//				}
//				System.out.println("Enter 'stop' to quit.");
//				System.out.println("Enter 'bulk' to start bulk creation or 'addUser' for start single user creation");
//				
//				str = br.readLine().trim();
//				if(str == null || str.length() < 1){
//					System.out.println("ERROR: string is empty");
//					continue;
//				}
//				
//				if(str.equals("bulk") || str.equals("addUser")){
//					type = str;
//					exit = true;
//				}
//				else if(str.equals("stop")){
//					exit = true;
//				}
//				else{
//					System.out.println("ERROR");
//				}
//			} while(!exit);
//			
//			do {
//				if(type.equals("bulk")){
//					if(file == null){
//						System.out.println("Please add the file path with the users data");
//						str = br.readLine().trim();
//					}
//					else{
//						str = file;
//					}
//					
//					if(str.equals("stop")){
//						continue;
//					}
//					
//					if(str.equals("addUser")){
//						type = str;
//					}
//					
//					if(FileUtils.fileExists(str)){
//						String json = FileUtils.fileRead(str);
//						initDB.bulkCreation(json);
//					}
//					else{
//						System.out.println("ERROR: the file '" + str + "' does not exist");
//					}
//					
//					System.out.println("Bye!");
//					System.exit(0);
//				}
//				else if(type.equals("addUser")){
//					switch(_status){
//					case UserName:
//						System.out.println("Please add user name and press enter");
//						str = br.readLine().trim();
//						if(str.equals("stop")){
//							continue;
//						}
//						
//						if(str == null || str.length() < 1){
//							System.out.println("ERROR: string is empty");
//							continue;
//						}
//						
//						Pattern p = Pattern.compile("[^a-zA-Z0-9]");
//						hasSpecialChar = p.matcher(str).find();
//						if(hasSpecialChar){
//							System.out.println("ERROR: please use only alphanumeric characters");
//							continue;
//						}
//						userName = str;
//						_status = CommandEnum.Password;
//						break;
//						
//					case Password:
//						System.out.println("Please add password and press enter");
//						str = br.readLine().trim();
//						if(str.equals("stop")){
//							continue;
//						}
//						
//						if(str == null || str.length() < 1){
//							System.out.println("ERROR: string is empty");
//							continue;
//						}
//						pwd = str;
//						_status = CommandEnum.TestPassword;
//						break;
//						
//					case TestPassword:
//						System.out.println("Please re-type user password and press enter");
//						str = br.readLine().trim();
//						if(str.equals("stop")){
//							continue;
//						}
//						
//						if(str == null || str.length() < 1){
//							System.out.println("ERROR: string is empty");
//							continue;
//						}
//						if(!pwd.equals(str)){
//							//pwd = null;
//							System.out.println("ERROR: password does not match");
//							_status = CommandEnum.TestPassword;
//						}
//						else{
//							pwd = str;
//							_status = CommandEnum.UserEmail;
//						}
//						break;
//						
//					case UserEmail:
//						System.out.println("Please add user email and press enter");
//						str = br.readLine().trim();
//						if(str.equals("stop")){
//							continue;
//						}
//						
//						if(str == null || str.length() < 1){
//							System.out.println("ERROR: string is empty");
//							continue;
//						}
//						Pattern mailP = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
//						boolean isEmail = mailP.matcher(str).find();
//						if(!isEmail){
//							System.out.println("ERROR: please type a valid email");
//							continue;
//						}
//						
//						email = str;
//						_status = CommandEnum.Domain;
//						break;
//						
//					case Domain:
//						if(domains.size() == 0){
//							System.out.println("Please add user domain and press enter");
//						}
//						else{
//							System.out.println("Please add a new user domain and press enter");
//							System.out.println("or press 'q' for save and create user" );
//						}
//						
//						str = br.readLine().trim();
//						if(str.equals("stop")){
//							continue;
//						}
//						
//						if(domains.size() > 0){
//							if(str.equals("q")){
//								_status = CommandEnum.Create;
//								continue;
//							}
//						}
//						if(str == null || str.length() < 1){
//							System.out.println("ERROR: string is empty");
//							continue;
//						}
//						
//						Pattern domainP = Pattern.compile("[^a-zA-Z0-9]");
//						hasSpecialChar = domainP.matcher(str).find();
//						
//						if(hasSpecialChar){
//							System.out.println("ERROR: please use only alphanumeric characters");
//							continue;
//						}
//						
//						if(domains.contains(str)){
//							System.out.println("ERROR: the domain " + str + " was already added");
//							continue;
//						}
//						
//						domains.add(str);
//						_status = CommandEnum.Domain;
//						break;
//						
//					case Create:
//						System.out.println("Creating user and domain(s)...");
//						initDB.addUserAndDomain(userName, email, pwd, domains);
//						_status = CommandEnum.UserName;
//						break;
//					}
//				}
//			} while(!str.equals("stop"));
//			
//			System.out.println("Bye!");
//			System.exit(0);
//		} 
//		catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.exit(1);
//		}
//	}
	
	public void init() throws Exception{
		try {
			RATUtils.initProperties(RATConstants.PropertyFile);
//			_context = new FileSystemXmlApplicationContext(RATConstants.ConfigurationFolder + FileSystems.getDefault().getSeparator() + "spring-producer-unitTest.xml");
			RATSessionManager.init();
			
			_rootDomainName = AppProperties.getInstance().getStringProperty(RATConstants.RootPlatformDomainName);
			_rootDomainUUID = this.getDomainByName("GetDomainByName.conf", _rootDomainName);
			
			_dbManager = new DBManager();
			_dbManager.openDB();
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	public void addUserAndDomain(String userName, String email, String pwd, List<String> domains) throws Exception{
		String userUUID = this.addUser(userName, email, pwd);
		
		List<Vertex> result = this.getUser(VertexType.User, _rootDomainUUID, "userEmail", email, "GetUserByEmail.conf");
		System.out.println(result.size());
		String domainUUID = null;
		
		Map<String, String>domainMap = new HashMap<String, String>();
		for(String domain : domains){
			if(!this.domainExists(userUUID, domain)){
				domainUUID = this.createNewDomain(_rootDomainUUID, domain);
				this.setBind(domainUUID, userUUID);
				this.domainExists(userUUID, domain);
			}
			else{
				result = this.getDomain(userUUID, domain);
	    		System.out.println(result.size());
	    		domainUUID = result.get(0).getProperty(RATConstants.VertexUUIDField);
			}
			domainMap.put(domain, domainUUID);
			
			_dbManager.addDomain(domainUUID, domain);
			_dbManager.setDomainRoles(domainUUID, userUUID, "domainadmin");
			_dbManager.setUserDomain(userUUID, domainUUID, domain);
		}
	}
	
	public void bulkCreation(String json) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
    	mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    	JsonNode jsonNode = mapper.readTree(json);
    	Iterator <JsonNode> it = jsonNode.iterator();
    	while(it.hasNext()){
    		JsonNode node = it.next();
    		User user = mapper.readValue(node.toString(), User.class);
    		
    		String userUUID = this.addUser(user.name, user.email, user.password);
    		
    		List<Vertex> result = this.getUser(VertexType.User, _rootDomainUUID, "userEmail", user.email, "GetUserByEmail.conf");
    		System.out.println(result.size());
//	    		Assert.assertTrue(result.size() == 1);
    		String domainUUID = null;
    		
    		Map<String, String>domainMap = new HashMap<String, String>();
    		List<String>domains = user.domains;
    		for(String domain : domains){
    			if(!this.domainExists(userUUID, domain)){
    				domainUUID = this.createNewDomain(_rootDomainUUID, domain);
    				this.setBind(domainUUID, userUUID);
    				this.domainExists(userUUID, domain);
    			}
    			else{
    				result = this.getDomain(userUUID, domain);
    	    		System.out.println(result.size());
//	    	    		Assert.assertTrue(result.size() == 1);
    	    		domainUUID = result.get(0).getProperty(RATConstants.VertexUUIDField);
    			}
    			domainMap.put(domain, domainUUID);
    			
				_dbManager.addDomain(domainUUID, domain);
				_dbManager.setDomainRoles(domainUUID, userUUID, "domainadmin");
				_dbManager.setUserDomain(userUUID, domainUUID, domain);
    		}
    		
    		Comments comments = mapper.readValue(node.toString(), Comments.class);
    		List<Comment>commentList = comments.comments;
    		if(commentList != null){
	    		for(Comment comment : commentList){
	    			domainUUID = domainMap.get(comment.domain);
	    			System.out.println(domainUUID);
	    			this.addComment(comment, domainUUID, userUUID);
	    		}
    		}
			// Tests
			//getUserURLs("GetUserURLs.conf", userUUID);
			
    		System.out.println(node.toString());
    	}
	}
	
	private void addComment(Comment comment, String domainUUID, String userUUID) throws Exception{
		String commandJSON = SystemInitializerTestHelpers.addUserComment("AddComment.conf", 
				domainUUID, userUUID, "{json coordinates}", comment.url, comment.VertexContentField, comment.VertexLabelField);
		CommandSink commandSink = new CommandSink();
		Response response = commandSink.doCommand(commandJSON);
		String jsonResponse = JSONObjectBuilder.serializeCommandResponse(response);
		System.out.println(RATJsonUtils.jsonPrettyPrinter(jsonResponse));
		MQMessage message = JSONObjectBuilder.deserializeCommandResponse(jsonResponse);
		String status = message.getHeader().getStatusCode();
//		Assert.assertEquals("200", status);
	}
	
	private void setBind(String domainUUID, String userUUID) throws Exception{
		String commandJSON = SystemInitializerTestHelpers.bindUserToDomain("BindFromUserToDomain.conf", domainUUID, userUUID);
		CommandSink commandSink = new CommandSink();
		Response response = commandSink.doCommand(commandJSON);
		String jsonResponse = JSONObjectBuilder.serializeCommandResponse(response);

		MQMessage message = JSONObjectBuilder.deserializeCommandResponse(jsonResponse);
		String resultUUID = message.getHeaderProperty(RATConstants.VertexUUIDField).toString();
		if(!Utils.isUUID(resultUUID)){
			throw new Exception();
			// TODO log
		}
	}
	
	private String createNewDomain(String rootDomainUUID, String domainName) throws Exception{
		String commandJSON = SystemInitializerTestHelpers.createNewDomain("AddNewDomain.conf", rootDomainUUID, domainName);
		CommandSink commandSink = new CommandSink();
		Response response = commandSink.doCommand(commandJSON);
		String jsonResponse = JSONObjectBuilder.serializeCommandResponse(response);
		System.out.println(RATJsonUtils.jsonPrettyPrinter(jsonResponse));
		
		MQMessage message = JSONObjectBuilder.deserializeCommandResponse(jsonResponse);
		String domainUUID = message.getHeaderProperty(RATConstants.VertexUUIDField).toString();
		if(!Utils.isUUID(domainUUID)){
			throw new Exception();
			// TODO log
		}
		
		return domainUUID;
	}
	
	private boolean domainExists(String userUUID, String domainName) throws Exception{
		List<Vertex> result = this.getDomain(userUUID, domainName);
		
		return result.size() > 0 ? true : false;
	}
	
	private List<Vertex> getDomain(String userUUID, String domainName) throws Exception{
		String commandJSON  = SystemInitializerTestHelpers.getUserDomainByName("GetUserDomainByName.conf", userUUID, domainName, VertexType.Domain);
		CommandSink commandSink = new CommandSink();
		Response response = commandSink.doCommand(commandJSON);
		String jsonResponse = JSONObjectBuilder.serializeCommandResponse(response);
		System.out.println(RATJsonUtils.jsonPrettyPrinter(jsonResponse));
		RATJSONMessage ratJSONMessage = RATJSONMessage.deserialize(jsonResponse);
		System.out.println("countAllNodes: " + ratJSONMessage.countAllNodes());
		System.out.println("countNodes: " + ratJSONMessage.countNodes());
		
		List<Vertex> result = ratJSONMessage.getNode(VertexType.Domain, "domainName", domainName);
		
		return result;
	}
	
	private String addUser(String userName, String userEmail, String userPWD) throws Exception{
		String userUUID = null;
		List<Vertex> result = this.getUser(VertexType.User, _rootDomainUUID, "userEmail", userEmail, "GetUserByEmail.conf");
		
		if(result == null || result.size() == 0){
			String commandJSON = SystemInitializerTestHelpers.createNewUser("AddNewUser.conf", _rootDomainUUID, userName, userPWD, userEmail);
//			String jsonResponse = RATSessionManager.getInstance().sendMessage(_context, commandJSON);
//			System.out.println(RATJsonUtils.jsonPrettyPrinter(jsonResponse));
			
			CommandSink commandSink = new CommandSink();
			Response response = commandSink.doCommand(commandJSON);
			String jsonResponse = JSONObjectBuilder.serializeCommandResponse(response);
//			System.out.println(RATJsonUtils.jsonPrettyPrinter(jsonResponse));
//			System.out.println(response.getCommandResponse().getRootUUID());
			
			MQMessage message = JSONObjectBuilder.deserializeCommandResponse(jsonResponse);
			userUUID = message.getHeaderProperty(RATConstants.VertexUUIDField).toString();
		}
		else if(result.size() == 1){
			Vertex vertex = result.get(0);
			userUUID = vertex.getProperty(RATConstants.VertexUUIDField);
		}
		else{
			throw new Exception();
		}
		
		if(!Utils.isUUID(userUUID)){
			throw new Exception();
			// TODO log
		}
		_dbManager.addUser(userName, userEmail, userPWD, userUUID);
		_dbManager.addUserRole(userUUID, "domainadmin");
		
		return userUUID;
	}
	
	private List<Vertex> getUser(VertexType userType, String domainUUID, String paramName, String paramValue, String fileName) throws Exception{
		List<Vertex> result = null;
		
		String commandJSON = SystemInitializerTestHelpers.getUserByEmail(fileName, "paramValue", paramValue, ReturnType.string);
		//System.out.println(RATJsonUtils.jsonPrettyPrinter(commandJSON));
		CommandSink commandSink = new CommandSink();
		Response response = commandSink.doCommand(commandJSON);
		if(response.getStatusCode().equals("200")){
			String jsonResponse = JSONObjectBuilder.serializeCommandResponse(response);
			RATJSONMessage ratJSONMessage = RATJSONMessage.deserialize(jsonResponse);
			result = ratJSONMessage.getNode(userType, paramName, paramValue);
		}
		
		return result;
	}
	
	private String getDomainByName(String fileName, String domainName) throws Exception{
		String json = SystemInitializerTestHelpers.getUserByEmail("GetNodeByType.conf", "paramValue", VertexType.RootDomain.toString(), ReturnType.string);
		
		CommandSink commandSink = new CommandSink();
		Response response = commandSink.doCommand(json);
		
		json = JSONObjectBuilder.serializeCommandResponse(response);
		RATJSONMessage ratJSONMessage = RATJSONMessage.deserialize(json);
		List<Vertex> result = ratJSONMessage.getNode(VertexType.RootDomain, RATConstants.VertexContentField, domainName);
		if(result.size() != 1){
			throw new Exception();
		}
		
		Vertex v = result.get(0);
		String strUUID = v.getProperty(RATConstants.VertexUUIDField);
		if(!Utils.isUUID(strUUID)){
			throw new Exception();
		}
		
		return strUUID;
	}

}
