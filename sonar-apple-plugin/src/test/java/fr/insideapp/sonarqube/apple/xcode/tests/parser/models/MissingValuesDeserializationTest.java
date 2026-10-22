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
package fr.insideapp.sonarqube.apple.xcode.tests.parser.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public final class MissingValuesDeserializationTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void valuesDeserializer_missingValues_isEmpty() throws IOException {
        ActionTestPlanRunSummaries result = mapper.readValue("{\"summaries\":{}}", ActionTestPlanRunSummaries.class);
        assertThat(result.summaries).isEmpty();
    }

    @Test
    public void valuesDeserializer_emptyValues_isEmpty() throws IOException {
        ActionTestPlanRunSummaries result = mapper.readValue("{\"summaries\":{\"_values\":[]}}", ActionTestPlanRunSummaries.class);
        assertThat(result.summaries).isEmpty();
    }

    @Test
    public void actionTestDeserializer_missingValues_doesNotThrow() throws IOException {
        ActionTestSummaryGroup result = mapper.readValue("{\"subtests\":{}}", ActionTestSummaryGroup.class);
        assertThat(result).isNotNull();
    }
}
