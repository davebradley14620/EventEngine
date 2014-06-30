/**
 * 
 */
package eventengine.server;

/**
 * @author db
 * This is an interface to hide the implementations of backend Geocoder services we might use.
 */
public interface Geocoder 
{
	/**
	 * Geocode the given address into a latitude and longitude.
	 * @param pCountry
	 * @param pAddress
	 * @param pCity
	 * @param pState
	 * @param pZip
	 * @return Coordinate if successful, null otherwise.
	 */
	public abstract Location geocode(String pCountry, String pAddress, String pCity, String pState, String pZip) throws GeocoderException;
	
	/**
	 * Geocode the given IP address into a latitude and longitude, country, city, state, address.
	 */
	public abstract Location geoip(String pIPAddress) throws GeocoderException;
	
}