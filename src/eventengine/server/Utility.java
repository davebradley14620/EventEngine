/**
 * 
 */
package eventengine.server;
import java.util.*;
import java.text.*;
import java.net.URLEncoder;
import java.net.URLDecoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.io.IOException;
import java.lang.reflect.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.net.URL;
import java.net.MalformedURLException;
import javax.servlet.*;
import javax.servlet.http.*;
//import javax.servlet.jsp.JspWriter;
import javax.mail.*;
import javax.mail.internet.*;
//import javax.activation.*;
import org.apache.commons.lang3.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


/**
 * @author db
 *
 */
public class Utility
{

	public Utility()
	{
	}
	
	/**
	 * Escape a string bound for the database.  This is to prevent SQL injection attacks by
	 * hackers randomly placing single quotes in strings.
	 * @param pStr		unsafe string
	 * @return		safe string
	 */
	public static String escapeSql(String pStr ) {
		if (pStr == null) {
	        	return null;
	        }		
		return ( pStr.replace("'", "\'") );
	}
	
	/**
	 * Return the HTML for a selection input full of the 50 states.   If pSelectedState is given,
	 * it is marked 'selected' in the list.
	 * @param pName			- Name of selection widget to assign.
	 * @param pId			- ID of selection widget to assign.
	 * @param pSelectedState	- Optional selected state.
	 * @return String		- Full <select></select> containing all states.
	 */
	public static String drawStateSelection(String pName, String pId, String pSelectedState ) {
	        final String[] state_values = {"","AL","AK","AZ","AR","CA","CO","CT","DC","DE","FL","GA","HI","IL","IN","IA","KS","KY","LA","ME","MD","MA","MI","MN","MS","MO","MT","NE","NV","NH","NJ","NM","NY","NC","ND","OH","OK","OR","PA","PR","RI","SC","SD","TN","TX","UT","VT","VA","WA","WV","WI","WY"};
	        StringBuffer sb = new StringBuffer();
	        sb.append("<select name=\""+pName+"\" id=\""+pId+"\">");
	        sb.append("<option value=\""+pSelectedState+"\" SELECTED>"+pSelectedState+"</option>");
	        for( int i=0; i < state_values.length; i++ ) {
	                sb.append("<option value=\""+state_values[i]+"\">"+state_values[i]+"</option>");
	        }
	        sb.append("</select>");
		return( sb.toString() );
	}

	/**
	 * Given a phone number string, format it into something human readable.  i.e. put hyphens and
	 * parentheses in the correct places.
	 * @param 	pPhone		Input phone number.
	 * @return	String		Formatted phone number.
	 * @exception	NumberFormatException	String is not a valid phone number.
	 */
	public static String formatPhoneNumber( String pPhone )
		throws NumberFormatException
	{
		if ( (pPhone == null) || (pPhone.length() == 0) ) {
			return( "" );
		}
		//
		// Strip out non-digits.  It may already have formatting characters in it.
		//
		StringBuffer number = new StringBuffer(pPhone.length());
		for( int i=0; i < pPhone.length(); i++ ) {
			String c = pPhone.substring(i,i+1);
			try {
				Integer.parseInt(c);
				number.append(c);
			} catch( NumberFormatException e ) {
				// Do nothing.
			}
		}
		//
		// Insert the parens and hyphens in the correct spots.
		//
		if ( number.length() == 10 ) {
			number.insert(0, '(');
			number.insert(4, ')');
			number.insert(5, ' ');
			number.insert(9, '-');
		} else if ( number.length() == 11 ) {
			number.insert(1, '-');
			number.insert(2, '(');
			number.insert(6, ')');
			number.insert(7, ' ');
			number.insert(11, '-');
		} else {
			throw new NumberFormatException("Utility.formatPhoneNumber: Not a valid phone number: "+number.toString());
		}		
		return( number.toString() );
	}
	
	/**
	 * Given a string that is supposed to represent a URL, format it into a well-formed URL and
	 * return it.
	 * @param 	pURL		URL to format.
	 * @return	String		Well formed URL
	 */
	public static String formatURL( String pURL )
		throws MalformedURLException
	{
		if ( (pURL == null) || (pURL.length() == 0) ) {
			return( "" );
		}
		String newUrl = "";
		if ( ! pURL.startsWith( "http://") ) {
			newUrl = "http://";
		}
		newUrl += pURL;
		//
		// If the URL is still bogus, URL() will throw an exception here.
		//
		new URL( newUrl );
		return( newUrl );
	}
	
