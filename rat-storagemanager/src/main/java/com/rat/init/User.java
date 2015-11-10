/*
 * @author Daniele Grignani
 * Mar 14, 2015
*/

package com.rat.init;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;
 
@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.PUBLIC_ONLY)
// {"name":"dgr","password":"dgr","email":"dgr@gmail.com","domains":["domain dgr","domain dgr1"]}
public class User {
	@JsonProperty("name")
	public String name = null;
	@JsonProperty("password")
	public String password = null;
	@JsonProperty("email")
	public String email = null;
	@JsonProperty("domains")
	public List<String> domains = null;
}
