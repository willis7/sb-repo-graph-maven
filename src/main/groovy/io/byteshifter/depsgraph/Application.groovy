package io.byteshifter.depsgraph

import io.byteshifter.depsgraph.domain.Artifact
import io.byteshifter.depsgraph.domain.ArtifactRepository
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
        return new SpringRestGraphDatabase("http://localhost:7474/db/data");
    }

    @Autowired
    ArtifactRepository artifactRepository

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

                // Create and save an Artifact node from the Maven Model
                Artifact artifact = new Artifact(groupId: model.groupId,
                                                    artifactId: model.artifactId,
                                                    version: model.version)
                artifactRepository.save(artifact)

                // For each of the dependencies, create a new Artifact node and create
                // a dependency vector
                model.dependencies.each { dependency ->
                    Artifact depnd = new Artifact(groupId: dependency.groupId,
                                                    artifactId: dependency.artifactId,
                                                    version: dependency.version)
                    artifactRepository.save(depnd)
                    artifact.dependsOn(depnd)
                    artifactRepository.save(artifact)
                }

                // Retrieve the artifact from the database and print its dependencies
                artifactRepository.findByArtifactId( artifact.artifactId ).dependencies.each { println "\t-" + it.groupId + " " + it.artifactId + " " + it.version }
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
