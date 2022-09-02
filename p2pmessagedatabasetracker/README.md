# p2p chat-service message database tracker
RESTful chat service API with HBase as persistence layer

### Setup

hbase-site.xml provided

Start HBase standalone
```
./bin/start-hbase.sh
```

### Running the REST API service
Run App.java, service available at
```
localhost:9991
```

BoltDB and Apache HBase follow same schema. 
Messages are read from BoltDB present in peer and streamed to tracker servers which host hbase sink.
From this sink, this data is consumed by p2p chat microservices such as analytics engine, notification/email system  
Messages are streamed in batch mode at 15 min intervals to keep load on peer device minimum.  
 
 
### REST API

Create chat room
```
PUT /room
{
    "id": 1234,
    "name": "whale",
    "created": 1662116169,
    "participants": [
        "blue whale",
        "green whale",
        "red whale"
    ]
}'
```

Add new messages
```
PUT /messages
{
    "chatRoomId": 1234,
    "messages": [
        {
            "index": 1,
            "timestamp": 1662116199,
            "author": "blue whale",
            "message": "hi, what's up?"
        },
        {
            "index": 2,
            "timestamp": 1662116234,
            "author": "green whale",
            "message": "just chilling"
        },
        {
            "index": 3,
            "timestamp": 1662116263,
            "author": "red whale",
            "message": "wow!!"
        },
    ]
}
```

Get room
```
GET /room/:id
```

Get messages from room between start <= t < end
```
GET /room/:id/messages/start/end
``` 

Count long pauses for messages from room between start <= t < end
```
GET /room/:id/long-pauses/start/end
```

### HBase table and rowkey design
Access pattern
1. request chat entries from a specific chat room by time range
2. count long pauses between chat entries inside a time range (a long pause is
any pause longer than the average time between entries in a given chat room)

Chat room schema
```
row             column
chatRoomId      info:created
                info:name
                info:participants
                meta:created     // same as info:created
                meta:count       // nunber of messages in room
                meta:lastMsgTs   // timestamp of last message
```

Messages schema
```
row                     column
chatRoomId_timestamp    message.index
                        message.peerId
                        message.timestamp
                        message.message
```

With this design, access pattern above are accomplished by:
1. Range scan Messages table with rowkey between chatRoomId_start and chatRoomId_end 
2. Range scan Messages table to get timestamp of messages, get Room meta data, return messages where (timestamp - last_message_timestamp) > (meta:lastMsgTs - meta:created)/meta:count
