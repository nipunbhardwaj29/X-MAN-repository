/*************************************************************************************
 *
 * Generated on Wed Mar 26 18:05:48 GMT 2014 by Spray PropertySection.xtend
 * 
 * This file is an extension point: copy to "src" folder to manually add code to this
 * extension point.
 *
 *************************************************************************************/
package man.ac.uk.xman.spray.atomicdesign.property;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.ui.editor.DefaultRefreshBehavior;
import org.eclipse.graphiti.ui.editor.DiagramBehavior;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;

public class MethodSignatureSection extends MethodSignatureSectionBase {

	final ModifyListener signatureNameListener = new ModifyListener() {

		@Override
		public void modifyText(ModifyEvent e) {
			final TransactionalEditingDomain editingDomain = getDiagramContainer()
					.getDiagramBehavior().getEditingDomain();
			editingDomain.getCommandStack().execute(
					new RecordingCommand(editingDomain) {
						@Override
						protected void doExecute() {
							DefaultRefreshBehavior df = new DefaultRefreshBehavior(
									(DiagramBehavior) getDiagramContainer()
											.getDiagramBehavior());
							df.refreshRenderingDecorators(getSelectedPictogramElement());
						}
					});
		}
	};

	@Override
	public void refresh() {
		super.refresh();
		signatureWidget.addModifyListener(signatureNameListener);
	}
}
