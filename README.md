# Speed of stream

Java streams API benchmarks

Install `Java 21` and `Apache Maven`

1. Run `mvn clean install` to produce `jar`
1. Create `target/results` folder
1. Run `java -jar target/speed-of-stream-1.0.0-SNAPSHOT.jar -p [profile]` to run benchmarks
  * where `profile` is the JVM arguments profile name (`default` is used if not defined)

Go to [jupyterlab](jupyterlab) to find out how to view the results.
