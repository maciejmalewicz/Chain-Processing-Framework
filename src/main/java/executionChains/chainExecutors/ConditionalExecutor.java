package executionChains.chainExecutors;

import executionChains.ChainNode;
import executionChains.Chain;

import java.util.List;
import java.util.function.Predicate;

public class ConditionalExecutor<Model> extends DefaultOrderedExecutor<Model>{

    private Predicate<Model> condition;

    public ConditionalExecutor(Chain<Model> processer, Predicate<Model> condition, Model model) {
        super(processer, model);
        this.condition = condition;
    }

    @Override
    public void execute(List<ChainNode<? super Model>> chainNodes) {
        super.execute(chainNodes);
    }

    @Override
    public boolean hasNext() {
        return super.hasNext() && isConditionSatisfied(model);
    }

    private boolean isConditionSatisfied(Model model){
        return condition.test(model);
    }
}
