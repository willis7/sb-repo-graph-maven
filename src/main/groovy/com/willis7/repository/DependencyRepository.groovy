package com.willis7.repository

import com.willis7.domain.Dependency
import org.springframework.data.repository.CrudRepository

/**
 * @author Sion Williams
 */
public interface DependencyRepository extends CrudRepository<Dependency, String> {

    Dependency findByArtifactId(String artifactId)

    Iterable<Dependency> findByDependenciesArtifactId(String artifactId)
}