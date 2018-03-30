package me.david.sploty4.util;

import java.util.HashMap;

public final class ErrorUtil {

    private static HashMap<Integer, String> errorCodes = new HashMap<>();

    static {
        errorCodes.put(-1, "Times Out");
        errorCodes.put(-2, "Dns Error");
        errorCodes.put(-3, "General IO Error");
        errorCodes.put(-4, "Cached Error");

        errorCodes.put(-100, "Server SSL Certification Expired");
        errorCodes.put(-101, "SSL -> Wrong Host");
        errorCodes.put(-102, "SSL -> Wrong Path");
        errorCodes.put(-103, "SSL -> Revoked");
        errorCodes.put(-104, "SSL -> Failed generating DH keypair(Internal Error)");
        errorCodes.put(-105, "SSL -> Strange DHPublicKey");
        errorCodes.put(-106, "SSL -> Handshake failure (Fatal)");
        errorCodes.put(-198, "Sploty Failed Generating SSL Context (No further Details)");
        errorCodes.put(-199, "SSL Certification is Wrong(No further Details)");


        errorCodes.put(400, "Bad Request");
        errorCodes.put(401, "Unauthorized");
        errorCodes.put(402, "Payment Required");
        errorCodes.put(403, "Access Denied");
        errorCodes.put(404, "File not found");
        errorCodes.put(405, "Method Not Allowed");
        errorCodes.put(406, "Not Acceptable");
        errorCodes.put(407, "Proxy Authentication Required");
        errorCodes.put(408, "Request Timeout");
        errorCodes.put(409, "Conflict");
        errorCodes.put(410, "Gone");
        errorCodes.put(411, "Length Required");
        errorCodes.put(412, "Precondition Failed");
        errorCodes.put(413, "Request Entity Too Large");
        errorCodes.put(414, "URI Too Long");
        errorCodes.put(415, "Unsupported Media Type");
        errorCodes.put(416, "Requested range not satisfiable");
        errorCodes.put(417, "Expectation Failed");
        errorCodes.put(420, "Policy Not Fulfilled");
        errorCodes.put(421, "Misdirected Request");
        errorCodes.put(422, "Unprocessable Entity");
        errorCodes.put(423, "Locked");
        errorCodes.put(424, "Failed Dependency");
        errorCodes.put(426, "Upgrade Required");
        errorCodes.put(428, "Precondition Required");
        errorCodes.put(429, "Too Many Requests");
        errorCodes.put(431, "Request Header Fields Too Large");
        errorCodes.put(451, "Unavailable For Legal Reasons");
        errorCodes.put(418, "I’m a teapot(Aprilscherz der IETF)");
        errorCodes.put(425, "Unordered Collection");
        errorCodes.put(444, "No Response");
        errorCodes.put(449, "The request should be retried after doing the appropriate action");
        errorCodes.put(499, "Client Closed Request");

        errorCodes.put(300, "Umleitung (Multiple Choices)");
        errorCodes.put(301, "Umleitung (Moved Permanently)");
        errorCodes.put(302, "Umleitung (Found (Moved Temporarily))");
        errorCodes.put(303, "Umleitung (See Other)");
        errorCodes.put(304, "Umleitung (Not Modified)");
        errorCodes.put(305, "Umleitung (Use Proxy)");
        errorCodes.put(307, "Umleitung (Temporary Redirect)");
        errorCodes.put(308, "Umleitung (Permanent Redirect)");

        errorCodes.put(500, "Internal Server Error");
        errorCodes.put(501, "Not Implemented");
        errorCodes.put(502, "Bad Gateway");
        errorCodes.put(503, "Service Unavailable");
        errorCodes.put(504, "Gateway Timeout");
        errorCodes.put(505, "HTTP Version not supported");
        errorCodes.put(506, "Variant Also Negotiates");
        errorCodes.put(507, "Insufficient Storage");
        errorCodes.put(508, "Loop Detected");
        errorCodes.put(509, "Bandwidth Limit Exceeded");
        errorCodes.put(510, "Not Extended");
        errorCodes.put(511, "Network Authentication Required");
    }

    public static String getErrorMessage(int code){
        String message = errorCodes.get(code);
        if(message == null) return "Unknown";
        return message;
    }

}
