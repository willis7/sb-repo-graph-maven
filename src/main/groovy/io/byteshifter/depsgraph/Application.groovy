package io.byteshifter.depsgraph

import io.byteshifter.depsgraph.domain.Artifact
import io.byteshifter.depsgraph.domain.ArtifactRepository
import io.byteshifter.depsgraph.services.MavenPomReader
import io.byteshifter.depsgraph.services.POMFinder
import io.byteshifter.depsgraph.utils.ModelToArtifact
import org.apache.maven.model.Model
import org.neo4j.graphdb.GraphDatabaseService
import org.neo4j.graphdb.Transaction
import org.neo4j.graphdb.factory.GraphDatabaseFactory
import org.neo4j.kernel.impl.util.FileUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.neo4j.config.EnableNeo4jRepositories
import org.springframework.data.neo4j.config.Neo4jConfiguration
import org.springframework.data.neo4j.core.GraphDatabase

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
        return new GraphDatabaseFactory().newEmbeddedDatabase("build/mavendeps.db")
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
        def transformer = new ModelToArtifact()


        Transaction tx = graphDatabase.beginTx()
        try {

            // For each pom, read it and build its dependency graph
            results.each { pomFile ->
                // Build a Maven Model from the POM
                Model model = MavenPomReader.readModelPom(new File(pomFile.path))

                // Create and save an Artifact node from the Maven Model
                Artifact artifact = transformer.transform(model)
                artifactRepository.save(artifact)

                // For each of the dependencies, create a new Artifact node and create
                // a dependency vector
                model.dependencies.each { dependency ->
                    Artifact depnd = transformer.transform(dependency)
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
        FileUtils.deleteRecursively( new File("build/mavendeps.db") )

        SpringApplication.run(Application.class, args)
    }
}
