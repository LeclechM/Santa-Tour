import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Tour {
    private int totalDistance;
    private List<String> path;

    public int getTotalDistance() {
        return totalDistance;
    }


    public void increaseTotalDistance(Integer distance){
            totalDistance += distance;

    }


    public List<String> getPath() {
        return path;
    }

    public void addPath(String p){
        path.add(p);
    }

    public void setPath(List<String> path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return  path + " -> Distance = " + totalDistance;

    }

    public Tour(){
        totalDistance =0;
        path= new ArrayList<>();
    }

    public Tour clone(){
        Tour cloneTour = new Tour();
        cloneTour.totalDistance = this.totalDistance;
        cloneTour.path = this.path;
        return cloneTour;
    }
}
