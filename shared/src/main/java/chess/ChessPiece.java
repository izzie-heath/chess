package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.List;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }
    
    public Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        ArrayList<ChessMove> moves = new ArrayList<>();

        boolean otherPieceFound = false;
        int r = myPosition.getRow() + 1;
        int c = myPosition.getColumn() + 1;
        while (new ChessPosition(r, c).inBounds() && !otherPieceFound) {
            otherPieceFound = isOtherPieceFound(board, myPosition, r, c, moves, piece, otherPieceFound);
            r = r + 1;
            c = c + 1;
        }
        otherPieceFound = false;
        r = myPosition.getRow() + 1;
        c = myPosition.getColumn() - 1;
        while (new ChessPosition(r, c).inBounds() && !otherPieceFound) {
            otherPieceFound = isOtherPieceFound(board, myPosition, r, c, moves, piece, otherPieceFound);
            r = r + 1;
            c = c - 1;
        }
        otherPieceFound = false;
        r = myPosition.getRow() - 1;
        c = myPosition.getColumn() + 1;
        while (new ChessPosition(r, c).inBounds() && !otherPieceFound) {
            otherPieceFound = isOtherPieceFound(board, myPosition, r, c, moves, piece, otherPieceFound);
            r = r - 1;
            c = c + 1;
        }
        otherPieceFound = false;
        r = myPosition.getRow() - 1;
        c = myPosition.getColumn() - 1;
        while (new ChessPosition(r, c).inBounds() && !otherPieceFound) {
            otherPieceFound = isOtherPieceFound(board, myPosition, r, c, moves, piece, otherPieceFound);
            r = r - 1;
            c = c - 1;
        }

        return moves;
    }

    public Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        ArrayList<ChessMove> moves = new ArrayList<>();

        boolean otherPieceFound = false;
        int r = myPosition.getRow() + 1;
        int c = myPosition.getColumn();
        while (new ChessPosition(r, c).inBounds() && !otherPieceFound) {
            otherPieceFound = isOtherPieceFound(board, myPosition, r, c, moves, piece, otherPieceFound);
            r = r + 1;
        }
        otherPieceFound = false;
        r = myPosition.getRow() - 1;
        c = myPosition.getColumn();
        while (new ChessPosition(r, c).inBounds() && !otherPieceFound) {
            otherPieceFound = isOtherPieceFound(board, myPosition, r, c, moves, piece, otherPieceFound);
            r = r - 1;
        }
        otherPieceFound = false;
        r = myPosition.getRow();
        c = myPosition.getColumn() + 1;
        while (new ChessPosition(r, c).inBounds() && !otherPieceFound) {
            otherPieceFound = isOtherPieceFound(board, myPosition, r, c, moves, piece, otherPieceFound);
            c = c + 1;
        }
        otherPieceFound = false;
        r = myPosition.getRow();
        c = myPosition.getColumn() - 1;
        while (new ChessPosition(r, c).inBounds() && !otherPieceFound) {
            otherPieceFound = isOtherPieceFound(board, myPosition, r, c, moves, piece, otherPieceFound);
            r = r;
            c = c - 1;
        }

        return moves;
    }

    public Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        ArrayList<ChessMove> moves = new ArrayList<>();

        moves.addAll(bishopMoves(board, myPosition));
        moves.addAll(rookMoves(board, myPosition));

        return moves;
    }

    public Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        int[][] directions = {{1, 1}, {-1, 1}, {1, -1}, {-1, -1}, {1, 0}, {0, 1}, {-1, 0}, {0, -1}};
        ChessPiece piece = board.getPiece(myPosition);
        ArrayList<ChessMove> moves = new ArrayList<>();
        boolean otherPieceFound = false;

        for (int i = 0; i < directions.length; i++) {
            int r = myPosition.getRow() + directions[i][0];
            int c = myPosition.getColumn() + directions[i][1];
            if(new ChessPosition(r,c).inBounds()){
                otherPieceFound = isOtherPieceFound(board, myPosition, r, c, moves, piece, otherPieceFound);
            }
        }
        return moves;
    }

    public Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        int[][] directions = {{1, 2}, {-1, 2}, {1, -2}, {-1, -2}, {2, 1}, {-2, 1}, {2, -1}, {-2, -1}};
        ChessPiece piece = board.getPiece(myPosition);
        ArrayList<ChessMove> moves = new ArrayList<>();
        boolean otherPieceFound = false;

        for (int i = 0; i < directions.length; i++) {
            int r = myPosition.getRow() + directions[i][0];
            int c = myPosition.getColumn() + directions[i][1];
            if(new ChessPosition(r,c).inBounds()){
                otherPieceFound = isOtherPieceFound(board, myPosition, r, c, moves, piece, otherPieceFound);
            }
        }
        return moves;
    }

    public Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        int r = myPosition.getRow();
        int c = myPosition.getColumn();
        ArrayList<ChessMove> moves = new ArrayList<>();
        boolean otherPieceFound = false;
        int direction = 1;
        int promotionRow = 8;
        int startRow = 2;

        if(piece.pieceColor == ChessGame.TeamColor.BLACK){
            direction = -1;
            promotionRow = 1;
            startRow = 7;
        }

        if(r + direction == promotionRow){
            if(new ChessPosition(r + direction, c).inBounds() && (board.getPiece(new ChessPosition(r + direction,  c)) == null || board.getPiece(new ChessPosition(r + direction,  c)).pieceColor != pieceColor)){
                addPromotionPieces(myPosition, r + direction, c, moves);
            }
            if(new ChessPosition(r + direction, c + 1).inBounds() && board.getPiece(new ChessPosition(r + direction,  c + 1)) != null && board.getPiece(new ChessPosition(r + direction,  c + 1)).pieceColor != pieceColor){
                addPromotionPieces(myPosition, r + direction, c + 1, moves);
                moves.add(new ChessMove(myPosition, new ChessPosition(r + direction, c + 1), ChessPiece.PieceType.BISHOP));
            }
            if(new ChessPosition(r + direction, c - 1).inBounds() && board.getPiece(new ChessPosition(r + direction,  c - 1)) != null && board.getPiece(new ChessPosition(r + direction,  c - 1)).pieceColor != pieceColor){
                addPromotionPieces(myPosition, r + direction, c - 1, moves);
            }
        } else {
            if(new ChessPosition(r + direction, c).inBounds() && board.getPiece(new ChessPosition(r + direction,  c)) == null){
                moves.add(new ChessMove(myPosition, new ChessPosition(r + direction, c), null));
            }
            if(new ChessPosition(r + direction, c + 1).inBounds() && board.getPiece(new ChessPosition(r + direction,  c + 1)) != null && board.getPiece(new ChessPosition(r + direction,  c + 1)).pieceColor != piece.pieceColor){
                moves.add(new ChessMove(myPosition, new ChessPosition(r + direction, c + 1), null));
            }
            if(new ChessPosition(r + direction, c - 1).inBounds() && board.getPiece(new ChessPosition(r + direction,  c - 1)) != null && board.getPiece(new ChessPosition(r + direction,  c - 1)).pieceColor != piece.pieceColor){
                moves.add(new ChessMove(myPosition, new ChessPosition(r + direction, c - 1), null));
            }
        }
        if(r == startRow && board.getPiece(new ChessPosition(r + (direction * 2),  c)) == null && board.getPiece(new ChessPosition(r + direction, c)) == null) {
            moves.add(new ChessMove(myPosition, new ChessPosition(r + (direction * 2), c), null));
        }

        return moves;
    }


    public static boolean isOtherPieceFound(ChessBoard board, ChessPosition myPosition, int r, int c, ArrayList<ChessMove> moves, ChessPiece piece, boolean otherPieceFound) {
        if (board.getPiece(new ChessPosition(r, c)) == null) {
            moves.add(new ChessMove(myPosition, new ChessPosition(r, c), null));
        } else {
            if(board.getPiece(new ChessPosition(r, c)).pieceColor != piece.pieceColor){
                moves.add(new ChessMove(myPosition, new ChessPosition(r, c), null));
            }
            otherPieceFound = true;
        }
        return otherPieceFound;
    }

    public static void addPromotionPieces(ChessPosition myPosition, int r, int c, ArrayList<ChessMove> moves){
        moves.add(new ChessMove(myPosition, new ChessPosition(r, c), ChessPiece.PieceType.BISHOP));
        moves.add(new ChessMove(myPosition, new ChessPosition(r, c), ChessPiece.PieceType.ROOK));
        moves.add(new ChessMove(myPosition, new ChessPosition(r, c), ChessPiece.PieceType.QUEEN));
        moves.add(new ChessMove(myPosition, new ChessPosition(r, c), ChessPiece.PieceType.KNIGHT));
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        if(piece.getPieceType() == PieceType.BISHOP) {
            return bishopMoves(board, myPosition);
        }
        if(piece.getPieceType() ==  PieceType.ROOK) {
            return rookMoves(board, myPosition);
        }
        if(piece.getPieceType() == PieceType.QUEEN){
            return queenMoves(board, myPosition);
        }
        if(piece.getPieceType() == PieceType.KING){
            return kingMoves(board, myPosition);
        }
        if(piece.getPieceType() == PieceType.KNIGHT){
            return knightMoves(board, myPosition);
        }
        if(piece.getPieceType() == PieceType.PAWN){
            return pawnMoves(board, myPosition);
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
