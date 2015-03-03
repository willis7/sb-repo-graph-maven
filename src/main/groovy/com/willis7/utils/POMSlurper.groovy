package com.willis7.utils

/**
 * POM Slurper is used for parsing a Maven POM file using {@code @XmlSlurper}
 *
 * @author Sion Williams
 */
class POMSlurper {

    /**
     * Return a list of dependencies slurped from the POM
     *
     * @param pomFile File which represents the Maven POM
     * @return List representing all dependencies found in the pomFile
     */
    def dependencyExctractor(File pomFile) {
        def pom = new XmlSlurper().parse(pomFile)

        pom.dependencies.dependency
    }
}
