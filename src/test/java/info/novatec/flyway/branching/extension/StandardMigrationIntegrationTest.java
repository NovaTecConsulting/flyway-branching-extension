/*
* Flyway Branching Extension.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package info.novatec.flyway.branching.extension;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfoService;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;

/**
 * Integration test to verify that the standard
 * flywaydb migrations are still working.
 */
public class StandardMigrationIntegrationTest {
    private static final int EXPECTED_MIGRATIONS = 3;
    private static final long SLEEP_TIME = 1000L;

    private String databasePath;

    /**
     * Verifies that the standard migration is still working.
     */
    @Test
    public final void verifyStandardMigration() {
        // Create the Flyway instance
        Flyway cut = new Flyway();

        // Point it to the database
        databasePath = "dbmigration" + System.nanoTime();
        cut.setDataSource("jdbc:h2:file:./target/" + databasePath, "sa", null);

        cut.setValidateOnMigrate(true);
        cut.setInitOnMigrate(true);
        cut.setInitVersion("0");

        // Start the migration
        int migrations = cut.migrate();

        assertThat("", migrations, is(2));

        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException e) {
            // Not expected
        }

        MigrationInfoService migrationInfoService = cut.info();
        assertThat("", migrationInfoService.pending().length, is(0));
    }

    /**
     * Cleaning up the test database.
     * @throws InterruptedException if thread is interrupted
     */
    @After
    public final void cleanup() throws InterruptedException {
        Thread.sleep(SLEEP_TIME);
        File file = new File("./target/" + databasePath);
        if (file.exists()) {
            FileUtils.deleteQuietly(file);
        }
    }

}
