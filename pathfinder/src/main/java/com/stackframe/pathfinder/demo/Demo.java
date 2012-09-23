// Copyright (C) 2002-2010 StackFrame, LLC http://www.stackframe.com/
// This software is provided under the GNU General Public License, version 2.
package com.stackframe.pathfinder.demo;

import com.stackframe.pathfinder.PathEvent;
import com.stackframe.pathfinder.PathFinder;
import com.stackframe.pathfinder.PathListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.AbstractAction;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

public class Demo extends JApplet {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private Map map;
    private MapView mapView;
    private final JButton startButton = new JButton("Find Path");
    private final JButton newButton = new JButton("Clear");
    private final JButton randomize = new JButton("Randomize");
    private Location start;
    private Location goal;
    private JComboBox algo;
    private static final String idle = "PathFinder idle.";
    private final JLabel status = new JLabel(idle);
    private long startTime;
    private JCheckBox showSteps = new JCheckBox("Show Steps");
    private static final int bounds = 50;
    private Future<List<Location>> futurePath;

    private void handleDone(final List<Location> path) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                long stopTime = System.currentTimeMillis();
                mapView.setPath(path);
                String message;
                if (path == null) {
                    message = new String("No path found in ");
                } else {
                    message = new String("Path of " + path.size() + " steps found in ");
                }

                message += (stopTime - startTime) + " milliseconds.";
                status.setText(message);
                mapView.setEnabled(true);
                newButton.setEnabled(true);
                randomize.setEnabled(true);
                algo.setEnabled(true);
                startButton.setText("Find Path");
            }

        });
    }

    private void connect(PathFinder<Location> pathFinder) {
        pathFinder.addPathListener(new PathListener<Location>() {

            public void considered(final PathEvent<Location> pathEvent) {
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {

                        public void run() {
                            if (showSteps.isSelected()) {
                                mapView.setPath(pathEvent.getPath());
                            }
                        }

                    });
                } catch (InterruptedException ie) {
                    // We expect to be interrupted if the computation was canceled.
                } catch (InvocationTargetException ite) {
                    // We do not expect to get an InvocationTargetException.
                    throw new AssertionError(ite);
                }
            }

        });
    }

    private void doNewSearch() {
        startButton.setText("Abort");
        randomize.setEnabled(false);
        algo.setEnabled(false);
        newButton.setEnabled(false);
        mapView.setEnabled(false);
        status.setText("PathFinder working ...");
        mapView.setPath(null);
        startTime = System.currentTimeMillis();
        futurePath = executor.submit(new Callable<List<Location>>() {

            public List<Location> call() {
                PathFinder<Location> pathfinder = (PathFinder<Location>)algo.getSelectedItem();
                final List<Location> path = pathfinder.findPath(map.getLocations(), start, Collections.singleton(goal));
                handleDone(path);
                return path;
            }

        });
    }

    public Demo() {
        map = new Map(bounds);
        getContentPane().setLayout(new BorderLayout());
        start = map.getLocation(0, 0);
        goal = map.getLocation(map.getXSize() - 1, map.getYSize() - 1);
        mapView = new MapView(map, start, goal);
        mapView.setBorder(new BevelBorder(BevelBorder.LOWERED));
        getContentPane().add(mapView, BorderLayout.CENTER);
        getContentPane().add(status, BorderLayout.SOUTH);
        JPanel controlBox = new JPanel();
        controlBox.add(startButton);
        controlBox.add(showSteps);
        getContentPane().add(controlBox, BorderLayout.NORTH);
        startButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                if (startButton.getText().equals("Find Path")) {
                    doNewSearch();
                } else {
                    PathFinder pathFinder = (PathFinder)algo.getSelectedItem();
                    pathFinder.cancel();
                    futurePath.cancel(true);
                }
            }

        });

        controlBox.add(newButton);
        controlBox.add(randomize);
        newButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                map = new Map(bounds);
                start = map.getLocation(0, 0);
                goal = map.getLocation(map.getXSize() - 1, map.getYSize() - 1);
                mapView.setStart(start);
                mapView.setGoal(goal);
                mapView.setPath(null);
                mapView.setMap(map);
            }

        });

        randomize.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                map = new Map(bounds);
                start = map.getLocation(0, 0);
                goal = map.getLocation(map.getXSize() - 1, map.getYSize() - 1);
                map.randomize();
                mapView.setStart(start);
                mapView.setGoal(goal);
                mapView.setMap(map);
                mapView.setPath(null);
            }

        });

        Vector<PathFinder<Location>> algorithms = new Vector<PathFinder<Location>>();
        ServiceLoader<PathFinder> pathFinders = ServiceLoader.load(PathFinder.class);
        for (PathFinder p : pathFinders) {
            addAlgorithm((PathFinder<Location>)p, algorithms);
        }

        algo = new JComboBox(algorithms);
        algo.setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                PathFinder pathFinder = (PathFinder)value;
                return super.getListCellRendererComponent(list, pathFinder.name(), index, isSelected, cellHasFocus);
            }

        });
        controlBox.add(algo);
    }

    private void addAlgorithm(PathFinder<Location> pathFinder, Vector<PathFinder<Location>> algorithms) {
        algorithms.add(pathFinder);
        connect(pathFinder);
    }

    public void loadMap() {
        String dir = System.getProperty("user.dir");
        JFileChooser chooser = new JFileChooser(dir);
        int result = chooser.showOpenDialog(getParent());
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                FileInputStream stream = new FileInputStream(file);
                ObjectInputStream oos = new ObjectInputStream(stream);
                map = (Map)oos.readObject();
                start = map.getLocation(0, 0);
                goal = map.getLocation(map.getXSize() - 1, map.getYSize() - 1);
                mapView.setPath(null);
                mapView.setGoal(goal);
                mapView.setStart(start);
                mapView.setMap(map);
            } catch (Exception e) {
                System.err.println("Could not load: " + e);
                e.printStackTrace();
            }
        }
    }

    public void saveMap() {
        String dir = System.getProperty("user.dir");
        JFileChooser chooser = new JFileChooser(dir);
        int result = chooser.showSaveDialog(getParent());
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                FileOutputStream os = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(os);
                oos.writeObject(map);
            } catch (Exception e) {
                System.err.println("Could not save: " + e);
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        final Demo demo = new Demo();
        JFrame frame = new JFrame("PathFinder");
        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);

        JMenuItem load = new JMenuItem("Open");
        load.setMnemonic(KeyEvent.VK_O);
        load.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, frame.getToolkit().getMenuShortcutKeyMask(), false));
        file.add(load);
        load.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                demo.loadMap();
            }

        });

        JMenuItem save = new JMenuItem("Save");
        save.setMnemonic(KeyEvent.VK_S);
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, frame.getToolkit().getMenuShortcutKeyMask(), false));
        file.add(save);
        save.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                demo.saveMap();
            }

        });

        JMenuItem exit = file.add(new AbstractAction("Exit") {

            public void actionPerformed(ActionEvent evt) {
                System.exit(0);
            }

        });
        exit.setMnemonic(KeyEvent.VK_X);
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, frame.getToolkit().getMenuShortcutKeyMask(), false));
        menuBar.add(file);
        demo.setJMenuBar(menuBar);
        frame.getContentPane().add(demo);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
