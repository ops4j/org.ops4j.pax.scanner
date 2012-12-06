/*
 * Copyright 2007 Alin Dreghiciu.
 *
 * Licensed  under the  Apache License,  Version 2.0  (the "License");
 * you may not use  this file  except in  compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under the  License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS  OF ANY KIND, either  express  or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.pax.scanner.file.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ops4j.lang.NullArgumentException;
import org.ops4j.net.URLUtils;
import org.ops4j.pax.scanner.MalformedSpecificationException;
import org.ops4j.pax.scanner.ProvisionSpec;
import org.ops4j.pax.scanner.ScannedBundle;
import org.ops4j.pax.scanner.Scanner;
import org.ops4j.pax.scanner.ScannerException;
import org.ops4j.pax.scanner.common.ScannedFileBundle;
import org.ops4j.pax.scanner.common.ScannerConfiguration;
import org.ops4j.pax.scanner.common.ScannerConfigurationImpl;
import org.ops4j.pax.scanner.common.SystemPropertyUtils;
import org.ops4j.pax.scanner.file.ServiceConstants;
import org.ops4j.util.property.PropertyResolver;

/**
 * A scanner that scans plain text file.
 *
 * @author Alin Dreghiciu
 * @since August 15, 2007
 */
public class FileScanner
    implements Scanner
{

    /**
     * Logger.
     */
    private static final Log LOGGER = LogFactory.getLog( FileScanner.class );
    /**
     * The starting character for a comment line.
     */
    private static final String COMMENT_SIGN = "#";
    /**
     * Prefix for properties.
     */
    private static final String PROPERTY_PREFIX = "-D";
    /**
     * Regex pattern used to print property key/value.
     */
    private static final Pattern PROPERTY_PATTERN = Pattern.compile( "-D(.*)=(.*)" );
    /**
     * Regex pattern used to quote property value.
     */
    private static final Pattern QUOTED_PROPERTY_VALUE_PATTERN = Pattern.compile("\"[^\"\\\\]*(?:\\\\.[^\"\\\\]*)*\"");
    /**
     * PropertyResolver used to resolve properties.
     */
    private PropertyResolver m_propertyResolver;

    /**
     * Creates a new file scanner.
     *
     * @param propertyResolver a propertyResolver; mandatory
     */
    public FileScanner( final PropertyResolver propertyResolver )
    {
        NullArgumentException.validateNotNull( propertyResolver, "PropertyResolver" );
        m_propertyResolver = propertyResolver;
    }

    /**
     * Reads the bundles from the file specified by the urlSpec.
     * {@inheritDoc}
     */
    public List<ScannedBundle> scan( final ProvisionSpec provisionSpec )
        throws MalformedSpecificationException, ScannerException
    {
        NullArgumentException.validateNotNull( provisionSpec, "Provision spec" );

        LOGGER.debug( "Scanning [" + provisionSpec.getPath() + "]" );
        List<ScannedBundle> scannedBundles = new ArrayList<ScannedBundle>();
        ScannerConfiguration config = createConfiguration();
        BufferedReader bufferedReader = null;
        try
        {
            try
            {
                bufferedReader = new BufferedReader(
                    new InputStreamReader(
                        URLUtils.prepareInputStream(
                            provisionSpec.getPathAsUrl(),
                            !config.getCertificateCheck()
                        )
                    )
                );
                Integer defaultStartLevel = getDefaultStartLevel( provisionSpec, config );
                Boolean defaultStart = getDefaultStart( provisionSpec, config );
                Boolean defaultUpdate = getDefaultUpdate( provisionSpec, config );
                String line;
                while( ( line = bufferedReader.readLine() ) != null )
                {
                    String trimmedLine = line.trim();
                    if( trimmedLine.length() > 0 && !trimmedLine.startsWith( COMMENT_SIGN ) )
                    {
                        if( trimmedLine.startsWith( PROPERTY_PREFIX ) )
                        {
                            Matcher matcher = PROPERTY_PATTERN.matcher( trimmedLine );
                            if( !matcher.matches() || matcher.groupCount() != 2 )
                            {
                                throw new ScannerException( "Invalid property: " + line );
                            }
                            String key = matcher.group( 1 );
                            String value = matcher.group( 2 );
                            StringBuffer stringBuffer = new StringBuffer( value.length() );
                            for ( matcher = QUOTED_PROPERTY_VALUE_PATTERN.matcher( value ); 
                            		matcher.find(); matcher.appendReplacement( stringBuffer, value ) ) {
                            	String group = matcher.group();
                            	value = group.substring( 1, group.length() - 1 ).replace( "\\\"", "\"" ).replace( "$", "\\$" );   
                            }
                            int index = stringBuffer.length();
                            matcher.appendTail(stringBuffer);
                            if ( index < stringBuffer.length()  && !( stringBuffer.indexOf( " ", index ) < 0 && stringBuffer.indexOf( "\"", index ) < 0 ) ) {
                            	throw new ScannerException( "Invalid property: " + line );
                            }
                            value = SystemPropertyUtils.resolvePlaceholders( stringBuffer.toString() );
                            System.setProperty( key, value );
                        }
                        else
                        {
                            line = SystemPropertyUtils.resolvePlaceholders( line );
                            final ScannedFileBundle scannedFileBundle = new ScannedFileBundle(
                                line, defaultStartLevel, defaultStart, defaultUpdate
                            );
                            scannedBundles.add( scannedFileBundle );
                            LOGGER.debug( "Installing bundle [" + scannedFileBundle + "]" );
                        }
                    }
                }
            }
            finally
            {
                if( bufferedReader != null )
                {
                    bufferedReader.close();
                }
            }
        }
        catch( IOException e )
        {
            throw new ScannerException( "Could not parse the provision file", e );
        }
        return scannedBundles;
    }

    /**
     * Returns the default start level by first looking at the parser and if not set fallback to configuration.
     *
     * @param provisionSpec provision spec
     * @param config        a configuration
     *
     * @return default start level or null if nos set.
     */
    private Integer getDefaultStartLevel( ProvisionSpec provisionSpec, ScannerConfiguration config )
    {
        Integer startLevel = provisionSpec.getStartLevel();
        if( startLevel == null )
        {
            startLevel = config.getStartLevel();
        }
        return startLevel;
    }

    /**
     * Returns the default start by first looking at the parser and if not set fallback to configuration.
     *
     * @param provisionSpec provision spec
     * @param config        a configuration
     *
     * @return default start level or null if nos set.
     */
    private Boolean getDefaultStart( final ProvisionSpec provisionSpec, final ScannerConfiguration config )
    {
        Boolean start = provisionSpec.shouldStart();
        if( start == null )
        {
            start = config.shouldStart();
        }
        return start;
    }

    /**
     * Returns the default update by first looking at the parser and if not set fallback to configuration.
     *
     * @param provisionSpec provision spec
     * @param config        a configuration
     *
     * @return default update or null if nos set.
     */
    private Boolean getDefaultUpdate( final ProvisionSpec provisionSpec, final ScannerConfiguration config )
    {
        Boolean update = provisionSpec.shouldUpdate();
        if( update == null )
        {
            update = config.shouldUpdate();
        }
        return update;
    }

    /**
     * Sets the propertyResolver to use.
     *
     * @param propertyResolver a propertyResolver
     */
    public void setResolver( final PropertyResolver propertyResolver )
    {
        NullArgumentException.validateNotNull( propertyResolver, "PropertyResolver" );
        m_propertyResolver = propertyResolver;
    }

    /**
     * Creates a new configuration.
     *
     * @return a configuration
     */
    ScannerConfiguration createConfiguration()
    {
        return new ScannerConfigurationImpl( m_propertyResolver, ServiceConstants.PID );
    }

}
