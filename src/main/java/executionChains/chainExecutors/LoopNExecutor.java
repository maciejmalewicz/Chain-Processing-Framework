package executionChains.chainExecutors;

import executionChains.ChainNode;
import executionChains.Chain;

import java.util.List;

public class LoopNExecutor<Model> extends LoopExecutor<Model>{

    private int loops;
    private int timesExecuted = 0;

    public LoopNExecutor(Chain<Model> processer, int loops, Model model) {
        super(processer, model);
        this.loops = loops;
    }

    @Override
    public boolean hasNext() {
        return super.hasNext() && !mustStop();
    }

    private boolean mustStop(){
        return timesExecuted >= loops;
    }

    @Override
    public void execute(List<ChainNode<? super Model>> chainNodes) {
        while (hasNext()){
            needsNavigation = true;
            ChainNode<? super Model> node = next();
            if (canBeExecuted(node)){
                node.execute(model, this);
            }
            if (needsNavigation){
                updateNextIndex(node);
            }
            if (nextIndex == 0){
                timesExecuted++;
            }
        }
    }


    @Override
    public void skipToEnd() {
        setNextIndex(0);
        timesExecuted++;
    }
}
