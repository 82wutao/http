package http.protocol;


public class HttpHeaders {

	private HttpHeaders() {
	}
    
	// Header names
    public static final String ACCEPT = "Accept";
    public static final String ACCEPT_CHARSET = "Accept-Charset";
    public static final String ACCEPT_ENCODING = "Accept-Encoding";
    public static final String ACCEPT_LANGUAGE = "Accept-Language";
    public static final String ACCEPT_RANGES = "Accept-Ranges";
    public static final String AGE = "Age";
    public static final String ALLOW = "Allow";
    public static final String AUTHENTICATION_INFO = "Authentication-Info";
    public static final String AUTHORIZATION = "Authorization";
    public static final String CACHE_CONTROL = "Cache-Control";
    public static final String COOKIE = "Cookie";
    public static final String COOKIE2 = "Cookie2";
    public static final String CONNECTION = "Connection";
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    public static final String CONTENT_ENCODING = "Content-Encoding";
    public static final String CONTENT_LANGUAGE = "Content-Language";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String CONTENT_LOCATION = "Content-Location";
    public static final String CONTENT_MD5 = "Content-MD5";
    public static final String CONTENT_RANGE = "Content-Range";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String DATE = "Date";
    public static final String ETAG = "ETag";
    public static final String EXPECT = "Expect";
    public static final String EXPIRES = "Expires";
    public static final String FROM = "From";
    public static final String HOST = "Host";
    public static final String IF_MATCH = "If-Match";
    public static final String IF_MODIFIED_SINCE = "If-Modified-Since";
    public static final String IF_NONE_MATCH = "If-None-Match";
    public static final String IF_RANGE = "If-Range";
    public static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
    public static final String LAST_MODIFIED = "Last-Modified";
    public static final String LOCATION = "Location";
    public static final String MAX_FORWARDS = "Max-Forwards";
    public static final String ORIGIN = "Origin";
    public static final String PRAGMA = "Pragma";
    public static final String PROXY_AUTHENTICATE = "Proxy-Authenticate";
    public static final String PROXY_AUTHORIZATION = "Proxy-Authorization";
    public static final String RANGE = "Range";
    public static final String REFERER = "Referer";
    public static final String REFRESH = "Refresh";
    public static final String RETRY_AFTER = "Retry-After";
    public static final String SEC_WEB_SOCKET_ACCEPT = "Sec-WebSocket-Accept";
    public static final String SEC_WEB_SOCKET_EXTENSIONS = "Sec-WebSocket-Extensions";
    public static final String SEC_WEB_SOCKET_KEY = "Sec-WebSocket-Key";
    public static final String SEC_WEB_SOCKET_KEY1 = "Sec-WebSocket-Key1";
    public static final String SEC_WEB_SOCKET_KEY2 = "Sec-WebSocket-Key2";
    public static final String SEC_WEB_SOCKET_LOCATION = "Sec-WebSocket-Location";
    public static final String SEC_WEB_SOCKET_ORIGIN = "Sec-WebSocket-Origin";
    public static final String SEC_WEB_SOCKET_PROTOCOL = "Sec-WebSocket-Protocol";
    public static final String SEC_WEB_SOCKET_VERSION = "Sec-WebSocket-Version";
    public static final String SERVER = "Server";
    public static final String SERVLET_ENGINE = "Servlet-Engine";
    public static final String SET_COOKIE = "Set-Cookie";
    public static final String SET_COOKIE2 = "Set-Cookie2";
    public static final String SSL_CLIENT_CERT = "SSL_CLIENT_CERT";
    public static final String SSL_CIPHER = "SSL_CIPHER";
    public static final String SSL_SESSION_ID = "SSL_SESSION_ID";
    public static final String SSL_CIPHER_USEKEYSIZE = "SSL_CIPHER_USEKEYSIZE";
    public static final String STATUS = "Status";
    public static final String STRICT_TRANSPORT_SECURITY = "Strict-Transport-Security";
    public static final String TE = "TE";
    public static final String TRAILER = "Trailer";
    public static final String TRANSFER_ENCODING = "Transfer-Encoding";
    public static final String UPGRADE = "Upgrade";
    public static final String USER_AGENT = "User-Agent";
    public static final String VARY = "Vary";
    public static final String VIA = "Via";
    public static final String WARNING = "Warning";
    public static final String WWW_AUTHENTICATE = "WWW-Authenticate";
    public static final String X_FORWARDED_FOR = "X-Forwarded-For";
    public static final String X_FORWARDED_PROTO = "X-Forwarded-Proto";
    public static final String X_FORWARDED_HOST = "X-Forwarded-Host";
    public static final String X_FORWARDED_PORT = "X-Forwarded-Port";
    public static final String X_DISABLE_PUSH = "X-Disable-Push";
    public static final String X_FORWARDED_SERVER = "X-Forwarded-Server";

