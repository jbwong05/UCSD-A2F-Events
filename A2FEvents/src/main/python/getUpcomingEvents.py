import requests
from bs4 import BeautifulSoup

class Event:
    def __init__(self, theImageLink, theImageName, theMonth, theDayNumber, theName, theDescription, theTime, theLocation, theDateAndTime):
        self.eventImageLink = theImageLink
        self.eventImageName = theImageName
        self.eventMonth = theMonth
        self.eventDayNumber = theDayNumber
        self.eventName = theName
        self.eventDescription = theDescription
        self.eventTime = theTime
        self.eventLocation = theLocation
        self.eventDateAndTime = theDateAndTime

def setup():
    # Gets HTML from A2F website
    webpage = 'http://www.ucsda2f.org/'
    request = requests.get(webpage)
    return BeautifulSoup(request.text, 'html.parser')

def stripImageLinks(images):
    # Strips links to event images
    strippedImageLinks = []

    for image in images:
        strippedImageLinks.append(image['data-image'])

    return strippedImageLinks

def stripImageNames(images):
    # Strips image names
    strippedImageNames = []

    for imageLink in images:
        strippedImageNames.append(imageLink[imageLink.rfind('/') + 1:])

    return strippedImageNames

def stripMonths(months):
    # Strips text from month tags
    strippedMonths = []

    for month in months:
        strippedMonths.append(month.text)

    return strippedMonths

def stripDayNumbers(dayNumbers):
    # Strips text from day number tags

    strippedDayNumbers = []

    for dayNumber in dayNumbers:
        strippedDayNumbers.append(dayNumber.text)

    return strippedDayNumbers

def removeReturns(listToRemove):
    # Removes '\n' from list
    newList = []

    for item in listToRemove:
        if item != '\n':
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

    # Checks for save the date events
    saveTheDate = soup.find_all('div', attrs={'id': 'savethedate-page'})
    if len(saveTheDate) > 0:
        saveTheDate = saveTheDate[0].contents[1].contents[1].contents[0].contents[0].contents
        saveTheDateTitle = saveTheDate[0].text.strip()
        saveTheDate = saveTheDate[1].contents
        saveTheDateImage = saveTheDate[0].contents[0].contents[0].contents[1].contents[1].contents[1].contents[1].contents[0]
        saveTheDateImageLink = saveTheDateImage['src']
        saveTheDateImageName = saveTheDateImage['alt']
        saveTheDateInfo = saveTheDate[1].contents[0].contents[0].contents
        saveTheDateDescription = saveTheDateInfo[0].text
        saveTheDateDate = saveTheDateInfo[1].text
        saveTheDateLocation = saveTheDateInfo[2].text
        eventList.append(Event(saveTheDateImageLink, saveTheDateImageName, '', '', saveTheDateTitle, saveTheDateDescription, saveTheDateDate, saveTheDateLocation, ''))

    # Get Information
    events = soup.find_all('div', attrs={'class': 'summary-content sqs-gallery-meta-container'})

    # Checks if events found
    if len(events) > 0:

        images = soup.find_all('img', attrs={'class': 'summary-thumbnail-image'})
        images = stripImageLinks(images)

        imageNames = []
        imageNames = stripImageNames(images)

        months = soup.find_all('span', attrs={'class': 'summary-thumbnail-event-date-month'})
        months = stripMonths(months)

        dayNumbers = soup.find_all('span', attrs={'class': 'summary-thumbnail-event-date-day'})
        dayNumbers = stripDayNumbers(dayNumbers)

        for i in range(len(events)):
            contents = removeReturns(events[i].contents)

            # Retrieves the title
            titleTag = contents[getIndex(' Title ', contents)]
            title = titleTag.a.text

            # Retrieves the excerpt tag
            excerptTag = contents[getIndex(' Excerpt ', contents)]
            excerptList = removeReturns(excerptTag.contents)

            description = ''

            # Checks for description
            if len(excerptList) > 2:
                # Extracts the description
                description = excerptList[0].text
                excerptList.remove(excerptList[0])

            # Extracts the time and location
            time = excerptList[0].text
            location = excerptList[1].text

            # Extracts the date and time
            dateAndTimeTag = contents[getIndex(' Metadata (Below Content) ', contents)]
            primaryDateTag = removeReturns(dateAndTimeTag.contents)[0]
            primaryDate = primaryDateTag.time.text
            secondaryTimeTag = removeReturns(dateAndTimeTag.contents)[1]
            secondaryTime = secondaryTimeTag.time.contents[0].text
            dateAndTime = primaryDate + ' ' + secondaryTime

            # Adds the event
            eventList.append(Event(images[i], imageNames[i], months[i], dayNumbers[i], title, description, time, location, dateAndTime))

        return eventList

    else:
        # Return empty list if no events found
        return eventList
