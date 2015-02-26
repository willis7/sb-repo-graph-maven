package com.willis7.utils

/**
 * @author Sion Williams
 */
public class POMFinder {
    def repoDir
    def projectDir = System.getProperty("user.dir")

    /**
     * getAllPOMs
     *
     * @return pomList a list of matching files ending in .pom
     */
    def getAllPOMs() {
        def absoluteRepoDir = new File(projectDir, repoDir)
        def pomList = []

        absoluteRepoDir.traverse { File file ->
            if(file.name.endsWith('.pom')) {
                pomList << file
            }
        }
        pomList
    }
}
