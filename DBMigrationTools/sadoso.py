import sqlite3
import os


class ScheduleArchiveDBOptimizer:

    def __init__(self, source, destination):

        # declaring variables
        self.source = source
        self.destination = destination

        os.chdir(self.destination)

        self.oldDB = sqlite3.connect(self.source)
        self.optimizedDB = sqlite3.connect(self.destination)

        self.oldDBCursor = self.oldDB.cursor()
        self.optimizedDBCursor = self.optimizedDB.cursor()


    def fillActivitiesTable(self):

        # reading frequency values of the oldDB
        self.oldDBCursor.execute(
            "SELECT DISTINCT activityID, name, category, "
            "CASE "
            "   WHEN frequency IS NULL"
            "       THEN 1"
            "       ELSE frequency "
            "END AS frequency "
            "FROM activities "
            "LEFT JOIN frequencies "
            "USING (activityID)"
        )
        activitiesTableContent = self.oldDBCursor.fetchall()

        # inserting the frequency values to the activities table of the newDB
        self.optimizedDBCursor.executemany("INSERT INTO "
                                           "activities "
                                           "VALUES (?, ?, ?, ?)",
                                           activitiesTableContent
                                           )


    def fillEventsTable(self):

        # reading the content of the events table of the oldDB
        self.oldDBCursor.execute("SELECT * FROM events")
        eventsTableContent = self.oldDBCursor.fetchall()

        # inserting the data to the events table of the newDB
        self.optimizedDBCursor.executemany(
            "INSERT INTO "
            "events "
            "("
            "activityID, "
            "startTime, "
            "endTime, "
            "date"
            ") "
            "VALUES (?, ?, ?, ?)",
            eventsTableContent
        )


    def createDB(self):
        print("[+] Creating new DB...")

        # creating tables
        self.optimizedDBCursor.execute(
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

        self.optimizedDBCursor.execute(
            "CREATE TABLE "
            "IF NOT EXISTS "
            "events"
            "("
            "activityID INTEGER, "
            "description TEXT, "
            "startTime, "
            "endTime INTEGER, "
            "date TEXT"
            ")"
        )

        # emptying the tables
        self.optimizedDBCursor.execute("DELETE FROM activities")
        self.optimizedDBCursor.execute("DELETE FROM events")

        # reading databases
        print("[+] Reading file %s..." % self.source)

        # moving frequency values from the frequencies table from the oldDB
        # to the activities table of the newDB
        print("[+] Moving data to 'activities' table...")
        self.fillActivitiesTable()

        # copy and pasting the events table to the new DB
        print("[+] Moving data to 'events' table...")
        self.fillEventsTable()

        # saving and closing databases

        print("[+] Saving databases...")

        self.optimizedDB.commit()
        self.optimizedDB.close()

        self.oldDB.close()

        print("[+] Created file " + self.destination)


    def run(self):
        self.createDB()


if __name__ == "__main__":
    optimizer = ScheduleArchiveDBOptimizer("", "")
    optimizer.run()
