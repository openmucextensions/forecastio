package forecast.io;

import com.github.dvdme.ForecastIOLib.FIOCurrently;
import com.github.dvdme.ForecastIOLib.FIODaily;
import com.github.dvdme.ForecastIOLib.FIOHourly;
import com.github.dvdme.ForecastIOLib.ForecastIO;

public class ShowFields {

	public static final String API_KEY = "d472db24f0f741166c62dbcdb65b3181";
	
	// coordinates of Salzburg/Austria
	public static final String LATITUDE = "47.815470";
	public static final String LONGITUDE = "13.046815";
	
	public static void main(String[] args) {
		
		ForecastIO forecast = new ForecastIO(API_KEY);
		forecast.setUnits(ForecastIO.UNITS_SI);
		forecast.setExcludeURL("minutely");
		
		if(forecast.getForecast(LATITUDE, LONGITUDE)) {
			
			FIOCurrently currently = new FIOCurrently(forecast);
			System.out.println("*** Fields for currently: ***");
			for (String field : currently.get().getFieldsArray()) {
				System.out.println(field);
			}
			
			FIODaily daily = new FIODaily(forecast);
			System.out.println("*** Fields for daily: ***");
			for (String field : daily.getDay(0).getFieldsArray()) {
				System.out.println(field);
			}
			
			FIOHourly hourly = new FIOHourly(forecast);
			System.out.println("*** Fields for hourly: ***");
			for (String field : hourly.getHour(0).getFieldsArray()) {
				System.out.println(field);
			}
		}

	}

}
