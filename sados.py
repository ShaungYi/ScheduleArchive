import sqlite3
import os
import sadoso

SECONDS_IN_A_DAY = 86400
dbName = "archive.db"
source = ""
destination = ""


# functions

def readTables(cursor):
    data = []

    cursor.execute("SELECT name FROM sqlite_master WHERE type = 'table';")
    tableNameList = cursor.fetchall()

    if tableNameList[0][0] == "annals":
        tableNameList.pop(0)

    for tableName in tableNameList:
        cursor.execute("SELECT * FROM " + tableName[0])
        data.append(cursor.fetchall())

    return data


def fillActivities(cursor, data):
    activityList = []
    tableContent = []
    activityID = 0

    for table in data:

        for row in table:
            name = row[0]
            category = row[1]

            activity = [name, category]

            if activity not in activityList:
                activityList.append(activity)
                tableContent.append((activityID, name, category))
                activityID += 1

    cursor.executemany(
        "INSERT INTO activities "
        "VALUES (?, ?, ?)", tableContent
    )


def fillEvents(cursor, data):
    tableContent = []
    activityCount = []

    for day in data:
        date = day[0][-1]

        for row in day:
            name = row[0]
            category = row[1]
            startTime = row[3]
            endTime = row[4]
            dateOfRow = row[-1]

            cursor.execute(
                "SELECT activityID "
                "FROM activities "
                "WHERE name = ? AND category = ?",
                (name, category)
            )

            activityID = cursor.fetchall()
            activityID = activityID[0][0]

            if dateOfRow != date:

                if endTime < SECONDS_IN_A_DAY:

                    if startTime < endTime:
                        startTime += SECONDS_IN_A_DAY

                    endTime += SECONDS_IN_A_DAY

            tableContent.append((activityID, startTime, endTime, date))
            activityCount.append(activityID)

    cursor.executemany(
        "INSERT INTO events "
        "VALUES (?, ?, ?, ?)",
        tableContent
    )

    # getting frequencies of the activities
    print("[+] Moving data to 'frequencies' table")
    fillFrequencies(cursor, activityCount)


def fillFrequencies(cursor, data):
    tableContent = []

    for activityID in data:
        activityFrequency = data.count(activityID)
        tableContent.append((activityID, activityFrequency))

    cursor.executemany(
        "INSERT INTO frequencies "
        "VALUES (?, ?)",
        tableContent
    )


# creating the database and tables

print("[+] Creating DB file...")

oldDB = sqlite3.connect(source)
os.chdir(destination)
optimizedDB = sqlite3.connect(dbName)

oldDBCursor = oldDB.cursor()
optimizedDBCursor = optimizedDB.cursor()

optimizedDBCursor.execute(
    "CREATE TABLE "
    "IF NOT EXISTS "
    "activities ("
    "activityID INTEGER, "
    "name TEXT, "
    "category TEXT"
    ")"
)

optimizedDBCursor.execute(
    "CREATE TABLE "
    "IF NOT EXISTS "
    "events "
    "("
    "activityID INTEGER, "
    "startTime, "
    "endTime INTEGER, "
    "date TEXT"
    ")"
)

optimizedDBCursor.execute(
    "CREATE TABLE "
    "IF NOT EXISTS "
    "frequencies "
    "(activityID INTEGER, "
    "frequency INTEGER"
    ")"
)

optimizedDBCursor.execute("DELETE FROM activities")
optimizedDBCursor.execute("DELETE FROM events")
optimizedDBCursor.execute("DELETE FROM frequencies")

# reading databases

print("[+] Reading file %s..." % source)

allTables = readTables(oldDBCursor)

# giving activities id and storing them in the activities table

print("[+] Moving data to 'activities' table...")
fillActivities(optimizedDBCursor, allTables)

# matching id with time and storing them in the time table

print("[+] Moving data to 'events' table...")
fillEvents(optimizedDBCursor, allTables)

# saving and closing databases

print("[+] Saving databases...")

optimizedDB.commit()
optimizedDB.close()

oldDB.close()

sadoso = sadoso.ScheduleArchiveDBOptimizer(source, destination)
sadoso.run()

print("[+] Created file {0}\\{1}".format(destination, dbName))
