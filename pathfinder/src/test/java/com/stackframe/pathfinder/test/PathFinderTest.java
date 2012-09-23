// Copyright (C) 2002-2010 StackFrame, LLC http://www.stackframe.com/
// This software is provided under the GNU General Public License, version 2.
package com.stackframe.pathfinder.test;

import com.stackframe.pathfinder.AStar;
import com.stackframe.pathfinder.DepthFirstSearch;
import com.stackframe.pathfinder.Dijkstra;
import com.stackframe.pathfinder.Node;
import com.stackframe.pathfinder.PathFinder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test code for the PathFinder library.
 *
 * @author Gene McCulley
 */
public class PathFinderTest {

    public PathFinderTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    private static class TestNode implements Node<TestNode> {

        private final Map<TestNode, Double> neighbors = new HashMap<TestNode, Double>();
        private final String name;

        public TestNode(String name) {
            this.name = name;
        }

        public void addNeighbor(TestNode neighbor) {
            neighbors.put(neighbor, 1.0);
        }

        public void addNeighbor(TestNode neighbor, Double weight) {
            neighbors.put(neighbor, weight);
        }

        public void addSymmetricalNeighbor(TestNode neighbor, Double weight) {
            neighbors.put(neighbor, weight);
            neighbor.addNeighbor(this, weight);
        }

        public Iterable<TestNode> neighbors() {
            return neighbors.keySet();
        }

        public double pathCostEstimate(TestNode goal) {
            return 0;
        }

        public double traverseCost(TestNode dest) {
            return neighbors.get(dest);
        }

        @Override
        public String toString() {
            return name;
        }

    }

    private void simple(PathFinder pathFinder) {
        TestNode a = new TestNode("a");
        TestNode b = new TestNode("b");
        List<TestNode> graph = new ArrayList<TestNode>();
        graph.add(a);
        graph.add(b);
        a.addNeighbor(b);
        List<TestNode> path = pathFinder.findPath(graph, a, Collections.singleton(b));
        assertEquals(2, path.size());
        assertEquals(a, path.get(0));
        assertEquals(b, path.get(1));
    }

    @Test
    public void simpleAStar() {
        simple(new AStar());
    }

    @Test
    public void simpleDijkstra() {
        simple(new Dijkstra());
    }

    @Test
    public void simpleDepthFirstSearch() {
        simple(new DepthFirstSearch());
    }

    private void longPath(PathFinder pathFinder) {
        TestNode a = new TestNode("a");
        TestNode b = new TestNode("b");
        TestNode c = new TestNode("c");
        TestNode d = new TestNode("d");
        TestNode e = new TestNode("e");
        TestNode f = new TestNode("f");
        TestNode g = new TestNode("g");
        List<TestNode> graph = new ArrayList<TestNode>();
        graph.add(a);
        graph.add(b);
        graph.add(c);
        graph.add(d);
        graph.add(e);
        graph.add(f);
        graph.add(g);
        a.addNeighbor(b);
        b.addNeighbor(c);
        c.addNeighbor(d);
        d.addNeighbor(e);
        e.addNeighbor(f);
        f.addNeighbor(g);
        List<TestNode> path = pathFinder.findPath(graph, a, Collections.singleton(g));
        assertEquals(7, path.size());
        assertEquals(a, path.get(0));
        assertEquals(b, path.get(1));
        assertEquals(c, path.get(2));
        assertEquals(d, path.get(3));
        assertEquals(e, path.get(4));
        assertEquals(f, path.get(5));
        assertEquals(g, path.get(6));
    }

    @Test
    public void longPathAStar() {
        longPath(new AStar());
    }

    @Test
    public void longPathDijkstra() {
        longPath(new Dijkstra());
    }

    @Test
    public void longPathDepthFirstSearch() {
        longPath(new DepthFirstSearch());
    }

    private void shortcut(PathFinder pathFinder) {
        TestNode a = new TestNode("a");
        TestNode b = new TestNode("b");
        TestNode c = new TestNode("c");
        TestNode d = new TestNode("d");
        TestNode e = new TestNode("e");
        TestNode f = new TestNode("f");
        TestNode g = new TestNode("g");
        List<TestNode> graph = new ArrayList<TestNode>();
        graph.add(a);
        graph.add(b);
        graph.add(c);
        graph.add(d);
        graph.add(e);
        graph.add(f);
        graph.add(g);
        a.addNeighbor(b);
        b.addNeighbor(c);
        c.addNeighbor(d);
        d.addNeighbor(e);
        e.addNeighbor(f);
        f.addNeighbor(g);
        b.addNeighbor(f);
        List<TestNode> path = pathFinder.findPath(graph, a, Collections.singleton(g));
        assertEquals(4, path.size());
        assertEquals(a, path.get(0));
        assertEquals(b, path.get(1));
        assertEquals(f, path.get(2));
        assertEquals(g, path.get(3));
    }

