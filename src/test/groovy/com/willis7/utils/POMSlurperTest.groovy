package com.willis7.utils

import spock.lang.Specification

/**
 * @author Sion Williams
 */
class POMSlurperTest extends Specification {
    def pomSlurps

    def setup() {
        pomSlurps = new POMSlurper()
    }

    def "DependencyExctractor slurps correct number of dependencies"() {
        given: "a pom with dependencies"
        def resource = getClass().getClassLoader().getResource('repository/org/apache/activemq/activemq-pool/5.9.1/activemq-pool-5.9.1.pom').toURI()
        File pomFile = new File(resource)

        when: "the extractor is run against the pom"
        def result = pomSlurps.dependencyExctractor(pomFile)

        then: "result shows the correct number of dependencies"
        result.size() == 12
    }
}
