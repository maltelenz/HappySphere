package com.laserfountain.sensortrouble;

import android.graphics.Point;

public class MathExt {
    public static Point getClosestPointOnLine(Point linePoint1, Point linePoint2, Point point0) {
        int y0, y1, y2, x0, x1, x2;
        y0 = point0.y;
        x0 = point0.x;

        y1 = linePoint1.y;
        x1 = linePoint1.x;
        y2 = linePoint2.y;
        x2 = linePoint2.x;

        double subExpression = Math.abs((x0 - x1) * (x2 - x1) + (y0 - y1) * (y2 - y1))/(Math.pow(-x1+x2, 2) + Math.pow(-y1+y2, 2));
        float fsubExpression = (float) subExpression;
        Point p = new Point(
                Math.round(x1 + (x2 - x1) * fsubExpression),
                Math.round(y1 + (y2 - y1) * fsubExpression)
            );
        
        return p;
    }
}
