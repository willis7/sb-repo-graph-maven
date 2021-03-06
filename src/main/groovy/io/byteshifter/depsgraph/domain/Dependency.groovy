package io.byteshifter.depsgraph.domain

import org.neo4j.graphdb.Direction
import org.springframework.data.neo4j.annotation.Fetch
import org.springframework.data.neo4j.annotation.GraphId
import org.springframework.data.neo4j.annotation.NodeEntity
import org.springframework.data.neo4j.annotation.RelatedTo

/**
 * Dependency Node Entity.
 * The Dependency class is annotated {@code @NodeEntity}. When Neo4j stores it, it results in the creation of a new node.
 * @author Sion Williams
 */
@NodeEntity
public class Dependency {
    // Coordinates
    @GraphId Long id
    String groupId
    String artifactId
    String version

    @Fetch
    @RelatedTo(type = "DEPENDS_ON", direction = Direction.OUTGOING)
    public Set<Dependency> dependencies

    /**
     * Used to link dependencies together with Direction.OUTGOING
     *
     * @param dependency Another dependency entity
     */
    public void dependsOn(Dependency dependency) {
        if ( !dependencies ) {
            dependencies = new HashSet<Dependency>()
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
