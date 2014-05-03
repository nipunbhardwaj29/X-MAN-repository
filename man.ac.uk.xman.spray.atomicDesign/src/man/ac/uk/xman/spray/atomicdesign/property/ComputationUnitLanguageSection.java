/*************************************************************************************
 *
 * Generated on Fri Mar 28 15:58:23 GMT 2014 by Spray PropertySection.xtend
 * 
 * This file is an extension point: copy to "src" folder to manually add code to this
 * extension point.
 *
 *************************************************************************************/
package man.ac.uk.xman.spray.atomicdesign.property;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import man.ac.uk.xman.spray.atomicdesign.helpers.AtomicDesignUtils;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.platform.IDiagramContainer;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

import uk.man.xman.xcore.ComputationUnit;

public class ComputationUnitLanguageSection extends
		ComputationUnitLanguageSectionBase {

	private final String COMPUTATION_UNIT_FILE_NAME = "ComputationUnit.java";
	private final String COMPUTATION_UNIT_DIRECTORY_NAME = "/src/";

	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);
		final TabbedPropertySheetWidgetFactory factory = tabbedPropertySheetPage
				.getWidgetFactory();
		final Composite composite = factory.createFlatFormComposite(parent
				.getParent());
		
		Button createBehaviorButton = factory.createButton(composite,
				"Create Behavior", CENTER_SPACE);
		FormData data = new FormData();
		data.left = new FormAttachment(0, STANDARD_LABEL_WIDTH);
		createBehaviorButton.setLayoutData(data);
		createBehaviorButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				IWorkbench wb = PlatformUI.getWorkbench();
				IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
				IWorkbenchPage page = win.getActivePage();
				IEditorPart editor = page.getActiveEditor();
				if (editor instanceof IDiagramContainer) {
					IDiagramContainer graphitiEditor = (IDiagramContainer) editor;
					PictogramElement pe = graphitiEditor
							.getSelectedPictogramElements()[0];
					final Object bo = Graphiti.getLinkService()
							.getBusinessObjectForLinkedPictogramElement(pe);
					if (bo instanceof ComputationUnit) {
						final ComputationUnit cu = (ComputationUnit) bo;
						String workspaceDirectory = ResourcesPlugin
								.getWorkspace().getRoot().getLocation()
								.toString()
								+ "/";
						IProject project = AtomicDesignUtils
								.getCurrentResource().getProject();
						String currentProject = project.getName() + "/";
						String projectDirectory = workspaceDirectory
								+ currentProject;
						try {
							(new File(projectDirectory
									+ COMPUTATION_UNIT_DIRECTORY_NAME
									+ currentProject)).mkdirs();
							final File fileToOpen = new File(projectDirectory
									+ COMPUTATION_UNIT_DIRECTORY_NAME
									+ currentProject
									+ COMPUTATION_UNIT_FILE_NAME);
							fileToOpen.createNewFile();
							if (cu.getSourceCode() != null) {
								FileWriter fw = new FileWriter(fileToOpen
										.getAbsoluteFile());
								BufferedWriter bw = new BufferedWriter(fw);
								bw.write(cu.getSourceCode());
								bw.close();
								fw.close();
							}
							IFileStore fileOnLocalDisk = EFS
									.getLocalFileSystem().getStore(
											fileToOpen.toURI());
							project.refreshLocal(IResource.DEPTH_INFINITE, null);
							IDE.openInternalEditorOnFileStore(page,
									fileOnLocalDisk).addPropertyListener(
									new IPropertyListener() {
										@Override
										public void propertyChanged(
												Object source, int propId) {
											TransactionalEditingDomain editingDomain = getDiagramTypeProvider()
													.getDiagramBehavior()
													.getEditingDomain();
											editingDomain
													.getCommandStack()
													.execute(
															new RecordingCommand(
																	editingDomain) {
																@Override
																protected void doExecute() {
																	try {
																		@SuppressWarnings("resource")
																		BufferedReader reader = new BufferedReader(
																				new FileReader(
																						fileToOpen));
																		String line = null;
																		StringBuilder stringBuilder = new StringBuilder();
																		String ls = System
																				.getProperty("line.separator");

																		while ((line = reader
																				.readLine()) != null) {
																			stringBuilder
																					.append(line);
																			stringBuilder
																					.append(ls);
																		}
																		cu.setSourceCode(stringBuilder
																				.toString());
																	} catch (FileNotFoundException e) {
																		System.out
																				.println("Error opening file");
																	} catch (IOException e) {
																		System.out
																				.println("Error reading file");
																		e.printStackTrace();
																	}
																}
															});
										}
									});
						} catch (IOException ex) {
							System.out.println("Error creating file");
						} catch (PartInitException ex) {
							System.out.println("Error opening editor");
						} catch (CoreException ex) {
							System.out.println("Error refreshing project");
						}
					}
				}
			}	

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
			}
		});
		
		CLabel sourceCodeLabel = factory.createCLabel(composite, "sourceCode");
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		sourceCodeLabel.setLayoutData(data);
	}
}
