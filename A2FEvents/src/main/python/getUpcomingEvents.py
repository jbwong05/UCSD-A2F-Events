import requests
from bs4 import BeautifulSoup

class Event:
    def __init__(self, theImageLink, theImageClickLink, theImageName, theMonth, theDayNumber, theName, theExcerpts, theExcerptsLinks):
        self.eventImageLink = theImageLink
        self.eventImageClickLink = theImageClickLink
        self.eventImageName = theImageName
        self.eventMonth = theMonth
        self.eventDayNumber = theDayNumber
        self.eventName = theName
        self.eventExcerpts = theExcerpts
        self.eventExcerptsLinks = theExcerptsLinks
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

            # Image link and name
            currentLink = ''
            currentImageName = ''
            if currentImage != None:
                currentLink = currentImage['data-image']
                currentImageName = stripImageNames(currentLink)
            
            # Image click link
            currentImageClickLink = event.find('a', attrs={'class': 'summary-thumbnail-container sqs-gallery-image-container'})
            if currentImageClickLink != None:
                currentImageClickLink = "https://www.ucsda2f.org" + currentImageClickLink['href']
            else:
                currentImageClickLink = ''

            # Event month
            currentMonth = event.find('span', attrs={'class': 'summary-thumbnail-event-date-month'})
            if currentMonth != None:
                currentMonth = currentMonth.text

            # Event day number
            currentDayNumber = event.find('span', attrs={'class': 'summary-thumbnail-event-date-day'})
            if currentDayNumber != None:
                currentDayNumber = currentDayNumber.text
            
            # Event name
            currentTitle = event.find('a', attrs={'class': 'summary-title-link'})
            if currentTitle != None:
                currentTitle = currentTitle.text

            # Description and links
            excerptList = []
            excerptClickLinks = []
            excerpts = event.find_all('p')
            for excerpt in excerpts:
                excerptList.append(excerpt.text)
                
                link = excerpt.find('a')
                if link != None:
                    excerptClickLinks.append(link['href'])
                else:
                    excerptClickLinks.append('')

            eventList.append(Event(currentLink, currentImageClickLink, currentImageName, currentMonth, currentDayNumber, currentTitle, excerptList, excerptClickLinks))

        return eventList
       
    else:
        # Return empty list if no events found
        return eventList