package executionChains.chainExecutors;

import executionChains.Chain;

public class LoopExecutor<Model> extends DefaultOrderedExecutor<Model> {

    public LoopExecutor(Chain<Model> processer, Model model){
        super(processer, model);
    }

    @Override
    public boolean hasNext() {
        return proceeds;
    }

    @Override
    protected int getDefaultNext() {
        if (nextIndex < nodes.size()-1){
            return nextIndex+1;
        } else {
            return 0;
        }
    }

    @Override
    public void skipToEnd() {
        setNextIndex(0);
    }

}
