from maven:3.9-amazoncorretto-21 as packager

arg VERSION

env HOME=/rtsl/utils
run mkdir -p $HOME
workdir $HOME
add . $HOME
workdir $HOME/CommonUtils/Dhis2CucumberTestTool

run --mount=type=cache,target=$HOME/.m2 mvn clean package

# this docker file must not built the mvn package
# it should only copy the JAR into the docker image
