package io.byteshifter.depsgraph.domain

import org.springframework.data.repository.CrudRepository

/**
 * @author Sion Williams
 */
public interface ArtifactRepository extends CrudRepository<Artifact, String> {

    Artifact findByArtifactId(String artifactId)

}