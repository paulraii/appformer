/*
 * 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.uberfire.java.nio.fs.jgit;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jgit.diff.DiffEntry;
import org.junit.Test;
import org.uberfire.java.nio.fs.jgit.util.Git;
import org.uberfire.java.nio.fs.jgit.util.GitImpl;
import org.uberfire.java.nio.fs.jgit.util.commands.Commit;
import org.uberfire.java.nio.fs.jgit.util.commands.CreateBranch;
import org.uberfire.java.nio.fs.jgit.util.commands.CreateRepository;
import org.uberfire.java.nio.fs.jgit.util.commands.GetTreeFromRef;
import org.uberfire.java.nio.fs.jgit.util.commands.ListDiffs;
import org.uberfire.java.nio.fs.jgit.util.commands.Merge;
import org.uberfire.java.nio.fs.jgit.util.exceptions.GitException;

import static org.assertj.core.api.Assertions.assertThat;

public class JGitMergeTest extends AbstractTestInfra {

    private static final String SOURCE_GIT = "source/source";

    @Test
    public void testMergeSuccessful() throws IOException {
        final File parentFolder = createTempDirectory();

        final File gitSource = new File(parentFolder,
                                        SOURCE_GIT + ".git");
        final Git origin = new CreateRepository(gitSource).execute().get();

        new Commit(origin,
                   "master",
                   "name",
                   "name@example.com",
                   "master-1",
                   null,
                   null,
                   false,
                   new HashMap<String, File>() {{
                       put("file1.txt",
                           tempFile("temp1"));
                   }}).execute();

        new CreateBranch((GitImpl) origin,
                         "master",
                         "develop").execute();

        new Commit(origin,
                   "develop",
                   "name",
                   "name@example.com",
                   "develop-1",
                   null,
                   null,
                   false,
                   new HashMap<String, File>() {{
                       put("file2.txt",
                           tempFile("temp2"));
                   }}).execute();

        new Commit(origin,
                   "develop",
                   "name",
                   "name@example.com",
                   "develop-2",
                   null,
                   null,
                   false,
                   new HashMap<String, File>() {{
                       put("file3.txt",
                           tempFile("temp3"));
                   }}).execute();

        new Commit(origin,
                   "develop",
                   "name",
                   "name@example.com",
                   "develop-3",
                   null,
                   null,
                   false,
                   new HashMap<String, File>() {{
                       put("file4.txt",
                           tempFile("temp4"));
                   }}).execute();

        new Commit(origin,
                   "develop",
                   "name",
                   "name@example.com",
                   "develop-4",
                   null,
                   null,
                   false,
                   new HashMap<String, File>() {{
                       put("file5.txt",
                           tempFile("temp5"));
                   }}).execute();

        new Merge(origin,
                  "develop",
                  "master").execute();

        final List<DiffEntry> result = new ListDiffs(origin,
                                                     new GetTreeFromRef(origin,
                                                                        "master").execute(),
                                                     new GetTreeFromRef(origin,
                                                                        "develop").execute()).execute();

        assertThat(result).isEmpty();
    }

    @Test
    public void testMergeConflict() throws IOException {
        final File parentFolder = createTempDirectory();

        final File gitSource = new File(parentFolder,
                                        SOURCE_GIT + ".git");
        final Git origin = new CreateRepository(gitSource).execute().get();

        new Commit(origin,
                   "master",
                   "name",
                   "name@example.com",
                   "master-1",
                   null,
                   null,
                   false,
                   new HashMap<String, File>() {{
                       put("file1.txt",
                           tempFile("temp1"));
                   }}).execute();

        new CreateBranch((GitImpl) origin,
                         "master",
                         "develop").execute();

        new Commit(origin,
                   "develop",
                   "name",
                   "name@example.com",
                   "develop-1",
                   null,
                   null,
                   false,
                   new HashMap<String, File>() {{
                       put("file1.txt",
                           tempFile("temp1"));
                   }}).execute();

        new Merge(origin,
                  "develop",
                  "master").execute();

        final List<DiffEntry> result = new ListDiffs(origin,
                                                     new GetTreeFromRef(origin,
                                                                        "master").execute(),
                                                     new GetTreeFromRef(origin,
                                                                        "develop").execute()).execute();

        assertThat(result).isEmpty();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParametersNotNull() {

        new Merge(null,
                  "develop",
                  "master").execute();
    }

    @Test(expected = GitException.class)
    public void testTryToMergeNonexistentBranch() throws IOException {
        final File parentFolder = createTempDirectory();

        final File gitSource = new File(parentFolder,
                                        SOURCE_GIT + ".git");
        final Git origin = new CreateRepository(gitSource).execute().get();

        new Commit(origin,
                   "master",
                   "name",
                   "name@example.com",
                   "master-1",
                   null,
                   null,
                   false,
                   new HashMap<String, File>() {{
                       put("file1.txt",
                           tempFile("temp1"));
                   }}).execute();

        new CreateBranch((GitImpl) origin,
                         "master",
                         "develop").execute();

        new Commit(origin,
                   "develop",
                   "name",
                   "name@example.com",
                   "develop-1",
                   null,
                   null,
                   false,
                   new HashMap<String, File>() {{
                       put("file2.txt",
                           tempFile("temp2"));
                   }}).execute();

        new Commit(origin,
                   "develop",
                   "name",
                   "name@example.com",
                   "develop-2",
                   null,
                   null,
                   false,
                   new HashMap<String, File>() {{
                       put("file3.txt",
                           tempFile("temp3"));
                   }}).execute();

        new Commit(origin,
                   "develop",
                   "name",
                   "name@example.com",
                   "develop-3",
                   null,
                   null,
                   false,
                   new HashMap<String, File>() {{
                       put("file4.txt",
                           tempFile("temp4"));
                   }}).execute();

        new Commit(origin,
                   "develop",
                   "name",
                   "name@example.com",
                   "develop-4",
                   null,
                   null,
                   false,
                   new HashMap<String, File>() {{
                       put("file5.txt",
                           tempFile("temp5"));
                   }}).execute();

        new Merge(origin,
                  "develop",
                  "nonexistent").execute();
    }

    @Test(expected = GitException.class)
    public void testMergeBinaryInformationButHasConflicts() throws IOException {

        final byte[] contentA = this.loadImage("images/drools.png");
        final byte[] contentB = this.loadImage("images/jbpm.png");
        final byte[] contentC = this.loadImage("images/opta.png");

        final File parentFolder = createTempDirectory();

        final File gitSource = new File(parentFolder,
                                        SOURCE_GIT + ".git");
        final Git origin = new CreateRepository(gitSource).execute().get();

        new Commit(origin,
                   "master",
                   "name",
                   "name@example.com",
                   "master-1",
                   null,
                   null,
                   false,
                   new HashMap<String, File>() {{
                       put("file1.jpg",
                           tempFile(contentA));
                   }}).execute();

        new CreateBranch((GitImpl) origin,
                         "master",
                         "develop").execute();

        new Commit(origin,
                   "develop",
                   "name",
                   "name@example.com",
                   "develop-1",
                   null,
                   null,
                   false,
                   new HashMap<String, File>() {{
                       put("file1.jpg",
                           tempFile(contentB));
                   }}).execute();

        new Commit(origin,
                   "master",
                   "name",
                   "name@example.com",
                   "master-1",
                   null,
                   null,
                   false,
                   new HashMap<String, File>() {{
                       put("file1.jpg",
                           tempFile(contentC));
                   }}).execute();

        new Merge(origin,
                  "develop",
                  "master").execute();

        final List<DiffEntry> result = new ListDiffs(origin,
                                                     new GetTreeFromRef(origin,
                                                                        "master").execute(),
                                                     new GetTreeFromRef(origin,
                                                                        "develop").execute()).execute();

        assertThat(result).isEmpty();
    }

    @Test
    public void testMergeBinaryInformationSuccessful() throws IOException {

        final byte[] contentA = this.loadImage("images/drools.png");
        final byte[] contentB = this.loadImage("images/jbpm.png");

        final File parentFolder = createTempDirectory();

        final File gitSource = new File(parentFolder,
                                        SOURCE_GIT + ".git");
        final Git origin = new CreateRepository(gitSource).execute().get();

        new Commit(origin,
                   "master",
                   "name",
                   "name@example.com",
                   "master-1",
                   null,
                   null,
                   false,
                   new HashMap<String, File>() {{
                       put("file1.jpg",
                           tempFile(contentA));
                   }}).execute();

        new CreateBranch((GitImpl) origin,
                         "master",
                         "develop").execute();

        new Commit(origin,
                   "develop",
                   "name",
                   "name@example.com",
                   "develop-1",
                   null,
                   null,
                   false,
                   new HashMap<String, File>() {{
                       put("file1.jpg",
                           tempFile(contentB));
                   }}).execute();

        new Merge(origin,
                  "develop",
                  "master").execute();

        final List<DiffEntry> result = new ListDiffs(origin,
                                                     new GetTreeFromRef(origin,
                                                                        "master").execute(),
                                                     new GetTreeFromRef(origin,
                                                                        "develop").execute()).execute();

        assertThat(result).isEmpty();
    }
}
