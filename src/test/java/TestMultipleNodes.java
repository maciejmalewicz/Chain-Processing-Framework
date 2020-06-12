import executionChains.Chain;
import executionChains.ChainNode;
import executionChains.SimpleChainNode;
import org.junit.Test;

public class TestMultipleNodes {

    @Test
    public void testNodeAddedTwice(){
        ChainNode<TestModel> node = new SimpleChainNode<TestModel>(m -> m.number++);
        Chain<TestModel> chain = new Chain<>();
        chain.pushNode(node);
        chain.pushNode(node);
        TestModel model = new TestModel();
        chain.executeDefaultOrdered(model);
        System.out.println();
    }
}
