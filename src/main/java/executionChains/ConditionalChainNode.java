package executionChains;

import executionChains.ChainNode;
import executionChains.chainExecutors.ChainExecutor;

import java.util.function.Predicate;

public abstract class ConditionalChainNode<Model> extends ChainNode<Model> {

    private Predicate<Model> condition;
    public abstract void tryExecute(Model model, ChainExecutor executor);

    public ConditionalChainNode(Predicate<Model> predicate){
        condition = predicate;
    }

    @Override
    public void execute(Model model, ChainExecutor executor) {
        if (condition.test(model)){
            tryExecute(model, executor);
        }
    }
}
