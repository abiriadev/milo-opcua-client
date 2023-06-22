# Milo OPC UA to AAS Client

## How to build

### Manual Build

```
$ ./gradlew build
```

### Docker

```
$ docker buildx build -t milo-opcua-client:v0.1.0 --target runner -o type=docker .
```

## How to run

### Using Gradle

```
$ ./gradlew run
```

### Run manually

```
$ java -jar ./app/build/libs/app-all.jar
```

### Using Docker

```
$ docker run --rm milo-opcua-client:v0.1.0
```

### Using Docker Compose

```
$ docker compose -d
```

## Usage

List all the nodes that you want to monitor in the
`./app/src/main/resources/ua.properties` file. Each node
should be formatted as 'Name: OPC UA Node ID' and placed on
a separate line.
