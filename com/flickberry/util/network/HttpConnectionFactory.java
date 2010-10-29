package com.flickberry.util.network;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import com.flickberry.log.Logger;

import net.rim.device.api.io.http.HttpHeaders;
import net.rim.device.api.io.http.HttpProtocolConstants;
import net.rim.device.api.servicebook.ServiceBook;
import net.rim.device.api.servicebook.ServiceRecord;
import net.rim.device.api.system.Branding;
import net.rim.device.api.system.CoverageInfo;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.WLANInfo;

public class HttpConnectionFactory {

	public static final int TRANSPORT_WIFI = 1;
	public static final int TRANSPORT_BES = 2;
	public static final int TRANSPORT_BIS = 4;
	public static final int TRANSPORT_DIRECT_TCP = 8;
	public static final int TRANSPORT_WAP2 = 16;
	public static final int TRANSPORT_SIM = 32;

	public static final int TRANSPORTS_ANY = TRANSPORT_WIFI | TRANSPORT_BES
			| TRANSPORT_BIS | TRANSPORT_DIRECT_TCP | TRANSPORT_WAP2
			| TRANSPORT_SIM;
	public static final int TRANSPORTS_AVOID_CARRIER = TRANSPORT_WIFI
			| TRANSPORT_BES | TRANSPORT_BIS | TRANSPORT_SIM;
	public static final int TRANSPORTS_CARRIER_ONLY = TRANSPORT_DIRECT_TCP
			| TRANSPORT_WAP2 | TRANSPORT_SIM;

	public static final int DEFAULT_TRANSPORT_ORDER[] = { TRANSPORT_SIM,
			TRANSPORT_WIFI, TRANSPORT_BIS, TRANSPORT_BES, TRANSPORT_WAP2,
			TRANSPORT_DIRECT_TCP };

	private static final int TRANSPORT_COUNT = DEFAULT_TRANSPORT_ORDER.length;

	private static ServiceRecord srWAP2[];
	private static boolean serviceRecordsLoaded = false;

	private int transports[];
	private int lastTransport = -1;

	protected Logger log = Logger.getLogger(getClass());

	public HttpConnectionFactory() {
		this(0);
	}

	public HttpConnectionFactory(int allowedTransports) {
		this(transportMaskToArray(allowedTransports));
	}

	public HttpConnectionFactory(int transportPriority[]) {
		if (!serviceRecordsLoaded) {
			loadServiceBooks(false);
		}
		transports = transportPriority;
	}

	public static String getUserAgent() {
		StringBuffer sb = new StringBuffer();
		sb.append("BlackBerry");
		sb.append(DeviceInfo.getDeviceName());
		sb.append("/");
		sb.append(DeviceInfo.getSoftwareVersion());
		sb.append(" Profile/");
		sb.append(System.getProperty("microedition.profiles"));
		sb.append(" Configuration/");
		sb.append(System.getProperty("microedition.configuration"));
		sb.append(" VendorID/");
		sb.append(Branding.getVendorId());

		return sb.toString();
	}

	public static String getProfile() {
		StringBuffer sb = new StringBuffer();
		sb.append("http://www.blackberry.net/go/mobile/profiles/uaprof/");
		sb.append(DeviceInfo.getDeviceName());
		sb.append("/");
		// RDF file format is 4.5.0.rdf (does not include build version)
		sb.append(DeviceInfo.getSoftwareVersion().substring(0, 3));
		sb.append(".rdf");

		return sb.toString();
	}

	public String getLastTransportName() {
		return getTransportName(getLastTransport());
	}

	public int getLastTransport() {
		return lastTransport;
	}

	public void setLastTransport(int lastTransport) {
		this.lastTransport = lastTransport;
	}

	private int nextTransport(int currIndex)
			throws HttpConnectionFactoryException {
		if ((currIndex >= 0) && (currIndex < transports.length - 1)) {
			return currIndex + 1;
		} else {
			throw new HttpConnectionFactoryException(
					"No more transport available");
		}
	}

	public HttpConnection getHttpConnection(String url) {
		return getHttpConnection(url, null, null);
	}

	public HttpConnection getHttpConnection(String url, HttpHeaders headers) {
		return getHttpConnection(url, headers, null);
	}

	public HttpConnection getHttpConnection(String url, byte[] data) {
		return getHttpConnection(url, null, data);
	}

