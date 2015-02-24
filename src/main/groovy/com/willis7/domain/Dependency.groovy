package com.willis7.domain

import org.neo4j.graphdb.Direction
import org.springframework.data.neo4j.annotation.Fetch
import org.springframework.data.neo4j.annotation.GraphId
import org.springframework.data.neo4j.annotation.NodeEntity
import org.springframework.data.neo4j.annotation.RelatedTo

/**
 * Dependency Node Entity
 * @author Sion Williams
 */
@NodeEntity
public class Dependency {

    @GraphId Long id
    String groupId
    String artifactId
    String version

    @RelatedTo(type = "DEPENDS", direction = Direction.INCOMING)
    public @Fetch Set<Dependency> dependencies

    public void dependsOn(Dependency dependency) {
        if ( !dependencies ) {
            dependencies == new HashSet<Dependency>()
        }
        dependencies.add(dependency)
    }

    @Override
    String toString() {
        String results = groupId + " " + artifactId + " " + version + "\n"
        if (dependencies) {
            dependencies.each {
                results += "\t-" + it.groupId + " " + it.artifactId + " " + it.version + "\n"
            }
        }
        results
    }
}
