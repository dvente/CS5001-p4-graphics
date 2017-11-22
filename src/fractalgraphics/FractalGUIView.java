package fractalgraphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Stack;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class FractalGUIView extends JFrame implements Observer {

	public final static int DEFAULT_X_RESOLUTION = 800;
	public final static int DEFAULT_Y_RESOLUTION = 800;

	private FractalGUIController controler;
	private FractalGUICanvas canvas;
	private JMenuBar menuBar;
	private JMenuItem resetButton;
	private JMenuItem undoButton;
	private JMenuItem redoButton;
	private JMenuItem saveButton;
	private JMenuItem loadButton;
	private JMenuItem exportButton;
	private JTextField maxIterationInputField;
	
	private int firstX, firstY, secondX = -1, secondY = -1;
	
	private int leftX;
	private int upperY;
	private int rightX;
	private int lowerY;
	
	private int currentXSize = DEFAULT_X_RESOLUTION;
	private int curretnYSize = DEFAULT_Y_RESOLUTION;

	public int getCurrentXSize() {
		return DEFAULT_X_RESOLUTION;
	}

	public int getCurrentYSize() {
		return DEFAULT_Y_RESOLUTION;
	}

	private void setupToolbar() {

		menuBar = new JMenuBar();
		getContentPane().add(menuBar);

		resetButton = new JMenuItem("Reset");
		menuBar.add(resetButton);
		resetButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controler.reset();

			}
		});

		undoButton = new JMenuItem("Undo");
		menuBar.add(undoButton);
		undoButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (controler.hasConfigHistory()) {

					controler.undo();
				} else {
					showErrorDialoge("No previous state to revert to");
				}

			}
		});

		redoButton = new JMenuItem("Redo");
		menuBar.add(redoButton);
		redoButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (controler.hasConfigUndoneHistory()) {

					controler.redo();
				} else {
					showErrorDialoge("No future state to revert to");
				}
			}
		});
		
		saveButton = new JMenuItem("Save");
		menuBar.add(saveButton);
		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(fc);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						controler.saveConfigHistory(fc.getSelectedFile());
					} catch (FileNotFoundException e1) {
						showErrorDialoge("File not found");
						e1.printStackTrace();
					} catch (IOException e1) {
						showErrorDialoge("IOExecption\n"  );
						e1.printStackTrace();
					}
				}
			}
		});
		
		
		loadButton = new JMenuItem("Load");
		menuBar.add(loadButton);
		loadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(fc);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						controler.loadConfigHistory(fc.getSelectedFile());
					} catch (FileNotFoundException e1) {
						showErrorDialoge("File not found");
						e1.printStackTrace();
					} catch (ClassNotFoundException e1) {
						showErrorDialoge("Internal error, class not found");
						e1.printStackTrace();
					} catch (IOException e1) {
						showErrorDialoge("IOException");
					}
				}
			}
		});
		
		
		exportButton = new JMenuItem("Export");
		menuBar.add(exportButton);
		exportButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(fc);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					controler.PNGImageFromCurrentConfig(fc.getSelectedFile());
				}
			}
		});
		
		maxIterationInputField = new JTextField(50);
		menuBar.add(maxIterationInputField);
		maxIterationInputField.setText(Integer.toString(controler.getMaxIterations()));
		maxIterationInputField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
				controler.setMaxIterations(Integer.parseInt(maxIterationInputField.getText()));
				} catch (NumberFormatException nfe) {
					showErrorDialoge("Please input a number");
				}
				
			}
		});
		
		

		add(menuBar, BorderLayout.NORTH);

	}

	public void showErrorDialoge(String message) {
		JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public FractalGUIView(FractalGUIController controler, int[][] fractalData) {

		this.controler = controler;
		setupToolbar();
		canvas = new FractalGUICanvas(fractalData,controler.getMaxIterations());
		getContentPane().add(canvas);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(DEFAULT_X_RESOLUTION, DEFAULT_Y_RESOLUTION);
		
		addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				secondX = e.getX();
				secondY = e.getY();
				leftX = Math.min(firstX, secondX);
				upperY = Math.max(firstY, secondY);
							
				int width = Math.abs(firstX-secondX);
				int height = Math.abs(firstY-secondY);
				
				rightX = leftX + Math.max(width,height);
				lowerY = upperY - Math.max(width,height);
				repaint();
				
			}
		});

		addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {
				
				secondX = e.getX();
				secondY = e.getY();
				int leftX = Math.min(firstX, secondX);
				int upperY = Math.max(firstY, secondY);
							
				int width = Math.abs(firstX-secondX);
				int height = Math.abs(firstY-secondY);
				
				int rightX = leftX + Math.max(width,height);
				int lowerY = upperY - Math.max(width,height);

				if(rightX > leftX && upperY > lowerY) {
					controler.recentre(leftX,upperY,rightX,lowerY);
				}
				secondX = -1;
			}

			@Override
			public void mousePressed(MouseEvent e) {

				firstX = e.getX();
				firstY = e.getY();
				System.out.println("FirstX: " + Integer.toString(firstX) + " FirstY: " + Integer.toString(firstY));
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {}

			@Override
			public void mouseExited(MouseEvent arg0) {}
		});

		setVisible(true);

	}

	@Override
	public void update(Observable arg0, Object arg1) {
		canvas.setData((int[][]) arg1,controler.getMaxIterations());
		repaint();

	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if(secondX != -1) {
			g.drawRect(leftX,lowerY,rightX-leftX,upperY-lowerY);
		}
		
		
	}

	public void setInputFieldText(int maxIterations) {
		maxIterationInputField.setText(Integer.toString(maxIterations));
		
	}

}
