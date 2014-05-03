package man.ac.uk.xman.spray.atomicdesign.custom;

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
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipselabs.spray.runtime.graphiti.features.AbstractCustomFeature;

import uk.man.xman.xcore.ComputationUnit;

public class AtomicDesignDirectEditComputationUnitCreateBehavior extends
		AbstractCustomFeature {

	private final String COMPUTATION_UNIT_FILE_NAME = "ComputationUnit.java";
	private final String COMPUTATION_UNIT_DIRECTORY_NAME = "/src/";

	public AtomicDesignDirectEditComputationUnitCreateBehavior(
			IFeatureProvider fp) {
		super(fp);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canExecute(ICustomContext context) {
		// allow rename if exactly one pictogram element
		// representing a ComputationUnit is selected
		boolean ret = false;
		PictogramElement[] pes = context.getPictogramElements();
		if (pes != null && pes.length == 1) {
			Object bo = getBusinessObjectForPictogramElement(pes[0]);
			if (bo instanceof ComputationUnit) {
				ret = true;
			}
		}
		return ret;
	}

	@Override
	public void execute(ICustomContext context) {
		PictogramElement[] pes = context.getPictogramElements();
		if (pes != null && pes.length == 1) {
			Object bo = getBusinessObjectForPictogramElement(pes[0]);
			if (bo instanceof ComputationUnit) {
				final ComputationUnit cu = (ComputationUnit) bo;
				String workspaceDirectory = ResourcesPlugin.getWorkspace()
						.getRoot().getLocation().toString()
						+ "/";
				IProject project = AtomicDesignUtils.getCurrentResource()
						.getProject();
				String currentProject = project.getName() + "/";
				String projectDirectory = workspaceDirectory + currentProject;
				try {
					(new File(projectDirectory
							+ COMPUTATION_UNIT_DIRECTORY_NAME + currentProject))
							.mkdirs();
					final File fileToOpen = new File(projectDirectory
							+ COMPUTATION_UNIT_DIRECTORY_NAME + currentProject
							+ COMPUTATION_UNIT_FILE_NAME);
					fileToOpen.createNewFile();
					if (cu.getSourceCode() != null) {
						FileWriter fw = new FileWriter(
								fileToOpen.getAbsoluteFile());
						BufferedWriter bw = new BufferedWriter(fw);
						bw.write(cu.getSourceCode());
						bw.close();
						fw.close();
					}
					IFileStore fileOnLocalDisk = EFS.getLocalFileSystem()
							.getStore(fileToOpen.toURI());
					IWorkbenchPage page = PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getActivePage();
					project.refreshLocal(IResource.DEPTH_INFINITE, null);
					IDE.openInternalEditorOnFileStore(page, fileOnLocalDisk)
							.addPropertyListener(new IPropertyListener() {
								@Override
								public void propertyChanged(Object source,
										int propId) {
									TransactionalEditingDomain editingDomain = getFeatureProvider()
											.getDiagramTypeProvider()
											.getDiagramBehavior()
											.getEditingDomain();
									editingDomain.getCommandStack()
											.execute(
													new RecordingCommand(
															editingDomain) {
														@Override
														protected void doExecute() {
															try {
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
																cu.setSourceCode(stringBuilder.toString());
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
				} catch (IOException e) {
					System.out.println("Error creating file");
				} catch (PartInitException e) {
					System.out.println("Error opening editor");
				} catch (CoreException e) {
					System.out.println("Error refreshing project");
				}
			}
		}
	}
}