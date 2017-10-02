import psycopg2

# Create a connection
try:
    conn = psycopg2.connect("dbname='cs425' user='postgres' host='localhost' password='test'")
except:
    print "I am unable to connect to the database"

# Create a curson
cur = conn.cursor()

# Execute a statements and fetch results
try:
    cur.execute("SELECT name FROM student")
except:
    print "I can't SELECT from student"

# now let's fetch all the rows and print them
rows = cur.fetchall()
print "\nResults: \n"
for row in rows:
    print "   ", row


# now a query with more result columns
try:
    cur.execute("SELECT id, name, tot_cred FROM student ORDER BY name ASC")
except:
    print "I can't SELECT from student"

rows = cur.fetchall()
print "\nResults: \n"
for row in rows:
    print "   ", row
    print " or to access a particular column (2nd one):", row[1]

# close the connection
cur.close()

# close the connection
conn.close()



