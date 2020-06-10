import executionChains.Chain;
import executionChains.SimpleChainNode;
import org.junit.Assert;
import org.junit.Test;

public class TestMultithreaded {

    @Test
    public void multithreaded(){
        Chain<TestModel> chain = new Chain<>(
                new SimpleChainNode<>(m -> m.number++),
                new SimpleChainNode<>(m -> m.number += 2)
        );
        for (int i = 0; i < 100; i++){
            new Thread(){
                @Override
                public void run() {
                    TestModel model = new TestModel();
                    chain.executeDefaultOrdered(model);
                    Assert.assertEquals(model.number, 3);
                }
            }.start();
        }

    }
}
