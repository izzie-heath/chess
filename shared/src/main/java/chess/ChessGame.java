package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessGame.TeamColor turn;
    private ChessBoard board;

    public ChessGame() {
        this.turn = TeamColor.WHITE;
        board = new ChessBoard();
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        Collection<ChessMove> pieceMoves = piece.pieceMoves(board, startPosition);
        ArrayList<ChessMove> validMoves = new ArrayList<>();

        for(ChessMove move : pieceMoves){
            ChessBoard boardCopy = copyBoard(board);
            ChessBoard boardOriginal = board;
            boardCopy.addPiece(move.getStartPosition(), null);
            boardCopy.addPiece(move.getEndPosition(), piece);
            setBoard(boardCopy);
            if(! isInCheck(piece.getTeamColor())){
                validMoves.add(move);
            }
            setBoard(boardOriginal);
        }
        return validMoves;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if(board.getPiece(move.getStartPosition()) == null){
            throw new InvalidMoveException("No starting piece found");
        }
        if(board.getPiece(move.getStartPosition()).getTeamColor() != turn){
            throw new InvalidMoveException("Not your turn");
        }
        if(! validMoves(move.getStartPosition()).contains(move)){
            throw new InvalidMoveException("Invalid move");
        }

        if(move.getPromotionPiece() != null){
            board.addPiece(move.getEndPosition(), new ChessPiece(board.getPiece(move.getStartPosition()).getTeamColor(), move.getPromotionPiece()));
        } else {
            board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
        }
        board.addPiece(move.getStartPosition(), null);

        if(board.getPiece(move.getEndPosition()).getTeamColor() == TeamColor.WHITE){
            setTeamTurn(TeamColor.BLACK);
        } else {
            setTeamTurn(TeamColor.WHITE);
        }

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = getTeamsKingPosition(teamColor);
        ArrayList<ChessMove> enemyMoves = new ArrayList<>();
        TeamColor enemyTeam;
        if(teamColor == TeamColor.WHITE){
            enemyTeam = TeamColor.BLACK;
        } else {
            enemyTeam = TeamColor.WHITE;
        }

        for(int r=1; r<=8; r++){
            for(int c=1; c<=8; c++){
                if(board.getPiece(new ChessPosition(r,c)) != null && board.getPiece(new ChessPosition(r,c)).getTeamColor() != teamColor){
                    enemyMoves.addAll(new ChessPiece(enemyTeam, board.getPiece(new ChessPosition(r,c)).getPieceType()).pieceMoves(board, new ChessPosition(r,c)));
                }
            }
        }

        for(ChessMove move : enemyMoves){
            if(move.getEndPosition().equals(kingPosition)){
                return true;
            }
        }

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        ArrayList<ChessMove> teamMoves = new ArrayList<>();

        for(int r=1; r<=8; r++){
            for(int c=1; c<=8; c++){
                if(board.getPiece(new ChessPosition(r,c)) != null && board.getPiece(new ChessPosition(r,c)).getTeamColor() == teamColor){
                    teamMoves.addAll(validMoves(new ChessPosition(r,c)));
                }
            }
        }

        System.out.println(teamMoves);
        if(teamMoves.isEmpty()){
            return true;
        }

        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {

    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    public ChessPosition getTeamsKingPosition(TeamColor teamColor){
        for(int r=1; r<=8; r++){
            for(int c=1; c<=8; c++){
                if(board.getPiece(new ChessPosition(r,c)) != null && board.getPiece(new ChessPosition(r,c)).getTeamColor() == teamColor && board.getPiece(new ChessPosition(r,c)).getPieceType() == ChessPiece.PieceType.KING){
                    return new ChessPosition(r,c);
                }
            }
        }
        return null;
    }

    public ChessBoard copyBoard(ChessBoard board) {
        ChessBoard newBoard = new ChessBoard();
        for (int r=1; r<=8; r++) {
            for (int c=1; c<=8; c++) {
                if (board.getPiece(new ChessPosition(r, c)) != null) {
                    newBoard.addPiece(new ChessPosition(r, c), board.getPiece(new ChessPosition(r, c)));
                }
            }
        }
        return newBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return turn == chessGame.turn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(turn, board);
    }
}
