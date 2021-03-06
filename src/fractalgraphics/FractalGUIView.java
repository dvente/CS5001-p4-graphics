/*
 *
 */
package fractalgraphics;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

/**
 * The Class FractalGUIView.
 *
 * @author 170008773
 */
public class FractalGUIView extends JFrame implements Observer {

    private static final int MENU_BAR_HEIGHT = 2;

    private static final int MENU_BAR_WIDTH = 6;

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4501449361859666201L;

    /** The Constant DEFAULT_X_RESOLUTION. */
    public static final int DEFAULT_X_RESOLUTION = 500;

    /** The Constant DEFAULT_Y_RESOLUTION. */
    public static final int DEFAULT_Y_RESOLUTION = 500;

    /** The controler. */
    private FractalGUIController controler;

    /** The canvas. */
    private FractalGUICanvas canvas;

    /** The menu bar. */
    private JMenuBar menuBar;

    /** The reset button. */
    private JMenuItem resetButton;

    /** The undo button. */
    private JMenuItem undoButton;

    /** The redo button. */
    private JMenuItem redoButton;

    /** The save button. */
    private JMenuItem saveButton;

    /** The load button. */
    private JMenuItem loadButton;

    /** The export to PNG button. */
    private JMenuItem exportToPNGButton;

    /** The help. */
    private JMenuItem helpButton;

    /** The about. */
    private JMenuItem aboutButton;

    /** The max iteration input field. */
    private JTextField maxIterationInputField;

    /** The second Y. */
    private int firstX, firstY, secondX = -1, secondY = -1;

    /** The zoom in factor. */
    private final double zoomInFactor = 1.2;

