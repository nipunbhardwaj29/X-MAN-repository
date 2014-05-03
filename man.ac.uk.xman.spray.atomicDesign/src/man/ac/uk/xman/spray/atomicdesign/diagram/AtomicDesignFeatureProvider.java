/*************************************************************************************
 *
 * Generated on Mon Mar 24 19:58:47 GMT 2014 by Spray FeatureProvider.xtend
 * 
 * This file is an extension point: copy to "src" folder to manually add code to this
 * extension point.
 *
 *************************************************************************************/
package man.ac.uk.xman.spray.atomicdesign.diagram;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipselabs.spray.runtime.graphiti.zest.features.ZestLayoutDiagramFeature;

import com.google.common.collect.Iterables;

public class AtomicDesignFeatureProvider extends
		AtomicDesignFeatureProviderBase {

	public AtomicDesignFeatureProvider(final IDiagramTypeProvider dtp) {
		super(dtp);
	}

	@Override
	public ICustomFeature[] getCustomFeatures(ICustomContext context) {
		ArrayList<ICustomFeature> features = new ArrayList<ICustomFeature>(
				Arrays.asList(super.getCustomFeatures(context)));
		ZestLayoutDiagramFeature zestLayoutFeature = new ZestLayoutDiagramFeature(
				this);
		if (zestLayoutFeature.canExecute(context)) {
			features.add(zestLayoutFeature);
		}
		return Iterables.toArray(features, ICustomFeature.class);
	}
}
