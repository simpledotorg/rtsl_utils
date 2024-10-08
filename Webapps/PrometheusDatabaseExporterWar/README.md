# Purpose

This application purpose is to allow generating metrics at the prometheus format from DB queries, along with useful metametrics (status, processing time, etc ...) .



# Metrics Descriptor syntax

A metric descriptor is a json file that contain all the information required to return a coherent set of metrics. A part of it depends on the type of Metric source we want to use, and a part is dynamic and depends on the selected type.

For now, only two types exist: **sql** and **file**. File is implemented as the default asynch cache for metrics as memory was not suitable in case of restart. It should not be used in a real life scenario.

# Common Syntax


| Syntax      | Type  | Possible Values| Description |
| ----------- | ----------- |----------- |----------- |
| type  | String        | sql / file        | The type of that descritor. Each types comes with its own parameters |
| asynch      | Boolean        | true / **false**       | Describes if the query should be made synchronously or asynchronously. |
| cron   | Cron string        | Any other cron string (like "* * * * *")  | Frequency of the call. Only taken in account if **asynch** is **true**. |
| name   | String        |  | Human friendly name of the decriptor. Will be present as a label il all metametrics |


## SQL Metric Descript syntax


| Syntax      | Type  | Possible Values| Description |
| ----------- | ----------- |----------- |----------- |
| query  | String        | Any Valid SQL statement        | The query used to gather the metrics. |
| metrics      | List of Metrics Descriptors |      | Describes if the query should be made synchronously or asynchronously. |
| metrics[X].metricName      | List of Metrics Descriptor        |      |  |
| metrics[X].metricValueKey      | List of Metrics Descriptor        |      |  |
| metrics[X].labelsKeys      | Map of labelNames <=> the query column in which they can be found        |      |  |


## Examples

### Heartbeat (synchronous)

The simplest descriptor possible is that one:
```json
{
    "asynch": false,
    "type": "sql",
    "name": "dbHeartbeat",
    "query": "select 1 as value, 'database' as source;",
    "metrics": [{
            "metricName": "postgres_heartbeat",
            "metricValueKey": "value",
            "labelsKeys": {
                "source": "source"
            }
        }]
}
```

The fixed value **1** is selected into column **value** and the fixed string **'database'** into column **source** 

This will generate the following metric 
```
postgres_heartbeat{source="database"} 1
```

### getSessionInfo (Synchronous)

Another Simple descriptor can be this one, that gets a count of DB sessions per by application_name (in Postgresql)

It will return one metric per application_name present in the currently connected sessions.

```json
{
    "asynch": false,
    "type": "sql",
    "name": "getSessionInfo",
    "query": "select application_name, count(*) as value from pg_stat_activity group by application_name",
    "metrics": [{
            "metricName": "postgres_sessions_per_application_count",
            "metricValueKey": "value",
            "labelsKeys": {
                "application_name": "application_name"
            }
        }]
}
```


This will generate the following metrics (obviously depending on the application 
```
postgres_sessions_per_application_count{application_name="Patroni"} 1
postgres_sessions_per_application_count{application_name=""} 5
postgres_sessions_per_application_count{application_name="Metabase v0.47.10 [19167a88-5ba7-42ce-8800-7e0443652b59]"} 1
postgres_sessions_per_application_count{application_name="PrometheusDatabaseExporterWar"} 2
```



### Tracked Entities (Asynchronous)

A more complex query can be found below. It is DHIS2 specific and counts the number of tracked entities by type.

It is configured to be run every minute. The real time query will be against a local file containing cached values.

```json
{
    "asynch": true,
    "type": "sql",
    "cron": "* * * * *",
    "name": "DHIS2_Trackedentityinstance",
    "query": "select  trackedentityinstance.trackedentitytypeid, trackedentitytype.name, count(*) as value from trackedentityinstance left outer join trackedentitytype on trackedentityinstance.trackedentitytypeid = trackedentitytype.trackedentitytypeid group by trackedentityinstance.trackedentitytypeid, trackedentitytype.name",
    "metrics": [{
            "metricName": "dhis2_trackedentityinstance_count",
            "metricValueKey": "value",
            "labelsKeys": {
                "trackedentitytypeid": "trackedentitytypeid",
                "name": "name"
            }
        }]
}
```

Following the same logic, it will generate metrics looking like this:
```
dhis2_trackedentityinstance_count{name="Patient", trackedentitytypeid="XXXXXXXXX"} 45623
dhis2_trackedentityinstance_count{name="Other", trackedentitytypeid="YYYYYYYYY"} 45
```


# Architectural Design Overview and Appliction Configuration.

## Packaging

The application is packaged as a Java War. This allows to build it agnostically of the deployment environment. In case we want to change the packaging, it should be done easily as classes that are not related to configuration are abstracted in a dependency jar (OpenMetricsUtils)

Note that everything related to the packaging is already done in the docker image created by this build

## Logs

Logging is done through logback. Nothing in the code refers logback explicitly, logback.xml file location is supposed to be injected through environment variables (or java arguments if you must)

### Configuring Logs in Tomcat
If run in a tomcat, the following environment variables should be added:

```
JAVA_OPTS " -Dlogback.ContextSelector=JNDI "
```

Then logs should be configured in the deployment as in the descriptor below
* [Tomcat Deployment descriptor](../../docker_content/prometheus_db_exporter/PrometheusDbExporter.xml)


Note that all of this is done in the docker image generated by the build

## Properties File

The configuration can be done through a properties file containing the following properties


| Property      | Type  | Default Value| Description |
| ----------- | ----------- |----------- |----------- |
| database.jdbc.driverClassName | String | org.postgresql.Driver | JDBC driver class. Can be left to default if using postgresql |
| database.jdbc.url | String | jdbc:postgresql://localhost:5432/dhis?ApplicationName=PrometheusDatabaseExporterWar | JDBC Url. It can be a good idea to keep ApplicationName in the final JDBC url |
| database.jdbc.username | String |  | username (**mandatory**) |
| database.jdbc.password | String |  | password (**mandatory**) |
| database.jdbc.maxPoolSize | int | 10 | number of DB connections the app is going to maintain. It is useful for multithreading |
| config.parallel.thread.pool.size | int | 10 | number of maximum concurrent threads |
| config.root.folder | String |  | path to the folder containing the json descriptors|
| cache.folder | String |  | Path to the folder that will be used as a local cache for asynch metrics.  |

## Docker Image
