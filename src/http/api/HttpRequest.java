package http.api;

import java.io.IOException;
import java.util.Map;

public interface HttpRequest {
	// public static final String Head_METHOD_Request = "Method";
	// public static final String Head_Uri_Request = "Uri";
	// public static final String Head_HttpVersion_Request = "Version";
	//
	// public static final String Head_Accept_Request = "Accept";
	// public static final String Head_AcceptLanguage_Request =
	// "Accept-Language";
	// public static final String Head_AcceptEcoding_Request =
	// "Accept-Encoding";
	// public static final String Head_Connection_Request = "Connection";
	// public static final String Head_ContentLength_Request = "Content-Length";
	// public static final String Head_ContentType_Request = "Content-Type";
	// public static final String Head_Cookie_Request = "Cookie";
	// public static final String Head_DNT_Request = "DNT";
	// public static final String Head_Host_Request = "Host";
	// public static final String Head_Referer_Request = "Referer";
	// public static final String Head_UserAgent_Request = "User-Agent";
	/**
	 * 能够接受的回应内容类型（Content-Types）。参见内容协商。 Accept: text/plain Permanent
	 */
	public static final String Accept = "Accept";
	/**
	 * 能够接受的字符集 Accept-Charset: utf-8 Permanent
	 */
	public static final String Accept_Charset = "Accept-Charset";
	/**
	 * 能够接受的编码方式列表。参考 超文本传输协议压缩 。 Accept-Encoding: gzip, deflate Permanent
	 */
	public static final String Accept_Encoding = "Accept-Encoding";
	/**
	 * 能够接受的回应内容的自然语言列表。参考 内容协商 。 Accept-Language: en-US Permanent
	 */
	public static final String Accept_Language = "Accept-Language";
	/**
	 * 能够接受的按照时间来表示的版本 Accept-Datetime: Thu, 31 May 2007 20:35:00 GMT
	 * Provisional
	 */
	public static final String Accept_Datetime = "Accept-Datetime";
	/**
	 * 用于超文本传输协议的认证的认证信息 Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==
	 * Permanent
	 */
	public static final String Authorization = "Authorization";
	/**
	 * 用来指定在这次的请求/回复链中的所有缓存机制 都必须 遵守的指令 Cache-Control: no-cache Permanent
	 */
	public static final String Cache_Control = "Cache-Control";
	/**
	 * 该浏览器想要优先使用的连接类型 Connection: keep-alive Connection: Upgrade Permanent
	 */
	public static final String Connection = "Connection";
	/**
	 * 之前由服务器通过 Set- Cookie （下文详述）发送的一个 超文本传输协议Cookie Cookie: $Version=1;
	 * Skin=new; Permanent: standard
	 */
	public static final String Cookie = "Cookie";
	/**
	 * 以 八位字节数组 （8位的字节）表示的请求体的长度 Content-Length: 348 Permanent
	 */
	public static final String Content_Length = "Content-Length";
	/**
	 * 请求体的内容的二进制 MD5 散列值，以 Base64 编码的结果 Content-MD5: Q2hlY2sgSW50ZWdyaXR5IQ==
	 * Obsolete[4]
	 */
	public static final String Content_MD5 = "Content-MD5";
	/**
	 * 请求体的 多媒体类型 （用于POST和PUT请求中） Content-Type:
	 * application/x-www-form-urlencoded Permanent
	 */
	public static final String Content_Type = "Content-Type";
	/**
	 * 发送该消息的日期和时间(按照 RFC 7231 中定义的"超文本传输协议日期"格式来发送) Date: Tue, 15 Nov 1994
	 * 08:12:31 GMT Permanent
	 */
	public static final String Date = "Date";
	/**
	 * 表明客户端要求服务器做出特定的行为 Expect: 100-continue Permanent
	 */
	public static final String Expect = "Expect";
	/**
	 * 发起此请求的用户的邮件地址 From: user@example.com Permanent
	 */
	public static final String From = "From";
	/**
	 * 服务器的域名(用于虚拟主机)，以及服务器所监听的传输控制协议端口号。 如果所请求的端口是对应的服务的标准端口，则端口号可被省略。
	 * 自超文件传输协议版本1.1（HTTP/1.1）开始便是必需字段。 Host: en.wikipedia.org:80 Host:
	 * en.wikipedia.org Permanent
	 */
	public static final String Host = "Host";
	/**
	 * 仅当客户端提供的实体与服务器上对应的实体相匹配时，才进行对应的操作。 主要作用时，用作像 PUT
	 * 这样的方法中，仅当从用户上次更新某个资源以来，该资源未被修改的情况下，才更新该资源。 If-Match:
	 * "737060cd8c284d8af7ad3082f209582d" Permanent
	 */
	public static final String If_Match = "If-Match";
	/**
	 * 允许在对应的内容未被修改的情况下返回304未修改（ 304 Not Modified ） If-Modified-Since: Sat, 29
	 * Oct 1994 19:43:31 GMT Permanent
	 */
	public static final String If_Modified_Since = "If-Modified-Since";
	/**
	 * 允许在对应的内容未被修改的情况下返回304未修改（ 304 Not Modified ），参考超文本传输协议的实体标记
	 * If-None-Match: "737060cd8c284d8af7ad3082f209582d" Permanent
	 */
	public static final String If_None_Match = "If-None-Match";
	/**
	 * 如果该实体未被修改过，则向我发送我所缺少的那一个或多个部分；否则，发送整个新的实体 If-Range:
	 * "737060cd8c284d8af7ad3082f209582d" Permanent
	 */
	public static final String If_Range = "If-Range";
	/**
	 * 仅当该实体自某个特定时间已来未被修改的情况下，才发送回应。 If-Unmodified-Since: Sat, 29 Oct 1994
	 * 19:43:31 GMT Permanent
	 */
	public static final String If_Unmodified_Since = "If-Unmodified-Since";
	/**
	 * 限制该消息可被代理及网关转发的次数。 Max-Forwards: 10 Permanent
	 */
	public static final String Max_Forwards = "Max-Forwards";
	/**
	 * 发起一个针对 跨来源资源共享
	 * 的请求（要求服务器在回应中加入一个‘访问控制-允许来源’（'Access-Control-Allow-Origin'）字段）。 Origin:
	 * http://www.example-social-network.com Permanent: standard
	 */
	public static final String Origin = "Origin";
	/**
	 * 与具体的实现相关，这些字段可能在请求/回应链中的任何时候产生 多种效果。 Pragma: no-cache Permanent
	 */
	public static final String Pragma = "Pragma";
	/**
	 * 用来向代理进行认证的认证信息。 Proxy-Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==
	 * Permanent
	 */
	public static final String Proxy_Authorization = "Proxy-Authorization";
	/**
	 * 仅请求某个实体的一部分。字节偏移以0开始。参考 字节服务 。 Range: bytes=500-999 Permanent
	 */
	public static final String Range = "Range";
	/**
	 * 表示浏览器所访问的前一个页面，正是那个页面上的某个链接将浏览器带到了当前所请求的这个页面。 (“引导者”（“referrer”）这个单词，在RFC
	 * 中被拼错了，因此在大部分的软件实现中也拼错了，以至于，错误的拼法成为了标准的用法，还被当成了正确的术语) Referer:
	 * http://en.wikipedia.org/wiki/Main_Page Permanent
	 */
	public static final String Referer = "Referer";
	/**
	 * 浏览器预期接受的传输编码方式：可使用回应协议头Transfer-Encoding 字段中的那些值，另外还有一个值可用，"trailers"（与
	 * " 分 块 "传输方式相关），用来表明，浏览器希望在最后一个尺寸为0的块之后还接收到一些额外的字段。 TE: trailers, deflate
	 * Permanent
	 */
	public static final String TE = "TE";
	/**
	 * 浏览器的浏览器身份标识字符串 User-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:12.0)
	 * Gecko/20100101 Firefox/21.0 Permanent
	 */
	public static final String User_Agent = "User-Agent";
	/**
	 * 要求服务器升级到另一个协议。 Upgrade: HTTP/2.0, SHTTP/1.3, IRC/6.9, RTA/x11 Permanent
	 */
	public static final String Upgrade = "Upgrade";
	/**
	 * 向服务器告知，这个请求是由哪些代理发出的。 Via: 1.0 fred, 1.1 example.com (Apache/1.1)
	 * Permanent
	 */
	public static final String Via = "Via";
	/**
	 * 一个一般性的警告，告知，在实体内容体中可能存在错误。 Warning: 199 Miscellaneous warning Permanent
	 */
	public static final String Warning = "Warning";
	/**
	 * 主要用于标识 Ajax 及可扩展标记语言 请求。大部分的Javascript框架 会发送这个字段，且将其值设置为 XMLHttpRequest
	 * X-Requested-With: XMLHttpRequest
	 */
	public static final String X_Requested_With = "X-Requested-With";
	/**
	 * 请求某个网页应用程序停止跟踪某个用户。在火狐浏览器中，相当于X-Do-Not-Track协议头字段(自 火狐4.0 测试（Beta）
	 * 11版开始支持)。 Safari 和 Internet Explorer 9 也支持这个字段。在2011 年三月7
	 * 日，有人向互联网工程任务组提交了一个草案。 万维网协会 的跟踪保护工作组正在就此制作一项规范。 DNT: 1 (Do Not Track
	 * Enabled) DNT: 0 (Do Not Track Disabled)
	 */
	public static final String DNT = "DNT";
	/**
	 * 一个事实标准 ，用于标识某个通过超文本传输协议代理或负载均衡连接到某个网页服务器的客户端的原始互联网地址 X-Forwarded-For:
	 * client1, proxy1, proxy2 X-Forwarded-For: 129.78.138.66, 129.78.64.103
	 * 
	 */
	public static final String X_Forwarded_For = "X-Forwarded-For";
	/**
	 * a de facto standard for identifying the original host requested by the
	 * client in the Host HTTP request header, since the host name and/or port
	 * of the reverse proxy (load balancer) may differ from the origin server
	 * handling the request. X-Forwarded-Host: en.wikipedia.org:80
	 * X-Forwarded-Host: en.wikipedia.org
	 * 
	 */
	public static final String X_Forwarded_Host = "X-Forwarded-Host";
	/**
	 * 一个事实标准
	 * 用于标识某个超文本传输协议请求最初所使用的协议，因为，在反向代理(负载均衡)上，即使最初发往该反向代理的请求类型是安全的超文本传输协议（HTTPS
	 * ）， 该反向代理也仍然可能会使用超文本传输协议（HTTP）来与网页服务器通信。
	 * 谷歌客户端在与谷歌服务器通信时会使用该协议头的一个替代形式（X-ProxyUser-Ip）。 X-Forwarded-Proto: https
	 * 
	 */
	public static final String X_Forwarded_Proto = "X-Forwarded-Proto";
	/**
	 * Non-standard header field used by Microsoft applications and
	 * load-balancers Front-End-Https: on
	 * 
	 */
	public static final String Front_End_Https = "Front-End-Https";
	/**
	 * 请求某个网页应用程序使用该协议头字段中指定的方法（一般是PUT或DELETE）来覆盖掉在请求中所指定的方法（一般是POST）。
	 * 当某个浏览器或防火墙阻止直接发送PUT 或DELETE
	 * 方法时（注意，这可能是因为软件中的某个漏洞，因而需要修复，也可能是因为某个配置选项就是如此要求的，因而不应当设法绕过），可使用这种方式。
	 * X-HTTP-Method-Override: DELETE
	 * 
	 */
	public static final String X_Http_Method_Override = "X-Http-Method-Override";
	/**
	 * Allows easier parsing of the MakeModel/Firmware that is usually found in
	 * the User-Agent String of AT&T Devices X-Att-Deviceid: GT-P7320/P7320XXLPG
	 * 
	 */
	public static final String X_ATT_DeviceId = "X-ATT-DeviceId";
	/**
	 * Links to an XML file on the Internet with a full description and details
	 * about the device currently connecting. In the example to the right is an
	 * XML file for an AT&T Samsung Galaxy S2. x-wap-profile:
	 * http://wap.samsungmobile.com/uaprof/SGH-I777.xml
	 * 
	 */
	public static final String X_Wap_Profile = "X-Wap-Profile";
	/**
	 * 因为对超文本传输协议规范的误解而实现的一个协议头。
	 * 因为早期超文本传输协议版本实现中的错误而出现。与标准的连接（Connection）字段的功能完全相同。 Proxy-Connection:
	 * keep-alive
	 * 
	 */
	public static final String Proxy_Connection = "Proxy-Connection";
	/**
	 * Server-side deep packet insertion of a unique ID identifying customers of
	 * Verizon Wireless; also known as "perma-cookie" or "supercookie" X-UIDH:
	 * ...
	 * 
	 */
	public static final String X_UIDH = "X-UIDH";
	/**
	 * Used to prevent cross-site request forgery. Alternative header names are:
	 * X-CSRFToken[21] and X-XSRF-TOKEN[22] X-Csrf-Token:
	 * i8XNjC4b8KVok4uw5RftR38Wgp2BFwql
	 * 
	 */
	public static final String X_Csrf_Token = "X-Csrf-Token";


	public String getRequestMethod();
	public String getRequestUri();
	public String getHttpVersion();

	public Map<String, String> getRequestHeads();
	public String getRequestHead(String key);
	public String getContentLength();
	public String getContentType();
	public String getCharset();

	public Cookie[] getCookies();
	
	public HttpRequestBody getRequestBody()throws IOException;

	public String getParamerValue(String paramer);
/////////////////////////////////
//
//	public int readFromBody(byte[] buffer) throws IOException;
//
//	public String dumpHead();
//
//	public String dumpBody();
//
//	public MultiPartForm getMultiPartForm();
}
