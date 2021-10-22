import sqlite3


class Optimizer:

    def __init__(self, source, destination):

        # declaring variables
        self.SECONDS_IN_A_DAY = 86400
        self.source = source
        self.destination = destination

        self.oldDB = sqlite3.connect(self.source)
        self.newDB = sqlite3.connect(self.destination)

        self.oldDBCursor = self.oldDB.cursor()
        self.newDBCursor = self.newDB.cursor()


    def readTables(self):
        dbContent = []

        # querying for table names in the old database
        self.oldDBCursor.execute("SELECT name FROM sqlite_master WHERE type = 'table'")
        tableNameList = self.oldDBCursor.fetchall()

        # ignoring the name if the table name is annals
        if tableNameList[0][0] == "annals":
            tableNameList.pop(0)

        # reading all the tables from the oldDB and appending it to the list
        for tableName in tableNameList:
            self.oldDBCursor.execute("SELECT * FROM " + tableName[0])
            tableContent = self.oldDBCursor.fetchall()
            dbContent.append(tableContent)

        return dbContent


    def fillActivitiesTable(self, dbContent):
        tableContent = []

        for table in dbContent:

            for row in table:

                # getting name and category from the row
                name = row[0]
                category = row[1]

                activity = (name, category)

                # appending the activity to the activityList if the activity is not already in the list
                if activity not in tableContent:
                    tableContent.append(activity)

        # inserting all the activities to the newDB
        self.newDBCursor.executemany(
            "INSERT INTO activities(name, category)    "
            "VALUES(?, ?)", tableContent
        )


    def fillEventsTable(self, dbContent):
        tableContent = []
        activityCount = []

        for day in dbContent:

            # getting the date of the table
            date = day[0][-1]

            for row in day:
                name = row[0]
                category = row[1]
                startTime = row[3]
                endTime = row[4]
                dateOfRow = row[-1]

                # querying for activityID of the given name and category from the newDB
                self.newDBCursor.execute(
                    "SELECT activityID "
                    "FROM activities "
                    "WHERE name = ? AND category = ?",
                    (name, category)
                )

                queriedActivityID = self.newDBCursor.fetchall()
                activityID = queriedActivityID[0][0]

                if dateOfRow != date:

                    if endTime < self.SECONDS_IN_A_DAY:

                        # adding SECONDS_IN_A_DAY to startTime and endTime
                        # if the event's date has exceeded the date of the table
                        if startTime < endTime:
                            startTime += self.SECONDS_IN_A_DAY

                        endTime += self.SECONDS_IN_A_DAY

                # appending the data to the tableContent list
                # and appending the activityID to the activityCount list to get the frequencies
                tableContent.append((activityID, startTime, endTime, date))
                activityCount.append(activityID)

        # inserting all events to the events table
        self.newDBCursor.executemany(
            "INSERT INTO events(activityID, startTime, endTime, date) "
            "VALUES(?, ?, ?, ?)",
            tableContent
        )

        # adding frequencies to the activities
        self.addFrequencies(activityCount)


    def addFrequencies(self, activityCount):
        frequencyList = []

        # getting frequencies of activities and appending it to the frequency list
        for activityID in activityCount:
            activityFrequency = activityCount.count(activityID)
            print(activityFrequency)
            frequencyList.append((activityFrequency, activityID))

        # update the frequency value of the activities table with the new frequency values
        self.newDBCursor.executemany(
            "UPDATE activities "
            "SET frequency = ? "
            "WHERE activityID = ?",
            frequencyList
        )


    def createDB(self):
        print("[+] Creating the database...")

        # creating tables on the new database
        self.newDBCursor.execute(
            "CREATE TABLE "
            "IF NOT EXISTS "
            "activities"
            "("
            "activityID INTEGER PRIMARY KEY, "
            "name TEXT, "
            "category TEXT, "
            "frequency INTEGER"
            ")"
        )

        self.newDBCursor.execute(
            "CREATE TABLE "
            "IF NOT EXISTS "
            "events"
            "("
            "activityID INTEGER, "
            "description TEXT, "
            "startTime INTEGER, "
            "endTime INTEGER, "
            "date TEXT"
            ")"
        )

        # emptying the tables
        self.newDBCursor.execute("DELETE FROM activities")
        self.newDBCursor.execute("DELETE FROM events")

        # reading the old database
        print("[+] Reading file %s..." % self.source)
        dbContent = self.readTables()

        # giving activities ID and storing them in the activities table
        print("[+] Moving data to 'activities' table...")
        self.fillActivitiesTable(dbContent)

        # matching id with time and storing them in the events table
        print("[+] Moving data to 'events' table...")
        self.fillEventsTable(dbContent)

        # saving and closing databases
        print("[+] Saving databases...")

        self.newDB.commit()
        self.newDB.close()

        self.oldDB.close()

        print("[+] Created file " + self.destination)


    def run(self):
        self.createDB()


if __name__ == "__main__":
    optimizer = Optimizer("", "")
    optimizer.run()
