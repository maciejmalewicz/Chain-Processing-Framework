import executionChains.Chain;
import executionChains.ChainNode;
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

    @Test
    public void testTripleLoop(){
        Chain<TestModel> chain = new Chain<>(
                new SimpleChainNode<>(m -> m.number++)
        );

        for (int i = 0; i < 100; i++){
            TestModel current = new TestModel();
            new Thread(){
                @Override
                public void run(){
                    chain.loopNTimes(current, 100);
                    if (current.number != 100){
                        System.out.println("!!!");
                    }
                }
            }.start();
        }
    }

    @Test
    public void testNewNodeDividedFeature(){
        ChainNode<TestModel> node = new SimpleChainNode<>(m -> m.number++);
        Chain<TestModel> chain1 = new Chain<>(
                node
        );
        Chain<TestModel> chain2 = new Chain<>(
                node
        );
        TestModel model1 = new TestModel();
        TestModel model2 = new TestModel();

        new Thread(){
            @Override
            public void run(){
                chain1.loopNTimes(model1, 10);
                Assert.assertEquals(model1.number, 10);
            }
        }.start();
        new Thread(){
            @Override
            public void run(){
                chain2.loopNTimes(model2, 10);
                Assert.assertEquals(model2.number, 10);
            }
        }.start();
    }

}
