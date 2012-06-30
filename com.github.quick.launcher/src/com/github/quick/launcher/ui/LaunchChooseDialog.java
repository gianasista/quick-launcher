package com.github.quick.launcher.ui;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;

import com.github.quick.launcher.LaunchFavoriteContentProvider;

/**
 * @author vera
 *
 */
public class LaunchChooseDialog extends PopupDialog implements DisposeListener
{
	private ILaunchConfiguration selectedConfiguration;
	private TreeViewer treeViewer;
	
	public LaunchChooseDialog()
	{
		super(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), PopupDialog.INFOPOPUP_SHELLSTYLE, true, false, false, false, false, "Targets", null);
	}
	
	public void widgetDisposed(DisposeEvent e) 
	{
		treeViewer = null;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) 
	{
		treeViewer = new TreeViewer(parent, SWT.NO_TRIM);
        treeViewer.setAutoExpandLevel(AbstractTreeViewer.ALL_LEVELS);
        LaunchFavoriteContentProvider provider = new LaunchFavoriteContentProvider();
		treeViewer.setContentProvider(provider);
		treeViewer.setLabelProvider(provider);
		treeViewer.setInput(this);
        
		final Tree tree = treeViewer.getTree();
        tree.addKeyListener(new KeyListener()
        {
            public void keyPressed(KeyEvent e)
            {
                if(e.character == SWT.ESC)
                    close();
            }

            public void keyReleased(KeyEvent e)
            {
                // do nothing
            }
        });

        tree.addSelectionListener(new SelectionListener()
        {
            public void widgetSelected(SelectionEvent e)
            {
                // do nothing
            }

            public void widgetDefaultSelected(SelectionEvent e)
            {
                selectedConfiguration = (ILaunchConfiguration) ((IStructuredSelection) treeViewer.getSelection()).getFirstElement();
                close();
            }
        });
        
        tree.addMouseMoveListener(new MouseMoveListener()
        {
            TreeItem fLastItem = null;

            public void mouseMove(MouseEvent e)
            {
                if(tree.equals(e.getSource()))
                {
                    TreeItem o = tree.getItem(new Point(e.x, e.y));
                    if(! o.equals(fLastItem))
                    {
                        fLastItem = (TreeItem) o;
                        tree.setSelection(new TreeItem[] { fLastItem });
                    }
                    else if(e.y < tree.getItemHeight() / 4)
                    {
                        // Scroll up
                        Point p = tree.toDisplay(e.x, e.y);
                        Item item = treeViewer.scrollUp(p.x, p.y);
                        if(item instanceof TreeItem)
                        {
                            fLastItem = (TreeItem) item;
                            tree.setSelection(new TreeItem[] { fLastItem });
                        }
                    }
                    else if(e.y > tree.getBounds().height - tree.getItemHeight() / 4)
                    {
                        // Scroll down
                        Point p = tree.toDisplay(e.x, e.y);
                        Item item = treeViewer.scrollDown(p.x, p.y);
                        if(item instanceof TreeItem)
                        {
                            fLastItem = (TreeItem) item;
                            tree.setSelection(new TreeItem[] { fLastItem });
                        }
                    }
                }
            }
        });
        
        tree.addMouseListener(new MouseAdapter()
        {
            public void mouseUp(MouseEvent e)
            {
                if(! tree.equals(e.getSource()))
                    close();

                if(tree.getSelectionCount() < 1)
                    return;

                if(e.button != 1)
                    return;

                if(tree.equals(e.getSource()))
                {
                    Object o = tree.getItem(new Point(e.x, e.y));
                    TreeItem selection = tree.getSelection()[0];
                    if(selection.equals(o))
                    {
                        selectedConfiguration = (ILaunchConfiguration) ((IStructuredSelection) treeViewer.getSelection()).getFirstElement();
                        close();
                    }
                }
            }
        });
        
        getShell().addDisposeListener(this);
        return treeViewer.getControl();
	}
	
	public ILaunchConfiguration getChoice()
    {
        open();
        runEventLoop(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
        return selectedConfiguration;
    }
	
	private void runEventLoop(Shell loopShell)
    {
        // NullSafe
        if (loopShell == null) {
            return;
        }
        
        Display display = loopShell.getDisplay();

        while (loopShell != null && ! loopShell.isDisposed() && treeViewer != null)
        {
            try
            {
                if(! display.readAndDispatch())
                {
                    display.sleep();
                }
            }
            catch (Throwable e)
            {
            	System.err.println(e);
            }
        }
        display.update();
    }

}
