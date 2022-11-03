import java.util.ArrayList;  
import java.lang.Math;

public class MoveChooser {
    public static int[][] scoreBoard = {{120,-20,20,5,5,20,-20,120},
    {-20,-40,-5,-5,-5,-5,-40,-20},{20,-5,15,3,3,15,-5,20},
    {5,-5,3,3,3,3,-5,5},{5,-5,3,3,3,3,-5,5},{20,-5,15,3,3,15,-5,20},
    {-20,-40,-5,-5,-5,-5,-40,-20},{120,-20,20,5,5,20,-20,120}};

    public static Move chooseMove(BoardState boardState){
        if (boardState.getLegalMoves().isEmpty())
            return null;

	    int searchDepth= Othello.searchDepth;
        int move = MiniMax(boardState, searchDepth, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);

        int y = move%10; int x = move/10;
        return new Move(x,y);

    }

    public static boolean boardFull(BoardState bs){
        for (int i =0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if (bs.getContents(i, j) == 0)
                    return false;
            }
        }
        return true;
    }

    public static int MiniMax(BoardState bs, int depth, int alpha, int beta, int player){
        

        if (depth == 0 || boardFull(bs)){
            int score = 0;
            for (int i = 0; i < 8; i++){
                for (int j = 0; j < 8; j++){
                    if (bs.getContents(i,j) == 1){
                        score += scoreBoard[i][j];
                    }
                    else if (bs.getContents(i,j) == -1){
                        score -= scoreBoard[i][j];
                    }
                }
            }
            return score;
        }

        ArrayList<Move> moves= bs.getLegalMoves();

        if (player == -1 && bs.getLegalMoves().isEmpty()){
            bs.colour = bs.colour*-1;
            return MiniMax(bs, depth-1, alpha, beta, 1);
        }

        else if (player == 1 && bs.getLegalMoves().isEmpty()){
            bs.colour = bs.colour*-1;
            return MiniMax(bs, depth-1, alpha, beta, 1);
        }

        if (player == 1){
            int maxEval = Integer.MIN_VALUE;
            Move move = null;
            for (Move m: moves){
                BoardState newBs = bs.deepCopy();
                newBs.makeLegalMove(m.x, m.y);
                int eval = MiniMax(newBs, depth-1, alpha, beta, player*-1);
                if (eval > maxEval){
                    maxEval = eval;
                    move = m;
                }
                alpha = Math.max(alpha, maxEval);
                if (beta <= alpha)
                    break;
            }
            if (depth == Othello.searchDepth){
                return 10*move.x+move.y;
            }
            return maxEval;
        }

        else {
            int minEval = Integer.MAX_VALUE;
            for (Move m: moves){
                BoardState newBs = bs.deepCopy();
                newBs.makeLegalMove(m.x, m.y);
                int eval = MiniMax(newBs, depth-1, alpha, beta, player*-1);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, minEval);
                if (beta <= alpha)
                    break;
            }
            return minEval;
        }
        
        
    }
}
