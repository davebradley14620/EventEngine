package eventengine.server;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;
import java.sql.SQLException;
import com.sun.jersey.api.NotFoundException;

public class EventResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	public EventResource(UriInfo uriInfo, Request request, int id) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.mId = id;
	}
	
	public EventResource(UriInfo uriInfo, Request request, Event event ) {
		this(uriInfo,request,event.getId());
		this.mEvent = event;
	}
	
	//Application integration 		
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Event getEvent() {
		if ( this.mEvent != null ) {
			return( this.mEvent );
		}
		if ( this.mId > 0 ) {
			try {
				this.mEvent = Model.instance.getEventById(this.mId);
			} catch( SQLException e ) {
				throw new RuntimeException("EventResource.Get: "+e.getMessage());
			}
		}

		if ( this.mEvent == null ) {
			//throw new RuntimeException("EventResource.Get: Event with " + id +  " not found");
			throw new NotFoundException("Event with id " + this.mId +  " not found");
		}

		return ( this.mEvent );
	}
	
	// For the browser
//	@GET
//	@Produces(MediaType.TEXT_XML)
//	public Event getEventHTML() {
//		return( getEvent() );
//	}
	
	@PUT
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//	public Response putEvent(JAXBElement<Event> event) {
	public Event putEvent(JAXBElement<Event> event) {
Utility.getLogger().debug("EventResource.putEvent: qname="+event.getName());
		Event ret = null;
		Event c = event.getValue();
		Response res = Response.created(uriInfo.getAbsolutePath()).build();
		try {
			ret = Model.instance.updateEvent(c);
		} catch( SQLException e ) {
			Utility.getLogger().error("EventResource.putAndGetResponse: "+e.getMessage());
			e.printStackTrace(System.err);
			res = Response.noContent().build();
		}
//		return res;	
		return( ret );

	}
	
	@DELETE
	public Response deleteEvent() {
		Response res = Response.created(this.uriInfo.getAbsolutePath()).build();

		int id = this.mId;
		if ( (id == -1 ) && (this.mEvent != null) ) {
			id = this.mEvent.getId();
		}
		if ( id != -1 ) {
			try {
				Model.instance.removeEvent(id);
			} catch( SQLException e ) {
				Utility.getLogger().error("Error deleting event with id: "+id+": "+e.getMessage());
				e.printStackTrace(System.err);
				res = Response.noContent().build();
			}
		}
		return( res );
	}
	
	private int mId = -1;
	private Event mEvent;
} 
