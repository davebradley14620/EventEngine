package eventengine.server;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import javax.sql.rowset.serial.SerialBlob;
import javax.naming.*;

import org.apache.commons.lang.StringEscapeUtils;





public enum Model {
	instance;
	
/*
	private Model() {

//		Location loc1 = new Location( 47.156, 72.595, "United States", "40 Pinehill Drive", "Greenwood Lake", "NY", "10925" );
//		Event event1 = new Event(1, "Event 1", "This is event 1", new Date(), new Date(), 120, false, null, loc1);
//		event1.setDescription("Read http://www.vogella.com/articles/REST/article.html");
//		contentProvider.put(1, event1);
//		Location loc2 = new Location( 45.129, 73.000, "United States", "1937 Factors Walk", "Ionia", "NY", "14475" );
//		Event event2 = new Event(2, "Event 2", "This is event 2", new Date(), new Date(), 120, false, null, loc2);
//		event2.setDescription("Read complete http://www.vogella.com");
//		contentProvider.put(2, event2);
		this.mUtility = new Utility();
		try {
			String jndiName = "jdbc/EventEngine";
			//Properties p = new Properties();
			//p.put("java.naming.factory.initial", "com.sun.jndi.cosnaming.CNCtxFactory");
			//p.put("java.naming.provider.url","java:comp/env");
	        Context initContext = new InitialContext();
	        Context envContext  = (Context)initContext.lookup("java:comp/env");
	        if ( envContext == null ) {
	        	Utility.getLogger().error("Model: Unable to find initial context.");
	        } else {
		        DataSource ds = (DataSource)envContext.lookup(jndiName);
	        	this.mConnection = ds.getConnection();
	        }
		} catch ( Exception e ) {
			System.err.println("Model: "+e.getMessage());
			e.printStackTrace();
		}
	}
*/
	private Model() {
		try {
	        Context initContext = new InitialContext();
	        Context envContext  = (Context)initContext.lookup("java:/comp/env");
	        this.mDataSource = (DataSource)envContext.lookup(JNDIDatabaseName);
		} catch( NamingException e ) {
			Utility.getLogger().fatal("Model: Unable to instantiate data source: "+e.getMessage());
			e.printStackTrace(System.err);
			System.exit(-1);
		}
	}

	/**
	 * Build an Event object from a ResultSet
	 * @param 	pRS		ResultSet from query into users table
	 * @return	Event		Event object or null
	 * @throws 	SQLException	Database error
	 */
	private Event _buildEvent( ResultSet pRS )
		throws SQLException
	{
		boolean isrepeating = false;
		String c = pRS.getString("isRepeating");
		if ( (c != null) && c.equals("Y") ) {
			isrepeating = true;
		}
		Cycle repeatCycle = null;
		String rc = pRS.getString("repeatCycle");
		if ( (rc != null) && (rc.length() > 0) ) {
			repeatCycle = Cycle.valueOf(rc);
		}
		Location loc = _buildLocation( pRS );		
		int id = pRS.getInt("id");
		List<Category> categories = getCategoriesForEvent( id );
		int participantCount = getUserCountForEvent( id );

		Event event = new Event (id, pRS.getString("name"), pRS.getString("description"), pRS.getTimestamp("created_at"), pRS.getInt("creator"), pRS.getTimestamp("start"), pRS.getInt("durationMinutes"), isrepeating, repeatCycle, loc, categories, participantCount);

		Utility.getLogger().debug("_buildEvent: new Event ("+pRS.getInt("id")+","+ pRS.getString("name")+","+ pRS.getString("description")+","+ pRS.getTimestamp("created_at")+","+pRS.getInt("creator")+","+ pRS.getTimestamp("start")+","+ pRS.getInt("durationMinutes")+","+ isrepeating+","+ repeatCycle+","+ loc + "," + categories + "," + participantCount);

		return( event );		
	}
	
	/**
	 * Build a Location object from the given ResultSet
	 * @param 	pRS				ResultSet from query into users table
	 * @return	Location		Location object or null
	 * @throws 	SQLException	Database error
	 */
	private Location _buildLocation( ResultSet pRS )
		throws SQLException
	{
		Location loc = new Location( pRS.getDouble("latitude"), pRS.getDouble("longitude"), pRS.getString("country"), pRS.getString("address"), pRS.getString("city"), pRS.getString("state"), pRS.getString("zip") );
		Utility.getLogger().debug("_buildLocation: new Coordinate("+pRS.getDouble("latitude")+","+pRS.getDouble("longitude")+","+null+","+pRS.getString("address")+","+pRS.getString("city")+","+pRS.getString("state")+","+pRS.getString("zip")+ ")" );
		return( loc );
	}
	
	/**
	 * Build a User object from a ResultSet
	 * @param 	pRS		ResultSet from query into users table
	 * @return	User		User object or null
	 * @throws 	SQLException	Database error
	 */
	private User _buildUser( ResultSet pRS )
		throws SQLException
	{
		if ( ! pRS.next() ) {
			return( null );
		}
		Location loc = _buildLocation( pRS );
		String termsread = pRS.getString("termsread");
		boolean isTermsRead = false;
		if ( (termsread != null) && termsread.equals("Y") ) {
			isTermsRead = true;
		}
		User user = new User(pRS.getInt("id"), pRS.getString("screenname"), pRS.getString("firstname"), pRS.getString("lastname"), loc, pRS.getString("fbid"), pRS.getString("email"), pRS.getTimestamp("created_at"), pRS.getTimestamp("updated_at"), isTermsRead, pRS.getInt("registercode"));

		Utility.getLogger().debug("_buildUser: new User("+pRS.getInt("id")+","+pRS.getTimestamp("created_at")+","+pRS.getString("firstname")+","+pRS.getString("lastname")+","+pRS.getString("screenname")+","+ loc+","+ pRS.getString("fbid")+","+ pRS.getString("email")+","+isTermsRead+","+pRS.getInt("registercode"));

		return( user );		
	}
	/**
	 * Build a Category object from a ResultSet
	 * @param	pRS				ResultSet from query into categories table
	 * @return	Category		Category object or null
	 * @throws	SQLException	Database error
	 */
	private Category _buildCategory( ResultSet pRS )
		throws SQLException
	{
		if ( ! pRS.next() ) {
			return( null );
		}
		Category category = new Category (pRS.getInt("id"), pRS.getTimestamp("created_at"), pRS.getInt("parent_id"), pRS.getString("name"), pRS.getString("description"));
	
		Utility.getLogger().debug("_buildCategory: new Category ("+pRS.getInt("id")+","+ pRS.getTimestamp("created_at")+","+ pRS.getInt("parent_id")+","+ pRS.getString("name")+","+ pRS.getString("description"));
	
		return( category );		
	}
	
