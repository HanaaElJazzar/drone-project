## Drones Management Rest APIs

----------

## **Getting Started**

This drone-project is a sample project for registering and managing a fleet of **10 drones** by registering them successfully with required validations on the fields, getting the Idle ones that are ready for loading, load the drones with medications based on specific constraints, and get the list of medications loaded by a drone. The below are the detailed requirements for the project.

## Task description

We have a fleet of **10 drones**. We will use them to load medications.

A **Drone** has and validated upon all the below conditions:

    - serial number (100 characters max);
    - model (Lightweight, Middleweight, Cruiserweight, Heavyweight);
    - weight limit (500gr max);
    - battery capacity (percentage);
    - state (IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING).

Each **Medication** has and validated upon all the below conditions:

    - name (allowed only letters, numbers, ‘-‘, ‘_’);
    - weight;
    - code (allowed only upper case letters, underscore and numbers);
    - image (picture of the medication case).

I will develop services via REST API's that allows clients to communicate with our drones fleet. 

The service should allow:

    - registering a drone;
    - loading a drone with medication items;
    - checking loaded medication items for a given drone;
    - checking available drones for loading;
    - check drone battery level for a given drone;

## Requirements

While implementing, take care of the following requirements:

**Functional requirements**

- no need for UI;
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
    cd /
    git init
    git remote add origin https://github.com/HanaaElJazzar/drone-project.git
    git pull
    git checkout main
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
   - ![Database Connection](https://github.com/HanaaElJazzar/test-repo/blob/master/dummyData/DatabaseConnect.JPG)

------------

## API Endpoints and Testing:

### My Project Assumptions and Suggestions

Some of the assumptions used in the project:
- Each medication can be loaded to only one drone
- We can load more than one medication as long as their total weights is not exceeding the allowed drone weight.
- The scheduled task runs every minute to check drones' batteries' life
- Drone weight limit should be between 1-500 grams
- Drone battery capacity is between 0-100 % assuming it could be decimal like 98.8%
- Battery Monitor Event Scheduler will log the levels of Battery Capacity for the drones with less than 25% level every minute for all drone with different statuses assuming that a drone battery might deteriorate to less than 25% while delivering or even while returning from delivering medications.
- Logging of Drones Monitor Battery Capacity will be done in DB Table called DRONE_MONITOR_EVENTS table and on Application Console
- Validations Checks are done on all Drone and Medications requests variables.
- Our Drone Fleet can have maximum 10 drones. This is defined as a variable in drone.properties file (drone.fleet = 10) where it can be easily modified for testing purposes or if requirements changes.
  - ![Number of Drones in our Fleet](https://github.com/HanaaElJazzar/test-repo/blob/master/OriginalDroneFleet.JPG)
- I assume we can send in the load Drone Request with Medications that might not be in Database and can be added in the request itself if it is not there.
- Check if the Medication is added already to another drone, or the total some of the medications is greater than the drone limit, the whole request is rejected (It cannot be partially delivered)

### Available Unit Tests that can be used:

- In DroneControllerTest:
    - testRegisterDroneSuccess()
    - testRegisterDroneFail()
    - testGetAvailableDronesSuccess()
    - testGetBatteryCapacitySuccess()
    - testGetLoadedMedications()
- In DroneServiceTest:
    - testSaveDrone()
    - testGetAvailableDrones()
    - testGetBatteryCapacity()
- In DroneMonitorEventServiceTest:
    - testDroneMonitorLogEvent()
  
### Testing using Postman

- Open postman for testing purposes
- Make sure the content-type in postman Headers is set to application/json

### Dummy Data Initialized

- Once the project is initialized, **DataInitializer** class will initialize some dummy data in the database (Drone and Medication tables):
  - Drone Table Initial Data:
    - ![DRONE Table](https://github.com/HanaaElJazzar/test-repo/blob/master/dummyData/Drone.JPG)
  - Medication Table Initial Data:
    - ![MEDICATION Table](https://github.com/HanaaElJazzar/test-repo/blob/master/dummyData/Medication.JPG)
  - The scheduled Task to Monitor Battery Level for Drones will automatically starts inserting records in Drone_Monitor_events Table will Automatically.
    - ![DRONE_MONITOR_EVENTS Table](https://github.com/HanaaElJazzar/test-repo/blob/master/dummyData/DroneMonitorEvent.JPG)

--------- 
### Battery Monitor Log Scheduler:

CheckDronesBatteryLevelsScheduler will run:
- method checkDronesBatteryLevels every minute
- log the levels of Battery Capacity for the drones with less than 25% every minute.
- It is logging this under logType: BATTERY_MONITOR in table DRONE_MONITOR_EVENTS in database
- Also, it is logging this on application console

---------

### 1 - **Drone Registration API:**

### - The api details:
- url is: http://localhost:8082/api/v1/drones/registerDrone
- It is a POST api.
- Sample Successful Request:
  ```
   {
    "serialNumber":"DR1234567B", 
    "model":"Lightweight", 
    "weightLimit":"300.0", 
    "batteryCapacity":"80.0", 
    "state":"IDLE"
   }
- Sample Successful Response:
  ```
  {
    "success": true,
    "message": "New Drone created successfully",
    "data": {
        "drone": {
            "id": 1,
            "serialNumber": "DR1234567B",
            "model": "Lightweight",
            "weightLimit": 300.0,
            "batteryCapacity": 80.0,
            "state": "IDLE",
            "medications": []
        }
    },
    "timestamp": "2023-05-07T00:50:20.8191153"
  }

- Kindly check the below postman request with successful request and response:

  ![Register A Drone Successfully](https://github.com/HanaaElJazzar/test-repo/blob/master/RegisterDroneSuccess.JPG)


### - Validations Applied:
  - First of all, If you try to create two drones with same serialNumber, you will get an error with success = false
    - ![Register A Drone with already existing serialNumber](https://github.com/HanaaElJazzar/test-repo/blob/master/2-DuplicateDrone.JPG)
  - Second, you will get error messages as below if you tried to enter wrong model or state values for the Drone:
    - ![Register A Drone with wrong model](https://github.com/HanaaElJazzar/test-repo/blob/master/4-InvalidModel.JPG) 
  - then, an automated validations based on restrictions previously applied on Drone Fields like weight should be between 1 and 500 grs, battery capacity should be between 0 - 100%.
    - ![Register A Drone with wrong weight limit not within range](https://github.com/HanaaElJazzar/test-repo/blob/master/3-Validations.JPG)

    - ![Register A Drone with wrong weight limit not within range](https://github.com/HanaaElJazzar/test-repo/blob/master/wrong-validations.JPG)

  - Test the dynamic number of drones in our fleet as follows: Set drone.fleet = 3 in drones.properties file, and run register Drone Api for the fourth time, you will get the below error:
    - ![drone.properties file](https://github.com/HanaaElJazzar/test-repo/blob/master/TestDronesFleetNumber.JPG)
    - ![Error Message we get for Exceeding Fleet Number](https://github.com/HanaaElJazzar/test-repo/blob/master/TestFleetDronesNumber.JPG)
  - Handle Any Exceptions that might happen
------------------

### 2 - **Get List of Available Drones for Loading API:**

### - The api details:
- url is: http://localhost:8082/api/v1/drones/getAvailableDrones
- It is a GET api.
- No Request Body
- Sample Successful Response:
  ```
  {
    "success": true,
    "message": "Available drones retrieved successfully.",
    "data": {
        "availableDrones": [
            {
                "id": 1,
                "serialNumber": "DR1234567A",
                "model": "Lightweight",
                "weightLimit": 350.0,
                "batteryCapacity": 80.0,
                "state": "IDLE",
                "medications": []
            },
            {
                "id": 2,
                "serialNumber": "DR1234567D",
                "model": "Lightweight",
                "weightLimit": 450.0,
                "batteryCapacity": 40.0,
                "state": "IDLE",
                "medications": []
            }
        ]
    },
    "timestamp": "2023-05-07T02:19:00.1673599"
  }

- Kindly check the below postman request with successful request and response:

  ![Get Available Drones](https://github.com/HanaaElJazzar/test-repo/blob/master/getAvailableIdleDrones.JPG)


### - Validations Applied:
- Handle the case where list returned is empty, you will get an error with success = false
    - ![No Drones Available - Returned Empty List](https://github.com/HanaaElJazzar/test-repo/blob/master/NoIdleDronesAvailable.JPG)
- Handle Any Exceptions that might happen

--------------------------------------

### 3 - **Get Battery Level of a Drone:**

### - The api details:
- url is: http://localhost:8082/api/v1/drones/getDroneBatteryCapacity
- It is a GET api.
  - Sample Successful Request:
     ```
    {
      "serialNumber": "DR1234567C"
    }

- Sample Successful Response:
  ```
  {
    "success": true,
    "message": "Battery Capacity Returned.",
    "data": null,
    "timestamp": "2023-05-05T00:39:13.0572976",
    "batteryCapacity": 50.0
  }

- Kindly check the below postman request with successful request and response:
  ![Get Drone Battery Capacity](https://github.com/HanaaElJazzar/test-repo/blob/master/BatteryCapacityApi.JPG)

### - Validations Applied:
- Handle the case where the serialNumber is sent as empty, you will get an error with success = false
    - ![serialNumber is missing Error](https://github.com/HanaaElJazzar/test-repo/blob/master/BatteryCapacityValidation2.JPG)
- Handle the case where no Drone is found for such serialNumber, thus, return error with success = false
    - ![serialNumber is missing Error](https://github.com/HanaaElJazzar/test-repo/blob/master/BatteryCapacityValidation1.JPG)      
- Handle Any Exceptions that might happen


-------------------------------------

### 4 - **Load Drone with Medications:**

### - The api details:
- url is: http://localhost:8082/api/v1/drones/loadDroneWithMedications
  - It is a POST api.
- Sample Successful Request:
   ```
  {
    "serialNumber": "DR1234567A",
    "medications": [
       {
           "name": "Medication1",
           "weight": 100,
           "code": "MED001",
           "image": "iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg=="
       },
       {
           "name": "Medication2",
           "weight": 200,
           "code": "MED002",
           "image": ""
       }
    ]
  }

- Sample Successful Response:
  ```
  {
    "success": true,
    "message": "Medications Loaded successfully to drone with serialNumber: DR1234567A",
    "data": {
        "drone": {
            "id": 1,
            "serialNumber": "DR1234567A",
            "model": "Lightweight",
            "weightLimit": 350.0,
            "batteryCapacity": 80.0,
            "state": "LOADED",
            "medications": [
                {
                    "id": 1,
                    "name": "Medication1",
                    "weight": 100.0,
                    "code": "MED001",
                    "image": "iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg=="
                },
                {
                    "id": 2,
                    "name": "Medication2",
                    "weight": 200.0,
                    "code": "MED002",
                    "image": ""
                }
            ]
        }
    },
    "timestamp": "2023-05-07T00:28:23.3553612"
  }

- Kindly check the below postman request with successful request and response:
  ![Load Drone Successfully with Medications](https://github.com/HanaaElJazzar/test-repo/blob/master/loadDrone/SucessfulDroneLoading.JPG)

### - Validations Applied:
- Handle the case where the serialNumber is sent as empty, you will get an error with success = false
- Handle the case where the serialNumber sent do not have an existing drone corresponds to it, or no IDLE Drone available
    - ![IDLE Drone Not Available](https://github.com/HanaaElJazzar/test-repo/blob/master/loadDrone/Drone%20not%20found%20or%20not%20available.JPG)
- Handle the case where the IDLE drone has Battery Capacity < 25%, thus Drone cannot be loaded
    - ![Drone Battery < 25%](https://github.com/HanaaElJazzar/test-repo/blob/master/loadDrone/DroneBatteryLow.JPG)
- Handle the case where the medication requested it loading into a specific drone is loaded into another drone already. 
    - ![Medication Already loaded to another Drone](https://github.com/HanaaElJazzar/test-repo/blob/master/loadDrone/MedicationLoadedIntoAnotherDrone.JPG)
- Handle the case where Medications total weight exceeds the drone weight limit
    - ![Medications total weight exceeds Drone Weight Limit](https://github.com/HanaaElJazzar/test-repo/blob/master/loadDrone/MedicationsWeightExceedsDroneWeightLimit.JPG)
- Handle the case where Validations on all Medication Requests Fields are applied
    - ![Medication Object Fields' Validations](https://github.com/HanaaElJazzar/test-repo/blob/master/loadDrone/ValidationOfMedicationsFields.JPG)
- Handle Any Exceptions that might happen

#### Database:

- Database Entities for Drone and Medication entities will look like:
  - DRONE
    - ![DRONE](https://github.com/HanaaElJazzar/test-repo/blob/master/loadDrone/Drones.JPG)
  - MEDICATION
    - ![MEDICATION](https://github.com/HanaaElJazzar/test-repo/blob/master/loadDrone/Medications2.JPG)

-------------------------------------

### 5 - **Get Medications Loaded in a Drone:**

### - The api details:
- url is: http://localhost:8082/api/v1/drones/getLoadedMedications
    - It is a GET api.
- Sample Successful Request:
   ```
  {
    "serialNumber": "DR1234567A"
  }

- Sample Successful Response:
  ```
  {
    "success": true,
    "message": "Medications Loaded into drone DR1234567A returned successfully.",
    "data": {
        "medications": [
            {
                "id": 1,
                "name": "Medication1",
                "weight": 100.0,
                "code": "MED001",
                "image": "iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg=="
            },
            {
                "id": 2,
                "name": "Medication2",
                "weight": 200.0,
                "code": "MED002",
                "image": ""
            }
        ]
    },
    "timestamp": "2023-05-07T00:39:35.5865484"
  }

- Kindly check the below postman request with successful request and response:
  ![Get Loaded Medications to a Drone Successfully](https://github.com/HanaaElJazzar/test-repo/blob/master/getLoadedMedications/GetLoadedMedicationsForDrone.JPG)

### - Validations Applied:
- Handle the case where the serialNumber sent do not have an existing drone corresponds to it
    - ![Drone Not Found](https://github.com/HanaaElJazzar/test-repo/blob/master/getLoadedMedications/DroneNotFound.JPG)
- Handle Any Exceptions that might happen