	/**
	 * Try to determine the user's location based on what's stored in the session, or what's in
	 * the user's profile.  If there is no session or user object, then geoIP the IP address.
	 * @param pRequest
	 * @return
	 * @throws GeocoderException
	 */
/*
	public static Location determineLocation( HttpServletRequest pRequest, ServletContext pContext )
		throws GeocoderException
	{
		Location coord = null;
		HttpSession session = pRequest.getSession();		


		boolean force = false;
		
		//
		// Look in the session and see if we previously saved our lat/lon values.
		//
		String loc = null;
		double latitude = 0.;
		double longitude = 0.;
		if ( session != null ) {
			loc = (String)session.getAttribute("location");
			if ( (loc != null) && (loc.length() > 0) ) {
				Double slat = (Double)session.getAttribute("lat");
				if ( slat != null ) {
					latitude = slat.doubleValue();
				}
				Double slon = (Double)session.getAttribute("lon");
				if ( slon != null ) {
					longitude = slon.doubleValue();
				}	
				if ( (latitude != 0.) && (longitude != 0.) ) {
					coord = new Location(latitude,longitude,loc);
				}
			}
		}

		String ploc = pRequest.getParameter("location");
		if ( (ploc != null) && (ploc.length() != 0) ) {
			//
			// If we had a location stored in the session and
			// we have one coming in from the request, and they're different,
			// then we want to do a geocode.
			//
			if ( (loc != null) && (! loc.equals(ploc)) ) {
System.err.println("Utility.determineLocation: forcing geocode because session and request are different.");
				loc = ploc;
				force = true;
			} else {
				loc = ploc;
				String plat = pRequest.getParameter("lat");
				String plon = pRequest.getParameter("lon");				
				if ( (plat != null) && (plat.length() > 0) && (plon != null) && (plon.length() > 0) ) {
					latitude = Double.parseDouble(plat);
					longitude = Double.parseDouble(plon);
					if ( (latitude != 0.) && (longitude != 0.) ) {
						coord = new Location(latitude,longitude,loc);
					}
				}					
			}
		}
		
		//
		// If we're not forcing a geocode, and we still don't have our location, then
		// look into the user object, if there is one.
		//
		if ( (! force) && (session != null) && (coord == null) ) {
			User user = (User)session.getAttribute("user");
			if ( user != null ) {
				Location c = user.getLocation();
				if ( c != null ) {
					loc = c.getAddress()+" "+c.getCity()+", "+c.getState()+", "+c.getZip();
					Model.getLogger().debug("Utility.determineLocation: loc from user object is: "+loc);
					coord = new Location(c.getLatitude(), c.getLongitude(),loc);
				}
			}
		}		
		
		Model model = (Model)pContext.getAttribute("model");
		if ( model == null ) {
			System.err.println("Utility.determineLocation: no model");
			return( null );
		}
		
		//
		// If we still don't have a latitude and longitude, then see if we at least have a location.
		// If so, then attempt to geocode it.
		//
		if ( force || ((coord == null) && (loc != null) && (loc.length() > 0)) ) {
System.err.println("Utility.determineLocation: geocoding "+loc);
			coord = model.geocode("","","",loc);
		}		
		//
		// If we still don't have a lat/lon, then attempt to map the IP address.
		//
		if ( coord == null ) {
			try {
				String remoteAddr = pRequest.getRemoteAddr();
				//
				// If we have a proxy server in front of us, we may get the
				// proxy server's IP, not the client's.  Many proxies set X-FORWARDED-FOR
				// in the header, with the client's IP address.  Look for this
				// and use it if it's available.
				//
				String x = pRequest.getHeader("X-FORWARDED-FOR");
				if (x != null) {
					remoteAddr = x;
					int idx = remoteAddr.indexOf(',');
					if (idx > -1) {
						remoteAddr = remoteAddr.substring(0, idx);
					}
				}
				
System.err.println("Utility.determineLocation: geoIPing "+remoteAddr);				
				coord = model.geoIP(remoteAddr);
				if ( coord != null ) {
					System.err.println("Utility.determineLocation: geoIP: latitude="+coord.getLatitude()+",longitude="+coord.getLongitude()+",loc="+loc);				
				}
			} catch( SQLException sqle ) {
				System.err.println("Utility.determineLocation: failed to geoIP("+pRequest.getRemoteAddr()+"): "+sqle.getMessage());
			}
		}
		
		//
		// As a last ditch, we simply assign "Rochester, NY" to it, geocode that, and be done.
		//
		if ( coord == null ) {
			System.err.println("Utility.determineLocation: no known latitude/longitude. Forcing to Rochester,NY");			
			coord = new Location(43.16103,-77.6109219, "Rochester, NY");
		}

		//
		// Update the session with the latest info.
		//
		session.setAttribute("lat", coord.getLatitude());
		session.setAttribute("lon", coord.getLongitude());
		session.setAttribute("location",coord.getLocation());
System.err.println("Utility.determineLocation: params => lat="+coord.getLatitude()+",lon="+coord.getLongitude()+",location="+coord.getLocation());
		
		return( coord );
	}
*/
	
