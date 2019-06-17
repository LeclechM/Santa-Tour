import javax.swing.*;

public class SantaTour {

    public static void main(String[] args) {
        GraphGenerator graphGenerator = new GraphGenerator();
        graphGenerator.createGraph();
        JGraphXAdapterDemo applet = new JGraphXAdapterDemo();
        applet.setGraph(graphGenerator.getGraph());
        applet.init();

        JFrame frame = new JFrame();
        frame.getContentPane().add(applet);
        frame.setTitle("JGraphT Adapter to JGraphX Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
