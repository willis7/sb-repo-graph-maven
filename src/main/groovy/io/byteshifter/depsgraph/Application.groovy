package io.byteshifter.depsgraph

import io.byteshifter.depsgraph.domain.Dependency
import io.byteshifter.depsgraph.domain.DependencyRepository
import io.byteshifter.depsgraph.services.MavenPomReader
import io.byteshifter.depsgraph.services.POMFinder
import org.apache.maven.model.Model
import org.neo4j.graphdb.GraphDatabaseService
import org.neo4j.graphdb.Transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.neo4j.config.EnableNeo4jRepositories
import org.springframework.data.neo4j.config.Neo4jConfiguration
import org.springframework.data.neo4j.core.GraphDatabase
import org.springframework.data.neo4j.rest.SpringRestGraphDatabase

/**
 * Application entry point
 *
 * @author Sion Williams
 */
@Configuration
@EnableNeo4jRepositories(basePackages = "io.byteshifter.depsgraph")
class Application extends Neo4jConfiguration implements CommandLineRunner {

    public Application() {
        setBasePackage("io.byteshifter.depsgraph")
    }

    @Bean
    GraphDatabaseService graphDatabaseService() {
        def hostname = System.getenv("DB_PORT_7474_TCP_ADDR") ?: 'localhost'
        println "<<<<<<<<<<<<<<<<<<<< Hostname: ${hostname}"
        return new SpringRestGraphDatabase("http://${hostname}:7474/db/data")
    }

    @Autowired
    DependencyRepository artifactRepository

    @Autowired
    GraphDatabase graphDatabase

    @Override
    void run(String... args) throws Exception {

        // Search a directory and return all of the available poms
        POMFinder pomFinder = new POMFinder()
        def results = pomFinder.getAllPOMs('/src/test/resources/repository/')

        Transaction tx = graphDatabase.beginTx()
        try {
            // Delete any entries from previous runs
            artifactRepository.deleteAll()


            // For each pom, read it and build its dependency graph
            results.each { pomFile ->
                // Build a Maven Model from the POM
                Model model = MavenPomReader.readModelPom(new File(pomFile.path))

                // Create and save an Dependency node from the Maven Model
                Dependency dependency = new Dependency(groupId: model.groupId,
                                                    artifactId: model.artifactId,
                                                    version: model.version)
                artifactRepository.save(dependency)

                // For each of the dependencies, create a new Dependency node and create
                // a dependency vector
                model.dependencies.each { Dependency it ->
                    Dependency depnd = new Dependency(groupId: it.groupId,
                                                    artifactId: it.artifactId,
                                                    version: it.version)
                    artifactRepository.save(depnd)
                    it.dependsOn(depnd)
                    artifactRepository.save(it)
                }

                // Retrieve the artifact from the database and print its dependencies
                artifactRepository.findByArtifactId( dependency.artifactId ).dependencies.each { println "\t-" + it.groupId + " " + it.artifactId + " " + it.version }
            }

            tx.success()

        } finally {
            tx.close()
        }
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args)
    }
}