	/**
	 * Given an address, determine its latitude and longitude.
	 * @param pClass	Name of Geocoder class to instantiate.
	 * @param pCountry	Country
	 * @param pAddress	Address
	 * @param pCity		City
	 * @param pState	State
	 * @param pZip		Zip code.
	 * @return Coordinate	Coordinate of given address, or null if not found.
	 * @throws	GeocoderException	Problem occurred during geocoding process
	 */
	public Location geocode( String pCountry, String pAddress, String pCity, String pState, String pZip )
		throws GeocoderException
	{
		if ( this.mGeocoder == null ) {
			String geocoderClassStr = System.getProperty("GeocoderClass");		
			if ( geocoderClassStr == null ) {
				throw new GeocoderException("Utility: Unable to find 'GeocoderClass' property.");
			}
			this.mGeocoder = _initializeGeocoder(geocoderClassStr);
			String geoipClassStr = System.getProperty("GeoIPClass");
			if ( geoipClassStr == null ) {
				throw new GeocoderException("Utility: Unable to find 'GeoIPClass' property.");
			}
			this.mGeoIP = _initializeGeocoder(geoipClassStr);
		}
		return( this.mGeocoder.geocode(pCountry,pAddress,pCity,pState,pZip) );
	}

	/**
	 * Given an IP address (in String form), attempt to geolocate it and return a Coordinate
	 * object representing the approximate location of that IP address.
	 * @param 	pIPAddress			IP address to geo-locate
	 * @return	Coordinate			Coordinate of given IP address, or null if not found
	 * @throws	GeocoderException	Problem occurred during geoIP process
	 */
	public Location geoIP( String pIPAddress )
		throws GeocoderException
	{
		return( this.mGeoIP.geoip(pIPAddress) );
	}
	
