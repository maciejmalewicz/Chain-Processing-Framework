package inheritance;

import executionChains.ChainNode;
import executionChains.chainExecutors.ChainExecutor;

public class DoubleIncreasingNode extends ChainNode <ComplexModel> {
    @Override
    public void execute(ComplexModel model, ChainExecutor executor) {
        model.num1++;
        model.num2++;
    }
}
