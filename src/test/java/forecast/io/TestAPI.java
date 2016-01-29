package forecast.io;

import com.github.dvdme.ForecastIOLib.FIODaily;
import com.github.dvdme.ForecastIOLib.FIOHourly;
import com.github.dvdme.ForecastIOLib.ForecastIO;
import com.github.dvdme.ForecastIOLib.FIODataPoint;

public class TestAPI {

	public static final String API_KEY = "your_api_key";
	
	// e.g. coordinates of Salzburg/Austria
	public static final String LATITUDE = "47.815470";
	public static final String LONGITUDE = "13.046815";
	private static final String PROXYNAME = "your_proxy_hostname";
	private static final int PROXYPORT = 8080; //your_proxy_port
	
	public static void main(String[] args) {
		
		ForecastIO forecast = new ForecastIO(API_KEY);
		forecast.setUnits(ForecastIO.UNITS_SI);
		forecast.setExcludeURL("minutely");
		forecast.setHTTPProxy(PROXYNAME, PROXYPORT);
		
		if(forecast.getForecast(LATITUDE, LONGITUDE)) {
			
			System.out.println("Forecast received successfully");
			System.out.println("");
			
			//Response Headers info
			System.out.println("Response Headers:");
			System.out.println("Cache-Control: "+forecast.getHeaderCache_Control());
			System.out.println("Expires: "+forecast.getHeaderExpires());
			System.out.println("X-Forecast-API-Calls: "+forecast.getHeaderX_Forecast_API_Calls());
			System.out.println("X-Response-Time: "+forecast.getHeaderX_Response_Time());
			System.out.println("");
			
			//ForecastIO info
			System.out.println("Latitude: "+forecast.getLatitude());
			System.out.println("Longitude: "+forecast.getLongitude());
			System.out.println("Timezone: "+forecast.getTimezone());
			System.out.println("Offset: "+forecast.offsetValue());
			System.out.println("");
			
			FIODaily daily = new FIODaily(forecast);
			System.out.println("");
			System.out.println("Got forecast for " + daily.days() + " days");
			
//			// print fields
//			for (String field : daily.getDay(0).getFieldsArray()) {
//				System.out.println(field);
//			}
			
			for(int day=0; day<daily.days(); day++) {
				FIODataPoint data = daily.getDay(day);
				
				System.out.println("Forecast for " + data.getByKey("time"));
				System.out.println("Minimum temperature at " + data.getByKey("temperatureMinTime") + ": " + data.getByKey("temperatureMin"));
				System.out.println("Maximum temperature at " + data.getByKey("temperatureMaxTime") + ": " + data.getByKey("temperatureMax"));
				
				
			}
			
			FIOHourly hourly = new FIOHourly(forecast);
			System.out.println("");
			System.out.println("Got hourly forecast for the next " + hourly.hours() + " hours");
			System.out.println("");
			
			System.out.println("*** Fields for hourly: ***");
			for (String field : hourly.getHour(0).getFieldsArray()) {
				System.out.println(field);
			}
			System.out.println("");
			
			System.out.println("*** Forecast (time, temperature, cloud cover) ***");
			for(int hour = 0; hour<hourly.hours(); hour++) {
				String temperature = hourly.getHour(hour).getByKey("temperature");
				String time = hourly.getHour(hour).getByKey("time");
				String cloud = hourly.getHour(hour).getByKey("cloudCover");
				System.out.println(time + ": " + temperature + " degree C; CC: " + cloud);
			}
			
			
		} else {
			System.err.println("Error while retrieving forecast");
		}
		
		
		
	}

}
