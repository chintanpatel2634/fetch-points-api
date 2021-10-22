# Fetch-Points-API

## Build & Run Instructions
Download Maven on your system (https://maven.apache.org/download.cgi) and configure its path to be located by the cmd/terminal.

Clone the repository onto your local repository.
Go inside the directory where pom.xml is located.

First clean the project by following command
```
mvn clean
```

To run the application
```
mvn spring-boot:run
```

## Test Endpoints
Open another cmd/terminal window and then run following commands to test the api

### Adding five new transactions
```
curl -d "{\"payer\":\"DANNON\",\"points\":1000,\"timestamp\":\"2020-11-02T14:00:00Z\"}" -H "Content-Type:application/json" http://localhost:8080/api/addTransaction
```
```
curl -d "{\"payer\":\"UNILEVER\",\"points\":200,\"timestamp\":\"2020-10-31T11:00:00Z\"}" -H "Content-Type:application/json" http://localhost:8080/api/addTransaction
```
```
curl -d "{\"payer\":\"DANNON\",\"points\":-200,\"timestamp\":\"2020-10-31T15:00:00Z\"}" -H "Content-Type:application/json" http://localhost:8080/api/addTransaction
```
```
curl -d "{\"payer\":\"MILLER COORS\",\"points\":10000,\"timestamp\":\"2020-11-01T14:00:00Z\"}" -H "Content-Type:application/json" http://localhost:8080/api/addTransaction
```
```
curl -d "{\"payer\":\"DANNON\",\"points\":300,\"timestamp\":\"2020-10-31T10:00:00Z\"}" -H "Content-Type:application/json" http://localhost:8080/api/addTransaction
```

### Spending points
```
curl -d "{\"points\":5000}" -H "Content-Type:application/json" http://localhost:8080/api/point/spend
```
Response
```
[{"payer":"DANNON","points":-100},{"payer":"UNILEVER","points":-200},{"payer":"MILLER COORS","points":-4700}]
```

### Get all payers balance
```
curl -v http://localhost:8080/api/payer/balance
```
Response
```
*   Trying ::1...
* TCP_NODELAY set
* Connected to localhost (::1) port 8080 (#0)
> GET /api/payer/balance HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.55.1
> Accept: */*
>
< HTTP/1.1 200
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Fri, 22 Oct 2021 07:28:51 GMT
<
{"UNILEVER":0,"MILLER COORS":5300,"DANNON":1000}
```
