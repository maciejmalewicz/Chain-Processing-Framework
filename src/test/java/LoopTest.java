import executionChains.ChainNode;
import executionChains.Chain;
import executionChains.SimpleChainNode;
import org.junit.Assert;
import org.junit.Test;

public class LoopTest {

    @Test
    public void testLoops(){
        TestModel testModel = new TestModel();
        testModel.number = 0;

        Chain<TestModel> processer = new Chain<>();
        ChainNode<TestModel> chainNode = new ChainNode<TestModel>() {
            @Override
            public void execute(TestModel model) {
                model.number++;
                if (model.number >= 10){
                    stop();
                }
            }
        };
        processer.pushNode(chainNode);
        processer.loop(testModel);
        Assert.assertEquals(testModel.number, 10);
    }


    @Test
    public void testLoopN(){
        TestModel model = new TestModel();
        model.number = 0;
        Chain<TestModel> processer = new Chain<>();
        processer.pushNode(new SimpleChainNode<>(m -> m.number++));
        processer.loopNTimes(model, 10);
        Assert.assertEquals(model.number, 10);
    }

    @Test
    public void testLoopWhile(){
        TestModel model = new TestModel();
        model.number = 0;
        Chain<TestModel> processer = new Chain<>();
        processer.pushNode(new SimpleChainNode<>(m -> m.number++));
        processer.loopWhile(model, m -> m.number < 10);
        Assert.assertEquals(model.number, 10);
    }

    @Test
    public void testLoopNSkipping(){
        TestModel model = new TestModel();
        model.number = 0;
        Chain<TestModel> processer = new Chain<>();
        ChainNode<TestModel> skippingNode = new ChainNode<TestModel>() {
            @Override
            public void execute(TestModel testModel) {
                skipToEnd();
            }
        };
        processer.pushNode(skippingNode);
        processer.pushNode(new SimpleChainNode<>(m -> m.number++));
        processer.loopNTimes(model, 5);
        Assert.assertEquals(model.number, 0);
    }
}
