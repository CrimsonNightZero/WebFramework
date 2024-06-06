# WebFramework

The research project goal to understand what is a web framework tech stack.

The project include three important parts:
1. HTTP Protocol: The client can send a request, and then the server listens on a socket port. When the server receives the data, it processes from request to responsse.
2. Core Web Framework: This core framework provides developers with tools to quickly build their own services.
3. Domain Service: Developers build domain-specific services using the web framework. This is easy user login system.

## HTTP protocol

* The client can send an HTTP request. 
* The HTTP request deserializes HTTP information, including the method, path, query, body, and headers, and then the server handles these requests. 
* When the request task is finished, the server serializes the data into an HTTP response. 
* Finally, the client receives data from the HTTP response.

## Core WebFramework

Current core stack including the following components:
* Router:
  * Developer can define controller, including request and response infomation.
  * The router will automatically reflect the defined request and reponse to target class.
  * Finally, you can easily procees received request and transfer response using your injection service.
* TransformBodyTypeHandler: 
* ExceptionHandler: 
* Container: 
  
## Domain Service

* Application
* Domain
* Infrastructure

## Running service

1. Locate the package named `UserSystem`.
2. Run `Main.java`
3. You will can see following infomation indicating that the service has successfully started on port 8080:
```
Server started. Listening for connections on port 8080...
```
4. At the point, you can try to send an HTTP request using Postman or any browser or in any other way.

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
