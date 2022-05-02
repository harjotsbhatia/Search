package pathsolver;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class PathSolver {

    public int arrSize = 3;
    String path = "";
    int nodesCount = 0;

    int[] horizontalIdx = {1, 0, -1, 0};
    int[] verticalIdx = {0, -1, 0, 1};

    public int findEffortOfNode(int[][] starterNodes, int[][] endingNodes) {
        int indexerNodes = 0;
        int totalNodes = starterNodes.length;
        for (int iter1 = 0; iter1 < totalNodes; iter1++) {
            for (int iter2 = 0; iter2 < totalNodes; iter2++) {
                if (starterNodes[iter1][iter2] != 0 && starterNodes[iter1][iter2] != endingNodes[iter1][iter2]) {
                    indexerNodes++;
                    this.nodesCount++;
                }
            }
        }
        return indexerNodes;
    }

    public void displayNodes(int[][] nodesSet) {
        for (int iter1 = 0; iter1 < nodesSet.length; iter1++) {
            for (int iter2 = 0; iter2 < nodesSet.length; iter2++) {
                System.out.print(nodesSet[iter1][iter2] + " ");
            }
            System.out.println();
        }
    }

    public boolean canMoveToNode(int nodePt1, int nodePt2) {
        return (nodePt1 >= 0 && nodePt1 < arrSize && nodePt2 >= 0 && nodePt2 < arrSize);
    }

    public void showNodeWay(PathNode root) {
        if (root == null) {
            return;
        }
        showNodeWay(root.topNode);
        displayNodes(root.nodesSet);
        System.out.println();
    }

    public boolean canGetNodeResult(int[][] nodesSet) {
        int indexerNodes = 0;
        List<Integer> array = new ArrayList<Integer>();

        for (int iter1 = 0; iter1 < nodesSet.length; iter1++) {
            for (int iter2 = 0; iter2 < nodesSet.length; iter2++) {
                array.add(nodesSet[iter1][iter2]);
            }
        }

        Integer[] anotherArray = new Integer[array.size()];
        array.toArray(anotherArray);

        for (int iter1 = 0; iter1 < anotherArray.length - 1; iter1++) {
            for (int iter2 = iter1 + 1; iter2 < anotherArray.length; iter2++) {
                if (anotherArray[iter1] != 0 && anotherArray[iter2] != 0 && anotherArray[iter1] > anotherArray[iter2]) {
                    indexerNodes++;
                }
            }
        }

        return indexerNodes % 2 == 0;
    }

    public void solve(int[][] starterNodes, int[][] endingNodes, int nodePt1, int nodePt2) {
        PriorityQueue<PathNode> nodeIdxr = new PriorityQueue<PathNode>(1000, (a, b) -> (a.cost + a.level) - (b.cost + b.level));
        PathNode root = new PathNode(starterNodes, nodePt1, nodePt2, nodePt1, nodePt2, 0, null);
        root.cost = findEffortOfNode(starterNodes, endingNodes);
        nodeIdxr.add(root);

        while (!nodeIdxr.isEmpty()) {
            PathNode leastNode = nodeIdxr.poll();
            if (leastNode.cost == 0) {
                showNodeWay(leastNode);
                return;
            }

            for (int iter1 = 0; iter1 < 4; iter1++) {
                if (canMoveToNode(leastNode.nodePt1 + horizontalIdx[iter1], leastNode.nodePt2 + verticalIdx[iter1])) {
                    if(horizontalIdx[iter1]==-1)
                        path+="u";
                    else if(horizontalIdx[iter1]==1)
                        path+="d";
                    else if(verticalIdx[iter1]==-1)
                        path+="l";
                    else if(verticalIdx[iter1]==1)
                        path+="r";
                    PathNode child = new PathNode(leastNode.nodesSet, leastNode.nodePt1, leastNode.nodePt2, leastNode.nodePt1 + horizontalIdx[iter1], leastNode.nodePt2 + verticalIdx[iter1], leastNode.level + 1, leastNode);
                    child.cost = findEffortOfNode(child.nodesSet, endingNodes);
                    nodeIdxr.add(child);
                }
            }
        }
    }

    public static void main(String[] args) {
        String input = "123450678";
        int[][] starterNodes = new int[3][3];
        int[][] endingNodes = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
        int nodePt1 = 0, nodePt2 = 0;
        int iter2 = 0;
        for (int iter1 = 0; iter1 < input.length(); iter1++) {
            starterNodes[iter2][iter1 % 3] = Integer.parseInt(String.valueOf(input.charAt(iter1)));
            if(starterNodes[iter2][iter1%3]==0){
                nodePt1=iter2;
                nodePt2=iter1%3;
            }
            if (iter1 == 2 || iter1 == 5) {
                iter2++;
            }
        }
        for (int iter1 = 0; iter1 < 3; iter1++) {
            for (int k = 0; k < 3; k++) {
                System.out.print(starterNodes[iter1][k] + " ");
            }
            System.out.println();
        }

        PathSolver puzzle = new PathSolver();
        if (puzzle.canGetNodeResult(starterNodes)) {
            puzzle.solve(starterNodes, endingNodes, nodePt1, nodePt2);
            System.out.println("Test: "+input);
            System.out.println("Goal: 123456780");
            System.out.println("nodes expanded: "+puzzle.nodesCount);
            System.out.println(puzzle.path);
        } else {
            System.out.println("The given starterNodes is impossible to solve");
        }
    }

}