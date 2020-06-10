import executionChains.Chain;
import executionChains.SimpleChainNode;
import org.junit.Assert;
import org.junit.Test;

import java.util.function.Predicate;

public class ConditionalTest {

    @Test
    public void testConditional(){
        Chain<TestModel> processer = new Chain<>();
        Predicate<TestModel> condition = m -> m.number < 5;
        TestModel model = new TestModel();
        model.number = 0;
        processer.pushNode(new SimpleChainNode<>(m -> m.number += 2));
        processer.pushNode(new SimpleChainNode<>(m -> m.number += 2));
        processer.pushNode(new SimpleChainNode<>(m -> m.number += 2));
        processer.pushNode(new SimpleChainNode<>(m -> m.number += 2));
        processer.executeWhile(model, condition);
        Assert.assertEquals(model.number, 6);
    }
}
