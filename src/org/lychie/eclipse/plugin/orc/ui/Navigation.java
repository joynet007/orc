package org.lychie.eclipse.plugin.orc.ui;

import java.awt.Font;
import java.awt.Image;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

/**
 * 进度导航类
 * 
 * @author Lychie Fan
 */
public class Navigation extends JFrame {
	
    public Navigation() {
    	super("Create Source File Progress");
        initComponents();
    }
    
    public void setProgressBound(int max) {
    	this.max = max;
		progressBar.setMinimum(MIN);
		progressBar.setMaximum(max);
		progressBar.setValue(MIN);
		setAlwaysOnTop(true);
		setLocationRelativeTo(null);
		progressBar.setStringPainted(true);
		this.setVisible(true);
	}
    
	public void setProgress(int value, String message) {
		if (value > max) {
			this.dispose();
		} else {
			progressBar.setValue(value);
			label.setText(message);
		}
	}
    
    private void initComponents() {

        panel = new JPanel();
        label = new JLabel();
        progressBar = new JProgressBar();

		setIconImage(IMAGE_LOGO);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        panel.setFont(DEFAULT_FONT);

        label.setFont(DEFAULT_FONT);
        label.setText("Preparing . . .");

        progressBar.setFont(DEFAULT_FONT);

        GroupLayout panelLayout = new GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(progressBar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addComponent(label, GroupLayout.PREFERRED_SIZE, 568, GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pack();
    }
    
    private static final int MIN = 0;
    
	private static final long serialVersionUID = 5468603317396208333L;

	private static final Font DEFAULT_FONT = new Font("Consolas", 0, 12);

	private static final Image IMAGE_LOGO = new ImageIcon(Navigation.class.getResource("synchronize.png")).getImage();
	
	private int max;
	private JLabel label;
    private JPanel panel;
    private JProgressBar progressBar;

}