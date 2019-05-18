package application;

/**
 * This class contains utility methods for handling links
 * @author Gia-Phong Nguyen
 *
 */
public class LinkEncoders {

	private static int shift = 5; // key shift in encoding
	
	/**
	 * Keys to be for encoding
	 * @return keys
	 */
	private static String getKeys() {
		return "LZs1wui5xcvlzD4AS6WbnmQ23dfgP789qFGertyHJ0opaKTYUhjkCVEBXRIONM";
	}
	
	/**
	 * Converts ip and port into a link
	 * @param ip
	 * @param port
	 * @return link
	 */
	public static String convertToLink(String ip, int port) {
		String s = "";
		String splitIp[] = ip.split("\\.");
		for(String str:splitIp) {
			String hex = Integer.toHexString(Integer.parseInt(str));
			if(hex.length()==1) {
				hex = "g" + hex;
			}
			char hex1 = hex.charAt(0);
			char hex2 = hex.charAt(1);
			char hex1f = ' ';
			char hex2f = ' ';
			for(int i=0;i<getKeys().length()-shift;i++) {
				if(hex1 == getKeys().charAt(i))
					hex1f = getKeys().charAt(i+shift);
				if(hex2 == getKeys().charAt(i))
					hex2f = getKeys().charAt(i+shift);
			}
			s += ""+hex1f+hex2f;
		}
		String portStr = Integer.toHexString(port);
		String codedPort = "";
		
		for(int k=0;k<portStr.length();k++) {
			char hex = portStr.charAt(k);
			char hexf = ' ';
			for(int i=0;i<getKeys().length()-shift;i++) {
				if(hex == getKeys().charAt(i))
					hexf = getKeys().charAt(i+shift);
			}
			codedPort+=hexf;
		}
		s += codedPort;
		return s;
	}
	
	/**
	 * Converts a link into an ip and port
	 * @param code - link to be decoded
	 * @return array with the ip and port
	 */
	public static String[] decodeLink(String code) {
		String[] data = new String[2];
		String codedIp = code.substring(0, 8);
		int[] ipParts = new int[4];
		
		for(int i=0; i<8; i+=2) {
			String codedBit = codedIp.substring(i,i+2);
			String hexIp = "";
			char hex1 = ' ';
			char hex2 = ' ';
			for(int k=shift;k<getKeys().length();k++) {
				if(codedBit.charAt(0) == getKeys().charAt(k))
					hex1 = getKeys().charAt(k-shift);
				if(codedBit.charAt(1) == getKeys().charAt(k))
					hex2 = getKeys().charAt(k-shift);
			}
			hexIp += ""+hex1+hex2;
			
			int a = 0;
			if(hexIp.charAt(0)=='g')
				a++;
			ipParts[i/2]=Integer.decode("0x"+hexIp.substring(a,2));
		}
		
    String ip = "";
    for(int i:ipParts) {
    	ip+=i+".";
    }

    data[0] = ip.substring(0,ip.length()-1);
    
		String hexPort = "";
		String codedPort = code.substring(8);
		
		for(int k=0;k<codedPort.length();k++) {
			char hex = codedPort.charAt(k);
			char hexf = ' ';
			for(int i=shift;i<getKeys().length();i++) {
				if(hex == getKeys().charAt(i))
					hexf = getKeys().charAt(i-shift);
			}
			hexPort+=hexf;
		}
    
		data[1] = ""+Integer.decode("0x" + hexPort);
		return data;
	}
}
