# Maven Dependency Graph
[![Build Status](https://travis-ci.org/willis7/sb-repo-graph-maven.svg?branch=master)](https://travis-ci.org/willis7/sb-repo-graph-maven)
[![Coverage Status](https://coveralls.io/repos/willis7/sb-repo-graph-maven/badge.svg?branch=master&service=github)](https://coveralls.io/github/willis7/sb-repo-graph-maven?branch=master)
[![License](http://img.shields.io/:license-mit-blue.svg)](http://doge.mit-license.org)

This project is a work in progress and very early in development.

The idea behind this project is to leverage the metadata from your Maven (and eventually Gradle) files to build a graph of dependendies and how they link together. The idea is that you can then search the graph using the popular GAV parameters of Maven to establish the usage of dependencies.


## Installation

This project uses both Docker and Gradle to help build and develop this application.

To run this application, follow these instructions:

1. First you will need an instance of Neo4j. The easiest way is to use Docker.
2. In `Application.groovy` make sure you have the correct url for your Neo4j.
3. Run `./gradlew bootRun` from the project root.

If this is successful you should see some artifacts output to the command line, then you can look at your graph in Neo4j.

## Usage

TODO: Write usage instructions

## Contributing

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

## History

TODO: Write history

## Credits

TODO: Write credits

## License

MIT