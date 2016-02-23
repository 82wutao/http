package http.protocol;

public class HttpMethods {
	private HttpMethods() {
	}
//1.1	GET、POST、HEAD、OPTIONS、PUT、DELETE和TARCE。
	public static final String Http11_GET = "GET";
	public static final String Http11_POST = "POST";
	public static final String Http11_HEAD = "HEAD";
    public static final String Http11_OPTIONS = "OPTIONS";
    public static final String Http11_PUT = "PUT";
    public static final String Http11_DELETE = "DELETE";
    public static final String Http11_TRACE = "TRACE";

    public static final String CONNECT = "CONNECT";
    public static final String PROPFIND = "PROPFIND";
    public static final String PROPPATCH = "PROPPATCH";
    public static final String MKCOL = "MKCOL";
    public static final String COPY = "COPY";
    public static final String MOVE = "MOVE";
    public static final String LOCK = "LOCK";
    public static final String UNLOCK = "UNLOCK";
    public static final String ACL = "ACL";
    public static final String REPORT = "REPORT";
    public static final String VERSION_CONTROL = "VERSION-CONTROL";
    public static final String CHECKIN = "CHECKIN";
    public static final String CHECKOUT = "CHECKOUT";
    public static final String UNCHECKOUT = "UNCHECKOUT";
    public static final String SEARCH = "SEARCH";
    public static final String MKWORKSPACE = "MKWORKSPACE";
    public static final String UPDATE = "UPDATE";
    public static final String LABEL = "LABEL";
    public static final String MERGE = "MERGE";
    public static final String BASELINE_CONTROL = "BASELINE_CONTROL";
    public static final String MKACTIVITY = "MKACTIVITY";
}
