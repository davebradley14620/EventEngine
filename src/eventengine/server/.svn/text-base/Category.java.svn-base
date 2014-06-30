/**
 * 
 */
package eventengine.server;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author db
 *
 */
public class Category 
{
		public Category() {
		}
        public Category( int pId, Date pCreationTime, int pParentId, String pName, String pDescription) {
        	setId( pId );
        	setCreationTime( pCreationTime );
        	setParentId( pParentId );
        	setName( pName );
        	setDescription( pDescription );
        }
        public int getId() {
                return( this.mId );
        }
    	public void setId(int pId) {
    		this.mId = pId;
    	}
    	public Date getCreationTime() {
    		return( this.mCreationTime );
    	}
    	public void setCreationTime( Date pCreationTime ) {
    		this.mCreationTime = pCreationTime;
    	}
        public int getParentId() {
        	return( this.mParentId );
        }
        public void setParentId( int pParentId ) {
        	this.mParentId = pParentId;
        }
        public String getName() {
        	return( this.mName );
        }
        public void setName( String pName ) {
        	this.mName = pName;
        }
        public String getDescription() {
        	return( this.mDescription );
        }
        public void setDescription( String pDescription ) {
        	this.mDescription = pDescription;
        }
        public JSONObject toJSON()
        	throws JSONException
		{
			JSONObject json = new JSONObject();
			json.put("id", new Integer(this.mId));
			json.put("creationTime", this.mCreationTime); 
			json.put("parentId", this.mParentId);
			json.put("description", this.mDescription);      		
			return( json );
		} 
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("Category( ");
			sb.append("id="+this.mId);
			sb.append(",creationTime="+this.mCreationTime);
			sb.append(",parentId="+this.mParentId);
			sb.append(",description="+this.mDescription);
			sb.append(" )");
			return( sb.toString() );
		}          
        private int mId;
        private Date mCreationTime;
        private int mParentId;
        private String mName;
        private String mDescription;
}
