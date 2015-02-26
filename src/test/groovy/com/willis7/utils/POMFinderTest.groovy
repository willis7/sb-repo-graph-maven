package com.willis7.utils

import spock.lang.Specification

/**
 * Created by willis7 on 24/02/15.
 */
class POMFinderTest extends Specification {

    def "getAllPOMs method returns correct number of files"() {
        given:
        POMFinder pomFinder = new POMFinder(repoDir: '/src/test/resources/repository/')

        when: "I run the finder method"
        def results = pomFinder.getAllPOMs()

        then: "expect results to equal actual number of files"
        results.size() == 3
    }
}
