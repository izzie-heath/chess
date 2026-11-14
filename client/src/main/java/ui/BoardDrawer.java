package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static ui.EscapeSequences.*;

public class BoardDrawer {
    public static String draw(ChessBoard board, ChessGame.TeamColor perspective){
        StringBuilder output = new StringBuilder();
        if(perspective == ChessGame.TeamColor.WHITE){
            String columns = "abcdefgh";
            String paddedColumns = "  ";
            for(int s=0; s<columns.length(); s++){
                paddedColumns += " " + columns.charAt(s) + " ";
            }
            output.append(paddedColumns).append("\n");
            for(int r=8; r>=1; r--){
                output.append(RESET_TEXT_COLOR);
                output.append(r).append(" ");
                for(int c=1; c<=8; c++){
                    buildBoard(board, r, c, output);
                }
                output.append(RESET_BG_COLOR);
                output.append(RESET_TEXT_COLOR);
                output.append(" ").append(r);
                output.append("\n");
            }
            output.append(paddedColumns).append("\n");
        } else{
            String columns = "hgfedcba";
            String paddedColumns = "  ";
            for(int s=0; s<columns.length(); s++){
                paddedColumns += " " + columns.charAt(s) + " ";
            }
            output.append(paddedColumns).append("\n");
            for(int r=1; r<=8; r++){
                output.append(RESET_TEXT_COLOR);
                output.append(r).append(" ");
                for(int c=8; c>=1; c--){
                    buildBoard(board, r, c, output);
                }
                output.append(RESET_BG_COLOR);
                output.append(RESET_TEXT_COLOR);
                output.append(" ").append(r);
                output.append("\n");
            }
            output.append(paddedColumns).append("\n");

        }
        return output.toString();
    }

    private static void buildBoard(ChessBoard board, int r, int c, StringBuilder output) {
        if((r + c) % 2 == 1){
            output.append(SET_BG_COLOR_MAGENTA);
        } else{
            output.append(SET_BG_COLOR_BLACK);
        }
        if(board.getPiece(new ChessPosition(r, c)) == null){
            output.append(EMPTY);
        } else{
            output.append(getPiece(board.getPiece(new ChessPosition(r, c)), output));
        }
    }

    public static String getPiece(ChessPiece piece, StringBuilder output){
        boolean isWhite = false;
        String str = "";
        if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            isWhite = true;
            output.append(SET_TEXT_COLOR_RED);
        } else {
            output.append(SET_TEXT_COLOR_BLUE);
        }
        if(piece.getPieceType() == ChessPiece.PieceType.KING){
            return isWhite ? WHITE_KING : BLACK_KING;
        }
        if(piece.getPieceType() == ChessPiece.PieceType.QUEEN){
            return isWhite ? WHITE_QUEEN : BLACK_QUEEN;
        }
        if(piece.getPieceType() == ChessPiece.PieceType.BISHOP){
            return isWhite ? WHITE_BISHOP : BLACK_BISHOP;
        }
        if(piece.getPieceType() == ChessPiece.PieceType.KNIGHT){
            return isWhite ? WHITE_KNIGHT : BLACK_KNIGHT;
        }
        if(piece.getPieceType() == ChessPiece.PieceType.ROOK){
            return isWhite ? WHITE_ROOK : BLACK_ROOK;
        }
        if(piece.getPieceType() == ChessPiece.PieceType.PAWN){
            return isWhite ? WHITE_PAWN : BLACK_PAWN;
        }
        return null;
    }

    static void main() {
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        System.out.println("WHITE PERSPECTIVE:");
        System.out.print(BoardDrawer.draw(board, ChessGame.TeamColor.WHITE));
        System.out.println();
        System.out.println("BLACK PERSPECTIVE:");
        System.out.print(BoardDrawer.draw(board, ChessGame.TeamColor.BLACK));
    }
}

