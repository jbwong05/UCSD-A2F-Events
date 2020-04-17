import requests
from bs4 import BeautifulSoup

class Event:
    def __init__(self, theImageLink, theImageName, theMonth, theDayNumber, theName, theExcerpts):
        self.eventImageLink = theImageLink
        self.eventImageName = theImageName
        self.eventMonth = theMonth
        self.eventDayNumber = theDayNumber
        self.eventName = theName
        self.eventExcerpts = theExcerpts
        self.eventNumExcerpts = len(theExcerpts)

def setup(webpage):
    # Gets HTML from A2F website
    request = requests.get(webpage)
    return BeautifulSoup(request.text, 'html.parser')

def stripImageNames(imageLink):
    # Strips image names
    if imageLink != '':
        return imageLink[imageLink.rfind('/') + 1:]
    else:
        return ''

def main(webpage):
    # Setup HTML Parser
    soup = setup(webpage)
    eventList = []

    # Get Information
    events = soup.find_all('div', attrs={'class': 'summary-item summary-item-record-type-event sqs-gallery-design-autocolumns-slide summary-item-has-thumbnail summary-item-has-excerpt summary-item-has-cats summary-item-has-tags summary-item-has-author'})
   
    # Checks if events found
    if len(events) > 0:

        eventList = []

        for event in events:

            currentImage = event.find('img')

            currentLink = currentImage['data-image']

            currentImageName = stripImageNames(currentLink)

            currentMonth = event.find('span', attrs={'class': 'summary-thumbnail-event-date-month'})
            if currentMonth != None:
                currentMonth = currentMonth.text

            currentDayNumber = event.find('span', attrs={'class': 'summary-thumbnail-event-date-day'})
            if currentDayNumber != None:
                currentDayNumber = currentDayNumber.text
            
            currentTitle = event.find('a', attrs={'class': 'summary-title-link'})
            if currentTitle != None:
                currentTitle = currentTitle.text

            currentExcerpt = event.find('p')
            if currentExcerpt != None:
                currentExcerpt = currentExcerpt.text

            eventList.append(Event(currentLink, currentImageName, currentMonth, currentDayNumber, currentTitle, currentExcerpt))

        return eventList
       
    else:
        # Return empty list if no events found
        return eventList
