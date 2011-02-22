package org.ops4j.pax.scanner.features.internal;

import org.apache.karaf.features.internal.FeaturesServiceImpl;

public class FeatureServiceWrapper extends FeaturesServiceImpl {

    @Override
    protected void saveState() {
        // overwrite method to avoid exception here
    }

}
