package HW2;

import java.util.*;

public class Tree {
    String node;
    List<Tree> children;

    public Tree(String node, Tree... children) {
        this.node = node;
        this.children = Arrays.asList(children);
    }

    public Tree(String node) {
        this.node = node;
        this.children = new ArrayList<>();
    }

    public String printToGraphviz(HashMap<String, Integer> numbers) {
        StringBuilder ans = new StringBuilder();
        numbers.putIfAbsent(node, 0);
        ans.append(node.equals(")") ? "close" : node.equals("(") ? "open" : node.equals("*") ? "star" : node.equals("|") ? "or" : node)
                .append("_").append(numbers.get(node) > 0 ? numbers.get(node) - 1 : numbers.get(node)).append("[label = \"").append(node).append("\"];\n");
        int num = numbers.get(node) > 0 ? numbers.get(node) - 1 : numbers.get(node);
        for (Tree tree : children) {
            numbers.putIfAbsent(tree.node, 0);
            if (tree.node.equals("A")) {
                numbers.put(tree.node, numbers.get(tree.node) + 1);
            }
            ans.append(node).append("_").append(num).append("->").append(tree.node.equals(")") ? "close" : tree.node.equals("(") ? "open" : node.equals("*") ? "star" : tree.node.equals("|") ? "or" : tree.node)
                    .append("_").append(numbers.get(tree.node)).append(";\n");
            numbers.put(tree.node, numbers.get(tree.node) + 1);
            ans.append(tree.printToGraphviz(numbers));
        }
        return ans.toString();
    }

    public String printStr() {
        if ('A' <= node.charAt(0) && node.charAt(0) <= 'Z' || node.equals("ε")) {
            StringBuilder ans = new StringBuilder();
            for (Tree tree : children) {
                ans.append(tree.printStr());
            }
            return ans.toString();
        } else {
            return node;
        }
    }

}