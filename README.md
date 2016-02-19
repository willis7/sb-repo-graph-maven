# Maven Dependency Graph
[![Build Status](https://travis-ci.org/willis7/sb-repo-graph-maven.svg?branch=master)](https://travis-ci.org/willis7/sb-repo-graph-maven)
[![Coverage Status](https://coveralls.io/repos/willis7/sb-repo-graph-maven/badge.svg?branch=master&service=github)](https://coveralls.io/github/willis7/sb-repo-graph-maven?branch=master)
[![License](http://img.shields.io/:license-mit-blue.svg)](http://doge.mit-license.org)

This project is a work in progress, and very early in development.

The idea behind this project is to leverage the metadata from your Maven (and eventually Gradle) build definitions to build a graph of dependendies. The idea is that you can then search the graph using GAV parameters to better understand the usage of dependencies.

## Installation

This project uses Docker Compose to build the development environment. Make sure its installed before progressing.

To run this application, follow these instructions:

1. First, you will need to compile the build. From the command line run `./gradlew clean build` from the root of the project.
2. Once the build has compiled successfully, run `docker-compose build && docker-compose run -d`

If this is successful you should see some artifact details logged to the command line. You can then navigate to the Neo4J container url and see the graph which was generated. `http://<<container address>>:7474`

## Showcase

<img src="https://cdn.pbrd.co/images/1FGI3w3H.png" width="200" height="200" />

## Todo

* Seperate the application into a runner and a server.
    * The runner can push the results of a local analysis, and the server can perform the security check. The runner could be Gradle and Maven plugins.
* Use [Dependency Check](https://github.com/jeremylong/DependencyCheck)

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