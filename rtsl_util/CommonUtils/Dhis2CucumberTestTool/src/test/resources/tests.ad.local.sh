
BASEDIR=$(dirname "$0")
export TARGET_DIR=${BASEDIR}/../../../target


java \
    -jar ${TARGET_DIR}/Dhis2CucumberTestTool-*-jar-with-dependencies.jar \
    --plugin html:${TARGET_DIR}/cucumber-reports/test_reports.html \
    --plugin json:${TARGET_DIR}/cucumber-reports/test_reports.json \
    --plugin pretty \
    --glue "org.rtsl.dhis2.cucumber.definitions" \
     ${BASEDIR}/scenarios




ls    ${BASEDIR}/scenarios