	public HttpConnection getHttpConnection(String url, HttpHeaders headers,
			byte[] data) {
		int curIndex = 0;
		HttpConnection connection = null;

		while ((connection = tryHttpConnection(url, curIndex, headers, data)) == null) {
			try {
				curIndex = nextTransport(curIndex);
			} catch (HttpConnectionFactoryException e) {
				e.printStackTrace();
				break;
			}
		}

		if (connection != null) {
			setLastTransport(transports[curIndex]);
		}
		return connection;
	}

	/*
	 * Trying connection with all of transport
	 */
	private HttpConnection tryHttpConnection(String url, int tIndex,
			HttpHeaders headers, byte[] data) {
		HttpConnection connection = null;
		OutputStream os = null;

		log.debug("Try " + getTransportName(transports[tIndex]) + "..... ");
		switch (transports[tIndex]) {
		case TRANSPORT_SIM:
			try {
				connection = getSimConnection(url, false);
			} catch (IOException e) {
				log.debug(e.getMessage());
			} finally {
				break;
			}
		case TRANSPORT_WIFI:
			try {
				connection = getWifiConnection(url);
			} catch (IOException e) {
				log.debug(e.getMessage());
			} finally {
				break;
			}
		case TRANSPORT_BIS:
			try {
				connection = getBisConnection(url);
			} catch (IOException e) {
				log.debug(e.getMessage());
			} finally {
				break;
			}
		case TRANSPORT_BES:
			try {
				connection = getBesConnection(url);
			} catch (IOException e) {
				log.debug(e.getMessage());
			} finally {
				break;
			}
		case TRANSPORT_DIRECT_TCP:
			try {
				connection = getTcpConnection(url);
			} catch (IOException e) {
				log.debug(e.getMessage());
			} finally {
				break;
			}
		case TRANSPORT_WAP2:
			try {
				connection = getWap2Connection(url);
			} catch (IOException e) {
				log.debug(e.getMessage());
			} finally {
				break;
			}
		}

		log.debug("Connection : " + connection);
		if (connection != null) {
			try {
				log.debug("URL :  " + connection.getURL());
				// add headers to connection
				if (headers != null) {
					int size = headers.size();
					for (int i = 0; i < size;) {
						String header = headers.getPropertyKey(i);
						String value = headers.getPropertyValue(i++);

						if (value != null) {
							connection.setRequestProperty(header, value);
						}
					}
				}

				// post data
				if (data != null) {
					connection.setRequestMethod(HttpConnection.POST);
					connection
							.setRequestProperty(
									HttpProtocolConstants.HEADER_CONTENT_TYPE,
									HttpProtocolConstants.CONTENT_TYPE_APPLICATION_X_WWW_FORM_URLENCODED);
					connection.setRequestProperty(
							HttpProtocolConstants.HEADER_CONTENT_LENGTH, String
									.valueOf(data.length));
					os = connection.openOutputStream();
					os.write(data);
				} else {
					connection.setRequestMethod(HttpConnection.GET);
				}
			} catch (IOException e) {
				log.debug(e.getMessage());
				e.printStackTrace();
			}
		}
		return connection;
	}

	/*
	 * Create HttpConnection when running with simulator
	 */
	private HttpConnection getSimConnection(String url,
			boolean mdsSimulatorRunning) throws IOException {
		if (DeviceInfo.isSimulator()) {
			if (mdsSimulatorRunning) {
				return getConnection(url, ";deviceside=false", null);
			} else {
				return getConnection(url, ";deviceside=true", null);
			}
		}
		return null;
	}

	/*
	 * Create HttpConnection when connect with WIFI on it's device
	 */
	private HttpConnection getWifiConnection(String url) throws IOException {
		if (WLANInfo.getWLANState() == WLANInfo.WLAN_STATE_CONNECTED) {
			return getConnection(url, ";interface=wifi", null);
		}
		return null;
	}

	/*
	 * Create HttpConnection when connect with BES on it's device
	 */
	private HttpConnection getBesConnection(String url) throws IOException {
		// 2 For ConverageInfo.COVERAGE_MDS
		if (CoverageInfo.isCoverageSufficient(2)) {
			return getConnection(url, ";deviceside=false", null);
		}
		return null;
	}

