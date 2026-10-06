package chess;
import java.util.*;
import static java.util.Objects.isNull;

public class PieceMoves {

    private final ChessBoard board;
    private final ChessPosition currPos;
    private final int currRow;
    private final int currCol;
    private final ChessGame.TeamColor color;
    private final ChessPiece.PieceType type;
    private final ArrayList<ChessPosition> promoPositions = new ArrayList<>();
    private final int[][] rulerPaths = {{1,-1}, {1,0}, {1,1}, {0,-1}, {0,1}, {-1,-1}, {-1,0}, {-1,1}};
    private final int[][] bishopPaths = {{1,-1}, {1,1}, {-1,-1}, {-1,1}};
    private final int[][] rookPaths = {{1,0}, {0,-1}, {0,1}, {-1,0}};
    private final int[][] knightPaths = {{2,-1}, {2,1}, {1,-2}, {1,2}, {-2,-1}, {-2,1}, {-1,-2}, {-1,2}};


    public PieceMoves(ChessBoard board, ChessPosition currPos, int currRow, int currCol, ChessGame.TeamColor color, ChessPiece.PieceType type) {
    this.board = board;
    this.currPos = currPos;
    this.currRow = currRow;
    this.currCol = currCol;
    this.color = color;
    this.type = type;
    }


    Collection<ChessMove> getMoves() {
        ArrayList<ChessPiece.PieceType> types = new ArrayList<>(Arrays.asList(ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT));
        ArrayList<ChessPosition> positions = new ArrayList<>();
        ArrayList<ChessMove> moves = new ArrayList<>();
        if (type == ChessPiece.PieceType.KING) {
            positions = kingMoves();
        }
        else if (type == ChessPiece.PieceType.QUEEN) {
            positions = continuousMoves(rulerPaths);
        }
        else if (type == ChessPiece.PieceType.BISHOP) {
            positions = continuousMoves(bishopPaths);
        }
        else if (type == ChessPiece.PieceType.ROOK) {
            positions = continuousMoves(rookPaths);
        }
        else if (type == ChessPiece.PieceType.KNIGHT) {
            positions = knightMoves(knightPaths);
        }
        else {
            positions = pawnMoves();
        }
        if (!positions.isEmpty()) {
            for (ChessPosition position : positions) {
                ChessMove move = new ChessMove(currPos, position, null);
                moves.add(move);
            }
        }
        if (!promoPositions.isEmpty()) {
            for (ChessPosition pos : promoPositions) {
                for (ChessPiece.PieceType piece : types) {
                    ChessMove move = new ChessMove(currPos, pos, piece);
                    moves.add(move);
                }
            }
        }
        return moves;
    }


    private ArrayList<ChessPosition> kingMoves() {
        ArrayList<ChessPosition> positions = new ArrayList<>();
        for (int[] path : rulerPaths) {
            ChessPosition position = isSafe(currRow + path[0] - 1, currCol + path[1] - 1);
            if (!isNull(position)) {
                positions.add(position);
            }
        }
        return positions;
    }


    private ArrayList<ChessPosition> continuousMoves(int[][] paths) {
        ArrayList<ChessPosition> positions = new ArrayList<>();
        for (int[] path : paths) {
            int row = currRow;
            int col = currCol;
            while (true) {
                ChessPosition pos = isSafe(row + path[0] - 1, col + path[1] - 1);
                if (pos != null) {
                    positions.add(pos);
                    ChessPosition position = new ChessPosition(row+path[0], col+path[1]);
                    ChessPiece piece = board.getPiece(position);
                    if (piece != null) {
                        break;
                    }
                }
                else {
                    break;
                }
                row += path[0];
                col += path[1];
            }
        }
        return positions;
    }


    private ArrayList<ChessPosition> knightMoves(int[][] paths) {
        ArrayList<ChessPosition> positions = new ArrayList<>();
        for (int[] path : paths) {
            ChessPosition position = isSafe(currRow + path[0] - 1, currCol + path[1] - 1);
            if (!isNull(position)) {
                positions.add(position);
            }
        }
        return positions;
    }


    private ArrayList<ChessPosition> pawnMoves() {
        ArrayList<ChessPosition> positions = new ArrayList<>();
        int direction = 1;
        if (color == ChessGame.TeamColor.BLACK) {
            direction = -1;
        }
        for (int i = -1; i < 2; i++) {
            if (currRow != 1 && currRow != 8) {
                ChessPosition pos = isSafe(currRow - 1 + direction, currCol + i - 1);
                if (pos != null) {
                    ChessPosition ahead = new ChessPosition(currRow + direction, currCol + i);
                    ChessPiece pieceAhead = board.getPiece(ahead);
                    boolean isPromoPiece = currRow + direction == 1 || currRow + direction == 8;
                    if (i == 0 && pieceAhead == null && !isPromoPiece) {
                        positions.add(ahead);
                        if (color == ChessGame.TeamColor.WHITE && currRow == 2 || color == ChessGame.TeamColor.BLACK && currRow == 7) {
                            ahead = new ChessPosition(currRow + direction * 2, currCol);
                            pieceAhead = board.getPiece(ahead);
                            if (pieceAhead == null) {
                                positions.add(ahead);
                            }
                        }
                    }
                    else if (i != 0  && pieceAhead != null && !isPromoPiece) {
                        positions.add(pos);
                    }
                    else if (isPromoPiece){
                        if (i == 0 && pieceAhead == null) {
                            promoPositions.add(pos);
                        }
                        else if (i != 0 && pieceAhead != null) {
                            promoPositions.add(pos);
                        }
                    }
                }
            }
        }
        return positions;
    }


    private ChessPosition isSafe(int row, int col) {
        if (row < 8 && row >= 0 && col < 8 && col >= 0) {
            ChessPosition pos = new ChessPosition(row + 1, col + 1);
            ChessPiece piece = board.getPiece(pos);
            if (piece == null || piece.getTeamColor() != color) {
                return pos;
            }
        }
        return null;
    }


    @Override
    public String toString() {
        return "PieceMoves{" +
                "board=" + board +
                ", currPos=" + currPos +
                ", currRow=" + currRow +
                ", currCol=" + currCol +
                ", color=" + color +
                ", type=" + type +
                ", promoPositions=" + promoPositions +
                ", rulerPaths=" + Arrays.toString(rulerPaths) +
                ", bishopPaths=" + Arrays.toString(bishopPaths) +
                ", rookPaths=" + Arrays.toString(rookPaths) +
                ", knightPaths=" + Arrays.toString(knightPaths) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PieceMoves that = (PieceMoves) o;
        return Objects.equals(board, that.board) && Objects.equals(currPos, that.currPos) && color == that.color && type == that.type && Objects.equals(promoPositions, that.promoPositions) && Objects.deepEquals(rulerPaths, that.rulerPaths) && Objects.deepEquals(bishopPaths, that.bishopPaths) && Objects.deepEquals(rookPaths, that.rookPaths) && Objects.deepEquals(knightPaths, that.knightPaths);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, currPos, color, type, promoPositions, Arrays.deepHashCode(rulerPaths), Arrays.deepHashCode(bishopPaths), Arrays.deepHashCode(rookPaths), Arrays.deepHashCode(knightPaths));
    }
}
