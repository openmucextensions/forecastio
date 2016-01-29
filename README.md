# forecast.io Driver
This OpenMUC driver retrieves weather forecasts from the free [Dark Sky Forecast API](https://developer.forecast.io/) and provides the data via OpenMUC's data manager. To use the bundle, an API key is necessary, which will be provided after registration at the [developer's website](https://developer.forecast.io/). Forecasts may be retrieved for different locations within one driver instance, because each location is represented as an OpenMUC device.

After a forecast has been retrieved from the provider, all available data fields are provided as OpenMUC channels. Channel scan is supported to get an overview about all available data. Forecasts will be retrieved after bundle start-up and updated every 60 minutes. The time stamp of the last update from the external server can be read from the channel address `forecastio/lastupdate`.

If reading a value using OpenMUC's data manager, a temporarily stored value will be delivered. Thus, reading a value doesn't force the driver to update the forecast from the external provider. It's also possible to listen for value updates. In this case, the listener will be called each time forecast data will be retrieved from the external provider - irrespective of whether data has changed or not. Writing values is not supported.

This driver implementation is based on the underlying [Java Wrapper Library] (https://github.com/dvdme/forecastio-lib-java) by David Ervideira.

## Setting up a device
As mentioned before, an OpenMUC device represents the forecast for a single location. Because forecasts may be retrieved for any location specified by its coordinates, a device scan is not supported by the driver. The following device properties must be set:

* The device address represents the coordinates (latitude, longitude) of the location, separated by a comma. The following example shows the coordinates of Salzburg/Austria in the required format: `47.815470,13.046815`
* The device settings string must contain the API key provided by Dark Sky (see above)
* If a proxy has to be used, the settings string must contain the API key, the http-proxy-name and port, separated by a comma, e.g. `YOUR_API_KEY,127.0.0.1,8080`.

After a new device has been configured, the forecast will be retrieved immediately. Because available data fields may vary with the specified location, use OpenMUC's channel scan to get a list of available fields.
