import databaseCombiner


def addLargestActivityID(cursor, largestID):
    cursor.execute("UPDATE activities SET activityID = activityID + ?", (largestID + 1,))
    cursor.execute("UPDATE events SET activityID = activityID + ?", (largestID + 1,))


def main():
    source1 = ""
    source2 = ""
    destination = ""

    combiner = databaseCombiner.Combiner(source1, source2, destination)

    # getting the largest activityID from DB1 and DB2
    largestActivityID1 = combiner.readTables(combiner.cursor1)["activities"][-1][0]
    largestActivityID2 = combiner.readTables(combiner.cursor2)["activities"][-1][0]

    # comparing the IDs to get the largest largest ID
    if largestActivityID1 < largestActivityID2:

        # setting new activityIDs for DB1 if the DB2's largest ID is larger then DB1's
        addLargestActivityID(combiner.cursor1, largestActivityID2)
    else:

        # setting new activityIDs for DB2 if the DB1's largest ID is larger then DB2's
        addLargestActivityID(combiner.cursor2, largestActivityID1)

    # combining databases
    combiner.run()


if __name__ == "__main__":
    main()
