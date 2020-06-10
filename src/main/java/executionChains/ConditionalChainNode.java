package executionChains;

import executionChains.ChainNode;

import java.util.function.Predicate;

public abstract class ConditionalChainNode<Model> extends ChainNode<Model> {

    private Predicate<Model> condition;
    public abstract void tryExecute(Model model);

    public ConditionalChainNode(Predicate<Model> predicate){
        condition = predicate;
    }

    @Override
    public void execute(Model model) {
        if (condition.test(model)){
            tryExecute(model);
        }
    }
}
