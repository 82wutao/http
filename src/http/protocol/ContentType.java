package http.protocol;

import java.util.HashMap;
import java.util.Map;

/*
 *  <form enctype="1|2">
 *  Content-Type: application/x-www-form-urlencoded;charset=utf-8
 *  
 *  title=test&sub%5B%5D=1&sub%5B%5D=2&sub%5B%5D=3
 *  -----------------------------------------------------------------------------------------
 *  Content-Type:multipart/form-data; boundary=----WebKitFormBoundaryrGKCBY7qhFd3TrwA
 *  
 *  ------WebKitFormBoundaryrGKCBY7qhFd3TrwA
 *  Content-Disposition: form-data; name="text"
 *  
 *  title
 *  ------WebKitFormBoundaryrGKCBY7qhFd3TrwA
 *  Content-Disposition: form-data; name="file"; filename="chrome.png"
 *  Content-Type: image/png
 *  
 *  PNG ... content of chrome.png ...
 *  ------WebKitFormBoundaryrGKCBY7qhFd3TrwA--
 *  -----------------------------------------------------------------------------------------
 *  js ajax 3|4
 *  Content-Type: application/json;charset=utf-8
 *  
 *  {"title":"test","sub":[1,2,3]}
 *  -----------------------------------------------------------------------------------------
 *  Content-Type: text/xml
 *  
 *  <?xml version="1.0"?>
 *  <methodCall>
 *  	<methodName>examples.getStateName</methodName>
 *  	<params>
 *  		<param>
 *  			<value><i4>41</i4></value>
 *  		</param>
 *  	</params>
 *  </methodCall>
 */

public class ContentType {
	
	public static final String Audio_Basic="audio/basic";
	public static final String Audio_Mpeg="audio/mpeg";
	public static final String Audio_X_aiff="audio/x-aiff";
	public static final String Audio_X_wav="audio/x-wav";
	public static final String Application_Javascript="application/javascript";
	public static final String Application_Java="application/java";
	public static final String Application_Json="application/json";
	public static final String Application_VndWapWmlc="application/vnd.wap.wmlc";
	public static final String Application_VndWapWmlscriptc="application/vnd.wap.wmlscriptc";
	public static final String Application_Xml="application/xml";
	public static final String Application_XhtmlXml="application/xhtml+xml";
	public static final String Application_X_WWWFormUrlencoded="application/x-www-form-urlencoded";
	public static final String Application_X_postscript="application/x-postscript";
	public static final String Application_X_csh="application/x-csh";
	public static final String Application_X_sh="application/x-sh";
	public static final String Application_X_tcl="application/x-tcl";
	public static final String Application_X_tex="application/x-tex";
	public static final String Application_X_texinfo="application/x-texinfo";
	public static final String Application_X_Troff="application/x-troff";
	public static final String Application_X_TroffMan="application/x-troff-man";
	public static final String Application_X_TroffMe="application/x-troff-me";
	public static final String Application_X_WaisSource="application/x-wais-source";
	public static final String Application_X_bcpio="application/x-bcpio";
	public static final String Application_X_cpio="application/x-cpio";
	public static final String Application_X_gtar="application/x-gtar";
	public static final String Application_X_shar="application/x-shar";
	public static final String Application_X_sv4cpio="application/x-sv4cpio";
	public static final String Application_X_sv4crc="application/x-sv4crc";
	public static final String Application_X_tar="application/x-tar";
	public static final String Application_X_ustar="application/x-ustar";
	public static final String Application_X_dvi="application/x-dvi";
	public static final String Application_X_hdf="application/x-hdf";
	public static final String Application_X_latex="application/x-latex";
	public static final String application_Zip="application/zip";
	public static final String Application_OctetStream="application/octet-stream";
	public static final String Application_Oda="application/oda";
	public static final String Application_Pdf="application/pdf";
	public static final String Application_Postscript="application/postscript";
	public static final String application_Rtf="application/rtf";
	public static final String Application_X_netcdf="application/x-netcdf";
	public static final String Application_X_X509CaCert="application/x-x509-ca-cert";
	public static final String Application_X_gzip="application/x-gzip";
	public static final String Application_X_compress="application/x-compress";
	public static final String Application_X_mif="application/x-mif";
	public static final String Application_Mac_Binhex40="application/mac-binhex40";
	public static final String Image_Bmp="image/bmp";
	public static final String Image_Gif="image/gif";
	public static final String Image_Ief="image/ief";
	public static final String Image_Jpeg="image/jpeg";
	public static final String Image_Png="image/png";
	public static final String Image_Tiff="image/tiff";
	public static final String Image_SvgXml="image/svg+xml";
	public static final String Image_VndWapWbmp="image/vnd.wap.wbmp";
	public static final String Image_X_CmuRaster="image/x-cmu-raster";
	public static final String Image_X_PortableAnymap="image/x-portable-anymap";
	public static final String Image_X_PortableBitmap="image/x-portable-bitmap";
	public static final String Image_X_PortableGraymap="image/x-portable-graymap";
	public static final String Image_X_PortablePixmap="image/x-portable-pixmap";
	public static final String Image_X_rgb="image/x-rgb";
	public static final String Image_X_xbitmap="image/x-xbitmap";
	public static final String Image_X_xpixmap="image/x-xpixmap";
	public static final String Image_X_xwindowdump="image/x-xwindowdump";
	public static final String Multipart_Formdata="multipart/form-data";
	public static final String Text_Css="text/css";
	public static final String Text_Html="text/html";
	public static final String Text_Plain="text/plain";
	public static final String Text_Richtext="text/richtext";
	public static final String Text_TabSeparatedValues="text/tab-separated-values";
	public static final String Text_VndWapWml="text/vnd.wap.wml";
	public static final String Text_VndWapWmlscript="text/vnd.wap.wmlscript";
	public static final String Text_Xml="text/xml";
	public static final String Text_X_setext="text/x-setext";
	public static final String Video_mpeg2="video/mpeg2";
	public static final String video_quicktime="video/quicktime";
	public static final String Video_X_Msvideo="video/x-msvideo";
	public static final String Video_X_SgiMovie="video/x-sgi-movie";
	public static final String Video_X_RadScreenplay="video/x-rad-screenplay";
	public static final String XWorld_X_vrml="x-world/x-vrml";
	
