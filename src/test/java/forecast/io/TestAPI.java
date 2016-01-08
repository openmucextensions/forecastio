package forecast.io;

import com.github.dvdme.ForecastIOLib.FIODaily;
import com.github.dvdme.ForecastIOLib.FIODataPoint;
import com.github.dvdme.ForecastIOLib.ForecastIO;

public class TestAPI {

	public static final String API_KEY = "enter_your_api_key_here";
	
	// coordinates of Salzburg/Austria
	public static final String LATITUDE = "47.815470";
	public static final String LONGITUDE = "13.046815";
	
	public static void main(String[] args) {
		
		ForecastIO forecast = new ForecastIO(API_KEY);
		forecast.setUnits(ForecastIO.UNITS_SI);
		forecast.setExcludeURL("hourly,minutely");
		
		if(forecast.getForecast(LATITUDE, LONGITUDE)) {
			
			System.out.println("Forecast received successfully");
			
			FIODaily daily = new FIODaily(forecast);
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
			
//			FIOHourly hourly = new FIOHourly(forecast);
//			System.out.println("Got hourly forecast for the next " + hourly.hours() + " hours");
//			
//			for (String field : hourly.getHour(0).getFieldsArray()) {
//				System.out.println(field);
//			}
//			
//			
//			for(int hour = 0; hour<hourly.hours(); hour++) {
//				String temperature = hourly.getHour(hour).getByKey("temperature");
//				String time = hourly.getHour(hour).getByKey("time");
//				System.out.println(time + ": " + temperature + " degree C");
//			}
			
			
		} else {
			System.err.println("Error while retrieving forecast");
		}
		
		
		
	}

}
