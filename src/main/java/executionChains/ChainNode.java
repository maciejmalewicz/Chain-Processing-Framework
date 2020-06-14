package executionChains;

import executionChains.chainExecutors.ChainExecutor;
import executionChains.misc.IdManager;

import java.util.Objects;

public abstract class ChainNode <Model>  {

    int chainNodeId = IdManager.generate();
    public abstract void execute(Model model, ChainExecutor executor);

    @Override
    public String toString(){
        return this.getClass().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChainNode<?> chainNode = (ChainNode<?>) o;
        return chainNodeId == chainNode.chainNodeId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chainNodeId);
    }
}
