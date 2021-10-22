import databaseCombiner


def addLargestActivityID(cursor, largestID):
    cursor.execute("UPDATE activities SET activityID = activityID + ?", (largestID + 1,))


def main():
    source1 = "C:/Users/Joonius/Downloads/archive1.db"
    source2 = "C:/Users/Joonius/Downloads/archive2.db"
    destination = "combinedArchive.db"
    
    combiner = databaseCombiner.Combiner(source1, source2, destination)

    largestActivityID1 = combiner.readTables(combiner.cursor1)["activities"][-1][0]

    largestActivityID2 = combiner.readTables(combiner.cursor2)["activities"][-1][0]

    if largestActivityID1 < largestActivityID2:
        addLargestActivityID(combiner.cursor1, largestActivityID2)
    else:
        addLargestActivityID(combiner.cursor2, largestActivityID1)

    combiner.run()


if __name__ == "__main__":
    main()
