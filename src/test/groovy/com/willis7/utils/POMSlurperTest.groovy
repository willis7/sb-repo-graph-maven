package com.willis7.utils

import spock.lang.Specification

/**
 * @author Sion Williams
 */
class POMSlurperTest extends Specification {
    def resource
    File pomFile

    def setup() {
        resource = getClass().getClassLoader().getResource('repository/org/apache/activemq/activemq-pool/5.9.1/activemq-pool-5.9.1.pom').toURI()
        pomFile = new File(resource)
    }

    def "getDependencies() slurps correct number of dependencies"() {
        when: "getDependencies() is run against the pom"
        def result = POMSlurper.getDependencies(pomFile)

        then: "result shows the correct number of dependencies"
        result.size() == 12
    }

    def "getParent() returns the correct parent attributes"() {
        when: "getParent() is run against the pom"
        def result = POMSlurper.getParent(pomFile)

        then: "the result has the correct attributes"
        result.size() == 3
        result['groupId'] == 'org.apache.activemq'
        result['artifactId'] == 'activemq-parent'
        result['version'] == '5.9.1'
    }
}
