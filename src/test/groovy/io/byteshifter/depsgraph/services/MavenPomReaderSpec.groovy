package io.byteshifter.depsgraph.services

import org.apache.maven.model.Model
import spock.lang.Specification

/**
 * @author Sion Williams
 */
class MavenPomReaderSpec extends Specification {

    def "creating an instance of MavenPomReader throws an exception"() {
        when:
        new MavenPomReader()

        then:
        thrown UnsupportedOperationException
    }

    def "test readModelPom()"() {
        setup:
        def resource = getClass()
                .getClassLoader()
                .getResource('repository/org/apache/activemq/activemq-pool/5.9.1/activemq-pool-5.9.1.pom')
                .toURI()
        def pomFile = new File(resource)

        when:
        Model model = MavenPomReader.readModelPom(pomFile)

        then:
        model.name == "ActiveMQ :: Pool"
        model.dependencies.size() == 12
        model.groupId == null
        model.artifactId == 'activemq-pool'
        model.version == '5.9.1'
    }

    def "canonicalToPath throws exception with bad form"() {
        when:
        MavenPomReader.canonicalToPath("garbage")

        then:
        thrown IllegalArgumentException
    }

    def "canonicalToPath converts correctly"() {
        expect:
        MavenPomReader.canonicalToPath(a) == b

        where:
        a                                                                   || b
        "org.codehaus.groovy:groovy-all:jar:2.4.0"                          || "org/codehaus/groovy/groovy-all/2.4.0/groovy-all-2.4.0.jar"
        "org.springframework.data:spring-data-neo4j-rest:jar:3.2.1.RELEASE" || "org/springframework/data/spring-data-neo4j-rest/3.2.1.RELEASE/spring-data-neo4j-rest-3.2.1.RELEASE.jar"
    }
}
