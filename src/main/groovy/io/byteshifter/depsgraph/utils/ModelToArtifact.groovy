package io.byteshifter.depsgraph.utils

import io.byteshifter.depsgraph.domain.Artifact
import org.apache.commons.collections4.Transformer
import org.apache.maven.model.Model

/**
 * Transformer class for mapping a Maven Model to an Artifact
 * @author Sion Williams
 */
class ModelToArtifact implements Transformer<Model,Artifact> {

    @Override
    Artifact transform(Model input) {
        if(input){
            return new Artifact(groupId: input.groupId,
                    artifactId: input.artifactId,
                    version: input.version)
        } else {
            return null
        }

    }
}
