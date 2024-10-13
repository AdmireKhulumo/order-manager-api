# Overview
This document contains information about my solution including important assumptions I made, things I would add if I had more time, and how to run the application. 

Tech stack: Java 17, MySQL, Docker.

## Assumptions
* An order is immutable and cannot be updated.
* A ticker is at least two letters. This is enforced in the validation.
* The order summary:
  * Requirement is to filter by a date range instead of single day. This makes it more granular.
  * The currency is the same when fetching an order summary. In reality, there would be a need to do conversations prior to storing an order or at the time of getting the summary.

## If I had more time ... 
I had to stop somewhere, unfortunately, so here is a list of things I would have done if I had more time:
* Add an authentication library and associated roles/permissions for the endpoints.
* Add a middleware which acts as a global error handler to catch errors thrown by the system and provide standard API responses using the ApiResponse class. This would also implement a handler for the validation errors to provide more details in 400 error responses.
* Add integration tests at controller level. At present, I have unit tests but the testing strategy would be better with integration and system tests.
* Replace the database secrets in my docker-compose.yml file with a Kubernetes secret (opaque) object for running in production. Same goes with the application-local.yml file.
* Add Swagger UI or a similar OpenAPI alternative for viewing and interacting with the API spec.
* Add more custom validation depending on the business logic. 
* Emit Kafka events when a new order is created. In theory these would be useful to other systems which may need to perform subsequent actions.
* Setup Open Telemetry and ship logs, traces, and metrics to a platform such as Elastic. I would also inject the correlation/trace Id in the API response.
* Connect and run it on the cloud - GCP of course, with a non-local database.
* Conditionally run tests as part of the build process. These would only run when building in certain scenarios, such as a PR, but not on merge. It can be controlled by an environment variable. 

# How To Run The App
This application can be run with a Docker compose command. I have kept sample secrets in the docker-compose.yml file, so it should be enough to run this on the project's root: 
` docker-compose up --build `

# REST API Endpoints

## Get Order
Fetch a single order using its Id.

```
curl --location 'http://localhost:8080/v1/orders/3'
```

**Sample Response - 200 OK**
```
{
    "success": true,
    "message": "Order found.",
    "data": {
        "id": 3,
        "ticker": "AAPL",
        "side": "SELL",
        "volume": 16745.00,
        "currency": "BWP",
        "price": 167.45,
        "createdAt": "2024-10-11T20:00:16Z"
    }
}
```

## Create Order
Creates a new immutable order whereby the date is set to the time at which the order is created. The order Id is added automatically.

**Sample Request**
```
curl --location 'http://localhost:8080/v1/orders' \
--header 'Content-Type: application/json' \
--data '{
  "ticker": "SAVE",
  "side": "BUY",
  "volume": 16745,
  "currency": "BWP",
  "price": 1637.45
}'
```

**Sample Response - 201 Created**
```
{
    "success": true,
    "message": "Created new order.",
    "data": {
        "id": 10,
        "ticker": "SAVE",
        "side": "BUY",
        "volume": 167345,
        "currency": "BWP",
        "price": 1137.45,
        "createdAt": "2024-10-11T21:54:41.297794Z"
    }
}
```

## Get Order Summary
Gets the summary of an order given a ticker symbol and timestamp range. 

**Sample Request**
```
curl --location 'http://localhost:8080/v1/orders/summary?ticker=AAPL&startDate=2024-10-11T00%3A00%3A00Z&endDate=2024-10-11T23%3A59%3A59Z'
```

**Sample Success Response - 200 OK**
```
{
    "success": true,
    "message": "Order summary calculated.",
    "data": {
        "totalOrders": 5,
        "sideSummaries": {
            "BUY": {
                "averagePrice": 46.12,
                "maxPrice": 123.45,
                "minPrice": 1.45
            },
            "SELL": {
                "averagePrice": 91.95,
                "maxPrice": 167.45,
                "minPrice": 16.45
            }
        }
    }
}
```

*PS: "BWP" is the currency of my home country, Botswana :)*