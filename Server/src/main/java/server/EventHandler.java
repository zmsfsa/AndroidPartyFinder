package main.java.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.Event;
import main.java.Include;
import main.java.User;
import main.java.WorkSql;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class EventHandler implements HttpHandler {

	private static final int HTTP_OK_STATUS = 200;
	private static final String EVENT_IS_BAD = "No such event";
	private static final String DATE_DELIMETR = "-";
	private static final String DEL = "/";
	private static final String OK = "OK";

	private static final String PHOTO = "photo";
	private static final String MAKE_JOIN = "makeJoin";
	private static final String CHECK_IN = "checkIn";
	private static final String WIDTH = "width";
	private static final String HEIGHT = "height";
	private static final String ADDR = "addr";
	private static final String EVENT_NAME = "eventName";
	private static final String FNAME = "fName";
	private static final String LNAME = "lName";
	private static final String DELIMETR = "=";
	private static final String AND = "&";
	private static final String LOGIN = "login";
	private static final String DATE = "date";
	private static final String IN = "in";

	@Override
	public void handle(HttpExchange t) throws IOException {

		System.out.println("EventHandler");
		InputStream is = t.getRequestBody();
		byte[] b = new byte[is.available()];
		is.read(b);
		Map<String, String> params = new HashMap<String, String>();
		params = Mapper.queryToMap(new String(b));

		WorkSql work = new WorkSql();
		Event event = work.getEventByName(params.get(EVENT_NAME));
		if (event == null) {
			t.sendResponseHeaders(HTTP_OK_STATUS,
					EVENT_IS_BAD.getBytes().length);
			OutputStream os = t.getResponseBody();
			os.write(EVENT_IS_BAD.getBytes());
			os.close();
		} else {
			// to join
			if (params.get(MAKE_JOIN).equals("YES")) {
				work.makeInclude(params.get(LOGIN), params.get(EVENT_NAME));
				t.sendResponseHeaders(HTTP_OK_STATUS, OK.getBytes().length);
				OutputStream os = t.getResponseBody();
				os.write(OK.getBytes());
				os.close();
				return;
			}
			// to checkIn
			if (params.get(CHECK_IN).equals("YES")) {
				work.checkIn(params.get(LOGIN), params.get(EVENT_NAME),
						params.get("width"), params.get("height"));
				t.sendResponseHeaders(HTTP_OK_STATUS, OK.getBytes().length);
				OutputStream os = t.getResponseBody();
				os.write(OK.getBytes());
				os.close();
				return;
			}
			// to show page
			Calendar calendar = event.getDateEvent();
			int month = calendar.get(Calendar.MONTH) + 1;
			StringBuilder sendBuilder = new StringBuilder("");
			List<Include> checkList = work.getIncludeByEvent(event
					.getNameEvent());
			int k = 0;
			int idUser = work.getUserByLogin(params.get("login")).getIdUser();

			for (Include a : checkList)
				if (a.getIdUser() == idUser)
					k = 1;
			User user = work.getUserByLogin(params.get(LOGIN));
			if (k == 1) {
				int photoId = user.getPhotoId();
				Include checkIn = work.getInclude(params.get(LOGIN),
						params.get(EVENT_NAME));

				sendBuilder.append("myWidth" + DELIMETR + checkIn.getWidth()
						+ AND + "myHeight" + DELIMETR + checkIn.getHeight()
						+ AND + EVENT_NAME + DELIMETR + event.getNameEvent()
						+ AND + ADDR + DELIMETR + event.getAddres() + AND + IN
						+ DELIMETR + 1 + AND + DATE + DELIMETR
						+ calendar.get(Calendar.DATE) + DATE_DELIMETR + month
						+ DATE_DELIMETR + calendar.get(Calendar.YEAR) + AND
						+ "coodrinates" + DELIMETR);
				for (Include a : checkList) {
					if (idUser != a.getIdUser())
							sendBuilder.append(a.getWidth() + "-" + a.getHeight()  + " ");
				}
			} else {
				sendBuilder.append(EVENT_NAME + DELIMETR + event.getNameEvent()
						+ AND + FNAME + DELIMETR + user.getFName() + AND
						+ LNAME + DELIMETR + user.getLName() + AND + IN
						+ DELIMETR + 0 + AND + DATE + DELIMETR
						+ calendar.get(Calendar.DATE) + DATE_DELIMETR + month
						+ DATE_DELIMETR + calendar.get(Calendar.YEAR) + DEL);
			}

			String send = new String(sendBuilder);

			System.out.println("\n" + send + "\n");

			t.sendResponseHeaders(HTTP_OK_STATUS, send.getBytes().length);
			OutputStream os = t.getResponseBody();
			os.write(send.getBytes());
			os.close();
		}
	}

}
