package com.willis7

import com.willis7.domain.Dependency
import com.willis7.repository.DependencyRepository
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
 * @author Sion Williams
 */
@Configuration
@EnableNeo4jRepositories(basePackages = "com.willis7")
class Application extends Neo4jConfiguration implements CommandLineRunner {

    public Application() {
        setBasePackage("com.willis7")
    }

    @Bean
    GraphDatabaseService graphDatabaseService() {
        return new GraphDatabaseFactory().newEmbeddedDatabase("mavendeps.db")
    }

    @Autowired
    DependencyRepository dependencyRepository

    @Autowired
    GraphDatabase graphDatabase

    @Override
    void run(String... args) throws Exception {
        Dependency junit = new Dependency(groupId: 'junit', artifactId: 'junit', version: '1.0')
        Dependency spock = new Dependency(groupId: 'spock', artifactId: 'spock', version: '1.0')
        Dependency geb = new Dependency(groupId: 'spock', artifactId: 'spock', version: '1.0')

        println "Before linking with neo4j.."
        [junit, spock, geb].each { println it }

        Transaction tx = graphDatabase.beginTx()
        try {

            // Save the entities to the database
            dependencyRepository.save(junit)
            dependencyRepository.save(spock)
            dependencyRepository.save(geb)

            // Retrieve the entities and configure their relationships
            geb = dependencyRepository.findByArtifactId(geb.artifactId)
            geb.dependsOn(junit)
            geb.dependsOn(spock)
            dependencyRepository.save(geb)

            spock = dependencyRepository.findByArtifactId(spock.artifactId)
            spock.dependsOn(junit)
            dependencyRepository.save(spock)

            println "Lookup each dependency by artifactId"
            [junit, spock, geb].each { println dependencyRepository.findByArtifactId( it.artifactId ) }

            println "Print Gebs dependencies"
            dependencyRepository.findByDependenciesArtifactId(geb.artifactId).each { println it.artifactId }

            tx.success()

        } finally {
            tx.close()
        }
    }

    public static void main(String[] args) throws Exception {
        FileUtils.deleteRecursively( new File("mavendeps.db") )

        SpringApplication.run(Application.class, args)
    }
}
