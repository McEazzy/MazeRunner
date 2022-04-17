
/**
 * Purpose: interface for each maze square encapsulated with feature(s) - from decorator pattern
 * Name: Minh Vu
 * Last modified date: 14/04/2022
 */
import java.util.List;

@SuppressWarnings("PMD.NoPackage")
public interface Square
{
    //method signature:
    public List<Feature> getFeatures();
    public void addFeature(Feature feature);
    public boolean removeFeature(Feature feature);
    public boolean hasStartP();
    public void setStartP(boolean startP);
    public boolean hasEndP();
    public void setEndP(boolean endP);    
}
