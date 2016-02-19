package io.byteshifter.depsgraph.services

import groovy.io.FileType

/**
 * Helper class for finding and returning files which satisfy a matcher
 *
 * @author Sion Williams
 */
class POMFinder {
    // Reference to determine where the code is being run from
    def projectDir = System.getProperty("user.dir")

    /**
     * Return a list of files which end in '.pom'
     *
     * @param repoPath a path relative to System.getProperty("user.dir") which should hold Maven poms
     * @return List of matching files ending in .pom
     * @throws FileNotFoundException
     */
    def getAllPOMs(String repoPath) throws FileNotFoundException {
        def absoluteRepoDir = new File(projectDir, repoPath)
        def pomList = []

        absoluteRepoDir.eachFileRecurse (FileType.FILES) { file ->
            if(isPomFile(file)) {
                pomList << file
            }
        }
        pomList
    }

    /**
     *
     * @param file
     * @return boolean based on whether file is a POM
     */
    private boolean isPomFile(File file) {
        file.name.endsWith('.pom')
    }
}