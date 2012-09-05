package org.housered.simul.model.navigation;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.housered.simul.model.location.Vector;

import straightedge.geom.KPolygon;
import straightedge.geom.path.PathBlockingObstacle;
import straightedge.geom.path.PathBlockingObstacleImpl;

public class RectangleInverseUtility
{
    public static ArrayList<PathBlockingObstacle> createObstacles(double worldWidth, double worldHeight,
            List<Rectangle2D.Double> rects)
    {
        ArrayList<PathBlockingObstacle> obstacles = new ArrayList<PathBlockingObstacle>();

        for (Rectangle2D.Double r : inverseRectangles(worldWidth, worldHeight, rects))
        {
            Vector topLeft = new Vector(r.getMinX(), r.getMinY());
            Vector topRight = new Vector(r.getMaxX(), r.getMinY());
            Vector bottomRight = new Vector(r.getMaxX(), r.getMaxY());
            Vector bottomLeft = new Vector(r.getMinX(), r.getMaxY());

            KPolygon poly = new KPolygon(topLeft, topRight, bottomRight, bottomLeft);
            PathBlockingObstacle o = PathBlockingObstacleImpl.createObstacleFromInnerPolygon(poly);
            obstacles.add(o);
        }

        return obstacles;
    }

    public static List<Rectangle2D.Double> inverseRectangles(double worldWidth, double worldHeight,
            List<Rectangle2D.Double> rects)
    {
        List<Rectangle2D.Double> outputs = new ArrayList<Rectangle2D.Double>();
        outputs.add(new Rectangle2D.Double(0, 0, worldWidth, worldHeight));

        for (Rectangle2D.Double r : rects)
        {
            List<Rectangle2D.Double> newOutputs = new ArrayList<Rectangle2D.Double>();

            for (Rectangle2D.Double output : outputs)
                newOutputs.addAll(slice(output, r));
            outputs = newOutputs;
        }

        return outputs;
    }

    private static List<Rectangle2D.Double> slice(Rectangle2D.Double r, Rectangle2D.Double mask)
    {
        List<Rectangle2D.Double> rects = new ArrayList<Rectangle2D.Double>();

        Rectangle2D.Double intersection = new Rectangle2D.Double();
        Rectangle2D.Double.intersect(r, mask, intersection);

        if (!intersection.isEmpty())
        {
            rects.add(new Rectangle2D.Double(r.x, r.y, r.width, intersection.y - r.y));
            rects.add(new Rectangle2D.Double(r.x, intersection.y + intersection.height, r.width, (r.y + r.height)
                    - (intersection.y + intersection.height)));
            rects.add(new Rectangle2D.Double(r.x, intersection.y, intersection.x - r.x, intersection.height));
            rects.add(new Rectangle2D.Double(intersection.x + intersection.width, intersection.y, (r.x + r.width)
                    - (intersection.x + intersection.width), intersection.height));

            for (Iterator<Rectangle2D.Double> iter = rects.iterator(); iter.hasNext();)
                if (iter.next().isEmpty())
                    iter.remove();
        }
        else
            rects.add(r);

        return rects;
    }
}
