package io.byteshifter.depsgraph.domain

import org.springframework.data.neo4j.annotation.Fetch
import org.springframework.data.neo4j.annotation.GraphId
import org.springframework.data.neo4j.annotation.NodeEntity
import org.springframework.data.neo4j.annotation.RelatedTo

import static org.neo4j.graphdb.Direction.OUTGOING

// Example Payload
//{
//    "groupId": "group3",
//    "artifactId": "artifact3",
//    "version": "1.0",
//    "dependencies": ["http://127.0.0.1:8080/dependency/0", "http://127.0.0.1:8080/dependency/1", "http://127.0.0.1:8080/dependency/2"]
//}

/**
 * Dependency Node Entity.
 * @author Sion Williams
 */
@NodeEntity
class Dependency {
    @GraphId
    private Long id

    // Maven Coordinates
    String groupId
    String artifactId
    String version

    @RelatedTo(type = "DEPENDS_ON", direction = OUTGOING)
    @Fetch
    Set<Dependency> dependencies

    @Override
    String toString() {
        return "Dependency{" +
                "id=" + id +
                ", groupId='" + groupId + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", version='" + version + '\'' +
                ", dependencies=" + dependencies +
                '}';
    }
}
