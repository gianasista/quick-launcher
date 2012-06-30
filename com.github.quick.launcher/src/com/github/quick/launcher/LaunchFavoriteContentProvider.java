/**
 * 
 */
package com.github.quick.launcher;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org.eclipse.debug.internal.ui.launchConfigurations.LaunchConfigurationManager;
import org.eclipse.debug.internal.ui.launchConfigurations.LaunchGroupExtension;
import org.eclipse.debug.internal.ui.launchConfigurations.LaunchHistory;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * @author vera
 *
 */
public class LaunchFavoriteContentProvider extends LabelProvider implements ITreeContentProvider 
{
	List<ILaunchConfiguration> favoriteAntConfigurations = new ArrayList<ILaunchConfiguration>();
	
	public LaunchFavoriteContentProvider()
	{
		// FavoritesContentProvider, LaunchConfigurationTreeContentProvider
		LaunchConfigurationManager manager = DebugUIPlugin.getDefault().getLaunchConfigurationManager();
		LaunchHistory history = manager.getLaunchHistory("org.eclipse.ui.externaltools.launchGroup");
		ILaunchConfiguration[] favorites = history.getFavorites();
		for(ILaunchConfiguration configuration : favorites)
		{
			try
			{
				if("org.eclipse.ant.AntLaunchConfigurationType".equals(configuration.getType().getIdentifier()))
				{
					favoriteAntConfigurations.add(configuration);
				}
			}
			catch (CoreException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) 
	{
	}

	public Object[] getElements(Object inputElement) 
	{
		return favoriteAntConfigurations.toArray();
	}

	public Object[] getChildren(Object parentElement) 
	{
		return null;
	}

	public Object getParent(Object element) 
	{
		return null;
	}

	public boolean hasChildren(Object element) 
	{
		return false;
	}
	
	@Override
	public String getText(Object element) 
	{
		return ((ILaunchConfiguration)element).getName();
	}
	
	@Override
	public Image getImage(Object element) 
	{
		return AbstractUIPlugin.imageDescriptorFromPlugin("com.google.code.moreant", "icons/targetpublic_obj.gif").createImage();
	}

}
