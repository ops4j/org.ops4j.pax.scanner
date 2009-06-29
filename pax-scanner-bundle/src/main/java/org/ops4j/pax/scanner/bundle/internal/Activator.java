package org.ops4j.pax.scanner.bundle.internal;

import org.ops4j.pax.runner.provision.scanner.AbstractScannerActivator;
import org.ops4j.pax.scanner.bundle.ServiceConstants;
import org.ops4j.pax.swissbox.property.BundleContextPropertyResolver;
import org.ops4j.util.property.PropertyResolver;
import org.osgi.framework.BundleContext;

/**
 * Bundle activator for bundle scanner.
 *
 * @author Alin Dreghiciu
 * @since September 04, 2007
 */
public class Activator
    extends AbstractScannerActivator<BundleScanner>
{

    /**
     * {@inheritDoc}
     */
    @Override
    protected BundleScanner createScanner( final BundleContext bundleContext )
    {
        return new BundleScanner( new BundleContextPropertyResolver( bundleContext ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getPID()
    {
        return ServiceConstants.PID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getSchema()
    {
        return ServiceConstants.SCHEMA;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setResolver( final PropertyResolver propertyResolver )
    {
        getScanner().setResolver( propertyResolver );
    }

}
