public class Solution {
    public int expanded, explored;
    public Node goalNode;

    public Solution() {}

    public Solution(int expanded, int explored, Node goalNode) {
        this.expanded = expanded;
        this.explored = explored;
        this.goalNode = goalNode;
    }
}
