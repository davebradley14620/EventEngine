/**
 * User class
 */
package eventengine.server;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author hoorate
 *
 */
@XmlRootElement
public class User implements Entity {
	public User() {		
	}
	
	public User (int pId, Date pCreatedOn, Location pLocation) {
		this.mId = pId;
		this.mCreatedOn = pCreatedOn;
		this.mLocation = pLocation;
	}
	
	public User (int pId, String pScreenName, String pFirstName, String pLastName, Location pLocation, String pFacebookId, String pEmailAddress, Date pCreatedOn, Date pUpdatedOn, boolean pIsTermsRead, int pRegistrationCode  )
	{
		this(pId,pCreatedOn,pLocation);
		setScreenName(pScreenName);
		setFirstName(pFirstName);
		setLastName(pLastName);
		setLocation(pLocation);
		setFacebookId(pFacebookId);
		setEmailAddress(pEmailAddress);
		setCreatedOn(pCreatedOn);
		setUpdatedOn(pUpdatedOn);
		setIsTermsRead(pIsTermsRead);
		setRegistrationCode(pRegistrationCode);
	}
	
	public int getId() {
		return(this.mId);
	}
	public void setId(int pId) {
		this.mId = pId;
	}
	public Date getCreatedOn() {
		return( this.mCreatedOn );
	}
	public void setCreatedOn( Date pCreatedOn ) {
		this.mCreatedOn = pCreatedOn;
	}
	public Date getUpdatedOn() {
		return( this.mUpdatedOn );
	}
	public void setUpdatedOn( Date pUpdatedOn ) {
		this.mUpdatedOn = pUpdatedOn;
	}    
	public String getFirstName() {
		return( this.mFirstName );
	}
	public void setFirstName(String pFirstName) {
		this.mFirstName = pFirstName;
	}
	public String getLastName() {
		return( this.mLastName );
	}
	public void setLastName(String pLastName) {
		this.mLastName = pLastName;
	}
	public String getScreenName() {
		return( this.mScreenName );
	}
	public void setScreenName( String pScreenName ) {
		this.mScreenName = pScreenName;
	}
	public Location getLocation() {
		return( this.mLocation );
	}
	public void setLocation( Location pLocation ) {
		this.mLocation = pLocation;
	}
	public String getFacebookId() {
		return( this.mFacebookId );
	}
	public void setFacebookId( String pFacebookId ) {
		this.mFacebookId = pFacebookId;
	}
	public String getEmailAddress() {
		return( this.mEmailAddress );
	}
	public void setEmailAddress( String pEmailAddress ) {
		this.mEmailAddress = pEmailAddress;
	}
	public boolean getIsTermsRead() {
		return( this.mIsTermsRead );
	}
	public void setIsTermsRead( boolean pIsTermsRead ) {
		this.mIsTermsRead = pIsTermsRead;
	}
    public void setRegisteredOn( Date pRegisteredOn ) {
    	this.mRegisteredOn = pRegisteredOn;
    }
    public Date getRegisteredOn() {
    	return( this.mRegisteredOn );
    }	
	public int getRegistrationCode() {
		return( this.mRegistrationCode );
	}
	public void setRegistrationCode( int pRegistrationCode ) {
		this.mRegistrationCode = pRegistrationCode;
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
		json.put("facebookId", this.mFacebookId);
		json.put("firstName", this.mFirstName);
		json.put("lastName", this.mLastName);
		json.put("screenName", this.mScreenName);
		json.put("creationOn", this.mCreatedOn); 
		json.put("updatedOn", this.mUpdatedOn); 		
		json.put("loc", loc);
		json.put("email", this.mEmailAddress);      		
		json.put("isTermsRead", this.mIsTermsRead);
		json.put("registrationCode", this.mRegistrationCode);
		return( json );
	} 
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("User( ");
		sb.append("id="+this.mId);
		sb.append(",facebookId="+this.mFacebookId);
		sb.append(",firstName="+this.mFirstName);
		sb.append(",lastName="+this.mLastName);
		sb.append(",screenName="+this.mScreenName);
		sb.append(",createdOn="+this.mCreatedOn);
		sb.append(",updatedOn="+this.mUpdatedOn);
		sb.append(",loc="+this.mLocation);
		sb.append(",email="+this.mEmailAddress);
		sb.append(",isTermsRead="+this.mIsTermsRead);
		sb.append(",registrationCode="+this.mRegistrationCode);
		sb.append(" )");
		return( sb.toString() );
	}              	
	private int mId;
	private String mFacebookId;
	private String mFirstName;
	private String mLastName;
	private String mScreenName;
	private Date mCreatedOn;
	private Date mUpdatedOn;
	private Location mLocation;
	private String mEmailAddress;
    private Date mRegisteredOn;	
	private boolean mIsTermsRead;
	private int mRegistrationCode;
}
