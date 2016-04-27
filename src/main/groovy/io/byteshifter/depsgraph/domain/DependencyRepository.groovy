package io.byteshifter.depsgraph.domain

import org.springframework.data.repository.CrudRepository

/**
 * @author Sion Williams
 */
interface DependencyRepository extends CrudRepository<Dependency, String> {
    Dependency findByArtifactId(String artifactId)
}