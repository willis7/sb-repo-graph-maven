package com.willis7.utils

/**
 * Helper class for finding and returning files which satisfy a matcher
 *
 * @author Sion Williams
 */
public class POMFinder {
    // Helper property to determine where the code is being run from
    def projectDir = System.getProperty("user.dir")

    /**
     * Return a list of files which end in '.pom'
     *
     * @param repoPath relative path to System.getProperty("user.dir") which should hold Maven poms
     * @return List of matching files ending in .pom
     */
    def getAllPOMs(String repoPath) {
        def absoluteRepoDir = new File(projectDir, repoPath)
        def pomList = []

        absoluteRepoDir.traverse { File file ->
            if(file.name.endsWith('.pom')) {
                pomList << file
            }
        }
        pomList
    }
}
