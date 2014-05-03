/*************************************************************************************
 *
 * Generated on Mon Mar 24 15:23:12 GMT 2014 by Spray DirectEditFeature.xtend
 * 
 * This file is an extension point: copy to "src" folder to manually add code to this
 * extension point.
 *
 *************************************************************************************/
package man.ac.uk.xman.spray.atomicdesign.features;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IDirectEditingContext;

public class AtomicDesignDirectEditDataElementFeature extends AtomicDesignDirectEditDataElementFeatureBase {
    public AtomicDesignDirectEditDataElementFeature(IFeatureProvider fp) {
        super(fp);
    }
    
    @Override
    public boolean canDirectEdit(IDirectEditingContext context) {
    	return false;
    }
}
