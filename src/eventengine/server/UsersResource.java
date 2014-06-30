package eventengine.server;


import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.sql.SQLException;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;



// Will map the resource to the URL Users
@Path("/users")
public class UsersResource {

	// Allows to insert contextual objects into the class, 
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;


	// Return the list of Users to the user in the browser
//	@GET
//	@Produces(MediaType.TEXT_XML)
//	public List<User> getUsersBrowser() {
//		try {
//			return( Model.instance.getUsers() );
//		} catch( SQLException e ) {
//			throw new RuntimeException("UsersResource.getUsersBrowser: "+e.getMessage());
//		}
//	}
	
//	// Return the list of Users for applications
//	@GET
//	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//	public List<User> getUsers() {
//		try {
//			return( Model.instance.getUsers() );
//		} catch( SQLException e ) {
//			throw new RuntimeException("UsersResource.getUsers: "+e.getMessage());
//		}
//	}
	
	
	// retuns the number of Users
	// Use http://localhost:8080/de.vogella.jersey.User/rest/users/count
	// to get the total number of records
	@GET
	@Path("count")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCount() {
		int count = 0;
		try {
			count = Model.instance.getUserCount();
		} catch( SQLException e ) {
			throw new RuntimeException("UsersResource.getUsers: "+e.getMessage());
		}
		return (String.valueOf(count));
	}
	
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void updateUser(@FormParam("id") String id,
			@FormParam("name") String name,
			@FormParam("firstname") String firstname,
			@FormParam("lastname") String lastname,
			@FormParam("screenname") String screenname,
			@FormParam("fbid") String fbid,
			@FormParam("email") String email,
			@FormParam("address") String address,
			@FormParam("city") String city,
			@FormParam("state") String state,
			@FormParam("zip") String zip,
			@FormParam("regcode") String regcode,
			@FormParam("istermsread") String istermsread,						
			@Context HttpServletResponse servletResponse) throws IOException {
		Date now = new Date();
		//
		// start should be a time and date string in the format yyyy.mm.dd.hh.mm separated by period.
		//
		boolean isTermsRead=false;
		if ( (istermsread != null) && (istermsread.length() > 0) ) {
			isTermsRead = Boolean.parseBoolean(istermsread);
		}

		Location location = new Location( 0., 0., null, address, city, state, zip );
		int _id = -1;
		try {
			_id = Integer.parseInt(id);
		} catch( NumberFormatException e ) {
			throw new RuntimeException("UsersResource.updateUser: 'id' is not a number: "+id);		
		}
		int _regcode = -1;
		try {
			_regcode = Integer.parseInt(regcode);
		} catch( NumberFormatException e ) {
			throw new RuntimeException("UsersResource.updateUser: 'regcode' is not a number: "+regcode);		
		}		
		User user = new User(_id, screenname, firstname, lastname, location, fbid, email, now, null, isTermsRead, _regcode);

		try {
			Model.instance.updateUser(user);
			servletResponse.sendRedirect("../create_user.html");			
		} catch( SQLException e ) {
			e.printStackTrace(System.err);
			Utility.getLogger().error("UsersResource.updateUser: failed to create new user: "+e.getMessage());
		}
	}

	/**
	 * Join an event.
	 * @param eventid		Event ID of event to join.
	 * @param userid		User ID of user joining event.
	 * @return Event		Event joined, or null if none
	 */
	@Path("/join/{eventid}/{userid}")
	public Event joinEvent(@PathParam("eventid") String eventid, @PathParam("userid") String userid) {
		int _eventid;
		try {
			_eventid = Integer.parseInt(eventid);
		} catch( NumberFormatException e ) {
			throw new RuntimeException("UsersResource.joinEvent: 'eventid' is not a number: "+eventid);		
		}
		int _userid;
		try {
			_userid = Integer.parseInt(userid);
		} catch( NumberFormatException e ) {
			throw new RuntimeException("UsersResource.joinEvent: 'userid' is not a number: "+userid);		
		}
		Event event = null;
		try {
			event = Model.instance.joinEvent( _eventid, _userid );
		} catch( SQLException e ) {
			throw new RuntimeException("UsersResource.joinEvent: "+e.getMessage());		

		}
		return( event );
	}
	
	// Defines that the next path parameter after events is
	// treated as a parameter and passed to the UserResources
	// Allows to type http://localhost:8080/EventEngine/rest/events/1
	// 1 will be treaded as parameter event and passed to UserResource
	@Path("/get/{id}")
	public UserResource getUser(@PathParam("id") String id) {
		Utility.getLogger().debug("getUser: id="+id);
		int _id = -1;
		try {
			_id = Integer.parseInt(id);
		} catch( NumberFormatException e ) {
			throw new RuntimeException("UsersResource.getUser: 'id' is not a number: "+id);		
		}		
		return new UserResource(uriInfo, request, _id);
	}

} 
