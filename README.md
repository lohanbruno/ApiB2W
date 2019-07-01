# API Stars Wars

# Requirements

- Java 8
- Maven
- MongoDB

# How to use 
- Launch MongoDB at default port by typing **mongod** on your terminal.
- Inside the project's folder, type **mvn spring-boot:run** on your terminal to launch the application.
- To run tests use **mvn test**

# URLS
## Get all Planets
## GET/planets 
### Response: 
```
[
  {
    "id": "5d1946aef067ed1738eb3e76",
    "name": "Alderaan",
    "climate": "temperate",
    "terrain": "grasslands, mountains",
    "appearances": 2
  },
  {
    "id": "5d1946d5f067ed1738eb3e77",
    "name": "Hoth",
    "climate": "frozen",
    "terrain": "tundra, ice caves, mountain ranges",
    "appearances": 1
  },
  {
    "id": "5d194717f067ed1738eb3e78",
    "name": "Naboo",
    "climate": "temperate",
    "terrain": "grassy hills, swamps, forests, mountains",
    "appearances": 4
  }
]
```

## Get a planet by id
## GET/planets/id
### Example: planets/5d194717f067ed1738eb3e78
### Response: 200 OK
```
{
  "id": "5d194717f067ed1738eb3e78",
  "name": "Naboo",
  "climate": "temperate",
  "terrain": "grassy hills, swamps, forests, mountains",
  "appearances": 4
}
```
### Response: 404 Not Found
If there's no planet with that id

## Get a planet by name
## GET/planets?name=searchQuery
### Example: /planets?name=Alderaan
### Response: 200 OK
```
{
  "id": "5d1946aef067ed1738eb3e76",
  "name": "Alderaan",
  "climate": "temperate",
  "terrain": "grasslands, mountains",
  "appearances": 2
}
```
### Response: 404 Not Found
If there's no planet with that name

## Create a planet by sending name,terrain and climate and it'll use Swapi Api to calculate films appearances
### POST/planets
### Example: 
```
{
    "name": "Coruscant",
    "climate": "temperate",
    "terrain": "cityscape, mountains"
}
```
### Reponse: 200 OK
```
{
  "id": "5d194e9ef067ed1738eb3e79",
  "name": "Coruscant",
  "climate": "temperate",
  "terrain": "cityscape, mountains",
  "appearances": 4
}
```

### Response: 409 Conflict
Planet already exists

### Response: 400 Bad Request
If no name, climate or terrain are sent 

## Delete a planet by id
### DELETE/planets/id
### Example: /planets/5d1946aef067ed1738eb3e76
### Response: 204 No Content
If it finds a planet with that id

### Reponse: 404 Not Found
If there's no planet with that id










