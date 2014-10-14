package org.ops4j.pax.scanner.features.internal;

import org.apache.karaf.features.internal.BundleManager;
import org.apache.karaf.features.internal.FeatureConfigInstaller;
import org.apache.karaf.features.internal.FeaturesServiceImpl;

public class FeatureServiceWrapper extends FeaturesServiceImpl {

    public FeatureServiceWrapper(BundleManager bundleManager) {
        super(bundleManager);
    }

    public FeatureServiceWrapper(BundleManager bundleManager, FeatureConfigInstaller configManager) {
        super(bundleManager, configManager);
    }

    @Override
    protected void saveState() {
        // overwrite method to avoid exception here
    }

}
