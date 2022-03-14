# BIMtoOSM
[![CI](https://github.com/rebeccasc/BIMtoOSM/actions/workflows/gradle.yml/badge.svg?branch=master&event=push)](https://github.com/rebeccasc/BIMtoOSM/actions/workflows/gradle.yml)
[![license: AGPLv3](https://img.shields.io/badge/license-AGPLv3-blue.svg?style=flat-square&maxAge=7200)](https://github.com/rebeccasc/BIMtoOSM/blob/master/LICENSE)

<p align="left">
  <img width="97%" src="docs/banner.svg">
</p>

A parser to transform **B**uilding **I**nformation **M**odeling ([BIM](https://en.wikipedia.org/wiki/Building_information_modeling)) to **O**pen**S**treet**M**ap ([OSM](https://www.openstreetmap.org/#map=18/50.81353/12.92928&layers=N)) data. This open source project wants to make Building Information Model data available for OpenStreetMap.

## Quick start
### Use the API
:construction: **Currently, the source code is being rebuilt from scratch and the API might change** :construction:
```kotlin
// Setup configuration (for full description see wiki -> Usage):
val defaultConfig = Configuration()

// Init parser
val parser = BIMtoOSMParser(defaultConfig)

// Parse BIM file
val osmData: OSMDataSet = parser.parse("path-to-file.ifc")

// Access the data (for full description see wiki -> Usage)

// Export OSM data
Exporter.exportOSM("bim-to-osm.osm", osmData, true)
```

### Use JOSM GUI
The parser is integrated into [JOSM](https://josm.openstreetmap.de/)s [IndoorHelper plugin](https://wiki.openstreetmap.org/wiki/JOSM/Plugins/indoorhelper). To use the GUI you need to install JOSM and download the IndoorHelper plugin via `Edit` &#8594; `Preferences` &#8594; `Plugins`. Import example:

<p align="center">
  <img width="75%" src="https://wiki.openstreetmap.org/w/images/e/e2/Bim-import.gif">
</p>

<br>

## Dependencies
* [opensourceBIM/IfcPlugins](https://github.com/opensourceBIM/IfcPlugins)
* [JTS Topology Suite](https://github.com/locationtech/jts)

## Contributing
Want to contribute? Check the [contribution guidelines](https://github.com/rebeccasc/BIMtoOSM/blob/master/CONTRIBUTING.md)

## Wiki

Find a detailed user guide at https://rebeccasc.github.io/BIMtoOSM/

## Authors
Rebecca Schmidt (rebeccasmdt@gmail.com)
