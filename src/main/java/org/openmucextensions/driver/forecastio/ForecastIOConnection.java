package org.openmucextensions.driver.forecastio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TreeMap;

import org.openmuc.framework.config.ArgumentSyntaxException;
import org.openmuc.framework.config.ChannelScanInfo;
import org.openmuc.framework.config.ScanException;
import org.openmuc.framework.data.Flag;
import org.openmuc.framework.data.Record;
import org.openmuc.framework.data.StringValue;
import org.openmuc.framework.data.ValueType;
import org.openmuc.framework.driver.spi.ChannelRecordContainer;
import org.openmuc.framework.driver.spi.ChannelValueContainer;
import org.openmuc.framework.driver.spi.Connection;
import org.openmuc.framework.driver.spi.ConnectionException;
import org.openmuc.framework.driver.spi.RecordsReceivedListener;

import com.github.dvdme.ForecastIOLib.FIOCurrently;
import com.github.dvdme.ForecastIOLib.FIODaily;
import com.github.dvdme.ForecastIOLib.FIOHourly;
import com.github.dvdme.ForecastIOLib.ForecastIO;

public class ForecastIOConnection implements Connection, ForecastReceivedCallback {
	
	private ForecastIO forecast;
	private Timer retrieveForecastTimer;
	private Map<String, String> values;
	private List<ChannelRecordContainer> listeningContainers = null;
	private RecordsReceivedListener listener = null;
	
	private long refreshInterval = 1000 * 60 * 60;
	
	public ForecastIOConnection(final String apiKey) {
		forecast = new ForecastIO(apiKey);
		forecast.setUnits(ForecastIO.UNITS_SI);
		forecast.setExcludeURL("minutely");
		
		values = new TreeMap<>();
	}
	
	public void setHTTPProxy( final String proxhostname, final int proxyport) {
		forecast.setHTTPProxy(proxhostname, proxyport);
	}
	
	public void startForecastRetrieval(final String latitude, final String longitude) {
		retrieveForecastTimer = new Timer("ForecastIO-driver", true);
		RetrieveForecastTask task = new RetrieveForecastTask(forecast, latitude, longitude, this);
		retrieveForecastTimer.schedule(task, 0, refreshInterval);
	}
	
	@Override
	public List<ChannelScanInfo> scanForChannels(String settings)
			throws UnsupportedOperationException, ArgumentSyntaxException, ScanException, ConnectionException {
		
		List<ChannelScanInfo> result = new ArrayList<>();
		
		synchronized (values) {
			for (String key : values.keySet()) {
				ChannelScanInfo info = new ChannelScanInfo(key, key, ValueType.STRING, 255, true, false);
				result.add(info);
			}
		}
		
		return result;
	}

	@Override
	public Object read(List<ChannelRecordContainer> containers, Object containerListHandle, String samplingGroup)
			throws UnsupportedOperationException, ConnectionException {
		
		if(values.isEmpty()) throw new ConnectionException("Didn't receive any forecast up to now");
		
		long timestamp = new Date().getTime();
		
		for (ChannelRecordContainer channelRecordContainer : containers) {
			Record record;
			synchronized (values) {
				if (values.containsKey(channelRecordContainer.getChannelAddress())) {
					record = new Record(new StringValue(values.get(channelRecordContainer.getChannelAddress())),
							timestamp);
				} else {
					record = new Record(Flag.DRIVER_ERROR_CHANNEL_WITH_THIS_ADDRESS_NOT_FOUND);
				}
			}
			channelRecordContainer.setRecord(record);
		}
		
		return containers;
	}

	@Override
	public void startListening(List<ChannelRecordContainer> containers, RecordsReceivedListener listener)
			throws UnsupportedOperationException, ConnectionException {
		
		this.listeningContainers = containers;
		this.listener = listener;
		
		if(!values.isEmpty()) updateListener();
		
	}
	
	@Override
	public Object write(List<ChannelValueContainer> containers, Object containerListHandle)
			throws UnsupportedOperationException, ConnectionException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void disconnect() {
		if(retrieveForecastTimer != null) retrieveForecastTimer.cancel();
	}

	@Override
	public void forecastReceived(FIOCurrently currentlyForecast, FIODaily dailyForecast, FIOHourly hourlyForecast) {
		
		synchronized (values) {
			
			values.clear();
			
			for (String fieldName : currentlyForecast.get().getFieldsArray()) {
				String value = currentlyForecast.get().getByKey(fieldName);
				values.put("forecastio/currently/" + fieldName, value);
			}
			
			for (int day = 0; day < dailyForecast.days(); day++) {
				for (String fieldName : dailyForecast.getDay(day).getFieldsArray()) {
					String value = dailyForecast.getDay(day).getByKey(fieldName);
					values.put("forecastio/daily/" + day + "/" + fieldName, value);
				}
			}
			
			for (int hour = 0; hour < hourlyForecast.hours(); hour++) {
				for (String fieldName : hourlyForecast.getHour(hour).getFieldsArray()) {
					String value = hourlyForecast.getHour(hour).getByKey(fieldName);
					values.put("forecastio/hourly/" + hour + "/" + fieldName, value);
				}
			}
			
			values.put("forecastio/lastupdate", Long.toString(new Date().getTime()));
		}
		
		updateListener();
		
	}
	
	private void updateListener() {
		
		if(listener==null||listeningContainers==null) return;
		long timestamp = new Date().getTime();
		
		for (ChannelRecordContainer channelRecordContainer : listeningContainers) {
			Record record;
			synchronized (values) {
				if (values.containsKey(channelRecordContainer.getChannelAddress())) {
					record = new Record(new StringValue(values.get(channelRecordContainer.getChannelAddress())),
							timestamp);
				} else {
					record = new Record(Flag.DRIVER_ERROR_CHANNEL_WITH_THIS_ADDRESS_NOT_FOUND);
				}
			}
			channelRecordContainer.setRecord(record);
		}
		
		listener.newRecords(listeningContainers);	
	}
}
