/*************************************************************************************
 *
 * Generated on Tue Mar 25 13:32:33 GMT 2014 by Spray PropertySection.xtend
 * 
 * This file is an extension point: copy to "src" folder to manually add code to this
 * extension point.
 *
 *************************************************************************************/
package man.ac.uk.xman.spray.atomicdesign.property;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.ISection;
import org.eclipse.ui.views.properties.tabbed.ITabDescriptor;
import org.eclipse.ui.views.properties.tabbed.ITabItem;
import org.eclipse.ui.views.properties.tabbed.TabContents;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

public class ComputationUnitSourceCodeSection extends
		ComputationUnitSourceCodeSectionBase {

	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		parent.setVisible(false);
		super.createControls(parent, tabbedPropertySheetPage);
	}
}
