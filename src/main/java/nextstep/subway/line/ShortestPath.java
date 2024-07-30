package nextstep.subway.line;

import nextstep.subway.station.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class ShortestPath {

    WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    int maxDistance;

    public ShortestPath(List<Section> sections) {
        initGraph(sections);
        initMaxDistance(sections);
    }

    private void initGraph(List<Section> sections) {
        sections.forEach(section -> {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        });
    }

    private void initMaxDistance(List<Section> sections) {
        maxDistance = sections.stream()
                .mapToInt(Section::getDistance)
                .max()
                .orElse(0);
    }

    public List<Station> getStations(Station start, Station end) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return dijkstraShortestPath.getPath(start, end)
                .getVertexList();
    }

    public int getDistance(Station start, Station end) {
        List<GraphPath<Station, DefaultWeightedEdge>> paths = new KShortestPaths<>(graph, maxDistance)
                .getPaths(start, end);
        return (int) paths.get(0)
                .getWeight();
    }
}
