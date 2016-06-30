package http.api;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.Pair;

public class Route {
	protected List<Pair<Pattern, HttpServerlet>> servlets = new ArrayList<Pair<Pattern, HttpServerlet>>();

	public Route() {
	}

	public void registRoute(String urlPattern, HttpServerlet serverlet) {
		Pattern pattern = Pattern.compile(urlPattern);
		servlets.add(new Pair<Pattern, HttpServerlet>(pattern, serverlet));
	}

	public HttpServerlet matchHttpServerlet(String uri) {
		for (Pair<Pattern, HttpServerlet> pair : servlets) {
			Matcher matcher = pair.k.matcher(uri);
			if (matcher.matches()) {
				return pair.t;
			}
		}
		return null;
	}
}
