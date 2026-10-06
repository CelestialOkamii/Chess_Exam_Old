package chess;
import java.util.*;


/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    ChessPiece[][] board =  new ChessPiece[8][8];


    public ChessBoard() {
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        board = new ChessPiece[8][8];
        ArrayList<Integer> rows = new ArrayList<>(Arrays.asList(1,2,7,8));
        ArrayList<ChessPiece.PieceType> specialty = new ArrayList<>(Arrays.asList(ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.KING, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK));
        ArrayList<ChessPiece.PieceType> pawns = new ArrayList<>(Arrays.asList(ChessPiece.PieceType.PAWN, ChessPiece.PieceType.PAWN, ChessPiece.PieceType.PAWN, ChessPiece.PieceType.PAWN, ChessPiece.PieceType.PAWN, ChessPiece.PieceType.PAWN, ChessPiece.PieceType.PAWN, ChessPiece.PieceType.PAWN));
        for (int row : rows) {
            ChessGame.TeamColor color = ChessGame.TeamColor.WHITE;
            if (row == 1 || row == 8) {
                ArrayList<ChessPosition> positions = getPositions(row);
                if (row == 8) {
                    color = ChessGame.TeamColor.BLACK;
                }
                for (int i = 0; i < 8 ; i++) {
                    ChessPiece piece = new ChessPiece(color, specialty.get(i));
                    addPiece(positions.get(i), piece);
                }
            }
            else {
                ArrayList<ChessPosition> positions = getPositions(row);
                if (row == 7) {
                    color = ChessGame.TeamColor.BLACK;
                }
                for (int i = 0; i < 8 ; i++) {
                    ChessPiece piece = new ChessPiece(color, pawns.get(i));
                    addPiece(positions.get(i), piece);
                }
            }
        }
    }


    private ArrayList<ChessPosition> getPositions(int row) {
        ArrayList<ChessPosition> positions = new ArrayList<>();
        for (int col = 1; col < 9; col++) {
            ChessPosition position = new ChessPosition(row, col);
            positions.add(position);
        }
        return positions;
    }


    @Override
    public String toString() {
        return "ChessBoard{" +
                "board=" + Arrays.toString(board) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
}
