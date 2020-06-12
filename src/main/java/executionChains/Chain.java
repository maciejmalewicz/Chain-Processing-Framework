package executionChains;

import executionChains.chainExecutors.*;
import executionChains.misc.ChainExecutingFunctions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

public class Chain<Model> implements ChainExecutingFunctions <Model> {

    private List<ChainNode<? super Model>> nodes;
    private ChainExecutor<Model> executor;
    private HashMap<Integer, Integer> nodesMap;
    private HashMap<Integer, List<Navigation<? super Model>>> navigationMap;

    //constructors
    @SafeVarargs
    public Chain(ChainNode<? super Model>... nodes){
        this.nodes = Arrays.asList(nodes);
        nodesMap = new HashMap<>();
        navigationMap = new HashMap<>();
    }

    public Chain(List<ChainNode<? super Model>> nodes){
        this.nodes = nodes;
        nodesMap = new HashMap<>();
        navigationMap = new HashMap<>();
    }

    public Chain(){
        nodes = new ArrayList<>();
        nodesMap = new HashMap<>();
        navigationMap = new HashMap<>();
    }

    //chain editing methods

    public void pushNode(ChainNode<? super Model> node){
        nodes.add(node);
    }

    public boolean insertNode(ChainNode<? super Model> node, int index){
        if (index < nodes.size()){
            nodes.add(index, node);
            return true;
        }
        return false;
    }

    public boolean removeNode(ChainNode<? super Model> node){
        return nodes.remove(node);
    }

    //navigation

    public void addNavigation(Navigation<? super Model> navigation){
        ChainNode<? super Model> node = navigation.getFrom();
        addNavigationListIfAbsent(node);
        int id = node.chainNodeId;
        List<Navigation<? super Model>> navigationList = navigationMap.get(id);
        navigationList.add(navigation);
    }

    private void addNavigationListIfAbsent(ChainNode<? super Model> node){
        int nodeId = node.chainNodeId;
        if (!navigationMap.containsKey(nodeId)){
            navigationMap.put(nodeId, new ArrayList<>());
        }
    }


    public List<Navigation<? super Model>> getNavigationList(ChainNode<? super Model> node){
        int nodeId = node.chainNodeId;
        return navigationMap.get(nodeId);
    }


    //executing chain
    @Override
    public void executeDefaultOrdered(Model model){
        executor = new DefaultOrderedExecutor<>(this, model);
        executeExecutor(executor);
    }

    @Override
    public void executeWhile(Model model, Predicate<Model> condition){
        executor = new ConditionalExecutor<>(this, condition, model);
        executeExecutor(executor);
    }

    @Override
    public void loop(Model model){
        executor = new LoopExecutor<>(this, model);
        executeExecutor(executor);
    }

    @Override
    public void loopWhile(Model model, Predicate<Model> condition){
        executor = new LoopUntilExecutor<>(this, condition, model);
        executeExecutor(executor);
    }

    @Override
    public void loopNTimes(Model model, int loops){
        executor = new LoopNExecutor<>(this, loops, model);
        executeExecutor(executor);
    }

    private synchronized void executeExecutor(ChainExecutor<Model> executor){
        prepareExecution(executor);
        executor.execute(nodes);
    }

    //getters / setters

    public List<ChainNode<? super Model>> getNodes(){
        return nodes;
    }

    public int getNodeIndex(ChainNode node){
        int nodeId = node.chainNodeId;
        return nodesMap.getOrDefault(nodeId, -1);
    }


    //preparing for execution
    private void prepareExecution(ChainExecutor<Model> executor){
        connectNodes(executor);
        initializeNodeMap();

    }

    private void initializeNodeMap() {
        nodesMap = new HashMap<>();
        for (int i = 0; i < nodes.size(); i++){
            int nodeId = nodes.get(i).chainNodeId;
            nodesMap.put(nodeId, i);
        }
    }



    private void connectNodes(ChainExecutor<Model> executor){
        nodes.forEach(n -> n.setExecutor(executor));
    }

}
