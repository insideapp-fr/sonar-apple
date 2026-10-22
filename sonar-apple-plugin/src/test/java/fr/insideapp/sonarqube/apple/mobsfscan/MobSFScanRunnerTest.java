/*
 * SonarQube Apple Plugin - Enables analysis of Swift and Objective-C projects into SonarQube.
 * Copyright © 2022 inside|app (contact@insideapp.fr)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.insideapp.sonarqube.apple.mobsfscan;

import fr.insideapp.sonarqube.apple.commons.SonarProjectConfiguration;
import fr.insideapp.sonarqube.apple.mobsfscan.runner.MobSFScanRunner;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class MobSFScanRunnerTest {

    private MobSFScanRunner runner;

    private SonarProjectConfiguration sonarProjectConfiguration;
    private Class<? extends MobSFScanRunner> clazz;

    @Before
    public void prepare() {
        sonarProjectConfiguration = mock(SonarProjectConfiguration.class);
        runner = new MobSFScanRunner(sonarProjectConfiguration);
        clazz = runner.getClass();
    }

    @Test
    public void one_source() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method options = clazz.getDeclaredMethod("arguments");
        options.setAccessible(true);
        mockSources(List.of("source1"));
        String[] optionsBuilt = (String[]) options.invoke(runner);
        assertThat(optionsBuilt).isEqualTo(new String[]{
                "--json",
                "source1"
        });
    }

    @Test
    public void many_sources() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method options = clazz.getDeclaredMethod("arguments");
        options.setAccessible(true);
        mockSources(List.of("source1", "source2"));
        String[] optionsBuilt = (String[]) options.invoke(runner);
        assertThat(optionsBuilt).isEqualTo(new String[]{
                "--json",
                "source1", "source2"
        });
    }

    @Test
    public void exitCodes_acceptFindings() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method exitCodes = clazz.getDeclaredMethod("exitCodes");
        exitCodes.setAccessible(true);
        assertThat(exitCodes.invoke(runner)).isEqualTo(new Integer[]{0, 1});
    }

    // Private

    private void mockSources(List<String> values) {
        when(sonarProjectConfiguration.sources()).thenReturn(values);
    }

}
