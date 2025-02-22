/*
 * Copyright 2019-2023 the original author or authors.
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

package org.vividus.steps;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import org.jbehave.core.embedder.StoryControls;
import org.jbehave.core.expressions.ExpressionResolver;
import org.vividus.variable.Variables;

public class PlaceholderResolver
{
    private static final int MAX_DEPTH = 16;

    private final VariableResolver variableResolver;
    private final ExpressionResolver expressionResolver;
    private final StoryControls storyControls;

    public PlaceholderResolver(VariableResolver variableResolver, ExpressionResolver expressionResolver,
            StoryControls storyControls)
    {
        this.variableResolver = variableResolver;
        this.expressionResolver = expressionResolver;
        this.storyControls = storyControls;
    }

    public Object resolvePlaceholders(String value, Type type)
    {
        return resolvePlaceholders(value, value, type, 1);
    }

    private Object resolvePlaceholders(String originalValue, String value, Type type, int iteration)
    {
        Object adaptedValue = variableResolver.resolve(value);
        if (Variables.VIVIDUS_NULL_EXPRESSION.equals(adaptedValue))
        {
            return null;
        }
        if (type == String.class || adaptedValue instanceof String)
        {
            if (adaptedValue instanceof byte[])
            {
                adaptedValue = new String((byte[]) adaptedValue, StandardCharsets.UTF_8);
            }
            adaptedValue = processExpressions(String.valueOf(adaptedValue));
            if (!value.equals(adaptedValue) && adaptedValue instanceof String)
            {
                if (iteration == MAX_DEPTH)
                {
                    // If we reached max depth, then we see circular reference or variables have more than MAX_DEPTH
                    // nested levels, we need to fallback to one cycle of variable resolution and exit early
                    return variableResolver.resolve(originalValue);
                }
                return resolvePlaceholders(originalValue, (String) adaptedValue, type, iteration + 1);
            }
        }
        return adaptedValue;
    }

    public Object processExpressions(String valueToConvert)
    {
        return expressionResolver.resolveExpressions(storyControls.dryRun(), valueToConvert);
    }
}
