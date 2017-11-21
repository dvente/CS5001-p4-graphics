package fractalgraphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class FractalGUIView extends JFrame implements Observer {

    private final int DEFAULT_X_RESOLUTION = 800;
    private final int DEFAULT_Y_RESOLUTION = 800;

    private final FractalGUIConfig defaultConfig = new FractalGUIConfig(DEFAULT_X_RESOLUTION, DEFAULT_Y_RESOLUTION,
            MandelbrotCalculator.INITIAL_MIN_REAL, MandelbrotCalculator.INITIAL_MAX_REAL,
            MandelbrotCalculator.INITIAL_MIN_IMAGINARY, MandelbrotCalculator.INITIAL_MAX_IMAGINARY,
            MandelbrotCalculator.INITIAL_MAX_ITERATIONS, MandelbrotCalculator.DEFAULT_RADIUS_SQUARED, Color.white,
            Color.black);

    private final FractalGUIModel model;

    private FractalGUICanvas canvas;
    private JMenuBar menuBar;
    private JMenu resetButton;
    private JMenu undoButton;
    private JMenu redoButton;
    private FractalGUIConfig currentConfig = defaultConfig;

    public static void main(String[] args) {

        FractalGUIView window = new FractalGUIView();
    }

    private void setupToolbar() {

        // gui.setLayout(new FlowLayout());
        // Container cp = new JPanel();
        // cp.setLayout(new FlowLayout());
        //
        // toolBar = new JToolBar();
        // getContentPane().add(toolBar);
        //
        // resetButton = new JButton("Reset");
        // toolBar.add(resetButton);
        //
        // undoButton = new JButton("Undo");
        // toolBar.add(undoButton);
        //
        // redoButton = new JButton("Redo");
        // toolBar.add(redoButton);
        // cp.add(toolBar);
        // gui.add(cp);

        menuBar = new JMenuBar();
        getContentPane().add(menuBar);

        resetButton = new JMenu("Reset");
        menuBar.add(resetButton);

        undoButton = new JMenu("Undo");
        menuBar.add(undoButton);

        redoButton = new JMenu("Redo");
        menuBar.add(redoButton);

        canvas.add(menuBar, BorderLayout.SOUTH);

    }

    public FractalGUIView() {

        model = new FractalGUIModel(defaultConfig);
        model.addObserver(this);
        canvas = new FractalGUICanvas(model.calcModel(defaultConfig));
        setupToolbar();
        getContentPane().add(canvas);
        setSize(800, 800);

        addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {

                System.out.println("Mouse clicked " + e.getX() + " " + e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e) {

                System.out.println("Mouse released " + e.getX() + " " + e.getY());
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }
        });
        addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {

                System.out.println("Mouse dragged " + e.getX() + " " + e.getY());
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });

        addComponentListener(new ComponentListener() {
            public void componentResized(ComponentEvent e) {
            	Rectangle r = getBounds();
				model.setCurrentConfig(new FractalGUIConfig(r.width, r.height, currentConfig.getMinReal(),
						currentConfig.getMaxReal(), currentConfig.getMaxImaginary(), currentConfig.getMinImaginary(),
						currentConfig.getMaxIterations(), currentConfig.getRadiusSquared(), currentConfig.getEndColor(), currentConfig.getEndColor()));         
            }

			@Override
			public void componentHidden(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentMoved(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentShown(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}
        });
        
        setVisible(true);

    }

    @Override
    public void update(Observable arg0, Object arg1) {

        canvas.setFactalData(model.calcModel());
        repaint();

    }

}
