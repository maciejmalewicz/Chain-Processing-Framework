import executionChains.Chain;
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
}
