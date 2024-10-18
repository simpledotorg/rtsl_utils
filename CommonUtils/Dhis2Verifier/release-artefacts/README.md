# Dhis2Verifier

This tool runs your test against a running version of DHIS2 accessible over the internet. The `exec` script is what ties everything together to run the tests. But before this can work, there are some pre-requisites necessary

> :warning: **DISCLAIMER**: With this version of the tool, you would have to run the script from a command prompt; "Terminal" on OS X, or "Command Prompt" on Windows. Future versions, where this disclaimer is not in the README, would allow you to double-click on the script and have it run once the `input.properties` have been properly specified.

## Pre-requisites

### Path to scenarios

Ensure the path to your scenarios is properly specified as an **absolute path**. What this looks like is

…on Windows

```
scenarios=C:\path\to\the\scenarios
```

…on Unix

```
scenarios=/path/to/the/scenarios
```

### DHIS2 is up

Please ensure that the DHIS2 instance is running, and is accessible from the internet.

To check

- enter the value of `dhis2.api.url` in your browser
- ensure you can login with `dhis2.api.username` and `dhis2.api.password`

## How to run

- Download the zip file from the release into `/some/path` on your computer. This could be `~/Downloads` on Unix, or `C:\Downloads` on Windows; any path would do.
- Open `input.properties` in your favourite text editor and ensure the properties are set to their correct values as instructed above
- Open a command prompt
- Enter the following commands in order

```sh
cd /some/path
./exec.sh
```

>If you run into any issues running this, please open an issue with us at https://github.com/simpledotorg/rtsl_utils/issues.
