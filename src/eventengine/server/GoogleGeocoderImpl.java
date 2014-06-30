/**
 * 
 */
package eventengine.server;
import java.net.URL;
import java.net.URLEncoder;
import java.net.MalformedURLException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
/*
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.ext.DefaultHandler2;
*/


/**
 * @author db
 *
 */
public class GoogleGeocoderImpl
	implements Geocoder
{

	/**
	 * @param pUserOrVendorId
	 * @param isUserId
	 * @param pAddress
	 * @param pCity
	 * @param pState
	 * @param pZip
	 * @exception GeocoderException	Something went horribly wrong.
	 */
	public GoogleGeocoderImpl()
		throws GeocoderException
	{
		super();	
		this.mGoogleURL = System.getProperty("geocoder.google.url");
		if ( this.mGoogleURL == null ) {
			System.err.println("GoogleGeocoderImpl: geocoder.google.url not defined.  Check your web.xml.");
		}
	}

	/* (non-Javadoc)
	 * @see com.hoorate.Geocoder#geocodeSync()
	 */
	@Override
	public Location geocode(String pCountry, String pAddress, String pCity, String pState, String pZip) throws GeocoderException {
//System.err.println("GoogleGeocodeImpl.geocode: enter");
		if ( this.mGoogleURL == null ) {
			throw new GeocoderException("GoogleGeocoderImpl.geocode: bad URL.");
		}
		Location coord = null;
//System.err.println("GoogleGeocodeImpl.geocode: about to geocode: "+pAddress+","+pCity+","+pState+","+pZip);
		//
		// Google doesn't handle any non-US locations as far as I know.   Ignore the country param.
		//
		String fulladdr = "";
		if ( (pAddress != null) && (pAddress.length() > 0) ) {
			fulladdr += pAddress;
		}
		if ( (pCity != null) && (pCity.length() > 0) ) {
			if ( fulladdr.length() > 0 ) {
				fulladdr += ",";
			}
			fulladdr += pCity;
		}	
		if ( (pState != null) && (pState.length() > 0) ) {
			if ( fulladdr.length() > 0 ) {
				fulladdr += ",";
			}
			fulladdr += pState;
		}			
		if ( (pZip != null) && (pZip.length() > 0) ) {
			if ( fulladdr.length() > 0 ) {
				fulladdr += ",";
			}
			fulladdr += pZip;
		}				
		coord = _geocode(fulladdr);
		if ( coord != null ) {
			return( coord );
		}
		System.err.println("GoogleGeocderImpl.geocode: unable to geocode: "+fulladdr+".  Trying "+pAddress+","+pCity+","+pState);

		fulladdr = "";
		if ( (pAddress != null) && (pAddress.length() > 0) ) {
			fulladdr += pAddress;
		}
		if ( (pCity != null) && (pCity.length() > 0) ) {
			if ( fulladdr.length() > 0 ) {
				fulladdr += ",";
			}
			fulladdr += pCity;
		}	
		if ( (pState != null) && (pState.length() > 0) ) {
			if ( fulladdr.length() > 0 ) {
				fulladdr += ",";
			}
			fulladdr += pState;
		}			
		if ( fulladdr.length() > 0 ) {
			coord = _geocode(fulladdr);
			if ( coord != null ) {
				return( coord );
			}
		}
		System.err.println("GoogleGeocoderImpl.geocode: unable to geocode: "+fulladdr+".  Trying "+pCity+","+pState+","+pZip);
		fulladdr = "";
		if ( (pCity != null) && (pCity.length() > 0) ) {
			fulladdr += pCity;
		}	
		if ( (pState != null) && (pState.length() > 0) ) {
			if ( fulladdr.length() > 0 ) {
				fulladdr += ",";
			}
			fulladdr += pState;
		}			
		if ( (pZip != null) && (pZip.length() > 0) ) {
			if ( fulladdr.length() > 0 ) {
				fulladdr += ",";
			}
			fulladdr += pZip;
		}					
		if ( fulladdr.length() > 0 ) {				
			coord = _geocode(fulladdr);
			if ( coord != null ) {
				return( coord );
			}
		}
		System.err.println("GoogleGeocoderImpl.geocode: unable to geocode: "+fulladdr+".  Trying "+pZip);
		fulladdr = "";
		if ( (pZip != null) && (pZip.length() > 0) ) {
			fulladdr += pZip;
		}					
		if ( fulladdr.length() > 0 ) {				
			coord = _geocode(fulladdr);
			if ( coord != null ) {
				return( coord );
			}
		}
		System.err.println("GoogleGeocoderImpl.geocode: unable to geocode: "+fulladdr+". Giving up.");
		
		return( null );
	}

	public Location geoip(String pIPAddress) throws GeocoderException
	{
		throw new GeocoderException("GoogleGeocoder does not support GeoIP functionality.");
	}

	private Location _geocode(String pFullAddress) throws GeocoderException {
		URL url;
		try {
			url = new URL(this.mGoogleURL+"&address="+URLEncoder.encode(pFullAddress,"UTF-8"));
//System.err.println("GoogleGeocoderImpl._geocode: URL="+url);
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			
			StringBuffer sb = new StringBuffer();
			String inputLine;
			while ((inputLine = br.readLine()) != null) {
				sb.append(inputLine);
			}
			br.close();
			String xml = sb.toString();
			xml.replaceAll("\\n","");			
			int idx = xml.indexOf("<location>");
			if ( idx == -1 ) {
				throw new GeocoderException("GoogleGeocoderImpl._geocode: Can't find <location> tag.");
			}
			int idx2 = xml.indexOf("<lat>",idx);
			if ( idx2 == -1 ) {
				throw new GeocoderException("GoogleGeocoderImpl._geocode: Can't find <lat> tag.");
			}			
			int idx3 = xml.indexOf("</lat>",idx2);
			if ( idx3 == -1 ) {
				throw new GeocoderException("GoogleGeocoderImpl._geocode: Can't find </lat> tag.");
			}
			String lat = xml.substring(idx2+5,idx3);
			Double latitude = null;
			try {
				latitude = Double.parseDouble(lat);
			} catch( NumberFormatException e ) {
				throw new GeocoderException("GoogleGeocoderImpl._geocode: latitude value ("+lat+") is not a floating point value.");
			}
			int idx4 = xml.indexOf("<lng>",idx);
			if ( idx4 == -1 ) {
				throw new GeocoderException("GoogleGeocoderImpl._geocode: Can't find <lng> tag.");
			}
			int idx5 = xml.indexOf("</lng>",idx4);
			if ( idx5 == -1 ) {
				throw new GeocoderException("GoogleGeocoderImpl._geocode: Can't find </lng> tag.");
			}
			String lon = xml.substring(idx4+5,idx5);
			Double longitude = null;
			try {
				longitude = Double.parseDouble(lon);
			} catch( NumberFormatException e ) {
				throw new GeocoderException("GoogleGeocoderImpl._geocode: longitude value ("+lon+") is not a floating point value.");
			}			
			Location coord = new Location(latitude.doubleValue(), longitude.doubleValue(), pFullAddress);
//			System.err.println("GoogleGeocoderImpl._geocode: SUCCESS - ("+pFullAddress+") = "+coord);
			return( coord );
/*
			SAXParserFactory factory = SAXParserFactory.newInstance();			
			SAXParser sax = SAXParserFactory.newSAXParser();
			// Turn on validation, and turn off namespaces
			factory.setValidating(false);
			factory.setNamespaceAware(false);
			SAXParser parser = factory.newSAXParser();
			parser.parse(url.openStream(), new MyHandler());
		} catch (ParserConfigurationException e) {
			System.out.println("The underlying parser does not support " +
			" the requested features.");
		} catch (FactoryConfigurationError e) {
			System.out.println("Error occurred obtaining SAX Parser Factory.");
*/			
		} catch( UnsupportedEncodingException uee ) {
			throw new GeocoderException("GoogleGeocoderImpl:  Bad URL ("+this.mGoogleURL+").  Check your web.xml.");
		} catch( MalformedURLException e ) {
			throw new GeocoderException("GoogleGeocoderImpl:  Bad URL ("+this.mGoogleURL+").  Check your web.xml.");
		} catch( IOException ioe ) {
			ioe.printStackTrace(System.err);
			throw new GeocoderException("GoogleGeocoderImpl:  IOException ("+this.mGoogleURL+"): "+ioe.getMessage());
		}		
	}
	
	private String mGoogleURL;
}
