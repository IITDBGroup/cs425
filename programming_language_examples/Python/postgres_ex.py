import psycopg2

def exec_and_print_result(c,sql):
    """
    Execute a query sql using cursor c and print its results
    """
    try:
        print(80 * "=" + f"\nQUERY: {sql}\n")
        c.execute(sql)
        rows = c.fetchall()
        for r in rows:
            print(f"{r}")
    except:
        print(f"execution of query {sql} did fail")

connection = {
    'dbname': 'university',
    'user': 'postgres',
    'host': '127.0.0.1',
    'password': 'test',
    'port': 5432
    }

# Create a connection
try:
    # conn = psycopg2.connect(dbname="university", user="postgres") 
    conn = psycopg2.connect(**connection)
except:
    print(f"I am unable to connect to the database with connection parameters:\n{connection}")
    exit(1)

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
cur.execute("DELETE FROM student")
conn.rollback()
exec_and_print_result(cur, "SELECT count(*) FROM student")
    
# close the connection
cur.close()

# close the connection
conn.close()