    @Test
    public void shortcutAStar() {
        shortcut(new AStar());
    }

    @Test
    public void shortcutDijkstra() {
        shortcut(new Dijkstra());
    }

    private void noPath(PathFinder pathFinder) {
        TestNode a = new TestNode("a");
        TestNode b = new TestNode("b");
        TestNode c = new TestNode("c");
        TestNode d = new TestNode("d");
        TestNode e = new TestNode("e");
        TestNode f = new TestNode("f");
        TestNode g = new TestNode("g");
        a.addNeighbor(b);
        b.addNeighbor(c);
        c.addNeighbor(d);
        d.addNeighbor(e);
        e.addNeighbor(f);
        f.addNeighbor(g);

        TestNode h = new TestNode("h");
        TestNode i = new TestNode("i");
        TestNode j = new TestNode("j");
        TestNode k = new TestNode("k");
        TestNode l = new TestNode("l");
        TestNode m = new TestNode("m");
        TestNode n = new TestNode("n");
        h.addNeighbor(i);
        i.addNeighbor(j);
        j.addNeighbor(k);
        l.addNeighbor(m);
        m.addNeighbor(n);

        List<TestNode> graph = new ArrayList<TestNode>();
        graph.add(a);
        graph.add(b);
        graph.add(c);
        graph.add(d);
        graph.add(e);
        graph.add(f);
        graph.add(g);
        graph.add(h);
        graph.add(i);
        graph.add(j);
        graph.add(k);
        graph.add(l);
        graph.add(m);
        graph.add(n);

        List<TestNode> path = pathFinder.findPath(graph, a, Collections.singleton(n));
        assertNull(path);
    }

    @Test
    public void noPathAStar() {
        noPath(new AStar());
    }

    @Test
    public void noPathDijkstra() {
        noPath(new Dijkstra());
    }

    @Test
    public void noPathDepthFirstSearch() {
        noPath(new DepthFirstSearch());
    }

    // An example taken from http://en.wikipedia.org/wiki/Dijkstra's_algorithm
    private void testWeighted(PathFinder pathFinder) {
        TestNode n1 = new TestNode("1");
        TestNode n2 = new TestNode("2");
        TestNode n3 = new TestNode("3");
        TestNode n4 = new TestNode("4");
        TestNode n5 = new TestNode("5");
        TestNode n6 = new TestNode("6");
        List<TestNode> graph = new ArrayList<TestNode>();
        graph.add(n1);
        graph.add(n2);
        graph.add(n3);
        graph.add(n4);
        graph.add(n5);
        graph.add(n6);
        n1.addSymmetricalNeighbor(n2, 7.0);
        n1.addSymmetricalNeighbor(n3, 9.0);
        n1.addSymmetricalNeighbor(n6, 14.0);
        n2.addSymmetricalNeighbor(n3, 10.0);
        n2.addSymmetricalNeighbor(n4, 15.0);
        n3.addSymmetricalNeighbor(n6, 2.0);
        n3.addSymmetricalNeighbor(n4, 11.0);
        n4.addSymmetricalNeighbor(n5, 6.0);
        n5.addSymmetricalNeighbor(n6, 9.0);
        List<TestNode> path = pathFinder.findPath(graph, n1, Collections.singleton(n4));
        assertEquals(3, path.size());
        assertEquals(n1, path.get(0));
        assertEquals(n3, path.get(1));
        assertEquals(n4, path.get(2));

        path = pathFinder.findPath(graph, n1, Collections.singleton(n5));
        assertEquals(4, path.size());
        assertEquals(n1, path.get(0));
        assertEquals(n3, path.get(1));
        assertEquals(n6, path.get(2));
        assertEquals(n5, path.get(3));
    }

    @Test
    public void testWeightedDijkstra() {
        testWeighted(new Dijkstra());
    }

    @Test
    public void testWeightedAStar() {
        testWeighted(new AStar());
    }

}
