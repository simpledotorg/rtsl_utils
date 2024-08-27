# Purpose

This application purpose is to allow generating metrics at the prometheus format from DB queries, along with useful metadata.




# Descriptor syntax

A metric descriptor is a json file that contain the following information



| Syntax      | Type  | Possible Values| Description |
| ----------- | ----------- |----------- |----------- |
| type  | String        | sql / file        | The type of that descritor. Each types comes with its own parameters |
| asynch      | Boolean        | true / **false**       | Describes if the query should be made synchronously or asynchronously. |
| cron   | Cron string        | "* * * * *" or any other cron string | Frequency of the call. Only taken in account if **asynch** is **true**. |


Examples
* 


