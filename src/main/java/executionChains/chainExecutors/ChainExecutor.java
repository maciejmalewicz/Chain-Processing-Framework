package executionChains.chainExecutors;

import executionChains.ChainNode;
import executionChains.Chain;
import executionChains.Navigation;
import executionChains.misc.ChainNodeFunctions;

import java.util.Iterator;
import java.util.List;

public abstract class ChainExecutor <Model> implements Iterator<ChainNode<? super Model>>, ChainNodeFunctions {

    boolean needsNavigation = true;
    boolean proceeds = true;
    int nextIndex = 0;
    List<ChainNode<? super Model>> nodes;
    protected Chain<Model> processer;
    private boolean[] nodeActivity;
    protected Model model;

    ChainExecutor(Chain<Model> processer, Model model){
        this.processer = processer;
        this.nodes = processer.getNodes();
        buildNodeActivities();
        this.model = model;
    }

    private void buildNodeActivities(){
        nodeActivity = new boolean[nodes.size()];
        for (int i = 0; i < nodeActivity.length; i++){
            nodeActivity[i] = true;
        }
    }

    public abstract void execute(List<ChainNode<? super Model>> nodes);
    protected abstract int getDefaultNext();


    @Override
    public void stop() {
        proceeds = false;
        needsNavigation = false;
    }

    @Override
    public void goTo(int index) throws ArrayIndexOutOfBoundsException {
        if (index >= nodes.size() || index < 0){
            throw new ArrayIndexOutOfBoundsException("Index in goTo(index) was out of bounds!");
        }
        setNextIndex(index);
    }

    @Override
    public void goTo(ChainNode node) throws NodeNotFoundException {
        int index = processer.getNodeIndex(node);
        if (index == -1){
            throw new NodeNotFoundException("Node " + node + " not found!");
        }
        if (index >= nodes.size()){
            throw new ArrayIndexOutOfBoundsException("Index in goTo(index) was out of bounds!");
        }
        setNextIndex(index);
    }

    //    @Override
//    public void skipUntil(ChainNode targetNode) throws NodeNotFoundException {
//        for (int i = nextIndex; i < nodes.size(); i++){
//            ChainNode<Model> node = nodes.get(i);
//            if (targetNode.equals(node)){
//                setNextIndex(i);
//                return;
//            }
//        }
//        throw new NodeNotFoundException("Node " + targetNode + " not found!");
//    }
//
//    @Override
//    public void goBackUntil(ChainNode targetNode) throws NodeNotFoundException{
//        int startingIndex = getBackwardsStartingIndex();
//        for (int i = startingIndex; i >=0; i--){
//            ChainNode<Model> node = nodes.get(i);
//            if (targetNode.equals(node)){
//                setNextIndex(i);
//                return;
//            }
//        }
//        throw new NodeNotFoundException("Node " + targetNode + " not found!");
//    }

    private int getBackwardsStartingIndex(){
        return nextIndex-1;
    }


    @Override
    public void restart() {
        setNextIndex(0);
    }

    @Override
    public void skipToEnd() {
        setNextIndex(nodes.size());
    }

    @Override
    public void skipNode(ChainNode node) {
        int id = processer.getNodeIndex(node);
        if (id < 0 || id >= nodeActivity.length){
            return;
        }
        nodeActivity[id] = false;
    }

    @Override
    public ChainNode<? super Model> next(){
        return nodes.get(nextIndex);
    }

    void updateNextIndex(ChainNode<? super Model> node){
        int targetIndex = getNavigatedNext(node);
        if (targetIndex == -1){
            targetIndex = getDefaultNext();
        }
        setNextIndex(targetIndex);
    }

    private int getNavigatedNext(ChainNode<? super Model> node){
        List<Navigation<? super Model>> navigationList = processer.getNavigationList(node);
        if (navigationList == null){
            return -1;
        }
        for (Navigation<? super Model> navigation: navigationList){
            if (navigation.canBeUsed(model)){
                return processer.getNodeIndex(navigation.getTo());
            }
        }
        return -1;
    }

    void setNextIndex(int index){
        needsNavigation = false;
        nextIndex = index;
    }

    boolean canBeExecuted(ChainNode<? super Model> node){
        int id = processer.getNodeIndex(node);
        if (id < 0 || id >= nodeActivity.length){
            return false;
        }
        return nodeActivity[id];
    }
}
