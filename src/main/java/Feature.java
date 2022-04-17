
/**
 * Purpose: an abstract class inherit from Square interface to add feature(s) to a maze square (decorator pattern)
 * Name: Minh Vu
 * Last modified date: 14/04/2022
 */
import java.util.List;

@SuppressWarnings("PMD.NoPackage")
public abstract class Feature implements Square
{
    //Class-field:
    protected Square next;

    //Constructor:
    public Feature(Square next)
    {
        this.next = next;
        //append the current feature to the list of features for the current square
        addFeature(this);
    }

    //Accessor:
    @Override
    public List<Feature> getFeatures()
    {
        return next.getFeatures();
    }

    @Override
    public boolean hasStartP()
    {
        return next.hasStartP();
    }

    @Override
    public boolean hasEndP()
    {
        return next.hasEndP();
    }

    //Mutators:
    @Override
    public void setStartP(boolean startP)
    {
        next.setStartP(startP);
    }

    @Override
    public void setEndP(boolean endP)
    {
        next.setEndP(endP);
    }

    @Override
    public void addFeature(Feature feature)
    {
        next.addFeature(feature);
    }

    @Override
    public boolean removeFeature(Feature feature)
    {
        return next.removeFeature(feature);
    }
}
