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

def setup():
    # Gets HTML from A2F website
    webpage = 'http://www.ucsda2f.org/'
    request = requests.get(webpage)
    return BeautifulSoup(request.text, 'html.parser')

def stripImageLinks(events):
    # Strips links to event images
    strippedImageLinks = []

    for event in events:
        data = removeNonTags(event.contents)

        if len(data) > 0 and (data[0]['class'][0] == 'summary-thumbnail-outer-container'):
            strippedImageLinks.append(data[0].div.img['data-image'])
        else:
            strippedImageLinks.append('')
   
    return strippedImageLinks

def stripImageNames(images):
    # Strips image names
    strippedImageNames = []

    for imageLink in images:
        if imageLink != '':
            strippedImageNames.append(imageLink[imageLink.rfind('/') + 1:])
        else:
            strippedImageNames.append('')

    return strippedImageNames

def stripMonths(events):
    # Strips links to event images
    strippedMonths = []

    for event in events:
        data = removeNonTags(event.contents)

        if len(data) > 0 and (data[0]['class'][0] == 'summary-thumbnail-outer-container'):
            strippedMonths.append(data[0].div.div.div.span.text)
        else:
            strippedMonths.append('')
   
    return strippedMonths

def stripDayNumbers(events):
    # Strips text from day number tags
    strippedDayNumbers = []

    for event in events:
        data = removeNonTags(event.contents)

        if len(data) > 0 and (data[0]['class'][0] == 'summary-thumbnail-outer-container'):
            strippedDayNumbers.append(removeNonTags(data[0].div.div.div.contents)[1].text)
        else:
            strippedDayNumbers.append('')
   
    return strippedDayNumbers

def removeReturns(listToRemove):
    # Removes '\n' from list
    newList = []

    for item in listToRemove:
        if item != '\n':
            newList.append(item)

    return newList

def removeNonTags(listToRemove):
    newList = []

    tempSoup = BeautifulSoup('<b class="temp">temp</b>', 'html.parser')

    for item in listToRemove:
        if type(item) == type(tempSoup.b):
            newList.append(item)

    return newList

def getIndex(key, listToSearch):
    index = 0

    while index < len(listToSearch):
        if listToSearch[index] == key:
            return index + 1

        index = index + 1
    return str(-1)

def main():
    # Setup HTML Parser
    soup = setup()
    eventList = []

    # Get Information
    events = soup.find_all('div', attrs={'class': 'summary-item'})
   
    # Checks if events found
    if len(events) > 0:

        events = removeNonTags(events)

        images = []
        images = stripImageLinks(events)
        
        imageNames = []
        imageNames = stripImageNames(images)

        months = []
        months = stripMonths(events)

        dayNumbers = []
        dayNumbers = stripDayNumbers(events)

        excerpts = []

        info = soup.find_all('div', attrs={'class': 'summary-content sqs-gallery-meta-container'})
        for i in range(len(events)):
            contents = removeReturns(info[i].contents)

            # Retrieves the title
            titleTag = contents[getIndex(' Title ', contents)]
            title = titleTag.a.text

            # Retrieves the excerpt tag
            excerptTag = contents[getIndex(' Excerpt ', contents)]
            excerptList = removeReturns(excerptTag.contents)

            excerpts = []
            for j in range(len(excerptList)):
                excerpts.append(excerptList[j].text)

            # Adds the event
            eventList.append(Event(images[i], imageNames[i], months[i], dayNumbers[i], title, excerpts))

        return eventList

       
    else:
        # Return empty list if no events found
        return eventList
