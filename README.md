# Customer Statement Processor

Bank receives monthly deliveries of customer statement records.
This information is delivered in two formats, CSV and XML. These records need to be validated.

# Technologies
Java 8 , Spring Boot

## Application info
Application is hosted on port `8080` and accessible using below url

http://localhost:8080/

Upload valid csv or xml file . Failure Report is displayed below. 

# Usage

## Input
The format of the file is a simplified format of the MT940 format. The format is as follows:

| Field | Description |
| ---- | ---- |
| Transaction reference | A numeric value |
| Account number | An IBAN  |
| Start Balance | The starting balance in Euros  |
| Mutation | Either an addition (+) or a deduction (-) |
| Description | Free text  |
| End Balance | The end balance in Euros |

## Output
There are two validations:
* all transaction references should be unique
* the end balance needs to be validated

At the end of the processing, a report needs to be created which will display both the transaction reference and description of each of the failed records.

## Application Build Test and Deployment
Build the Application using Maven
`mvn clean install`

Run Unit Test
`mvn clean test`

Server can be started using below command:

`java -jar target/statement-processor-0.0.1-SNAPSHOT.jar`

or using Maven
`mvn spring-boot:run`

## The Application has a REST API service and UI

### via REST API
REST API to process the files, located at `/processFile`
The XML or CSV file which contains transactions can be posted to the service and gets validated.

### Use Swagger to Test the REST Endpoint
http://localhost:8080/swagger-ui.html#/statement-processor-controller

Check attached "Screenshot.doc" for detailed screenhot for testing

### Via Postman
Either PostMan or an equivalent applications can be used to use the service.

### Via Curl
The endpoint can be used via `cURL` using the following command:
```bash
curl -X POST "http://localhost:8080/processFile" -H "accept: */*" -H "Content-Type: multipart/form-data" -F "file=@records.csv;type=text/csv"




