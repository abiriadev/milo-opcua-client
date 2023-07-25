# Milo OPC UA to AAS Client

A configurable OPC UA Client

## How to build

### Manual Build

```
$ ./gradlew build
```

### Docker

```
$ docker buildx build -t milo-opcua-client:v0.1.0 --target runner -o type=docker .
```

### Docker Compose

```
$ docker compose build
```

## How to run

### Using Gradle

```
$ ./gradlew run

# with target OPC UA server address
$ ./gradlew run --args="--url opc.tcp://vcp_generator:52530/OPCUA/VcpDemoServer"
```

### Run manually

```
$ java -jar ./app/build/libs/app-all.jar

# with target OPC UA server address
$ java -jar ./app/build/libs/app-all.jar --url opc.tcp://vcp_generator:52530/OPCUA/VcpDemoServer
```

### Using Docker

```
$ docker run --rm milo-opcua-client:v0.1.0

# with target OPC UA server address
$ docker run --rm milo-opcua-client:v0.1.0 --url opc.tcp://vcp_generator:52530/OPCUA/VcpDemoServer
```

### Using Docker Compose

```
$ docker compose -d

# with target OPC UA server address
$ docker compose -f compose.yaml -f compose-opcua.yaml up
```

## Usage

List all the nodes that you want to monitor in the
`./app/src/main/resources/ua.properties` file. Each node
should be formatted as 'Name: OPC UA Node ID' and placed on
a separate line.
