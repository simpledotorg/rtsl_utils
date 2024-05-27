
BASEDIR=$(dirname "$0")
export TARGET_DIR=${BASEDIR}/../../../../target

# -Dlogback.debug=true 

java \
    -Dlogback.configurationFile=${BASEDIR}/logback.xml -Dfile.ending=UTF8  -Dlogback.debug=true \
    -jar ${TARGET_DIR}/Dhis2CucumberTestTool-*-jar-with-dependencies.jar \
    --plugin html:${TARGET_DIR}/cucumber-reports/test_reports.html \
    --plugin json:${TARGET_DIR}/cucumber-reports/test_reports.json \
    --plugin pretty \
    --glue "org.rtsl.dhis2.cucumber.definitions" \
    --threads 4 \
     ${BASEDIR}/scenarios