	/**
	 * Get a User object by user ID.
	 * @param 	pUserId		User's ID.
	 * @return	User		User object, or null if not found.
	 * @throws 	SQLException	Database error
	 */
	public User getUserById( int pUserId )
		throws SQLException
	{
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		try {
			String sql = "SELECT *, 0 AS 'distance' FROM users WHERE id = ?";
			conn = getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1,pUserId);			
	    	rs = executeQuery(stmt);
	    	return( _buildUser( rs ) );
		} finally {
			if (rs != null) { 
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			if ( conn != null ) {
				putConnection(conn);
			}
		}	
	}
	
	/**
	 * Get a User object by user ID, determining the distance of this user from
	 * some other user or business whose latitude and longitude are given.
	 * @param 	pUserId		User's ID.
	 * @param 	pLatitude	Latitude of another person or business.
	 * @param 	pLongitude	Longitude of another person or business.
	 * @return	User		User object, or null if not found.
	 * @throws 	SQLException	Database error
	 */
	public User getUserByIdWithDistance( int pUserId, double pLatitude, double pLongitude )
		throws SQLException
	{
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		try {
Utility.getLogger().debug("Model.getUserByIdWithDistance: latitude="+pLatitude+", longitude="+pLongitude);
//			String sql = "SELECT * FROM users WHERE id = ?";
			String sql = "SELECT u.*, 3956 * 2 * ASIN(SQRT( POWER(SIN((? - u.latitude) * pi()/180 / 2), 2) + COS(? * pi()/180) * COS(u.latitude * pi()/180) * POWER(SIN((? - u.longitude) * pi()/180 / 2), 2) )) AS distance FROM users u WHERE u.id=?";
Utility.getLogger().debug("Model.getUserByIdWithDistance: pUserId="+pUserId+", pLatitude="+pLatitude+", pLongitude="+pLongitude+"\nsql="+sql);

			conn = getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.setDouble(1,pLatitude);
			stmt.setDouble(2,pLatitude);
			stmt.setDouble(3,pLongitude);			
			stmt.setInt(4,pUserId);			
	    	rs = executeQuery(stmt);
	    	return( _buildUser( rs ) );

		} finally {
			if (rs != null) { 
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			putConnection(conn);
		}	
	}	
	
	public List<User> getUsers()
		throws SQLException
	{
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		List<User> list = new Vector<User>(1000);
		try {
			conn = getConnection();
			stmt = conn.prepareStatement("SELECT * FROM users");
			rs = executeQuery(stmt);
			while( rs.next() ) {
				User u = _buildUser( rs );
				list.add(u);
			}
		} finally {
			if (rs != null) { 
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			putConnection(conn);
		}	
		return( list );
	}
	
	/**
	 * Get a User object by screen name.
	 * @param 	pScreenName	User's screen name
	 * @return	User		User object, or null if not found.
	 * @throws 	SQLException	Database error
	 */
	public User getUserByScreenName( String pScreenName )
		throws SQLException
	{
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		try {
			String sql = "SELECT *, 0 AS 'distance' FROM users WHERE screenname = ?";
			conn = getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.setString(1,escapeSql(pScreenName));			
	    		rs = executeQuery(stmt);
	    		return( _buildUser( rs ) );
		} finally {
			if (rs != null) { 
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			putConnection(conn);
		}	
	}
	
	/**
	 * Get the number of users in the database.
	 * @return	int				Number of users in the database.
	 * @throws	SQLException	Database error occurred
	 */
	public int getUserCount()
		throws SQLException
	{
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		List<User> list = new Vector<User>(1000);
		try {
			conn = getConnection();
			stmt = conn.prepareStatement("SELECT count(*) FROM users");
			rs = executeQuery(stmt);
			if( rs.next() ) {
				int count = rs.getInt(1);
				return( count );
			}
		} finally {
			if (rs != null) { 
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			putConnection(conn);
		}	
		return( 0 );
	}
	/**
	 * Change a user's password.
	 * 
	 * @param pScreenName		Screenname of user
	 * @param pPassword		Password to change to
	 * @throws SQLException		Database error
	 */
	public void userChangePassword( String pScreenName, String pPassword ) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			String sql = "UPDATE users SET password=PASSWORD(?) WHERE screenname=?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1,escapeSql(pPassword));
			stmt.setString(2,escapeSql(pScreenName));
			executeUpdate(stmt);
		} finally {
			if (rs != null) { 
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			putConnection(conn);
		}			
	}
	/**
	 * Given a screen name and a password, login the specified user and return the User
	 * object, or return null if this is not a user in our database, is not yet fully registered,
	 * or has been disabled.
	 *
	 * @param	pScreenName	- User's screen name 
	 * @param	pPassword	- User's password 
	 * @param	isCheckPassword	- true if we should verify user's password.  false o/w.
	 * @return	User	- User object of specified user, or null if not found. 
	 * @throws	SQLException if database problem.
	 */
	public User userLogin( String pScreenName, String pPassword, boolean isCheckPassword ) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
//Utility.getLogger().debug("Login attempt by user ("+pScreenName+") password ("+pPassword+")");
			String sql = "SELECT * FROM users WHERE screenname=?";
			if ( isCheckPassword ) {
				sql += " AND password=PASSWORD(?) AND registered IS NOT NULL";
			}
			stmt = conn.prepareStatement(sql);
			stmt.setString(1,escapeSql(pScreenName));
			if ( isCheckPassword ) {
				stmt.setString(2,escapeSql(pPassword));
			}
			rs = executeQuery(stmt);
			//
			// If there are no results, then there is no such user
			// with that password in our database.
			//
			if ( ! rs.first() ) {
				Utility.getLogger().debug("Login attempt FAIL for user ("+pScreenName+")");
				return( null );
			}
			//
			// If there are hits, there should be only one.  But, in
			// case, somehow, there are more (there should never be),
			// we only take the first one.
			//
//			HashMap<Integer,Boolean> hoorays = getHooraysForUser( rs.getInt("id") );
			User user = _buildUser( rs ); 
			Utility.getLogger().debug("Login attempt SUCCESS for user ("+pScreenName+")");

			//
			// Update 'last-login' for this user.
			//
			sql = "UPDATE users SET last_login=NOW() WHERE id=?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1,user.getId());
			executeUpdate(stmt);
			
			return( user );
		} finally {
			if (rs != null) { 
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			putConnection(conn);
		 }
	}

	
	/**
	 * Insert/update a user's profile into the users table.
	 * @param		pUser		User object, if edit, or null, if new user.
	 * @param		pPassword	Password, or null if we're not setting it this time.
	 * @return 		User		User object after insertion or update.  This may be altered from the
	 * 							originally passed pUser parameter.
	 * @exception	SQLException	- Database error occurred.
	 */		
	public User updateUser(User pUser, String pPassword) throws SQLException {
		String country = null;
		String address = null;
		String city = null;
		String state = null;
		String zip = null;
		double lat = 0.;
		double lon = 0.;
		Location loc = pUser.getLocation();
		if ( loc != null ) {
			country = loc.getCountry();
			address = loc.getAddress();
			city = loc.getCity();
			state = loc.getState();
			zip = loc.getZip();
			lat = loc.getLatitude();
			lon = loc.getLongitude();
			//
			// If we have no latitude and longitude computed, and we DO have
			// a valid piece of an address, then compute the latitude and longitude.
			//
			if ( ((lat == 0.) && (lon == 0.)) &&
					((city != null) || (state != null) || (zip != null))
				) {
				try {					
					loc = this.mUtility.geocode(country,address,city,state,zip);
					if ( loc != null ) {
						address = loc.getAddress();
						city = loc.getCity();
						state = loc.getState();
						zip = loc.getZip();
						lat = loc.getLatitude();
						lon = loc.getLongitude();							
					} else {
						Utility.getLogger().debug("updateUser: ERROR trying to determine lat/lon for "+address+","+city+","+state+","+zip);
					}
				} catch( GeocoderException hrge ) {
					hrge.printStackTrace(System.err);
				}
			}
		}
		boolean haspw = false;
		if ( (pPassword != null) && (pPassword.length() > 0) && (! pPassword.equals(DUMMY_PASSWORD)) ) {
			haspw = true;
		}

		boolean newuser = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			
			//
			// If we have a user ID greater than 0, then it's an existing user's
			// profile we're updating.  Otherwise it's a new user registration.
			//
			String sql = "INSERT INTO users (created_at, fbid, screenname, firstname, lastname, country, address, city, state, zip, email, latitude, longitude, termsread, registercode ) VALUE(NOW(),?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			if ( haspw ) {
				sql = "INSERT INTO users (created_at, fbid, screenname, firstname, lastname, country, address, city, state, zip, email, latitude, longitude, termsread, registercode, password ) VALUE(NOW(),?,?,?,?,?,?,?,?,?,?,?,?,?,?,PASSWORD(?))";
			}

			if ( pUser.getId() > 0 ) {
					newuser = true;
					sql = "UPDATE users SET updated_at=NOW(), fbid=?, screenname=?,firstname=?,lastname=?,country=?,address=?,city=?,state=?,zip=?,email=?,latitude=?,longitude=?,termsread=?,registercode=?";
					if ( haspw ) {
						sql += ",password=PASSWORD(?)";
					}
					sql += " WHERE id = ?";
			}

			int idx=1;
			conn = getConnection();
			stmt = conn.prepareStatement(sql);
			if ( pUser.getFacebookId() == null ) {
				stmt.setNull(idx++,java.sql.Types.VARCHAR);
			} else {
				stmt.setString(idx++,escapeSql(pUser.getFacebookId()));									
			}
			if ( pUser.getScreenName() == null ) {
				stmt.setNull(idx++,java.sql.Types.VARCHAR);
			} else {
				stmt.setString(idx++,escapeSql(pUser.getScreenName()));					
			}
			if ( pUser.getFirstName() == null ) {
				stmt.setNull(idx++,java.sql.Types.VARCHAR);
			} else {
				stmt.setString(idx++,escapeSql(pUser.getFirstName()));					
			}
			if ( pUser.getLastName() == null ) {
				stmt.setNull(idx++,java.sql.Types.VARCHAR);
			} else {
				stmt.setString(idx++,escapeSql(pUser.getLastName()));					
			}
			if ( country == null ) {
				stmt.setNull(idx++,java.sql.Types.VARCHAR);
			} else {
				stmt.setString(idx++,escapeSql(country));
			}
			if ( address == null ) {
				stmt.setNull(idx++,java.sql.Types.VARCHAR);
			} else {
				stmt.setString(idx++,escapeSql(address));
			}
			if ( city == null ) {
				stmt.setNull(idx++,java.sql.Types.VARCHAR);
			} else {
				stmt.setString(idx++,escapeSql(city));
			}
			if ( state == null ) {
				stmt.setNull(idx++,java.sql.Types.VARCHAR);
			} else {
				stmt.setString(idx++,escapeSql(state));
			}
			if ( zip == null ) {
				stmt.setNull(idx++,java.sql.Types.VARCHAR);
			} else {
				stmt.setString(idx++,escapeSql(zip));
			}
			if ( pUser.getEmailAddress() == null ) {
				stmt.setNull(idx++,java.sql.Types.VARCHAR);					
			} else {
				stmt.setString(idx++,escapeSql(pUser.getEmailAddress()));
			}
			stmt.setDouble(idx++, lat);
			stmt.setDouble(idx++, lon);
			if ( pUser.getIsTermsRead() ) {
				stmt.setString(idx++, "Y");
			} else {
				stmt.setString(idx++, "N");				
			}
			stmt.setInt(idx++, pUser.getRegistrationCode());
			if ( haspw ) {
				stmt.setString(idx++,escapeSql(pPassword));
			}
			if ( newuser ) {
				stmt.setInt(idx++,pUser.getId());
			}
			executeUpdate(stmt);				
			//
			// If this was a newly created event, update our Event object with our new 
			// ID assigned by the database.
			//			
			if ( pUser.getId() <= 0 ) {
				sql = "SELECT LAST_INSERT_ID() FROM users";
				stmt = conn.prepareStatement(sql);			
				ResultSet rs = executeQuery(stmt);
				int user_id = -1;
				if( rs.next() ) {
					user_id = rs.getInt(1);
				}
				pUser.setId(user_id);
			}				
			Utility.getLogger().debug("updateUser: SUCCESS!");
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			putConnection(conn);
		}		
		return( pUser );
	}
	
