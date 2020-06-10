package executionChains;


@FunctionalInterface
public interface ChainFunction<Model>{
    void execute(Model model);
}
