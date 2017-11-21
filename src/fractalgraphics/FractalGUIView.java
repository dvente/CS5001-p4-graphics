package fractalgraphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

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
    private JMenuItem resetButton;
    private JMenuItem undoButton;
    private JMenuItem redoButton;
    private FractalGUIConfig currentConfig = defaultConfig;
    private Stack<FractalGUIConfig> configHistory;
    private Stack<FractalGUIConfig> configHistoryUndone;

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

        resetButton = new JMenuItem("Reset");
        menuBar.add(resetButton);
        resetButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                //Store config for undo
                configHistory.push(currentConfig);
                //reset to default
                currentConfig = defaultConfig;
                //update model
                model.setCurrentConfig(defaultConfig);
                //reset window
                setSize(defaultConfig.getxResolution(), defaultConfig.getyResolution());

            }
        });

        undoButton = new JMenuItem("Undo");
        menuBar.add(undoButton);
        undoButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                //Store config for undo
                configHistory.push(currentConfig);
                //reset to default
                currentConfig = defaultConfig;
                //update model
                model.setCurrentConfig(defaultConfig);
                //reset window
                setSize(defaultConfig.getxResolution(), defaultConfig.getyResolution());

            }
        });

        redoButton = new JMenuItem("Redo");
        menuBar.add(redoButton);

        add(menuBar, BorderLayout.NORTH);

    }

    public FractalGUIView() {

        model = new FractalGUIModel(defaultConfig);
        model.addObserver(this);
        canvas = new FractalGUICanvas(model.calcModel(defaultConfig));
        configHistory = new Stack<FractalGUIConfig>();
        configHistory.push(defaultConfig);
        configHistoryUndone = new Stack<FractalGUIConfig>();
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

            @Override
            public void componentResized(ComponentEvent e) {

                Rectangle r = getBounds();
                model.setCurrentConfig(new FractalGUIConfig(r.width, r.height, currentConfig.getMinReal(),
                        currentConfig.getMaxReal(), currentConfig.getMaxImaginary(), currentConfig.getMinImaginary(),
                        currentConfig.getMaxIterations(), currentConfig.getRadiusSquared(), currentConfig.getEndColor(),
                        currentConfig.getEndColor()));
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

        //future gets erased
        //        configHistoryUndone.clear();
        canvas.setFactalData(model.calcModel());
        repaint();

    }

}
