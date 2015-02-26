package com.willis7.utils

/**
 * Created by willis7 on 26/02/15.
 */
class POMSlurper {

    /**
     * Return a list of dependencies slurped from the POM
     * @param pom
     */
    def dependencyExctractor(def pomFile) {
        def pom = new XmlSlurper().parse(pomFile)

        pom.dependencies.dependency.each {

        }
    }
}
