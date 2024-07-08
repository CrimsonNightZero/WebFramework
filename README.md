# WebFramework

The research project goal to understand what is a web framework tech stack.

The project includes three important parts:
1. HTTP Protocol: The client can send a request, and then the server listens on a socket port. When the server receives the data, it processes it from request to response.
2. Core Web Framework: This core framework provides developers with tools to build their services quickly.
3. Domain Service: Developers build domain-specific services using the web framework. This is an easily user login system.

## HTTP protocol

* The client can send an HTTP request. 
* The HTTP request deserializes HTTP information, including the method, path, query, body, and headers, and then the server handles these requests. 
* When the requested task is finished, the server serializes the data into an HTTP response. 
* Finally, the client receives data from the HTTP response.

## Core WebFramework

The current core stack includes the following components:

* Router:
  * Developer can define controller, including request and response information.
  * The router will automatically reflect the defined request and response to the target class.
  * Finally, you can easily process received requests and transfer responses using your injection service.
* TransformBodyTypeHandler:
  * When received a request needs to be deserialized and the transferred response needs to be serialized.
  * TransformBodyTypeHandler will transform your defined data type to offer developers convenient usage. 
* ExceptionHandler:
  * If the service encounters any exceptions, you want to respond with a response-specific status code, message, and so on.
  * ExceptionHandler can reduce the time needed to wrap the response format and efficiently decouple your code.
  * You just need to define handling exceptions and register a custom ExceptionHandler, ExceptionHandler will automatically handle exceptions. 
* Container:
  * Container helps the system maintain the injection state in your controller and service, preventing your service from recreating the injection object with every request.
  * You can freely choose injected objects and how to maintain the object state.
  
## Domain Service

The service follows clean architecture and domain-driven design concepts in building its architecture

* Domain layer:
  * This layer contains core domain knowledge of service, allowing you to wrap rich product domains and iteratively your best product.
  * To maintain clarity, this layer should not depend on any other layers.
* Infrastructure layer:
  * This layer's main responsibility is handling external data or services.
  * You can define controllers to receive requests and transfer responses, access your database, manage third-party API, and so on.
* Application layer:
  * This layer facilitates communication between the domain layer and the infrastructure layer for each use case.

## Running service

1. Locate the package named `UserSystem`.
2. Run `Main.java`
3. You will see the following information indicating that the service has successfully started on port 8080:
```
Server started. Listening for connections on port 8080...
```
4. At this point, you can try to send an HTTP request using Postman or any browser or in any other way.

## System Designing Diagram
![alt_text](https://github.com/clarkwtc/WebFramework/blob/5f47aa7b8c0e1eebf220c4807183f46c297371aa/images/Webframework.png)

## API 

### RegisterUser 
#### Path
* Mehod: POST
* Path: /api/users

#### Headers
* content-type: application/json
  
#### Body
| name| type |
|-|-|
|email|string |
|name|string |
|password|string |

#### Response 
* status code: 201

---

### Login 
#### Path
* Mehod: POST
* Path: /api/users/login

#### Headers
* content-type: application/json
  
#### Body
| name| type |
|-|-|
|email|string |
|password|string |

#### Response 
* status code: 200

#### Response Headers
* content-type: application/json
* content-encoding: UTF-8

#### Response Body
| name| type |
|-|-|
|id|int |
|email|string |
|name|string |
|token|string |

---

### Rename 
#### Path
* Mehod: PATCH
* Path: /api/users/1

#### Headers
* content-type: application/json
* Authorization: Bearer <token>
  
#### Body
| name| type |
|-|-|
|newName|string |

#### Response 
* status code: 204

---

### UserQuery

#### Path
* Mehod: GET
* Path: /api/users

#### Headers
* Authorization: Bearer <token>
  
#### Query
| name| type |
|-|-|
|keyword|string |

#### Response 
* status code: 200

#### Response Headers
* content-type: application/json
* content-encoding: UTF-8

#### Response Body
| name| type |
|-|-|
|id|int |
|email|string |
|name|string |

---
## Contact me

If you are curious about the project, I have OOA and OOD design diagrams. Feel free to contact me to discuss anything you find interesting.
