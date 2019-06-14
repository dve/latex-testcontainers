# latex-testcontainers

A java wrapper around [`latex-docker`](https://github.com/blang/latex-docker) implemnted with [Testcontainers](https://www.testcontainers.org/)

## Build

Clone and run:

```
./mvnw clean package
```

To see a vaadin based demo run:

```
./mvnw -pl demos exec:java -Dexec.mainClass="net.vergien.ltc.demo.Launcher"
```

and open [`localhost:8899`](http://localhost:8899/) in your webbrowser.