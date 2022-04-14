# Log Event Processor

## Pre Requisites

```bash
Java 1.8
Gradle 7.4.2
```
All other dependencies are pulled-in by Gradle during build.

## Executing Program

Build the project
```bash
cd [project_checkout_path]
gradle clean build
```
Start the Application 
```bash
cd [project_checkout_path]\build\libs
java -jar event-processor.jar <log file path>
```

## Details

1. Log file path should be provided as the input to the program. If not provided the program fails with an exception

```bash
Caused by: java.lang.IllegalArgumentException: Path of log file expected as input argument.
	at cs.event.LogEventApplication.run(LogEventApplication.java:27) [classes!/:na]
	at org.springframework.boot.SpringApplication.callRunner(SpringApplication.java:818) [spring-boot-2.0.5.RELEASE.jar!/:2.0.5.RELEASE]
	... 13 common frames omitted
```
2. For the log file name provided, the file is read and each line in the file processed independently as an individual event, allowing the multi-threading to handle each line separately.
3. Standard java-8 feature file.lines to support large files (another classic way would be using BufferedReader)
4.  .parallel is used to improve performance by parallelising reading of parallel lines.
4. If the difference between the STARTED and FINISHED state is more than 4 ms, then an alert status is flagged in DB
