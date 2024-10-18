BASEDIR=$(dirname "$0")
export TARGET_DIR=${BASEDIR}/../../../../target

java \
    -Dorg.rtsl.dhis2.testtool.properties=${BASEDIR}/testtool.properties \
    -Dlogback.configurationFile=${BASEDIR}/logback.xml -Dfile.ending=UTF8 \
    -jar ${TARGET_DIR}/Dhis2CucumberTestTool-*.jar \
    --object-factory io.cucumber.spring.SpringFactory \
    --plugin html:${TARGET_DIR}/cucumber-reports/test_reports.html \
    --plugin json:${TARGET_DIR}/cucumber-reports/test_reports.json \
    --plugin pretty \
    --glue "org.rtsl.dhis2.cucumber.definitions" \
    --glue "io.cucumber.spring" \
    --threads 1 \
    ${BASEDIR}/scenarios
