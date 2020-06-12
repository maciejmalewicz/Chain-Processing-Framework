import executionChains.Chain;
import executionChains.Navigation;
import inheritance.BaseModel;
import inheritance.ComplexModel;
import inheritance.DoubleIncreasingNode;
import inheritance.IncreasingNode;
import org.junit.Assert;
import org.junit.Test;

public class TestInheritance {

    @Test
    public void testBase(){
        Chain<BaseModel> chain = new Chain<>(
                new IncreasingNode()
        );
        BaseModel model = new BaseModel();
        chain.executeDefaultOrdered(model);
        Assert.assertEquals(model.num1, 1);
    }

    @Test
    public void testExtending(){
        Chain<ComplexModel> chain = new Chain<>(
                new IncreasingNode(),
                new DoubleIncreasingNode()
        );
        ComplexModel model = new ComplexModel();
        chain.executeDefaultOrdered(model);
        Assert.assertEquals(model.num1, 2);
        Assert.assertEquals(model.num2, 1);
    }

    @Test
    public void testInheritedNavigations(){
        IncreasingNode node = new IncreasingNode();
        DoubleIncreasingNode node2 = new DoubleIncreasingNode();

        Chain<ComplexModel> chain = new Chain<>(
                node,
                node2
        );
        //Navigation<BaseModel> navigation = new Navigation<>(node, node2);
        ComplexModel model = new ComplexModel();
    }
}
