package org.lychie.eclipse.plugin.orc.ui;

import java.io.File;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.lychie.eclipse.plugin.orc.res.model.Configuration;
import org.lychie.eclipse.plugin.orc.util.Configurations;
import org.lychie.eclipse.plugin.orc.util.Project;
import org.lychie.jutil.StringUtil;

/**
 * 引导界面视图
 * 
 * @author Lychie Fan
 */
public class Guidance extends TitleAreaDialog implements ModifyListener {

	private OnOKClick onOKClick;

	public Guidance(Shell parentShell) {
		super(parentShell);
	}

	public static void show(Shell parentShell, Configuration configuration,
			OnOKClick onOKClick) {
		Guidance guidance = new Guidance(parentShell);
		guidance.onOKClick = onOKClick;
		guidance.create();
		guidance.init(configuration);
		guidance.open();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(550, 305));
		
		menu = new Menu(getShell());

		final TabFolder folder = new TabFolder(container, SWT.NONE);
		folder.setBounds(15, 10, 520, 280);
		folder.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				if (folder.getSelectionIndex() == 0) {
					displayDBSettingInfo();
				}
				if (folder.getSelectionIndex() == 1) {
					displayExtraSettingInfo();
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent event) {}
		});

		TabItem dbTabItem = new TabItem(folder, SWT.NONE);
		dbTabItem.setImage(IMAGE_LOCK);
		dbTabItem.setText("DB Settings");

		Composite dbComposite = new Composite(folder, SWT.NONE);

		Label usernameLabel = new Label(dbComposite, SWT.NONE);
		usernameLabel.setFont(DEFAULT_FONT);
		usernameLabel.setText("DB Username :");
		usernameLabel.setBounds(25, 35, 100, 23);
		usernameText = new Text(dbComposite, SWT.BORDER);
		usernameText.setFont(DEFAULT_FONT);
		usernameText.setBounds(128, 35, 348, 23);
		usernameText.addModifyListener(this);
		usernameText.setMenu(menu);

		Label passwordLabel = new Label(dbComposite, SWT.NONE);
		passwordLabel.setFont(DEFAULT_FONT);
		passwordLabel.setText("DB Password :");
		passwordLabel.setBounds(25, 75, 100, 23);
		passwordText = new Text(dbComposite, SWT.BORDER | SWT.PASSWORD);
		passwordText.setFont(DEFAULT_FONT);
		passwordText.setBounds(128, 75, 348, 23);
		passwordText.addModifyListener(this);
		passwordText.setMenu(menu);

		Label connectionLabel = new Label(dbComposite, SWT.NONE);
		connectionLabel.setFont(DEFAULT_FONT);
		connectionLabel.setText("Connect URL :");
		connectionLabel.setBounds(25, 115, 100, 23);
		connectionText = new Text(dbComposite, SWT.BORDER);
		connectionText.setFont(DEFAULT_FONT);
		connectionText.setBounds(128, 115, 348, 23);
		connectionText.addModifyListener(this);
		connectionText.setMenu(menu);

		dbTabItem.setControl(dbComposite);

		TabItem extraTabItem = new TabItem(folder, SWT.NONE);
		extraTabItem.setImage(IMAGE_PUZZLE);
		extraTabItem.setText("Extra Settings");

		Composite extraComposite = new Composite(folder, SWT.NONE);

		Label packagenameLabel = new Label(extraComposite, SWT.NONE);
		packagenameLabel.setFont(DEFAULT_FONT);
		packagenameLabel.setText("Package Name    :");
		packagenameLabel.setBounds(25, 35, 130, 23);
		packagenameText = new Text(extraComposite, SWT.BORDER);
		packagenameText.setFont(DEFAULT_FONT);
		packagenameText.setBounds(158, 35, 318, 23);
		packagenameText.addModifyListener(this);
		packagenameText.setMenu(menu);

		Label sourceFolderLabel = new Label(extraComposite, SWT.NONE);
		sourceFolderLabel.setFont(DEFAULT_FONT);
		sourceFolderLabel.setText("Source Folder   :");
		sourceFolderLabel.setBounds(25, 75, 130, 23);
		sourceFolderText = new Text(extraComposite, SWT.BORDER);
		sourceFolderText.setFont(DEFAULT_FONT);
		sourceFolderText.setBounds(158, 75, 318, 23);
		sourceFolderText.setEditable(false);
		sourceFolderText.addModifyListener(this);
		sourceFolderText.addMouseListener(new MouseUpListener(getShell(),
				"Select the source folder"));
		sourceFolderText.setMenu(menu);

		Label excludeTablesLabel = new Label(extraComposite, SWT.NONE);
		excludeTablesLabel.setFont(DEFAULT_FONT);
		excludeTablesLabel.setText("Exclude Tables  :");
		excludeTablesLabel.setBounds(25, 115, 130, 23);
		excludeTablesText = new Text(extraComposite, SWT.BORDER);
		excludeTablesText.setToolTipText("table_name1, table_name2");
		excludeTablesText.setFont(DEFAULT_FONT);
		excludeTablesText.setBounds(158, 115, 318, 23);
		excludeTablesText.setMenu(menu);

		Label excludeColumnsLabel = new Label(extraComposite, SWT.NONE);
		excludeColumnsLabel.setFont(DEFAULT_FONT);
		excludeColumnsLabel.setText("Exclude Columns :");
		excludeColumnsLabel.setBounds(25, 155, 130, 23);
		excludeColumnsText = new Text(extraComposite, SWT.BORDER);
		excludeColumnsText.setToolTipText("*.column_name1, table.column_name2");
		excludeColumnsText.setFont(DEFAULT_FONT);
		excludeColumnsText.setBounds(158, 155, 318, 23);
		excludeColumnsText.setMenu(menu);

		Label templateFolderLabel = new Label(extraComposite, SWT.NONE);
		templateFolderLabel.setFont(DEFAULT_FONT);
		templateFolderLabel.setText("Template Folder :");
		templateFolderLabel.setBounds(25, 195, 130, 23);
		templateFolderText = new Text(extraComposite, SWT.BORDER);
		templateFolderText.setFont(DEFAULT_FONT);
		templateFolderText.setBounds(158, 195, 318, 23);
		templateFolderText.setEditable(false);
		templateFolderText.addMouseListener(new MouseUpListener(getShell(),
				"Select the model template folder"));
		templateFolderText.setMenu(menu);
		templateFolderText.setToolTipText("Click the right mouse button to clear");

		extraTabItem.setControl(extraComposite);

		return area;
	}

	@Override
	protected void okPressed() {
		Configuration config = getCurrent();
		super.okPressed();
		onOKClick.okClick(config);
	}

	@Override
	public boolean isHelpAvailable() {
		return false;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Project Configuration Guidance");
		newShell.setImage(IMAGE_LOGO);
	}

	@Override
	public void modifyText(ModifyEvent event) {
		if (StringUtil.isNotEmpty(usernameText.getText())
				&& StringUtil.isNotEmpty(passwordText.getText())
				&& StringUtil.isNotEmpty(connectionText.getText())
				&& StringUtil.isNotEmpty(packagenameText.getText())
				&& StringUtil.isNotEmpty(sourceFolderText.getText())) {
			getButton(IDialogConstants.OK_ID).setEnabled(true);
		} else {
			getButton(IDialogConstants.OK_ID).setEnabled(false);
		}
	}

	private Configuration getCurrent() {
		Configuration config = new Configuration();
		config.setUsername(usernameText.getText());
		config.setPassword(passwordText.getText());
		config.setConnection(connectionText.getText());
		config.setPackageName(packagenameText.getText());
		config.setSourceFolder(sourceFolderText.getText());
		config.setExcludeTables(excludeTablesText.getText());
		config.setExcludeColumns(excludeColumnsText.getText());
		config.setTemplateFolder(templateFolderText.getText());
		return config;
	}
	
	private void displayDBSettingInfo() {
		setTitle("This wizard will connect your project to a database");
		setMessage("support mysql and sqlserver",
				IMessageProvider.INFORMATION);
	}

	private void displayExtraSettingInfo() {
		setTitle("This wizard will guide you to configure the other items");
		setMessage("the first two is necessary",
				IMessageProvider.INFORMATION);
	}

	private void init(Configuration configuration) {
		if (configuration == null) {
			if (new File(Project.getPath(),
					Configurations.MAVEN_STYLE_SOURCE_FOLDER).exists()) {
				sourceFolderText.setText(Configurations.MAVEN_STYLE_SOURCE_FOLDER);
			} else {
				sourceFolderText.setText(Configurations.JAVA_STYLE_SOURCE_FOLDER);
			}
			getButton(IDialogConstants.OK_ID).setEnabled(false);
		} else {
			usernameText.setText(configuration.getUsername());
			passwordText.setText(configuration.getPassword());
			connectionText.setText(configuration.getConnection());
			packagenameText.setText(configuration.getPackageName());
			sourceFolderText.setText(configuration.getSourceFolder());
			excludeTablesText.setText(configuration.getExcludeTables());
			excludeColumnsText.setText(configuration.getExcludeColumns());
			templateFolderText.setText(configuration.getTemplateFolder());
			getButton(IDialogConstants.OK_ID).setEnabled(true);
		}
		displayDBSettingInfo();
	}

	public static interface OnOKClick { void okClick(Configuration config); }
	
	private static class MouseUpListener implements MouseListener {
		
		private Shell shell;
		private String title;
		private static final int LEFT_MOUSE_BUTTON = 1;
		private static final int RIGHT_MOUSE_BUTTON = 3;
		
		private MouseUpListener(Shell shell, String title) {
			this.shell = shell;
			this.title = title;
		}

		@Override
		public void mouseUp(MouseEvent event) {
			Text text = (Text) event.getSource();
			if (event.button == LEFT_MOUSE_BUTTON) {
				DirectoryDialog dialog = new DirectoryDialog(shell);
				dialog.setText(title);
				String pathname = Project.getPath();
				dialog.setFilterPath(pathname);
				String selectedPath = dialog.open();
				if (selectedPath != null && selectedPath.contains(pathname)) {
					selectedPath = StringUtil.afterLastString(selectedPath,
							pathname);
					selectedPath = selectedPath.replace("\\", "/");
					text.setText(selectedPath);
				}
			}
			if (event.button == RIGHT_MOUSE_BUTTON) {
				text.setText("");
			}
		}

		@Override
		public void mouseDown(MouseEvent event) {
			
		}

		@Override
		public void mouseDoubleClick(MouseEvent event) {
			
		}
		
	}
	
	private static final Font DEFAULT_FONT = new Font(Display.getDefault(),
			"Consolas", 10, 0);

	private static final Image IMAGE_LOGO = new Image(Display.getDefault(),
			Guidance.class.getResourceAsStream("synchronize.png"));

	private static final Image IMAGE_LOCK = new Image(Display.getDefault(),
			Guidance.class.getResourceAsStream("unlock.png"));

	private static final Image IMAGE_PUZZLE = new Image(Display.getDefault(),
			Guidance.class.getResourceAsStream("puzzle.png"));
	
	private Menu menu;
	private Text usernameText;
	private Text passwordText;
	private Text connectionText;
	private Text packagenameText;
	private Text sourceFolderText;
	private Text excludeTablesText;
	private Text excludeColumnsText;
	private Text templateFolderText;

}