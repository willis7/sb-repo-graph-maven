package com.willis7.domain

import org.springframework.data.repository.CrudRepository

/**
 * @author Sion Williams
 */
public interface DependencyRepository extends CrudRepository<Artifact, String> {

    Artifact findByArtifactId(String artifactId)

}