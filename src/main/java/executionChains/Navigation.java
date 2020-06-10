package executionChains;

import java.util.function.Predicate;

public class Navigation<Model> {

    private ChainNode<Model> from;
    private ChainNode<Model> to;
    private Predicate<Model> predicate;

    public Navigation(ChainNode<Model> from, ChainNode<Model> to){
        this.from = from;
        this.to = to;
        predicate = m -> true;
    }

    public Navigation(ChainNode<Model> from, ChainNode<Model> to, Predicate<Model> condition){
        this.from = from;
        this.to = to;
        predicate = condition;
    }

    public boolean canBeUsed(Model model){
        return predicate.test(model);
    }

    public ChainNode<Model> getFrom() {
        return from;
    }

    public ChainNode<Model> getTo() {
        return to;
    }
}
