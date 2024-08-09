# OPA Java SDK Changelog

## v1.6.0

* Fixed omission of jackson-databind dependency type change to `api`, which was supposed to go out with 1.5.3.

## v1.5.3

* Marked jackson-databind as an `api` dependency in addition to jackson-core.
* Fixed a bug where `OPALatencyMeasuringHTTPClient` would report results in ns rather than ms.

## v1.5.2

* Fixed a bug where instantiating `OPALatencyMeasuringHTTPClient` with the default constructor could result in a null pointer exception while formatting messages.

## v1.5.1

* Regenerated low-level API with Speakeasy 1.351.2
* Release `OPALatencyMeasuringHTTPClient`

