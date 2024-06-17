

# Prerequisite

brew install openjdk@21 maven


# To compile the whole project

```
cd rtsl_util
mvn clean install
```



# To test the cucumber POC

```
cd rtsl_util/CommonUtils/Dhis2CucumberTestTool
bash ./src/test/resources/local_tests/tests.ad.local.sh 
```

# Reading Test results

Tests results can be found here:

```
cd rtsl_util/CommonUtils/Dhis2CucumberTestTool
ls rtsl_util/CommonUtils/Dhis2CucumberTestTool/target/cucumber-reports 
```








