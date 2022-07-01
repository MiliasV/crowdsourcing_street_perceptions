package de.wigeogis.wigeosocial.annotationserver.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class IPDetector {

    public static String getClientIp(HttpServletRequest request) {
        String remoteAddr = "unknown";
        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr))
                remoteAddr = request.getRemoteAddr();
        }

        if (remoteAddr.equals("unknown")) {
            Map<String, String> requestHeader = getRequestHeadersInMap(request);
            for (String ip : requestHeader.values()) {
                remoteAddr = ip;
            }
        }

        return remoteAddr;
    }

    private static Map<String, String> getRequestHeadersInMap(HttpServletRequest request) {

        Map<String, String> result = new HashMap<>();

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            result.put(key, value);
        }

        return result;
    }

}
