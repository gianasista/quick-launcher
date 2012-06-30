package com.github.quick.launcher.ui;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;


/**
 * @author vera
 */
public class LaunchChooseAction extends AbstractHandler implements IEditorActionDelegate 
{

	public void run(IAction action) 
	{
		chooseAction();
	}

	public void selectionChanged(IAction action, ISelection selection) 
	{
	}

	public Object execute(ExecutionEvent event) throws ExecutionException 
	{
		chooseAction();
		return null;
	}

	public void setActiveEditor(IAction action, IEditorPart targetEditor) 
	{
	}
	
	private void chooseAction()
	{
		LaunchChooseDialog dialog = new LaunchChooseDialog();
		ILaunchConfiguration configuration = dialog.getChoice();
		
		try 
		{
			if(configuration != null)
			{
				configuration.launch(ILaunchManager.RUN_MODE, null);
			}
		}
		catch (CoreException e) 
		{
			e.printStackTrace();
		}
	}

}
