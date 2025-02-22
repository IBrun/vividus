/*
 * Copyright 2019-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.vividus.transformer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jbehave.core.configuration.Keywords;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.model.ExamplesTable.TableProperties;
import org.jbehave.core.steps.ParameterConverters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CartesianProductTableTransformerTests
{
    private static final ExamplesTable FIRST_TABLE = new ExamplesTable(
            "|name   |planet  |\n"
          + "|Junit  |Jupiter |\n"
          + "|Freddie|Mercury |\n"
    );

    private static final ExamplesTable SECOND_TABLE = new ExamplesTable(
            "|col1 |col2 |\n"
                    + "|row11|row12|\n"
                    + "|row21|row22|\n"
                    + "|row31|row32|"
    );

    @Mock private TableProperties properties;
    @Spy private CartesianProductTableTransformer transformer;

    @Test
    void shouldTransform()
    {
        ExamplesTable thirdTable = new ExamplesTable(
                "|number|\n"
              + "|911   |"
        );

        doReturn(List.of(FIRST_TABLE, SECOND_TABLE, thirdTable)).when(transformer).loadTables(StringUtils.EMPTY,
                properties);

        String tableAsString = transformer.transform(StringUtils.EMPTY, null, properties);
        String expectedTable = "|name|planet|col1|col2|number|\n"
                             + "|Junit|Jupiter|row11|row12|911|\n"
                             + "|Junit|Jupiter|row21|row22|911|\n"
                             + "|Junit|Jupiter|row31|row32|911|\n"
                             + "|Freddie|Mercury|row11|row12|911|\n"
                             + "|Freddie|Mercury|row21|row22|911|\n"
                             + "|Freddie|Mercury|row31|row32|911|\n";
        assertEquals(expectedTable, tableAsString);
    }

    @Test
    void shouldTransformToEmptyTableIfOneTableEmpty()
    {
        var tableProperties = new TableProperties(StringUtils.EMPTY, new Keywords(), new ParameterConverters());
        ExamplesTable thirdTable = new ExamplesTable("|number|");

        doReturn(List.of(FIRST_TABLE, SECOND_TABLE, thirdTable)).when(transformer).loadTables(StringUtils.EMPTY,
                tableProperties);

        String tableAsString = transformer.transform(StringUtils.EMPTY, null, tableProperties);
        String expectedTable = "|name|planet|col1|col2|number|";
        assertEquals(expectedTable, tableAsString);
    }

    @Test
    void shouldFailIfTablesContainEqualHeaders()
    {
        doReturn(List.of(FIRST_TABLE, FIRST_TABLE)).when(transformer).loadTables(StringUtils.EMPTY, properties);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> transformer.transform(StringUtils.EMPTY, null, properties));
        assertEquals("Tables must contain different keys, but found the same keys: [planet, name]",
                thrown.getMessage());
    }
}