	/*
	 * Create HttpConnection when connect with BIS on it's Device
	 */
	private HttpConnection getBisConnection(String url) throws IOException {
		// 4 For CoverageInfo.COVERAGE_BIS_B
		if (CoverageInfo.isCoverageSufficient(4)) {
			return getConnection(url,
					";deviceside=false;ConnectionType=mds-public", null);
		}
		return null;
	}

	/*
	 * Create HttpConnection when connect with WAP2 on it's Device
	 */
	private HttpConnection getWap2Connection(String url) throws IOException {
		// 1 For CoverageInfo.COVERAGE_DIRECT = 1;
		if (CoverageInfo.isCoverageSufficient(1) && srWAP2 != null
				&& srWAP2.length != 0) {
			return getConnection(url, ";deviceside=true;ConnectionUID=",
					srWAP2[0].getUid());
		}
		return null;
	}

	/*
	 * Create HttpConnection with TCP on it's Device
	 */
	private HttpConnection getTcpConnection(String url) throws IOException {
		// 1 For CoverageInfo.COVERAGE_DIRECT = 1;
		if (CoverageInfo.isCoverageSufficient(1)) {
			return getConnection(url, ";deviceside=true", null);
		}
		return null;
	}

	/*
	 * Create Connection with added suffix
	 */
	private HttpConnection getConnection(String url, String transportExtras1,
			String transportExtras2) throws IOException {
		StringBuffer fullUrl = new StringBuffer();
		fullUrl.append(url);

		if (transportExtras1 != null) {
			fullUrl.append(transportExtras1);
		}
		if (transportExtras2 != null) {
			fullUrl.append(transportExtras2);
		}

		return (HttpConnection) Connector.open(fullUrl.toString());
	}

	public static void reloadServiceBooks() {
		loadServiceBooks(true);
	}

	private static synchronized void loadServiceBooks(boolean reload) {
		if (serviceRecordsLoaded && !reload) {
			return;
		}
		ServiceBook sb = ServiceBook.getSB();
		ServiceRecord[] records = sb.getRecords();
		Vector mdsVec = new Vector();
		Vector bisVec = new Vector();
		Vector wap2Vec = new Vector();
		Vector wifiVec = new Vector();

		if (!serviceRecordsLoaded) {
			for (int i = 0; i < records.length; i++) {
				ServiceRecord myRecord = records[i];
				String cid, uid;

				if (myRecord.isValid() && !myRecord.isDisabled()) {
					cid = myRecord.getCid().toLowerCase();
					uid = myRecord.getUid().toLowerCase();
					if ((cid.indexOf("wptcp") != -1)
							&& (uid.indexOf("wap2") != -1)
							&& (uid.indexOf("wifi") == -1)
							&& (uid.indexOf("mms") == -1)) {
						wap2Vec.addElement(myRecord);
					}
				}
			}

			srWAP2 = new ServiceRecord[wap2Vec.size()];
			wap2Vec.copyInto(srWAP2);
			wap2Vec.removeAllElements();
			wap2Vec = null;

			serviceRecordsLoaded = true;
		}
	}

	public static int[] transportMaskToArray(int mask) {
		if (mask == 0) {
			mask = TRANSPORTS_ANY;
		}
		int numTransports = 0;
		for (int i = 0; i < TRANSPORT_COUNT; i++) {
			if ((DEFAULT_TRANSPORT_ORDER[i] & mask) != 0) {
				numTransports++;
			}
		}
		int transports[] = new int[numTransports];
		int index = 0;
		for (int i = 0; i < TRANSPORT_COUNT; i++) {
			if ((DEFAULT_TRANSPORT_ORDER[i] & mask) != 0) {
				transports[index++] = DEFAULT_TRANSPORT_ORDER[i];
			}
		}
		return transports;
	}

	private static String getTransportName(int transport) {
		String transportName = "";

		switch (transport) {
		case TRANSPORT_WIFI:
			transportName = "WIFI";
			break;
		case TRANSPORT_BES:
			transportName = "BES";
			break;
		case TRANSPORT_BIS:
			transportName = "BIS";
			break;
		case TRANSPORT_DIRECT_TCP:
			transportName = "TCP";
			break;
		case TRANSPORT_WAP2:
			transportName = "WAP2";
			break;
		case TRANSPORT_SIM:
			transportName = "SIMULATOR";
			break;
		default:
			transportName = "UNKNOWN";
			break;
		}

		return transportName;
	}
}
