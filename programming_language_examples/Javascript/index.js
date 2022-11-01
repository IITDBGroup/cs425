// log pg module
const { Client } = require("pg")

const connectDb = async () => {
    try {
		// create a client
        const client = new Client({
            user: "postgres",
            host: "127.0.0.1",
            database: "university",
            password: "test",
            port: 5450
        })

		// connect and wait for the connection to be established
        await client.connect()

		// running a query and printing the result object
        const res = await client.query('SELECT * FROM student')
        console.log(res)

		// access the rows as an array of JS objects
		console.log(res.rows)		

		// loop over rows
		for (r of res.rows) {
			console.log(r.name) // access columns as fields of row objects
		}

		// run DML
		await client.query("DELETE FROM student WHERE name = 'does not exist'")
		
		// disconnecting
        await client.end()
    } catch (error) {
        console.log(error)
    }
}

// run our example test
connectDb()
