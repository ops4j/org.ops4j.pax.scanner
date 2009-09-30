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
package org.ops4j.pax.scanner.all.internal;

import java.util.ArrayList;
import java.util.Collection;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Composite activator for all embedded scanners and scanner service.
 *
 * @author Alin Dreghiciu (adreghiciu@gmail.com)
 * @since 1.1.0, September 30, 2009
 */
public class CompositeActivator
    implements BundleActivator
{

    /**
     * List of activators that makes up this composite.
     */
    private Collection<BundleActivator> m_activators;

    /**
     * Constructor. Create all activators that makes up this composite.
     */
    public CompositeActivator()
    {
        m_activators = new ArrayList<BundleActivator>();
        m_activators.add( new org.ops4j.pax.scanner.internal.Activator() );
        m_activators.add( new org.ops4j.pax.scanner.bundle.internal.Activator() );
        m_activators.add( new org.ops4j.pax.scanner.composite.internal.Activator() );
        m_activators.add( new org.ops4j.pax.scanner.dir.internal.Activator() );
        m_activators.add( new org.ops4j.pax.scanner.features.internal.Activator() );
        m_activators.add( new org.ops4j.pax.scanner.file.internal.Activator() );
        m_activators.add( new org.ops4j.pax.scanner.obr.internal.Activator() );
        m_activators.add( new org.ops4j.pax.scanner.pom.internal.Activator() );
    }

    /**
     * Start all composed activators.
     *
     * {@inheritDoc}
     */
    public void start( final BundleContext bundleContext )
        throws Exception
    {
        for( BundleActivator activator : m_activators )
        {
            activator.start( bundleContext );
        }
    }

    /**
     * Stop all composed activators.
     *
     * {@inheritDoc}
     */
    public void stop( final BundleContext bundleContext )
        throws Exception
    {
        for( BundleActivator activator : m_activators )
        {
            activator.stop( bundleContext );
        }
    }

}
