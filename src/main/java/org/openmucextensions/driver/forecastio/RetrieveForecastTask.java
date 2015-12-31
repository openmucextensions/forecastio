package org.openmucextensions.driver.forecastio;

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dvdme.ForecastIOLib.FIOCurrently;
import com.github.dvdme.ForecastIOLib.FIODaily;
import com.github.dvdme.ForecastIOLib.FIOHourly;
import com.github.dvdme.ForecastIOLib.ForecastIO;

public class RetrieveForecastTask extends TimerTask {

	private static final Logger logger = LoggerFactory.getLogger(ForecastIODriver.class);
	
	private final ForecastIO provider;
	private final ForecastReceivedCallback callback;
	private final String latitude;
	private final String longitude;
	
	public RetrieveForecastTask(final ForecastIO provider, final String latitude,
			final String longitude, final ForecastReceivedCallback callback) {
		this.provider = provider;
		this.callback = callback;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	@Override
	public void run() {
		
		if(provider.getForecast(latitude, longitude)) {
			
			FIOCurrently currentlyForecast = new FIOCurrently(provider);
			FIODaily dailyForecast = new FIODaily(provider);
			FIOHourly hourlyForecast = new FIOHourly(provider);
			
			logger.debug("Received new weather forecast from forecast.io");
			callback.forecastReceived(currentlyForecast, dailyForecast, hourlyForecast);
			
		} else {
			logger.error("An error occured while trying to retrieve forecast from forecast.io");
		}	
	}
}
