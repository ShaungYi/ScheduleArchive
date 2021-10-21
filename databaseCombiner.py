import sqlite3


class Combiner:

    def __init__(self, source1, source2, destination):

        # declaring variables
        self.source1 = source1
        self.source2 = source2
        self.destination = destination
        self.db1 = sqlite3.connect(source1)
        self.db2 = sqlite3.connect(source2)
        self.cursor1 = self.db1.cursor()
        self.cursor2 = self.db2.cursor()


    def getTableNames(self, cursor):
        tableNameList = []

        # querying for table names in the given database
        cursor.execute("SELECT name FROM sqlite_master WHERE type = 'table'")
        queriedTableNames = cursor.fetchall()

        # appending the queried names to a list and returning the value
        for tableName in queriedTableNames:
            tableNameList.append(tableName[0])

        return tableNameList


    def readTables(self, cursor):
        dbContent = {}

        # getting all table name of the given database
        tableNameList = self.getTableNames(cursor)

        # reading all contents from all tables and appending them to a dictionary
        for tableName in tableNameList:
            dbContent[tableName] = []
            cursor.execute("SELECT * FROM " + tableName)

            # appending an element to the dictionary
            # where the key is the tableName and the
            # value is a list of all rows from the table
            tableContent = cursor.fetchall()
            dbContent[tableName].extend(tableContent)

        return dbContent


    def merge(self):
        newDBContent = {}

        # reading the contents of both databases
        db1Content = self.readTables(self.cursor1)
        db2Content = self.readTables(self.cursor2)
        print(db2Content)

        # getting all table names from the db1
        tableNameList = self.getTableNames(self.cursor1)

        for tableName in tableNameList:

            # combining both table contents and appending it the the dictionary
            db1TableContent = db1Content[tableName]
            db2TableContent = db2Content[tableName]

            newDBContent[tableName] = db1TableContent

            for row in db2TableContent:

                if row not in db1TableContent:
                    newDBContent[tableName].append(row)

        return newDBContent


    def createDB(self):

        # getting the full path to the destination
        pathToNewDB = self.destination
        print("[+] merging databases..")

        # getting a dictionary with the merged db content
        newDBContent = self.merge()
        print("[+] creating the new database...")

        # copy and pasting the db1 to the destination folder to preserve all the tables and the columns
        with open(self.source1, "rb") as sourceFile:
            sourceFileContent = sourceFile.read()

            with open(pathToNewDB, "wb") as copiedFile:
                copiedFile.write(sourceFileContent)

        # connecting to the new DB
        newDB = sqlite3.connect(pathToNewDB)
        cursor = newDB.cursor()

        tableNameList = self.readTables(cursor)
        print("[+] inserting values...")

        # emptying the table and inserting all the merged data
        for tableName in tableNameList:
            newTableContent = newDBContent[tableName]
            fillTables = "INSERT INTO {0} VALUES(?{1})".format(tableName, ", ?" * (len(newTableContent[0]) - 1))

            cursor.execute("DELETE FROM " + tableName)
            cursor.executemany(fillTables, newTableContent)


        # committing and closing the database
        newDB.commit()
        newDB.close()

        print("[+] merge complete.")


    def run(self):
        self.createDB()


if __name__ == "__main__":
    combiner = Combiner("", "", "..")
    combiner.run()
