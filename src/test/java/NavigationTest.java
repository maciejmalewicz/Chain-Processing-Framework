import executionChains.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class NavigationTest {

    @Test
    public void testNavigationValid(){
        TestModel model = new TestModel();
        model.number = 0;
        Chain<TestModel> processer = new Chain<>();
        SimpleChainNode<TestModel> n1 = new SimpleChainNode<>(m -> m.number++);
        processer.pushNode(n1);
        SimpleChainNode<TestModel> n2 = new SimpleChainNode<>(m -> m.number += 2);
        processer.pushNode(n2);
        SimpleChainNode<TestModel> n3 = new SimpleChainNode<>(m -> m.number += 3);
        processer.pushNode(n3);
        Navigation<TestModel> navigation = new Navigation<>(n1, n3);
        processer.addNavigation(navigation);
        processer.executeDefaultOrdered(model);
        Assert.assertEquals(model.number, 4);
    }

    @Test
    public void testNavigationNonExisting(){
        TestModel model = new TestModel();
        model.number = 0;
        Chain<TestModel> processer = new Chain<>();
        SimpleChainNode<TestModel> n1 = new SimpleChainNode<>(m -> m.number++);
        processer.pushNode(n1);
        SimpleChainNode<TestModel> n2 = new SimpleChainNode<>(m -> m.number += 2);
        Navigation<TestModel> navigation = new Navigation<TestModel>(n1, n2);
        processer.addNavigation(navigation);
        SimpleChainNode<TestModel> n3 = new SimpleChainNode<>(m -> m.number += 3);
        processer.pushNode(n3);
        processer.executeDefaultOrdered(model);
        Assert.assertEquals(model.number, 4);
    }

    @Test
    public void testNavigationPredicate(){
        SimpleChainNode<TestModel> node = new SimpleChainNode<>(m -> m.number++);
        Chain<TestModel> processer = new Chain<>(node);
        Navigation<TestModel> navigation = new Navigation<TestModel>(node, node, m -> m.number < 10);
        TestModel model = new TestModel();
        model.number = 0;
        processer.addNavigation(navigation);
        processer.executeDefaultOrdered(model);
        Assert.assertEquals(model.number, 10);
    }

}
