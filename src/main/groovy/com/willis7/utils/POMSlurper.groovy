package com.willis7.utils

/**
 * POM Slurper is used for parsing a Maven POM file using {@code @XmlSlurper}
 *
 * @author Sion Williams
 */
class POMSlurper {

    /**
     * Populates and returns a map of parent attributes
     *
     * @param pomFile File which represents the Maven POM
     * @return a map of the parent attributes
     */
    static def getParent(File pomFile) {
        def pom = new XmlSlurper().parse(pomFile)
        def parentAttr = [:]

        if (pom.parent.groupId) {
            parentAttr << ['groupId': pom.parent.groupId]
        }
        if (pom.parent.artifactId) {
            parentAttr << ['artifactId': pom.parent.artifactId]
        }
        if (pom.parent.version) {
            parentAttr << ['version': pom.parent.version]
        }

        parentAttr
    }

    /**
     * Return a list of dependencies slurped from the POM
     *
     * @param pomFile File which represents the Maven POM
     * @return List representing all dependencies found in the pomFile
     */
    static def getDependencies(File pomFile) {
        def pom = new XmlSlurper().parse(pomFile)

        pom.dependencies.dependency
    }
}
