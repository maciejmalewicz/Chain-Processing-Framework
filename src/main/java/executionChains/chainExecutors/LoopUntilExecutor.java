package executionChains.chainExecutors;
import executionChains.Chain;
import java.util.function.Predicate;

public class LoopUntilExecutor<Model> extends LoopExecutor<Model> {

    private Predicate<Model> condition;

    public LoopUntilExecutor(Chain<Model> processer, Predicate<Model> condition, Model model) {
        super(processer, model);
        this.condition = condition;
    }

    @Override
    public boolean hasNext(){
        return super.hasNext() && condition.test(model);
    }
}
