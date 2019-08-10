import requests
from bs4 import BeautifulSoup

class Event:
    def __init__(self, theImageLink, theName, theLocation, theDateAndTime):
        self.eventImageLink = theImageLink
        self.eventName = theName
        self.eventLocation = theLocation
        self.eventDateAndTime = theDateAndTime

def setup():
    # Gets HTML from A2F website
    webpage = 'http://www.ucsda2f.org/'
    request = requests.get(webpage)
    return BeautifulSoup(request.text, 'html.parser')

def stripEventNames(eventNames):
    # Strips text from event tags
    strippedEvents = []

    for event in eventNames:
        strippedEvents.append(event.text)

    return strippedEvents

def stripImageLinks(images):
    # Strips links to event images
    strippedImageLinks = []

    for image in images:
        strippedImageLinks.append(image['data-image'])

    return strippedImageLinks

def stripLocations(locations):
    # Strips text from location tags
    strippedList = []

    for location in locations:
        strippedList.append(location.p.strong.text)

    return strippedList

def stripDateAndTime(dates, times):
    # Strips and combines dates and times
    strippedDatesAndTimes = []

    index = 0
    upper = len(dates)
    while index < upper:
        strippedDatesAndTimes.append(dates[index].text + " " + times[index].text)
        index += 3

    return strippedDatesAndTimes

def main():
    # Setup HTML Parser
    soup = setup()
    
    # Get Information
    eventNames = soup.find_all('a', attrs={'class': 'summary-title-link'})
    eventNames = stripEventNames(eventNames)

    # Checks if events found
    if len(eventNames) > 0:

        images = soup.find_all('img', attrs={'class': 'summary-thumbnail-image'})
        images = stripImageLinks(images)

        locations = soup.find_all('div', attrs={'class': 'summary-excerpt'})
        locations = stripLocations(locations)

        dates = soup.find_all('time', attrs={'class': 'summary-metadata-item summary-metadata-item--date'})
        times = soup.find_all('span', attrs={'class': 'event-time-12hr'})
        datesAndTimes = stripDateAndTime(dates, times)

        # Constructs list of upcoming events
        events = []
        for i in range(len(eventNames)):
            events.append(Event(images[i], eventNames[i], locations[i], datesAndTimes[i]))

        return events

    else:
        # Return empty list if no events found
        return []
