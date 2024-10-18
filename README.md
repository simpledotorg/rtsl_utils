

# Prerequisite

brew install openjdk@21 maven


# To compile the whole project

```
cd rtsl_util
mvn clean install
```



# To test the cucumber POC

```
cd CommonUtils/Dhis2Verifier
bash ./src/test/resources/local_tests/tests.ad.local.sh 
```

# Reading Test results

Tests results can be found here:

```
cd CommonUtils/Dhis2Verifier
ls CommonUtils/Dhis2Verifier/target/cucumber-reports 
```








