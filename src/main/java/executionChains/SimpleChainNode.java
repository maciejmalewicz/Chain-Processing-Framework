package executionChains;

import executionChains.chainExecutors.ChainExecutor;

public class SimpleChainNode <Model> extends ChainNode<Model> {

    private ChainFunction<Model> function;

    public SimpleChainNode(ChainFunction<Model> function){
        this.function = function;
    }

    @Override
    public void execute(Model model, ChainExecutor executor) {
        function.execute(model);
    }
}
