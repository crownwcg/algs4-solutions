import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.ArrayList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;

public class BaseballElimination {

    private final int n;
    private final int[] w;
    private final int[] l;
    private final int[] r;
    private final int[][] g;
    private final String[] tName;
    private final Map<String, Integer> teams;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        n  = in.readInt();
        teams = new HashMap<String, Integer>();
        tName = new String[n];
        w  = new int[n];
        l  = new int[n];
        r  = new int[n];
        g  = new int[n][n];

        for (int i = 0; i < n; i++) {
            tName[i]= in.readString();
            teams.put(tName[i], i);
            w[i] = in.readInt();
            l[i] = in.readInt();
            r[i] = in.readInt();
            for (int j = 0; j < n; j++) {
                g[i][j] = in.readInt();
            }
        }

        in.close();
    }
    // number of teams
    public              int numberOfTeams() {
        return n;
    }

    // all teams
    public Iterable<String> teams() {
        return Arrays.asList(tName);
    }

    private void check(String team) {
        if (!teams.containsKey(team)) throw new java.lang.IllegalArgumentException();
    }
    // number of wins for given team
    public              int wins(String team) {
        check(team);
        return w[teams.get(team)];
    }
    // number of losses for given team
    public              int losses(String team) {
        check(team);
        return l[teams.get(team)];
    }
    // number of remaining games for given team
    public              int remaining(String team) {
        check(team);
        return r[teams.get(team)];
    }
    // number of remaining games between team1 and team2
    public              int against(String team1, String team2) {
        check(team1);
        check(team2);
        return g[teams.get(team1)][teams.get(team2)];
    }
    // is given team eliminated?
    public          boolean isEliminated(String team) {
        check(team);
        return certificateOfElimination(team) != null;
    }
    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        check(team);
        int x = teams.get(team);
        ArrayList<String> res = new ArrayList<String>();
        // Trivial elimination
        for (int i = 0; i < n; i++)
            if (x != i && w[x] + r[x] < w[i])
                res.add(tName[i]);

        if (!res.isEmpty()) return res;

        // Nontrivial elimination
        int V = n + (n - 1) * (n - 2) / 2 + 2;
        int s = V - 2;
        int t = V - 1;
        FlowNetwork net = new FlowNetwork(V);

        for (int i = 0, v = n; i < n; i++) {
            if (i == x) continue;
            for (int j = i + 1; j < n; j++) {
                if (j == x) continue;
                net.addEdge(new FlowEdge(s, v, g[i][j]));
                net.addEdge(new FlowEdge(v, i, g[i][j]));
                net.addEdge(new FlowEdge(v, j, g[i][j]));
                v++;
            }
            net.addEdge(new FlowEdge(i, t, w[x] + r[x] - w[i]));
        }

        FordFulkerson max = new FordFulkerson(net, s, t);

        for (int v = 0; v < n; v++) {
            if (max.inCut(v)) res.add(tName[v]);
        }
        return res.isEmpty() ? null : res;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
