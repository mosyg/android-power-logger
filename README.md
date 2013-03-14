# Power Logger for Android
A simple logging utility that will track as much about your phone as possible while sacrificing as little CPU and battery as possible.
It polls various data sources and logs them at configurable intervals.

Current polled sources:
* Running Apps & Services
* output of 'ps' and other scripts
* Battery levels and statistics
* GPS and cell location (won't start a fix, so whatever the last one was)
* WiFi (since last scan)
* Various other events (alarms, etc.)

