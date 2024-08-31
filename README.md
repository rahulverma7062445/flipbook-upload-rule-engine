# How to use flipbook-upload-rule-engine in local environment

### Prerequisites

Java 17 or above.

* To install `java` in your local environment follow below steps:

1. Go to this [link](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) and download and install `java` in your local environment.


* To run `flipbook-upload-rule-engine` in your local environment follow below steps:

1. Go to this [link](https://innersource.soprasteria.com/di-codebase/sopraflipbook/flipbook-upload-rule-engine) and download
or clone `flipbook-upload-rule-engine` project in your local environment.
2. Create a directory called external and copy `flipbook-web` here.
3. Open `flipbook-upload-rule-engine` project in an IDE(i.e. IntelliJ IDEA).
4. Open `src/main/resources/application.properties` and edit that file e.g.

```
destinationDir = path of external directory+/
extension = png
logging.file.path=logs/
logging.file.name=logs/application.log
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=100MB
spring.servlet.multipart.max-request-size=100MB
server.port=8080
sourceDirectory= path of external directory+/flipbook-web/
targetDirectory= C:/Apache24/htdocs/
```
5. After step 4 run the project.
6. Now `flipbook-upload-rule-engine` project is up and running.


* To run `flipbook-upload-rule-engine` `jar` in your local environment follow below steps:

1. Go to this [link](https://innersource.soprasteria.com/di-codebase/sopraflipbook/flipbook-upload-rule-engine) and download `flipbook-upload-rule-engine` jar file in your local environment.
2. Create a directory called external and copy `flipbook-web` here.
3. Create `application.properties` file in the same directory as `flipbook-upload-rule-engine` jar, edit it and save it i.e.
4. Copy downloaded `jar` file in step 1 in the same directory as `application.properties`.

```
destinationDir = path of external directory+/
extension = png
logging.file.path=logs/
logging.file.name=logs/application.log
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=100MB
spring.servlet.multipart.max-request-size=100MB
server.port=8080
sourceDirectory= path of external directory+/flipbook-web/
targetDirectory= C:/Apache24/htdocs/
``` 

5. Now open command prompt in the directory where the `jar` file is present and run `java -jar name_of_jar_file_with_extension`.
6. Now `flipbook-upload-rule-engine` is up and running.
