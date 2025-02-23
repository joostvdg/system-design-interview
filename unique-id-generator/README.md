# Unique ID Generator

One of the constraints, is that the ID needs to be sortable.

One of the recommended types, is Twitter's Snowflake.

Another, I think is recommendable is the TSID, or Time-Sorted Unique Identifier.
As described by Vlad Mihalcea in his article [The best UUID type for a database Primary Key](https://vladmihalcea.com/uuid-database-primary-key/)

## TODO

* write more unit tests
* setup logging
  * so we can move the current bits setting logging to debug
* publish the artifact to GitHub Packages

## Snowflake

Snowflake is a 64-bit unique ID generator, that is composed of the following parts:

* 1 bit for sign (always 0)
* 41 bits for timestamp with millisecond precision, using a custom epoch.
* 10 bits for a machine id (5 bits for datacenter, 5 bits for worker)
* 12 bits for a sequence number

## TSID

Is very similar to Snowflake:

* 42 bits for timestamp with millisecond precision, using a custom epoch.
* 22 bits random component
  * node id (0-20 bits)
  * counter (2-22 bits)

## Calculate Epoch

You can find the current millis from this site: https://currentmillis.com/
