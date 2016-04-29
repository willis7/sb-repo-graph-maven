package io.byteshifter.depsgraph

import org.neo4j.graphdb.GraphDatabaseService
import org.neo4j.graphdb.factory.GraphDatabaseFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.data.neo4j.config.EnableNeo4jRepositories
import org.springframework.data.neo4j.config.Neo4jConfiguration

import org.springframework.data.neo4j.rest.SpringRestGraphDatabase

/**
 * Application entry point
 *
 * @author Sion Williams
 */

@EnableNeo4jRepositories(basePackages = "io.byteshifter.depsgraph")
@SpringBootApplication
class Application extends Neo4jConfiguration {

    public Application() {
        setBasePackage("io.byteshifter.depsgraph")
    }

//    @Profile("dev")
//    @Bean(destroyMethod = "shutdown")
//    GraphDatabaseService graphDatabaseServiceDev() {
//        return new GraphDatabaseFactory().newEmbeddedDatabase("target/dependency.db")
//    }


    @Bean
    GraphDatabaseService graphDatabaseService() {
        // Check if a Docker compose env var is set, else use localhost
        def hostname = System.getenv("DB_PORT_7474_TCP_ADDR") ?: 'localhost'
        println "<<<<<<<<<<<<<<<<<<<< Hostname: ${hostname}"
        return new SpringRestGraphDatabase("http://${hostname}:7474/db/data")
    }



    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application, args)
    }
}
