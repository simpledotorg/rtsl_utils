# DHIS2 Cucumber Test Tool

The tool is a collection of step definitions which allow DHIS2 operators vet the behaviour of their instance using Cucumber. It allows using business-level language to specify the behaviour of a DHIS2 instance. This way, non-technical people are welcome to the fun of shaping how a DHIS2 instance supports a program.

## Configuration

To get the latest release, click on the "Releases" on the right of the repo page under the "Code" tab and select the release you want to download. Under "Assets", download the `release-artefacts.zip`. Inside this ZIP folder you'll find the `input.properties` which we would be using to configure the verifier.

```properties
dhis2.api.url=<url of your running DHIS2 instance>
dhis2.api.username=<superuser username>
dhis2.api.password=<superuser password>
scenarios=<path-to-features[-folder]>
```

Update the `<missing details>` with the specific details for your instance.

## Documentation

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
