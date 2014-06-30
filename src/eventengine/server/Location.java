/**
 * Coordinate.  Encapsulates location metrics.
 */
package eventengine.server;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * @author db
 * Holds a latitude and longitude coordinate.
 */
@XmlRootElement
public class Location {
	public Location() {
	}
	public Location( double pLatitude, double pLongitude ) {
		this.mLatitude = pLatitude;
		this.mLongitude = pLongitude;
	}
	public Location( double pLatitude, double pLongitude, String pCountry, String pAddress, String pCity, String pState, String pZip ) {
		this.mLatitude = pLatitude;
		this.mLongitude = pLongitude;
		this.mCountry = pCountry;
		this.mAddress = pAddress;
		this.mCity = pCity;
		this.mState = pState;
		this.mZip = pZip;
	}
	public Location( double pLatitude, double pLongitude, String pLocation ) {
		this.mLatitude = pLatitude;
		this.mLongitude = pLongitude;
		this.mLocation = pLocation;
	}		
	public void setLatitude( double pLatitude ) {
		this.mLatitude = pLatitude;
	}
	public double getLatitude() {
		return( this.mLatitude );
	}
	public void setLongitude( double pLongitude ) {
		this.mLongitude = pLongitude;
	}
	public double getLongitude() {
		return( this.mLongitude );
	}
	public void setAddress( String pAddress ) {
		this.mAddress = pAddress;
	}
	public String getAddress() {
		return( this.mAddress );
	}	
	public void setCountry( String pCountry ) {
		this.mCountry = pCountry;
	}
	public String getCountry() {
		return( this.mCountry );
	}
	public void setCity( String pCity ) {
		this.mCity = pCity;
	}
	public String getCity() {
		return( this.mCity );
	}
	public void setState( String pState ) {
		this.mState = pState;
	}
	public String getState() {
		return( this.mState );
	}
	public void setZip( String pZip ) {
		this.mZip = pZip;
	}
	public String getZip() {
		return( this.mZip );
	}
	public void setLocation( String pLocation ) {
		this.mLocation = pLocation;
	}
	public String getLocation() {
		//
		// If we have a specific location set, then return that.
		//
		if ( this.mLocation != null ) {
			return( this.mLocation );
		}
		//
		// Otherwise build the location from the city, state, zip (if any).
		//
		StringBuffer sb = new StringBuffer();
//		if ( this.mCountry != null ) {
//			sb.append(this.mCountry);
//		}		
		if ( this.mAddress != null ) {
			if ( sb.length() > 0 ) {
				sb.append(", ");
			}			
			sb.append(this.mAddress);
		}
		if ( this.mCity != null ) {
			if ( sb.length() > 0 ) {
				sb.append(", ");
			}
			sb.append(this.mCity);
		}
		if ( this.mState != null ) {
			if ( sb.length() > 0 ) {
				sb.append(", ");
			}
			sb.append(this.mState);
		}
		if ( this.mZip != null ) {
			if ( sb.length() > 0 ) {
				sb.append(" ");
			}
			sb.append(this.mZip);
		}	
		return( sb.toString() );		
	}
//	public String toString() {
//		StringBuffer sb = new StringBuffer();
//		sb.append( "latitude: "+mLatitude+", longitude: "+mLongitude+" ");
//		sb.append( getLocation() );
//		return( sb.toString() );
//	}
    public JSONObject toJSON()
		throws JSONException
	{
		JSONObject json = new JSONObject();
		json.put("latitude", this.mLatitude);
		json.put("longitude", this.mLongitude);
		json.put("country", this.mCountry);
		json.put("address", this.mAddress);
		json.put("city", this.mCity);
		json.put("state", this.mState);
		json.put("zip", this.mZip);
		json.put("location", this.mLocation);
		return( json );
	} 
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Location( ");
		sb.append("latitude="+this.mLatitude);
		sb.append(",longitude"+this.mLongitude);
		sb.append(",country"+this.mCountry);
		sb.append(",address="+this.mAddress);
		sb.append(",city="+this.mCity);
		sb.append(",state="+this.mState);
		sb.append(",zip="+this.mZip);
		sb.append(",location="+this.mLocation);
		sb.append(" )");
		return( sb.toString() );
	}              
	private double mLatitude;
	private double mLongitude;
	private String mCountry;
	private String mAddress;
	private String mCity;
	private String mState;
	private String mZip;
	private String mLocation;
}
