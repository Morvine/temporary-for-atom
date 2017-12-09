package geometry;

public class Bar implements Collider {

    // fields
    public int x1;
    public int y1;
    public int x2;
    public int y2;

    public Bar(int x1, int y1, int x2, int y2) {
        
            if (x1 < x2) {
                this.x1 = x1;
                this.x2 = x2;
            } else {
                this.x1 = x2;
                this.x2 = x1;
            }
        if (y1 < y2) {
            this.y1 = y1;
            this.y2 = y2;
        } else {
            this.y1 = y2;
            this.y2 = y1;

        }
    }

    public boolean isColliding(Collider other) {

        if (other instanceof Bar)
                        return other instanceof Collider && (this.equals(other) || this.intersects(other));
               else if (other instanceof Point)
                        return other instanceof Collider && this.pointOnBar(other);
                else
                    return false;
    }


    // and methods

    /**
     * @param o1 - other object to check equality with
     * @return true if two points are equal and not null.
     */

    @Override


    public boolean equals(Object o1) {
        if (this == o1) return true;
        if (o1 == null || getClass() != o1.getClass()) return false;


        Bar bar = (Bar) o1;
        return x1 == bar.x1 && y1 == bar.y1 && x2 == bar.x2 && y2 == bar.y2;
    }
    public boolean pointOnBar(Object o1)
    {
        return true;
        /*Point point =(Point) o1;
        return x1<=point.x && point.x<=x2 && y1<=point.y && point.y<=y2;*/
    }
    public boolean intersects (Object o1)
    {
        Bar bar = (Bar) o1;
        return !((x1 > bar.x2 || y1 > bar.y2) || (x2 < bar.x1 || y2 < bar.y1));
    }
}
