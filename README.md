# Overview
This is a simple Android application that retrieves and displays any upcoming UCSD Acts2Fellowship events from the [UCSD A2F](http://www.ucsda2f.org/) website.

# How It Works
While the activity components are modified using Java, all of the web scraping and event retrieval is performed using the [`UCSD-A2F-Events/app/src/main/python/getUpcomingEvents.py`](https://github.com/jbwong05/UCSD-A2F-Events/blob/master/app/src/main/python/getUpcomingEvents.py) python script run with the [Chaquopy Python SDK for Android](https://chaquo.com/chaquopy/).

# Projct Status
Extensive testing has not yet been performed, but the application is currently functional on the Pixel 3 running Android 9.0. Further functionality will be added in the future.
