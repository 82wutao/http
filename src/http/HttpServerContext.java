package http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import http.api.ServerContext;
import http.api.WebAppContext;
import http.base.Alias;

public class HttpServerContext implements ServerContext {
	private String wwwDir;
	private String workerThread;
	private Alias alias = null;

	private Map<String, String> mimes = new HashMap<String, String>();

	public HttpServerContext() {

	}

	@Override
	public void initial(File file) {
		if (!file.exists()) {
			System.exit(1);
		}
		FileInputStream fileInputStream = null;
		Properties properties = new Properties();
		try {
			fileInputStream = new FileInputStream(file);
			properties.load(fileInputStream);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} finally {
			try {
				fileInputStream.close();
			} catch (IOException e) {
			}
		}
		wwwDir = (String) properties.get("wwwRoot");
		workerThread = (String) properties.get("workerThread");

		alias = new Alias(this);
		Set<Entry<Object, Object>> prop = properties.entrySet();
		for (Entry<Object, Object> e : prop) {
			String name = (String) e.getKey();
			if (!name.startsWith("alias.")) {
				continue;
			}
			String[] fix_value = name.split("\\.");
			alias.addAlia(fix_value[1], (String) e.getValue());
		}

		mimes.put("#.*", "application/octet-stream");
		mimes.put("#.", "application/x-");

		mimes.put("001", "application/x-001");
		mimes.put("301", "application/x-301");
		mimes.put("323", "text/h323");
		mimes.put("906", "application/x-906");
		mimes.put("907", "drawing/907");

		mimes.put("a11", "application/x-a11");
		mimes.put("acp", "audio/x-mei-aac");
		mimes.put("ai", "application/postscript");
		mimes.put("aif", "audio/aiff");
		mimes.put("aifc", "audio/aiff");
		mimes.put("aiff", "audio/aiff");
		mimes.put("anv", "application/x-anv");
		mimes.put("apk", "application/vnd.android.package-archive");
		mimes.put("asa", "text/asa");
		mimes.put("asf", "video/x-ms-asf");
		mimes.put("asp", "text/asp");
		mimes.put("asx", "video/x-ms-asf");
		mimes.put("au", "audio/basic");
		mimes.put("avi", "video/avi");
		mimes.put("wf", "application/vnd.adobe.workflow");

		mimes.put("biz", "text/xml");
		mimes.put("bmp", "application/x-bmp");
		mimes.put("bot", "application/x-bot");

		mimes.put("c4t", "application/x-c4t");
		mimes.put("c90", "application/x-c90");
		mimes.put("cal", "application/x-cals");
		mimes.put("cat", "application/vnd.ms-pki.seccat");
		mimes.put("cdf", "application/x-netcdf");
		mimes.put("cdr", "application/x-cdr");
		mimes.put("cel", "application/x-cel");
		mimes.put("cer", "application/x-x509-ca-cert");
		mimes.put("cg4", "application/x-g4");
		mimes.put("cgm", "application/x-cgm");
		mimes.put("cit", "application/x-cit");
		mimes.put("class", "java/*");
		mimes.put("cml", "text/xml");
		mimes.put("cmp", "application/x-cmp");
		mimes.put("cmx", "application/x-cmx");
		mimes.put("cot", "application/x-cot");
		mimes.put("crl", "application/pkix-crl");
		mimes.put("crt", "application/x-x509-ca-cert");
		mimes.put("csi", "application/x-csi");
		mimes.put("css", "text/css");
		mimes.put("cut", "application/x-cut");

		mimes.put("dbf", "application/x-dbf");
		mimes.put("dbm", "application/x-dbm");
		mimes.put("dbx", "application/x-dbx");
		mimes.put("dcd", "text/xml");
		mimes.put("dcx", "application/x-dcx");
		mimes.put("der", "application/x-x509-ca-cert");
		mimes.put("dgn", "application/x-dgn");
		mimes.put("dib", "application/x-dib");
		mimes.put("dll", "application/x-msdownload");
		mimes.put("doc", "application/msword");
		mimes.put("dot", "application/msword");
		mimes.put("drw", "application/x-drw");
		mimes.put("dtd", "text/xml");
		mimes.put("dwf", "Model/vnd.dwf application/x-dwf");
		mimes.put("dwg", "application/x-dwg");
		mimes.put("dxb", "application/x-dxb");
		mimes.put("dxf", "application/x-dxf");

		mimes.put("edn", "application/vnd.adobe.edn");
		mimes.put("emf", "application/x-emf");
		mimes.put("eml", "message/rfc822");
		mimes.put("ent", "text/xml");
		mimes.put("epi", "application/x-epi");
		mimes.put("eps", "application/x-ps application/postscript");
		mimes.put("etd", "application/x-ebx");
		mimes.put("exe", "application/x-msdownload");

		mimes.put("fax", "image/fax");
		mimes.put("fdf", "application/vnd.fdf");
		mimes.put("fif", "application/fractals");
		mimes.put("fo", "text/xml");
		mimes.put("frm", "application/x-frm");

		mimes.put("g4", "application/x-g4");
		mimes.put("gbr", "application/x-gbr");
		mimes.put("gif", "image/gif");
		mimes.put("gl2", "application/x-gl2");
		mimes.put("gp4", "application/x-gp4");

		mimes.put("hgl", "application/x-hgl");
		mimes.put("hmr", "application/x-hmr");
		mimes.put("hpg", "application/x-hpgl");
		mimes.put("hpl", "application/x-hpl");
		mimes.put("hqx", "application/mac-binhex40");
		mimes.put("hrf", "application/x-hrf");
		mimes.put("hta", "application/hta");
		mimes.put("htc", "text/x-component");
		mimes.put("htm", "text/html");
		mimes.put("html", "text/html");
		mimes.put("htt", "text/webviewhtml");
		mimes.put("htx", "text/html");

		mimes.put("icb", "application/x-icb");
		mimes.put("ico", "image/x-icon application/x-ico");
		mimes.put("iff", "application/x-iff");
		mimes.put("ig4", "application/x-g4");
		mimes.put("igs", "application/x-igs");
		mimes.put("iii", "application/x-iphone");
		mimes.put("img", "application/x-img");
		mimes.put("ins", "application/x-internet-signup");
		mimes.put("ipa", "application/vnd.iphone");
		mimes.put("isp", "application/x-internet-signup");
		mimes.put("IVF", "video/x-ivf");

		mimes.put("java", "java/*");
		mimes.put("jfif", "image/jpeg");
		mimes.put("jpe", "image/jpeg application/x-jpe");
		mimes.put("jpeg", "image/jpeg");
		mimes.put("jpg", "image/jpeg application/x-jpg");
		mimes.put("js", "application/x-javascript");
		mimes.put("jsp", "text/html");

		mimes.put("la1", "audio/x-liquid-file");
		mimes.put("lar", "application/x-laplayer-reg");
		mimes.put("latex", "application/x-latex");
		mimes.put("lavs", "audio/x-liquid-secure");
		mimes.put("lbm", "application/x-lbm");
		mimes.put("lmsff", "audio/x-la-lms");
		mimes.put("ls", "application/x-javascript");
		mimes.put("ltr", "application/x-ltr");

		mimes.put("m1v", "video/x-mpeg");
		mimes.put("m2v", "video/x-mpeg");
		mimes.put("m3u", "audio/mpegurl");
		mimes.put("m4e", "video/mpeg4");
		mimes.put("mac", "application/x-mac");
		mimes.put("man", "application/x-troff-man");
		mimes.put("math", "text/xml");
		mimes.put("mdb", "application/msaccess application/x-mdb");
		mimes.put("mfp", "application/x-shockwave-flash");
		mimes.put("mht", "message/rfc822");
		mimes.put("mhtml", "message/rfc822");
		mimes.put("mi", "application/x-mi");
		mimes.put("mid", "audio/mid");
		mimes.put("midi", "audio/mid");
		mimes.put("mil", "application/x-mil");
		mimes.put("mml", "text/xml");
		mimes.put("mnd", "audio/x-musicnet-download");
		mimes.put("mns", "audio/x-musicnet-stream");
		mimes.put("mocha", "application/x-javascript");
		mimes.put("movie", "video/x-sgi-movie");
		mimes.put("mp1", "audio/mp1");
		mimes.put("mp2", "audio/mp2");
		mimes.put("mp2v", "video/mpeg");
		mimes.put("mp3", "audio/mp3");
		mimes.put("mp4", "video/mpeg4");
		mimes.put("mpa", "video/x-mpg");
		mimes.put("mpd", "application/vnd.ms-project");
		mimes.put("mpe", "video/x-mpeg");
		mimes.put("mpeg", "video/mpg");
		mimes.put("mpg", "video/mpg");
		mimes.put("mpga", "audio/rn-mpeg");
		mimes.put("mpp", "application/vnd.ms-project");
		mimes.put("mps", "video/x-mpeg");
		mimes.put("mpt", "application/vnd.ms-project");
		mimes.put("mpv", "video/mpg");
		mimes.put("mpv2", "video/mpeg");
		mimes.put("mpw", "application/vnd.ms-project");
		mimes.put("mpx", "application/vnd.ms-project");
		mimes.put("mtx", "text/xml");
		mimes.put("mxp", "application/x-mmxp");

		mimes.put("net", "image/pnetvue");
		mimes.put("nrf", "application/x-nrf");
		mimes.put("nws", "message/rfc822");

		mimes.put("odc", "text/x-ms-odc");
		mimes.put("out", "application/x-out");

		mimes.put("p10", "application/pkcs10");
		mimes.put("p12", "application/x-pkcs12");
		mimes.put("p7b", "application/x-pkcs7-certificates");
		mimes.put("p7c", "application/pkcs7-mime");
		mimes.put("p7m", "application/pkcs7-mime");
		mimes.put("p7r", "application/x-pkcs7-certreqresp");
		mimes.put("p7s", "application/pkcs7-signature");
		mimes.put("pc5", "application/x-pc5");
		mimes.put("pci", "application/x-pci");
		mimes.put("pcl", "application/x-pcl");
		mimes.put("pcx", "application/x-pcx");
		mimes.put("pdf", "application/pdf");
		mimes.put("pdx", "application/vnd.adobe.pdx");
		mimes.put("pfx", "application/x-pkcs12");
		mimes.put("pgl", "application/x-pgl");
		mimes.put("pic", "application/x-pic");
		mimes.put("pko", "application/vnd.ms-pki.pko");
		mimes.put("pl", "application/x-perl");
		mimes.put("plg", "text/html");
		mimes.put("pls", "audio/scpls");
		mimes.put("plt", "application/x-plt");
		mimes.put("png", "image/png application/x-png");
		mimes.put("pot", "application/vnd.ms-powerpoint");
		mimes.put("ppa", "application/vnd.ms-powerpoint");
		mimes.put("ppm", "application/x-ppm");
		mimes.put("pps", "application/vnd.ms-powerpoint");
		mimes.put("ppt", "application/vnd.ms-powerpoint application/x-ppt");
		mimes.put("pr", "application/x-pr");
		mimes.put("prf", "application/pics-rules");
		mimes.put("prn", "application/x-prn");
		mimes.put("prt", "application/x-prt");
		mimes.put("ps", "application/x-ps application/postscript");
		mimes.put("ptn", "application/x-ptn");
		mimes.put("pwz", "application/vnd.ms-powerpoint");

		mimes.put("r3t", "text/vnd.rn-realtext3d");
		mimes.put("ra", "audio/vnd.rn-realaudio");
		mimes.put("ram", "audio/x-pn-realaudio");
		mimes.put("ras", "application/x-ras");
		mimes.put("rat", "application/rat-file");
		mimes.put("rdf", "text/xml");
		mimes.put("rec", "application/vnd.rn-recording");
		mimes.put("red", "application/x-red");
		mimes.put("rgb", "application/x-rgb");
		mimes.put("rjs", "application/vnd.rn-realsystem-rjs");
		mimes.put("rjt", "application/vnd.rn-realsystem-rjt");
		mimes.put("rlc", "application/x-rlc");
		mimes.put("rle", "application/x-rle");
		mimes.put("rm", "application/vnd.rn-realmedia");
		mimes.put("rmf", "application/vnd.adobe.rmf");
		mimes.put("rmi", "audio/mid");
		mimes.put("rmj", "application/vnd.rn-realsystem-rmj");
		mimes.put("rmm", "audio/x-pn-realaudio");
		mimes.put("rmp", "application/vnd.rn-rn_music_package");
		mimes.put("rms", "application/vnd.rn-realmedia-secure");
		mimes.put("rmvb", "application/vnd.rn-realmedia-vbr");
		mimes.put("rmx", "application/vnd.rn-realsystem-rmx");
		mimes.put("rnx", "application/vnd.rn-realplayer");
		mimes.put("rp", "image/vnd.rn-realpix");
		mimes.put("rpm", "audio/x-pn-realaudio-plugin");
		mimes.put("rsml", "application/vnd.rn-rsml");
		mimes.put("rt", "text/vnd.rn-realtext");
		mimes.put("rtf", "application/msword application/x-rtf");
		mimes.put("rv", "video/vnd.rn-realvideo");

		mimes.put("sam", "application/x-sam");
		mimes.put("sat", "application/x-sat");
		mimes.put("sdp", "application/sdp");
		mimes.put("sdw", "application/x-sdw");
		mimes.put("sis", "application/vnd.symbian.install");
		mimes.put("sisx", "application/vnd.symbian.install");
		mimes.put("sit", "application/x-stuffit");
		mimes.put("slb", "application/x-slb");
		mimes.put("sld", "application/x-sld");
		mimes.put("slk", "drawing/x-slk");
		mimes.put("smi", "application/smil");
		mimes.put("smil", "application/smil");
		mimes.put("smk", "application/x-smk");
		mimes.put("snd", "audio/basic");
		mimes.put("sol", "text/plain");
		mimes.put("sor", "text/plain");
		mimes.put("spc", "application/x-pkcs7-certificates");
		mimes.put("spl", "application/futuresplash");
		mimes.put("spp", "text/xml");
		mimes.put("ssm", "application/streamingmedia");
		mimes.put("sst", "application/vnd.ms-pki.certstore");
		mimes.put("stl", "application/vnd.ms-pki.stl");
		mimes.put("stm", "text/html");
		mimes.put("sty", "application/x-sty");
		mimes.put("svg", "text/xml");
		mimes.put("swf", "application/x-shockwave-flash");

		mimes.put("tdf", "application/x-tdf");
		mimes.put("tg4", "application/x-tg4");
		mimes.put("tga", "application/x-tga");
		mimes.put("tif", "image/tiff application/x-tif");
		mimes.put("tiff", "image/tiff");
		mimes.put("tld", "text/xml");
		mimes.put("top", "drawing/x-top");
		mimes.put("torrent", "application/x-bittorrent");
		mimes.put("tsd", "text/xml");
		mimes.put("txt", "text/plain");
		mimes.put("uin", "application/x-icq");
		mimes.put("uls", "text/iuls");

		mimes.put("vcf", "text/x-vcard");
		mimes.put("vda", "application/x-vda");
		mimes.put("vdx", "application/vnd.visio");
		mimes.put("vml", "text/xml");
		mimes.put("vpg", "application/x-vpeg005");
		mimes.put("vsd", "application/vnd.visio application/x-vsd");
		mimes.put("vss", "application/vnd.visio");
		mimes.put("vst", "application/vnd.visio application/x-vst");
		mimes.put("vsw", "application/vnd.visio");
		mimes.put("vsx", "application/vnd.visio");
		mimes.put("vtx", "application/vnd.visio");
		mimes.put("vxml", "text/xml");

		mimes.put("wav", "audio/wav");
		mimes.put("wax", "audio/x-ms-wax");
		mimes.put("wb1", "application/x-wb1");
		mimes.put("wb2", "application/x-wb2");
		mimes.put("wb3", "application/x-wb3");
		mimes.put("wbmp", "image/vnd.wap.wbmp");
		mimes.put("wiz", "application/msword");
		mimes.put("wk3", "application/x-wk3");
		mimes.put("wk4", "application/x-wk4");
		mimes.put("wkq", "application/x-wkq");
		mimes.put("wks", "application/x-wks");
		mimes.put("wm", "video/x-ms-wm");
		mimes.put("wma", "audio/x-ms-wma");
		mimes.put("wmd", "application/x-ms-wmd");
		mimes.put("wmf", "application/x-wmf");
		mimes.put("wml", "text/vnd.wap.wml");
		mimes.put("wmv", "video/x-ms-wmv");
		mimes.put("wmx", "video/x-ms-wmx");
		mimes.put("wmz", "application/x-ms-wmz");
		mimes.put("wp6", "application/x-wp6");
		mimes.put("wpd", "application/x-wpd");
		mimes.put("wpg", "application/x-wpg");
		mimes.put("wpl", "application/vnd.ms-wpl");
		mimes.put("wq1", "application/x-wq1");
		mimes.put("wr1", "application/x-wr1");
		mimes.put("wri", "application/x-wri");
		mimes.put("wrk", "application/x-wrk");
		mimes.put("ws", "application/x-ws");
		mimes.put("ws2", "application/x-ws");
		mimes.put("wsc", "text/scriptlet");
		mimes.put("wsdl", "text/xml");
		mimes.put("wvx", "video/x-ms-wvx");

		mimes.put("xap", "application/x-silverlight-app");
		mimes.put("xdp", "application/vnd.adobe.xdp");
		mimes.put("xdr", "text/xml");
		mimes.put("xfd", "application/vnd.adobe.xfd");
		mimes.put("xfdf", "application/vnd.adobe.xfdf");
		mimes.put("xhtml", "text/html");
		mimes.put("xls", "application/vnd.ms-excel");
		mimes.put("xls", "application/x-xls");
		mimes.put("xlw", "application/x-xlw");
		mimes.put("xml", "text/xml");
		mimes.put("xpl", "audio/scpls");
		mimes.put("xq", "text/xml");
		mimes.put("xql", "text/xml");
		mimes.put("xquery", "text/xml");
		mimes.put("xsd", "text/xml");
		mimes.put("xsl", "text/xml");
		mimes.put("xslt", "text/xml");
		mimes.put("xwd", "application/x-xwd");
		mimes.put("x_b", "application/x-x_b");
		mimes.put("x_t", "application/x-x_t");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see http.HttpServletContext#destory()
	 */
	@Override
	public void destory() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see http.HttpServletContext#mappingAppContext(java.lang.String)
	 */
	@Override
	public WebAppContext mappingAppContext(String url) {
		return alias.getWebApp(url);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see http.HttpServletContext#getWwwDir()
	 */
	@Override
	public String getWwwDir() {
		return wwwDir;
	}

	@Override
	public int getWorkerThreads() {
		return Integer.parseInt(workerThread);
	}

	@Override
	public String getMimeType(String resourceType) {
		return mimes.get(resourceType);
	}
}
