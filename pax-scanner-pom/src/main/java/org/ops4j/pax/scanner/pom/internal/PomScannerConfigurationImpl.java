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

import org.ops4j.pax.scanner.common.ScannerConfigurationImpl;
import org.ops4j.pax.scanner.pom.ServiceConstants;
import org.ops4j.util.property.PropertyResolver;

/**
 * Default implementation of {@link PomScannerConfiguration}.
 *
 * @author Alin Dreghiciu (adreghiciu@gmail.com)
 * @since 1.0.0, 05 July, 2009
 */
public class PomScannerConfigurationImpl
    extends ScannerConfigurationImpl
    implements PomScannerConfiguration
{

    /**
     * Default list of included packaging types.
     */
    private static final String[] DEFAULT_INCLUDED_TYPES = new String[]{ ".*" };
    /**
     * Default list of excluded packaging types.
     */
    private static final String[] DEFAULT_EXCLUDED_TYPES = new String[]{ "pom", "libd" };

    /**
     * Creates a new service configuration.
     *
     * @param propertyResolver propertyResolver used to resolve properties; mandatory
     */
    public PomScannerConfigurationImpl( final PropertyResolver propertyResolver )
    {
        super( propertyResolver, ServiceConstants.PID );
    }

    /**
     * {@inheritDoc}
     */
    public String[] getDefaultIncludedTypes()
    {
        final String propertyName = getPid() + ServiceConstants.PROPERTY_DEFAULT_INCLUDED_TYPES;
        if( !contains( propertyName ) )
        {
            final String propertyValue = getPropertyResolver().get( propertyName );
            if( propertyValue == null )
            {
                return set( propertyName, DEFAULT_INCLUDED_TYPES );
            }
            return set( propertyName, propertyValue.split( "," ) );
        }
        return get( propertyName );
    }

    /**
     * {@inheritDoc}
     */
    public String[] getDefaultExcludedTypes()
    {
        final String propertyName = getPid() + ServiceConstants.PROPERTY_DEFAULT_EXCLUDED_TYPES;
        if( !contains( propertyName ) )
        {
            final String propertyValue = getPropertyResolver().get( propertyName );
            if( propertyValue == null )
            {
                return set( propertyName, DEFAULT_EXCLUDED_TYPES );
            }
            return set( propertyName, propertyValue.split( "," ) );
        }
        return get( propertyName );
    }

}
