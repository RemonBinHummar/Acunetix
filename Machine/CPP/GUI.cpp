/*This is a C++ Program to implement Gift Wrapping algorithm to find convex hull in two dimensional space. In computational geometry, the gift wrapping algorithm is an algorithm for computing the convex hull of a given set of points. In the two-dimensional case the algorithm is also known as Jarvis march, after R. A. Jarvis, who published it in 1973; it has O(nh) time complexity, where n is the number of points and h is the number of points on the convex hull. Its real-life performance compared with other convex hull algorithms is favorable when n is small or h is expected to be very small with respect to n. In general cases the algorithm is outperformed by many others.*/

// A C++ program to find convex hull of a set of points
// Refer http://www.geeksforgeeks.org/check-if-two-given-line-segments-intersect/
// for explanation of orientation()
#include <iostream>
using namespace std;

// Define Infinite (Using INT_MAX caused overflow problems)
#define INF 10000

struct Point
{
    int x;
    int y;
};

// To find orientation of ordered triplet (p, q, r).
// The function returns following values
// 0 --> p, q and r are colinear
// 1 --> Clockwise
// 2 --> Counterclockwise
int orientation(Point p, Point q, Point r)
{
    int val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
    if (val == 0)
        return 0; // colinear
    return (val > 0) ? 1 : 2; // clock or counterclock wise
}

// Prints convex hull of a set of n points.
void convexHull(Point points[], int n)
{
    // There must be at least 3 points
    if (n < 3)
        return;
    // Initialize Result
    int next[n];
    for (int i = 0; i < n; i++)
        next[i] = -1;
    // Find the leftmost point
    int l = 0;
    for (int i = 1; i < n; i++)
        if (points[i].x < points[l].x)
            l = i;
    // Start from leftmost point, keep moving counterclockwise
    // until reach the start point again
    int p = l, q;
    do
        {
            // Search for a point 'q' such that orientation(p, i, q) is
            // counterclockwise for all points 'i'
            q = (p + 1) % n;
            for (int i = 0; i < n; i++)
                if (orientation(points[p], points[i], points[q]) == 2)
                    q = i;
            next[p] = q; // Add q to result as a next point of p
            p = q; // Set p as q for next iteration
        }
    while (p != l);
    // Print Result
    for (int i = 0; i < n; i++)
        {
            if (next[i] != -1)
                cout << "(" << points[i].x << ", " << points[i].y << ")\n";
        }
}

// Driver program to test above functions
int main()
{
    Point points[] = { { 0, 3 }, { 2, 2 }, { 1, 1 }, { 2, 1 }, { 3, 0 },
        { 0, 0 }, { 3, 3 }
    };
    cout << "The points in the convex hull are: ";
    int n = sizeof(points) / sizeof(points[0]);
    convexHull(points, n);
    return 0;
}
