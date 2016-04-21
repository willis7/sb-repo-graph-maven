/**
 * Copyright (C) 2015 Michael Schnell. All rights reserved. <http://www.fuin.org/>
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package io.byteshifter.depsgraph.services

import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.apache.maven.model.Dependency
import org.apache.maven.model.DependencyManagement
import org.apache.maven.model.Model
import org.apache.maven.model.io.xpp3.MavenXpp3Reader
import org.apache.maven.model.merge.ModelMerger
import org.codehaus.plexus.util.xml.pull.XmlPullParserException
import org.fuin.utils4j.Utils4J;
import org.jboss.shrinkwrap.resolver.api.NoResolvedResultException
import org.jboss.shrinkwrap.resolver.api.maven.Maven
import org.jboss.shrinkwrap.resolver.api.maven.MavenFormatStage
import org.jboss.shrinkwrap.resolver.api.maven.MavenStrategyStage

/**
 * Reads a Maven POM.
 */
public final class MavenPomReader {

    private MavenPomReader() {
        throw new UnsupportedOperationException(
                "Creating an instance is not supported for this utility class");
    }

    /**
     * Reads a Maven POM.
     *
     * @param canonicalForm
     *            Maven "group:artifact:version".
     *
     * @return Model created from POM and Super-POM.
     */
    public static Model readModel(final String canonicalForm) {

        final File artifactFile = resolveArtifact(canonicalForm);
        final File pomXmlFile = new File(FilenameUtils.removeExtension(artifactFile.toString()) + ".pom");
        try {
            final Model pre = readModelPom(pomXmlFile);
            final Model model = readModelPre(pomXmlFile, pre);
            final Model resultModel;
            if (model.getParent() == null) {
                resultModel = model;
            } else {
                final String parentGroupId = model.getParent().getGroupId();
                final String parentArtifactId = model.getParent()
                        .getArtifactId();
                final String parentVersion = model.getParent().getVersion();

                final Model parentModel = readModel(parentGroupId + ":" + parentArtifactId + ":pom:" + parentVersion);
                resultModel = merge(parentModel, model);
            }
            validate(resultModel);

            return resultModel;
        } catch (XmlPullParserException ex) {
            throw new RuntimeException("Error parsing POM [" + pomXmlFile + ", Requested Version='" + canonicalForm + "']!", ex);
        } catch (final IOException ex) {
            throw new RuntimeException("Error reading POM [" + pomXmlFile + ", Requested Version='" + canonicalForm + "']!", ex);
        }
    }

    public static Model readModelPom(final File pomXmlFile) throws IOException, XmlPullParserException {
        return readModelPre(pomXmlFile, null);
    }

    private static Model readModelPre(final File pomXmlFile, final Model preModel) throws IOException, XmlPullParserException {

        final Reader reader;
        if (preModel == null) {
            reader = new FileReader(pomXmlFile);
        } else {
            final File varReplacedPomXmlFile = replaceVars(pomXmlFile, preModel);
            reader = new FileReader(varReplacedPomXmlFile);
        }

        final Model model;
        try {
            final MavenXpp3Reader xpp3Reader = new MavenXpp3Reader();
            model = xpp3Reader.read(reader);
        } finally {
            reader.close();
        }
        if (model.getVersion() == null) {
            if (model.getParent() == null) {
                throw new IllegalStateException(
                        "Version of model is null and no parent is avaliable: "
                                + pomXmlFile);
            }
            if (model.getParent().getVersion() == null) {
                throw new IllegalStateException("Version of parent is null: "
                        + pomXmlFile);
            }
            model.setVersion(model.getParent().getVersion());
            if (model.getVersion() == null) {
                throw new IllegalStateException("Parent as also no version: "
                        + pomXmlFile);
            }
        }
        return model;
    }

    private static File replaceVars(final File pomXmlFile, final Model model) {
        final Map<String, Object> vars = new HashMap<String, Object>()
        vars.put("project", model)
        addAllModelProperties(vars, model)
        try {
            final String pomXml = FileUtils.readFileToString(pomXmlFile, "ISO-8859-1")
            final String replacedPomXml = Utils4J.replaceVars(pomXml, vars)
            final File targetFile = new File(Utils4J.getTempDir(), "tmp-pom.xml")
            FileUtils.write(targetFile, replacedPomXml)
            return targetFile;
        } catch (final IOException ex) {
            throw new RuntimeException(ex)
        }
    }

