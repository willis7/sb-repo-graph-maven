package com.willis7.utils

import spock.lang.Specification

/**
 * Created by willis7 on 26/02/15.
 */
class POMSlurperTest extends Specification {

    def "DependencyExctractor slurps correct number of dependencies"() {
        given: "a pom with dependencies"
        def pom = new File('repository/org/apache/activemq/activemq-pool/5.9.1/activemq-pool-5.9.1.pom')
        def pomSlurps = new POMSlurper()

        when: "the extractor is run against the pom"
        def result = pomSlurps.dependencyExctractor(pom)

        then: "result shows the correct number of dependencies"
        result.size() == 12
    }
}
