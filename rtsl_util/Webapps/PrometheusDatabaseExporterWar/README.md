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


## SQL Metric Descript systax


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
