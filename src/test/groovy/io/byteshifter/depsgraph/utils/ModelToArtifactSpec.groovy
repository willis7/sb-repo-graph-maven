package io.byteshifter.depsgraph.utils

import io.byteshifter.depsgraph.domain.Artifact
import org.apache.maven.model.Model
import spock.lang.Specification
import spock.lang.Subject

/**
 * @author Sion Williams
 */
class ModelToArtifactSpec extends Specification {
    @Subject
    private ModelToArtifact modelToArtifact

    void setup() {
        this.modelToArtifact = new ModelToArtifact()
    }

    void cleanup() {
        this.modelToArtifact = null
    }

    def "Transform throws exception with invalid input class"() {
        when:
        modelToArtifact.transform(new Long(10))

        then:
        thrown Exception
    }

    def "Transform returns null with null input"() {
        expect:
        modelToArtifact.transform(null) == null
    }

    def "Transform returns an Artifact when the input is a valid Model"() {
        given:
        Model input = Mock(Model)
        input.groupId >> 'groupId'
        input.artifactId >> 'artifactId'
        input.version >> '1.0'
        def output

        when:
        output = modelToArtifact.transform(input)

        then:
        output instanceof Artifact
        output.groupId == 'groupId'
        output.artifactId == 'artifactId'
        output.version == '1.0'
    }
}