	public static final Map<String, String> defaultMappings = new HashMap<String, String>();
	static{
    defaultMappings.put("txt", "text/plain");
    defaultMappings.put("css", "text/css");
    defaultMappings.put("html", "text/html");
    defaultMappings.put("htm", "text/html");
    defaultMappings.put("gif", "image/gif");
    defaultMappings.put("jpg", "image/jpeg");
    defaultMappings.put("jpe", "image/jpeg");
    defaultMappings.put("jpeg", "image/jpeg");
    defaultMappings.put("bmp", "image/bmp");
    defaultMappings.put("js", "application/javascript");
    defaultMappings.put("png", "image/png");
    defaultMappings.put("java", "text/plain");
    defaultMappings.put("body", "text/html");
    defaultMappings.put("rtx", "text/richtext");
    defaultMappings.put("tsv", "text/tab-separated-values");
    defaultMappings.put("etx", "text/x-setext");
    defaultMappings.put("json", "application/json");
    defaultMappings.put("ps", "application/x-postscript");
    defaultMappings.put("class", "application/java");
    defaultMappings.put("csh", "application/x-csh");
    defaultMappings.put("sh", "application/x-sh");
    defaultMappings.put("tcl", "application/x-tcl");
    defaultMappings.put("tex", "application/x-tex");
    defaultMappings.put("texinfo", "application/x-texinfo");
    defaultMappings.put("texi", "application/x-texinfo");
    defaultMappings.put("t", "application/x-troff");
    defaultMappings.put("tr", "application/x-troff");
    defaultMappings.put("roff", "application/x-troff");
    defaultMappings.put("man", "application/x-troff-man");
    defaultMappings.put("me", "application/x-troff-me");
    defaultMappings.put("ms", "application/x-wais-source");
    defaultMappings.put("src", "application/x-wais-source");
    defaultMappings.put("zip", "application/zip");
    defaultMappings.put("bcpio", "application/x-bcpio");
    defaultMappings.put("cpio", "application/x-cpio");
    defaultMappings.put("gtar", "application/x-gtar");
    defaultMappings.put("shar", "application/x-shar");
    defaultMappings.put("sv4cpio", "application/x-sv4cpio");
    defaultMappings.put("sv4crc", "application/x-sv4crc");
    defaultMappings.put("tar", "application/x-tar");
    defaultMappings.put("ustar", "application/x-ustar");
    defaultMappings.put("dvi", "application/x-dvi");
    defaultMappings.put("hdf", "application/x-hdf");
    defaultMappings.put("latex", "application/x-latex");
    defaultMappings.put("bin", "application/octet-stream");
    defaultMappings.put("oda", "application/oda");
    defaultMappings.put("pdf", "application/pdf");
    defaultMappings.put("ps", "application/postscript");
    defaultMappings.put("eps", "application/postscript");
    defaultMappings.put("ai", "application/postscript");
    defaultMappings.put("rtf", "application/rtf");
    defaultMappings.put("nc", "application/x-netcdf");
    defaultMappings.put("cdf", "application/x-netcdf");
    defaultMappings.put("cer", "application/x-x509-ca-cert");
    defaultMappings.put("exe", "application/octet-stream");
    defaultMappings.put("gz", "application/x-gzip");
    defaultMappings.put("Z", "application/x-compress");
    defaultMappings.put("z", "application/x-compress");
    defaultMappings.put("hqx", "application/mac-binhex40");
    defaultMappings.put("mif", "application/x-mif");
    defaultMappings.put("ief", "image/ief");
    defaultMappings.put("tiff", "image/tiff");
    defaultMappings.put("tif", "image/tiff");
    defaultMappings.put("ras", "image/x-cmu-raster");
    defaultMappings.put("pnm", "image/x-portable-anymap");
    defaultMappings.put("pbm", "image/x-portable-bitmap");
    defaultMappings.put("pgm", "image/x-portable-graymap");
    defaultMappings.put("ppm", "image/x-portable-pixmap");
    defaultMappings.put("rgb", "image/x-rgb");
    defaultMappings.put("xbm", "image/x-xbitmap");
    defaultMappings.put("xpm", "image/x-xpixmap");
    defaultMappings.put("xwd", "image/x-xwindowdump");
    defaultMappings.put("au", "audio/basic");
    defaultMappings.put("snd", "audio/basic");
    defaultMappings.put("aif", "audio/x-aiff");
    defaultMappings.put("aiff", "audio/x-aiff");
    defaultMappings.put("aifc", "audio/x-aiff");
    defaultMappings.put("wav", "audio/x-wav");
    defaultMappings.put("mp3", "audio/mpeg");
    defaultMappings.put("mpeg", "video/mpeg");
    defaultMappings.put("mpg", "video/mpeg");
    defaultMappings.put("mpe", "video/mpeg");
    defaultMappings.put("qt", "video/quicktime");
    defaultMappings.put("mov", "video/quicktime");
    defaultMappings.put("avi", "video/x-msvideo");
    defaultMappings.put("movie", "video/x-sgi-movie");
    defaultMappings.put("avx", "video/x-rad-screenplay");
    defaultMappings.put("wrl", "x-world/x-vrml");
    defaultMappings.put("mpv2", "video/mpeg2");

    /* Add XML related MIMEs */

    defaultMappings.put("xml", "application/xml");
    defaultMappings.put("xhtml", "application/xhtml+xml");
    defaultMappings.put("xsl", "application/xml");
    defaultMappings.put("svg", "image/svg+xml");
    defaultMappings.put("svgz", "image/svg+xml");
    defaultMappings.put("wbmp", "image/vnd.wap.wbmp");
    defaultMappings.put("wml", "text/vnd.wap.wml");
    defaultMappings.put("wmlc", "application/vnd.wap.wmlc");
    defaultMappings.put("wmls", "text/vnd.wap.wmlscript");
    defaultMappings.put("wmlscriptc", "application/vnd.wap.wmlscriptc");
	}
	
	public static String getMimeType(String extension){
		return defaultMappings.get(extension);
	}
}