	/**
	 * Initialize our geocoder.   There is only one geocoder object ever instantiated.  It is
	 * used by the entire system.
	 * @param	pClassName			Name of Geocoder class to instantiate.
	 * @return	Geocoder			An instance of the Geocoder object.
	 * @throws	GeocoderException	Instantiation failure
	 */
	private Geocoder _initializeGeocoder(String pClassName)
		throws GeocoderException
	{
System.err.println("Utility._initializeGeocoder: pClassName="+pClassName);
		try {
			Class coderClass = Class.forName(pClassName);
			System.err.println("Utility._initializeGeocoder: Class.forName successful.");
			Constructor con = coderClass.getConstructor();
			System.err.println("Utility._initializeGeocoder: getConstructor successful.");
			Object o = con.newInstance();
			System.err.println("Utility._initializeGeocoder: newInstance successful.");
			return( (Geocoder)o );
		} catch( NoSuchMethodException nsme ) {
			nsme.printStackTrace(System.err);
			throw new GeocoderException("Utility._initializeGeocoder: Unable to find constructor for class: "+pClassName);
		} catch( ClassNotFoundException cnfe ) {
			cnfe.printStackTrace(System.err);
			throw new GeocoderException("Utility._initializeGeocoder: Unable to find class for: "+pClassName);
		} catch( IllegalAccessException iae ) {
			iae.printStackTrace(System.err);			
			throw new GeocoderException("Utility._initializeGeocoder: IllegalAccessException for: "+pClassName);
		} catch( InstantiationException ie ) {
			ie.printStackTrace(System.err);
			throw new GeocoderException("Utility._initializeGeocoder: InstantiationException for: "+pClassName);
		} catch( InvocationTargetException ite ) {
			try {
				ite.getCause();	
			} catch( Throwable t ) {
				System.err.println(t.getMessage());
				t.printStackTrace(System.err);
				throw new GeocoderException("Utility._initializeGeocoder: InvocationTargetException: "+pClassName);
			}		
		}
		return( null );
	}
	
	

	
	/**
	 * Send an email.
	 * 
	 * @param pSender		Sender's email address
	 * @param pRecipient	Recipients' email addresses
	 * @param pSubject		Subject line
	 * @param pMessage		Message to send.
	 */
	public static void sendMail( String pSender, Vector<String> pRecipients, String pSubject, String pMessage )
	{
/*
		String host = System.getProperty("mail.smtp.host");
		if ( host == null ) {
			System.err.println("Utility.sendMail: ERROR: Property mail.smtp.host is not set.  Put this into web.xml.");
			return;
		}
		String debug = System.getProperty("mail.debug");
		if ( debug == null ) {
			debug = "false";
		}
*/
		Properties props = new Properties();
		Properties sysprops = System.getProperties();
		Enumeration en = sysprops.propertyNames();
		while( en.hasMoreElements() ) {
			String name = (String)en.nextElement();
			if ( name.startsWith("mail.smtp") ) {
				props.put(name,sysprops.getProperty(name));
			}
		}
/*
		props.put("mail.smtp.host", host);
		props.put("mail.debug", debug);
*/
		// Get a session
		if ( mailSession == null ) {
			mailSession = Session.getInstance(props);
		}

		try {
			// Get a Transport object to send e-mail
			Transport bus = mailSession.getTransport("smtp");

			// Connect only once here
			// Transport.send() disconnects after each send
			// Usually, no username and password is required for SMTP
//			bus.connect();
			String mailhost = (String)props.get("mail.smtp.host");
			String mailuser = (String)props.get("mail.smtp.user");
			String mailpw = (String)props.get("mail.smtp.password");
System.err.println("Utility.sendMail: "+mailhost+", mailbox: "+mailuser+", pw: "+mailpw);
			if ( (mailhost != null) && (mailhost.length() > 0) && (mailuser != null) && (mailuser.length() > 0) && (mailpw != null) && (mailpw.length() > 0) ) {
				System.err.println("Sending mail via host: "+mailhost+", mailbox: "+mailuser+", pw: "+mailpw);
				bus.connect(mailhost, mailuser, mailpw);
			} else {
				bus.connect();			
			}

			// Instantiate a message
			Message msg = new MimeMessage(mailSession);
			
System.err.println("sendMail: sender="+pSender+", # of recipients="+pRecipients.size());
			if ( pRecipients.size() == 0 ) {
				System.err.println("Utility.sendMail: no recipients specified");
				return;
			}
			// Set message attributes
			msg.setFrom(new InternetAddress(pSender));
			InternetAddress[] addresses = new InternetAddress[pRecipients.size()];
			Iterator<String> it = pRecipients.iterator();
			for( int i=0; it.hasNext(); i++ ) {
				String recip = it.next();
				addresses[i] = new InternetAddress(recip);
			}
			msg.setRecipients(Message.RecipientType.TO, addresses);
			// Parse a comma-separated list of email addresses. Be strict.
//			msg.setRecipients(Message.RecipientType.CC,
//					InternetAddress.parse(pRecipient, true));
			// Parse comma/space-separated list. Cut some slack.
//			msg.setRecipients(Message.RecipientType.BCC,
//					InternetAddress.parse(pRecipient, false));

			msg.setSubject(pSubject);
			msg.setSentDate(new Date());

			//
			// Set the actual message.
			//
			msg.setContent(pMessage, "text/plain");
			msg.saveChanges();

			//
			// Send it on its way.
			//
			bus.sendMessage(msg, addresses);
			bus.close();
		} catch (MessagingException mex) {
			// Prints all nested (chained) exceptions as well
			mex.printStackTrace(System.err);
			// How to access nested exceptions
			while (mex.getNextException() != null) {
				// Get next exception in chain
				Exception ex = mex.getNextException();
				ex.printStackTrace();
				if ( ! (ex instanceof MessagingException) ) { 
					break;
				} else {
					mex = (MessagingException)ex;
				}
			}
		}		
	}

