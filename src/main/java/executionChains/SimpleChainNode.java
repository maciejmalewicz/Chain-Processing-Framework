package executionChains;

public class SimpleChainNode <Model> extends ChainNode<Model> {

    private ChainFunction<Model> function;

    public SimpleChainNode(ChainFunction<Model> function){
        this.function = function;
    }

    @Override
    public void execute(Model model) {
        function.execute(model);
    }
}
