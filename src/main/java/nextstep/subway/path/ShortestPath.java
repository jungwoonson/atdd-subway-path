package nextstep.subway.path;

import nextstep.subway.line.Section;
import nextstep.subway.line.exception.NotAddedStationsToSectionException;
import nextstep.subway.line.exception.NotConnectedStationsException;
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
        GraphPath<Station, DefaultWeightedEdge> path = getPaths(start, end, dijkstraShortestPath);
        validatePath(path);
        return path.getVertexList();
    }

    private static GraphPath<Station, DefaultWeightedEdge> getPaths(Station start, Station end, DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath) {
        try {
            return dijkstraShortestPath.getPath(start, end);
        } catch (IllegalArgumentException exception) {
            throw handleNotAddedStationsToSectionException(exception, start, end);
        }
    }

    private static void validatePath(GraphPath<Station, DefaultWeightedEdge> path) {
        if (path == null) {
            throw new NotConnectedStationsException();
        }
    }

    public int getDistance(Station start, Station end) {
        List<GraphPath<Station, DefaultWeightedEdge>> paths = getPaths(start, end);
        validatePathsSize(paths);
        GraphPath<Station, DefaultWeightedEdge> shortestPath = paths.get(0);
        return (int) shortestPath.getWeight();
    }

    private static void validatePathsSize(List<GraphPath<Station, DefaultWeightedEdge>> paths) {
        if (paths.isEmpty()) {
            throw new NotConnectedStationsException();
        }
    }

    private List<GraphPath<Station, DefaultWeightedEdge>> getPaths(Station start, Station end) {
        try {
            return new KShortestPaths<>(graph, maxDistance).getPaths(start, end);
        } catch (IllegalArgumentException exception) {
            throw handleNotAddedStationsToSectionException(exception, start, end);
        }
    }

    private static RuntimeException handleNotAddedStationsToSectionException(IllegalArgumentException exception, Station start, Station end) {
        String message = exception.getMessage();
        if (message.contains("source") || message.contains("Graph must contain the start vertex")) {
            return new NotAddedStationsToSectionException(String.format("출발역(%s)", start.getName()));
        }
        if (message.contains("sink") || message.contains("Graph must contain the end vertex")) {
            return new NotAddedStationsToSectionException(String.format("도착역(%s)", end.getName()));
        }
        return new RuntimeException(message);
    }
}
