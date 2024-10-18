#!/bin/bash -l

cd "$(dirname "$0")"

PROPERTIES_FILE="input.properties"
SCENARIOS=""

if [ ! -f "$PROPERTIES_FILE" ]; then
  echo "$PROPERTIES_FILE not found!"
  read -rsp $'Press enter to continue...\n'
  exit 1
fi

cat "$PROPERTIES_FILE"

while IFS='=' read -r key value; do
  if [ "$key" = "scenarios" ]; then
    SCENARIOS=${value%$'\r'}
  fi
done < "$PROPERTIES_FILE"

echo "$SCENARIOS"

if [ -z "$SCENARIOS" ] || [ "$SCENARIOS" = "<path-to-features[-folder]>" ]; then
  echo "Ensure that the 'scenarios=' key is set in input.properties.\n"
  read -rsp $"Press enter to continue..."
  exit 1
elif [ -f "$SCENARIOS" ] || [ -d "$SCENARIOS" ]; then
  java -Dorg.rtsl.dhis2.testtool.properties=input.properties -Dlogback.configurationFile=log-config.xml -Dfile.ending=UTF8 -jar Dhis2Verifier-*.jar --object-factory io.cucumber.spring.SpringFactory --plugin html:./test_reports.html --plugin json:./test_reports.json --plugin pretty --glue "org.rtsl.dhis2.cucumber.definitions" --glue "io.cucumber.spring" --threads 1 $SCENARIOS
  read -rsp $"Press enter to continue..."
  exit 0
fi

read -rsp $"Press enter to continue..."
