package io.byteshifter.depsgraph.domain

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource

/**
 * @author Sion Williams
 */
@RepositoryRestResource(collectionResourceRel = "dependency", path = "dependency")
interface DependencyRepository extends PagingAndSortingRepository<Dependency, Long> {
    List<Dependency> findByArtifactId(@Param("artifactId") String artifactId)
}