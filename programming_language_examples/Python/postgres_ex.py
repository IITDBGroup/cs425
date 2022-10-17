import psycopg2

connection = {
    "dbname": 'university',
    "user": 'postgres',
    "host": '127.0.0.1',
    "password": 'test',
    "port": 5450
    }

# Create a connection
try:
    conn = psycopg2.connect(**connection)
except:
    print("I am unable to connect to the database")

# Create a curson
cur = conn.cursor()

# Execute a statements and fetch results
try:
    cur.execute("SELECT name FROM student")
except:
    print("I can't SELECT from student")

# now let's fetch all the rows and print them
rows = cur.fetchall()
print("\nResults: \n")
for row in rows:
    print(f"   {row}")


# now a query with more result columns
try:
    cur.execute("SELECT id, name, tot_cred FROM student ORDER BY name ASC")
except:
    print("I can't SELECT from student")

rows = cur.fetchall()
print("\nResults: \n")
for row in rows:
    # Rows are encoded as tuples
    print(f"{row}") 
    print(f" or to access a particular column (2nd one): {row[1]}")

# transactions are explicitely terminated by running con.rollback() or con.commit()

    
# close the connection
cur.close()

# close the connection
conn.close()



