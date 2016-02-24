package http.api;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public interface HttpResponse {
	// public static final String Head_AcceptRanges_Response="Accept-Ranges";
	// public static final String Head_Age_Response="Age";
	// public static final String Head_CacheControl_Response="Cache-Control";
	// public static final String Head_Connection_Response="Connection";
	// public static final String
	// Head_ContentEncoding_Response="Content-Encoding";
	// public static final String Head_ContentLength_Response="Content-Length";
	// public static final String Head_ContentType_Response="Content-Type";
	//
	// public static final String Head_Date_Response="Date";
	// public static final String Head_Expires_Response="Expires";
	// public static final String Head_Server_Response="Server";
	// public static final String Head_SetCookie_Response="Set-Cookie";
	// public static final String Head_Vary_Response="Vary";
	// public static final String Head_XCache_Response="X-Cache";

	/**
	 * 指定哪些网站可参与到 跨来源资源共享 过程中 Access-Control-Allow-Origin: * Provisional
	 */
	public static final String Access_Control_Allow_Origin = "Access-Control-Allow-Origin";
	/**
	 * Specifies which patch document formats this server supports Accept-Patch:
	 * text/example;charset=utf-8 Permanent
	 */
	public static final String Accept_Patch = "Accept-Patch";
	/**
	 * 这个服务器支持哪些种类的部分内容范围 Accept-Ranges: bytes Permanent
	 */
	public static final String Accept_Ranges = "Accept-Ranges";
	/**
	 * 这个对象在 代理缓存 中存在的时间，以秒为单位 Age: 12 Permanent
	 */
	public static final String Age = "Age";
	/**
	 * 对于特定资源有效的动作。针对405不允许该方法（ 405 Method not allowed ）而使用 Allow: GET, HEAD
	 * Permanent
	 */
	public static final String Allow = "Allow";
	/**
	 * 向从服务器直到客户端在内的所有缓存机制告知，它们是否可以缓存这个对象。其单位为秒 Cache-Control: max-age=3600
	 * Permanent
	 */
	public static final String Cache_Control = "Cache-Control";
	/**
	 * 针对该连接所预期的选项 Connection: close Permanent
	 */
	public static final String Connection = "Connection";

	/**
	 * An opportunity to raise a "File Download" dialogue box for a known MIME
	 * type with binary format or suggest a filename for dynamic content. Quotes
	 * are necessary with special characters. Content-Disposition: attachment;
	 * filename="fname.ext" Permanent
	 */
	public static final String Content_Disposition = "Content-Disposition";
	/**
	 * 在数据上使用的编码类型。参考 超文本传输协议压缩 。 Content-Encoding: gzip Permanent
	 */
	public static final String Content_Encoding = "Content-Encoding";
	/**
	 * 内容所使用的语言 Content-Language: da Permanent
	 */
	public static final String Content_Language = "Content-Language";
	/**
	 * 回应消息体的长度，以 字节 （8位为一字节）为单位 Content-Length: 348 Permanent
	 */
	public static final String Content_Length = "Content-Length";
	/**
	 * 所返回的数据的一个候选位置 Content-Location: /index.htm Permanent
	 */
	public static final String Content_Location = "Content-Location";
	/**
	 * 回应内容的二进制 MD5 散列，以 Base64 方式编码 Content-MD5: Q2hlY2sgSW50ZWdyaXR5IQ==
	 * Obsolete[26]
	 * 
	 */
	public static final String Content_MD5 = "Content-MD5";
	/**
	 * 这条部分消息是属于某条完整消息的哪个部分 Content-Range: bytes 21010-47021/47022 Permanent
	 */
	public static final String Content_Range = "Content-Range";
	/**
	 * 当前内容的MIME类型 Content-Type: text/html; charset=utf-8 Permanent
	 */
	public static final String Content_Type = "Content-Type";
	/**
	 * 此条消息被发送时的日期和时间(按照 RFC 7231 中定义的“超文本传输协议日期”格式来表示) Date: Tue, 15 Nov 1994
	 * 08:12:31 GMT Permanent
	 */
	public static final String Date = "Date";
	/**
	 * 对于某个资源的某个特定版本的一个标识符，通常是一个 消息散列 ETag: "737060cd8c284d8af7ad3082f209582d"
	 * Permanent
	 */
	public static final String ETag = "ETag";
	/**
	 * 指定一个日期/时间，超过该时间则认为此回应已经过期 Expires: Thu, 01 Dec 1994 16:00:00 GMT
	 * Permanent: standard
	 */
	public static final String Expires = "Expires";
	/**
	 * 所请求的对象的最后修改日期(按照 RFC 7231 中定义的“超文本传输协议日期”格式来表示) Last-Modified: Tue, 15
	 * Nov 1994 12:45:26 GMT Permanent
	 */
	public static final String Last_Modified = "Last-Modified";
	/**
	 * 用来表达与另一个资源之间的类型关系，此处所说的类型关系是在 RFC 5988 中定义的 Link: </feed>;
	 * rel="alternate"[27] Permanent
	 */
	public static final String Link = "Link";
	/**
	 * 用来 进行 重定向 ，或者在创建了某个新资源时使用。 Location:
	 * http://www.w3.org/pub/WWW/People.html Permanent
	 */
	public static final String Location = "Location";
	/**
	 * This field is supposed to set P3P policy, in the form of
	 * P3P:CP="your_compact_policy". However, P3P did not take off,[28] most
	 * browsers have never fully implemented it, a lot of websites set this
	 * field with fake policy text, that was enough to fool browsers the
	 * existence of P3P policy and grant permissions for third party cookies.
	 * P3P: CP=
	 * "This is not a P3P policy! See http://www.google.com/support/accounts/bin/answer.py?hl=en&answer=151657 for more info."
	 * Permanent
	 */
	public static final String P3P = "P3P";
	/**
	 * 与具体的实现相关，这些字段可能在请求/回应链中的任何时候产生多种效果。 Pragma: no-cache Permanent
	 */
	public static final String Pragma = "Pragma";
	/**
	 * 要求在访问代理时提供身份认证信息。 Proxy-Authenticate: Basic Permanent
	 */
	public static final String Proxy_Authenticate = "Proxy-Authenticate";
	/**
	 * 用于缓解 中间人攻击 ，声明网站的认证用的 传输 层安全协议 证书的散列值 Public-Key-Pins: max-age=2592000;
	 * pin-sha256="E9CZ9INDbd+2eRQozYqqbQ2yXLVKB9+xcprMF+44U1g="; Permanent
	 */
	public static final String Public_Key_Pins = "Public-Key-Pins";
	/**
	 * Used in redirection, or when a new resource has been created. This
	 * refresh redirects after 5 seconds. Refresh: 5;
	 * url=http://www.w3.org/pub/WWW/People.html Proprietary and non-standard: a
	 * header extension introduced by Netscape and supported by most web
	 * browsers.
	 */
	public static final String Refresh = "Refresh";
	/**
	 * 如果某个实体临时不可用，则，此协议头用来告知客户端日后重试。 其值可以是一个特定的时间段(以秒为单位)或一个超文本传输协议日期。
	 * Retry-After: 120 Retry-After: Fri, 07 Nov 2014 23:59:59 GMT Permanent
	 */
	public static final String Retry_After = "Retry-After";
	/**
	 * 服务器的名字 Server: Apache/2.4.1 (Unix) Permanent
	 */
	public static final String Server = "Server";
	/**
	 * HTTP cookie Set-Cookie: UserID=JohnDoe; Max-Age=3600; Version=1
	 * Permanent: standard
	 */
	public static final String Set_Cookie = "Set-Cookie";
	/**
	 * 通用网关接口 协议头字段，用来说明当前这个超文本传输协议回应的 状态 。
	 * 普通的超文本传输协议回应，会使用单独的“状态行”（"Status-Line"）作为替代，这一点是在 RFC 7230 中定义的。 Status:
	 * 200 OK Not listed as a registered field name
	 */
	public static final String Status = "Status";

	/**
	 * A HSTS Policy informing the HTTP client how long to cache the HTTPS only
	 * policy and whether this applies to subdomains. Strict-Transport-Security:
	 * max-age=16070400; includeSubDomains Permanent: standard
	 */
	public static final String Strict_Transport_Security = "Strict-Transport-Security";
	/**
	 * The Trailer general field value indicates that the given set of header
	 * fields is present in the trailer of a message encoded with chunked
	 * transfer coding. Trailer: Max-Forwards Permanent
	 */
	public static final String Trailer = "Trailer";
	/**
	 * 用来将实体安全地传输给用户的编码形式。 当前定义 的方法 包括： 分 块
	 * 、压缩（compress）、缩小（deflate）、压缩（gzip）、实体（identity）。 Transfer-Encoding:
	 * chunked Permanent
	 */
	public static final String Transfer_Encoding = "Transfer-Encoding";
	/**
	 * 要求客户端升级到另一个协议。 Upgrade: HTTP/2.0, SHTTP/1.3, IRC/6.9, RTA/x11 Permanent
	 */
	public static final String Upgrade = "Upgrade";
	/**
	 * 告知下游的代理服务器，应当如何对未来的请求协议头进行匹配，以决定是否可使用已缓存的回应内容而不是重新从原始服务器请求新的内容。 Vary: *
	 * Permanent
	 */
	public static final String Vary = "Vary";
	/**
	 * 告知代理服务器的客户端，当前回应是通过什么途径发送的。 Via: 1.0 fred, 1.1 example.com (Apache/1.1)
	 * Permanent
	 */
	public static final String Via = "Via";
	/**
	 * 一般性的警告，告知在实体内容体中可能存在错误。 Warning: 199 Miscellaneous warning Permanent
	 */
	public static final String Warning = "Warning";
	/**
	 * 表明在请求获取这个实体时应当使用的认证模式。 WWW-Authenticate: Basic Permanent
	 */
	public static final String WWW_Authenticate = "WWW-Authenticate";
	/**
	 * Clickjacking protection: deny - no rendering within a frame, sameorigin -
	 * no rendering if origin mismatch, allow-from - allow from specified
	 * location, allowall - non-standard, allow from any location
	 * X-Frame-Options: deny Obsolete[33]
	 */
	public static final String X_Frame_Options = "X-Frame-Options";
	/**
	 * 跨站脚本攻击 (XSS)过滤器 X-XSS-Protection: 1; mode=block
	 * 
	 */
	public static final String X_XSS_Protection = "X-XSS-Protection";
	/**
	 * 
	 * 
	 * 
	 */
	public static final String Content_Security_Policy = "Content-Security-Policy";
	/**
	 * 
	 * 
	 * 
	 */
	public static final String X_Content_Security_Policy = "X-Content-Security-Policy";
	/**
	 * 内容安全策略定义。 X-WebKit-CSP: default-src 'self'
	 * 
	 */
	public static final String X_WebKit_CSP = "X-WebKit-CSP";
	/**
	 * The only defined value, "nosniff", prevents Internet Explorer from
	 * MIME-sniffing a response away from the declared content-type. This also
	 * applies to Google Chrome, when downloading extensions.[37]
	 * X-Content-Type-Options: nosniff
	 */
	public static final String X_Content_Type_Options = "X-Content-Type-Options";
	/**
	 * 表明用于支持当前网页应用程序的技术（例如PHP）（版本号细节通常放置在 X-Runtime 或 X-Version 中）
	 * X-Powered-By: PHP/5.4.0
	 */
	public static final String X_Powered_By = "X-Powered-By";
	/**
	 * Recommends the preferred rendering engine (often a backward-compatibility
	 * mode) to use to display the content. Also used to activate Chrome Frame
	 * in Internet Explorer. X-UA-Compatible: IE=EmulateIE7 X-UA-Compatible:
	 * IE=edge X-UA-Compatible: Chrome=1
	 */
	public static final String X_UA_Compatible = "X-UA-Compatible";
	/**
	 * Provide the duration of the audio or video in seconds; only supported by
	 * Gecko browsers X-Content-Duration: 42.666
	 * 
	 */
	public static final String X_Content_Duration = "X-Content-Duration";

	public void setResponseHead(String key, String value);

	public void setResponseHeads(Map<String, String> heads);

	public void setHttpVersion(String version);

	public void setStatusCode(int code);

	public void setContentLength(long lenght);
	public void setContentType(String type);
	public void setCharset(String charset);

	public void write(byte[] data, int off, int length);

	public void write(String text);

	public void write(File file);

	public void flush()throws IOException;

	public void addCookie(Cookie cookie);
}