    /**
     * Instantiates a new fractal GUI view.
     *
     * @param controler
     *            the controler
     * @param fractalData
     *            the fractal data
     */
    public FractalGUIView(FractalGUIController controler, int[][] fractalData) {

        this.controler = controler;
        setupToolbar();
        canvas = new FractalGUICanvas(fractalData, controler.getColorMapping());
        getContentPane().add(canvas);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(DEFAULT_X_RESOLUTION, DEFAULT_Y_RESOLUTION);

        addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {

                firstX = secondX;
                firstY = secondY;
                secondX = e.getX();
                secondY = e.getY();

                controler.applyTranslateScreen(-1 * (firstX - secondX), -1 * (firstY - secondY));
                repaint();

            }

            @Override
            public void mouseMoved(MouseEvent e) {

                firstX = secondX;
                firstY = secondY;
                secondX = e.getX();
                secondY = e.getY();

            }
        });

        addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {

                int wheelRotationClicks = e.getWheelRotation();
                if (wheelRotationClicks != 0) {
                    controler.applyCentreScale(Math.pow(zoomInFactor, wheelRotationClicks));
                }

            }
        });

        addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent arg0) {

            }

            @Override
            public void mouseExited(MouseEvent arg0) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

                if (e.getButton() == MouseEvent.BUTTON3) {
                    controler.toggleAnimation();
                }

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }
        });

        setVisible(true);

    }

    // next function taken and addapted from
    // https://stackoverflow.com/questions/1349220/convert-jpanel-to-image
    /**
     * Gets the buffered image from canvas.
     *
     * @return the buffered image from canvas
     */
    public BufferedImage getBufferedImageFromCanvas() {

        int w = canvas.getWidth();
        int h = canvas.getHeight();
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        canvas.paint(g);
        return bi;
    }

    /**
     * Gets the current X size.
     *
     * @return the current X size
     */
    public int getXSize() {

        return DEFAULT_X_RESOLUTION;
    }

    /**
     * Gets the current Y size.
     *
     * @return the current Y size
     */
    public int getYSize() {

        return DEFAULT_Y_RESOLUTION;
    }

    /**
     * Sets the max iterations text.
     *
     * @param newMaxIterations
     *            the new max iterations text
     */
    public void setMaxIterationsText(int newMaxIterations) {

        maxIterationInputField.setText(Integer.toString(newMaxIterations));
    }

    /**
     * Setup toolbar.
     */
    private void setupToolbar() {

        menuBar = new JMenuBar();
        menuBar.setLayout(new FlowLayout(FlowLayout.LEADING));
        getContentPane().add(menuBar);

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenu editMenu = new JMenu("Edit");
        menuBar.add(editMenu);

        resetButton = new JMenuItem("Reset");
        resetButton.setLayout(new FlowLayout());
        KeyStroke keyStrokeToReset = KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK);
        resetButton.setAccelerator(keyStrokeToReset);
        editMenu.add(resetButton);
        resetButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                controler.reset();

            }
        });

        undoButton = new JMenuItem("Undo");
        KeyStroke keyStrokeToUndo = KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK);
        undoButton.setAccelerator(keyStrokeToUndo);
        editMenu.add(undoButton);
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
        KeyStroke keyStrokeToRedo = KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK);
        redoButton.setAccelerator(keyStrokeToRedo);
        editMenu.add(redoButton);
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
        KeyStroke keyStrokeToSave = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK);
        saveButton.setAccelerator(keyStrokeToSave);
        fileMenu.add(saveButton);
        saveButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser fc = new JFileChooser();
                int returnVal = fc.showSaveDialog(fc);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    try {
                        controler.save(file);
                    } catch (FileNotFoundException e1) {
                        showErrorDialoge("File Not Found");
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        showErrorDialoge("IOException");
                        e1.printStackTrace();
                    }

                }
            }
        });

        loadButton = new JMenuItem("Open");
        KeyStroke keyStrokeToLoad = KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK);
        loadButton.setAccelerator(keyStrokeToLoad);
        fileMenu.add(loadButton);
        loadButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser fc = new JFileChooser();
                int returnVal = fc.showOpenDialog(fc);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    try {
                        controler.load(file);
                    } catch (FileNotFoundException e1) {
                        showErrorDialoge("File Not Found");
                        e1.printStackTrace();
                    } catch (ClassNotFoundException e1) {
                        showErrorDialoge("Class not found");
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        showErrorDialoge("IOException");
                        e1.printStackTrace();
                    }

                }
            }
        });

        exportToPNGButton = new JMenuItem("Export to PNG");
        KeyStroke keyStrokeToExport = KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK);
        exportToPNGButton.setAccelerator(keyStrokeToExport);
        fileMenu.add(exportToPNGButton);
        exportToPNGButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser fc = new JFileChooser();
                int returnVal = fc.showSaveDialog(fc);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    try {
                        controler.exportToPNG(file);
                    } catch (FileNotFoundException e1) {
                        showErrorDialoge("File Not Found");
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        showErrorDialoge("IOException");
                        e1.printStackTrace();
                    }

                }
            }
        });

        helpButton = new JMenuItem("Help");
        menuBar.add(helpButton);
        helpButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                JLabel label = new JLabel(
                        "<html>Welcome to The Mandelbrot GUI Explorer. <br><br> <ul><li>You can click and drag the image or use the arrow keys, and zoom in and out with the mousewheel.</li><li> Clicking with the right mouse button will toggle a zoom animation.</li><li>You can cycle between different colour mappings by pressing the spacebar.</li><li>Using the buttons at the top you can reset, save, load, export to PNG, undo and redo any changes to the current image shown.</li><li>You can change the number of iterations used to draw the image by changing it in the text bar at the top. Increasing the number will increase the resolution of the image, but make the application slower.</li><li>You can exit the application by pressing the ESC button</li></ul>  </html>");
                label.setFont(new Font("Arial", Font.BOLD, 18));
                JOptionPane.showMessageDialog(null, label, "Help", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        aboutButton = new JMenuItem("About");
        menuBar.add(aboutButton);
        aboutButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                JLabel label = new JLabel(
                        "<html>This application was developed by Daniel Vente (<a href=\"https://github.com/dvente\">https://github.com/dvente</a>) as an assignement for CS5001 at the Universtiry of St. Andrews 2017.<br> Code for the Mandelbrot calculations was provided by Jonathan Lewis (jon.lewis at st-andrews.ac.uk)</html>");
                label.setFont(new Font("Arial", Font.BOLD, 18));
                JOptionPane.showMessageDialog(null, label, "Help", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        maxIterationInputField = new JTextField(5);
        menuBar.add(maxIterationInputField, BorderLayout.EAST);
        maxIterationInputField.setText(Integer.toString(controler.getMaxIterations()));
        maxIterationInputField.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    controler.setMaxIterations(Integer.parseInt(maxIterationInputField.getText().trim()));

                } catch (NumberFormatException nfe) {
                    showErrorDialoge("Please input a number");
                }

            }
        });

        maxIterationInputField.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    controler.applyNextColorMapping();
                }
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    controler.applyTranslateScreen(0, 1);
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    controler.applyTranslateScreen(0, -1);
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    controler.applyTranslateScreen(1, 0);
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    controler.applyTranslateScreen(-1, 0);
                }

                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    controler.close();
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }

            @Override
            public void keyTyped(KeyEvent e) {

            }
        });

        add(menuBar, BorderLayout.NORTH);

    }

    /**
     * Show error dialoge.
     *
     * @param message
     *            the message
     */
    public void showErrorDialoge(String message) {

        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void update(Observable arg0, Object arg1) {

        canvas.setData((int[][]) arg1, controler.getColorMapping());
        repaint();

    }
}
