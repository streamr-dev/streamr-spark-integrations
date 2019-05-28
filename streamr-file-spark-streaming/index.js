const StreamrClient = require('streamr-client')
require('dotenv').config()
const client = new StreamrClient({
    auth: {
        apiKey: process.env.STREAMR_API_KEY
    }
})
const fs = require('fs');
const path = require('path');
// Clear all files required by Spark
const rimraf = require('rimraf');
rimraf('./tmp/*', function () { console.log('tmp cleared'); });


let index = 0;
const stringifyAndWrite = (listedJson) => {
    const json = JSON.stringify(listedJson)
    fs.writeFile('tmp/' + index + '.json', json, (err) => {
        if (err) {
            console.error(err)
            return
        }
    })
    index++;
}
let listedJson = []
const sub = client.subscribe(
    {
    stream: process.env.STREAM_ID,
    resend: {
        from: {
            timestamp: Date.now() - 12000000
        }
    }
    },
    (message, metadata) => {
        listedJson.push(message)
        if (listedJson.length > 3000) {
            stringifyAndWrite(listedJson)
            listedJson = []
        }
        console.log(listedJson)
    }
)
