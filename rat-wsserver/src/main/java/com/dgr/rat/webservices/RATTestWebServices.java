/**
 * @author: Daniele Grignani
 * @date: Feb 7, 2016
 */

package com.dgr.rat.webservices;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

import com.dgr.rat.json.command.parameters.SystemInitializerTestHelpers;

@Path("/rattest")
@Stateless
public class RATTestWebServices {
	@POST @Path("/v0.1/addRootDomainAdminUser")
	@Asynchronous
	@Consumes (MediaType.TEXT_PLAIN)
	@Produces (MediaType.TEXT_PLAIN)
	public void addRootDomainAdminUser(@QueryParam("sessionid") String sessionID, @QueryParam("useruuid") String userUUID, @Suspended AsyncResponse asyncResponse){
		//String json = SystemInitializerTestHelpers.createAddRootDomainAdminUser("AddRootDomainAdminUser.conf", rootDomainUUID, userAdminName, userAdminPwd, userAdminEmail);
	}
}
