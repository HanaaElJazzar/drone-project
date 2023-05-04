## Drones Management Rest APIs

----------

:scroll: **Getting Started**

This drone-project is a sample project for registering and managing a fleet of 10 drones by registering them successfully with required validations on the fields, getting the Idle ones that are ready for loading, load the drones with medications based on specific constraints, and get the list of medications loaded by a drone.
We are free to make assumptions about the project, like:
- I am validating the correctness of the fields before I even proceed with the request for both Drones and Medications,
-

The below are the detailed requirements for the project:

## Task description

We have a fleet of **10 drones**. A drone is capable of carrying devices, other than cameras, and capable of delivering small loads. For our use case the load is medications.

A **Drone** has:

    - serial number (100 characters max);
    - model (Lightweight, Middleweight, Cruiserweight, Heavyweight);
    - weight limit (500gr max);
    - battery capacity (percentage);
    - state (IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING).

Each **Medication** has:

    - name (allowed only letters, numbers, ‘-‘, ‘_’);
    - weight;
    - code (allowed only upper case letters, underscore and numbers);
    - image (picture of the medication case).

Develop a service via REST API that allows clients to communicate with the drones (i.e. dispatch controller). The specific communicaiton with the drone is outside the scope of this task.

The service should allow:

    - registering a drone;
    - loading a drone with medication items;
    - checking loaded medication items for a given drone;
    - checking available drones for loading;
    - check drone battery level for a given drone;

## Requirements

While implementing, please take care of the following requirements:

**Functional requirements**

- There is no need for UI;
- Prevent the drone from being loaded with more weight that it can carry;
- Prevent the drone from being in LOADING state if the battery level is below 25%;
- Introduce a periodic task to check drones battery levels and create history/audit event log for this.

**Non-functional requirements**

- Input/output data must be in JSON format;
- Your project must be buildable and runnable;
- Your project must have a README file with build/run/test instructions (use DB that can be run locally, e.g. in-memory, via container);
- Required data must be preloaded in the database.
- JUnit tests are optional but advisable (if you have time);
- Advice: Show us how you work through your commit history.

--------------------------------------

## Technologies Used

- Java 17
- Spring Boot 3.0.5
- IntelliJ IDEA Ultimate
- JUnit 5.8.0 (For Unit Testing)
- H2 in-memory database (meaning creation of drones has to be done each time from the beginning, below there will be sample inputs for drones and medications)
- Postman (For Testing)
- Gradle 7.6.1

-------------------------------------

## Running the Application

1. Clone the project from GitHub:

    ```bash
    git clone https://github.com/HanaaElJazzar/drone-project.git
    ```

2. Navigate to the project directory:
    ```bash
    cd /path/drone-project
    ```
3. Open the project with IntelliJ IDEA
4. Go to gradle in the project and update all gradle dependencies
4. Build the project through gradle and run it.
5. Before running, you can run the JUnit test cases included in the project to assert that everything is running correctly.
6. After running the project, the application will start running at http://localhost:8082/
7. You can also after running the application, open and manage h2 database using url http://localhost:8082/h2-console with connection information:
    - username: sa
    - password:
    - url: jdbc:h2:mem:dronedb

------------

## API Endpoints and Testing:

### Assumptions

Some of the assumptions used in the project:
- Each medication can be loaded to only one drone
- We can load more than one medication as long as their total weights is not exceeding the allowed drone weight.
- The scheduled task runs every minute to check drones' batteries' life

### Testing using Postman

- Open postman for testing purposes
- Make sure the content-type in postman Headers is set to application/json

**Drone Registration API:**

The api details:
- url is: http://localhost:8082/api/v1/drones/registerDrone
- It is a POST api.
- Sample Successful Request:
  ```
   {
       "serialNumber":"DR1234567A",
       "model":"Lightweight",
       "weightLimit":"500.0",
       "batteryCapacity":"98.8",
       "state":"IDLE"
   }
- Sample Successful Response:
  ```
  {
    "success": true,
    "message": "New Drone created successfully",
    "data": {
      "id": 1,
      "serialNumber": "DR1234567A",
      "model": "Lightweight",
      "weightLimit": 500.0,
      "batteryCapacity": 98.8,
      "state": "IDLE"
    },
    "timestamp": "2023-05-04T15:14:57.2569029"
  }

- Kindly check the below postman request with successful request and response:

  ![Register A Drone Successfully](https://github.com/HanaaElJazzar/test-repo/blob/master/1-RegisterADrone.JPG)


- **Validations Applied:**
  - First of all, If you try to create two drones with same serialNumber, you will get an error with success = false
    - ![Register A Drone with already existing serialNumber](https://github.com/HanaaElJazzar/test-repo/blob/master/2-DuplicateDrone.JPG)
  - Second, you will get error messages as below if you tried to enter wrong model or state values for the Drone:
    - ![Register A Drone with wrong model](https://github.com/HanaaElJazzar/test-repo/blob/master/4-InvalidModel.JPG) 
  - then, an automated validations based on restrictions previously applied on Drone Fields like weight should be between 1 and 500 grs, battery capacity should be between 0 - 100%.
    - ![Register A Drone with wrong weight limit not within range](https://github.com/HanaaElJazzar/test-repo/blob/master/3-Validations.JPG)

    - ![Register A Drone with wrong weight limit not within range](https://github.com/HanaaElJazzar/test-repo/blob/master/wrong-validations.JPG)

**Get List of Available for Loading Drones API:**

The api details:
- url is: