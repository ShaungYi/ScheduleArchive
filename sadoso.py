import sqlite3
import os


class ScheduleArchiveDBOptimizer:

    def __init__(self, source, destination):
        self.source = source
        self.destination = destination
        self.dbName = "archive2.db"

        os.chdir(self.destination)

        self.oldDB = sqlite3.connect(self.source)
        self.optimizedDB = sqlite3.connect(self.dbName)

        self.oldDBCursor = self.oldDB.cursor()
        self.optimizedDBCursor = self.optimizedDB.cursor()

    def fillActivitiesTable(self):
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
        self.optimizedDBCursor.executemany("INSERT INTO "
                                           "activities "
                                           "VALUES (?, ?, ?, ?)",
                                           activitiesTableContent
                                           )

    def fillEventsTable(self):
        self.oldDBCursor.execute("SELECT * FROM events")
        eventsTableContent = self.oldDBCursor.fetchall()
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

        self.optimizedDBCursor.execute(
            "CREATE TABLE "
            "IF NOT EXISTS "
            "activities "
            "("
            "activityID INTEGER, "
            "name TEXT, "
            "category TEXT, "
            "frequency INTEGER"
            ")"
        )

        self.optimizedDBCursor.execute(
            "CREATE TABLE "
            "IF NOT EXISTS "
            "events "
            "("
            "activityID INTEGER, "
            "description TEXT, "
            "startTime, "
            "endTime INTEGER, "
            "date TEXT"
            ")"
        )

        self.optimizedDBCursor.execute("DELETE FROM activities")
        self.optimizedDBCursor.execute("DELETE FROM events")

        # reading databases

        print("[+] Reading file %s..." % self.source)

        # giving activities id and storing them in the activities table

        print("[+] Moving data to 'activities' table...")
        self.fillActivitiesTable()

        # matching id with time and storing them in the time table

        print("[+] Moving data to 'events' table...")
        self.fillEventsTable()

        # saving and closing databases

        print("[+] Saving databases...")

        self.optimizedDB.commit()
        self.optimizedDB.close()

        self.oldDB.close()

        print("[+] Created file " + self.destination + self.dbName)

    def run(self):
        self.createDB()


if __name__:
    optimizer = ScheduleArchiveDBOptimizer(source="", destination="")
    optimizer.run()
