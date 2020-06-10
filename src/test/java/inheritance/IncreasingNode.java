package inheritance;

import executionChains.ChainNode;

public class IncreasingNode extends ChainNode<BaseModel> {

    @Override
    public void execute(BaseModel model) {
        model.num1++;
    }


}
