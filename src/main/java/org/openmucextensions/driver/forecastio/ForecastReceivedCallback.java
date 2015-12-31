package org.openmucextensions.driver.forecastio;

import com.github.dvdme.ForecastIOLib.FIOCurrently;
import com.github.dvdme.ForecastIOLib.FIODaily;
import com.github.dvdme.ForecastIOLib.FIOHourly;

/**
 * The callback methods will be called if new forecasts has been received
 * 
 * @author Pichler
 *
 */
public interface ForecastReceivedCallback {
		
	public void forecastReceived(final FIOCurrently currentlyForecast, final FIODaily dailyForecast, final FIOHourly hourlyForecast);
	
}
