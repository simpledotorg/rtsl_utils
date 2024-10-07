#!/bin/sh

PROPERTIES_FILE="input.properties"
SCENARIOS=""

while IFS='=' read -r key value; do
    if [[ $key == scenarios ]]; then
        SCENARIOS=$value
    fi
done < "$PROPERTIES_FILE"

java -Dorg.rtsl.dhis2.testtool.properties=input.properties -Dlogback.configurationFile=log-config.xml -Dfile.ending=UTF8 -jar Dhis2CucumberTestTool-*.jar --object-factory io.cucumber.spring.SpringFactory --plugin html:./test_reports.html --plugin json:./test_reports.json --plugin pretty --glue "org.rtsl.dhis2.cucumber.definitions" --glue "io.cucumber.spring" --threads 1 $SCENARIOS
