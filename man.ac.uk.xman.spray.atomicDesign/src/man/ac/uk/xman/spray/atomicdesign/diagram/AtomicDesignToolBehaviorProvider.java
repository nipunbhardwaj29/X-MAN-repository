/*************************************************************************************
 *
 * Generated on Tue Mar 25 17:34:26 GMT 2014 by Spray ToolBehaviorProvider.xtend
 * 
 * This file is an extension point: copy to "src" folder to manually add code to this
 * extension point.
 *
 *************************************************************************************/
package man.ac.uk.xman.spray.atomicdesign.diagram;

import java.util.ArrayList;

import man.ac.uk.xman.spray.atomicdesign.custom.AtomicDesignDirectEditComputationUnitCreateBehavior;
import man.ac.uk.xman.spray.atomicdesign.helpers.AtomicDesignUtils;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.BasicDiagnostic;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IDoubleClickContext;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.tb.IDecorator;
import org.eclipse.graphiti.tb.ImageDecorator;

import com.google.common.collect.HashMultimap;

import uk.man.xman.xcore.ComputationUnit;
import uk.man.xman.xcore.DataElement;
import uk.man.xman.xcore.Method;
import uk.man.xman.xcore.Parameter;
import uk.man.xman.xcore.Service;
import uk.man.xman.xcore.util.XcoreValidator;

public class AtomicDesignToolBehaviorProvider extends
		AtomicDesignToolBehaviorProviderBase {

	private static final BasicDiagnostic diagnosticChain = new BasicDiagnostic();
	private static final HashMultimap<Integer, IMarker> markers = HashMultimap
			.create();
	private static final ArrayList<ImageDecorator> imageDecorators = new ArrayList<ImageDecorator>();
	private boolean called = false;

	public AtomicDesignToolBehaviorProvider(final IDiagramTypeProvider dtp) {
		super(dtp);
	}

	@Override
	public IDecorator[] getDecorators(final PictogramElement pe) {
		IFeatureProvider featureProvider = getFeatureProvider();
		Object bo = featureProvider.getBusinessObjectForPictogramElement(pe);
		Integer objectId;
		if (bo instanceof Method) {
			System.out.println("CHECKING METHOD");
			objectId = System.identityHashCode(bo.getClass());
			try { // generate decorator and markers
				if (!XcoreValidator.INSTANCE.validateMethod((Method) bo,
						diagnosticChain, null)) {
					Diagnostic lastDiagnostic = diagnosticChain.getChildren()
							.get(diagnosticChain.getChildren().size() - 1);
					markers.put(objectId, AtomicDesignUtils.createMarker(
							lastDiagnostic.getMessage(),
							IMarker.SEVERITY_ERROR, ((Method) bo).getClass()
									.getName()));
					return new ImageDecorator[] { AtomicDesignUtils
							.createImageDecorator(lastDiagnostic,
									lastDiagnostic.getMessage(), 20, 0) };

				} else {
					AtomicDesignUtils.destroyMarker(markers.get(objectId));
				}

			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (bo instanceof ComputationUnit) {
			System.out.println("CHECKING CU");
			if (!XcoreValidator.INSTANCE.validateComputationUnit(
					(ComputationUnit) bo, diagnosticChain, null)) {
				System.out.println("CHECKING METHOD");
			}
		}
		if (bo instanceof DataElement) {
			System.out.println("CHECKING DATAELEMENT");
			if (!XcoreValidator.INSTANCE.validateDataElement((DataElement) bo,
					diagnosticChain, null)) {
			}

		}

		if (bo instanceof Service) {
			System.out.println("CHECKING SERVICE");
			if (!XcoreValidator.INSTANCE.validateService((Service) bo,
					diagnosticChain, null)) {
			}
		}

		if (bo instanceof Parameter) {
			System.out.println("CHECKING PARAMETER");
			if (!XcoreValidator.INSTANCE.validateParameter((Parameter) bo,
					diagnosticChain, null)) {
			}
		}

		return super.getDecorators(pe);
	}

	@Override
	public ICustomFeature getDoubleClickFeature(IDoubleClickContext context) {
		ICustomFeature customFeature = new AtomicDesignDirectEditComputationUnitCreateBehavior(
				getFeatureProvider());
		// canExecute() tests especially if the context contains a
		// ComputationUnit
		if (customFeature.canExecute(context)) {
			return customFeature;
		}

		return super.getDoubleClickFeature(context);
	}
}
