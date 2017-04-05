# viewlift-api-client
Example client code for calling Viewlift API

# Building

The code can be compiled and and executable Jar can be created using Gradle by running the following command

./gradlew fatJar

# Testing

Once the jar is create you can run the following command to create a site named Viewlift

java -jar build/libs/viewlift-api-client-all-1.0.jar {apikey}

Make sure you pass the provided API Key as the first argument.
