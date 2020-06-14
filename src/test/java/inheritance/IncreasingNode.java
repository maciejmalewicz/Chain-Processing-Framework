package inheritance;

import executionChains.ChainNode;
import executionChains.chainExecutors.ChainExecutor;

public class IncreasingNode extends ChainNode<BaseModel> {

    @Override
    public void execute(BaseModel model, ChainExecutor executor) {
        model.num1++;
    }
}
