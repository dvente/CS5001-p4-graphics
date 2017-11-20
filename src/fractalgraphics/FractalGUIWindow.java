package fractalgraphics;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

public class FractalGUIWindow extends JFrame {

    private FractalGUIView gui;
    private JMenuBar toolBar;
    private JButton resetButton;
    private JButton undoButton;
    private JButton redoButton;

    public static void main(String[] args) {

        FractalGUIWindow window = new FractalGUIWindow();
    }

    private void setupToolbar() {

        //        gui.setLayout(new FlowLayout());
        //        Container cp = new JPanel();
        //        cp.setLayout(new FlowLayout());
        //
        //        toolBar = new JToolBar();
        //        getContentPane().add(toolBar);
        //
        //        resetButton = new JButton("Reset");
        //        toolBar.add(resetButton);
        //
        //        undoButton = new JButton("Undo");
        //        toolBar.add(undoButton);
        //
        //        redoButton = new JButton("Redo");
        //        toolBar.add(redoButton);
        //        cp.add(toolBar);
        //        gui.add(cp);

        toolBar = new JMenuBar();
        getContentPane().add(toolBar);

        resetButton = new JButton("Reset");
        toolBar.add(resetButton);

        undoButton = new JButton("Undo");
        toolBar.add(undoButton);

        redoButton = new JButton("Redo");
        toolBar.add(redoButton);

        gui.add(toolBar, BorderLayout.SOUTH);

    }

    public FractalGUIWindow() {
        gui = new FractalGUIView();
        setupToolbar();
        getContentPane().add(gui);
        setSize(800, 800);
        setVisible(true);

    }

}
