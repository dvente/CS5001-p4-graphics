package fractalgraphics;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JFrame;

public class FractalGUIWindow extends JFrame {

    public static void main(String[] args) {

        FractalGUIView gui = new FractalGUIView();
        FractalGUIWindow window = new FractalGUIWindow(gui);

        //        FractalGUIModel model = new FractalGUIModel();
        //        FractalGUIController controller = new FractalGUIController();
        //        gui.printMandelbrotData();

    }

    public FractalGUIWindow() throws HeadlessException {
    }

    public FractalGUIWindow(GraphicsConfiguration gc) {
        super(gc);
        // TODO Auto-generated constructor stub
    }

    public FractalGUIWindow(String title) throws HeadlessException {
        super(title);
        // TODO Auto-generated constructor stub
    }

    public FractalGUIWindow(String title, GraphicsConfiguration gc) {
        super(title, gc);
        // TODO Auto-generated constructor stub
    }

    public FractalGUIWindow(FractalGUIView gui) {
        getContentPane().add(gui);
        setSize(800, 800);
        setVisible(true);

    }

}
