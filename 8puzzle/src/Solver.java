import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private class Move implements Comparable<Move> {
        private Move previous;
        private Board board;
        private int numMoves = 0;

        Move(Board board) {
            this.board = board;
        }

        Move(Board board, Move previous) {
            this(board);

            this.previous = previous;
            this.numMoves = previous.numMoves + 1;
        }

        @Override
        public int compareTo(Move o) {
            return board.manhattan() + numMoves - o.board.manhattan() - o.numMoves;
        }
    }

    private Move lastMove;

    public Solver(Board initial) {
        MinPQ<Move> moves = new MinPQ<>();
        moves.insert(new Move((initial)));

        MinPQ<Move> twinMoves = new MinPQ<>();
        twinMoves.insert(new Move(initial.twin()));

        while (true) {
            lastMove = expand(moves);

            if (lastMove != null || expand(twinMoves) != null) return;
        }
    }

    private Move expand(MinPQ<Move> moves) {
        if (moves.isEmpty()) return null;

        Move bestMove = moves.delMin();

        if (bestMove.board.isGoal()) return bestMove;

        for (Board neighboard : bestMove.board.neighbors()) {
            if (bestMove.previous == null || !neighboard.equals(bestMove.previous.board)) {
                moves.insert(new Move(neighboard, bestMove));
            }
        }

        return null;
    }

    public boolean isSolvable() {
        return lastMove != null;
    }

    public int moves() {
        return isSolvable() ? lastMove.numMoves : -1;
    }

    public Iterable<Board> solution() {
        if (!isSolvable()) return null;

        Stack<Board> moves = new Stack<>();

        while (lastMove != null) {
            moves.push(lastMove.board);
            lastMove = lastMove.previous;
        }

        return moves;
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable()) {
            StdOut.println("No solution possible");
        } else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
