/**
 * 
 */
package eventengine.server;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.HashMap;
import edu.usc.webgis.GeocoderService_V02_95Locator;
import edu.usc.webgis.GeocoderService_V02_95SoapStub;
import edu.usc.webgis.WebServiceGeocodeResult;

/**
 * @author db
 *
 */
public class USCGeocoderImpl
	implements Geocoder
{
	public USCGeocoderImpl()
		throws GeocoderException
	{
		super();
		this.mKey = System.getProperty("geocoder.usc.key");
		if ( this.mKey == null ) {
			throw new GeocoderException("UCSGeocoderImpl: geocoder.usc.key not defined.  Check your web.xml.");
		}
		String version = System.getProperty("geocoder.usc.version");
		if ( version == null ) {
			throw new GeocoderException("UCSGeocoderImpl: geocoder.usc.version not defined.  Check your web.xml.");
		}		
		try {
			this.mVersion = Double.parseDouble(version);
		} catch( NumberFormatException e ) {
			throw new GeocoderException("UCSGeocoderImpl: geocoder.usc.version is not a floating point number.  Check your web.xml.");
		}
	}

	/* (non-Javadoc)
	 * @see com.hoorate.Geocoder#geocodeSync()
	 */
	@Override
	public Location geocode(String pCountry, String pAddress, String pCity, String pState, String pZip) throws GeocoderException {
		Location coord = null;
		//
		// This geocoder is US-only, so we ignore country parameter.
		//
		coord = _geocode(pAddress,pCity,pState,pZip);
		if ( coord != null ) {
			return( coord );
		}
		System.err.println("UCSGeocoderImpl.geocode: unable to geocode: "+pAddress+","+pCity+","+pState+","+pZip+".  Trying "+pAddress+","+pCity+","+pState);
		coord = _geocode(pAddress,pCity,pState,"");
		if ( coord != null ) {
			return( coord );
		}
		
		System.err.println("UCSGeocoderImpl.geocode: unable to geocode: "+pAddress+","+pCity+","+pState+".  Trying "+pCity+","+pState+","+pZip);
		coord = _geocode("",pCity,pState,pZip);
		if ( coord != null ) {
			return( coord );
		}
		System.err.println("UCSGeocoderImpl.geocode: unable to geocode: "+pCity+","+pState+","+pZip+". Giving up.");
		
		return( null );
	}
	
	public Location geoip(String pIPAddress) throws GeocoderException
	{
		throw new GeocoderException("USCGeocoder does not support GeoIP functionality.");		
	}
	
	private Location _geocode(String pAddress, String pCity, String pState, String pZip) throws GeocoderException {
		//
		// Make a SOAP call to USC WebGIS site to get the latitude and longitude for the given address.
		//
		try {
			GeocoderService_V02_95Locator locator = new GeocoderService_V02_95Locator();
			java.net.URL url = new java.net.URL(locator.getGeocoderService_V02_95SoapAddress());			
			GeocoderService_V02_95SoapStub stub = new GeocoderService_V02_95SoapStub(url,locator);	
			WebServiceGeocodeResult result = stub.geocodeAddressNonParsed(pAddress, pCity, pState, pZip, this.mKey, this.mVersion, false, true, true);
			Location coor = new Location(result.getLatitude(),result.getLongitude(), null, pAddress, pCity, pState, pZip);
System.err.println("determineLatLong: latitude="+result.getLatitude()+", longitude="+result.getLongitude());
			return( coor );
		} catch( MalformedURLException mfurl ) {
			System.err.println("USCGeocoderImpl.geocodeSync: Something is wrong with the URL");			
			throw new GeocoderException(mfurl.getMessage());
		} catch( RemoteException re ) {
			System.err.println("USCGeocoderImpl.geocodeSync: Unable to convert address to lat/lon ( "+pAddress+", "+pCity+", "+pState+", "+pZip+")");
			throw new GeocoderException(re.getMessage());
		}
	}

	private String mKey;
	private double mVersion;
//	private static final String USCKEY = "2adcf4e24c334bb39c78f52dda12ecc5";
//	private static final double USCVERSION = 2.95;
}
