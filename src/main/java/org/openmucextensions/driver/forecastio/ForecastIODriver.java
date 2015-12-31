package org.openmucextensions.driver.forecastio;

import org.openmuc.framework.config.ArgumentSyntaxException;
import org.openmuc.framework.config.DriverInfo;
import org.openmuc.framework.config.ScanException;
import org.openmuc.framework.config.ScanInterruptedException;
import org.openmuc.framework.driver.spi.Connection;
import org.openmuc.framework.driver.spi.ConnectionException;
import org.openmuc.framework.driver.spi.DriverDeviceScanListener;
import org.openmuc.framework.driver.spi.DriverService;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ForecastIODriver implements DriverService {

	private final static Logger logger = LoggerFactory.getLogger(ForecastIODriver.class);
	
	private final static DriverInfo driverInfo = new DriverInfo("forecastio", // id
			// description
			"Wheater forecast driver using forecast.io provider",
			// device address
			"LATITUDE,LONGITUDE",
			// settings
			"API-KEY",
			// channel address
			"Field name, see documentation",
			// device scan parameters
			"No settings possible");
	
	protected void activate(ComponentContext context) {
		logger.info("Activating ForecastIO driver");
	}
	
	protected void deactivated(ComponentContext context) {
		logger.info("Deactivating ForecastIO driver");
	}
	
	@Override
	public DriverInfo getInfo() {
		return driverInfo;
	}

	@Override
	public void scanForDevices(String settings, DriverDeviceScanListener listener)
			throws UnsupportedOperationException, ArgumentSyntaxException, ScanException, ScanInterruptedException {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void interruptDeviceScan() throws UnsupportedOperationException {
		// device scan is not supported
	}

	@Override
	public Connection connect(String deviceAddress, String settings)
			throws ArgumentSyntaxException, ConnectionException {
		
		if(settings==null||settings.isEmpty()) throw new ArgumentSyntaxException("Settings must contain the API key");
		
		String[] coordinates = deviceAddress.trim().split(",");
		if(coordinates.length != 2) {
			throw new ArgumentSyntaxException("Invalid device address " + deviceAddress + ", expected format is LATITUDE,LONGITUDE");
		} else {
			ForecastIOConnection connection = new ForecastIOConnection(settings);
			connection.startForecastRetrieval(coordinates[0], coordinates[1]);
			return connection;
		}
	}
	
}
