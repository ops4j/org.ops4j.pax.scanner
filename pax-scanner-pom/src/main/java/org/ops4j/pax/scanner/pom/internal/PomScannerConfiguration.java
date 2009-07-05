/*
 * Copyright 2009 Alin Dreghiciu.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.pax.scanner.pom.internal;

import org.ops4j.pax.scanner.common.ScannerConfiguration;

/**
 * POM scanner specific configuration.
 *
 * @author Alin Dreghiciu (adreghiciu@gmail.com)
 * @since 1.0.0, 05 July, 2009
 */
public interface PomScannerConfiguration
    extends ScannerConfiguration
{

    /**
     * Returns a comma separated list of artifact types to be included in the list of scanned artifacts.
     * Every entry can be an regexp that will be matched against scanned artifact type.
     *
     * @return comma separated list of artifact types
     */
    String[] getDefaultIncludedTypes();

    /**
     * Returns a comma separated list of artifact types to be excluded in the list of scanned artifacts.
     * Every entry can be an regexp that will be matched against scanned artifact type.
     *
     * @return comma separated list of artifact types
     */
    String[] getDefaultExcludedTypes();

}
