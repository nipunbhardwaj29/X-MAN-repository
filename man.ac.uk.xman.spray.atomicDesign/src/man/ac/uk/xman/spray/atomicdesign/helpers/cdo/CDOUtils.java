package man.ac.uk.xman.spray.atomicdesign.helpers.cdo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import man.ac.uk.xman.spray.atomicdesign.helpers.AtomicDesignUtils;

import org.apache.commons.lang3.Validate;
import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.net4j.CDONet4jSession;
import org.eclipse.emf.cdo.net4j.CDONet4jSessionConfiguration;
import org.eclipse.emf.cdo.net4j.CDONet4jUtil;
import org.eclipse.emf.cdo.session.CDOSession;
import org.eclipse.emf.cdo.transaction.CDOTransaction;
import org.eclipse.emf.cdo.util.CommitException;
import org.eclipse.emf.cdo.util.ConcurrentAccessException;
import org.eclipse.emf.cdo.view.CDOQuery;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.platform.IDiagramContainer;
import org.eclipse.net4j.connector.IConnector;
import org.eclipse.net4j.util.container.IPluginContainer;

import uk.man.xman.xcore.AtomicComponent;
import uk.man.xman.xcore.Component;

/**
 * Utility methods for CDO server This class is not intended to be instanced,
 * nor extended
 */
public class CDOUtils {

	private static final String PRODUCT_GROUP = "org.eclipse.net4j.connectors";
	private static final String CONNECTION_TYPE = "tcp";
	private static final String ADDRESS = "localhost";
	private static final String RETRIEVE_ALL_OBJECTS_FROM_REPOSITORY_QUERY = "SELECT cdo_id FROM ";
	private static final String SQL_QUERY = "sql";

	private CDOUtils() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Opens a CDO Session
	 * 
	 * @param repoName
	 *            the name of the database, not null
	 * @return {@link CDONet4jSession} the CDO session for the provided
	 *         repoName, null if problems occur
	 */
	public static CDONet4jSession openSession(String repoName) {

		Validate.notNull(repoName, "The repository name must be not null");

		CDONet4jSession session = null;
		final IConnector connector = (IConnector) IPluginContainer.INSTANCE
				.getElement(PRODUCT_GROUP, CONNECTION_TYPE, ADDRESS);

		CDONet4jSessionConfiguration config = CDONet4jUtil
				.createNet4jSessionConfiguration();
		config.setConnector(connector);
		config.setRepositoryName(repoName);
		session = config.openNet4jSession();
		return session;
	}

	public static void depositComponent(CDOSession cdoSession,
			Component component) throws ConcurrentAccessException,
			CommitException {
		Validate.notNull(cdoSession);
		Validate.notNull(component);

		// Open transaction
		CDOTransaction transaction = cdoSession.openTransaction();

		// Create a resource
		final CDOResource resource = transaction
				.getOrCreateResource("/myResource");

		// Check if it is already in the repository
		/*if (isPresent(component, transaction)) {
			update(component,
					(IDiagramContainer) AtomicDesignUtils.getCurrentEditor(),
					resource, transaction);
		} else {*/
			deposit(component,
					(IDiagramContainer) AtomicDesignUtils.getCurrentEditor(),
					resource, transaction);
			
			transaction.close();
		//}
	}

	/**
	 * Deposits the provider component in the CDO repository
	 * 
	 * @param component
	 *            the component that is going to be deposited
	 * @param editor
	 *            the current active editor
	 * @param resource
	 *            the currently open CDO repository resource
	 * @param transaction
	 *            the currently open CDO transaction
	 * @throws CommitException
	 * @throws ConcurrentAccessException
	 */
	private static void deposit(final Component component,
			IDiagramContainer editor, final CDOResource resource,
			CDOTransaction transaction) throws ConcurrentAccessException,
			CommitException {
		Validate.noNullElements(new ArrayList<Object>(Arrays.asList(component,
				editor, resource, transaction)));
		final TransactionalEditingDomain domain = editor.getDiagramBehavior()
				.getEditingDomain();
		domain.getCommandStack().execute(new RecordingCommand(domain) {
			protected void doExecute() {
				resource.getContents().add(component);
				
			}
		});
		transaction.commit();
		
		
	}

	/**
	 * Updates the provider component in the CDO repository
	 * 
	 * @param component
	 *            the component that is going to be deposited
	 * @param editor
	 *            the current active editor
	 * @param resource
	 *            the currently open CDO repository resource
	 * @param transaction
	 *            the currently open CDO transaction
	 * @throws CommitException
	 * @throws ConcurrentAccessException
	 */
	private static void update(final Component component,
			IDiagramContainer editor, final CDOResource resource,
			CDOTransaction transaction) throws ConcurrentAccessException,
			CommitException {
		Validate.noNullElements(new ArrayList<Object>(Arrays.asList(component,
				editor, resource, transaction)));
		final TransactionalEditingDomain domain = editor.getDiagramBehavior()
				.getEditingDomain();
		domain.getCommandStack().execute(new RecordingCommand(domain) {
			protected void doExecute() {
				int indexOfComponent = resource.getContents()
						.indexOf(component);
				resource.getContents().set(indexOfComponent, component);
			}
		});

		transaction.commit();
		transaction.close();
	}

	/**
	 * Checks the component presence in the CDO repository
	 * 
	 * @param component
	 *            the component that is going to be deposited
	 * @param transaction
	 *            the currently open CDO transaction
	 * @return {@link Boolean} true if is present, false otherwise
	 */
	private static boolean isPresent(Component component,
			CDOTransaction transaction) {
		Validate.noNullElements(new ArrayList<Object>(Arrays.asList(component,
				transaction)));
		String from = (component instanceof AtomicComponent) ? "atomiccomponent" : "compositecomponent";
		System.out.println("sql="+from);
		CDOQuery query = transaction.createQuery(SQL_QUERY,
				RETRIEVE_ALL_OBJECTS_FROM_REPOSITORY_QUERY + from);
		List<Component> componentList = query.getResult();
		for (Component cmp : componentList) {
			if (cmp.getName().matches(component.getName())) {
				return true;
			}
		}
		return false;
	}
}