package man.ac.uk.xman.spray.atomicdesign.helpers;

import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.graphiti.platform.IDiagramContainer;
import org.eclipse.graphiti.platform.IPlatformImageConstants;
import org.eclipse.graphiti.tb.ImageDecorator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.ide.ResourceUtil;

/**
 * 
 * 
 * Utility methods for the atomiDesign project. This class is not intended to be
 * instantiated.
 */

public class AtomicDesignUtils {

	private AtomicDesignUtils() {
	}

	/**
	 * Returns the resource associated with the opened editor part
	 * 
	 * @return {@link IResource} the resource of the current active editor part
	 */
	public static IResource getCurrentResource() {
		if (PlatformUI.getWorkbench() != null) {
			IWorkbench wb = PlatformUI.getWorkbench();
			if (wb.getActiveWorkbenchWindow() != null) {
				IWorkbenchWindow window = wb.getActiveWorkbenchWindow();
				if (window.getActivePage() != null) {
					IWorkbenchPage page = window.getActivePage();
					if (page.getActiveEditor() != null) {
						IEditorPart editor = page.getActiveEditor();
						if (ResourceUtil.getResource(editor.getEditorInput()) != null) {
							IResource currentResource = ResourceUtil
									.getResource(editor.getEditorInput());
							return currentResource;
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Returns the appropriate <code> IPlatformImageConstant </code> according
	 * to the severity mask set in diagnostic.
	 * 
	 * @param diagnostic
	 *            the diagnostic object containing severity, not null
	 * @return string the string containing the image id
	 * @throws NullPointerException
	 *             if diagnostic is null
	 */
	public static String getImageIDBySeverity(Diagnostic diagnostic)
			throws NullPointerException {
		if (diagnostic == null) {
			throw new NullPointerException("diagnostic is null");
		}
		String imageId = null;
		switch (diagnostic.getSeverity()) {
		case Diagnostic.ERROR:
			imageId = IPlatformImageConstants.IMG_ECLIPSE_ERROR;
			break;
		case Diagnostic.WARNING:
			imageId = IPlatformImageConstants.IMG_ECLIPSE_WARNING;
			break;
		default:
			break;
		}

		return imageId;
	}

	/**
	 * Creates a marker with the provided attributes
	 * 
	 * @param message
	 *            message to visualise
	 * @param severity
	 *            severity of the marker
	 * @param location
	 *            location of where the marker should be placed
	 * @return {@link IMarker} the create marker with the set attribute
	 * @throws CoreException
	 */
	public static IMarker createMarker(String message, Integer severity,
			String location) throws CoreException {
		IMarker marker = getCurrentResource().createMarker(IMarker.PROBLEM);
		marker.setAttribute(IMarker.MESSAGE, message);
		marker.setAttribute(IMarker.SEVERITY, severity);
		marker.setAttribute(IMarker.LOCATION, location);
		return marker;
	}

	/**
	 * Destroys the provided set of markers
	 * 
	 * @param markers
	 *            the set of markers to destroy
	 * @throws CoreException
	 */
	public static void destroyMarker(Set<IMarker> markers) throws CoreException {
		for (IMarker marker : markers) {
			marker.delete();
		}
	}

	/**
	 * Creates an {@link ImageDecorator} according to the severity provided by
	 * diagnostic
	 * 
	 * @param diagnostic
	 *            the status of the operation
	 * @param message
	 *            the message to show as tooltip
	 * @param x
	 *            the x position
	 * @param y
	 *            the y position
	 * @return {@link ImageDecorator}
	 */
	public static ImageDecorator createImageDecorator(Diagnostic diagnostic,
			String message, int x, int y) {
		ImageDecorator imgDecorator = new ImageDecorator(
				getImageIDBySeverity(diagnostic));
		imgDecorator.setMessage(message);
		imgDecorator.setX(x);
		imgDecorator.setY(y);
		return imgDecorator;
	}

	/**
	 * Pop up a message to the user
	 * 
	 * @param event
	 *            the execution event that contains the application context
	 * @param title
	 *            the pop up window title
	 * @param msg
	 *            the message to display
	 * 
	 */
	public static void popUpMessage(ExecutionEvent event, String title,
			String msg) {
		Validate.notNull(event);
		Validate.notNull(title);
		Validate.notNull(msg);
		MessageDialog.openInformation(
				HandlerUtil.getActiveWorkbenchWindow(event).getShell(), title,
				msg);
	}

	
	/**
	 * Returns the opened editor part
	 * 
	 * @return {@link IEditorPart} the current active editor part
	 */
	public static IEditorPart getCurrentEditor() {
		if (PlatformUI.getWorkbench() != null) {
			IWorkbench wb = PlatformUI.getWorkbench();
			if (wb.getActiveWorkbenchWindow() != null) {
				IWorkbenchWindow window = wb.getActiveWorkbenchWindow();
				if (window.getActivePage() != null) {
					IWorkbenchPage page = window.getActivePage();
					if (page.getActiveEditor() != null) {
						IEditorPart editor = page.getActiveEditor();
						return editor;
					}
				}
			}
		}
		return null;
	}
}