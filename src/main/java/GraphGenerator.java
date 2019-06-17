import com.google.common.graph.Graphs;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;


public class GraphGenerator {

    protected MutableValueGraph<String,Integer> santaTour = ValueGraphBuilder.undirected().allowsSelfLoops(false).build();
    protected Graph<String,Integer> santaTourDemo = new DefaultUndirectedWeightedGraph(Integer.class);

    public Graph<String,Integer> getGraph(){
        return santaTourDemo;
    }

    /**
     *  Create a graph from data-santa-tour.txt
     *  One for visualisation and one for logic 
     */
    public void createGraph() {
        try {
            Stream<String> lines = Files.lines(Paths.get("src\\main\\resources\\data-santa-tour.txt"));
            lines.forEach(s -> {
                int distance = Integer.valueOf(s.split("=")[1].replace(" ", ""));
                String beginTown = s.split("=")[0].split("to")[0].replace(" ", "");
                String endTown = s.split("=")[0].split("to")[1].replace(" ", "");
                santaTour.putEdgeValue(beginTown,endTown,distance);

                santaTourDemo.addVertex(beginTown);
                santaTourDemo.addVertex(endTown);
                santaTourDemo.addEdge(beginTown,endTown,distance);
            });
            lines.close();
        } catch (IOException io) {
            io.printStackTrace();
        }
        System.out.println("Best Path is : ");
        System.out.println(getAllPath().get(0).toString());
        System.out.println();
        getAllPath().stream().forEach(System.out::println);
    }


    /**
     *
     * @return List of all the shorten path
     *
     *  function which get all the shortest path starting from every nodes
     */
    public List<Tour> getAllPath(){
        List<Tour> tours= new ArrayList<>();
        santaTour.nodes().forEach( o -> {
            Tour tour = new Tour();
            tour.addPath( o);
            MutableValueGraph copySantaTour = Graphs.copyOf(santaTour);
            tours.add(getTourFrom( o,copySantaTour, tour));
        });
        return tours.stream().sorted(Comparator.comparing(Tour::getTotalDistance)).collect(Collectors.toList());
    }


    /**
     *
     * @param startTown
     * @param santaTourCopy
     * @param tour
     * @return shorten path from startTown
     *
     *  Recursive function that find the shortest path through all nodes only one time.
     */
    private Tour getTourFrom(String startTown, MutableValueGraph santaTourCopy, Tour tour){
        if(santaTourCopy.adjacentNodes(startTown).size() == 0){
            return tour;
        }else{
            Map<String, Integer> result =
                    (Map<String, Integer>) santaTourCopy.adjacentNodes(startTown).stream().collect(toMap(t -> t, o ->   santaTourCopy.edgeValue(startTown,o).get()));
            Map.Entry<String, Integer> bestNode = result
                    .entrySet()
                    .stream()
                    .sorted(comparingByValue())
                    .collect(
                            toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,
                                    LinkedHashMap::new)).entrySet().iterator().next();

            santaTourCopy.removeNode(startTown);
            tour.addPath(bestNode.getKey());
            tour.increaseTotalDistance(bestNode.getValue());
            return getTourFrom(bestNode.getKey(), santaTourCopy, tour);
        }
    }

}
