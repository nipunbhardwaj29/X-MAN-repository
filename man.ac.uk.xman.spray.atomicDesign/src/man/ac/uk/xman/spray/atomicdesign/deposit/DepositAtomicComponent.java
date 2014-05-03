package man.ac.uk.xman.spray.atomicdesign.deposit;

import man.ac.uk.xman.spray.atomicdesign.diagram.AtomicDesignModelService;
import man.ac.uk.xman.spray.atomicdesign.helpers.AtomicDesignUtils;
import man.ac.uk.xman.spray.atomicdesign.helpers.cdo.CDOUtils;

import org.apache.commons.lang3.Validate;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.emf.cdo.session.CDOSession;
import org.eclipse.emf.cdo.util.CommitException;
import org.eclipse.emf.cdo.util.ConcurrentAccessException;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.platform.IDiagramContainer;
import org.eclipse.net4j.Net4jUtil;
import org.eclipse.net4j.tcp.TCPUtil;
import org.eclipse.net4j.util.container.IPluginContainer;

import uk.man.xman.xcore.AtomicComponent;

public class DepositAtomicComponent extends AbstractHandler {

	private final String ERROR_MESSAGE = "Error";
	static AtomicComponent atomicComponent = null;

	@Override
	public Object execute(ExecutionEvent event) {
		Validate.notNull(event);
		
		// Root element services
		final AtomicDesignModelService atomicDesignModelService = AtomicDesignModelService
				.getModelService();
		
		if (atomicDesignModelService != null) {
			 
			// Retrieve the root element (AtomicComponent)
			IDiagramContainer editor = (IDiagramContainer) AtomicDesignUtils.getCurrentEditor();
		final TransactionalEditingDomain domain = editor.getDiagramBehavior()
				.getEditingDomain();
		domain.getCommandStack().execute(new RecordingCommand(domain) {
			protected void doExecute() {
				atomicComponent = atomicDesignModelService.getModel();
				String atomicComponentName = AtomicDesignUtils.getCurrentEditor().getTitle();
				atomicComponent.setName(atomicComponentName);
			}
		});
		
			
			// Deposit it
			  //Trigger validation
				// If successful
			    // Open Session
				Net4jUtil.prepareContainer(IPluginContainer.INSTANCE);
				TCPUtil.prepareContainer(IPluginContainer.INSTANCE);
				CDOSession cdoSession = CDOUtils.openSession("atomicComponent");
			  //Deposit it
			try {
				CDOUtils.depositComponent(cdoSession, atomicComponent);
			} catch (ConcurrentAccessException e) {
				AtomicDesignUtils.popUpMessage(event, ERROR_MESSAGE,
						"Error during the CDO repository transaction.");
			} catch (CommitException e) {
				AtomicDesignUtils.popUpMessage(event, ERROR_MESSAGE,
						"Error commiting the CDO repository transaction.");
			}
			// Close the session
			cdoSession.close();
			// Else show a message
		} else { // atomicDesign is null
			AtomicDesignUtils.popUpMessage(event, ERROR_MESSAGE,
					"Error retrieving the model service.");
		}
		return null;
	}
}
