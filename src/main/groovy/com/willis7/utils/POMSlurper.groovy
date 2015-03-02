package com.willis7.utils

/**
 * POM Slurper
 * Used for parsing a Maven POM file
 *
 * @author Sion Williams
 */
class POMSlurper {

    /**
     * Return a list of dependencies slurped from the POM
     * @param File pomFile
     * @return List dependencies
     */
    def dependencyExctractor(File pomFile) {
        def pom = new XmlSlurper().parse(pomFile)

        pom.dependencies.dependency
    }
}
