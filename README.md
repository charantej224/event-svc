# EventService
Event service is the listener application for the employee service.

## Running the application

###### 1. clone employee service using below command
git clone https://github.com/charantej224/event-svc.git

###### 2. change the directory
cd event-svc/

###### 3. Build,compile,tests the application
./gradlew build
* view jacoco test coverage reports at path  *
./build/reports/jacoco/test/html/index.html
Note: Only Service and controllers are covered as part of test coverage.

###### 4. Run the application
./gradlew

Swagger API contracts:
http://localhost:9095/event-service/v2/api-docs

## Testing the application:
** Note: Before proceding this step, event service "Running the application" steps are to be completed. **

before this step, the employee service is been tested as per instructions.
* step1: run the below curl command to get the list of events streamed for an email id (please note, the data below is inline with the curls provided for employee service)

```
curl -X GET \
  'http://localhost:9095/event-service/api/event-records?emailId=charantej.career@gmail.com' \
  -H 'Postman-Token: 506c70f9-30cc-4e5b-a8ac-38a78e301f2f' \
  -H 'cache-control: no-cache'
  ```


### Additional: SONARQUBE reports

Sonar is used to analyse code quality. You can start a local Sonar server (accessible on http://localhost:9001) with:
```
docker-compose -f src/main/docker/sonar.yml up -d
```

Then, run a Sonar analysis:

```
./gradlew -Pprod clean test sonarqube
```
