# Overview
This is a simple Android application that retrieves and displays any upcoming UCSD Acts2Fellowship events from the [UCSD A2F](http://www.ucsda2f.org/) website. Tapping on each event will prompt and allow the user to add the event directly the default user's default calendar.

# How It Works
While the activity components are modified using Java, all of the web scraping and event retrieval is performed using the [`UCSD-A2F-Events/app/src/main/python/getUpcomingEvents.py`](https://github.com/jbwong05/UCSD-A2F-Events/blob/master/app/src/main/python/getUpcomingEvents.py) python script run with the [Chaquopy Python SDK for Android](https://chaquo.com/chaquopy/).

# Project Status
Extensive testing has not yet been performed, but the application is currently functional on the Samsung Galaxy S4 running Android 5.0.1, the Pixel 3 running Android 9.0, and the Pixel 3 running Android 10.0.

# Note
The app will take longer to display events upon the very first run due to the need to download images in the background. Every subsequent run following the very first one often will likely not suffer from this issue unless several images are needed to be downloaded due to event update or addition. Faster download methods to reduce waiting and lag time are being explored.