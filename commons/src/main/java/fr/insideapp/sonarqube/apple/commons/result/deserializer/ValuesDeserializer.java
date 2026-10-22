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
package fr.insideapp.sonarqube.apple.commons.result.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class ValuesDeserializer extends JsonDeserializer<ArrayList<?>> implements ContextualDeserializer {

    private Class<?> clazz;

    public ValuesDeserializer(Class<?> clazz) {
        this.clazz = clazz;
    }

    public ValuesDeserializer() { super(); }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext deserializationContext, BeanProperty beanProperty) throws JsonMappingException {
        JavaType type = beanProperty.getType();
        JavaType valueType = type.containedType(0);
        return new ValuesDeserializer(valueType.getRawClass());
    }

    @Override
    @SuppressWarnings("unchecked")
    public ArrayList<?> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectCodec codec = jsonParser.getCodec();
        JsonNode tree = codec.readTree(jsonParser);
        // get the nested values
        JsonNode values = tree.path("_values");
        Iterator<JsonNode> iterator = values.iterator();
        ArrayList records = new ArrayList<>();
        while (iterator.hasNext()) {
            JsonNode value = iterator.next();
            records.add(codec.treeToValue(value, clazz));
        }
        return records;
    }
}
