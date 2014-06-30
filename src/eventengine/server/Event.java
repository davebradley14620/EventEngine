/**
 * Event
 * This encapsulates the basic idea of an Event.
 */
package eventengine.server;


import javax.xml.bind.annotation.XmlRootElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

@XmlRootElement
public class Event {	
	public Event() {		
	}
	
	public Event (int pId, String pName, String pDescription, Date pCreationTime, int pCreatorUser, Date pStartTime, int pDurationMinutes, boolean isRepeating, Cycle pRepeatCycle, Location pLocation, List<Category> pCategories, int pParticipantCount)
	{
		setId( pId );
		setName( pName );
		setDescription( pDescription );
		setCreationTime( pCreationTime );
		setCreatorUser(pCreatorUser);
		setStartTime( pStartTime );
		setDurationMinutes( pDurationMinutes );
		setIsRepeating( isRepeating );
		setRepeatCycle( pRepeatCycle );
		setLocation( pLocation );
		setCategories( pCategories );
		setParticipantCount( pParticipantCount );
	}
	public void setId( int pId ) {
		this.mId = pId;
	}
	public int getId() {
		return(this.mId);
	}
	public Date getCreationTime() {
		return( this.mCreationTime );
	}
	public void setCreationTime( Date pCreationTime ) {
		this.mCreationTime = pCreationTime;
	}
	public int getCreatorUser() {
		return( this.mCreatorUser );
	}
	public void setCreatorUser( int pCreatorUser ) {
		this.mCreatorUser = pCreatorUser;
	}
	public String getName() {
		return( this.mName );
	}
	public void setName(String pName) {
		this.mName = pName;
	}
	public String getDescription() {
		return( this.mDescription );
	}
	public void setDescription(String pDescription) {
		this.mDescription = pDescription;
	}
	public Date getStartTime() {
		return( this.mStartTime );
	}
	public void setStartTime( Date pStartTime ) {
		this.mStartTime = pStartTime;
	}
	public int getDurationMinutes() {
		return( this.mDurationMinutes );
	}
	public void setDurationMinutes( int pDurationMinutes ) {
		this.mDurationMinutes = pDurationMinutes;
	}
	public boolean getIsRepeating() {
		return( this.mIsRepeating );
	}
	public void setIsRepeating( boolean pIsRepeating ) {
		this.mIsRepeating = pIsRepeating;
	}
	public Cycle getRepeatCycle() {
		return( this.mRepeatCycle );
	}
	public void setRepeatCycle( Cycle pRepeatCycle ) {
		this.mRepeatCycle = pRepeatCycle;
	}
	public Location getLocation() {
		return( this.mLocation );
	}
	public void setLocation( Location pLocation ) {
		this.mLocation = pLocation;
	}
	public List<Category> getCategories() {
		return( this.mCategories );
	}
	public void setCategories( List<Category> pCategories ) {
		this.mCategories = pCategories;
	}
	public int getParticipantCount() {
		return( this.mParticipantCount );
	}
	public void setParticipantCount( int pParticipantCount ) {
		this.mParticipantCount = pParticipantCount;
	}
    public JSONObject toJSON()
		throws JSONException
	{
    	String loc="";
    	if ( this.mLocation != null ) {
    		loc = this.mLocation.toJSON().toString();
    	}
    	JSONObject json = new JSONObject();
		json.put("id", new Integer(this.mId));
		json.put("name", this.mName);
		json.put("loc", loc);
		json.put("creationTime", this.mCreationTime); 
		json.put("durationMinutes", this.mDurationMinutes);      		
		json.put("isRepeating", this.mIsRepeating);
		json.put("repeatCycle", this.mRepeatCycle);

		JSONArray catsjson = new JSONArray();
		List<Category> cats = getCategories();
		Iterator<Category> it = cats.iterator();
		while( it.hasNext() ) {
			Category cat = it.next();
			catsjson.put(cat.toJSON());
		}
		json.put("categories", catsjson);

		return( json );
	} 
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Event( ");
		sb.append("id="+this.mId);
		sb.append(",name="+this.mName);
		sb.append(",loc="+this.mLocation);
		sb.append(",creationTime="+this.mCreationTime);
		sb.append(",durationMinutes="+this.mDurationMinutes); 
		sb.append(",isRepeating="+this.mIsRepeating);
		sb.append(",repeatCycle="+this.mRepeatCycle);
		sb.append(",categories=(");
		List<Category> cats = getCategories();
		if ( cats != null ) {
			Iterator<Category> it = cats.iterator();
			for( int i=0; it.hasNext(); i++ ) {
				Category cat = it.next();
				if ( i > 0 ) {
					sb.append(",");
				}
				sb.append(cat.getName());
			}
		}
		sb.append(")");
		sb.append(" )");
		return( sb.toString() );
	}              
	private int mId;
	private String mName;
	private String mDescription;
	private Date mCreationTime;
	private int mCreatorUser;
	private Date mStartTime;
	private int mDurationMinutes;
	private boolean mIsRepeating;
	private Cycle mRepeatCycle;
	private Location mLocation;
	private List<Category> mCategories;
	private int mParticipantCount;
} 
