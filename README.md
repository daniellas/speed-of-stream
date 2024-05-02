# Speed of Stream

Java streams API benchmarks described in my articles on medium.com:

* [Speed of Stream](https://medium.com/@daniel.las/speed-of-java-stream-1cc3a94b44c2)
* [Performance of Parallel Stream](https://medium.com/@daniel.las/performance-of-parallel-java-streams-68988191d9f8)

Install `Java 21` and `Apache Maven`

1. Run `mvn clean install` to produce `jar`
1. Run `java -jar target/speed-of-stream.jar -p [profile]` to run benchmarks
   - where `profile` is the JVM arguments profile name (`default` is used if not defined)

Go to [jupyterlab](jupyterlab) to find out how to view the results.
