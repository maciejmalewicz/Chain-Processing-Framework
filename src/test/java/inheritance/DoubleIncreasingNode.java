package inheritance;

import executionChains.ChainNode;

public class DoubleIncreasingNode extends ChainNode <ComplexModel> {
    @Override
    public void execute(ComplexModel model) {
        model.num1++;
        model.num2++;
    }
}
