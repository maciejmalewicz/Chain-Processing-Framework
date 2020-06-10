package executionChains.misc;
import java.util.function.Predicate;

public interface ChainExecutingFunctions<Model> {
    void executeDefaultOrdered(Model model);
    void executeWhile(Model model, Predicate<Model> condition);
    void loop(Model model);
    void loopWhile(Model model, Predicate<Model> condition);
    void loopNTimes(Model model, int loops);
}
