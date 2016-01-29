package forecast.io;

import com.github.dvdme.ForecastIOLib.FIOCurrently;
import com.github.dvdme.ForecastIOLib.FIODaily;
import com.github.dvdme.ForecastIOLib.FIOHourly;
import com.github.dvdme.ForecastIOLib.ForecastIO;

public class ShowFields {

	public static final String API_KEY = "your_api_key";
	
	// coordinates of Salzburg/Austria
	public static final String LATITUDE = "47.815470";
	public static final String LONGITUDE = "13.046815";
	// optional, if proxy has to be used, if not, PROXYNAME = null;
	private static final String PROXYNAME = "your_proxy_hostname";
	private static final int PROXYPORT = 8080; //your_proxy_port
	
	
	public static void main(String[] args) {
		
		ForecastIO forecast = new ForecastIO(API_KEY);
		forecast.setUnits(ForecastIO.UNITS_SI);
		forecast.setExcludeURL("minutely");
		forecast.setHTTPProxy(PROXYNAME, PROXYPORT);
		
		if(forecast.getForecast(LATITUDE, LONGITUDE)) {
			
			FIOCurrently currently = new FIOCurrently(forecast);
			System.out.println("*** Fields for currently: ***");
			for (String field : currently.get().getFieldsArray()) {
				System.out.println(field);
			}
			System.out.println("");
			
			FIODaily daily = new FIODaily(forecast);
			System.out.println("*** Fields for daily: ***");
			for (String field : daily.getDay(0).getFieldsArray()) {
				System.out.println(field);
			}
			System.out.println("");
			
			FIOHourly hourly = new FIOHourly(forecast);
			System.out.println("*** Fields for hourly: ***");
			for (String field : hourly.getHour(0).getFieldsArray()) {
				System.out.println(field);
			}
			System.out.println("");
		}

	}

}