	/**
	 * Generate a new password with random alpha-numeric characters.
	 * @param length	Length of new password.
	 * @return String	New password.
	 */
	public static String generateRandomPassword( int length ) {
		Random rand = new Random(System.currentTimeMillis());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int pos = rand.nextInt(charset.length());
			sb.append(charset.charAt(pos));
		}
		return sb.toString();
	}		

	/**
	 * Convert a string of the form <MM/DD/YYYY> into a java.sql.Timestamp object.
	 * @param	pDateStr		Date to convert.
	 * @return	Timestamp		Timestamp object.
	 * @exception	IllegalArgumentException	Input was not in the form MM/DD/YYYY
	 */
	public static Timestamp datestrToTimestamp( String pDateStr ) 
		throws IllegalArgumentException
	{
//System.err.println("Utility.datestrToTimestamp: converting "+pDateStr);
		
		Timestamp ts = null;
		if ( (pDateStr != null) && (pDateStr.length() > 0) ) {						
			String[] parts = pDateStr.split("/");
			if ( (parts == null) || (parts.length < 3) ) {
				System.err.println("Utility.datestrToTimestamp: invalid date format: "+pDateStr);
				throw new IllegalArgumentException("invalid date format.  not in the form MM/DD/YYYY: "+pDateStr);
			} else {
				String _mm = parts[0];
				String _dd = parts[1];
				String _yyyy = parts[2];
				int mm, dd, yyyy;
				try {
					mm = Integer.parseInt(_mm);
					dd = Integer.parseInt(_dd);
					yyyy = Integer.parseInt(_yyyy);
//System.err.println("Utility.datestrToTimestamp: mm="+mm+", dd="+dd+", yyyy="+yyyy);
					ts = new Timestamp(yyyy-1900, mm-1, dd, 0, 0, 0, 0);
				} catch( NumberFormatException e ) {
					System.err.println("Utility.datestrToTimestamp: invalid date non-numeric: "+pDateStr);
					throw new IllegalArgumentException("invalid date format.  found a non-numeric value: "+pDateStr);
					
				}
			}
		}	
		return( ts );		
	}

	/**
	 * Convert a java.sql.Timestamp into a Hoorate standard String form of the date. MM/DD/YYYY
	 * @param	pTimestamp		Timestamp to convert to String
	 * @return	String			Converted timestamp.
	 */
	public static String timestampToDatestr( Timestamp pTimestamp ) 
	{
		String _date = "";
		if ( pTimestamp != null ) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(pTimestamp);
			_date = (cal.get(Calendar.MONTH)+1) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR);
		}
		return( _date);		
	}	

	/**
	 * Given a string representing time in HH:MM <am|pm> format, return 24-hour format (HH:MM)
	 * @param pTime
	 * @return	Normalized time
	 */
	public static String normalizeTime( String pTime )
		throws NumberFormatException
	{
		int amloc = pTime.indexOf(" am");
		if ( amloc == -1 ) {
			amloc = pTime.indexOf(" AM");
		}
		int pmloc = pTime.indexOf(" pm");
		if ( pmloc == -1 ) {
			pmloc = pTime.indexOf(" PM");
		}		
		//
		// If there is no am or pm, then we're already normalized.
		//
		if ( (amloc == -1) && (pmloc == -1) ) {
			return( pTime );
		}
		String time;
		if ( pmloc != -1 ) {
			time = pTime.substring(0,pmloc);
		} else {
			time = pTime.substring(0,amloc);
		}		
		int colon = time.indexOf(":");
		String lhs = time.substring(0,colon);
		String rhs = time.substring(colon+1,time.length());
		int lhsint = Integer.parseInt(lhs);
		int rhsint = Integer.parseInt(rhs);
		if ( (amloc != -1) && (lhsint == 12) ) {
			lhsint = 0;
		} else if ( (pmloc != -1) && (lhsint < 12) ) {
			lhsint += 12;
		}
		String intermediate = lhsint + ":" + rhsint;
		Time ts = new Time(lhsint,rhsint,0);
		SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
		String result = formatter.format( ts );		
		return( result );
	}
	
	/**
	 * Get the Logger singleton object.
	 * @return	Logger		Log4j logger
	 */
	public static Logger getLogger() {
		return( logger );
	}
	
	private static Logger logger;
	
	static {
//		PropertyConfigurator.configure("log4j.properties");
		logger = Logger.getLogger("eventengine.server");
	}
	private static Session mailSession = null;

	static final String charset = "!&0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";	
	private Geocoder mGeocoder;
	private Geocoder mGeoIP;	
}
