import executionChains.ChainNode;
import executionChains.Chain;
import executionChains.SimpleChainNode;
import executionChains.chainExecutors.ChainExecutor;
import executionChains.chainExecutors.NodeNotFoundException;
import org.junit.Assert;
import org.junit.Test;

public class DefaultTest {

    @Test
    public void testSimple(){
        TestModel model = new TestModel();
        model.number = 0;
        Chain<TestModel> processer = new Chain<>();
        processer.pushNode(new SimpleChainNode<>(m -> m.number += 2));
        processer.pushNode(new SimpleChainNode<>(m -> m.number -= 1));
        processer.executeDefaultOrdered(model);
        Assert.assertEquals(model.number, 1);
    }

    @Test
    public void testStop(){
        TestModel model = new TestModel();
        model.number = 0;
        Chain<TestModel> processer = new Chain<>();
        processer.pushNode(new SimpleChainNode<>(m -> m.number += 10));

        ChainNode<TestModel> stoppingNode = new ChainNode<TestModel>() {
            @Override
            public void execute(TestModel testModel, ChainExecutor executor) {
                executor.stop();
            }
        };
        processer.pushNode(stoppingNode);

        processer.pushNode(new SimpleChainNode<>(m -> m.number += 5));
        processer.executeDefaultOrdered(model);
        Assert.assertEquals(model.number, 10);
    }

    @Test
    public void testGoTo(){
        TestModel model = new TestModel();
        model.number = 0;
        SimpleChainNode<TestModel> addingNode = new SimpleChainNode<>(m -> m.number += 2);
        ChainNode<TestModel> goToNode = new ChainNode<TestModel>() {
            @Override
            public void execute(TestModel testModel, ChainExecutor executor) {
                if (testModel.number < 10){
                    executor.goTo(0);
                }
            }
        };
        Chain<TestModel> processer = new Chain<>();
        processer.pushNode(addingNode);
        processer.pushNode(goToNode);
        processer.executeDefaultOrdered(model);
        Assert.assertEquals(model.number, 10);
    }

    @Test
    public void testGoToByObject(){
        TestModel model = new TestModel();
        model.number = 0;
        SimpleChainNode<TestModel> addingNode = new SimpleChainNode<>(m -> m.number += 2);
        ChainNode<TestModel> goToNode = new ChainNode<TestModel>() {
            @Override
            public void execute(TestModel testModel, ChainExecutor executor) {
                if (testModel.number < 10){
                    try {
                        executor.goTo(addingNode);
                    } catch (NodeNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Chain<TestModel> processer = new Chain<>();
        processer.pushNode(addingNode);
        processer.pushNode(goToNode);
        processer.executeDefaultOrdered(model);
        Assert.assertEquals(model.number, 10);
    }

    @Test
    public void testRestart(){
        TestModel model = new TestModel();
        model.number = 0;
        SimpleChainNode<TestModel> addingNode = new SimpleChainNode<>(m -> m.number++);
        ChainNode<TestModel> restartingNode = new ChainNode<TestModel>() {
            @Override
            public void execute(TestModel testModel, ChainExecutor executor) {
                if (testModel.number < 10){
                    executor.restart();
                }
            }
        };
        Chain<TestModel> processer = new Chain<>();
        processer.pushNode(addingNode);
        processer.pushNode(restartingNode);
        processer.executeDefaultOrdered(model);
        Assert.assertEquals(model.number, 10);
    }

    @Test
    public void testSkipToEnd(){
        TestModel model = new TestModel();
        model.number = 0;
        Chain<TestModel> processer = new Chain<>();
        ChainNode<TestModel> skippingNode = new ChainNode<TestModel>() {
            @Override
            public void execute(TestModel testModel, ChainExecutor executor) {
                executor.skipToEnd();
            }
        };
        processer.pushNode(skippingNode);
        processer.pushNode(new SimpleChainNode<>(m -> m.number++));
        processer.executeDefaultOrdered(model);
        Assert.assertEquals(model.number, 0);
    }

    @Test
    public void testSkipNode(){
        TestModel model = new TestModel();
        model.number = 0;
        Chain<TestModel> processer = new Chain<>();
        SimpleChainNode<TestModel> skippedNode = new SimpleChainNode<>(m -> m.number++);
        ChainNode<TestModel> skippingNode = new ChainNode<TestModel>() {
            @Override
            public void execute(TestModel testModel, ChainExecutor executor) {
                executor.skipNode(skippedNode);
            }
        };
        processer.pushNode(skippingNode);
        processer.pushNode(skippedNode);
        Assert.assertEquals(model.number, 0);
    }
}
