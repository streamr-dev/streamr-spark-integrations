# Tool to pull Real time / Historical data from streamr to a directory for Spark.

Created with Node.js. You can use this tool to pull historical data and/or real time data from Streamr and then use Apache Spark to process the data.

If you wish to use the integration script you can clone the repo and run `npm install` in side this directory. Alternatively you can copy and paste the code or create your own integration script.

### If you clone the repo:
Before running the integration script, you must create a .env file and specify these variables found in Streamr's editor after you have created an account and a stream:

```
STREAM_ID = 'your streams id'
STREAMR_API_KEY = 'your Streamr api key'
```

Now you can run the script with 
```
node index.js
```

## Historical data

To pull historical data you need to specify a resend time for the subscribe function. 

```javascript
const sub = client.subscribe(
    {
    stream: process.env.STREAM_ID,
    resend: {
        from: {
            // Specify the start time for here pulling historical data
            timestamp: Date.now() - 12000000
        }
    }
```

After all historical data is received, the client will keep on subscribing to real time data. All the JSON data files from Streamr are stored in the /tmp directory. You can 

The tmp, checkpoint and res directories are provided for Apache Spark for ingesting and outputting data. More info about the directories in the [Scala Streamr-Spark File Streaming docs](../scala-spark). You might need to empty atleast the /checkpoint and /res directories completely before running Spark, as the script might be unable to remove some files on restart.