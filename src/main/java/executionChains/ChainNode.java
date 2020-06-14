package executionChains;

import executionChains.chainExecutors.ChainExecutor;
import executionChains.misc.IdManager;

import java.util.Objects;

public abstract class ChainNode <Model>  {

    //private ChainExecutor<? extends Model> executor;
    int chainNodeId = IdManager.generate();

//    void setExecutor(ChainExecutor<? extends Model> executor) {
//        this.executor = executor;
//    }

    public abstract void execute(Model model, ChainExecutor executor);

    @Override
    public String toString(){
        return this.getClass().toString();
    }

//    @Override
//    public void stop() {
//        executor.stop();
//    }
//
//    @Override
//    public void goTo(int index) throws ArrayIndexOutOfBoundsException {
//        executor.goTo(index);
//    }
//
//    @Override
//    public void goTo(ChainNode node) throws NodeNotFoundException {
//        executor.goTo(node);
//    }
//
//    @Override
//    public void restart() {
//        executor.restart();
//    }
//
//    @Override
//    public void skipToEnd() {
//        executor.skipToEnd();
//    }
//
//    @Override
//    public void skipNode(ChainNode node) {
//        executor.skipNode(node);
//    }

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
