# Chain-Processing-Framework
## What is it?
It’s a small library, responsible for managing order of processes execution. 
It’s a mix for <b>Chain of responsibility</b> and <b>Composite command</b> design patterns implemented already in built in classes. 
## When is it useful?
It becomes useful in situations, when programmer has to implement:
- long, complex procedure, that consists of many steps which should be divided into smaller classes by their responsibilities
- algorithm, consisting of many steps, executed in complicated order
## How is is useful?
It provides programmer with set of classes, that help managing order of methods execution, doing all of the dirty work. 
User can create so called <b>Chain Nodes</b> and put them into <b>Chain</b>, in order to use their methods and 
organize the processing. Here is the structure of solution, using framework:
![alt text](https://i.imgur.com/0KnbIHV.png)
We implement <b>Chain Node</b> abstract class in our customized way, by implementing void method `execute(Model model)`.
<b>Chain Node</b> is a generic class with Type parameter <b>Model</b>, so method <b>execute</b> can be called with any type
of parameter we want. Example implementation of <b>Chain Node</b> with its model:
```java
public class Model {
  public int num;
}
```
> Creating simple model with one interer field

```java
public class IncrementingNode extends ChainNode <Model> {

  @Override
  public void execute(Model model) {
    model.num++;
  }
}
```
> Creating a node that increments number in above model

This example is very simple, and obviously using this classes just to increment number would be abusing this framework, but you can 
imagine, that such nodes could be much more complicated. For example, they could contain complex database query calls or some 
complicated business logic. Let’s stick to the simple things first though, and see, how these nodes work in a <b>Chain</b>.
For this purpose, we will implement <b>Chain Nodes</b> as anonymous classes:
```java
public class Main {
    public static void main(String[] args) {

        ChainNode<Model> node1 = new ChainNode<Model>() {
            @Override
            public void execute(Model model) {
                model.num += 2;
            }
        };
        ChainNode<Model> node2 = new ChainNode<Model>() {
            @Override
            public void execute(Model model) {
                model.num += 3;
            }
        };

        Chain<Model> chain = new Chain<>(node1, node2);
        Model model = new Model();
        chain.executeDefaultOrdered(model);
        System.out.println("The result is: " + model.num);
    }
}
```
Program prints "The result is: 5". What happened here, step by step, is:
- Two nodes were created.
- They were passed to the constructor of <b>Chain</b> when creating new it
- <b>Chain</b> was executed in a default order. It means, that nodes are being executed in the same order, as they were added.
So in this case, `node1` has been executed before `node2`.

## Faster ways of creating <b>Chain Nodes</b>
Unfortunately, we can’t create chain nodes using lambdas, because they aren’t functional interfaces. 
They provide different, useful methods that will be discussed later. There is, however, a way to create them faster. 
If we don’t want to use any of <b>Chain Node</b> built in methods, we can create <b>Simple Chain Node</b>, 
which extends <b>Chain Node</b>. <b>Simple Chain Node</b> can take a lambda as a constructor parameter 
(which is implementation of <b>Chain Function</b> functional interface). Might sound complicated, but it’s actually very simple.
Here is the program shown above, but with <b>Chain Nodes</b> implemented as <b>Simple Chain Nodes</b>:

```java
public class Main {
    public static void main(String[] args) {

        ChainNode<Model> node1 = new SimpleChainNode<>(model -> model.num += 2);
        ChainNode<Model> node2 = new SimpleChainNode<>(model -> model.num += 3);

        Chain<Model> chain = new Chain<>(node1, node2);
        Model model = new Model();
        chain.executeDefaultOrdered(model);
        System.out.println("The result is: " + model.num);
    }
}
```
In this way we save a bit of work, but we can’t use full potential of <b>Chain Nodes</b>. In general, when using 
<b>Simple Chain Nodes</b>, it’s probably easier not to use this framework, but they might be useful when complexity of nodes 
are different and order of execution is not that simple.

## <b>Chain Node</b> functions
Now, let’s discuss these functions implemented in <b>Chain Nodes</b>. They all communicate with <b>Chain</b> 
(maybe not exactly with chain, but for now we can assume it’s chain) they are linked with, so if we will just execute this functions 
outside of <b>Chains</b> process, they will throw some Nullpointers!
That’s why we should invoke these functions only inside of `execute` implementation of our <b>Chain Node</b>.

Let's describe, what they do:
- `void stop()` makes the <b>Chain</b> stop right after execution of node in which it was invoked. Tip: Adding `return;` after it will 
cause the chain to stop immediately.
- `void goTo(int index)` makes <b>Chain</b> proceed to the <b>Node</b> with selected index after execution of <b>Node</b> in which it was 
invoked. It throws `ArrayIndexOutOfBoundsException` if the index does not exist in the <b>Chain</b>. Indexing if based on order of 
<b>Nodes</b> pushed to the <b>Chain</b>, so first <b>Node</b> added will have index "0", second will have "1" and so on…
- `void goTo(ChainNode node)` makes <b>Chain</b> proceed to selected <b>Node</b> after execution of <b>Node</b> on which it was invoked.
- `void restart()` makes <b>Chain</b> move to the first <b>Node</b> after finishing execution of <b>Node</b> on which it was invoked.
- `void skipToEnd()` makes <b>Chain</b> move to end after finishing execution of <b>Node</b> on which it was invoked.
It means, that in default execution mode the <b>Chain</b> will simply end. The difference between `stop()` is that for example in `loop()`
<b>chain mode</b> it will simply skip all the remaining <b>Nodes</b> and try to execute chain again.
- `void skipNode(ChainNode node)` makes <b>Chain</b> skip given <b>Node</b> when the <b>Chain</b> will be trying to invoke it.
The <b>Node</b> will be skipped every time it’s tried to invoke by this single execution from now.
- `void skipNodes(List<ChainNode> nodes)` does the same for many <b>Nodes</b>.

These functions are giving user a lot of freedom in designing the <b>Chain</b>. User must be aware though, that they will do a bit
different things, depending on how we execute it. This will be explained in more details later. On some execution modes, 
several functions will do exactly the same thing, like for example in <b>default ordered mode</b> both `skipToEnd()` and `stop()`
will have the same effect. Advantages of using these functions are that they are very intuitive and they help implementing <b>Chain</b>
fast. However, they might be problematic, when it comes to flexibility and independence of <b>Nodes</b>. There is an alternative to 
these functions – <b>Navigations</b>, that will be used later.

Example `stop()` usage:
```java
public class Main {
    public static void main(String[] args) {
    
        ChainNode<Model> firstNode = new SimpleChainNode<>(n -> n.num += 2);
        ChainNode<Model> stoppingNode = new ChainNode<Model>() {
            @Override
            public void execute(Model model) {
                stop();
                model.num += 3;
            }
        };
        ChainNode<Model> skippedNode = new SimpleChainNode<>(n -> n.num += 4);
        
        Chain<Model> chain = new Chain<>(firstNode, stoppingNode, skippedNode);
        Model model = new Model();
        chain.executeDefaultOrdered(model);
        System.out.println("Result is: " + model.num);
    }
}
```
Program prints "Result is: 5". That's happening is:
-	`firstNode` is being executed – number in model increases and becomes a "2"
- `stoppingNode` is being executed and so
  -	`stop()` is being invoked – it blocks any other remaining <b>Nodes</b> from being executed
  - number in model is being increased anyway and it becomes a "5"
-	`skippedNode` isn’t executed anymore, because it was stopped by the `stoppingNode`. Number in model is not increased by 4,
so it remains "5" until the and.

## Chain building

Building <b>Chain</b> means adding and removing elements like <b>Chain Nodes</b> to it.<b>Chain</b> provides user with several 
constructors and methods that help with it:

Constructors:
-	`Chain(ChainNode<? super Model>… nodes)` – builds chain from nodes, in the order given. Chain may be later modified.
-	`Chain (List<ChainNode<? super Model>> nodes)` – builds chain from list of nodes, in the order given. Chain may be later modified.
-	`Chain()` – builds chain and creates inside empty list of nodes, that may be later modified

Chain modifying methods:
-	`pushNode(ChainNode<? super Model> node)` – adds new node to the chains node list
- `insertNode(ChainNode<? super Model> node, int index)` – adds new node to the chains list on specified position
- `removeNode(ChainNode<? super Model> node)` – removes node from the chain

## Chain execution modes
<b>Chains</b> can be executed in a several ways, by calling different methods on them. These are:
-	`executeDefaultOrdered(Model model)` – simply makes <b>Chain</b> running from beginning to end.
-	`executeWhile(Model model, Predicate<Model> condition)` – does exactly the same thing as above, but before execution of each <b>Node</b>,the condition is being checked. If it’s not satisfied, <b>Chain</b> stops.
-	`loop(Model model)` – makes <b>Chain</b> running over and over – from beginning to end. May be stopped only by invoking `stop()`.
-	`loopWhile(Model model, Predicate<Model> condition)` – does exactly the same as above, but at every loop beginning the condition is checked. If it’s not satisfied, <b>Chain</b> stops
-	`loopNTimes(Model model, int times)` – executes all <b>Nodes</b> in order n times

## Combining execution methods with node methods
Node methods and chain methods cope with each other in a different, but predictable ways. Here are 3 methods 
(and not the only ones) of implementing counting from 1 to 10.

Fist approach is probably the least appropriate. Instead of using a loop, we are restarting the <b>Chain</b> manually from the last <b>Node</b>, until it has printed "10". It works, but in this framework we
have better (more readable) ways to do it.
```java
public class First10 {
    public static void main(String[] args) {
        Model model = new Model();
        model.num = 1;
        Chain<Model> chain = new Chain<>(
                new SimpleChainNode<>(m -> System.out.println(m.num)),
                new SimpleChainNode<>(m -> m.num++),
                new ChainNode<Model>() {
                    @Override
                    public void execute(Model model) {
                        if (model.num <= 10){
                            restart();
                        }
                    }
                }
        );
        chain.executeDefaultOrdered(model);

    }
}
```

In second example, we are using `loopWhile()`. Instead of creating 3 <b>Nodes</b> now, we created only one. We could have divided printing and incrementing into two <b>Nodes</b>, but there is no need. Loop is printing numbers until "10", because of the predicate 
`m -> m.num <= 10`. If number exceeds "10", predicate returns false and loop finishes. This approach is much better than previous one, but still we can do better.
```java
public class Second10 {
    public static void main(String[] args) {
        Model model = new Model();
        model.num = 1;
        Chain<Model> chain = new Chain<>(
                new ChainNode<Model>() {
                    @Override
                    public void execute(Model model) {
                        System.out.println(model.num);
                        model.num++;
                    }
                }
        );
        chain.loopWhile(model, m -> m.num <= 10);
    }
}
```

In third example, we are doing something very similar, but instead of `loopWhile` we use `loopNTimes`. It works in a very similar way, but here instead of saying until when should it print, we are saying how many times.
```java
public class Third10 {
    public static void main(String[] args) {
        Chain<Model> chain = new Chain<>(
                new ChainNode<Model>() {
                    @Override
                    public void execute(Model model) {
                        System.out.println(model.num);
                        model.num++;
                    }
                }
        );
        Model model = new Model();
        model.num = 1;
        chain.loopNTimes(model, 10);
    }
}
```

These were just examples of how the framework works. Of course, using good old "for loop" is much better in this case, and in examples above we are abusing chains. 

```java
public class Fourth10 {
    public static void main(String[] args) {
        for (int i = 1; i <= 10; i++){
            System.out.println(i);
        }
    }
}
```
> Implementation without using framework

## Navigations – chain nodes flexibility problem
Let’s imagine following situation:
![alt text](https://i.imgur.com/0TItCqm.png)

We have 2 <b>Chains</b> using the same <b>Nodes</b> – "Get Price" and "Validate", if customer has enough money. There is a problem in second one. In "PAYMENT" chain, we don’t charge customers account if customer doesn’t have enough money – we skip one node, order is being changed. In the second one, we don’t skip anything and chain goes on. What we could do in this situation is:
-	use `skipNode(ChainNode node)` method in validating payment node. This would work for this case, but what if we had third chain like this:

![alt text](https://i.imgur.com/IgLMKxZ.png)

> Here, we assume that customer has second account that we can always charge if he doesn’t have enough money. So in this case, we can’t skip charging account node anymore. 

-	Another solution would be to implement validating node twice – once with skipping and once without it. This is bad, because we might have another chain that will need even different behaviour.
-	We can use <b>Navigations</b>.

## Navigations – idea
In order to avoid the problem discussed above, we can separate <b>ordering nodes</b> from <b>nodes</b> themselves and encapsulate ordering in <b>Navigations</b>. It means, we will leave "validating node" without any chain functions and add some properties to a particular <b>Chain</b>. Like this, we can separate ordering process from business logic of <b>Nodes</b>.

## Navigations – implementation
Class `Navigation` is a generic type (Navigation<Model>) and has two similar constructors:
-	`Navigation(ChainNode<Model> from, ChainNode<Model> to)`
-	`Navigation(ChainNode<Model> from, ChainNode<Model> to, Predicate<Model> condition)`
  
Both of these have "from" and "to" node. When we apply navigation to the chain, every time after executing "from" chain, navigation will proceed to "to" node. This happens in case we use first constructor. Second constructor gives us a bit more control. We pass here also a predicate which will be checked every time, when navigation is trying to move chain to "to". If predicate will return false, navigation will fail and default order from chain processing mode will be used - chain execution mode will decide, which node is executed next.
  
## Navigations – collatz example (how it can be used in algorithms)
Let’s see, a collatz sequence example. In this case, framework will actually improve readability of the code and save programmer a bit of struggle. 
Our job is to simply print over and over next terms of collatz sequence starting from number we choose (let’s say "500"). Generating next term of collatz sequence is based on the previous one. If previous is even, our next term is equal to "previous/2" and if it’s odd, next term is equal to "3*previous + 1". We will divide generating each term into 3 different nodes:
- Print current term
-	Do n/2 (when printed term was even)
-	Do 3n+1 (when printed term was odd)
Now we won’t use loops or any chain node functions, we will use only navigations on these 3 steps. We will be printing current term, then generating next one using second and third node and then go back to first, printing node. And repeat. 

![alt text](https://i.imgur.com/fNMOTmd.png)

Let’s see implementation. We will be using `isEven(int num)` function:
```java
private static boolean isEven(int num){
    return num%2 == 0;
}
```

And here we go:
```java
public static void main(String[] args) {

        //creating chain
        SimpleChainNode<Model> printingNode = new SimpleChainNode<>(m -> System.out.println(m.num));
        SimpleChainNode<Model> evenNode = new SimpleChainNode<>(m -> m.num /= 2);
        SimpleChainNode<Model> oddNode = new SimpleChainNode<>(m -> m.num = m.num*3 + 1);
        Chain<Model> chain = new Chain<>(
                printingNode,
                evenNode,
                oddNode
        );

        //adding navigations
        Navigation<Model> toEvenNavigation = new Navigation<>(printingNode, evenNode, m -> isEven(m.num));
        Navigation<Model> toOddNavigation = new Navigation<>(printingNode, oddNode, m -> !isEven(m.num));
        Navigation<Model> fromEvenNavigation = new Navigation<>(evenNode, printingNode);
        Navigation<Model> fromOddNavigation = new Navigation<>(oddNode, printingNode);

        chain.addNavigation(toEvenNavigation);
        chain.addNavigation(toOddNavigation);
        chain.addNavigation(fromEvenNavigation);
        chain.addNavigation(fromOddNavigation);

        //executing infinite loop
        Model model = new Model();
        model.num = 500;
        chain.executeDefaultOrdered(model);
    }
```
This is one way of implementing it. Not the shortest way, but it requires from programmer less thinking about what can go wrong, etc. 
















