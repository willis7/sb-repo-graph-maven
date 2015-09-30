package io.byteshifter.depsgraph.services

import org.apache.maven.model.Model
import spock.lang.Specification

/**
 * @author Sion Williams
 */
class MavenPomReaderSpec extends Specification {
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
        model.groupId == 'org.apache.activemq'
        model.artifactId == 'activemq-parent'
        model.version == '5.9.1'
    }
}
