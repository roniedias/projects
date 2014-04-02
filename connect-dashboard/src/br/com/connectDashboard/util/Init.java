package br.com.connectDashboard.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class Init {
    private static final String WEB_INF_DIR_NAME = "WEB-INF";
    private static String WEB_INF_PATH;
    
    public static String getWebInfPath() {
        
    	if (WEB_INF_PATH == null) {
    		
    		try {    		
	            WEB_INF_PATH = URLDecoder.decode(Init.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF8");
	            WEB_INF_PATH = WEB_INF_PATH.substring(0, WEB_INF_PATH.lastIndexOf(WEB_INF_DIR_NAME) + WEB_INF_DIR_NAME.length());
    		}
            catch (UnsupportedEncodingException e) {
            	e.printStackTrace();
            }
        }

    	return WEB_INF_PATH;
    }
}