    // Content codings

    public static final String COMPRESS = "compress";
    public static final String X_COMPRESS = "x-compress";
    public static final String DEFLATE = "deflate";
    public static final String IDENTITY = "identity";
    public static final String GZIP = "gzip";
    public static final String X_GZIP = "x-gzip";

    // Transfer codings

    public static final String CHUNKED = "chunked";
    // IDENTITY
    // GZIP
    // COMPRESS
    // DEFLATE

    // Connection values
    public static final String KEEP_ALIVE = "Keep-Alive";
    public static final String CLOSE = "close";

    //MIME header used in multipart file uploads
    public static final String CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding";

    // Authentication Schemes
    public static final String BASIC = "Basic";
    public static final String DIGEST = "Digest";
    public static final String NEGOTIATE = "Negotiate";

    // Digest authentication Token Names
    public static final String ALGORITHM = "algorithm";
    public static final String AUTH_PARAM = "auth-param";
    public static final String CNONCE = "cnonce";
    public static final String DOMAIN = "domain";
    public static final String NEXT_NONCE = "nextnonce";
    public static final String NONCE = "nonce";
    public static final String NONCE_COUNT = "nc";
    public static final String OPAQUE = "opaque";
    public static final String QOP = "qop";
    public static final String REALM = "realm";
    public static final String RESPONSE = "response";
    public static final String RESPONSE_AUTH = "rspauth";
    public static final String STALE = "stale";
    public static final String URI = "uri";
    public static final String USERNAME = "username";


    /**
     * Extracts a token from a header that has a given key. For instance if the header is
     * <p>
     * content-type=multipart/form-data boundary=myboundary
     * and the key is boundary the myboundary will be returned.
     *
     * @param header The header
     * @param key    The key that identifies the token to extract
     * @return The token, or null if it was not found
     */
    public static String extractTokenFromHeader(final String header, final String key) {
        int pos = header.indexOf(key + '=');
        if (pos == -1) {
            return null;
        }
        int end;
        int start = pos + key.length() + 1;
        for (end = start; end < header.length(); ++end) {
            char c = header.charAt(end);
            if (c == ' ' || c == '\t' || c == ';') {
                break;
            }
        }
        return header.substring(start, end);
    }

    /**
     * Extracts a quoted value from a header that has a given key. For instance if the header is
     * <p>
     * content-disposition=form-data; name="my field"
     * and the key is name then "my field" will be returned without the quotes.
     *
     * @param header The header
     * @param key    The key that identifies the token to extract
     * @return The token, or null if it was not found
     */
    public static String extractQuotedValueFromHeader(final String header, final String key) {

        int keypos = 0;
        int pos = -1;
        boolean inQuotes = false;
        for (int i = 0; i < header.length() - 1; ++i) { //-1 because we need room for the = at the end
            //TODO: a more efficient matching algorithm
            char c = header.charAt(i);
            if (inQuotes) {
                if (c == '"') {
                    inQuotes = false;
                }
            } else {
                if (key.charAt(keypos) == c) {
                    keypos++;
                } else if (c == '"') {
                    keypos = 0;
                    inQuotes = true;
                } else {
                    keypos = 0;
                }
                if (keypos == key.length()) {
                    if (header.charAt(i + 1) == '=') {
                        pos = i + 2;
                        break;
                    } else {
                        keypos = 0;
                    }
                }
            }

        }
        if (pos == -1) {
            return null;
        }

        int end;
        int start = pos;
        if (header.charAt(start) == '"') {
            start++;
            for (end = start; end < header.length(); ++end) {
                char c = header.charAt(end);
                if (c == '"') {
                    break;
                }
            }
            return header.substring(start, end);

        } else {
            //no quotes
            for (end = start; end < header.length(); ++end) {
                char c = header.charAt(end);
                if (c == ' ' || c == '\t' || c == ';') {
                    break;
                }
            }
            return header.substring(start, end);
        }
    }
}
