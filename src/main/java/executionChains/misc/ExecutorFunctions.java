package executionChains.misc;

import executionChains.ChainNode;
import executionChains.chainExecutors.NodeNotFoundException;

import java.util.List;

public interface ExecutorFunctions {
    void stop();
    void goTo(int index) throws ArrayIndexOutOfBoundsException;
    void goTo(ChainNode node) throws NodeNotFoundException;
    void restart();
    void skipToEnd();
    void skipNode(ChainNode node);
    default void skipNodes(List<ChainNode> nodes){
        nodes.forEach(this::skipNode);
    }
}