    private static File replaceVars2(final pomXmlFile, final Model model) {
        final def vars = [:]
        vars << [project: model]
        addAllModelProperties(vars, model)
    }

    /**
     * Adds all model properties to the map.
     *
     * @param vars
     *            Variables to add properties to.
     * @param model
     *            Model to add properties from.
     */
    public static void addAllModelProperties(final Map<String, Object> vars,
                                             final Model model) {
        final Enumeration<Object> enu = model.getProperties().keys();
        while (enu.hasMoreElements()) {
            final String key = "" + enu.nextElement();
            vars.put(key, model.getProperties().get(key));
        }
    }

    private static void validate(final Model model) {
        final List<Dependency> dependencies = model.getDependencies();
        for (final Dependency dependency : dependencies) {
            if (dependency.getVersion() == null) {
                final DependencyManagement dm = model.getDependencyManagement();
                final String version = findVersion(dm, dependency);
                if (version == null) {
                    throw new IllegalStateException(
                            "Dependency version not set for '"
                                    + dependency.getGroupId() + ":"
                                    + dependency.getArtifactId() + "' in '"
                                    + model.getGroupId() + ":"
                                    + model.getArtifactId() + ":"
                                    + model.getVersion() + "'");
                }
                dependency.setVersion(Utils4J.replaceVars(version,
                        model.getProperties()));
            }
        }
    }

    private static String findVersion(final DependencyManagement dm,
                                      final Dependency dep) {
        final List<Dependency> dependencies = dm.getDependencies();
        for (final Dependency dependency : dependencies) {
            if ((dependency.getGroupId().equals(dep.getGroupId()))
                    && (dependency.getArtifactId().equals(dep.getArtifactId()))) {
                return dependency.getVersion();
            }
        }
        return null;
    }

    private static Model merge(final Model parent, final Model child) {
        final Model newModel = new Model();
        final ModelMerger merger = new ModelMerger();
        final Map<Object, Object> hints = new HashMap<Object, Object>();
        merger.merge(newModel, parent, true, hints);
        merger.merge(newModel, child, true, hints);
        return newModel;
    }

    private static void loadPom(final String canonicalForm) {
        try {
            final MavenStrategyStage strategyStage = Maven.resolver().resolve(canonicalForm);
            final MavenFormatStage formatStage = strategyStage.withoutTransitivity();
            formatStage.asSingleFile();
        } catch (final NoResolvedResultException ex) {
            // Ignored by intention
            System.currentTimeMillis(); // Satisfy Checkstyle
        }
    }

    private static File resolveArtifact(final String canonicalForm) {
        try {
            if (canonicalForm.contains(":pom:")) {

                // Workaround for POM bug
                // https://issues.jboss.org/browse/SHRINKRES-214

                // Makes sure that the POM file is loaded
                loadPom(canonicalForm);

                // Resolve any JAR file to get the local repository path
                final String jarNameAndPath = resolveArtifact("org.apache.maven:maven-core:jar:3.2.5")
                        .getCanonicalPath();
                final int len = "org/apache/maven/maven-core/3.2.5/maven-core-3.2.5.jar"
                        .length();

                // Create the local POM file path and name
                final String filename = jarNameAndPath.substring(0,
                        jarNameAndPath.length() - len)
                + "/"
                + canonicalToPath(canonicalForm);

                return new File(filename);
            } else {
                final MavenStrategyStage strategyStage = Maven.resolver().resolve(canonicalForm);
                final MavenFormatStage formatStage = strategyStage.withoutTransitivity();
                return formatStage.asSingleFile();
            }
        } catch (final NoResolvedResultException ex) {
            throw new IllegalStateException("Couldn't resolve '" + canonicalForm + "'", ex);
        } catch (final IOException ex) {
            throw new IllegalStateException("Couldn't resolve '" + canonicalForm + "'", ex);
        }
    }

    private static String canonicalToPath(final String canonicalForm) {
        final StringTokenizer tok = new StringTokenizer(canonicalForm, ":'");
        if (tok.countTokens() != 4) {
            throw new IllegalArgumentException("Invalid canonical form: '" + canonicalForm + "'");
        }
        final String groupId = tok.nextToken();
        final String artifactId = tok.nextToken();
        final String type = tok.nextToken();
        final String version = tok.nextToken();
        return groupId.replace('.', '/') + "/" + artifactId + "/" + version + "/" + artifactId + "-" + version + "." + type;
    }

}