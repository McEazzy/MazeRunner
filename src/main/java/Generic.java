
/**
 * Purpose: holds an empty, or a shell/basic maze square to be equipped with features or no feature (decorator pattern)
 * Name: Minh Vu
 * Last modified date: 14/04/2022
 */
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("PMD.NoPackage")
public class Generic implements Square 
{
    //Class-fields:
    private List<Feature> features;
    private boolean startP;
    private boolean endP;
    
    //Constructor:
    public Generic()
    {
        features = new ArrayList<>();
        startP = false;
        endP = false;
    }

    //Accessors:
    @Override
    public List<Feature> getFeatures()
    {
        return features;
    }

    @Override
    public boolean hasStartP()
    {
        return startP;
    }

    @Override
    public boolean hasEndP()
    {
        return endP;
    }

    //Mutator:
    @Override
    public void addFeature(Feature feature)
    {
        features.add(feature);
    }

    @Override
    public boolean removeFeature(Feature feature)
    {
        return features.remove(feature);
    }

    @Override
    public void setStartP(boolean startP)
    {
        this.startP = startP;
    }

    @Override
    public void setEndP(boolean endP)
    {
        this.endP = endP;
    }
}
