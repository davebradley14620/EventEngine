/**
 * A Group is a logical collection of Users with a common purpose.   A group has a location.
 */
package eventengine.server;

import java.util.Vector;
/**
 * @author hoorate
 *
 */
public class Group extends Vector implements Entity {
	public Group( int pId, Location pLocation, String pName ) {
		this.mId = pId;
		this.mLocation = pLocation;
		this.mName = pName;
	}
	public int getId() {
		return( this.mId );
	}
	public Location getLocation() {
		return( this.mLocation );
	}
	public void setLocation( Location pLocation ) {
		this.mLocation = pLocation;
	}
	public String getName() {
		return( this.mName );
	}
	public void setName( String pName ) {
		this.mName = pName;
	}
	private int mId;
	private Location mLocation;
	private String mName;
}
