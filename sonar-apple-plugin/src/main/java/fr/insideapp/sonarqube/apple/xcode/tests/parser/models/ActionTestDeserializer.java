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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import fr.insideapp.sonarqube.apple.xcode.tests.parser.models.wrap.ActionTest;
import fr.insideapp.sonarqube.apple.xcode.tests.parser.models.wrap.ActionTestCase;
import fr.insideapp.sonarqube.apple.xcode.tests.parser.models.wrap.ActionTestGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class ActionTestDeserializer extends JsonDeserializer<ActionTest> {

    public ActionTestDeserializer() {
        super();
    }

    @Override
    public ActionTest deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectCodec codec = jsonParser.getCodec();
        JsonNode tree = codec.readTree(jsonParser);
        // get the nested values
        JsonNode values = tree.path("_values");
        Iterator<JsonNode> iterator = values.iterator();
        // we keep two separate lists
        ArrayList<ActionTestSummaryGroup> groups = new ArrayList<>();
        ArrayList<ActionTestMetadata> metadata = new ArrayList<>();
        while (iterator.hasNext()) {
            JsonNode value = iterator.next();
            JsonNode typeName = value.path("_type").path("_name");
            ActionTest.Type type = codec.treeToValue(typeName, ActionTest.Type.class);
            switch (type) {
                case GROUP:
                    groups.add(codec.treeToValue(value, ActionTestSummaryGroup.class));
                    break;
                case METADATA:
                    metadata.add(codec.treeToValue(value, ActionTestMetadata.class));
                    break;
            }
        }
        if (!groups.isEmpty()) {
            return new ActionTestGroup(groups);
        } else {
            return new ActionTestCase(metadata);
        }
    }
}
