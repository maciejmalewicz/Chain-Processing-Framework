package executionChains.chainExecutors;
import executionChains.ChainNode;
import executionChains.Chain;

import java.util.List;

public class DefaultOrderedExecutor <Model> extends ChainExecutor <Model> {

    public DefaultOrderedExecutor(Chain<Model> processer, Model model){
        super(processer, model);
    }

    @Override
    public void execute(List<ChainNode<? super Model>> nodes) {
        while (hasNext()){
            needsNavigation = true;
            ChainNode<? super Model> node = next();
            if (canBeExecuted(node)){
                node.execute(model);
            }
            if (needsNavigation){
                updateNextIndex(node);
            }
        }
    }


    @Override
    public boolean hasNext() {
        return nextIndex < nodes.size() && proceeds;
    }

    @Override
    protected int getDefaultNext() {
        return nextIndex+1;
    }

}
