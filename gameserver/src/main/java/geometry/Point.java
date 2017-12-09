package geometry;

/**
 * Template class for
 */
public class Point implements Collider{

    // fields
    private int x;
    private int y;
    public Point(int x, int y) {
               this.y = y;
               this.x = x;
           }
    public boolean isColliding(Collider other) {

                        return other instanceof Collider && this.equals(other);
            }


    // and methods

    /**
     * @param o - other object to check equality with
     * @return true if two points are equal and not null.
     */


    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        Point point = (Point) o;
        return x == point.x && y == point.y;
    }
}
