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

package org.vividus.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WrapsDriver;

public class VividusWebDriver implements WrapsDriver
{
    private WebDriver webDriver;
    private boolean remote;

    public void setWebDriver(WebDriver webDriver)
    {
        this.webDriver = webDriver;
    }

    public boolean isRemote()
    {
        return remote;
    }

    public void setRemote(boolean remote)
    {
        this.remote = remote;
    }

    @SuppressWarnings("checkstyle:SimpleAccessorNameNotation")
    @Override
    public WebDriver getWrappedDriver()
    {
        return webDriver;
    }
}
