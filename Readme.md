<img src="https://github.com/hashmapinc/hashmap.github.io/blob/master/images/tempus/Tempus_Logo_Black_with_TagLine.png" width="950" height="245" alt="Hashmap, Inc Tempus"/>

[![License](http://img.shields.io/:license-Apache%202-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt)

# TimeSeries Simulator
This is a wrapper around the TSimulus time series generator that allows for realistic time-series CSV files to be created
for testing in a time series analysis system. It leverages the TSimulus generator configuration JSON. It outputs a CSV file 
with columns that are representative of the exports in the JSON file. 

## Table of Contents

- [Features](#features)
- [Requirements](#requirements)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [License](#license)

## Features

This application aims to provide a few key features:

* Realistic time series CSV file creation
* Configurable file sizes

## Requirements

* JDK 1.8 at a minimum
* Scala 2.11
* Maven 3.1 or newer
* Git client (to build locally)

## Getting Started
To build the library and get started first off clone the GitHub repository 

    git clone https://github.com/hashmapinc/TimeSeriesSimulator.git

Change directory into the WitsmlObjectsLibrary

    cd TimeSeriesSimulator
    
Execute a maven clean install

    mvn clean install
    
A Build success message should appear
    
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time: 7.681 s
    [INFO] Finished at: 2017-08-16T15:47:03-05:00
    [INFO] Final Memory: 22M/327M
    [INFO] ------------------------------------------------------------------------

## Usage

Copy the electicConfig.json from the resources directory to the target directory

Change into the target directory and execute the uber jar with the following command

    java -jar uber-tssimulatorcontroller-1.0-SNAPSHOT.jar electricConfig.json 100
    
A csv file names output - 0.csv should be created with the 100 values.

## License

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

 

