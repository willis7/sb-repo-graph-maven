package com.willis7.utils

import spock.lang.Specification

/**
 * Created by willis7 on 24/02/15.
 */
class POMFinderTest extends Specification {

    def "getAllPOMs method returns correct number of files"() {
        given:
        POMFinder pomFinder = new POMFinder()

        when: "I run the finder method"
        def results = pomFinder.getAllPOMs('/src/test/resources/repository/')

        then: "expect results to equal actual number of files"
        results.size() == 3
    }
}
