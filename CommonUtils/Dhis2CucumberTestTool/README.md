# DHIS2 Cucumber Test Tool

The tool is a collection of step definitions which allow DHIS2 operators vet the behaviour of their instance using Cucumber. It allows using business-level language to specify the behaviour of a DHIS2 instance. This way, non-technical people are welcome to the fun of shaping how a DHIS2 instance supports a program.

## Viewing Step Documentation

The step definitions we have are documented in the javadoc. This means documentation for the step definitions are intermingled with the code documentation.

### How to generate the step documentation

First of all, build a clean package with the `javadoc:javadoc` goal

```
mvn clean package javadoc:javadoc
```

This would export the docs into `${PROJECT_ROOT}/apidocs`. You can then use your favourite HTTP server to serve the HTML documents in that folder.

```
python -m http.server <port>
```

Point your browser to `localhost:<port>` to see the generated Java documentation.

The step definitions are at `org.rtsl.dhis2.cucumber.definitions > Dhis2StepDefinitions`
