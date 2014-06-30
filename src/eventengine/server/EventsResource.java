package eventengine.server;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.List;
import java.util.Vector;
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


// Will map the resource to the URL Events
@Path("/events")
public class EventsResource {




	// Return the list of Events to the user in the browser
//	@GET
//	@Produces(MediaType.TEXT_XML)
//	public List<Event> getEventsBrowser() {
//		List<Event> Events = new ArrayList<Event>();
////		Events.addAll(Model.instance.contentProvider.values());
//		return Events; 
//	}
	
//	// Return the list of Events for applications
//	@GET
//	@Produces({MediaType.APPLICATION_XML})
//	public List<Event> getEvents() {
//		List<Event> Events = new ArrayList<Event>();
////		Events.addAll(Model.instance.contentProvider.values());
//		return Events; 
//	}
	
	
	// returns the number of Events
	// Use http://localhost:8080/de.vogella.jersey.Event/rest/Events/count
	// to get the total number of records
	@GET
	@Path("count")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCount() {
		int count = 0;
		try {
			count = Model.instance.getEventCount();
		} catch( SQLException e ) {
			throw new RuntimeException("EventsResource.getCount: "+e.getMessage());		
		}
		return (String.valueOf(count));
	}

	/**
	 * Create or update an event.
	 *
	 * id - ID of event to update, or 0 if creating a new event.
	 * name - name of event
	 * description		- description of event
	 * start			- start time of event
	 * duration			- duration of event, in minutes
	 * isrepeating		- "true" if this event repeats (i.e. not a one-time event)
	 * cycle			- if isrepeating is true, then this is set to "daily", "weekly", "monthly", or "yearly"
	 * address			- address of event
	 * city				- city of event
	 * state			- state of event
	 * zip				- postal code of event
	 * lat				- latitude of event.  If blank, this will be calculated from the address.
	 * lon				- longitude of event. If blank, this will be calculated from the address.
	 */ 
	@POST
    @Path("update")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//	@Consumes({MediaType.APPLICATION_JSON})
	public Response updateEvent(
			@FormParam("id") Integer id,
			@FormParam("name") String name,
			@FormParam("description") String description,
			@FormParam("creator") Integer creator,
			@PathParam("catid") String catid, 
			@FormParam("start") String start,
			@FormParam("duration") Integer duration,
			@FormParam("isrepeating") String isrepeating,
			@FormParam("cycle") String cycle,
			@FormParam("address") String address,
			@FormParam("city") String city,
			@FormParam("state") String state,
			@FormParam("zip") String zip,
			@FormParam("lat") String lat,
			@FormParam("lon") String lon,
			@Context HttpServletResponse servletResponse) throws EventEngineException {
		Utility.getLogger().debug("EventsResource.updateEvent("+name+","+description+","+creator+","+start+","+duration+","+isrepeating+","+cycle+","+address+","+city+","+state+","+zip+","+lat+","+lon);
		Date now = new Date();
		//
		// start should be a time and date string in the format yyyy.mm.dd.hh.mm separated by period.
		//
		Date startTime = parseTimestamp(start);
		boolean isRepeating=false;
		if ( (isrepeating != null) && (isrepeating.length() > 0) ) {
			isRepeating = Boolean.parseBoolean(isrepeating);
		}
		Cycle repeatCycle = null;
		if ( (cycle != null) && (cycle.length() > 0) ) {
			repeatCycle = Cycle.valueOf(cycle);
		}
		int _id = -1;
		try {
			//_id = Integer.parseInt(id);
			_id = id.intValue();
		} catch( NumberFormatException e ) {
			throw new EventEngineException("EventsResource.updateEvent: 'id' is not a number: "+id);		
		}
		int _creator = -1;
		try {
			//_creator = Integer.parseInt(creator);
			_creator = creator.intValue();
		} catch( NumberFormatException e ) {
			throw new EventEngineException("EventsResource.updateEvent: 'creator' is not a number: "+creator);		
		}
		double _lat = 0.;
		try {
			_lat = Double.parseDouble(lat);
		} catch( NumberFormatException e ) {
			throw new EventEngineException("EventsResource.updateEvent: 'lat' is not a number: "+lat);		
		}
		double _lon = 0.;
		try {
			_lon = Double.parseDouble(lon);
		} catch( NumberFormatException e ) {
			throw new EventEngineException("EventsResource.updateEvent: 'lon' is not a number: "+lon);		
		}	
		int _catid=-1;
		List<Category> cats = new Vector<Category>(0);
		try {
			if ( (catid != null) && (catid.length() > 0) ) {
				_catid = Integer.parseInt(catid);
				try {
					cats.add(Model.instance.getCategoryById(_catid));
				} catch( SQLException e ) {
					throw new EventEngineException("EventsResource.updateEvent: Unknown category with ID: "+catid);		
				}
			}
		} catch( NumberFormatException e ) {
			throw new EventEngineException("EventsResource.getEventsLocal: 'catid' is not a number: "+catid);		
		}
		Location location = new Location( _lat, _lon, null, address, city, state, zip );
		Event event = new Event(_id, name, description, now, _creator, startTime, duration, isRepeating, repeatCycle, location, cats, 0);

		try {
			Model.instance.updateEvent(event);
		} catch( SQLException e ) {
			throw new EventEngineException("EventsResource.newEvent: "+e.getMessage());		
		}
		//
        // Return the event ID back to the caller.
		//
        return Response.ok().entity("id="+event.getId()).build();
	}
	
	
	/**
	 * Join an event.
	 * @param eventid		Event ID of event to join.
	 * @param userid		User ID of user joining event.
	 * @return Event		Event joined, or null if none
	 */
	@GET
	@Path("/join/{eventid}/{userid}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Event joinEvent(@PathParam("eventid") String eventid, @PathParam("userid") String userid) {
		int _eventid;
		try {
			_eventid = Integer.parseInt(eventid);
		} catch( NumberFormatException e ) {
			throw new RuntimeException("EventsResource.joinEvent: 'eventid' is not a number: "+eventid);		
		}
		int _userid;
		try {
			_userid = Integer.parseInt(userid);
		} catch( NumberFormatException e ) {
			throw new RuntimeException("EventsResource.joinEvent: 'userid' is not a number: "+userid);		
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
	// treated as a parameter and passed to the EventResources
	// Allows to type http://localhost:8080/EventEngine/rest/events/1
	// 1 will be treaded as parameter event and passed to EventResource
	//	@Produces({MediaType.APPLICATION_JSON})
	@Path("/get/{id}")
	public EventResource getEvent(@PathParam("id") String id) {
		Utility.getLogger().debug("EventsResource.getEvent("+id+")");

		int _id = -1;
		try {
			_id = Integer.parseInt(id);
		} catch( NumberFormatException e ) {
			throw new RuntimeException("EventsResource.newEvent: 'id' is not a number: "+id);		
		}
		return new EventResource(uriInfo, request, _id);
	}
	
	//
	// Get all events for the given radius at the given location, possibly meeting the given category and/or
	// keyword search criteria.
	//
//	@POST
//	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//	http://172.16.75.154:8080/EventEngine/rest/events/search/41.335329/-74.187698/30/true//0/0/0/-1/foobar/
	@GET
	@Path("/search/{lat}/{lon}/{rad}/{expired}/{catid}/{startdate}/{enddate}/{offset}/{count}/{keyword}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public List<Event> getEventsLocal(
			@PathParam("lat") String lat, 
			@PathParam("lon") String lon, 
			@PathParam("rad") String rad, 
			@PathParam("expired") String expired, 
			@PathParam("catid") String catid, 
			@PathParam("startdate") String startDate,
			@PathParam("enddate") String endDate,
			@PathParam("offset") String offset, 
			@PathParam("count") String count,
			@PathParam("keyword") String keyword
	) {
		
		Utility.getLogger().debug("EventsResource.getEventsLocal("+lat+","+lon+","+rad+","+expired+","+catid+","+startDate+","+endDate+","+offset+","+count+","+keyword+")");

		List<Event> events = new ArrayList<Event>(0);
		double _lat = 0.;
		double _lon = 0.;
		int _rad = DEFAULT_SEARCH_RADIUS_MILES;
		boolean isExpired = Boolean.parseBoolean(expired);
		int _catid = -1;
		int _offset = 0;
		int _count = 0;
		int _startDate = 0;	// number of seconds since 1/1/70
		int _endDate = 0;	// number of seconds since 1/1/70
		try {
			if ( (lat != null) && (lat.length() > 0) ) {
				_lat = Double.parseDouble(lat);
			}
		} catch( NumberFormatException e ) {
			throw new RuntimeException("EventsResource.getEventsLocal: 'lat' is not a number: "+lat);		
		}
		try {
			if ( (lon != null) && (lon.length() > 0) ) {
				_lon = Double.parseDouble(lon);
			}
		} catch( NumberFormatException e ) {
			throw new RuntimeException("EventsResource.getEventsLocal: 'lon' is not a number: "+lon);		
		}
		try {
			if ( (rad != null) && (rad.length() > 0) ) {
				_rad = Integer.parseInt(rad);
			}
		} catch( NumberFormatException e ) {
			throw new RuntimeException("EventsResource.getEventsLocal: 'rad' is not a number: "+rad);		
		}
		try {
			if ( (catid != null) && (catid.length() > 0) ) {
				_catid = Integer.parseInt(catid);
			}
		} catch( NumberFormatException e ) {
			throw new RuntimeException("EventsResource.getEventsLocal: 'catid' is not a number: "+catid);		
		}
		try {
			if ( (startDate != null) && (startDate.length() > 0) ) {
				_startDate = Integer.parseInt(startDate);
			}
		} catch( NumberFormatException e ) {
			throw new RuntimeException("EventsResource.getEventsLocal: 'startDate' is not a number: "+startDate);		
		}
		try {
			if ( (endDate != null) && (endDate.length() > 0) ) {
				_endDate = Integer.parseInt(endDate);
			}
		} catch( NumberFormatException e ) {
			throw new RuntimeException("EventsResource.getEventsLocal: 'endDate' is not a number: "+endDate);		
		}
		try {
			if ( (offset != null) && (offset.length() > 0) ) {
				_offset = Integer.parseInt(offset);
			}
		} catch( NumberFormatException e ) {
			throw new RuntimeException("EventsResource.getEventsLocal: 'offset' is not a number: "+offset);		
		}
		try {
			if ( (count != null) && (count.length() > 0) ) {
				_count = Integer.parseInt(count);
			}				
		} catch( NumberFormatException e ) {
			throw new RuntimeException("EventsResource.getEventsLocal: 'count' is not a number: "+count);		
		}
		try {
			List<Event> evs = Model.instance.getEventsByLocation( _lat, _lon, _rad, isExpired, _catid, _startDate, _endDate, keyword, _offset, _count );
			events.addAll(evs);
		} catch( SQLException e ) {
			throw new RuntimeException("EventsResource.getEventsLocal: "+e.getMessage());		
		}

		return( events );
	}
	
	/**
	 * Convert a string containing seconds since the epoch (Jan 1, 1970) into a Date object.
	 * @param pTimestamp	String containing seconds since the epoch
	 * @return Date			Date object representing given time and date.
	 * @throws IllegalArgumentException	Parameter is not an integer.
	 */
	private static Date parseTimestamp( String pTimestamp ) 
		throws IllegalArgumentException
	{
		long millis = 0;
		try {
			millis = Long.parseLong(pTimestamp);
			millis *= 1000;		// convert seconds to milliseconds
		} catch( NumberFormatException e ) {
			throw new IllegalArgumentException("parseTimestamp: non-numeric field in timestamp: "+pTimestamp);		
		}
		Calendar c = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
	    c.setTimeInMillis(millis);
		return( c.getTime() );

/* This parses a timestamp of the form yyyy.MM.dd.hh.mm
		System.err.println("enter");
		String[] parts = pTimestamp.split("[.]");
		if ( (parts == null) || (parts.length != 5) ) {
			throw new IllegalArgumentException("parseTimestamp: Invalid timestamp "+pTimestamp+".  Should be yyyy.mm.dd.hh.mm");
		}	
		int year, month, day, hour, minute;
		try {
			year = Integer.parseInt(parts[0]);
			month = Integer.parseInt(parts[1]);
			day = Integer.parseInt(parts[2]);
			hour = Integer.parseInt(parts[3]);
			minute = Integer.parseInt(parts[4]);
		} catch( NumberFormatException e ) {
			throw new IllegalArgumentException("parseTimestamp: non-numeric field in timestamp: "+pTimestamp);
		}
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day, hour, minute);
		return( cal.getTime() );
*/
	}
	/**
	 * Testjig
	 * @param args
	 */
	public static void main( String[] args ) {
		Date d = parseTimestamp("2012.07.30.18.32");
		System.err.println("date = "+d);
	}
	
	// Allows to insert contextual objects into the class, 
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	private static final int DEFAULT_SEARCH_RADIUS_MILES = 30;
} 
