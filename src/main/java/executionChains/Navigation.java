package executionChains;

import java.util.function.Predicate;

public class Navigation<Model> {

    private ChainNode<? super Model> from;
    private ChainNode<? super Model> to;
    private Predicate<Model> predicate;

    public Navigation(ChainNode<? super Model> from, ChainNode<? super Model> to){
        this.from = from;
        this.to = to;
        predicate = m -> true;
    }

    public Navigation(ChainNode<? super Model> from, ChainNode<? super Model> to, Predicate<Model> condition){
        this.from = from;
        this.to = to;
        predicate = condition;
    }

    public boolean canBeUsed(Model model){
        return predicate.test(model);
    }

    public ChainNode<? super Model> getFrom() {
        return from;
    }

    public ChainNode<? super Model> getTo() {
        return to;
    }
}
