package io.byteshifter.depsgraph.utils

import io.byteshifter.depsgraph.domain.Dependency
import org.apache.commons.collections4.Transformer
import org.apache.maven.model.Model

/**
 * Transformer class for mapping a Maven Model to an Dependency
 * @author Sion Williams
 */
class ModelToArtifact implements Transformer<Model,Dependency> {

    @Override
    Dependency transform(Model input) {
        if(input){
            return new Dependency(groupId: input.groupId,
                    artifactId: input.artifactId,
                    version: input.version)
        } else {
            return null
        }

    }
}
