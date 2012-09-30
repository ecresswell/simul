package org.housered.simul.model.navigation.road.quadtree;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.quadtree.Key;
import com.vividsolutions.jts.util.Assert;

public class ExitEarlyNode extends ExitEarlyNodeBase
{
    public static ExitEarlyNode createNode(Envelope env)
    {
        Key key = new Key(env);
        ExitEarlyNode node = new ExitEarlyNode(key.getEnvelope(), key.getLevel());
        return node;
    }

    public static ExitEarlyNode createExpanded(ExitEarlyNode node, Envelope addEnv)
    {
        Envelope expandEnv = new Envelope(addEnv);
        if (node != null)
            expandEnv.expandToInclude(node.env);

        ExitEarlyNode largerNode = createNode(expandEnv);
        if (node != null)
            largerNode.insertNode(node);
        return largerNode;
    }

    private Envelope env;
    private Coordinate centre;
    private int level;

    public ExitEarlyNode(Envelope env, int level)
    {
        //this.parent = parent;
        this.env = env;
        this.level = level;
        centre = new Coordinate();
        centre.x = (env.getMinX() + env.getMaxX()) / 2;
        centre.y = (env.getMinY() + env.getMaxY()) / 2;
    }

    public Envelope getEnvelope()
    {
        return env;
    }

    protected boolean isSearchMatch(Envelope searchEnv)
    {
        return env.intersects(searchEnv);
    }

    /**
     * Returns the subquad containing the envelope. Creates the subquad if it does not already
     * exist.
     */
    public ExitEarlyNode getNode(Envelope searchEnv)
    {
        int subnodeIndex = getSubnodeIndex(searchEnv, centre);
        // if subquadIndex is -1 searchEnv is not contained in a subquad
        if (subnodeIndex != -1)
        {
            // create the quad if it does not exist
            ExitEarlyNode node = getSubnode(subnodeIndex);
            // recursively search the found/created quad
            return node.getNode(searchEnv);
        }
        else
        {
            return this;
        }
    }

    /**
     * Returns the smallest <i>existing</i> node containing the envelope.
     */
    public ExitEarlyNodeBase find(Envelope searchEnv)
    {
        int subnodeIndex = getSubnodeIndex(searchEnv, centre);
        if (subnodeIndex == -1)
            return this;
        if (subnode[subnodeIndex] != null)
        {
            // query lies in subquad, so search it
            ExitEarlyNode node = subnode[subnodeIndex];
            return node.find(searchEnv);
        }
        // no existing subquad, so return this one anyway
        return this;
    }

    void insertNode(ExitEarlyNode node)
    {
        Assert.isTrue(env == null || env.contains(node.env));
        //System.out.println(env);
        //System.out.println(quad.env);
        int index = getSubnodeIndex(node.env, centre);
        //System.out.println(index);
        if (node.level == level - 1)
        {
            subnode[index] = node;
            //System.out.println("inserted");
        }
        else
        {
            // the quad is not a direct child, so make a new child quad to contain it
            // and recursively insert the quad
            ExitEarlyNode childNode = createSubnode(index);
            childNode.insertNode(node);
            subnode[index] = childNode;
        }
    }

    /**
     * get the subquad for the index. If it doesn't exist, create it
     */
    private ExitEarlyNode getSubnode(int index)
    {
        if (subnode[index] == null)
        {
            subnode[index] = createSubnode(index);
        }
        return subnode[index];
    }

    private ExitEarlyNode createSubnode(int index)
    {
        // create a new subquad in the appropriate quadrant

        double minx = 0.0;
        double maxx = 0.0;
        double miny = 0.0;
        double maxy = 0.0;

        switch (index)
        {
        case 0:
            minx = env.getMinX();
            maxx = centre.x;
            miny = env.getMinY();
            maxy = centre.y;
            break;
        case 1:
            minx = centre.x;
            maxx = env.getMaxX();
            miny = env.getMinY();
            maxy = centre.y;
            break;
        case 2:
            minx = env.getMinX();
            maxx = centre.x;
            miny = centre.y;
            maxy = env.getMaxY();
            break;
        case 3:
            minx = centre.x;
            maxx = env.getMaxX();
            miny = centre.y;
            maxy = env.getMaxY();
            break;
        }
        Envelope sqEnv = new Envelope(minx, maxx, miny, maxy);
        ExitEarlyNode node = new ExitEarlyNode(sqEnv, level - 1);
        return node;
    }
}
