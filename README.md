# personal-site

Welcome to the repository of my personal site!

It was written in Spring Boot, with a vanilla front-end (HTML/CSS/JavaScript), and a PostgreSQL database.

## Download & Build Instructions

This project requires [Java 25](https://openjdk.org/projects/jdk/25/) to build and run.

To download this project, run the following command:

`git clone https://github.com/patrickijieh/personal-site.git && cd personal-site`

To build this project, run the following command:

`./mvnw verify`

This will then place the build artifacts and the final packaged jar file in a folder called `target`. You can then run the packaged file like so:

`java -jar target/personalsite-2.0.jar`

Which will serve the application to localhost on port 8443. This can be changed in the application.properties file, located in `src/main/resources`.
