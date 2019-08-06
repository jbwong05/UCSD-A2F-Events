import requests
from bs4 import BeautifulSoup

class Event:
    def __init__(self, theName, theLocation, theDateAndTime):
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

    locations = soup.find_all('div', attrs={'class': 'summary-excerpt'})
    locations = stripLocations(locations)

    dates = soup.find_all('time', attrs={'class': 'summary-metadata-item summary-metadata-item--date'})
    times = soup.find_all('span', attrs={'class': 'event-time-12hr'})
    datesAndTimes = stripDateAndTime(dates, times)

    # Prints upcoming events
    events = []
    for i in range(len(eventNames)):
        events.append(Event(eventNames[i], locations[i], datesAndTimes[i]))

    return events