	/**
	 * Create/update a User profile to the database.
	 * @param	pUser		User object to create/update
	 * @return	User		User object after insertion into database.  This may be
	 * 						altered from the originally passed User object.
	 * @throws SQLException	Database error occurred
	 */
	public User updateUser(User pUser)
		throws SQLException
	{
			return( updateUser(pUser, null ) );
	}
		
	/**
	 * Register a new user.   The user gets an email after he's filled out the registration
	 * form.  The email contains a link back to a servlet on Hoorate.  It also contains the
	 * userId and a registrationCode.   If the userId and registration code match what's in the
	 * database for this user, then this will do the final registration step.  This consists
	 * of setting the 'registered' column to the current time and date.
	 * 
	 * @param pUserId			User's ID
	 * @param pRegistrationCode		Registration code assigned to this user.
	 * @throws SQLException			Database error OR incorrect userid or registration code.
	 */
	public void registerUser( int pUserId, int pRegistrationCode ) 
		throws SQLException
	{
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			//
			// If we have a user object passed in, then this is an update of an
			// existing user's profile.  Otherwise it's a new user registration.
			//
			conn = getConnection();
			String sql = "UPDATE users SET registered=NOW() WHERE id=? AND registercode=?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1,pUserId);
			stmt.setInt(2,pRegistrationCode);
			executeUpdate(stmt);				
			Utility.getLogger().debug("registerUser: userId="+pUserId+": SUCCESS!");
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			putConnection(conn);
		}		
	}	

	/**
	 * Delete the user for the given ID.
	 * @param	pId				User ID
	 * @throws	SQLException	Database error occurred
	 */
	public void removeUser( int pId )
		throws SQLException
	{
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = getConnection();
			String sql = "DELETE FROM users WHERE id=?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1,pId);
			executeUpdate(stmt);
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			putConnection(conn);
		}		
	}
	
	/**
	 * Get an Event object by user ID.
	 * @param 	pId				Event ID.
	 * @return	Event			Event object, or null if not found.
	 * @throws 	SQLException	Database error
	 */
	public Event getEventById( int pId )
		throws SQLException
	{
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		try {
			String sql = "SELECT * FROM events WHERE id = ?";
			conn = getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1,pId);			
	    	rs = executeQuery(stmt);
	    	Event e = null;
	    	if ( rs.next() ) {
	    		e = _buildEvent( rs );
	    	}
	    	return( e );
		} finally {
			if (rs != null) { 
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			putConnection(conn);
		}	
	}	

	/**
	 * Get list of events being attended by given user.  If isExpired is true, this will also return
	 * events attended by this user in the past.
	 * @param 	pUserId			User ID.
	 * @param	isExpired		If true, also return past events attended by this user.
	 * @return	List<Event>		List of Event objects being attended by given User.
	 * @throws 	SQLException	Database error
	 */
	public List<Event> getEventsJoinedByUser( int pUserId, boolean isExpired )
		throws SQLException
	{
		String dateConstraint = "";
		if ( ! isExpired ) {
			dateConstraint = "AND start > NOW()";
		}
		List<Event> events = new Vector<Event>(3);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM events WHERE id IN (SELECT eventid FROM event2users WHERE userid=?) "+dateConstraint+";";
			conn = getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1,pUserId);			
	    	rs = executeQuery(stmt);
	    	while( rs.next() ) {
	    		Event ev = _buildEvent( rs );
	    		events.add(ev);
	    	}
	    	return( events );
		} finally {
			if (rs != null) { 
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			putConnection(conn);
		}	
	}		
	
	/**
	 * Join a user to an event.
	 * @param pEventId			ID of event to join
	 * @param pUserId			ID of user to join to event
	 * @throws SQLException		Database error occurred
	 */
	public Event joinEvent( int pEventId, int pUserId ) 
		throws SQLException
	{
		Event event = getEventById(pEventId);
		if ( event == null ) {
			throw new SQLException("Model.joinEvent: No event found with id: "+pEventId);
		}
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			//
			// If we have a user object passed in, then this is an update of an
			// existing user's profile.  Otherwise it's a new user registration.
			//
			String sql = "INSERT INTO event2users (eventid,userid) VALUES(?,?)";
			conn = getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1,pEventId);
			stmt.setInt(2,pUserId);
			executeUpdate(stmt);				
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			putConnection(conn);
		}		
		return( event );
	}	
	
	
	/**
	 * Get list of events around the given point on the earth within the given radius.  If isExpired is true, this will also return
	 * past events in this area.
	 * @param 	pLatitude		Latitude
	 * @param	pLongitude		Longitude
	 * @param	pRadiusMiles	Radius (in miles) to search
	 * @param	isExpired		If true, also return past events in this area.
	 * @param 	pCategoryId		If not set to 0, then constrain the results to the given category.
	 * @param 	pStartDate		Find events no earlier than startDate.  This is in # of seconds since 1/1/70.
	 * @param 	pEndDate		Find events no later than endDate. This is in # of seconds since 1/1/70.
	 * @param 	pKeyword		If non-null, search for events by the given keyword in the given area.
	 * @param	pCountOffset	Offset in the resultSet to start
	 * @param	pCountLimit		Maximum number of results to return.
	 * @return	List<Event>		List of Event objects.
	 * @throws 	SQLException	Database error
	 */
	public List<Event> getEventsByLocation( double pLatitude, double pLongitude, int pRadiusMiles, boolean isExpired, int pCategoryId,
			int pStartDate, int pEndDate, String pKeyword, int pCountOffset, int pCountLimit)
		throws SQLException
	{
		String dateConstraint = "";
		if ( ! isExpired ) {
			dateConstraint = " AND e.start > NOW() ";
		}
		if ( pStartDate > 0 ) {
			dateConstraint += " AND UNIX_TIMESTAMP(e.start) >= "+pStartDate;
		}
		if ( pEndDate > 0 ) {
			dateConstraint += " AND UNIX_TIMESTAMP(e.start) < "+pEndDate;
		}
		List<Event> events = new Vector<Event>(20);
		

		String tables = " events e ";
		
		String catConstraint = "";
		String searchConstraint = "";
		if ( pCategoryId > 0 ) {
			tables += " LEFT JOINT event2categories ec ON e.id = ec.eventid LEFT JOIN categories c ON c.id = ec.categoryid";
			//catConstraint = " AND e.id = ec.eventid AND c.id = ec.categoryid ";
		}
		if ( (pKeyword != null) && (pKeyword.length() > 0) && (! pKeyword.equals("\"\"")) && (! pKeyword.equals("''")) ) {
			String tmp = StringEscapeUtils.escapeSql(pKeyword);
			tables += " LEFT JOIN event2metadata emd ON e.id = emd.eventid";
			String catSearchConstraint = "";
			if ( pCategoryId > 0 ) {
				catSearchConstraint = " OR (c.name RLIKE '"+tmp+"') OR (c.description RLIKE '"+tmp+"') ";
			}
			searchConstraint = " AND ( (e.name RLIKE '"+tmp+"') OR (e.description RLIKE '"+tmp+"') OR (emd.keystr RLIKE '"+tmp+"') "+catSearchConstraint+" ) ";				
		}
			
		String limitConstraint = "";
		if ( pCountLimit > -1 ) {
			limitConstraint = " LIMIT "+ pCountOffset + "," + pCountLimit;
		}
		//
		// calculate lon and lat for the rectangle:
		// This will be used as a bounding box around the
		// great circle.  The purpose is to limit our search
		// to only events falling in the box, which should
		// significantly speed up the query.
		//
		double lon1 = pLongitude-pRadiusMiles/Math.abs(Math.cos(Math.toRadians(pLatitude))*69);
		double lon2 = pLongitude+pRadiusMiles/Math.abs(Math.cos(Math.toRadians(pLatitude))*69);
		double lat1 = pLatitude-(((double)pRadiusMiles)/69.0);
		double lat2 = pLatitude+(((double)pRadiusMiles)/69.0);

		//
		// Craft the SQL for the search.
		//
//		" 	3956 * 2 * ASIN(SQRT( POWER(SIN(("+pLatitude+" - e.latitude) * pi()/180 / 2), 2) + COS("+pLatitude+" * pi()/180) * COS(e.latitude * pi()/180) * POWER(SIN(("+pLongitude+" - e.longitude) * pi()/180 / 2), 2) ))  "+
//		" AS "+
//		"	distance,"+
//		" 	c.id "+
//		" AS "+
//		" 	catid "+		
		String sql =
			" SELECT " +
			"	e.*,"+
			" 	3956 * 2 * ASIN(SQRT( POWER(SIN(("+pLatitude+" - e.latitude) * pi()/180 / 2), 2) + COS("+pLatitude+" * pi()/180) * COS(e.latitude * pi()/180) * POWER(SIN(("+pLongitude+" - e.longitude) * pi()/180 / 2), 2) ))  "+
			" AS "+
			"	distance"+			
			" FROM "+
			tables +
			" WHERE "+
			"	e.latitude IS NOT NULL "+
			" AND "+
			"	e.longitude IS NOT NULL "+
			" AND "+
			"	e.latitude >= ? "+
			" AND "+
			"	e.latitude <= ? "+
			" AND "+
			"	e.longitude >= ? "+
			" AND "+
			"	e.longitude <= ? "+
			dateConstraint +
			catConstraint +
			searchConstraint +
			" ORDER BY distance " +			
			limitConstraint;
		
//System.err.println("_queryLocalVendors: sql = "+sql+"\nlat1="+lat1+", lat2="+lat2+", lon1="+lon1+", lon2="+lon2);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		try {
			conn = getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.setDouble(1,lat1);
			stmt.setDouble(2,lat2);
			stmt.setDouble(3,lon1);
			stmt.setDouble(4,lon2);
	
			rs = executeQuery(stmt);
	//System.err.println("_queryLocalVendors: sql = "+stmt.toString());
			while( rs.next() ) {
				Event ev = _buildEvent(rs);
	//System.err.println("Model.getEventsByLocation: found event: "+ev);
				events.add( ev );
			}
			return( events );
		} finally {
			if (rs != null) { 
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			putConnection(conn);			
		}
	}			
	
	/**
	 * Get list of events created by the given user.   If isExpired is true, this will also
	 * return events that were created by this user that have already come and gone.
	 * @param pUserId		User ID
	 * @param isExpired		If true, also return expired events created by this user.
	 * @return List<Event>	List of events created by this user.
	 * @throws SQLException	Database error occurred.
	 */
	public List<Event> getEventsCreatedByUser( int pUserId, boolean isExpired )
		throws SQLException
	{
		String dateConstraint = "";
		if ( ! isExpired ) {
			dateConstraint = "AND start > NOW()";
		}
		List<Event> events = new Vector<Event>(3);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		try {
			conn = getConnection();
			String sql = "SELECT * FROM events WHERE creator=? "+dateConstraint+";";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1,pUserId);			
	    	rs = executeQuery(stmt);
	    	while( rs.next() ) {
	    		Event ev = _buildEvent( rs );
	    		events.add(ev);
	    	}
	    	return( events );
		} finally {
			if (rs != null) { 
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			putConnection(conn);
		}	
	}			
	
	/**
	 * Return the total number of events in the database.
	 * @return int				Count of events
	 * @throws SQLException		Database error occurred
	 */
	public int getEventCount()
		throws SQLException
	{
    	int count=0;
    	Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		try {
			String sql = "SELECT COUNT(*) FROM events";
			conn = getConnection();
			stmt = conn.prepareStatement(sql);
	    	rs = executeQuery(stmt);
	    	if ( rs.next() ) {
	    		count = rs.getInt(1);
	    	}
		} finally {
			if (rs != null) { 
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			putConnection(conn);
		}	
    	return( count );
	}

	/**
	 * Return true if the given id is a valid event id.
	 * @param pId			Event ID to check.
	 * @return	boolean		true if given id is a valid event.
	 * @throws SQLException	Database error occurred
	 */
	public boolean isValidEventId( int pId )
		throws SQLException
	{
    	int count=0;
    	Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		try {
			conn = getConnection();
			String sql = "SELECT COUNT(*) FROM events WHERE id=?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1,pId);			
			
	    	rs = executeQuery(stmt);
	    	if ( rs.next() ) {
	    		count = rs.getInt(1);
	    	}
		} finally {
			if (rs != null) { 
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			putConnection(conn);
		}	
    	return( count > 0 );		
	}
	
	/**
	 * Create or update an event.
	 * @param pEvent		Event object containing event information to create or update.
	 * @returns	Event		Event object, updated with a new ID on insert, unmodified on update.
	 * @throws SQLException	Database error occurred.
	 */
	public Event updateEvent( Event pEvent )
		throws SQLException
	{
		Utility.getLogger().debug("Model.updateEvent: event = "+pEvent);

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			//
			// If the given Event object has an id less than 0, then this is a newly created
			// event.   Otherwise it's an update to an existing Event.
			//
			String sql = "UPDATE events SET updated_at=NOW(), name=?,description=?,creator=?,start=?,durationMinutes=?,isRepeating=?,repeatCycle=?,country=?,address=?,city=?,state=?,zip=?,latitude=?,longitude=? WHERE id=?";
			if ( pEvent.getId() <= 0 ) {
				sql = "INSERT INTO events (created_at, name, description, creator, start, durationMinutes, isRepeating, repeatCycle, country, address, city, state, zip, latitude, longitude) VALUE(NOW(),?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			}
			int idx=1;
			conn = getConnection();
			stmt = conn.prepareStatement(sql);
			if ( pEvent.getName() == null ) {
				stmt.setNull(idx++,java.sql.Types.VARCHAR);
			} else {
				stmt.setString(idx++, escapeSql(pEvent.getName()));
			}
			if ( pEvent.getDescription() == null ) {
				stmt.setNull(idx++,java.sql.Types.VARCHAR);
			} else {
				stmt.setString(idx++, escapeSql(pEvent.getDescription()));
			}
			stmt.setInt(idx++, pEvent.getCreatorUser());
			if ( pEvent.getStartTime() == null ) {
				stmt.setNull(idx++,java.sql.Types.DATE);
			} else {
				stmt.setDate(idx++, new java.sql.Date(pEvent.getStartTime().getTime()));
			}
			stmt.setInt(idx++, pEvent.getDurationMinutes());
			if ( pEvent.getIsRepeating() ) {
				stmt.setString(idx++, "Y");
			} else {
				stmt.setString(idx++, "N");
			}
			if ( pEvent.getRepeatCycle() == null ) {
				stmt.setNull(idx++,java.sql.Types.VARCHAR);
			} else {
				stmt.setString(idx++, escapeSql(pEvent.getRepeatCycle().toString()) );
			}
			String country = null;
			String address = null;
			String city = null;
			String state = null;
			String zip = null;
			double lat = 0.;
			double lon = 0.;
			Location loc = pEvent.getLocation();
			if ( loc != null ) {
				country = loc.getCountry();
				address = loc.getAddress();
				city = loc.getCity();
				state = loc.getState();
				zip = loc.getZip();
				lat = loc.getLatitude();
				lon = loc.getLongitude();
				//
				// If we have no latitude and longitude computed, and we DO have
				// a valid piece of an address, then compute the latitude and longitude.
				//
				if ( ((lat == 0.) && (lon == 0.)) &&
						((city != null) || (state != null) || (zip != null))
					) {
					try {					
						loc = this.mUtility.geocode(country,address,city,state,zip);
						if ( loc != null ) {
							address = loc.getAddress();
							city = loc.getCity();
							state = loc.getState();
							zip = loc.getZip();
							lat = loc.getLatitude();
							lon = loc.getLongitude();							
						} else {
							Utility.getLogger().debug("updateEvent: ERROR trying to determine lat/lon for "+address+","+city+","+state+","+zip);
						}
					} catch( GeocoderException hrge ) {
						hrge.printStackTrace(System.err);
					}
				}
			}
			if ( country == null ) {
				stmt.setNull(idx++,java.sql.Types.VARCHAR);
			} else {
				stmt.setString(idx++,escapeSql(country));
			}
			if ( address == null ) {
				stmt.setNull(idx++,java.sql.Types.VARCHAR);
			} else {
				stmt.setString(idx++,escapeSql(address));
			}
			if ( city == null ) {
				stmt.setNull(idx++,java.sql.Types.VARCHAR);
			} else {
				stmt.setString(idx++,escapeSql(city));
			}
			if ( state == null ) {
				stmt.setNull(idx++,java.sql.Types.VARCHAR);
			} else {
				stmt.setString(idx++,escapeSql(state));
			}
			if ( zip == null ) {
				stmt.setNull(idx++,java.sql.Types.VARCHAR);
			} else {
				stmt.setString(idx++,escapeSql(zip));
			}
			stmt.setDouble(idx++, lat);
			stmt.setDouble(idx++, lon);
			if ( pEvent.getId() > 0 ) {
				stmt.setInt(idx++,pEvent.getId());
			}
			executeUpdate(stmt);	
			//
			// If this was a newly created event, update our Event object with our new 
			// ID assigned by the database.
			//		
			if ( pEvent.getId() <= 0 ) {
				sql = "SELECT LAST_INSERT_ID() FROM events";
				stmt = conn.prepareStatement(sql);			
				rs = executeQuery(stmt);
				int event_id = -1;
				if( rs.next() ) {
					event_id = rs.getInt(1);
				}
				pEvent.setId(event_id);
			}
		} finally {
			if (rs != null) { 
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			putConnection(conn);
		}			
		return( pEvent );
	}
	
	/**
	 * Delete the event for the given ID.
	 * @param	pId				Event ID
	 * @throws	SQLException	Database error occurred
	 */
	public void removeEvent( int pId )
		throws SQLException
	{
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			String sql = "DELETE FROM events WHERE id=?";
			conn = getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1,pId);
			executeUpdate(stmt);
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			putConnection(conn);
		}		
	}

	/**
	 * Get the categories to which an event belongs.
	 * @param	pEventId		Event ID
	 * @return	List<Category>	List of categories to which this event belongs.
	 * @throws	SQLException	Database error occurred.
	 */
	public List<Category> getCategoriesForEvent( int pEventId )
		throws SQLException
	{
		Vector<Category> list = new Vector<Category>(1);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		try {
			String sql = "SELECT categoryid FROM event2category WHERE eventid=?";
			conn = getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1,pEventId);
	    	rs = executeQuery(stmt);
	    	while ( rs.next() ) {
	    		int catid = rs.getInt(1);
	    		Category c = getCategoryById( catid );
	    		list.add(c);
	    	}
		} finally {
			if (rs != null) { 
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			putConnection(conn);
		}	
		return( list );		
	}
	
	/**
	 * Get the current total number of contracts for an event.
	 * @param	pEventId			Event ID of event for which to get contract count
	 * @return	int					Contract count
	 * @throws	SQLException		Database error occurred
	 */
	public int getUserCountForEvent( int pEventId )
		throws SQLException
	{
		int count=0;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		try {
			conn = getConnection();
			String sql = "SELECT COUNT(*) FROM event2users WHERE eventid=?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1,pEventId);			
			
	    	rs = executeQuery(stmt);
	    	if ( rs.next() ) {
	    		count = rs.getInt(1);
	    	}
		} finally {
			if (rs != null) { 
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			putConnection(conn);
		}	
		return( count );		
	}
	
	/**
	 * Get a list of friends, for the given user, attending the given event.
	 * @param	pEventId		Event ID
	 * @param	pUserId			User ID for which to look for friends attending event.
	 * @return	List<User>		List of friends attending event.
	 * @throws SQLException
	 */
	public List<User> getFriendsForEvent( int pEventId, int pUserId )
		throws SQLException
	{
		Vector<User> list = new Vector<User>(10);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		try {
			String sql = "SELECT userid FROM event2users WHERE eventid=? AND userid IN (SELECT friendid FROM user2friends WHERE userid=?) OR userid IN (SELECT userid FROM user2friends WHERE friendid=?);";
			conn = getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1,pEventId);
			stmt.setInt(2, pUserId);
			stmt.setInt(3, pUserId);
	    	rs = executeQuery(stmt);
	    	while ( rs.next() ) {
	    		int userid = rs.getInt(1);
	    		User u = getUserById( userid );
	    		list.add(u);
	    	}
		} finally {
			if (rs != null) { 
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			putConnection(conn);
		}	
		return( list );		
	}
	
	/**
	 * Build a Category object for the given category ID.
	 * @param	pId				Category ID
	 * @return	Category		Category object, or null if not found
	 * @throws	SQLException	Database error
	 */
	public Category getCategoryById( int pId )
		throws SQLException
	{
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		try {
			String sql = "SELECT * FROM categories WHERE id = ?";
			conn = getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1,pId);			
	    	rs = executeQuery(stmt);
	    	return( _buildCategory( rs ) );
		} finally {
			if (rs != null) { 
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			putConnection(conn);
		}	
	}
	
	/**
	 * Given an address, make an attempt to geocode the address into a latitude and longitude.
	 * Any component of the address may be empty, but if they're all empty, it definitely won't
	 * come up with anything.
	 * @param	pCountry	country (or null or empty for United States)
	 * @param 	pAddress	address (or empty)
	 * @param 	pCity		city (or empty)
	 * @param 	pState		state (or empty)
	 * @param 	pZip		zip (or empty)
	 * @return	Location	Location, or null if not found.
	 */
	public Location geocode( String pCountry, String pAddress, String pCity, String pState, String pZip  ) {
		Location latlon = null;
		try {					
			latlon = this.mUtility.geocode(pCountry,pAddress,pCity,pState,pZip);
		} catch( GeocoderException hrge ) {
			hrge.printStackTrace(System.err);
		}
		return( latlon );
	}

	/**
	 * Given an IP address, return its approximate location in a Coordinate object, or null
	 * if the location couldn't be determined.
	 * @param 	pIPAddress	IP Address to geoIP
	 * @return	Coordinate	Coordinate of location, or null if not found
	 * @throws	SQLException	Database error occurred.
	 */
	public Location geoIP( String pIPAddress )
		throws SQLException
	{
		//
		// Skip the pathological cases.
		//
		if ( pIPAddress.equals("127.0.0.1") || pIPAddress.equals("localhost") || pIPAddress.startsWith("192.168") || pIPAddress.startsWith("10.") ) {
			return( null );
		}
		Location loc = null;
/*
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		try {
//			String sql = "SELECT l.country,l.city,l.region,l.postalcode,l.latitude,l.longitude FROM geo_location l, geo_blocks b WHERE b.location_id=l.location_id AND b.index_geo = INET_ATON(?)-(INET_ATON(?)%65536) AND INET_ATON(?) BETWEEN b.ip_start AND b.ip_end";
			String sql = "SELECT country,city,region,postalcode,latitude,longitude FROM geo_location where location_id = (SELECT location_id FROM geo_blocks WHERE index_geo = INET_ATON(?)-(INET_ATON(?)%65536) AND INET_ATON(?) BETWEEN ip_start AND ip_end)";
// The following query was recommended by http://jcole.us/blog/archives/2007/11/24/on-efficiently-geo-referencing-ips-with-maxmind-geoip-and-mysql-gis/
// However, it's not guaranteed to return the correct result.  However it is VERY fast.		
//			String sql = "select l.country,l.region,l.city,l.postalcode,l.latitude,l.longitude from geo_location l where l.location_id = (SELECT location_id FROM geo_blocks WHERE ip_start >= INET_ATON(?) ORDER BY ip_end ASC LIMIT 1);";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1,pIPAddress);	
			stmt.setString(2,pIPAddress);			
			stmt.setString(3,pIPAddress);			
			rs = executeQuery(stmt);
			if ( rs.next() ) {	
				loc = new Coordinate( rs.getDouble("latitude"), rs.getDouble("longitude"), rs.getString("country"), null, rs.getString("city"), rs.getString("region"), rs.getString("postalcode") );
			}
		} finally {
			if (rs != null) { 
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
*/
		//
		// If we struck out in the database, then call MaxMind.
		//
		if ( loc == null ) {
System.err.println("Model.geoIP: struck out in db.  calling maxmind.");
			try {					
				loc = this.mUtility.geoIP(pIPAddress);
System.err.println("Model.geoIP: maxmind returned: "+loc);
			} catch( GeocoderException hrge ) {
				hrge.printStackTrace(System.err);
			}			
			
			//
			// Save it back to the database so we won't incur a trip to the provider
			// next time.
			//
			if ( loc != null ) {
				_rememberIPCoordinate(pIPAddress,loc);
			}
		}
		return( loc );
	}
	
	private void _rememberIPCoordinate(String pIPAddress, Location pCoord )
		throws SQLException
	{
	// XXX - TBD
	}

	/*

	private Connection getConnection() {
		if ( this.mConnection == null ) {
			try {
				String jndiName = "jdbc/EventEngine";
//				Properties p = new Properties();
//				p.put("java.naming.factory.initial", "com.sun.jndi.cosnaming.CNCtxFactory");
//				p.put("java.naming.provider.url","java:comp/env");
		        Context initContext = new InitialContext();
		        Context envContext  = (Context)initContext.lookup("java:comp/env");
		        if ( envContext == null ) {
		        	getLogger().error("Model.getConnection: Unable to find initial context.");
		        } else {
			        DataSource ds = (DataSource)envContext.lookup(jndiName);
		        	this.mConnection = ds.getConnection();
		        }
			} catch ( Exception e ) {
				getLogger().error("Model.getConnection: "+e.getMessage());
				e.printStackTrace();
			}
		}
		return( this.mConnection );
	}
*/
	
	/**
	 * Escape a string bound for the database.  This is to prevent SQL injection attacks by
	 * hackers randomly placing single quotes in strings.
	 * @param pStr		unsafe string
	 * @return		safe string
	 */
	private static String escapeSql(String pStr ) {
		if (pStr == null) {
	        	return null;
	        }		
		return ( pStr.replace("'", "\'") );
	}
	private Connection getConnection() throws SQLException {
		return( this.mDataSource.getConnection() );	
	}

	private void putConnection( Connection pConnection ) throws SQLException {
		if ( pConnection != null ) {
			pConnection.close();
		}
	}	
	
	/**
	 * Wrapper to java.sql.Statement executeUpdate which allows logging of SQL.
	 * @param pStatement		Statement object
	 * @return int				return value of executeUpdate()
	 * @throws SQLException		Database error occurred
	 */
	private int executeUpdate(PreparedStatement pStatement)
    	throws SQLException
    {
		Utility.getLogger().debug(pStatement.toString());
		long start = System.nanoTime();
		int status = pStatement.executeUpdate();
		Utility.getLogger().debug("update took: "+(((System.nanoTime() - start) * 1.0) / 100000.0)+" ms");
		return( status );
    }
	
	/**
	 * Wrapper to java.sql.Statement executeQuery which allows logging of SQL.
	 * @param pStatement		Statement object
	 * @return int				return value of executeUpdate()
	 * @throws SQLException		Database error occurred
	 */	
	private ResultSet executeQuery(PreparedStatement pStatement)
    	throws SQLException
    {
		Utility.getLogger().debug(pStatement.toString());
		long start = System.nanoTime();	
		ResultSet status = pStatement.executeQuery();
		Utility.getLogger().debug("query took: "+(((System.nanoTime() - start) * 1.0) / 100000.0)+" ms");
		return( status );
    }

	//
	// For users editing their profile, we don't actually preload the form with their real
	// password.   We can't, because it's encrypted in the database.   So we preload the
	// form with this string.   Unless they actually change their password to something else,
	// we keep their old password if we see this value on the form submission.
	//
	public static final String DUMMY_PASSWORD = "_PASSWORD_ALREADY_SET_";
	private static final String JNDIDatabaseName = "jdbc/EventEngine";

	private Utility mUtility;
	private DataSource mDataSource;

} 
